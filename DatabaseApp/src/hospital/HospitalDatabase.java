package hospital;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import application.Main;
import business.*;

public class HospitalDatabase extends Main {

	public User getUserInfo(int id) {
		User user = null;
		
		PreparedStatement statement = db.prepareStatement(
				"SELECT firstname, lastname, phone, email, salary, notes, type "
						+ "FROM users "
						+ "WHERE user_id=?;");
		
		try {
			statement.setInt(1, id);
			ResultSet rs = db.executeStatement(statement);
			
			rs.next();
			Type type = Type.valueOf(rs.getString(7));
			
			switch (type) {
			case Admin:
				user = new Admin(id, rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),rs.getFloat(5));
				break;
			case Doctor:
				user = new Doctor(id, rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),rs.getFloat(5));
				break;
			case Patient:
				user = new Patient(id, rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
				((Patient)user).setNotes(rs.getString(6));
				break;
			default:
				System.out.println("An error occurred");
				return null;				
			}
		} catch (SQLException e) {
			System.out.println("An error occurred while fetching user information");
		}
		
		return user;
	}
	
	public List<User> getUsers(Type type) {

		PreparedStatement statement = db.prepareStatement(
				"SELECT user_id, firstname, lastname, phone, email, salary " + "FROM users " + "WHERE type=?;");
		try {
			statement.setString(1, type.toString());
		} catch (SQLException e) {
		}

		ResultSet rs = db.executeStatement(statement);
		List<User> list = new ArrayList<User>();

		try {
			while (rs.next()) {
				list.add(new Doctor(rs.getInt("user_id"), rs.getString("firstname"), rs.getString("lastname"),
						rs.getString("phone"), rs.getString("email"), rs.getFloat("salary")));
			}
		} catch (SQLException e) {
			System.out.println("An error occurred while fetching " + type + "s");
		}
		return list;
	}
	
	public List<String> getAllAppointments(Type type) {
		List<String> list = new ArrayList<String>();

		PreparedStatement statement = db.prepareStatement("SELECT appointment_id, firstname, lastname, date "
		+ "FROM appointments "
		+ "INNER JOIN users ON user_id=" + type.toString() + "_id "
		+ "WHERE date > NOW() "
		+ "ORDER BY date DESC;");

		ResultSet rs = db.executeStatement(statement);

		try {
			while (rs.next()) {
				String entry = rs.getInt(1) + ". " + rs.getString(2) + " " + rs.getString(3) + " on "
						+ rs.getDate(4);
				list.add(entry);
			}
		} catch (SQLException e) {
			System.out.println("An error occurred while reading appointments");
		}

		return list;
	}

	public List<String> getAppointments(Type type, int id) {
		List<String> list = new ArrayList<String>();

		PreparedStatement statement = db.prepareStatement("SELECT " + type.toString()
				+ "_id, firstname, lastname, date " + "FROM appointments " + "INNER JOIN users ON user_id="
				+ type.toString() + "_id " + "WHERE " + type.toString() + "_id=? " + "ORDER BY date DESC;");
		try {
			statement.setInt(1, id);
		} catch (SQLException e) {
		}

		ResultSet rs = db.executeStatement(statement);

		try {
			while (rs.next()) {
				String entry = rs.getInt(1) + ". " + rs.getString(2) + " " + rs.getString(3) + " on "
						+ rs.getDate(4);
				list.add(entry);
			}
		} catch (SQLException e) {
			System.out.println("An error occurred while reading appointments");
		}

		return list;
	}

	// Admin view
	// Admin view
	public void raiseDoctorSalary(int doctor, Float raise) {
		Float salary = 0f;
		PreparedStatement statement = db.prepareStatement("SELECT salary FROM users WHERE user_id=?;");
		try {
			statement.setInt(1, doctor);
		} catch (SQLException e) {
		}

		ResultSet rs = db.executeStatement(statement);
		try {
			rs.next();
			salary = rs.getFloat(1);
			salary += raise;

			statement = db.prepareStatement("UPDATE users SET salary=? WHERE user_id=?;");

			statement.setFloat(1, salary);
			statement.setInt(2, doctor);

			db.executeUpdateStatement(statement);
		} catch (SQLException e) {
			System.out.println("An error occurred while updating salary");
			return;
		}
	}

	
	public void bookAppointment(int patient, int doctor, LocalDate date, String notes) {
		if (date.isBefore(LocalDate.now())) {
			System.out.println("Cannot create appointment in the past");
			return;
		}

		PreparedStatement statement = db.prepareStatement(
				"INSERT INTO appointments (patient_id, doctor_id, date, notes) VALUES " + "(?, ?, ?, ?);");
		try {
			statement.setInt(1, patient);
			statement.setInt(2, doctor);
			statement.setDate(3, Date.valueOf(date));
			statement.setString(4, notes);
			
			db.executeUpdateStatement(statement);
			System.out.println("Successfully booked appointment for patient");
		} catch (SQLException e) {
			System.out.println("An error occurred while booking appointment");
		}
	}

	public void cancelAppointment(int appointment) {
		PreparedStatement statement = db.prepareStatement("DELETE FROM appointments WHERE appointment_id=?;");
		
		try {
			statement.setInt(1, appointment);
			db.executeUpdateStatement(statement);
			System.out.println("Successfully cancelled appointment");
		} catch (SQLException e) {
			System.out.println("An error occurred while cancelling appointment");
		}
	}

	public ResultSet getNotifications() {
		return db.executeStatement("SELECT * FROM notifications;");
	}
	
	// Doctor view
	
	
	// Patient view
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

}
