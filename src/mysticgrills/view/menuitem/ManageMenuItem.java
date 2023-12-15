package mysticgrills.view.menuitem;

import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import javafx.stage.Stage;
import mysticgrills.controller.MenuItemController;
import mysticgrills.model.MenuItem;
import mysticgrills.utils.Dialog;

public class ManageMenuItem extends BorderPane{

	ScrollPane container;
	TableView<MenuItem> viewMenuItem;
	ArrayList<MenuItem> menuItems;
	Button createButton;
	Button updateButton;
	Button deleteButton;
	GridPane formBox;
	HBox buttonBox;
	TextField nameTextField, descTextField, priceTextField;
	Label nameLbl, descLbl, priceLbl;
	Dialog dg;
	Integer tempId;
	MenuItem menuItem;

	private MenuItemController menuItemController = MenuItemController.getMC();

	public void initialize() {
		container = new ScrollPane();
		formBox = new GridPane();
		nameLbl = new Label("Name");
		descLbl = new Label("Description");
		priceLbl = new Label("Price");
		
		nameTextField = new TextField();
		descTextField = new TextField();
		priceTextField = new TextField();
		
		buttonBox = new HBox();
		createButton = new Button("Add");
		updateButton = new Button("Update");
		deleteButton = new Button("Delete");
		viewMenuItem = new TableView<MenuItem>();
		menuItems = new ArrayList<MenuItem>();
		dg = new Dialog();
		
		
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
		
		buttonBox.getChildren().addAll(createButton, updateButton, deleteButton);
		buttonBox.setAlignment(Pos.CENTER);
		
		container.setContent(viewMenuItem);
		container.setHbarPolicy(ScrollBarPolicy.NEVER);
		container.setFitToWidth(true);
		setTable();
	}

	public void setTable() {
		TableColumn<MenuItem, Integer> idColumn = new TableColumn<MenuItem, Integer>("Menu Item ID");
		TableColumn<MenuItem, String> nameColumn = new TableColumn<MenuItem, String>("Menu Item Name");
		TableColumn<MenuItem, String> descColumn = new TableColumn<MenuItem, String>("Menu Item Desc");
		TableColumn<MenuItem, Double> priceColumn = new TableColumn<MenuItem, Double>("Menu Item Price");

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
				if(menuItem != null) {
					nameTextField.setText(menuItem.getMenuItemName());
					descTextField.setText(menuItem.getMenuItemDesc());
					priceTextField.setText(String.valueOf(menuItem.getMenuItemPrice()));

					tempId = menuItem.getMenuItemId();
				}
				
			}
		};
		
	}
	
	public void addFunction() {
		String status = menuItemController.createMenuItem(nameTextField.getText(), descTextField.getText(), priceTextField.getText());
		if(status.contains("success")) {
			if(dg.informationDialog("Success Message", "Success", status)) {
				refreshTable();
				nameTextField.clear();
				descTextField.clear();
				priceTextField.clear();
			}
		} else {
			dg.informationDialog("Error Message", "Error", status);			
		}
	}
	
	public void deleteFunction() {
		if(dg.confirmationDialog("Confirm", "Confirm Message", "Are you sure want to delete this?")) {
			Boolean status = menuItemController.deleteMenuItem(tempId);
			if(status ) {
				if(dg.informationDialog("Success", "Success", "Menu Item has been deleted!")) {
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
	
	public void updateFunction() {
		if(dg.confirmationDialog("Confirm", "Confirm Message", "Are you sure want to update?")) {
			String status = menuItemController.updateMenuItem(tempId,nameTextField.getText(), descTextField.getText(), priceTextField.getText());
			System.out.println(priceTextField.getText());
			if(status.contains("success")) {
				if(dg.informationDialog("Success", "Success Message", "Account has been updated!")) {
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

	public ManageMenuItem(Stage stage) {
		initialize();
		setTop(container);
		setCenter(formBox);
		setBottom(buttonBox);
		
		createButton.setOnMouseClicked(e -> {
			addFunction();
		});
		
		deleteButton.setOnMouseClicked(e -> {
			deleteFunction();
		});
		
		updateButton.setOnMouseClicked(e -> {
			updateFunction();
		});
	}

}
