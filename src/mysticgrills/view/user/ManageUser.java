package mysticgrills.view.user;

import java.util.ArrayList;
import java.util.Optional;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mysticgrills.controller.UserController;
import mysticgrills.model.User;

public class ManageUser extends BorderPane {

	ScrollPane container;
	TableView<User> viewUser;
	ArrayList<User> users;
	Button updateButton;
	Button deleteButton;
	GridPane formBox;
	HBox buttonBox;
	TextField nameTextField, emailTextField;
	Label roleLbl, nameLbl, emailLbl;
	ComboBox<String> role;
	Integer tempId;

	private UserController userController = UserController.getUC();

	public void initialize() {
		container = new ScrollPane();
		formBox = new GridPane();
		nameLbl = new Label("Name");
		emailLbl = new Label("Email");
		roleLbl = new Label("Role");
		
		nameTextField = new TextField();
		emailTextField = new TextField();
		role = new ComboBox<>();
		
		buttonBox = new HBox();
		updateButton = new Button("Update");
		deleteButton = new Button("Delete");
		viewUser = new TableView<User>();
		users = new ArrayList<User>();
		
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
		
		buttonBox.getChildren().addAll(updateButton, deleteButton);
		buttonBox.setAlignment(Pos.CENTER);
		
		container.setContent(viewUser);
		container.setHbarPolicy(ScrollBarPolicy.NEVER);
		container.setFitToWidth(true);
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
				if(user != null) {
					nameTextField.setText(user.getUserName());
					emailTextField.setText(user.getUserEmail());
					role.getSelectionModel().select(user.getUserRole());
					
					tempId = user.getUserId();
				}
				
			}
		};
		
	}
	
	public void deleteFunction() {
		// confirmation dialog
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Cofirm");
		alert.setHeaderText("Confirm Message");
		alert.setContentText("Are you sure want to delete this?");
		Optional<ButtonType> yes = alert.showAndWait();
		// if user sure want to delete the user acocunt
		if (yes.isPresent() && yes.get() == ButtonType.OK) {
			// delete user from DB
			Boolean status = userController.deleteUser(tempId);
			
			// if user has been deleted refresh the table
			if(status) {
				Alert alert2 = new Alert(AlertType.INFORMATION);
				alert2.setTitle("Success");
				alert2.setHeaderText("Success");
				alert2.setContentText("Account has been deleted!");
				Optional<ButtonType> result = alert2.showAndWait();
				if (result.isPresent() && result.get() == ButtonType.OK) {
					System.out.println("The user clicked OK");
					refreshTable();
					nameTextField.clear();
					emailTextField.clear();
					role.getSelectionModel().clearSelection();
				}	
			// if error delete from the database display error message
			} else {
				Alert alert3 = new Alert(AlertType.INFORMATION);
				alert3.setTitle("Message");
				alert3.setHeaderText("Error message");
				alert3.setContentText("Error to delete");
				Optional<ButtonType> result = alert3.showAndWait();
				if (result.isPresent() && result.get() == ButtonType.OK) {
					System.out.println("The user clicked OK");
				}	
			}
		}
	}
	
	public void updateFunction() {
		// confirmation dialog
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Cofirm");
		alert.setHeaderText("Confirm Message");
		alert.setContentText("Are you sure want to update this?");
		Optional<ButtonType> yes = alert.showAndWait();
		if (yes.isPresent() && yes.get() == ButtonType.OK) {
			// update user from DB
			Boolean status = userController.updateUser(tempId, role.getSelectionModel().getSelectedItem());
			
			// if user has been updated refresh the table
			if(status) {
				Alert alert2 = new Alert(AlertType.INFORMATION);
				alert2.setTitle("Success");
				alert2.setHeaderText("Success");
				alert2.setContentText("Account has been updated!");
				Optional<ButtonType> result = alert2.showAndWait();
				if (result.isPresent() && result.get() == ButtonType.OK) {
					System.out.println("The user clicked OK");
					refreshTable();
					nameTextField.clear();
					emailTextField.clear();
					role.getSelectionModel().clearSelection();
				}	
			// if error update from the database display error message
			} else {
				Alert alert3 = new Alert(AlertType.INFORMATION);
				alert3.setTitle("Message");
				alert3.setHeaderText("Error message");
				alert3.setContentText("Error to delete");
				Optional<ButtonType> result = alert3.showAndWait();
				if (result.isPresent() && result.get() == ButtonType.OK) {
					System.out.println("The user clicked OK");
				}	
			}
		}
	}

	public ManageUser(Stage stage) {
		initialize();
		setTop(container);
		setCenter(formBox);
		setBottom(buttonBox);
		
		deleteButton.setOnMouseClicked(e -> {
			deleteFunction();
		});
		
		updateButton.setOnMouseClicked(e -> {
			updateFunction();
		});
	}

}
