package business;

public class User {

	String firstname;
	String lastname;
	String phone;
	String email;

	public User(String firstname, String lastname, String phone, String email) {

		if (validate(firstname)) {
			this.firstname = firstname;
		}

		if (validate(lastname)) {
			this.lastname = lastname;
		}

		setPhone(phone);
		setEmail(email);
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public String getPhone() {
		return phone;
	}

	public String getEmail() {
		return email;
	}

	public void setPhone(String phone) {
		if (validate(phone)) {
			this.phone = phone;
		}
	}

	public void setEmail(String email) {
		if (validate(email)) {
			this.email = email;
		}
	}

	private boolean validate(String string) {
		return string != null && !string.trim().equals("");
	}
}
