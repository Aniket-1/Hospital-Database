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
				"SELECT firstname, lastname, phone, email, salary, notes, type " + "FROM users " + "WHERE user_id=?;");

		try {
			statement.setInt(1, id);
			ResultSet rs = db.executeStatement(statement);

			rs.next();
			Type type = Type.valueOf(rs.getString(7));

			switch (type) {
			case Admin:
				user = new Admin(id, rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getFloat(5));
				break;
			case Doctor:
				user = new Doctor(id, rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getFloat(5));
				break;
			case Patient:
				user = new Patient(id, rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
				((Patient) user).setNotes(rs.getString(6));
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

	public List<User> getAllUsers(Type type) {

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

	public void updateUser(User user) {

		PreparedStatement statement;
		switch (user.getType()) {
		case Admin:
			statement = db.prepareStatement("CALL raise_salary(?, ?);");
			try {
				statement.setInt(1, user.getId());
				statement.setFloat(2, ((Admin) user).getSalary());

				db.executeUpdateStatement(statement);
			} catch (SQLException e) {
				System.out.println("Could not update admin");
			}
			break;
		case Doctor:
			statement = db.prepareStatement("CALL raise_salary(?, ?);");
			try {
				statement.setInt(1, user.getId());
				statement.setFloat(2, ((Doctor) user).getSalary());

				db.executeUpdateStatement(statement);
			} catch (SQLException e) {
				System.out.println("Could not update doctor");
			}
			break;
		case Patient:
			statement = db.prepareStatement("UPDATE users SET notes=? WHERE user_id=?;");
			try {
				statement.setString(1, ((Patient) user).getNotes());
				statement.setInt(2, user.getId());

				db.executeUpdateStatement(statement);
			} catch (SQLException e) {
				System.out.println("Could not update patient");
			}
			break;
		}
	}

	public List<String> getUpcomingAppointments(int id, Type type1, Type type2) {
		List<String> list = new ArrayList<String>();

		PreparedStatement statement = db.prepareStatement(
				"SELECT firstname, lastname, date " + "FROM appointments " + "INNER JOIN users ON user_id=" + type2
						+ "_id " + "WHERE date >= NOW() AND ?=" + type1 + "_id " + "ORDER BY date DESC;");
		try {
			statement.setInt(1, id);
			ResultSet rs = db.executeStatement(statement);

			while (rs.next()) {
				String entry = "Appointment with " + rs.getString(1) + " " + rs.getString(2) + " on " + rs.getDate(3);
				list.add(entry);
			}

			rs.close();
		} catch (SQLException e) {
			System.out.println("An error occurred while reading appointments");
		}

		return list;
	}

	public List<String> getPastAppointments(int id, Type type1, Type type2) {
		List<String> list = new ArrayList<String>();

		PreparedStatement statement = db.prepareStatement(
				"SELECT firstname, lastname, date " + "FROM appointments " + "INNER JOIN users ON user_id=" + type2
						+ "_id " + "WHERE date < NOW() AND ?=" + type1 + "_id " + "ORDER BY date DESC;");
		try {
			statement.setInt(1, id);
			ResultSet rs = db.executeStatement(statement);

			while (rs.next()) {
				String entry = "Appointment with " + rs.getString(1) + " " + rs.getString(2) + " on " + rs.getDate(3);
				list.add(entry);
			}

			rs.close();
		} catch (SQLException e) {
			System.out.println("An error occurred while reading appointments");
		}

		return list;
	}

	public List<String> getAppointments(int doctor) {
		List<String> list = new ArrayList<String>();

		PreparedStatement statement = db.prepareStatement(
				"SELECT appointment_id, firstname, lastname, date "
						+ "FROM appointments "
						+ "INNER JOIN users ON user_id=patient_id "
						+ "WHERE doctor_id=? "
						+ "AND date > CURDATE() "
						+ "ORDER BY date DESC;");
		try {
			statement.setInt(1, doctor);
			ResultSet rs = db.executeStatement(statement);

			while (rs.next()) {
				String entry = rs.getInt(1) + ". " + rs.getString(2) + " " + rs.getString(3) + " on " + rs.getDate(4);
				list.add(entry);
			}

			rs.close();
		} catch (SQLException e) {
			System.out.println("An error occurred while reading appointments");
		}

		return list;
	}
	
	public List<String> getAllAppointments(Type type) {
		List<String> list = new ArrayList<String>();

		PreparedStatement statement = db.prepareStatement("SELECT " + type + "_id, firstname, lastname, date "
				+ "FROM appointments " + "INNER JOIN users ON user_id=" + type + "_id " + "ORDER BY date DESC;");
		try {
			ResultSet rs = db.executeStatement(statement);

			while (rs.next()) {
				String entry = rs.getInt(1) + ". " + rs.getString(2) + " " + rs.getString(3) + " on " + rs.getDate(4);
				list.add(entry);
			}

			rs.close();
		} catch (SQLException e) {
			System.out.println("An error occurred while reading appointments");
		}

		return list;
	}

	// Admin view
	public void bookAppointment(int patient, int doctor, LocalDate date, String notes) {
		if (date.isBefore(LocalDate.now())) {
			System.out.println("Cannot create appointment in the past");
			return;
		}

		PreparedStatement statement = db.prepareStatement("CALL book_appointment(?, ?, ?, ?);");
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

	public List<String> getNotifications() {
		ResultSet rs = db.executeStatement("SELECT * FROM notifications;");

		List<String> list = new ArrayList<String>();
		try {
			while (rs.next()) {
				list.add(rs.getInt(1) + ". " + rs.getString(3));
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println("An error occurred while reading notifications");
		}

		return list;
	}

	public List<String> getLogs() {
		ResultSet rs = db.executeStatement("SELECT * FROM logs_admin;");

		List<String> list = new ArrayList<String>();
		try {
			while (rs.next()) {
				list.add(rs.getDate(2) + ": " + rs.getString(3));
			}
		} catch (SQLException e) {
			System.out.println("An error occurred while reading notifications");
		}

		return list;
	}

	// Doctor view
	public List<String> getAllProcedures() {
		List<String> list = new ArrayList<String>();

		PreparedStatement statement = db.prepareStatement("SELECT procedure_id, procedure_name " + "FROM procedures;");

		try {
			ResultSet rs = db.executeStatement(statement);

			while (rs.next()) {
				list.add(rs.getInt(1) + ". " + rs.getString(2));
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println("An error occurred while fetching procedures");
		}

		return list;
	}

	public void performProcedure(int patient, int procedure, int doctor) {
		PreparedStatement statement = db.prepareStatement("CALL perform_procedure(?, ?, ?);");

		try {
			statement.setInt(1, patient);
			statement.setInt(2, procedure);
			statement.setInt(3, doctor);

			db.executeUpdateStatement(statement);
			System.out.println("Successfully performed procedure. Patient is A-OK");
		} catch (SQLException e) {
			System.out.println("An error occurred while performing procedure. RIP patient");
		}
	}

	public void prescribeMedication(int patient, int medication, int doctor) {
		PreparedStatement statement = db.prepareStatement("CALL prescribe_medication(?, ?, ?);");

		try {
			statement.setInt(1, patient);
			statement.setInt(2, medication);
			statement.setInt(3, doctor);

			db.executeStatement(statement);
			System.out.println("Successfully prescribed medication");
		} catch (SQLException e) {
			System.out.println("An error occurred while prescribing medication");
		}
	}

	public List<String> getMedications() {
		List<String> list = new ArrayList<String>();
		PreparedStatement statement = db.prepareStatement("CALL get_medications();");

		try {
			ResultSet rs = db.executeStatement(statement);

			while (rs.next()) {
				list.add(rs.getInt(1) + ". " + rs.getString(2));
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println("An error occurred while reading medications");
		}

		return list;
	}

	// Patient view
	public List<String> getPatientPrescriptions(int patient) {
		List<String> list = new ArrayList<String>();
		PreparedStatement statement = db.prepareStatement("CALL get_prescriptions(?);");
		try {
			statement.setInt(1, patient);

			ResultSet rs = db.executeStatement(statement);
			while (rs.next()) {
				list.add(rs.getString(2));
			}
		} catch (SQLException e) {
		}
		return list;
	}

	public List<String> getPatientProcedures(int patient) {
		List<String> list = new ArrayList<String>();
		PreparedStatement statement = db.prepareStatement("CALL get_procedures(?);");
		try {
			statement.setInt(1, patient);

			ResultSet rs = db.executeStatement(statement);
			while (rs.next()) {
				list.add(rs.getString(2));
			}
		} catch (SQLException e) {
		}
		return list;
	}

	public List<String> getPatientInvoices(int patient) {
		List<String> list = new ArrayList<String>();
		PreparedStatement statement = db.prepareStatement("CALL get_invoices(?);");
		try {
			statement.setInt(1, patient);

			ResultSet rs = db.executeStatement(statement);
			while (rs.next()) {
				list.add(rs.getInt(1) + ". Invoice amount: $" + rs.getFloat(2));
			}
		} catch (SQLException e) {
		}
		return list;
	}

	public List<String> getFamilyDoctors(int patient) {
		List<String> list = new ArrayList<String>();

		PreparedStatement statement = db.prepareStatement("CALL get_family_doctors(?);");

		try {
			statement.setInt(1, patient);
			ResultSet rs = db.executeStatement(statement);

			rs.next();
			list.add(rs.getInt(1) + ". " + rs.getString(2) + " " + rs.getString(3));

			rs.close();
		} catch (SQLException e) {
			System.out.println("An error occurred while fetching family doctors");
		}

		return list;
	}

	public List<String> getSpecialists() {
		List<String> list = new ArrayList<String>();

		PreparedStatement statement = db.prepareStatement("CALL get_specialists();");

		try {
			ResultSet rs = db.executeStatement(statement);

			while (rs.next()) {
				list.add(rs.getInt(1) + ". " + rs.getString(2) + " " + rs.getString(3) + " for " + rs.getString(4));
			}

			rs.close();
		} catch (SQLException e) {
			System.out.println("An error occurred while fetching family doctors");
		}

		return list;
	}
}
