package application;

import java.sql.SQLException;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;

public class PatientViewController {

	public MenuBar menuBar;
	public Menu menuFile;
	public MenuItem menuItemLogout;
	public MenuItem menuItemClose;
	
	public AnchorPane paneMenu;
	public Button buttonAppointments;
	public Button buttonPrescriptions;
	public Button buttonProcedures;
	public Button buttonInvoices;
	public Button buttonNotes;

	public AnchorPane paneAppointments;
	public ListView<String> listAppointments;

	public AnchorPane panePrescriptions;
	public ListView<String> listPrescriptions;

	public AnchorPane paneProcedures;
	public ListView<String> listProcedures;

	public AnchorPane paneInvoices;
	public ListView<String> listInvoices;

	public AnchorPane paneNotes;
	public Label labelNotes;

	public void onAppointmentsClick() {
		hide();
		paneAppointments.setVisible(true);

		String results = null;
		displayList(listAppointments, results);
	}

	public void onPrescriptionsClick() {
		hide();
		panePrescriptions.setVisible(true);

		String results = null;
		displayList(listPrescriptions, results);
	}

	public void onPorceduresClick() {
		hide();
		paneProcedures.setVisible(true);

		String results = null;
		displayList(listProcedures, results);
	}

	public void onInvoicesClick() {
		hide();
		paneInvoices.setVisible(true);

		String results = null;
		displayList(listInvoices, results);
	}

	public void onNotesClick() {
		hide();
		paneNotes.setVisible(true);
		
		String results = null;
		labelNotes.setText(results);
	}
	
	private void displayList(ListView<String> listView, String results) {
		
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
		paneAppointments.setVisible(false);
		panePrescriptions.setVisible(false);
		paneProcedures.setVisible(false);
		paneInvoices.setVisible(false);
		paneNotes.setVisible(false);
	}
}
