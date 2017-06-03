package filters;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MediaType;

/**
 * @author Mark George <mark.george@otago.ac.nz>
 */
public class DebugFilter implements ContainerResponseFilter {

	@Override
	public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
		System.out.println("\n==Request==");
		System.out.println("URI: " + request.getUriInfo().getAbsolutePath());
		System.out.println("METHOD: " + request.getMethod());

		System.out.println("ACCEPT: " + request.getAcceptableMediaTypes());

		MediaType requestContentType = request.getMediaType();
		if (requestContentType != null) {
			System.out.println("CONTENT-TYPE: " + requestContentType);
		}

		System.out.println("REQUEST HEADERS: " + request.getHeaders().keySet());

		System.out.println("\n==Response==");
		System.out.println("RESPONSE STATUS: " + response.getStatusInfo().getStatusCode() + " " + response.getStatusInfo().getReasonPhrase());

		MediaType responseContentType = response.getMediaType();
		if (responseContentType != null) {
			System.out.println("CONTENT-TYPE: " + responseContentType);
		}

		System.out.println("RESPONSE HEADERS: " + response.getHeaders().keySet());

		System.out.println("\n--------------");
	}

}
