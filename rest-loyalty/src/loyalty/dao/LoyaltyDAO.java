package loyalty.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import loyalty.domain.Coupon;
import loyalty.domain.Customer;

/**
 * @author Mark George <mark.george@otago.ac.nz>
 */
public class LoyaltyDAO {

	private static Map<String, Customer> customers;

	public LoyaltyDAO() {

		if (customers == null) {

			customers = new HashMap<>();

			// these are valid Vend customer details
			Customer boris = new Customer("06bf537b-c7d7-11e7-ff13-2d957f9ff0f0");
			Customer doris = new Customer("06bf537b-c7d7-11e7-ff13-2d958c8879b7");
			Customer morris = new Customer("0af7b240-abd7-11e7-eddc-3923f166ef24");
	
			customers.put(boris.getId(), boris);
			customers.put(doris.getId(), doris);
			customers.put(morris.getId(), morris);
		}

	}

	public Collection<Customer> getCustomers() {
		return customers.values();
	}

	public Customer getCustomer(String customerId) {
		return customers.get(customerId);
	}

	public void addCustomer(Customer customer) {
		customers.put(customer.getId(), customer);
	}

	public void removeCustomer(Customer customer) {
		customers.remove(customer.getId());
	}

}
