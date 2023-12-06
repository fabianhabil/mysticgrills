package mysticgrills.view.home;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import mysticgrills.controller.UserController;

public class Register extends BorderPane {
	
	Scene sc;
	UserController userController = new UserController();


	public Register() {
		// TODO Auto-generated constructor stub
		VBox container = new VBox();
		Label title = new Label("Register");
		TextField usernameField = new TextField();
		TextField emailField = new TextField();
		PasswordField passField = new PasswordField();
		PasswordField passConfirmField = new PasswordField();
		
		Label username = new Label("Username");
		Label email = new Label("Email");
		Label pass = new Label("Password");
		Label passConfirm = new Label ("Password Confirmation");
		Button registerButton = new Button("Register");
		Label changePage = new Label("Already have an account? Login");
		
		
		container.getChildren().addAll(title, username, usernameField, email, emailField, pass, passField, passConfirm, passConfirmField, registerButton, changePage);
		
		registerButton.setOnMouseClicked(e -> {
			String status = userController.createUser("user",  usernameField.getText(), emailField.getText(), passField.getText(), passConfirmField.getText());
			System.out.println(emailField.getText());
			System.out.println(status);
		});
		
		setCenter(container);
	}

}
