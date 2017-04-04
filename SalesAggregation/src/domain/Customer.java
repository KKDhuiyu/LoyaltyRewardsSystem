package domain;

/**
 *
 * @author jiahu599
 */
public class Customer{

	private char gender;
	private String dateOfBirth;

	public Customer(String dob, char gender)  {
		this.dateOfBirth = dob;
		this.gender = gender;
	}

	public Customer() {
		this.dateOfBirth = "unknown";
		this.gender = '?';
	}

	public String getDateOfBirth() {
		return this.dateOfBirth;
	}

	public char getGender(){
		return this.gender;
	}

	public void setGender(char g) {
		this.gender = g;
	}

	public void setDateOfBirth(String dob) {
		this.dateOfBirth = dob;
	}

	@Override
	public String toString() {
		return "Customer{" + "date of birth=" + dateOfBirth +  ", gender=" + gender + '}';

	}
}
