package RMI;

import domain.Sale;
import java.rmi.RemoteException;



public class ISalesAggregationCallbackImpl implements ISalesAggregationCallback {

public ISalesAggregationCallbackImpl() throws RemoteException {

}
	@Override
	public void newSale(Sale sale) throws RemoteException {
	}

}
