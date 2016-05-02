package application;

import java.time.LocalDate;

import business.Doctor;
import business.Patient;
import business.Type;
import hospital.HospitalDatabase;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class DoctorViewController extends ViewController {

	private HospitalDatabase hdb = new HospitalDatabase();

	public MenuBar menuBar;
	public Menu menuFile;
	public MenuItem menuItemLogout;
	public MenuItem menuItemClose;

	public AnchorPane paneMenu;
	public Button buttonViewAppointments;
	public Button buttonPatients;
	public Button buttonProcedures;
	public Button buttonMedications;
	public Button buttonInformation;
	public Button buttonAppointments;

	public AnchorPane paneViewAppointments;
	public ListView<String> listUpcomingAppointments;
	public ListView<String> listPastAppointments;

	public AnchorPane panePatients;
	public ComboBox<String> comboBoxPatients;
	public TextField textFieldPatientFirstname;
	public TextField textFieldPatientLastname;
	public TextField textFieldPatientPhone;
	public TextField textFieldPatientEmail;
	public TextArea textAreaPatientNotes;
	public Button buttonUpdatePatient;

	public AnchorPane paneProcedures;
	public ComboBox<String> comboBoxDoctorPatients;
	public ComboBox<String> comboBoxProcedures;
	public Button buttonPerformProcedure;

	public AnchorPane paneMedications;
	public ComboBox<String> comboBoxMedicationPatient;
	public ComboBox<String> comboBoxMedications;
	public Button buttonPrescribeMedication;

	public AnchorPane paneInformation;
	public TextField textFieldFirstname;
	public TextField textFieldLastname;
	public TextField textFieldPhone;
	public TextField textFieldEmail;
	public TextField textFieldSalary;

	public AnchorPane paneAppointments;
	public Button buttonBookAppointment;
	public Button buttonCancelAppointment;

	public AnchorPane paneBookAppointment;
	public ComboBox<String> comboBoxPatient;
	public DatePicker datePickerDate;
	public TextArea textAreaNotes;
	public Button buttonBook;

	public AnchorPane paneCancelAppointment;
	public ComboBox<String> comboBoxAppointment;
	public Button buttonCancel;

	public void onButtonViewAppointmentsClick() {
		hide();
		paneViewAppointments.setVisible(true);

		displayList(listUpcomingAppointments,
				FXCollections.observableArrayList(hdb.getUpcomingAppointments(userId, Type.Doctor, Type.Patient)));
		displayList(listPastAppointments,
				FXCollections.observableArrayList(hdb.getPastAppointments(userId, Type.Doctor, Type.Patient)));
	}

	public void onButtonPatientsClick() {
		hide();
		panePatients.setVisible(true);

		setData(comboBoxPatients, hdb.getAllUsers(Type.Patient));
	}

	public void onButtonPatientSelect() {
		String patientText = comboBoxPatients.getValue();
		patientText = patientText.substring(0, patientText.indexOf('.'));
		int patient = Integer.parseInt(patientText);

		Patient user = (Patient) hdb.getUserInfo(patient);

		textFieldPatientFirstname.setText(user.getFirstname());
		textFieldPatientLastname.setText(user.getLastname());
		textFieldPatientPhone.setText(user.getPhone());
		textFieldPatientEmail.setText(user.getEmail());
		textAreaPatientNotes.setText(user.getNotes());
	}

	public void onButtonPatientUpdateClick() {
		String patientText = comboBoxPatients.getValue();
		patientText = patientText.substring(0, patientText.indexOf('.'));

		Patient patient = new Patient(Integer.parseInt(patientText), textFieldPatientFirstname.getText(),
				textFieldPatientLastname.getText(), textFieldPatientPhone.getText(), textFieldPatientEmail.getText());
		patient.setNotes(textAreaPatientNotes.getText());

		hdb.updateUser(patient);
	}

	public void onButtonProceduresClick() {
		hide();
		paneProcedures.setVisible(true);

		setData(comboBoxDoctorPatients, hdb.getAllUsers(Type.Patient));
		setData(comboBoxProcedures, hdb.getAllProcedures());
	}

	public void onButtonPerformProcedureClick() {
		String patientText = comboBoxDoctorPatients.getValue();
		patientText = patientText.substring(0, patientText.indexOf('.'));
		int patient = Integer.parseInt(patientText);

		String procedureText = comboBoxProcedures.getValue();
		procedureText = procedureText.substring(0, procedureText.indexOf('.'));
		int procedure = Integer.parseInt(procedureText);

		hdb.performProcedure(patient, procedure, userId);
	}

	public void onButtonMedicationsClick() {
		hide();
		paneMedications.setVisible(true);

		setData(comboBoxMedicationPatient, hdb.getAllUsers(Type.Patient));
		setData(comboBoxMedications, hdb.getMedications());
	}

	public void onButtonPrescribeMedicationClick() {
		String patientText = comboBoxMedicationPatient.getValue();
		patientText = patientText.substring(0, patientText.indexOf('.'));
		int patient = Integer.parseInt(patientText);

		String medicationText = comboBoxMedications.getValue();
		medicationText = medicationText.substring(0, medicationText.indexOf('.'));
		int medication = Integer.parseInt(medicationText);

		hdb.prescribeMedication(patient, medication, userId);
	}

	public void onButtonInformationClick() {
		hide();
		paneInformation.setVisible(true);

		Doctor user = (Doctor) hdb.getUserInfo(userId);

		textFieldFirstname.setText(user.getFirstname());
		textFieldLastname.setText(user.getLastname());
		textFieldPhone.setText(user.getPhone());
		textFieldEmail.setText(user.getEmail());
		textFieldSalary.setText("$" + user.getSalary());
	}

	public void onButtonAppointmentsClick() {
		hide();
		paneAppointments.setVisible(true);
		paneBookAppointment.setVisible(false);
		paneCancelAppointment.setVisible(false);
	}

	public void onButtonBookAppointmentClick() {
		paneBookAppointment.setVisible(true);
		paneCancelAppointment.setVisible(false);

		setData(comboBoxPatient, hdb.getAllUsers(Type.Patient));
	}

	public void onButtonBookClick() {
		String patientText = comboBoxPatient.getValue();

		patientText = patientText.substring(0, patientText.indexOf('.'));

		int patient = Integer.parseInt(patientText);

		LocalDate date = datePickerDate.getValue();

		String notes = textAreaNotes.getText();

		hdb.bookAppointment(patient, userId, date, notes);
	}

	public void onButtonCancelAppointmentClick() {
		paneBookAppointment.setVisible(false);
		paneCancelAppointment.setVisible(true);

		setData(comboBoxAppointment, hdb.getAppointments(userId));
	}

	public void onButtonCancelClick() {
		String appointmentText = comboBoxAppointment.getValue();
		appointmentText = appointmentText.substring(0, appointmentText.indexOf('.'));

		int appointment = Integer.parseInt(appointmentText);

		hdb.cancelAppointment(appointment);
	}

	@Override
	void hide() {
		paneViewAppointments.setVisible(false);
		panePatients.setVisible(false);
		paneProcedures.setVisible(false);
		paneMedications.setVisible(false);
		paneInformation.setVisible(false);
		paneAppointments.setVisible(false);
	}
}
