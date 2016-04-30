package hospital;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.Main;
import business.*;

public class HospitalDatabase extends Main {

	public List<User> getUser(Type type) {
		
		PreparedStatement statement = db.prepareStatement("SELECT * FROM users WHERE type=?;");
		try {
			statement.setString(1, type.toString());
		} catch (SQLException e) {
		}
		
		ResultSet rs = db.executeStatement(statement);
		List<User> list = new ArrayList<User>();

		try {
			while (rs.next()) {
				for (int i = 1; i < rs.getMetaData().getColumnCount(); i++) {
					list.add(new Doctor(rs.getInt("user_id"), rs.getString("firstname"), rs.getString("lastname"), rs.getString("phone"),
							rs.getString("email"), rs.getFloat("salary")));
				}
			}
		} catch (SQLException e) {
			System.out.println("An error occured while fetching users");
		}
		
		return list;
	}

	// Patient view
	public String getPatientAppointments() {
		return null;
	}

	public String getPatientPrescriptions() {
		return null;
	}

	public String getPatientProcedures() {
		return null;
	}

	public String getPatientInvoices() {
		return null;
	}

	public String getPatientNotes() {
		return null;
	}

	public void raiseDoctorSalary(int doctor, Float raise) {
		PreparedStatement statement = db.prepareStatement("SELECT * FROM users WHERE user_id=?;");
		try {
			statement.setInt(1, doctor);
		} catch (SQLException e) {
		}
		
		db.prepareStatement("UPDATE users SET salaray=? WHERE id=?;");
	}

	public void bookAppointment() {

	}

	public void cancelAppointment() {

	}
}
