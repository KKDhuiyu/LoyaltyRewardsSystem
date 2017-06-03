package routers;

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
public class MakePurchaseClient {

	private static final String sale = "{\"id\":\"06bf537b-c7d7-11e7-ff13-2d957f9ff0f0\",\"source\":\"USER\",\"source_id\":null,\"sale_date\":\"2017-05-15T04:08:17Z\",\"status\":\"CLOSED\",\"user_id\":\"06bf537b-c7d7-11e7-ff13-2ccf2991b7f4\",\"customer_id\":\"0af7b240-abd7-11e7-eddc-3923f166ef24\",\"register_id\":\"06bf537b-c7d7-11e7-ff13-2cc2fcd2ba8c\",\"market_id\":\"1\",\"invoice_number\":\"2\",\"short_code\":\"al4psu\",\"totals\":{\"total_price\":\"173.91304\",\"total_loyalty\":\"0.00000\",\"total_tax\":\"26.08696\",\"total_payment\":\"200.00000\",\"total_to_pay\":\"0.00000\"},\"note\":\"\",\"updated_at\":\"2017-05-15T04:08:22+00:00\",\"created_at\":\"2017-05-15 04:08:22\",\"customer\":{\"id\":\"0af7b240-abd7-11e7-eddc-3923f166ef24\",\"customer_code\":\"1234\",\"customer_group_id\":\"06bf537b-c77f-11e7-ff13-0c871e85cb6d\",\"first_name\":\"jiahu599\",\"last_name\":\"\",\"company_name\":\"\",\"email\":\"\",\"phone\":\"\",\"mobile\":\"\",\"fax\":\"\",\"balance\":\"0.000\",\"loyalty_balance\":\"0.00000\",\"enable_loyalty\":true,\"points\":0,\"note\":\"\",\"year_to_date\":\"0.00000\",\"sex\":\"M\",\"date_of_birth\":null,\"custom_field_1\":\"\",\"custom_field_2\":\"\",\"custom_field_3\":\"\",\"custom_field_4\":\"\",\"updated_at\":\"2017-05-15 04:06:59\",\"created_at\":\"2017-05-15 04:06:59\",\"deleted_at\":null,\"contact_first_name\":\"jiahu599\",\"contact_last_name\":\"\"},\"user\":{\"id\":\"06bf537b-c7d7-11e7-ff13-2ccf2991b7f4\",\"name\":\"jiahu599\",\"display_name\":\"Huiyu Jia\",\"email\":\"jiahu599@student.otago.ac.nz\",\"outlet_id\":null,\"target_daily\":null,\"target_weekly\":null,\"target_monthly\":null,\"created_at\":\"2017-04-29 11:29:52\",\"updated_at\":\"2017-04-29 11:29:52\"},\"register_sale_products\":[{\"id\":\"73f3b351-5853-b207-11e7-3924105803b6\",\"product_id\":\"06bf537b-c7d7-11e7-ff13-0c871ec9808b\",\"quantity\":1,\"price\":\"173.91304\",\"discount\":\"0.00000\",\"loyalty_value\":\"0.00000\",\"price_set\":false,\"tax\":\"26.08696\",\"price_total\":\"173.91304\",\"tax_total\":\"26.08696\",\"tax_id\":\"06bf537b-c77f-11e7-ff13-0c871e89e399\"}],\"register_sale_payments\":[{\"id\":\"73f3b351-5853-b207-11e7-39241fd6b077\",\"payment_date\":\"2017-05-15T04:08:17Z\",\"amount\":200,\"retailer_payment_type_id\":\"06bf537b-c77f-11e7-ff13-0c871e96c444\",\"payment_type_id\":3,\"retailer_payment_type\":{\"id\":\"06bf537b-c77f-11e7-ff13-0c871e96c444\",\"name\":\"Credit Card\",\"payment_type_id\":\"3\",\"config\":null},\"payment_type\":{\"id\":\"3\",\"name\":\"Credit Card\",\"has_native_support\":false},\"register_sale\":{\"id\":\"73f3b351-5853-b207-11e7-39240e40bc73\",\"source\":\"USER\",\"source_id\":null,\"sale_date\":\"2017-05-15T04:08:17Z\",\"status\":\"CLOSED\",\"user_id\":\"06bf537b-c7d7-11e7-ff13-2ccf2991b7f4\",\"customer_id\":\"0af7b240-abd7-11e7-eddc-3923f166ef24\",\"register_id\":\"06bf537b-c7d7-11e7-ff13-2cc2fcd2ba8c\",\"market_id\":\"1\",\"invoice_number\":\"2\",\"short_code\":\"al4psu\",\"totals\":{\"total_price\":\"173.91304\",\"total_loyalty\":\"0.00000\",\"total_tax\":\"26.08696\",\"total_payment\":\"200.00000\",\"total_to_pay\":\"0.00000\"},\"note\":\"\",\"updated_at\":\"2017-05-15T04:08:22+00:00\",\"created_at\":\"2017-05-15 04:08:22\"}}],\"taxes\":[{\"id\":\"1e929694-0c87-11e7-bf13-06bf537bc77f\",\"name\":\"GST\",\"rate\":\"0.15000\",\"tax\":26.08696}]}";

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
