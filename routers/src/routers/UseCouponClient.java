package routers;

/**
 *
 * @author jiahu599
 */
import com.google.gson.Gson;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

/**
 *
 * @author jiahu599
 */
public class UseCouponClient {
public static final String sale="";
	public static void main(String[] args) {

		// create default context
		CamelContext camel = new DefaultCamelContext();

		// register ActiveMQ as the JMS handler
		ActiveMQConnectionFactory activeMqFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
		camel.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(activeMqFactory));

// trust all classes being used to send serialised domain objects
		activeMqFactory.setTrustAllPackages(true);
		// create message producer
		ProducerTemplate producer = camel.createProducerTemplate();
		Gson gson = new Gson();

//        String jsonSale = gson.toJson(sale);
		// send message
		producer.sendBody("jms:queue:vend-email", sale);
		System.out.println("Sent JSON to queue vend-email");
	}
}
