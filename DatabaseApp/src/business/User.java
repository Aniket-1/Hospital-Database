package business;

public class User {

	int id;
	String firstname;
	String lastname;
	String phone;
	String email;

	public User(int id, String firstname, String lastname, String phone, String email) {

		this.id = id;

		if (validate(firstname)) {
			this.firstname = firstname;
		}

		if (validate(lastname)) {
			this.lastname = lastname;
		}

		setPhone(phone);
		setEmail(email);
	}

	public int getId() {
		return id;
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
	
	@Override
	public String toString() {
		return id + ". " + firstname + " " + lastname;
	}
}
