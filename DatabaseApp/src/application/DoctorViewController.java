package application;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import business.Doctor;
import business.Patient;
import business.Type;
import hospital.HospitalDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class DoctorViewController extends Main {

	private HospitalDatabase hdb = new HospitalDatabase();

	public MenuBar menuBar;
	public Menu menuFile;
	public MenuItem menuItemLogout;
	public MenuItem menuItemClose;

	public AnchorPane paneMenu;
	public Button buttonAppointments;
	public Button buttonPatients;
	public Button buttonProcedures;
	public Button buttonInvoices;
	public Button buttonMedications;
	public Button buttonInformation;

	public AnchorPane paneAppointments;
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

	public AnchorPane paneInvoices;

	public AnchorPane paneMedications;

	public AnchorPane paneInformation;
	public Label labelFirstname;
	public Label labelLastname;
	public Label labelPhone;
	public Label labelEmail;
	public Label labelSalary;

	public void onAppointmentsClick() {
		hide();
		paneAppointments.setVisible(true);

		ObservableList<String> upcoming = FXCollections
				.observableArrayList(hdb.getUpcomingAppointments(userId, Type.Doctor, Type.Patient));
		ObservableList<String> past = FXCollections
				.observableArrayList(hdb.getPastAppointments(userId, Type.Doctor, Type.Patient));

		displayList(listUpcomingAppointments, upcoming);
		displayList(listPastAppointments, past);
	}

	public void onPatientsClick() {
		hide();
		panePatients.setVisible(true);

		setData(comboBoxPatients, hdb.getAllUsers(Type.Patient));
	}

	public void onPatientSelect() {
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

	public void onPatientUpdateClick() {
		String patientText = comboBoxPatients.getValue();
		patientText = patientText.substring(0, patientText.indexOf('.'));

		Patient patient = new Patient(Integer.parseInt(patientText), textFieldPatientFirstname.getText(),
				textFieldPatientLastname.getText(), textFieldPatientPhone.getText(), textFieldPatientEmail.getText());
		patient.setNotes(textAreaPatientNotes.getText());

		hdb.updateUser(patient);
	}

	public void onProceduresClick() {
		hide();
		paneProcedures.setVisible(true);
		
		
	}

	public void onInvoicesClick() {
		hide();
		paneInvoices.setVisible(true);
	}

	public void onMedicationsClick() {
		hide();
		paneMedications.setVisible(true);
	}

	public void onInformationClick() {
		hide();
		paneInformation.setVisible(true);

		Doctor user = (Doctor) hdb.getUserInfo(userId);

		labelFirstname.setText("Firstname: " + user.getFirstname());
		labelLastname.setText("Lastname: " + user.getLastname());
		labelPhone.setText("Phone: " + user.getPhone());
		labelEmail.setText("Email: " + user.getEmail());
		labelSalary.setText("Salary: $" + user.getSalary());
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

	private void displayList(ListView<String> listView, ObservableList<String> list) {
		listView.setItems(list);
	}

	private void hide() {
		paneAppointments.setVisible(false);
		panePatients.setVisible(false);
		paneProcedures.setVisible(false);
		paneInvoices.setVisible(false);
		paneMedications.setVisible(false);
		paneInformation.setVisible(false);
	}

	private void setData(ComboBox<String> comboBox, List<?> list) {
		// Clear combobox
		comboBox.getItems().clear();
		// Add items from list
		int size = list.size();
		List<String> newList = new ArrayList<String>();
		for (int i = 0; i < size; i++) {
			newList.add(list.get(i).toString());
		}
		comboBox.getItems().addAll(newList);
	}
}
