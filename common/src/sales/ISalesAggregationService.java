package sales;

import sales.domain.Sale;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Mark George <mark.george@otago.ac.nz>
 */
public interface ISalesAggregationService extends Remote {

	void newSale(Sale sale) throws RemoteException;

}
