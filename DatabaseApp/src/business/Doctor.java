package business;

public class Doctor extends User {

	private Float salary;

	public Doctor(String firstname, String lastname, String phone, String email, Float salary) {
		super(firstname, lastname, phone, email);
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
