package mysticgrills.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import mysticgrills.controller.UserController;
import mysticgrills.utils.Dialog;

public class Register extends BorderPane {

	private UserController userController = UserController.getUC();

	VBox container, usernameBox, emailBox, passwordBox;

	Label title, username, email, pass, passConfirm, changePage;
	TextField usernameField, emailField;
	PasswordField passField, passConfirmField;
	Button registerButton;
	Dialog dg;

	public void initialize() {
		container = new VBox(16);
		usernameBox = new VBox(4);
		emailBox = new VBox(4);
		passwordBox = new VBox(4);

		usernameField = new TextField();
		emailField = new TextField();
		passField = new PasswordField();
		passConfirmField = new PasswordField();

		title = new Label("MysticGrills Register");
		username = new Label("Username");
		email = new Label("Email");
		pass = new Label("Password");
		passConfirm = new Label("Password Confirmation");
		changePage = new Label("Already have an account? Login");

		registerButton = new Button("Register");
		dg = new Dialog();
	}

	public void style() {
		title.setFont(new Font(24));
		registerButton.setMinWidth(180);

		// Username Container
		usernameBox.setMaxWidth(300);
		usernameBox.getChildren().addAll(username, usernameField);

		// Email Container
		emailBox.setMaxWidth(300);
		emailBox.getChildren().addAll(email, emailField);

		// Password Container
		passwordBox.setMaxWidth(300);
		passwordBox.getChildren().addAll(pass, passField, passConfirm, passConfirmField);

		// Page Container
		container.getChildren().addAll(title, usernameBox, emailBox, passwordBox, registerButton, changePage);
		container.setAlignment(Pos.CENTER);
	}

	public void listenerFunction(Stage stage) {
		// Register Button Functional
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
		style();
		setCenter(container);
		listenerFunction(stage);
	}
}
