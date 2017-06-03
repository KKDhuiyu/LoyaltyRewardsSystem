package routers;

import java.util.Map;
import loyalty.domain.Coupon;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import loyalty.domain.Transaction;
import sales.domain.Sale;
import sales.domain.SaleItem;

/**
 *
 * @author jiahu599
 */
public class UseCouponRouter {

	public static void main(String[] args) throws Exception {
		// create default context
		CamelContext camel = new DefaultCamelContext();

// register ActiveMQ as the JMS handler
		ActiveMQConnectionFactory activeMqFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
		camel.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(activeMqFactory));

// trust all classes being used to send serialised domain objects
		activeMqFactory.setTrustAllPackages(true);

		// create and add the builder(s)
		camel.addRoutes(new RouteBuilder() {
			@Override
			public void configure() {

//get sale
				from("imaps://outlook.office365.com?username=jiahu599@student.otago.ac.nz"
						  + "&password=" + getPassword("Enter your E-Mail password")
						  + "&searchTerm.subject=Vend:SaleUpdate")// 1
						  .convertBodyTo(String.class)
						  .to("jms:queue:vend-email");

				from("jms:queue:vend-email")
						  .setHeader("customer_id").jsonpath("$.customer.id")
						  .log("Customer ID: ${headers.customer_id}")
						  .log("trying to unmarshal json")
						  .unmarshal().json(JsonLibrary.Gson, Sale.class)
						  .log("successfully unmarshalled")
						  .to("jms:queue:sale");

				// queue contains objects that have a theCollection field
				from("jms:queue:sale")
						  // perform the split
						  .split()
						  // identify the field that holds the collection
						  .simple("${body}")
						  .log("${body}")
						  // send the split messages to next endpoint
						  .to("jms:queue:sales-items")
						  .log("sent to split products");
/////////////////////////////////////////////////////////// for each product
				from("jms:queue:sales-items")
						  .marshal().json(JsonLibrary.Gson)
						  .convertBodyTo(String.class)
						  //set header
						  .setHeader("productId").jsonpath("$.register_sale_products[0].product_id")
						  .log(" ${headers.productId}")
						  .unmarshal().json(JsonLibrary.Gson, SaleItem.class)
						  .log("convert to SaleItem message again")
						  .to("jms:queue:send-to-vend");

				from("jms:queue:send-to-vend")
						  .setProperty("productid").header("productId")
						  .removeHeaders("*", "customer_id", "productId")
						  .log("Headers removed")
						  // remove message body since you can't send a body in a GET
						  .setBody(constant(null))
						  .log("Body set to null")
						  .setHeader("Authorization", constant("Bearer CjOC4V9CKp10w3EkgLNtR:um8xRZhhaZpRNUXULT"))
						  .log("Authorization header set")
						  .setHeader(Exchange.HTTP_METHOD, constant("GET"))
						  .recipientList().simple("https4://info323otago.vendhq.com/api/products/${exchangeProperty.productid}")
						  .setHeader(Exchange.HTTP_METHOD, constant("GET"))
						  .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
						  //.log("got product from vend: {${body}")
						  .to("jms:queue:product-json")
						  .log("sent to product-json");

				from("jms:queue:product-json")
						  .convertBodyTo(String.class)
						  .setHeader("coupon_id").jsonpath("$.products[0].handle")
						  .log("Coupon ID: ${headers.coupon_id}").log(" Customer ID: ${headers.customer_id}")
						  .setProperty("coupon_id").simple("${headers.coupon_id}")
						  .setProperty("customer").simple("${headers.customer_id}")
						  .removeHeaders("*", "coupon_id", "customer_id", "productId")
						  .setBody(constant(null))
						  .setHeader(Exchange.HTTP_METHOD, constant("GET"))
						 							 .recipientList(simple("http4://localhost:8081/customers/${exchangeProperty.customer}/coupons/${exchangeProperty.coupon_id}"))
			//			  .recipientList(simple("http4://localhost:8081/customers/06bf537b-c7d7-11e7-ff13-2d957f9ff0f0/coupons/1495374977045"))
						  .to("jms:queue:coupon");
				from("jms:queue:coupon")
						  .unmarshal().jaxb("loyalty.domain")
						  //						  .marshal().json(JsonLibrary.Gson)
						  //						  .unmarshal().json(JsonLibrary.Gson, Coupon.class)
						  .log("got coupon from the RESTful: ${body}")
						  .to("language:simple:${body.setUsed(true)}?transform=false")
						  .log("Set used property to true.")
						  //						  .convertBodyTo(String.class)

						  .log("Coupon ID: ${headers.coupon_id}").log(" Customer ID: ${headers.customer_id}")
						  .to("jms:queue:coupon-put")
						  .log("sent to coupon-put");
				from("jms:queue:coupon-put")
						  .setProperty("coupon_id").simple("${headers.coupon_id}")
						  .setProperty("customer").simple("${headers.customer_id}")
						  .removeHeaders("*", "productId")
						  .setHeader(Exchange.HTTP_METHOD, constant("PUT"))
						  .setHeader(Exchange.CONTENT_TYPE, constant("text/xml"))
						  // .convertBodyTo(String.class)

						  .log("Put coupon: ${body}")
										 .recipientList(simple("http4://localhost:8081/customers/${exchangeProperty.customer}/coupons/${exchangeProperty.coupon_id}"))
//						  .recipientList(simple("http4://localhost:8081/customers/06bf537b-c7d7-11e7-ff13-2d957f9ff0f0/coupons/1495374977045"))
						  .to("jms:queue:coupon-delete").log("sent to coupon delete");  // HTTP response ends up in this queue

				//Delete
				from("jms:queue:coupon-delete")
						  .log("deleting product : ${headers.productId} from the vend")
						  .setProperty("product_id").simple("${headers.productId}")
						  .setHeader("Authorization", constant("Bearer CjOC4V9CKp10w3EkgLNtR:um8xRZhhaZpRNUXULT"))
						  .removeHeaders("*", "Authorization")
						  .setBody(constant(null))
						  .setHeader(Exchange.HTTP_METHOD, constant("DELETE"))
						  .recipientList(simple("https://info323otago.vendhq.com/api/products/${exchangeProperty.product_id}"))
						  .log("product Deleted");
			}
		});

// turn exchange tracing on or off (false is off)
		camel.setTracing(false);

// enable stream caching so that things like loggers don't consume the messages
		camel.setStreamCaching(true);
// start routing
		System.out.println("Starting router...");
		camel.start();
		System.out.println("... ready.");
	}

	public static String getPassword(String prompt) {
		JPasswordField txtPasswd = new JPasswordField();
		int resp = JOptionPane.showConfirmDialog(null, txtPasswd, prompt,
				  JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (resp == JOptionPane.OK_OPTION) {
			String password = new String(txtPasswd.getPassword());
			return password;
		}
		return null;
	}

	public static Coupon setToUsed(long id, int points) {
		return new Coupon(id, points);
	}

}
