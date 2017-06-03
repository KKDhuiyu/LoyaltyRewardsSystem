/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loyalty.resource;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import loyalty.dao.LoyaltyDAO;
import loyalty.domain.Customer;

/**
 * @author Mark George <mark.george@otago.ac.nz>
 */
@Path("/customers/{customerId}/points")
public class PointsResource {

	private static final LoyaltyDAO dao = new LoyaltyDAO();

	private final Customer customer;

	public PointsResource(@PathParam("customerId") String customerId) {
		this.customer = dao.getCustomer(customerId);
		if (customer == null) {
			throw new NotFoundException(customerId + " is not a valid customer ID");
		}
	}

	@GET
	@Path("/total")
	@Produces("text/plain")
	public Integer getTotal() {
		return customer.getTotalPoints();
	}

	@GET
	@Path("/unused")
	@Produces("text/plain")
	public Integer getUnused() {
		return customer.getTotalPoints() - customer.getUsedPoints();
	}

}
