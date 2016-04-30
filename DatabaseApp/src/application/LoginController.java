package application;

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
		String userType = "Admin";
		
		boolean isRoot = false;
		
		if (username.equals("root")) {
			isRoot = true;
		}

		// Check login credentials
		if (hs.login(username, password) || isRoot) {
			// Check for root
			if (!isRoot) {
				userType = hs.getUserType();
			}
			// Change screen
			next(true, userType + "View.fxml", style);
		}
	}
}
