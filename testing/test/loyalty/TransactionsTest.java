package loyalty;

import java.util.Collection;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import org.junit.After;
import org.junit.Before;
import loyalty.domain.Customer;
import loyalty.domain.Transaction;
import org.glassfish.jersey.client.ClientConfig;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 * @author Mark George <mark.george@otago.ac.nz>
 */
public class TransactionsTest {

	Customer testCustomer = new Customer("test");
	Transaction t1 = new Transaction("t1", "testing shop", 75);
	Transaction t2 = new Transaction("t2", "testing shop", 50);

	WebTarget customerTarget;
	WebTarget transactionsTarget;

	@Before
	public void setUp() {
		Client client = ClientBuilder.newClient(new ClientConfig());

		customerTarget = client.target("http://localhost:8081/customers/");
		transactionsTarget = client.target("http://localhost:8081/customers/test/transactions");

		customerTarget.request().post(Entity.entity(testCustomer, "text/xml"));

		transactionsTarget.request().post(Entity.entity(t1, "text/xml"));
		transactionsTarget.request().post(Entity.entity(t2, "application/json"));
	}

	@After
	public void tearDown() {
		customerTarget.path("test").request().delete();
	}

	@Test
	public void testGetAll() {
		Collection<Transaction> jsonCollection = transactionsTarget.request("application/json").get(new GenericType<Collection<Transaction>>() {});
		assertThat(jsonCollection, hasItems(t1, t2));

		Collection<Transaction> xmlCollection = transactionsTarget.request("text/xml").get(new GenericType<Collection<Transaction>>() {});
		assertThat(xmlCollection, hasItems(t1, t2));
	}

	@Test
	public void testGetOne() {
		Transaction t1Json = transactionsTarget.path(t1.getId()).request("application/json").get(Transaction.class);
		assertThat(t1Json, equalTo(t1));

		Transaction t1Xml = transactionsTarget.path(t1.getId()).request("text/xml").get(Transaction.class);
		assertThat(t1Xml, equalTo(t1));
	}

	@Test
	public void testDelete() {
		transactionsTarget.path(t1.getId()).request().delete();
		Response response = transactionsTarget.path(t1.getId()).request().get();

		assertThat(response.getStatus(), equalTo(404));
	}

}
