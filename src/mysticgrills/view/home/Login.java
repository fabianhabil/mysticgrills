package mysticgrills.view.home;

import java.util.Optional;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mysticgrills.controller.UserController;

public class Login extends BorderPane {
	
	private UserController userController = new UserController();

	public Login(Stage stage) {
		// TODO Auto-generated constructor stub
		VBox container = new VBox();
		Label title = new Label("Login");
		TextField emailField = new TextField();
		PasswordField passField = new PasswordField();
		
		Label email = new Label("Email");
		Label pass = new Label("Password");
		Button loginButton = new Button("Login");
		Label changePage = new Label("Register");
		
		container.getChildren().addAll(title, email, emailField, pass, passField, loginButton, changePage);
		
		loginButton.setOnMouseClicked(e -> {
			String status = userController.authenticateUser(emailField.getText(), passField.getText());
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Message");
			
			System.out.println(status);
			if(status.contains("success")) {
				alert.setHeaderText("Success");
				alert.setContentText(status);
				Optional<ButtonType> result = alert.showAndWait();
				if (result.isPresent() && result.get() == ButtonType.OK) {
					System.out.println("The user clicked OK");
				}
			} else {
				alert.setHeaderText("Error message");
				alert.setContentText(status);
				Optional<ButtonType> result = alert.showAndWait();
				if (result.isPresent() && result.get() == ButtonType.OK) {
					System.out.println("The user clicked OK");
				}
			}
		});
		
		setCenter(container);
		
		changePage.setOnMouseClicked(e -> {
			stage.setScene(new Scene(new Register(stage), 1366, 768));
		});
		
	}

}
