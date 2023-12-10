package mysticgrills.view.user;

import java.util.ArrayList;

import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import mysticgrills.controller.UserController;
import mysticgrills.model.User;

public class ManageUser extends BorderPane {

	TableView<User> viewUser;
	ArrayList<User> users;
	Button registerButton;

	private UserController userController = UserController.getUC();

	public void initialize() {
		registerButton = new Button("Register");
		viewUser = new TableView<User>();
		users = new ArrayList<User>();
		setTable();
	}

	public void setTable() {
		TableColumn<User, Integer> idColumn = new TableColumn<User, Integer>("User ID");
		TableColumn<User, String> roleColumn = new TableColumn<User, String>("User Role");
		TableColumn<User, String> nameColumn = new TableColumn<User, String>("User Name");
		TableColumn<User, String> emailColumn = new TableColumn<User, String>("User Email");

		viewUser.getColumns().addAll(idColumn, roleColumn, nameColumn, emailColumn);

		idColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
		roleColumn.setCellValueFactory(new PropertyValueFactory<>("userRole"));
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
		emailColumn.setCellValueFactory(new PropertyValueFactory<>("userEmail"));

		users = userController.getUser();
		viewUser.getItems().clear();
		viewUser.getItems().addAll(users);
	}

	public ManageUser(Stage stage) {
		ScrollPane container = new ScrollPane();
		initialize();
		container.setContent(viewUser);
		container.setHbarPolicy(ScrollBarPolicy.NEVER);
		container.setFitToWidth(true);
		setCenter(container);
		setBottom(registerButton);
	}

}
