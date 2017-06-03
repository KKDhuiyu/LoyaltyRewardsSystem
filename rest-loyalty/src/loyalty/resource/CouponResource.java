package loyalty.resource;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import loyalty.dao.LoyaltyDAO;
import loyalty.domain.Coupon;
import loyalty.domain.Customer;

/**
 *
 * @author mark
 */
@Path("/customers/{customerId}/coupons/{couponId}")
public class CouponResource {

	private static final LoyaltyDAO dao = new LoyaltyDAO();

	private final Customer customer;
	private final Coupon coupon;
	private final Long couponId;

	public CouponResource(@PathParam("customerId") String customerId, @PathParam("couponId") Long couponId) {
		customer = dao.getCustomer(customerId);
		if (customer == null) {
			throw new NotFoundException(customerId + " is not a valid customer ID");
		}

		coupon = customer.getCoupon(couponId);
		if (coupon == null) {
			throw new NotFoundException(couponId + " is not a valid coupon ID");
		}

		this.couponId = couponId;
	}

	@GET
	public Coupon getCoupon() {
		return coupon;
	}

	@DELETE
	public void removeCoupon() {
		customer.removeCoupon(coupon);
	}

	@PUT
	public Response updateCoupon(Coupon coupon) {
		if (couponId.equals(coupon.getId())) {
			customer.updateCoupon(couponId, coupon);
			return Response.ok().build();
		} else {
			return Response
					.status(Response.Status.CONFLICT)
					.entity("IDs do not match")
					.build();
		}
	}

}
