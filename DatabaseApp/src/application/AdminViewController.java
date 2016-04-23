package application;

import java.sql.SQLException;

import business.*;
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

public class AdminViewController {

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
	public TextField textFieldDoctorId;
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
		HospitalSecurity hs = new HospitalSecurity();

		String firstname = textFieldAdminFirstname.getText();
		String lastname = textFieldAdminLastname.getText();
		String phone = textFieldAdminPhone.getText();
		String email = textFieldAdminEmail.getText();
		Float salary = 0f;
		try {
			salary = Float.parseFloat(textFieldAdminSalary.getText());
		} catch (NumberFormatException e) {
			System.out.println("Could not create user");
			return;
		}
		String username = textFieldAdminUsername.getText();
		String password = passwordFieldAdminPassword.getText();

		User user = new Admin(firstname, lastname, phone, email, salary);
		hs.newUser(user, username, password);
	}

	public void onButtonCreateDoctorClick() {
		HospitalSecurity hs = new HospitalSecurity();

		String firstname = textFieldDoctorFirstname.getText();
		String lastname = textFieldDoctorLastname.getText();
		String phone = textFieldDoctorPhone.getText();
		String email = textFieldDoctorEmail.getText();
		Float salary = 0f;
		try {
			salary = Float.parseFloat(textFieldDoctorSalary.getText());
		} catch (NumberFormatException e) {
			System.out.println("Could not create user");
			return;
		}
		String username = textFieldDoctorUsername.getText();
		String password = passwordFieldDoctorPassword.getText();

		User user = new Doctor(firstname, lastname, phone, email, salary);
		hs.newUser(user, username, password);
	}

	public void onButtonCreatePatientClick() {
		HospitalSecurity hs = new HospitalSecurity();

		String firstname = textFieldPatientFirstname.getText();
		String lastname = textFieldPatientLastname.getText();
		String phone = textFieldPatientPhone.getText();
		String email = textFieldPatientEmail.getText();
		int doctor = 0;
		try {
			doctor = Integer.parseInt(textFieldPatientDoctor.getText());
		} catch (NumberFormatException e) {
			System.out.println("Could not create user");
			return;
		}
		String username = textFieldPatientUsername.getText();
		String password = passwordFieldPatientPassword.getText();

		User user = new Patient(firstname, lastname, phone, email, doctor);
		hs.newUser(user, username, password);
	}

	public void onButtonUpdateDoctorClick() {
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

	}

	private void hide() {
		paneCreateUser.setVisible(false);
		paneDoctors.setVisible(false);
		paneAppointments.setVisible(false);
		paneNotifications.setVisible(false);
		paneInformation.setVisible(false);
	}
}
