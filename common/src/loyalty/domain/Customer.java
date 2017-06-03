package loyalty.domain;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author Mark George <mark.george@otago.ac.nz>
 */
@XmlRootElement
public class Customer implements Serializable{
	 @SerializedName("id")
	private String id;
 @SerializedName("transactions")
	@XmlTransient
	private final Map<String, Transaction> transactions = new HashMap<>();
	@SerializedName("coupons")
	@XmlTransient
	private final Map<Long, Coupon> coupons = new HashMap<>();

	public Customer() {
	}

	public Customer(String id) {
		this.id = id;
	}

	public void addTransaction(Transaction transaction) {
		transactions.put(transaction.getId(), transaction);
	}

	public void addCoupon(Coupon coupon) {
		coupon.setId(System.currentTimeMillis());
		coupons.put(coupon.getId(), coupon);
	}

	public void updateCoupon(Long id, Coupon coupon) {
		coupons.put(id, coupon);
	}

	public void removeTransaction(Transaction transaction) {
		transactions.remove(transaction.getId());
	}

	public void removeCoupon(Coupon coupon) {
		coupons.remove(coupon.getId());
	}

	public Integer getTotalPoints() {

		int total = 0;

		for (Transaction transaction : transactions.values()) {
			total += transaction.getPoints();
		}

		return total;
	}

	public Integer getUsedPoints() {

		int total = 0;

		for (Coupon coupon : coupons.values()) {
			total += coupon.getPoints();
		}

		return total;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Collection<Transaction> getTransactions() {
		return transactions.values();
	}

	public Collection<Coupon> getCoupons() {
		return coupons.values();
	}

	public Transaction getTransaction(String transactionId) {
		return transactions.get(transactionId);
	}

	public Coupon getCoupon(Long couponId) {
		return coupons.get(couponId);
	}

}
