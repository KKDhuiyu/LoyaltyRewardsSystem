package loyalty;

import java.util.Collection;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import loyalty.domain.Coupon;
import loyalty.domain.Customer;
import org.glassfish.jersey.client.ClientConfig;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Mark George <mark.george@otago.ac.nz>
 */
public class CouponsTest {

	Customer testCustomer = new Customer("test");
	Coupon c1 = new Coupon(25);
	Coupon c2 = new Coupon(30);

	Long c1Id;
	Long c2Id;

	WebTarget customerTarget;
	WebTarget couponsTarget;

	@Before
	public void setUp() {
		Client client = ClientBuilder.newClient(new ClientConfig());

		customerTarget = client.target("http://localhost:8081/customers/");
		couponsTarget = client.target("http://localhost:8081/customers/test/coupons");

		customerTarget.request().post(Entity.entity(testCustomer, "text/xml"));

		// POST the coupon
		c1Id = couponsTarget.request("text/xml").post(Entity.entity(c1, "text/xml"))
				.readEntity(Coupon.class).getId(); // get the generated ID from the response

		// put the server generated ID in out test coupon so we can compare the objects
		c1.setId(c1Id);

		// POST the coupon
		c2Id = couponsTarget.request("application/json").post(Entity.entity(c2, "application/json"))
				.readEntity(Coupon.class).getId(); // get the generated ID from the response

		// put the server generated ID in out test coupon so we can compare the objects
		c2.setId(c2Id);
	}

	@After
	public void tearDown() {
		customerTarget.path("test").request().delete();
	}

	@Test
	public void testGetAll() {
		Collection<Coupon> jsonCollection = couponsTarget.request("application/json").get(new GenericType<Collection<Coupon>>() {});
		assertThat(jsonCollection, hasItems(c1, c2));

		Collection<Coupon> xmlCollection = couponsTarget.request("text/xml").get(new GenericType<Collection<Coupon>>() {});
		assertThat(xmlCollection, hasItems(c1, c2));
	}

	@Test
	public void testGetOne() {
		Coupon c1Json = couponsTarget.path(String.valueOf(c1Id)).request("application/json").get(Coupon.class);
		assertThat(c1Json, equalTo(c1));

		Coupon c1Xml = couponsTarget.path(String.valueOf(c1Id)).request("text/xml").get(Coupon.class);
		assertThat(c1Xml, equalTo(c1));
	}

	@Test
	public void testDelete() {
		couponsTarget.path(String.valueOf(c1Id)).request().delete();
		Response response = couponsTarget.path(String.valueOf(c1Id)).request().get();

		assertThat(response.getStatus(), is(404));
	}

	@Test
	public void testPut() {
		c1.setUsed(true);
		couponsTarget.path(String.valueOf(c1Id)).request().put(Entity.entity(c1, "application/json"));
		Coupon c1Json = couponsTarget.path(String.valueOf(c1Id)).request().get(Coupon.class);
		assertThat(c1Json.getUsed(), is(true));

		c2.setUsed(true);
		couponsTarget.path(String.valueOf(c2Id)).request().put(Entity.entity(c2, "text/xml"));
		Coupon c2Xml = couponsTarget.path(String.valueOf(c2Id)).request().get(Coupon.class);
		assertThat(c2Xml.getUsed(), is(true));
	}

}
