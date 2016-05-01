package application;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import business.*;
import hospital.HospitalDatabase;
import hospital.HospitalSecurity;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;

public class AdminViewController extends Main {

	private HospitalDatabase hdb = new HospitalDatabase();

	public MenuBar menuBar;
	public Menu menuFile;
	public MenuItem menuItemLogout;
	public MenuItem menuItemClose;

	public AnchorPane paneMenu;
	public Button buttonCreateAccount;
	public Button buttonDoctors;
	public Button buttonAppointments;
	public Button buttonNotifications;
	public Button buttonInformation;

	public AnchorPane paneCreateUser;
	public ToggleGroup rButtonUsers;
	public RadioButton rButtonAdmin;
	public RadioButton rButtonDoctor;
	public RadioButton rButtonPatient;

	public AnchorPane paneCreateAdmin;
	public TextField textFieldAdminFirstname;
	public TextField textFieldAdminLastname;
	public TextField textFieldAdminPhone;
	public TextField textFieldAdminEmail;
	public TextField textFieldAdminSalary;
	public TextField textFieldAdminUsername;
	public PasswordField passwordFieldAdminPassword;
	public Button buttonCreateAdmin;

	public AnchorPane paneCreateDoctor;
	public TextField textFieldDoctorFirstname;
	public TextField textFieldDoctorLastname;
	public TextField textFieldDoctorPhone;
	public TextField textFieldDoctorEmail;
	public TextField textFieldDoctorSalary;
	public TextField textFieldDoctorUsername;
	public PasswordField passwordFieldDoctorPassword;
	public Button buttonCreateDoctor;

	public AnchorPane paneCreatePatient;
	public TextField textFieldPatientFirstname;
	public TextField textFieldPatientLastname;
	public TextField textFieldPatientPhone;
	public TextField textFieldPatientEmail;
	public TextField textFieldPatientDoctor;
	public TextField textFieldPatientUsername;
	public PasswordField passwordFieldPatientPassword;
	public Button buttonCreatePatient;

	public AnchorPane paneDoctors;
	public ComboBox<String> comboBoxDoctorId;
	public TextField textFieldRaiseAmount;
	public Button buttonUpdateDoctor;

	public AnchorPane paneAppointments;
	public Button buttonBookAppointment;
	public Button buttonCancelAppointment;

	public AnchorPane paneBookAppointment;
	public ComboBox<String> comboBoxPatient;
	public ComboBox<String> comboBoxDoctor;
	public DatePicker datePickerDate;
	public TextArea textAreaNotes;
	public Button buttonBook;

	public AnchorPane paneCancelAppointment;
	public ComboBox<String> comboBoxAppointment;
	public Button buttonCancel;

	public AnchorPane paneNotifications;
	public TextArea textAreaNotifications;

	public AnchorPane paneInformation;
	public Label labelFirstname;
	public Label labelLastname;
	public Label labelPhone;
	public Label labelEmail;
	public Label labelSalary;

	public void onButtonCreateAccountClick() {
		hide();
		paneCreateUser.setVisible(true);
	}

	public void onButtonDoctorsClick() {
		hide();
		paneDoctors.setVisible(true);

		setData(comboBoxDoctorId, hdb.getAllUsers(Type.Doctor));
	}

	public void onButtonAppointmentsClick() {
		hide();
		paneAppointments.setVisible(true);
	}

	// TODO Also show log files on medications and procedures
	public void onButtonNotificationsClick() {
		hide();
		paneNotifications.setVisible(true);

		List<String> list = hdb.getNotifications();

		for (int i = 0; i < list.size(); i++) {
			textAreaNotifications.setText(list.get(i));
		}
	}
	
	public void onButtonLogsClick() {
		hide();
		paneNotifications.setVisible(true);

		List<String> list = hdb.getLogs();

		for (int i = 0; i < list.size(); i++) {
			textAreaNotifications.setText(list.get(i));
		}
	}

	public void onButtonInformationClick() {
		hide();
		paneInformation.setVisible(true);

		Admin user = (Admin) hdb.getUserInfo(userId);

		labelFirstname.setText("Firstname: " + user.getFirstname());
		labelLastname.setText("Lastname: " + user.getLastname());
		labelPhone.setText("Phone: " + user.getPhone());
		labelEmail.setText("Email: " + user.getEmail());
		labelSalary.setText("Salary: $" + user.getSalary());
	}

	public void radioButtonAdminSelected() {
		paneCreateAdmin.setVisible(true);
		paneCreateDoctor.setVisible(false);
		paneCreatePatient.setVisible(false);
	}

	public void radioButtonDoctorSelected() {
		paneCreateAdmin.setVisible(false);
		paneCreateDoctor.setVisible(true);
		paneCreatePatient.setVisible(false);
	}

	public void radioButtonPatientSelected() {
		paneCreateAdmin.setVisible(false);
		paneCreateDoctor.setVisible(false);
		paneCreatePatient.setVisible(true);
	}

	public void onButtonCreateAdminClick() {
		String firstname = textFieldAdminFirstname.getText();
		String lastname = textFieldAdminLastname.getText();
		String phone = textFieldAdminPhone.getText();
		String email = textFieldAdminEmail.getText();
		Float salary = 0f;
		try {
			salary = Float.parseFloat(textFieldAdminSalary.getText());
		} catch (NumberFormatException e) {
			System.out.println("Salary is not valid\nCould not create user");
			return;
		}
		String username = textFieldAdminUsername.getText();
		String password = passwordFieldAdminPassword.getText();

		User user = new Admin(0, firstname, lastname, phone, email, salary);

		createUser(user, username, password);
	}

	public void onButtonCreateDoctorClick() {
		String firstname = textFieldDoctorFirstname.getText();
		String lastname = textFieldDoctorLastname.getText();
		String phone = textFieldDoctorPhone.getText();
		String email = textFieldDoctorEmail.getText();
		Float salary = 0f;
		try {
			salary = Float.parseFloat(textFieldDoctorSalary.getText());
		} catch (NumberFormatException e) {
			System.out.println("Salary is not valid\nCould not create user");
			return;
		}
		String username = textFieldDoctorUsername.getText();
		String password = passwordFieldDoctorPassword.getText();

		User user = new Doctor(0, firstname, lastname, phone, email, salary);

		createUser(user, username, password);
	}

	public void onButtonCreatePatientClick() {
		String firstname = textFieldPatientFirstname.getText();
		String lastname = textFieldPatientLastname.getText();
		String phone = textFieldPatientPhone.getText();
		String email = textFieldPatientEmail.getText();

		String username = textFieldPatientUsername.getText();
		String password = passwordFieldPatientPassword.getText();

		User user = new Patient(0, firstname, lastname, phone, email);

		createUser(user, username, password);
	}

	private void createUser(User user, String username, String password) {
		HospitalSecurity hs = new HospitalSecurity();
		hs.newUser(user, username, password);
	}

	public void onButtonUpdateDoctorClick() {
		// Get doctor id
		String doctorText = comboBoxDoctorId.getValue();
		doctorText = doctorText.substring(0, doctorText.indexOf('.'));

		// Get raise amount
		Float raise = 0f;
		try {
			raise = Float.parseFloat(textFieldRaiseAmount.getText());
		} catch (NumberFormatException e) {
			System.out.println("Could not raise doctor's salary");
			return;
		}

		Doctor doctor = new Doctor(Integer.parseInt(doctorText), null, null, null, null, raise);

		hdb.updateUser(doctor);
	}

	public void onButtonBookAppointmentClick() {
		paneBookAppointment.setVisible(true);
		paneCancelAppointment.setVisible(false);

		setData(comboBoxPatient, hdb.getAllUsers(Type.Patient));
		setData(comboBoxDoctor, hdb.getAllUsers(Type.Doctor));
	}

	public void onButtonBookClick() {
		String patientText = comboBoxPatient.getValue();
		String doctorText = comboBoxDoctor.getValue();

		patientText = patientText.substring(0, patientText.indexOf('.'));
		doctorText = doctorText.substring(0, doctorText.indexOf('.'));

		int patient = Integer.parseInt(patientText);
		int doctor = Integer.parseInt(doctorText);

		LocalDate date = datePickerDate.getValue();

		String notes = textAreaNotes.getText();

		hdb.bookAppointment(patient, doctor, date, notes);
	}

	public void onButtonCancelAppointmentClick() {
		paneBookAppointment.setVisible(false);
		paneCancelAppointment.setVisible(true);

		setData(comboBoxAppointment, hdb.getAllAppointments(Type.Patient));
	}

	public void onButtonCancelClick() {
		String appointmentText = comboBoxAppointment.getValue();
		appointmentText = appointmentText.substring(0, appointmentText.indexOf('.'));

		int appointment = Integer.parseInt(appointmentText);

		hdb.cancelAppointment(appointment);
	}

	public void close() {
		logout();
		try {
			Main.db.closeConnection();
		} catch (SQLException e) {
			System.out.println("Could not successfully close connection");
		}
		System.exit(0);
	}

	public void logout() {
		// Change screen
		next(false, "Login.fxml", style);
	}

	private void hide() {
		paneCreateUser.setVisible(false);
		paneDoctors.setVisible(false);
		paneAppointments.setVisible(false);
		paneNotifications.setVisible(false);
		paneInformation.setVisible(false);
	}

	private void setData(ComboBox<String> comboBox, List<?> list) {
		// Clear combobox
		comboBox.getItems().clear();
		
		if (list.size() == 0) {
			return;
		}
		
		// Add items from list
		int size = list.size();
		List<String> newList = new ArrayList<String>();
		for (int i = 0; i < size; i++) {
			newList.add(list.get(i).toString());
		}
		comboBox.getItems().addAll(newList);
	}
}
