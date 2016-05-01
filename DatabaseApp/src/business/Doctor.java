package business;

public class Doctor extends User {

	private Float salary;

	public Doctor(int id, String firstname, String lastname, String phone, String email, Float salary) {
		super(id, firstname, lastname, phone, email, Type.Doctor);
		setSalary(salary);
	}

	public Float getSalary() {
		return salary;
	}

	public void setSalary(Float salary) {
		if (salary > 0) {
			this.salary = salary;
		}
	}
}
