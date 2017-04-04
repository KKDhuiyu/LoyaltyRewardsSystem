package domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Transaction {

	private String id;
	private String shot;
	private int points;

	public Transaction() {
	}

	public Transaction(String id, String name, String description, Double price, Double stockOnHand) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.stockOnHand = stockOnHand;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getStockOnHand() {
		return stockOnHand;
	}

	public void setStockOnHand(Double stockOnHand) {
		this.stockOnHand = stockOnHand;
	}

	@Override
	public String toString() {
		return "Product{" + "id=" + id + ", name=" + name + ", description=" + description + ", price=" + price + ", stockOnHand=" + stockOnHand + '}';
	}

}
