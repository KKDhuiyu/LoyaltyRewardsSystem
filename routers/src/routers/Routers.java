package routers;

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

/**
 *
 * @author jiahu599
 */
public class Routers {

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

				from("websocket://localhost:9091/email")
						  .setProperty("email").simple("${body.trim()}")
						  .removeHeaders("*")
						  .log("Headers removed")
						  // remove message body since you can't send a body in a GET
						  .setBody(constant(null))
						  .log("Body set to null")
						  // add authentication token to authorization header
						  .setHeader("Authorization", constant("Bearer CjOC4V9CKp10w3EkgLNtR:um8xRZhhaZpRNUXULT"))
						  .log("Authorization header set")
						  // set HTTP method
						  .setHeader(Exchange.HTTP_METHOD, constant("GET"))
						  .log("GET ${exchangeProperty.email}")
						  .recipientList().simple("https4://info323otago.vendhq.com/api/customers?email=${exchangeProperty.email}")
						  .to("jms:queue:vend-customer")
						  .log("sent to vend-customer");

				from("jms:queue:vend-customer")
						  //convert body to string
						  .convertBodyTo(String.class)
						  .to("websocket://localhost:9091/email?sendToAll=true")
						  .log("customer sent via websocket").log("${body.trim()}");

				// create coupon on REST
				from("websocket://localhost:9091/coupon")
						  .removeHeaders("*")
						  .setHeader("customer_id").jsonpath("$.customerID")
						  .setHeader("points").jsonpath("$.points")
						  .setHeader("used").jsonpath("$.used")
						  .log("Coupon: ${headers.customer_id},${headers.points},${headers.used}")
						  .to("jms:queue:post-coupon")
						  .log("sent to post-coupon");

				from("jms:queue:post-coupon")
						  .bean(Coupon.class, "createCoupon(${headers.points})")
						  .to("jms:queue:create-coupon-object")
						  .log("sent tocreate-coupon-object");

				from("jms:queue:create-coupon-object")
						  .setProperty("customer_id").header("customer_id")
						  .removeHeaders("*")
						  .log("header removed")
						  .setHeader(Exchange.HTTP_METHOD, constant("POST"))
						  .setHeader(Exchange.CONTENT_TYPE, constant("text/xml"))
						  .recipientList(simple("http4://localhost:8081/customers/${exchangeProperty.customer_id}/coupons"))
						  .log("posted cupon to RESTful customer")
						  .to("jms:queue:create-coupon-response");

				//products
				from("websocket://localhost:9091/products")
						  .setProperty("product").simple("${body}")
						  // remove headers so they don't get sent to Vend
						  .removeHeaders("*")
						  // add authentication token to authorization header
						  .setHeader("Authorization", constant("Bearer CjOC4V9CKp10w3EkgLNtR:um8xRZhhaZpRNUXULT"))
						  .setHeader(Exchange.CONTENT_TYPE).constant("application/json")
						  // set HTTP method
						  .setHeader(Exchange.HTTP_METHOD, constant("POST"))
						  // send it
						  .recipientList().simple("https4://info323otago.vendhq.com/api/products")
						  // store the response
						  .to("jms:queue:vend-response");

				from("jms:queue:vend-response")
						  //convert body to string
						  .convertBodyTo(String.class)
						  .to("websocket://localhost:9091/products?sendToAll=true")
						  .log("customer sent via websocket").log("${body.trim()}");

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
}
