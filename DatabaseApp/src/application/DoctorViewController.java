package application;

import java.sql.SQLException;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;

public class DoctorViewController extends Main {

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

	public AnchorPane panePatients;

	public AnchorPane paneProcedures;

	public AnchorPane paneInvoices;

	public AnchorPane paneMedications;

	public AnchorPane paneInformation;
	public Label labelFirstname;
	public Label labelLastname;
	public Label labelPhone;
	public Label labelEmail;
	public Label labelSalary;

	public void onButtonAppointmentsClick() {
		hide();
		paneAppointments.setVisible(true);
	}

	public void onButtonPatientsClick() {
		hide();
		panePatients.setVisible(true);
	}

	public void onButtonProceduresClick() {
		hide();
		paneProcedures.setVisible(true);
	}

	public void onButtonInvoicesClick() {
		hide();
		paneInvoices.setVisible(true);
	}

	public void onButtonMedicationsClick() {
		hide();
		paneMedications.setVisible(true);
	}

	public void onButtonInformationClick() {
		hide();
		paneInformation.setVisible(true);
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
		panePatients.setVisible(false);
		paneProcedures.setVisible(false);
		paneInvoices.setVisible(false);
		paneMedications.setVisible(false);
		paneInformation.setVisible(false);
	}
}
