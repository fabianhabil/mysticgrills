package mysticgrills.view;

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
import mysticgrills.GlobalState;
import mysticgrills.controller.UserController;
import mysticgrills.model.User;
import mysticgrills.utils.Dialog;
import mysticgrills.view.home.AdminHome;
import mysticgrills.view.home.CashierHome;
import mysticgrills.view.home.CustomerHome;
import mysticgrills.view.home.KitchenHome;

public class Login extends BorderPane {

	private UserController userController = UserController.getUC();
	private GlobalState gs = GlobalState.getInstance();

	VBox container;
	VBox emailBox;
	VBox passwordBox;
	Label title, email, pass, changePage;
	TextField emailField;
	PasswordField passField;
	Button loginButton;
	Dialog dg;

	// Initialize all component
	public void initialize() {
		container = new VBox(16);
		emailBox = new VBox(4);
		passwordBox = new VBox(4);

		emailField = new TextField();
		passField = new PasswordField();

		title = new Label("MysticGrills Login");
		email = new Label("Email");
		pass = new Label("Password");

		loginButton = new Button("Login");
		changePage = new Label("Dont have any account? Register");
		dg = new Dialog();

	}

	public void style() {
		title.setFont(new Font(24));
		loginButton.setMinWidth(180);

		// Email Container
		emailBox.setMaxWidth(300);
		emailBox.getChildren().addAll(email, emailField);

		// Password Container
		passwordBox.setMaxWidth(300);
		passwordBox.getChildren().addAll(pass, passField);

		// Page Container
		container.getChildren().addAll(title, emailBox, passwordBox, loginButton, changePage);
		container.setAlignment(Pos.CENTER);
	}

	public void listenerFunction(Stage stage) {
		// Login Button Functional
		loginButton.setOnMouseClicked(e -> {
			String status = userController.authenticateUser(emailField.getText(), passField.getText());

			if (status.contains("Success")) {
				if (dg.informationDialog("Success", "Success", status)) {
					redirectUser(stage);
				}
			} else {
				dg.informationDialog("Fail", "Fail", status);
			}
		});

		changePage.setOnMouseClicked(e -> {
			stage.setScene(new Scene(new Register(stage), 1366, 768));
		});
	}

	// User logged in and redirect user to the specified homepage according to their
	// role
	public void redirectUser(Stage stage) {
		User user = gs.getCurrentLoggedInUser();
		BorderPane bp;

		if (user.getUserRole().equals("Customer")) {
			bp = new CustomerHome(stage);
		}

		else if (user.getUserRole().equals("Admin")) {
			bp = new AdminHome(stage);
		}

		else if (user.getUserRole().equals("Chef") || user.getUserRole().equals("Waiter")) {
			bp = new KitchenHome(stage);
		}

		else if (user.getUserRole().equals("Cashier")) {
			bp = new CashierHome(stage);
		}

		else {
			bp = new BorderPane();
		}

		stage.setScene(new Scene(bp, 1366, 768));
	}

	public Login(Stage stage) {
		stage.setTitle("Login");
		initialize();
		style();
		setCenter(container);
		listenerFunction(stage);
	}

}
