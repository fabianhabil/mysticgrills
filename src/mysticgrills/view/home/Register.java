package mysticgrills.view.home;

import java.util.Optional;

import javafx.geometry.Insets;
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
import mysticgrills.GlobalState;
import mysticgrills.controller.UserController;
import mysticgrills.view.user.ManageUser;

public class Register extends BorderPane {

	private UserController userController = UserController.getUC();
	private GlobalState gb = GlobalState.getInstance();

	public Register(Stage stage) {
		VBox container = new VBox();
		Label title = new Label("Register");
		TextField usernameField = new TextField();
		TextField emailField = new TextField();
		PasswordField passField = new PasswordField();
		PasswordField passConfirmField = new PasswordField();

		Label username = new Label("Username");
		Label email = new Label("Email");
		Label pass = new Label("Password");
		Label passConfirm = new Label("Password Confirmation");
		Button registerButton = new Button("Register");
		Label changePage = new Label("Already have an account? Login");

		container.getChildren().addAll(title, username, usernameField, email, emailField, pass, passField, passConfirm,
				passConfirmField, registerButton, changePage);
		container.setPadding(new Insets(10));

		registerButton.setOnMouseClicked(e -> {
			String status = userController.createUser("user", usernameField.getText(), emailField.getText(),
					passField.getText(), passConfirmField.getText());

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Message");

			if (status.contains("successfully")) {
				alert.setHeaderText("Success");
				alert.setContentText(status);
				Optional<ButtonType> result = alert.showAndWait();
				if (result.isPresent() && result.get() == ButtonType.OK) {
					System.out.println("The user clicked OK");
					stage.setScene(new Scene(new Login(stage), 1366, 768));
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

		changePage.setOnMouseClicked(e -> {
			stage.setScene(new Scene(new Login(stage), 1366, 768));
		});

		setCenter(container);
	}

}
