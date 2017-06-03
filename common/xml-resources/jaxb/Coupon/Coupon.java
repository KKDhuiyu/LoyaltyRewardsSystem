package loyalty.domain;

import com.google.gson.annotations.SerializedName;
import java.util.Objects;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Mark George <mark.george@otago.ac.nz>
 */
@XmlRootElement
public class Coupon {
@SerializedName("id")
	private Long id;
@SerializedName("points")
	private Integer points;
@SerializedName("used")
	private Boolean used = false;

	public Coupon() {
	}

	public Coupon(Integer points) {
		this.id = System.currentTimeMillis();
		this.points = points;
	}
public Coupon createCoupon( int points){
	return  new Coupon(points);
}
	public Long getId() {
		return id;
	}
public Coupon(long id,Integer points) {
		this.id =id;
		this.points = points;
		this.used = true;
	}
public Coupon(long id){
	this.id =1495183283842L;
		this.points = 123;
		this.used = false;
}
	public void setId(Long id) {
		this.id = id;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public Boolean getUsed() {
		return used;
	}

	public void setUsed(Boolean used) {
		this.used = used;
	}

	@Override
	public String toString() {
		return "Coupon{" + "id=" + id + ", points=" + points + ", used=" + used + '}';
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 29 * hash + Objects.hashCode(this.id);
		hash = 29 * hash + Objects.hashCode(this.points);
		hash = 29 * hash + Objects.hashCode(this.used);
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
		final Coupon other = (Coupon) obj;
		if (!Objects.equals(this.id, other.id)) {
			return false;
		}
		if (!Objects.equals(this.points, other.points)) {
			return false;
		}
		if (!Objects.equals(this.used, other.used)) {
			return false;
		}
		return true;
	}

}
