package application;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import business.Patient;
import business.Type;
import hospital.HospitalDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class PatientViewController extends Main {

	private HospitalDatabase hdb = new HospitalDatabase();

	public MenuBar menuBar;
	public Menu menuFile;
	public MenuItem menuItemLogout;
	public MenuItem menuItemClose;

	public AnchorPane paneMenu;
	public Button buttonAppointments;
	public Button buttonPrescriptions;
	public Button buttonProcedures;
	public Button buttonInvoices;
	public Button buttonInformation;
	public Button buttonBookAppointment;

	public AnchorPane paneAppointments;
	public ListView<String> listUpcomingAppointments;
	public ListView<String> listPastAppointments;

	public AnchorPane panePrescriptions;
	public ListView<String> listPrescriptions;

	public AnchorPane paneProcedures;
	public ListView<String> listProcedures;

	public AnchorPane paneInvoices;
	public ListView<String> listInvoices;

	public AnchorPane paneInformation;
	public TextField textFieldFirstname;
	public TextField textFieldLastname;
	public TextField textFieldPhone;
	public TextField textFieldEmail;
	public TextArea textAreaNotes;
	
	public AnchorPane paneBookAppointment;
	public ComboBox<String> comboBoxFamilyDoctor;
	public ComboBox<String> comboBoxSpecialist;
	public DatePicker datePickerDate;
	public Button buttonBook;

	public void onAppointmentsClick() {
		hide();
		paneAppointments.setVisible(true);

		ObservableList<String> upcoming = FXCollections
				.observableArrayList(hdb.getUpcomingAppointments(userId, Type.Patient, Type.Doctor));
		ObservableList<String> past = FXCollections
				.observableArrayList(hdb.getPastAppointments(userId, Type.Patient, Type.Doctor));

		displayList(listUpcomingAppointments, upcoming);
		displayList(listPastAppointments, past);
	}

	public void onPrescriptionsClick() {
		hide();
		panePrescriptions.setVisible(true);

		ObservableList<String> results = FXCollections.observableArrayList(hdb.getPatientPrescriptions(userId));
		displayList(listPrescriptions, results);
	}

	public void onPorceduresClick() {
		hide();
		paneProcedures.setVisible(true);

		ObservableList<String> results = FXCollections.observableArrayList(hdb.getPatientProcedures(userId));
		displayList(listProcedures, results);
	}

	public void onInvoicesClick() {
		hide();
		paneInvoices.setVisible(true);

		ObservableList<String> results = FXCollections.observableArrayList(hdb.getPatientInvoices(userId));
		displayList(listInvoices, results);
	}

	public void onInformationClick() {
		hide();
		paneInformation.setVisible(true);

		Patient user = (Patient) hdb.getUserInfo(userId);

		textFieldFirstname.setText(user.getFirstname());
		textFieldLastname.setText(user.getLastname());
		textFieldPhone.setText(user.getPhone());
		textFieldEmail.setText(user.getEmail());
		textAreaNotes.setText(user.getNotes());
	}
	
	public void onBookAppointmentClick() {
		hide();
		paneBookAppointment.setVisible(true);
		
		setData(comboBoxSpecialist, hdb.getSpecialists());
		setData(comboBoxFamilyDoctor, hdb.getFamilyDoctors(userId));
	}
	
	public void onBookClick() {
		String doctorText = comboBoxFamilyDoctor.getValue();
		if (doctorText == null || doctorText.equals("")) {
			doctorText = comboBoxSpecialist.getValue();
		}
		
		doctorText = doctorText.substring(0, doctorText.indexOf('.'));
		int doctor = Integer.parseInt(doctorText);
		LocalDate date = datePickerDate.getValue();
		
		hdb.bookAppointment(userId, doctor, date, null);
	}

	private void displayList(ListView<String> listView, ObservableList<String> list) {
		listView.setItems(list);
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
		paneAppointments.setVisible(false);
		panePrescriptions.setVisible(false);
		paneProcedures.setVisible(false);
		paneInvoices.setVisible(false);
		paneInformation.setVisible(false);
		paneBookAppointment.setVisible(false);
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
