package RMI;

import domain.Sale;
import java.rmi.Remote;
import java.rmi.RemoteException;



public interface ISalesAggregationCallback extends Remote {
	
	void newSale(Sale sale) throws RemoteException;
	
}
