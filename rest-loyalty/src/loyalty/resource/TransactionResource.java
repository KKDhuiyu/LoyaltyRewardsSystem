package loyalty.resource;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import loyalty.dao.LoyaltyDAO;
import loyalty.domain.Customer;
import loyalty.domain.Transaction;

/**
 * @author Mark George <mark.george@otago.ac.nz>
 */
@Path("/customers/{customerId}/transactions/{transactionId}")
public class TransactionResource {

	private static final LoyaltyDAO dao = new LoyaltyDAO();

	private final Customer customer;
	private final Transaction transaction;

	public TransactionResource(@PathParam("customerId") String customerId, @PathParam("transactionId") String transactionId) {
		customer = dao.getCustomer(customerId);
		if (customer == null) {
			throw new NotFoundException(customerId + " is not a valid customer ID");
		}

		transaction = customer.getTransaction(transactionId);
		if (transaction == null) {
			throw new NotFoundException(transactionId + " is not a valid transaction ID");
		}
	}

	@GET
	public Transaction getTransaction() {
		return transaction;
	}

	@DELETE
	public void removeTransaction() {
		customer.removeTransaction(transaction);
	}

}
