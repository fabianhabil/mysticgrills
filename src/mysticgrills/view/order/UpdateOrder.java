package mysticgrills.view.order;

import java.util.ArrayList;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.util.Callback;
import mysticgrills.GlobalState;
import mysticgrills.controller.OrderController;
import mysticgrills.controller.OrderItemController;
import mysticgrills.model.Order;
import mysticgrills.model.OrderItem;
import mysticgrills.model.User;
import mysticgrills.utils.Dialog;
import mysticgrills.view.customer.ViewCustomerOrder;
import mysticgrills.view.home.AdminHome;

public class UpdateOrder extends BorderPane {
	VBox container, topContainer, titleContainer, buttonContainer, cartContainer;
	HBox tableOrder;
	HBox buttonAction;
	ScrollPane containerTable;
	TableView<OrderItem> viewOrderDetail;
	ArrayList<OrderItem> orderDetails;
	Button updateButton, backButton, deleteButton;
	GridPane formBox;
	TextField quantityTextField;
	Label nameLbl, quantityLbl, priceLbl;
	Text title, titleTop, cartTitle, itemName, itemPrice;
	Dialog dg;
	Double total;

	private OrderController orderController = OrderController.getInstance();
	private OrderItemController orderItemController = OrderItemController.getInstance();
	private GlobalState globalState = GlobalState.getInstance();

	private OrderItem orderItemSelected;
	private Integer id;
	private Order order;

	public void initialize(Stage stage) {
		containerTable = new ScrollPane();

		topContainer = new VBox(8);
		container = new VBox(8);
		titleContainer = new VBox();
		buttonContainer = new VBox(8);
		tableOrder = new HBox(16);
		buttonAction = new HBox(16);
		cartContainer = new VBox(8);

		formBox = new GridPane();
		nameLbl = new Label("Name");
		quantityLbl = new Label("Quantity");
		priceLbl = new Label("Price");

		quantityTextField = new TextField();

		updateButton = new Button("Update Menu Item");
		deleteButton = new Button("Delete Menu Item");
		backButton = new Button("Back");
		titleTop = new Text("");
		title = new Text("");
		itemName = new Text("");
		itemPrice = new Text("");

		viewOrderDetail = new TableView<OrderItem>();
		orderDetails = new ArrayList<OrderItem>();
		dg = new Dialog();

		order = orderController.getOrderByOrderId(id);
		total = (double) 0;

		setTable();
	}

	public void style() {
		titleTop.setFont(new Font(24));
		titleTop.setTextAlignment(TextAlignment.CENTER);
		titleTop.setText("Update Order (Order ID):" + id + "\nStatus: " + order.getOrderStatus());

		titleContainer.getChildren().add(titleTop);
		titleContainer.setAlignment(Pos.CENTER);

		viewOrderDetail.setMaxHeight(600);
		viewOrderDetail.setPadding(new Insets(10, 10, 10, 10));

		updateButton.setMinWidth(150);
		backButton.setMinWidth(150);
		deleteButton.setMinWidth(150);

		containerTable.setContent(viewOrderDetail);
		containerTable.setHbarPolicy(ScrollBarPolicy.NEVER);
		containerTable.setMinWidth(700);
		containerTable.setFitToWidth(true);

		buttonAction.getChildren().setAll(updateButton, deleteButton);
		buttonAction.setAlignment(Pos.CENTER);

		buttonContainer.getChildren().setAll(buttonAction, backButton);
		buttonContainer.setAlignment(Pos.CENTER);

		tableOrder.getChildren().setAll(containerTable);
		tableOrder.setAlignment(Pos.CENTER);

		formBox.setPadding(new Insets(10, 10, 10, 10));
		formBox.add(nameLbl, 0, 0);
		formBox.add(quantityLbl, 0, 1);
		formBox.add(priceLbl, 0, 2);

		formBox.setHgap(100);
		formBox.setVgap(10);

		formBox.add(itemName, 1, 0);
		formBox.add(quantityTextField, 1, 1);
		formBox.add(itemPrice, 1, 2);

		formBox.setAlignment(Pos.CENTER);

		formBox.setVisible(false);

		cartContainer.setMinWidth(500);

		container.getChildren().setAll(titleContainer, tableOrder, formBox, buttonContainer);
		container.setPadding(new Insets(16));

	}

	public void setTable() {
		TableColumn<OrderItem, Integer> idColumn = new TableColumn<OrderItem, Integer>("ID");
		TableColumn<OrderItem, String> menuNameColumn = new TableColumn<OrderItem, String>("Menu Item Name");
		TableColumn<OrderItem, Integer> qtyColumn = new TableColumn<OrderItem, Integer>("Item Quantity");
		TableColumn<OrderItem, String> priceColumn = new TableColumn<OrderItem, String>("Price");
		TableColumn<OrderItem, String> totalColumn = new TableColumn<OrderItem, String>("Total");

		viewOrderDetail.getColumns().addAll(idColumn, menuNameColumn, qtyColumn, priceColumn, totalColumn);

		idColumn.setCellValueFactory(new PropertyValueFactory<>("orderItemId"));
		menuNameColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<OrderItem, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<OrderItem, String> param) {
						return new SimpleObjectProperty<>(param.getValue().getMenuItem().getMenuItemName());
					}
				});

		qtyColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		priceColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<OrderItem, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<OrderItem, String> param) {
						return new SimpleObjectProperty<>("Rp " + param.getValue().getMenuItem().getMenuItemPrice());
					}
				});
		totalColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<OrderItem, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<OrderItem, String> param) {
						return new SimpleObjectProperty<>("Rp "
								+ (param.getValue().getMenuItem().getMenuItemPrice() * param.getValue().getQuantity()));
					}
				});

		refreshTable();
	}

	public void refreshTable() {
		orderDetails = orderItemController.getOrderItemsByOrderId(id);
		viewOrderDetail.getItems().clear();
		viewOrderDetail.getItems().addAll(orderDetails);
		viewOrderDetail.setOnMouseClicked(tableMouseEvent());

	}

	// to handle if user press a row in table
	private EventHandler<MouseEvent> tableMouseEvent() {
		return new EventHandler<MouseEvent>() {

			// get order data from pressed table row
			@Override
			public void handle(MouseEvent event) {
				TableSelectionModel<OrderItem> tableSelectionModel = viewOrderDetail.getSelectionModel();
				tableSelectionModel.setSelectionMode(SelectionMode.SINGLE);

				// get data from selected model
				orderItemSelected = tableSelectionModel.getSelectedItem();
				quantityTextField.setText("");

				if (orderItemSelected != null) {
					quantityTextField.setText("" + orderItemSelected.getQuantity());
					itemName.setText(orderItemSelected.getMenuItem().getMenuItemName());
					itemPrice.setText(orderItemSelected.getMenuItem().getMenuItemPrice().toString());
					formBox.setVisible(true);
				}
			}
		};
	}

	public void eventListener(Stage stage) {
		// Back to viewOrder page
		backButton.setOnMouseClicked(e -> {
			// If no more items in order, redirect to home based on the role
			// because if no more items in order = order is canceled.
			if (orderDetails.size() == 0) {
				redirectToHome(stage);
			} else {
				stage.setScene(new Scene(new ViewOrderDetail(stage, id), 1366, 768));
			}

		});

		// Delete Order Button Listener
		deleteButton.setOnMouseClicked(e -> {
			if (orderItemSelected == null) {
				dg.informationDialog("Information", "Information", "Please choose an item first!");
			} else {
				// confirmation to delete the data
				Boolean confirmationDelete = dg.confirmationDialog("Confirmation Dialog", "Confirmation",
						"Are you sure you want to delete this Order?");

				// if user click sure to delete the data
				if (confirmationDelete) {
					Boolean success = orderItemController.deleteOrderItem(orderItemSelected.getOrderItemId());

					// Refresh data
					if (success) {
						quantityTextField.setText("");
						itemName.setText("");
						itemPrice.setText("");
						formBox.setVisible(false);
						orderItemSelected = null;

						dg.informationDialog("Information", "Information", "Delete Item Success!");
						refreshTable();

					} else {
						dg.informationDialog("Error", "Error Message", "Delete Error!");
					}
				}

			}
		});

		// Update Order Button Listener
		updateButton.setOnMouseClicked(e -> {
			if (orderItemSelected == null) {
				dg.informationDialog("Information", "Information", "Please choose an item first!");
			} else {
				Boolean quantityValidated = validateQuantity(quantityTextField.getText());

				if (quantityValidated) {
					Boolean success = orderItemController.updateOrderItem(orderItemSelected.getOrderItemId(),
							Integer.parseInt(quantityTextField.getText()));

					// Refresh data
					if (success) {
						quantityTextField.setText("");
						itemName.setText("");
						itemPrice.setText("");
						formBox.setVisible(false);
						orderItemSelected = null;

						dg.informationDialog("Information", "Information", "Order Item Updated");
						refreshTable();

					} else {
						dg.informationDialog("Error", "Error Message", "Update Error!");
					}

				} else {
					// Input from quantity textfield is invalid, return notice error
					dg.informationDialog("Error", "Error Message", "Quantity cant be empty and must be a number!");
				}
			}
		});
	}
	
	public void redirectToHome(Stage stage) {
		User user = globalState.getCurrentLoggedInUser();
		BorderPane bp;

		if (user.getUserRole().equals("Customer")) {
			bp = new ViewCustomerOrder(stage);
		}

		else if (user.getUserRole().equals("Admin")) {
			bp = new AdminHome(stage);
		}

		else if (user.getUserRole().equals("Chef") || user.getUserRole().equals("Waiter")
				|| user.getUserRole().equals("Cashier")) {
			bp = new ViewOrder(stage);
		}

		else {
			bp = new BorderPane();
		}

		stage.setScene(new Scene(bp, 1366, 768));
	}

	// Helper to check if quantity not empty and must be a number
	public boolean validateQuantity(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public UpdateOrder(Stage stage, Integer id) {
		stage.setTitle("Update Order");
		this.id = id;
		initialize(stage);
		style();
		eventListener(stage);
		setCenter(container);
	}

}
