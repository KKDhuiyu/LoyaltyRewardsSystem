package sales.domain;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

/**
 * @author Mark George <mark.george@otago.ac.nz>
 */
public class Sale implements Serializable {
 @SerializedName("sale_date")
	private String date;
  @SerializedName("customer")
	private Customer customer;
  @SerializedName("register_sale_products")
	private Collection<SaleItem> items = new HashSet<>();

	public Sale() {
	}

	public Sale(String date, Customer customer) {
		this.date = date;
		this.customer = customer;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Collection<SaleItem> getItems() {
		return items;
	}

	public void setItems(Collection<SaleItem> items) {
		this.items = items;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public void addItem(SaleItem item) {
		items.add(item);
	}

	@Override
	public String toString() {
		return "Sale{" + "date=" + date + ", items=" + items + ", customer=" + customer + '}';
	}

}
