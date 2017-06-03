
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import routers.MakePurchaseBuilder;
import routers.MakePurchaseRouter;
import sales.domain.*;

/**
 *
 * @author jiahu599
 */
public class RoutersTest extends CamelTestSupport {

	private final Sale saleSoap;
	private final Sale saleRmi;

	public RoutersTest() {
		saleSoap=new Sale();
		saleRmi= new Sale();
	}


	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}

	@Override
	protected CamelContext createCamelContext() throws Exception {
		CamelContext camel = super.createCamelContext();
// replace JMS with direct
		camel.addComponent("jms", camel.getComponent("direct"));
		return camel;
	}

	private RouteBuilder createInterceptRoutes() {
		return new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				interceptSendToEndpoint("cxf://*")
						  .skipSendToOriginalEndpoint()
						  .log("Mock SOAP called")
						  .to("mock:soap");
				interceptSendToEndpoint("rmi://*")
						  .skipSendToOriginalEndpoint()
						  .log("Mock RMI called")
						  .to("mock:rmi");
				interceptSendToEndpoint("http4://*")
						  .skipSendToOriginalEndpoint()
						  .log("Mock REST called")
						  .to("mock:rest");
			}
		};
	}

	@Override
	protected RouteBuilder createRouteBuilder() throws Exception {
// create actual route builder
		RouteBuilder routeBuilder = new MakePurchaseBuilder();
// include the route builder for the intercepts
		routeBuilder.includeRoutes(createInterceptRoutes());
		return routeBuilder;
	}
}
