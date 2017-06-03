package filters;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mark George <mark.george@otago.ac.nz>
 */
@Provider
public class ExceptionMessageHandler implements ExceptionMapper<Exception> {

	private final Logger logger = LoggerFactory.getLogger(ExceptionMessageHandler.class);

	@Override
	public Response toResponse(Exception ex) {

		// exception messages are not included in error responses by default, so
		// this code explicitly extracts them, and inserts them into the response.

		if (ex instanceof WebApplicationException) {
			WebApplicationException wex = (WebApplicationException) ex;
			return Response.status(wex.getResponse().getStatus()).entity(wex.getMessage()).type("text/plain").build();
		} else {
			// else log and return a 500 response
			logger.error("Server threw an exception", ex);
			return Response.status(500).entity("Server-side error").build();
		}
	}

}
