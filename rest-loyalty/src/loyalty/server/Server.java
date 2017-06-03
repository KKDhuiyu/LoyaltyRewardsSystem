/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loyalty.server;

import filters.CorsFilter;
import filters.DebugFilter;
import filters.ExceptionLogger;
import filters.ExceptionMessageHandler;
import java.net.URI;
import loyalty.resource.CouponResource;
import loyalty.resource.CouponsResource;
import loyalty.resource.CustomerResource;
import loyalty.resource.PointsResource;
import loyalty.resource.TransactionResource;
import loyalty.resource.TransactionsResource;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.bridge.SLF4JBridgeHandler;

/**
 *
 * @author geoma48p
 */
public class Server {

	public static void main(String[] args) throws Exception {

		// configure the unified logger
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();

		// create a web service configuration
		ResourceConfig config = new ResourceConfig();

		// add a debug filter that prints request/response details
		config.register(DebugFilter.class);

		// add a more functional exception handling and logging
		config.register(ExceptionMessageHandler.class);
		config.register(ExceptionLogger.class);

		// add CORS filter
		config.register(CorsFilter.class);
		
		// add our resource classes
		config.register(CustomerResource.class);
		config.register(TransactionsResource.class);
		config.register(TransactionResource.class);
		config.register(CouponsResource.class);
		config.register(CouponResource.class);
		config.register(PointsResource.class);

		// define the URI that the server will use
		URI baseUri = new URI("http://localhost:8081/");

		// start the server (via the built−in Java HTTP server)
		JdkHttpServerFactory.createHttpServer(baseUri, config);
		System.out.println("Loyalty Service Ready on " + baseUri);

	}

}
