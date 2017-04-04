package resource;
import javax.ws.rs.*;

/**
 *
 * @author jiahu599
 
 * 
 * This code means that this resource class will handle requests that are sent to the
/products/ path of this web service.
*/
@Path("/points")
public class Points {


	
	@GET
	@Path("{total}")
	public int getTotal(@PathParam("total") int total){
		return total;
	}
	
	@GET
	@Path("{unused}")
	public int getUnused(@PathParam("unused") int unused){
		return unused;
	}

}
