package resource;
import javax.ws.rs.*;
/**
 *
 * @author jiahu599
 */
@Path("/transactions")
public class Transactions {
	@GET
	public Collection<Transaction> getTransactions(){
		
	}
}
