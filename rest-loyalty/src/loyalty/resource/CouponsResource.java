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
import loyalty.domain.Coupon;
import loyalty.domain.Customer;

/**
 * @author Mark George <mark.george@otago.ac.nz>
 */
@Path("/customers/{customerId}/coupons")
public class CouponsResource {

	private static final LoyaltyDAO dao = new LoyaltyDAO();

	private final Customer customer;

	public CouponsResource(@PathParam("customerId") String customerId) {
		this.customer = dao.getCustomer(customerId);
		if (customer == null) {
			throw new NotFoundException(customerId + " is not a valid customer ID");
		}
	}

	@GET
	public Collection<Coupon> getCoupons() {
		return customer.getCoupons();
	}

	@POST
	public Response addCoupon(Coupon coupon, @Context UriInfo uri) {
		customer.addCoupon(coupon);

		URI newURI = uri.getAbsolutePathBuilder()
				.path(coupon.getId().toString())
				.build();

		return Response.created(newURI).entity(coupon).build();
	}

}
