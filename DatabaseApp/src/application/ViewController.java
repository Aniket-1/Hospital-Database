package application;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;

public abstract class ViewController extends Main {
	
	public void close() {
		logout();
		try {
			Main.db.closeConnection();
		} catch (SQLException e) {
			System.out.println("Could not successfully close connection");
		}
		System.exit(0);
	}
	
	void displayList(ListView<String> listView, ObservableList<String> list) {
		listView.setItems(list);
	}

	abstract void hide();
	
	public void logout() {
		// Change screen
		next(false, "Login.fxml", style);
	}
	
	void setData(ComboBox<String> comboBox, List<?> list) {
		// Clear combobox
		comboBox.getItems().clear();
		
		if (list.size() == 0) {
			return;
		}
		
		// Add items from list
		int size = list.size();
		List<String> newList = new ArrayList<String>();
		for (int i = 0; i < size; i++) {
			newList.add(list.get(i).toString());
		}
		comboBox.getItems().addAll(newList);
	}
}
