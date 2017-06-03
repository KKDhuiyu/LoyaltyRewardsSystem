package filters;

import javax.ws.rs.WebApplicationException;
import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import static org.glassfish.jersey.server.monitoring.RequestEvent.Type.ON_EXCEPTION;
import org.glassfish.jersey.server.monitoring.RequestEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exceptions produced by marshaling frameworks seem to get swallowed
 * somewhere, and do not appear in the logs making debugging difficult.
 *
 * This code registers an exception event listener that will explicitly
 * log any exceptions that are not WebApplicationExceptions (since those are
 * expected responses for some requests and should not be logged).
 *
 * Based on code found at:
 * http://stackoverflow.com/a/22336601
 *
 * @author Mark George <mark.george@otago.ac.nz>
 */
public class ExceptionLogger implements ApplicationEventListener {

	@Override
	public void onEvent(ApplicationEvent event) {
	}

	@Override
	public RequestEventListener onRequest(RequestEvent requestEvent) {
		return new ExceptionRequestEventListener();
	}

	public static class ExceptionRequestEventListener implements RequestEventListener {

		private final Logger logger;

		public ExceptionRequestEventListener() {
			logger = LoggerFactory.getLogger(getClass());
		}

		@Override
		public void onEvent(RequestEvent event) {
			if (event.getType() == ON_EXCEPTION) {
				Throwable t = event.getException();
				if (t instanceof WebApplicationException) {
					WebApplicationException wex = (WebApplicationException) t;
					// only log WebApplicationExceptions if they are unexpected (>=500)
					if (wex.getResponse().getStatus() >= 500) {
						logger.error(t.getCause().getMessage(), t.getCause());
					}
				} else {
					// some other exception, so log it.
					logger.error(t.getMessage(), t);
				}
			}
		}
	}
}
