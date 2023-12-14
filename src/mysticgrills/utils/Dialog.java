package mysticgrills.utils;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class Dialog {
	
	public Dialog() {
		
	}

	public Boolean informationDialog(String title, String header, String body) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(body);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			System.out.println("The user clicked OK");
			return true;
		}
		return false;
	}
	
	public Boolean confirmationDialog(String title, String header, String body) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(body);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			System.out.println("The user clicked OK");
			return true;
		}
		return false;
	}
}
