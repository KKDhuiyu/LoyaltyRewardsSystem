package loyalty;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import loyalty.domain.Coupon;
import loyalty.domain.Customer;
import loyalty.domain.Transaction;
import org.glassfish.jersey.client.ClientConfig;
import static org.hamcrest.CoreMatchers.is;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Mark George <mark.george@otago.ac.nz>
 */
public class PointsTest {

	Customer testCustomer = new Customer("test");
	Transaction t1 = new Transaction("t1", "testing shop", 75);
	Transaction t2 = new Transaction("t2", "testing shop", 50);

	Coupon c1 = new Coupon(25);
	Coupon c2 = new Coupon(30);

	WebTarget customerTarget;
	WebTarget pointsTarget;

	@Before
	public void setUp() {
		Client client = ClientBuilder.newClient(new ClientConfig());

		customerTarget = client.target("http://localhost:8081/customers/");
		pointsTarget = client.target("http://localhost:8081/customers/test/points");
		
		WebTarget couponsTarget = client.target("http://localhost:8081/customers/test/coupons");
		WebTarget transactionsTarget = client.target("http://localhost:8081/customers/test/transactions");

		customerTarget.request().post(Entity.entity(testCustomer, "text/xml"));
		transactionsTarget.request().post(Entity.entity(t1, "text/xml"));
		transactionsTarget.request().post(Entity.entity(t2, "application/json"));
		couponsTarget.request().post(Entity.entity(c1, "text/xml"));
		couponsTarget.request().post(Entity.entity(c2, "application/json"));
	}

	@After
	public void tearDown() {
		customerTarget.path("test").request().delete();
	}

	@Test
	public void testTotal() {
		Integer result = pointsTarget.path("total").request("text/plain").get(Integer.class);
		assertThat(result, is(75 + 50));
	}

	@Test
	public void testUnused() {
		Integer result = pointsTarget.path("unused").request("text/plain").get(Integer.class);
		assertThat(result, is(75 + 50 - 25 - 30));
	}
	
}
