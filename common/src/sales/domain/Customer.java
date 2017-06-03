package sales.domain;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * @author Mark George <mark.george@otago.ac.nz>
 */
public class Customer implements Serializable {
 @SerializedName("sex")
	private Character gender;
  @SerializedName("date_of_birth")
	private String dateOfBirth;

	public Customer() {
	}

	public Customer(Character gender, String dateOfBirth) {
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
	}

	public Character getGender() {
		return gender;
	}

	public void setGender(Character gender) {
		this.gender = gender;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	@Override
	public String toString() {
		return "Customer{" + "gender=" + gender + ", dateOfBirth=" + dateOfBirth + '}';
	}

}
