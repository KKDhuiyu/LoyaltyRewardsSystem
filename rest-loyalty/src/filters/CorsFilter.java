package filters;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;

/**
 * @author Mark George <mark.george@otago.ac.nz>
 */
public class CorsFilter implements ContainerResponseFilter {

	@Override
	public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {

		// get the requested origin, method and headers
		String requestedOrigin = request.getHeaderString("Origin");
		String requestedMethod = request.getHeaderString("Access-Control-Request-Method");
		String requestedHeaders = request.getHeaderString("Access-Control-Request-Headers");

		// allow the requested method, headers, and origin
		MultivaluedMap<String, Object> headers = response.getHeaders();
		headers.add("Access-Control-Allow-Origin", requestedOrigin);
		headers.add("Access-Control-Allow-Methods", requestedMethod);
		headers.add("Access-Control-Allow-Headers", requestedHeaders);

		// allow 'non-standard' headers to be exposed to client
		headers.add("Access-Control-Expose-Headers", "Etag, Location");

	}

}
