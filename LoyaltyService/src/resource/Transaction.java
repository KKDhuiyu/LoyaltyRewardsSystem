package resource;
import javax.ws.rs.*;
/**
 *
 * @author jiahu599
 */
@Path("/{id}")
public class Transaction {
	String id;
	String shop;
	int points;
	
	@GET
	public String getTransaction(@PathParam("id") String id){
		return id;
	}
	
	@DELETE
	public void delete(){
		
	}
}
