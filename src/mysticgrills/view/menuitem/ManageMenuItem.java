package mysticgrills.view.menuitem;

import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
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
import mysticgrills.controller.MenuItemController;
import mysticgrills.model.MenuItem;
import mysticgrills.utils.Dialog;
import mysticgrills.view.home.AdminHome;

public class ManageMenuItem extends BorderPane {

	VBox container, titleContainer, buttonContainer;
	ScrollPane containerTable;
	TableView<MenuItem> viewMenuItem;
	ArrayList<MenuItem> menuItems;
	Button createButton, updateButton, deleteButton, backButton;
	GridPane formBox;
	HBox buttonBox;
	TextField nameTextField, descTextField, priceTextField;
	Label nameLbl, descLbl, priceLbl;
	Dialog dg;
	Integer tempId;
	MenuItem menuItem;
	Text title;

	private MenuItemController menuItemController = MenuItemController.getMC();

	public void initialize() {
		containerTable = new ScrollPane();
		formBox = new GridPane();
		nameLbl = new Label("Name");
		descLbl = new Label("Description");
		priceLbl = new Label("Price");

		nameTextField = new TextField();
		descTextField = new TextField();
		priceTextField = new TextField();

		container = new VBox(16);
		titleContainer = new VBox();
		buttonBox = new HBox(8);
		buttonContainer = new VBox(8);

		createButton = new Button("Add");
		updateButton = new Button("Update");
		deleteButton = new Button("Delete");
		backButton = new Button("Back");

		title = new Text("Manage Menu Item");
		viewMenuItem = new TableView<MenuItem>();
		menuItems = new ArrayList<MenuItem>();
		dg = new Dialog();

	}

	public void style() {
		title.setFont(new Font(24));
		title.setTextAlignment(TextAlignment.CENTER);
		titleContainer.getChildren().add(title);
		titleContainer.setAlignment(Pos.CENTER);

		viewMenuItem.setMaxHeight(800);
		viewMenuItem.setPadding(new Insets(10, 10, 10, 10));

		formBox.setPadding(new Insets(10, 10, 10, 10));
		formBox.add(nameLbl, 0, 0);
		formBox.add(descLbl, 0, 1);
		formBox.add(priceLbl, 0, 2);
		formBox.setHgap(10);
		formBox.add(nameTextField, 1, 0);
		formBox.add(descTextField, 1, 1);
		formBox.add(priceTextField, 1, 2);
		formBox.setAlignment(Pos.CENTER);

		buttonBox.getChildren().addAll(createButton, updateButton, deleteButton);
		buttonBox.setAlignment(Pos.CENTER);

		containerTable.setContent(viewMenuItem);
		containerTable.setHbarPolicy(ScrollBarPolicy.NEVER);
		containerTable.setFitToWidth(true);

		buttonContainer.getChildren().setAll(buttonBox, backButton);
		buttonContainer.setAlignment(Pos.CENTER);

		container.getChildren().setAll(titleContainer, containerTable, formBox, buttonContainer);
		container.setPadding(new Insets(8));
		setTable();
	}

	public void setTable() {
		TableColumn<MenuItem, Integer> idColumn = new TableColumn<MenuItem, Integer>("ID");
		TableColumn<MenuItem, String> nameColumn = new TableColumn<MenuItem, String>("Name");
		TableColumn<MenuItem, String> descColumn = new TableColumn<MenuItem, String>("Description");
		TableColumn<MenuItem, Double> priceColumn = new TableColumn<MenuItem, Double>("Price");

		viewMenuItem.getColumns().addAll(idColumn, nameColumn, descColumn, priceColumn);

		idColumn.setCellValueFactory(new PropertyValueFactory<>("menuItemId"));
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("menuItemName"));
		descColumn.setCellValueFactory(new PropertyValueFactory<>("menuItemDesc"));
		priceColumn.setCellValueFactory(new PropertyValueFactory<>("menuItemPrice"));

		refreshTable();
	}

	public void refreshTable() {
		menuItems = menuItemController.getAllMenuItems();
		viewMenuItem.getItems().clear();
		viewMenuItem.getItems().addAll(menuItems);
		viewMenuItem.setOnMouseClicked(tableMouseEvent());
	}

	// to handle if user press a row in table
	private EventHandler<MouseEvent> tableMouseEvent() {
		return new EventHandler<MouseEvent>() {

			// get user data from pressed table row
			@Override
			public void handle(MouseEvent event) {
				TableSelectionModel<MenuItem> tableSelectionModel = viewMenuItem.getSelectionModel();
				tableSelectionModel.setSelectionMode(SelectionMode.SINGLE);
				// get data from selected model
				menuItem = tableSelectionModel.getSelectedItem();

				// display data in textfield and combobox
				if (menuItem != null) {
					nameTextField.setText(menuItem.getMenuItemName());
					descTextField.setText(menuItem.getMenuItemDesc());
					priceTextField.setText(String.valueOf(menuItem.getMenuItemPrice()));

					tempId = menuItem.getMenuItemId();
				}

			}
		};

	}

	// Add menu item to database
	public void addFunction() {
		String status = menuItemController.createMenuItem(nameTextField.getText(), descTextField.getText(),
				priceTextField.getText());

		if (status.contains("success")) {
			if (dg.informationDialog("Success Message", "Success", status)) {
				refreshTable();
				nameTextField.clear();
				descTextField.clear();
				priceTextField.clear();
			}
		} else {
			dg.informationDialog("Error Message", "Error", status);
		}
	}

	// Delete menu item from database
	public void deleteFunction() {
		if (dg.confirmationDialog("Confirm", "Confirm Message", "Are you sure want to delete this?")) {
			Boolean status = menuItemController.deleteMenuItem(tempId);
			if (status) {
				if (dg.informationDialog("Success", "Success", "Menu Item has been deleted!")) {
					refreshTable();
					nameTextField.clear();
					descTextField.clear();
					priceTextField.clear();
				}
			} else {
				dg.informationDialog("Error Message", "Error to delete", "Error to delete from DB");
			}

		}
	}

	// Update menu item from database
	public void updateFunction() {
		if (dg.confirmationDialog("Confirm", "Confirm Message", "Are you sure want to update?")) {
			String status = menuItemController.updateMenuItem(tempId, menuItem.getMenuItemName(),
					nameTextField.getText(), descTextField.getText(), priceTextField.getText());

			if (status.contains("success")) {
				if (dg.informationDialog("Success", "Success Message", "Account has been updated!")) {
					refreshTable();
					nameTextField.clear();
					descTextField.clear();
					priceTextField.clear();
				}
			} else {
				dg.informationDialog("Error", "Error Message", status);
			}
		}
	}

	public void eventListener(Stage stage) {
		createButton.setOnMouseClicked(e -> {
			addFunction();
		});

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

	public ManageMenuItem(Stage stage) {
		stage.setTitle("Manage Menu Item");
		initialize();
		style();
		setCenter(container);
		eventListener(stage);

	}

}
