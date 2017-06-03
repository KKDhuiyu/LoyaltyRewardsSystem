package sales;

import sales.domain.Sale;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author Mark George <mark.george@otago.ac.nz>
 */
public class SalesAggregationImpl extends UnicastRemoteObject implements ISalesAggregationService {

	public SalesAggregationImpl() throws RemoteException {
	}

	@Override
	public void newSale(Sale sale) throws RemoteException {
		System.out.println("New Sale: " + sale);
	}

}
