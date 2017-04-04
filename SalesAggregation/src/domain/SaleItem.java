package domain;


public class SaleItem {

	private String productId;

	private Double price;
	private Double quantity;

	public SaleItem() {
	}

	public SaleItem(String id, Double price, Double quantity) {
		this.productId = id;
		this.price = price;
		this.quantity = quantity;
	}

	public String getId() {
		return productId;
	}

	public void setId(String id) {
		this.productId = id;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "SaleItem{" + "id=" + productId +  ", price=" + price + 
				  ", quantity=" + quantity + '}';
	}

}
