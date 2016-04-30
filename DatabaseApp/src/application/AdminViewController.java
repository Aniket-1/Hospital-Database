package application;

import java.sql.SQLException;
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
	public ComboBox<User> comboBoxDoctorId;
	public TextField textFieldRaiseAmount;
	public Button buttonUpdateDoctor;

	public AnchorPane paneAppointments;
	public Button buttonBookAppointment;
	public Button buttonCancelAppointment;

	public AnchorPane paneBookAppointment;
	public ComboBox<User> comboBoxPatient;
	public ComboBox<User> comboBoxDoctor;
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
		
		setData(comboBoxDoctorId, hdb.getUser(Type.Doctor));
	}

	public void onButtonAppointmentsClick() {
		hide();
		paneAppointments.setVisible(true);
	}

	public void onButtonNotificationsClick() {
		hide();
		paneNotifications.setVisible(true);
	}

	public void onButtonInformationClick() {
		hide();
		paneInformation.setVisible(true);
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

		User user = new Doctor(0,firstname, lastname, phone, email, salary);

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
		int doctor = comboBoxDoctorId.getValue().getId();
		Float raise = 0f;
		try {
			raise = Float.parseFloat(textFieldRaiseAmount.getText());
		} catch (NumberFormatException e) {
			System.out.println("Could not raise doctor's salary");
			return;
		}
		
		hdb.raiseDoctorSalary(doctor, raise);
	}

	public void onButtonBookAppointmentClick() {
		paneBookAppointment.setVisible(true);
		paneCancelAppointment.setVisible(false);
	}

	public void onButtonBookClick() {
	}

	public void onButtonCancelAppointmentClick() {
		paneBookAppointment.setVisible(false);
		paneCancelAppointment.setVisible(true);
	}

	public void onButtonCancelClick() {
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

	private void setData(ComboBox<User> comboBox, List<User> list) {
		// Clear combobox
		comboBox.getItems().clear();
		// Add items from list
		comboBox.getItems().addAll(list);
	}
}
