package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import sales.SalesAggregationImpl;
import sales.ISalesAggregationService;

/**
 * @author Mark George <mark.george@otago.ac.nz>
 */
public class Server {

	public static void main(String[] args) throws RemoteException {

		// create servant instance
		ISalesAggregationService service = new SalesAggregationImpl();

		// get a reference to RMI registry
		Registry registry = LocateRegistry.createRegistry(1099);

		// put servant reference into registry
		registry.rebind("sales", service);

		System.out.println("Sales Aggregation Service Running.");
	}

}
