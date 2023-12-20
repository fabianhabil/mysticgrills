package mysticgrills.view.user;

import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import mysticgrills.controller.UserController;
import mysticgrills.model.User;
import mysticgrills.utils.Dialog;
import mysticgrills.view.home.AdminHome;

public class ManageUser extends BorderPane {

	VBox container, titleContainer, buttonContainer;
	ScrollPane containerTable;
	TableView<User> viewUser;
	ArrayList<User> users;
	Button updateButton, deleteButton, backButton;
	GridPane formBox;
	HBox buttonBox;
	TextField nameTextField, emailTextField;
	Label roleLbl, nameLbl, emailLbl;
	ComboBox<String> role;
	Integer tempId;
	Dialog dg;
	Text title;

	private UserController userController = UserController.getUC();

	public void initialize() {
		containerTable = new ScrollPane();
		formBox = new GridPane();
		nameLbl = new Label("Name");
		emailLbl = new Label("Email");
		roleLbl = new Label("Role");

		nameTextField = new TextField();
		emailTextField = new TextField();
		role = new ComboBox<>();

		container = new VBox(16);
		titleContainer = new VBox();
		buttonBox = new HBox(8);
		buttonContainer = new VBox(8);

		updateButton = new Button("Update");
		deleteButton = new Button("Delete");
		backButton = new Button("Back");

		title = new Text("Manage User");

		viewUser = new TableView<User>();
		users = new ArrayList<User>();
		dg = new Dialog();

	}

	public void style() {
		title.setFont(new Font(24));
		title.setTextAlignment(TextAlignment.CENTER);
		titleContainer.getChildren().add(title);
		titleContainer.setAlignment(Pos.CENTER);

		nameTextField.setEditable(false);
		emailTextField.setEditable(false);

		viewUser.setMaxHeight(800);
		viewUser.setPadding(new Insets(10, 10, 10, 10));

		role.getItems().addAll("Admin", "Cashier", "Chef", "Waiter", "Customer");

		formBox.setPadding(new Insets(10, 10, 10, 10));
		formBox.add(nameLbl, 0, 0);
		formBox.add(emailLbl, 0, 1);
		formBox.add(roleLbl, 0, 2);
		formBox.setHgap(10);
		formBox.add(nameTextField, 1, 0);
		formBox.add(emailTextField, 1, 1);
		formBox.add(role, 1, 2);
		formBox.setAlignment(Pos.CENTER);

		buttonBox.getChildren().addAll(updateButton, deleteButton);
		buttonBox.setAlignment(Pos.CENTER);

		containerTable.setContent(viewUser);
		containerTable.setHbarPolicy(ScrollBarPolicy.NEVER);
		containerTable.setFitToWidth(true);

		buttonContainer.getChildren().setAll(buttonBox, backButton);
		buttonContainer.setAlignment(Pos.CENTER);

		container.getChildren().setAll(titleContainer, containerTable, formBox, buttonContainer);
		container.setPadding(new Insets(8));

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

		refreshTable();
	}

	public void refreshTable() {
		users = userController.getAllUser();
		viewUser.getItems().clear();
		viewUser.getItems().addAll(users);
		viewUser.setOnMouseClicked(tableMouseEvent());
	}

	// to handle if user press a row in table
	private EventHandler<MouseEvent> tableMouseEvent() {
		return new EventHandler<MouseEvent>() {

			// get user data from pressed table row
			@Override
			public void handle(MouseEvent event) {
				TableSelectionModel<User> tableSelectionModel = viewUser.getSelectionModel();
				tableSelectionModel.setSelectionMode(SelectionMode.SINGLE);
				// get data from selected model
				User user = tableSelectionModel.getSelectedItem();

				// display data in textfield and combobox
				if (user != null) {
					nameTextField.setText(user.getUserName());
					emailTextField.setText(user.getUserEmail());
					role.getSelectionModel().select(user.getUserRole());

					tempId = user.getUserId();
				}

			}
		};

	}

	public void deleteFunction() {
		if (tempId == null) {
			dg.informationDialog("Error", "Error Message", "Please select an user first!");
			return;
		}

		if (dg.confirmationDialog("Confirm", "Confirm Message", "Are you sure want to delete this?")) {
			Boolean status = userController.deleteUser(tempId);
			if (status) {
				if (dg.informationDialog("Success", "Success", "Account has been deleted!")) {
					refreshTable();
					nameTextField.clear();
					emailTextField.clear();
					role.getSelectionModel().clearSelection();
				}
			} else {
				dg.informationDialog("Error Message", "Error to delete", "Error to delete from DB");
			}

		}
	}

	public void updateFunction() {
		if (tempId == null) {
			dg.informationDialog("Error", "Error Message", "Please select an user first!");
			return;
		}

		if (dg.confirmationDialog("Confirm", "Confirm Message", "Are you sure want to update?")) {
			Boolean status = userController.updateUser(tempId, role.getSelectionModel().getSelectedItem());
			if (status) {
				if (dg.informationDialog("Success", "Success Message", "Account has been updated!")) {
					refreshTable();
					nameTextField.clear();
					emailTextField.clear();
					role.getSelectionModel().clearSelection();
				}
			} else {
				dg.informationDialog("Error", "Error Message", "Fail to update user");
			}
		}
	}

	public void eventListener(Stage stage) {
		deleteButton.setOnMouseClicked(e -> {
			deleteFunction();
		});

		updateButton.setOnMouseClicked(e -> {
			updateFunction();
		});

		backButton.setOnMouseClicked(e -> {
			stage.setScene(new Scene(new AdminHome(stage), 1366, 768));
		});
	}

	public ManageUser(Stage stage) {
		stage.setTitle("Manage User");
		initialize();
		style();
		setCenter(container);
		eventListener(stage);

	}

}
