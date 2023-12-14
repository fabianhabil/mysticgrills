package mysticgrills.view.home;


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
import mysticgrills.view.user.ManageUser;

public class Login extends BorderPane {

	private UserController userController = UserController.getUC();

	public Login(Stage stage) {
		
		VBox container = new VBox();
		Label title = new Label("Login");
		TextField emailField = new TextField();
		PasswordField passField = new PasswordField();

		Label email = new Label("Email");
		Label pass = new Label("Password");
		Button loginButton = new Button("Login");
		Label changePage = new Label("Register");
		
		Dialog dg = new Dialog();

		container.getChildren().addAll(title, email, emailField, pass, passField, loginButton, changePage);

		loginButton.setOnMouseClicked(e -> {
			String status = userController.authenticateUser(emailField.getText(), passField.getText());

			if (status.contains("success")) {
				if(dg.informationDialog("Success", "Success", status)) {
					stage.setScene(new Scene(new ManageUser(stage), 1366, 768));					
				}
			} else {
				dg.informationDialog("Fail", "Fail", status);
			}
		});

		setCenter(container);

		changePage.setOnMouseClicked(e -> {
			stage.setScene(new Scene(new Register(stage), 1366, 768));
		});

	}

}
