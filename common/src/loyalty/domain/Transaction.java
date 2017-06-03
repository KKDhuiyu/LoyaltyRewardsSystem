package loyalty.domain;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.Objects;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Mark George <mark.george@otago.ac.nz>
 */
@XmlRootElement
public class Transaction implements Serializable {
 @SerializedName("id")
	private String id;
  @SerializedName("shop")
	private String shop;
   @SerializedName("points")
	private Integer points;

	public Transaction() {
	}

	public Transaction(String id, String shop, Integer points) {
		this.id = id;
		this.shop = shop;
		this.points = points;
	}
	public Transaction(String id, Integer points) {
		this.id = id;
		this.shop = "";
		this.points = points;
	}
	public Transaction newTransaction(String id, Integer points){
	return new Transaction(id,points);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getShop() {
		return shop;
	}

	public void setShop(String shop) {
		this.shop = shop;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	@Override
	public String toString() {
		return "Transaction{" + "id=" + id + ", shop=" + shop + ", points=" + points + '}';
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 53 * hash + Objects.hashCode(this.id);
		hash = 53 * hash + Objects.hashCode(this.shop);
		hash = 53 * hash + Objects.hashCode(this.points);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Transaction other = (Transaction) obj;
		if (!Objects.equals(this.id, other.id)) {
			return false;
		}
		if (!Objects.equals(this.shop, other.shop)) {
			return false;
		}
		if (!Objects.equals(this.points, other.points)) {
			return false;
		}
		return true;
	}

}
