package mysticgrills.view.order;

import java.sql.Date;
import java.util.ArrayList;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.cell.PropertyValueFactory;
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
import mysticgrills.controller.ReceiptController;
import mysticgrills.model.Order;
import mysticgrills.model.OrderItem;
import mysticgrills.model.User;
import mysticgrills.utils.Dialog;
import mysticgrills.view.customer.ViewCustomerOrder;
import mysticgrills.view.home.AdminHome;
import mysticgrills.view.home.CashierHome;
import mysticgrills.view.home.KitchenHome;

public class ViewOrderDetail extends BorderPane {

	VBox container, topContainer, titleContainer, buttonContainer, cartContainer;
	HBox buttonBox, tableOrder;
	ScrollPane containerTable;
	GridPane formBox;
	TableView<OrderItem> viewOrderDetail;
	ArrayList<OrderItem> orderDetails;
	Button button1, button2, backButton;
	Dialog dg;
	Text title;
	Double total;
	TextField paymentAmount;
	Label typeLbl, amountLbl;
	ComboBox<String> type;

	private OrderController orderController = OrderController.getInstance();
	private OrderItemController orderItemController = OrderItemController.getInstance();
	private GlobalState globalState = GlobalState.getInstance();
	private ReceiptController receiptController = ReceiptController.getInstance();

	private Integer id;
	private Order order;

	public void initialize(Stage stage) {
		containerTable = new ScrollPane();
		topContainer = new VBox(8);
		container = new VBox(8);
		titleContainer = new VBox();
		buttonBox = new HBox(8);
		buttonContainer = new VBox(8);
		tableOrder = new HBox(16);
		cartContainer = new VBox(8);

		button1 = new Button("");
		button2 = new Button("");
		backButton = new Button("Back");
		title = new Text("");

		formBox = new GridPane();
		paymentAmount = new TextField();
		viewOrderDetail = new TableView<OrderItem>();
		orderDetails = new ArrayList<OrderItem>();
		dg = new Dialog();
		typeLbl = new Label("Type");
		amountLbl = new Label("Customer Amount Pay");
		type = new ComboBox<>();
		type.getItems().addAll("Cash", "Debit", "Credit");

		order = orderController.getOrderByOrderId(id);
		total = (double) 0;

		if (globalState.getCurrentLoggedInUser().getUserRole().equals("Customer")) {
			initCustomer(stage);
		}

		if (globalState.getCurrentLoggedInUser().getUserRole().equals("Waiter")
				|| globalState.getCurrentLoggedInUser().getUserRole().equals("Chef")) {
			initKitchen(stage);
		}

		if (globalState.getCurrentLoggedInUser().getUserRole().equals("Cashier")) {
			initCashier(stage);
		}

		getGrandTotal();
		setTable();
	}

	public void initCustomer(Stage stage) {
		button1.setText("Update Order");
		buttonBox.getChildren().addAll(button1);

		// If already paid, dont show the button
		if (order.getOrderStatus().equals("Paid")) {
			buttonBox.setVisible(false);
		}

		eventListenerCustomer(stage);
		buttonContainer.getChildren().setAll(buttonBox, backButton);
	}

	public void initKitchen(Stage stage) {
		button1.setText("Update Order");
		button2.setText("Delete Order");
		buttonBox.getChildren().addAll(button1, button2);

		// If already paid, dont show the button
		if (order.getOrderStatus().equals("Paid")) {
			buttonBox.setVisible(false);
		}

		eventListenerKitchen(stage);
		buttonContainer.getChildren().setAll(buttonBox, backButton);
	}

	public void initCashier(Stage stage) {
		button1.setText("Pay Order");

		formBox.setPadding(new Insets(10, 10, 10, 10));
		formBox.add(typeLbl, 0, 0);
		formBox.add(amountLbl, 0, 1);
		formBox.add(type, 1, 0);
		formBox.add(paymentAmount, 1, 1);
		formBox.setAlignment(Pos.CENTER);
		formBox.setVgap(10);
		formBox.setHgap(10);

		type.getSelectionModel().select("Cash");

		buttonBox.getChildren().addAll(button1);

		buttonContainer.getChildren().setAll(formBox, buttonBox, backButton);
		eventListenerCashier(stage);
	}

	public void style() {
		title.setFont(new Font(24));
		title.setTextAlignment(TextAlignment.CENTER);
		title.setText(
				"View Order Detail (Order ID):" + id + "\nStatus: " + order.getOrderStatus() + "\nTotal: Rp " + total);

		titleContainer.getChildren().add(title);
		titleContainer.setAlignment(Pos.CENTER);

		viewOrderDetail.setMaxHeight(600);
		viewOrderDetail.setPadding(new Insets(10, 10, 10, 10));

		button1.setMinWidth(150);
		button2.setMinWidth(150);
		backButton.setMinWidth(150);

		buttonBox.setAlignment(Pos.CENTER);

		containerTable.setContent(viewOrderDetail);
		containerTable.setHbarPolicy(ScrollBarPolicy.NEVER);
		containerTable.setMinWidth(700);
		containerTable.setFitToWidth(true);

		buttonContainer.setAlignment(Pos.CENTER);

		tableOrder.getChildren().setAll(containerTable);
		tableOrder.setAlignment(Pos.CENTER);

		cartContainer.setMinWidth(500);

		container.getChildren().setAll(titleContainer, tableOrder, buttonContainer);
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
	}

	public void eventListenerCustomer(Stage stage) {
		// Update Order Listener
		button1.setOnMouseClicked(e -> {
			stage.setScene(new Scene(new UpdateOrder(stage, id), 1366, 768));
		});

	}

	public void eventListenerCashier(Stage stage) {
		// Payment Button Listener
		button1.setOnMouseClicked(e -> {
			Boolean quantityValidated = validateQuantity(paymentAmount.getText());

			if (quantityValidated) {
				Double input = Double.parseDouble(paymentAmount.getText());

				if (input < total) {
					dg.informationDialog("Error", "Error Message", "Amount need to be greater or equal than total!");
				} else {
					if (orderController.updateOrder(id, total)) {
						long millis = System.currentTimeMillis();
						Date date = new java.sql.Date(millis);
						if (receiptController.createReceipt(order, type.getSelectionModel().getSelectedItem(), total,
								date)) {
							dg.informationDialog("Information", "Information", "Payment Success");
							stage.setScene(new Scene(new ViewOrder(stage), 1366, 768));
						} else {
							dg.informationDialog("Error", "Error Message", "Payment Error!");

						}

					} else {
						dg.informationDialog("Error", "Error Message", "Payment Error!");
					}
				}

			} else {
				// Input from quantity textfield is invalid, return notice error
				dg.informationDialog("Error", "Error Message", "Amount cant be empty and must be a number!");
			}

		});
	}

	public void eventListenerKitchen(Stage stage) {
		// Update Order Listener
		button1.setOnMouseClicked(e -> {
			stage.setScene(new Scene(new UpdateOrder(stage, id), 1366, 768));
		});

		// Delete Button Listener
		button2.setOnMouseClicked(e -> {
			// confirmation to delete the data
			Boolean confirmationDelete = dg.confirmationDialog("Confirmation Dialog", "Confirmation",
					"Are you sure you want to delete this Order?");

			// if user click sure to delete the data
			if (confirmationDelete) {
				if (orderController.deleteOrder(id)) {
					dg.informationDialog("Information", "Information", "Order deleted");
					redirectToHome(stage);
				}
			}

		});
	}

	public void eventListener(Stage stage) {
		// Back to viewOrder page
		backButton.setOnMouseClicked(e -> {
			redirectToHome(stage);
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

	// Get grand total of the order
	public void getGrandTotal() {
		ArrayList<OrderItem> orderItems = order.getOrderItems();
		total = (double) 0;
		for (int i = 0; i < orderItems.size(); i++) {
			total += orderItems.get(i).getMenuItem().getMenuItemPrice() * orderItems.get(i).getQuantity();
		}
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

	public ViewOrderDetail(Stage stage, Integer id) {
		// TODO Auto-generated constructor stub
		stage.setTitle("View Order Detail");
		this.id = id;
		initialize(stage);
		style();
		eventListener(stage);
		setCenter(container);
	}

}
