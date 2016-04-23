package business;

public class Patient extends User {

	private int doctor;

	public Patient(String firstname, String lastname, String phone, String email, int doctor) {
		super(firstname, lastname, phone, email);
		setDoctor(doctor);
	}

	public int getDoctor() {
		return doctor;
	}

	public void setDoctor(int doctor) {
		this.doctor = doctor;
	}
}
