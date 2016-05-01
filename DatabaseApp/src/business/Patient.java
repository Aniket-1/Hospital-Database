package business;

public class Patient extends User {

	private String notes;

	public Patient(int id, String firstname, String lastname, String phone, String email) {
		super(id, firstname, lastname, phone, email, Type.Patient);
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
}
