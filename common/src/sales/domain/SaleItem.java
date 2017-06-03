package sales.domain;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * @author Mark George <mark.george@otago.ac.nz>
 */
public class SaleItem implements Serializable {
 @SerializedName("product_id")
	private String productId;
  @SerializedName("quantity")
	private Double quantity;
   @SerializedName("price")
	private Double price;

	public SaleItem() {
	}

	public SaleItem(String productId, Double quantity, Double price) {
		this.productId = productId;
		this.quantity = quantity;
		this.price = price;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "SaleItem{" + "productId=" + productId + ", quantity=" + quantity + ", price=" + price + '}';
	}

}
