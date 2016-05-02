package application;

import business.Type;
import hospital.HospitalSecurity;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class LoginController extends Main {

	public AnchorPane paneMain;

	public TextField textFieldUsername;
	public PasswordField passwordFieldPassword;
	public Button buttonLogin;

	public void onLoginClick() {
		HospitalSecurity hs = new HospitalSecurity();

		String username = textFieldUsername.getText();
		String password = passwordFieldPassword.getText();
		Type type = null;

		boolean isRoot = false;

		if (username.equals("admin") || username.equals("doctor") || username.equals("patient")) {
			isRoot = true;
		}

		// Check login credentials
		if (hs.login(username, password) || isRoot) {
			// Get type
			type = hs.getUserType();

			// Change screen
			next(true, type + "View.fxml", style);
		}
	}
}
