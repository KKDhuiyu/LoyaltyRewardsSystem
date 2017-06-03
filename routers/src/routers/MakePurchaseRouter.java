package routers;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import sales.domain.Sale;
import loyalty.domain.Transaction;

/**
 *
 * @author jiahu599
 */
public class MakePurchaseRouter {

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

				//from webhook message to vend-email queue
				from("imaps://outlook.office365.com?username=jiahu599@student.otago.ac.nz"
						  + "&password=" + getPassword("Enter your E-Mail password")
						  + "&searchTerm.subject=Vend:SaleUpdate")// 1
						  .convertBodyTo(String.class)
						  .log("Found new E-Mail: ${body}")
						  .multicast()
						  .to("jms:queue:vend-email");
				// 2
				//from vend-email multicast to new-sale and new-transaction
				from("jms:queue:vend-email")
						  .convertBodyTo(String.class)
						  .setHeader("saleId").jsonpath("$.id")
						  .setHeader("customerId").jsonpath("$.customer_id")
						  .setHeader("totalPrice").jsonpath("$.totals.total_price")
						  .log("Total price is: ${headers.totalPrice}")
						  .multicast()
						  .to("jms:queue:new-sale", "jms:queue:new-transaction");
				// 3 4
				//from new-sale unmarshal to sale domain object
				//call new sale on sales aggregation service
				from("jms:queue:new-sale")
						  .log("trying to unmarshal json")
						  .unmarshal().json(JsonLibrary.Gson, Sale.class)
						  .log("successfully unmarshalled")
						  .to("rmi://localhost:1099/sales?remoteInterfaces=sales.ISalesAggregationService&method=newSale")
						  .log("newSale called on RMI");

				//from the new transaction caculateloyal points from total price and add to headers
				from("jms:queue:new-transaction")
						  .setHeader("points").method(MakePurchaseRouter.class, "getPoints(${headers.totalPrice})")
						  .to("jms:queue:calculated-points");

				//from calculated points convert into a transaction.
				// send transaction 
				from("jms:queue:calculated-points")
						  .log("Transaction ID = ${headers.saleId}")
						  .bean(Transaction.class, "newTransaction(${headers.saleId}, ${headers.points})")
						  .log("Points in header = ${headers.points}")
						  .to("jms:queue:send-transaction");

				//from send-transaction marshal to JSON
				from("jms:queue:send-transaction")
						  .setProperty("customerId").header("customerId")
						  // copycustomerID header to exchange property
						  .setProperty("pointsGained").header("points")
						  .removeHeaders("*") // remove all headers
						  // addhearders
						  .setHeader(Exchange.HTTP_METHOD, constant("POST"))
						  .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
						  .marshal().json(JsonLibrary.Gson)
						  .recipientList().simple("http4://localhost:8081/customers/${exchangeProperty.customerId}/transactions");
// send to service
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

	public Integer getPoints(Double totalPrice) {
		return (totalPrice.intValue()) / 10;
	}
}
