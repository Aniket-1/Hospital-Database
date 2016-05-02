package application;

import java.sql.SQLException;

import connector.DatabaseConnector;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;

public class Main extends Application {

	protected static Stage primaryStage;
	protected static Scene scene;

	private static FXMLLoader loader;

	protected static DatabaseConnector db;

	protected static String style = "light.css";
	
	protected static int userId;

	public static void main(String[] args) throws SQLException {

		// Connect to database
		String url = "waldo2.dawsoncollege.qc.ca/cs1430196";
		String username = "CS1430196";
		String password = "truskimp";

		try {
			db = new DatabaseConnector(url, username, password);
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			System.err.println("Could not create connection to database.\nOperation aborted");
			return;
		}

		launch(args);
	}

	/**
	 * Sets the scene of the primary stage to the given resource
	 * 
	 * @param resource
	 *            The fxml document
	 * 
	 */
	public void setScene(String resource) {

		loader = new FXMLLoader(getClass().getResource(resource));
		AnchorPane root = null;
		try {
			root = (AnchorPane) loader.load();
		} catch (Exception e) {
			e.printStackTrace();
		}

		scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Applies the given style sheet to the scene
	 * 
	 * @param stylesheet
	 *            The style sheet
	 */
	public void setStylesheet(String stylesheet) {
		// Add stylesheet
		scene.getStylesheets().add(getClass().getResource(stylesheet).toExternalForm());
	}

	@Override
	public void start(Stage primaryStage) {

		// Set stage
		Main.primaryStage = primaryStage;
		primaryStage.setResizable(false);
		primaryStage.getIcons().add(new Image("http://icons.iconarchive.com/icons/gcds/halloween/64/grim-reaper-icon.png"));

		// Say "Bye" on exit because that's adorable
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				if (db != null) {
					try {
						db.closeConnection();
					} catch (SQLException e) {
						System.out.println("SQLException: " + e.getMessage());
						System.out.println("SQLState: " + e.getSQLState());
						System.out.println("VendorError: " + e.getErrorCode());
					}
				}
				System.out.println("Bye!");
				System.exit(0);
			}
		});

		next(false, "Login.fxml", style);
	}

	/**
	 * Switches between login screen and main screen
	 * 
	 * @param isLogin
	 *            True if the current screen is the login screen. False
	 *            otherwise
	 * @param resource
	 *            The FXML document to be used for the login/main screen
	 * @param stylesheet
	 *            The style sheet to be applied to the scene
	 */
	protected void next(boolean isLogin, String resource, String stylesheet) {

		// Set title, width and height
		String title = "Login";
		int height = 170;
		int width = 300;

		if (isLogin) {
			title = "Hospital";
			height = 420;
			width = 545;
		}

		primaryStage.setTitle(title);
		primaryStage.setMinHeight(height);
		primaryStage.setHeight(height);
		primaryStage.setMinWidth(width);
		primaryStage.setWidth(width);

		//primaryStage.setResizable(isLogin);

		setScene(resource);
		setStylesheet(stylesheet);
	}
}
