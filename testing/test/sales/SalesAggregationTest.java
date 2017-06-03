package sales;

import sales.domain.Customer;
import sales.domain.Sale;
import sales.domain.SaleItem;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Date;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Mark George <mark.george@otago.ac.nz>
 */
public class SalesAggregationTest {

	ISalesAggregationService service;
	Sale sale;

	@Before
	public void setUp() throws RemoteException, NotBoundException {

		this.service = (ISalesAggregationService) LocateRegistry.getRegistry("localhost", 1099).lookup("sales");

		Customer customer = new Customer();
		customer.setDateOfBirth("13/3/1983");
		customer.setGender('M');

		SaleItem item1 = new SaleItem();
		item1.setPrice(3.12);
		item1.setProductId("12345678");
		item1.setQuantity(3.0);

		SaleItem item2 = new SaleItem();
		item2.setPrice(41.22);
		item2.setProductId("98765432");
		item2.setQuantity(2.7);

		this.sale = new Sale();
		sale.addItem(item1);
		sale.addItem(item2);
		sale.setCustomer(customer);
		sale.setDate(new Date().toString());

	}

	@Test
	public void testSalesService() throws RemoteException {
		service.newSale(sale);
		assertTrue(true);
	}

}
