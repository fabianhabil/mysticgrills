package mysticgrills.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mysticgrills.controller.UserController;
import mysticgrills.utils.Dialog;

public class Register extends BorderPane {

	private UserController userController = UserController.getUC();

	VBox container;
	Label title, username, email, pass, passConfirm, changePage;
	TextField usernameField, emailField;
	PasswordField passField, passConfirmField;
	Button registerButton;
	Dialog dg;

	public void initialize() {
		container = new VBox();
		title = new Label("Register");
		usernameField = new TextField();
		emailField = new TextField();
		passField = new PasswordField();
		passConfirmField = new PasswordField();

		username = new Label("Username");
		email = new Label("Email");
		pass = new Label("Password");
		passConfirm = new Label("Password Confirmation");
		registerButton = new Button("Register");
		changePage = new Label("Already have an account? Login");
		dg = new Dialog();

		container.getChildren().addAll(title, username, usernameField, email, emailField, pass, passField, passConfirm,
				passConfirmField, registerButton, changePage);
		container.setPadding(new Insets(10));
	}

	public void listenerFunction(Stage stage) {
		registerButton.setOnMouseClicked(e -> {
			String status = userController.createUser("user", usernameField.getText(), emailField.getText(),
					passField.getText(), passConfirmField.getText());

			if (status.contains("successfully")) {
				if (dg.informationDialog("Message", "Success", status)) {
					System.out.println("The user clicked OK");
					stage.setScene(new Scene(new Login(stage), 1366, 768));
				}
			} else {
				dg.informationDialog("Error", "Error Message", status);
			}
		});

		changePage.setOnMouseClicked(e -> {
			stage.setScene(new Scene(new Login(stage), 1366, 768));
		});
	}

	public Register(Stage stage) {
		stage.setTitle("Register");
		initialize();
		listenerFunction(stage);
		setCenter(container);
	}

}
