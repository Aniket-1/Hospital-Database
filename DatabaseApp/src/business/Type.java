package business;

public enum Type {
	
	Admin("Admin"),
	Doctor("Doctor"),
	Patient("Patient");

	private String type;
	
	private Type(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return type;
	}
}
