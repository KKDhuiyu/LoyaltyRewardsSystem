package loyalty.resource;

import java.net.URI;
import java.util.Collection;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import loyalty.dao.LoyaltyDAO;
import loyalty.domain.Customer;

/**
 * @author Mark George <mark.george@otago.ac.nz>
 */
@Path("/customers/")
public class CustomerResource {

	private static final LoyaltyDAO dao = new LoyaltyDAO();

	@POST
	public Response addCoupon(Customer customer, @Context UriInfo uri) {
		dao.addCustomer(customer);

		URI newURI = uri.getAbsolutePathBuilder()
				.path(customer.getId())
				.build();

		return Response.created(newURI).entity(customer).build();
	}

	@GET
	public Collection<Customer> getCustomers() {
		return dao.getCustomers();
	}

	@DELETE
	@Path("{customerId}")
	public void deleteCustomer(@PathParam("customerId") String customerId) {
		Customer customer = dao.getCustomer(customerId);
		if (customer == null) {
			throw new NotFoundException(customerId + " is not a valid customer ID");
		}

		dao.removeCustomer(customer);
	}

}
