package loyalty.resource;

import java.net.URI;
import java.util.Collection;
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
import loyalty.domain.Transaction;

/**
 * @author Mark George <mark.george@otago.ac.nz>
 */
@Path("/customers/{customerId}/transactions")
public class TransactionsResource {

	private static final LoyaltyDAO dao = new LoyaltyDAO();

	private final Customer customer;

	public TransactionsResource(@PathParam("customerId") String customerId) {
		this.customer = dao.getCustomer(customerId);
		if (customer == null) {
			throw new NotFoundException(customerId + " is not a valid customer ID");
		}
	}

	@GET
	public Collection<Transaction> getTransactions() {
		return customer.getTransactions();
	}

	@POST
	public Response addTransaction(Transaction transaction, @Context UriInfo uri) {
		customer.addTransaction(transaction);

		URI newURI = uri.getAbsolutePathBuilder()
				.path(transaction.getId())
				.build();

		return Response.created(newURI).build();
	}

}
