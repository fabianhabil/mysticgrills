package mysticgrills.view.order;

import java.sql.Date;
import java.util.ArrayList;
//import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import mysticgrills.controller.MenuItemController;
import mysticgrills.controller.OrderController;
import mysticgrills.controller.OrderItemController;
import mysticgrills.model.MenuItem;
import mysticgrills.model.Order;
import mysticgrills.model.OrderItem;
import mysticgrills.model.User;
import mysticgrills.utils.Dialog;
import mysticgrills.view.home.CustomerHome;
import mysticgrills.view.home.KitchenHome;

public class ViewOrder extends BorderPane {

	VBox container, topContainer, titleContainer, buttonContainer, cartContainer;
	HBox buttonBox, tableCart;
	ScrollPane containerTable;
	TableView<Order> viewOrder;
	ArrayList<Order> orders;
	Button processOrderButton, deleteButton, updateOrderButton, backButton;
	GridPane formBox;
	Dialog dg;
	Text title;
	int total;

	private OrderController orderController = OrderController.getInstance();
	private GlobalState globalState = GlobalState.getInstance();

	// We will use hashmap for temporary cart system, since we dont want two items
	// with two different quantity for better complexity and looping behaviour
//	private HashMap<Integer, Order> newOrderItems;
	private Order orderSelected;

	public void initialize() {

		containerTable = new ScrollPane();
		formBox = new GridPane();

		topContainer = new VBox(8);
		container = new VBox(8);
		titleContainer = new VBox();
		buttonBox = new HBox(8);
		buttonContainer = new VBox(8);
		tableCart = new HBox(16);
		cartContainer = new VBox(8);

		deleteButton = new Button("Delete");
		updateOrderButton = new Button("Update");
		backButton = new Button("Back");

		viewOrder = new TableView<Order>();
		orders = new ArrayList<Order>();
		dg = new Dialog();

		if (globalState.getCurrentLoggedInUser().getUserRole().equals("Chef")) {
			initChef();
		}

		if (globalState.getCurrentLoggedInUser().getUserRole().equals("Waiter")) {
			initWaiter();
		}

		if (globalState.getCurrentLoggedInUser().getUserRole().equals("Cashier")) {
			initCashier();
		}

		total = 0;
	}

	// set init for waiter
	public void initWaiter() {
		title = new Text("View Prepared Order");
		processOrderButton = new Button("Serve Order");
		setTableForWaiterandChef();
	}

	// set init for chef
	public void initChef() {
		title = new Text("View Pending Order");
		processOrderButton = new Button("Prepare Order");
		setTableForWaiterandChef();
	}

	public void initCashier() {
		title = new Text("View Order");
		processOrderButton = new Button("Pay Order");
		setTableForCashier();
	}

	public void style() {
		title.setFont(new Font(24));
		title.setTextAlignment(TextAlignment.CENTER);

		titleContainer.getChildren().add(title);
		titleContainer.setAlignment(Pos.CENTER);

		viewOrder.setMaxHeight(600);
		viewOrder.setPadding(new Insets(10, 10, 10, 10));

		formBox.setPadding(new Insets(10, 10, 10, 10));

		formBox.setHgap(100);
		formBox.setVgap(10);

		formBox.setAlignment(Pos.CENTER);

		formBox.setVisible(false);

		processOrderButton.setMinWidth(150);
		deleteButton.setMinWidth(150);
		updateOrderButton.setMinWidth(150);

		buttonBox.getChildren().addAll(processOrderButton, updateOrderButton, deleteButton);
		buttonBox.setAlignment(Pos.CENTER);

		containerTable.setContent(viewOrder);
		containerTable.setHbarPolicy(ScrollBarPolicy.NEVER);
		containerTable.setMinWidth(700);
		containerTable.setFitToWidth(true);

		buttonContainer.getChildren().setAll(buttonBox, backButton);
		buttonContainer.setAlignment(Pos.CENTER);

		tableCart.getChildren().setAll(containerTable);
		tableCart.setAlignment(Pos.CENTER);

		cartContainer.setMinWidth(500);

		container.getChildren().setAll(titleContainer, tableCart, formBox, buttonContainer);
		container.setPadding(new Insets(16));

	}

	public void setTableForWaiterandChef() {
		TableColumn<Order, Integer> idColumn = new TableColumn<Order, Integer>("ID");
		TableColumn<Order, String> nameColumn = new TableColumn<Order, String>("User Name");
		TableColumn<Order, String> statusColumn = new TableColumn<Order, String>("Status");
		TableColumn<Order, Date> dateColumn = new TableColumn<Order, Date>("Order Date");

		viewOrder.getColumns().addAll(idColumn, nameColumn, statusColumn, dateColumn);

		idColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));

		// Accessing value in nested objects, we need to make custom for the
		// PropertyValueFactory
		nameColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Order, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<Order, String> param) {
						return new SimpleObjectProperty<>(param.getValue().getOrderUser().getUserName());

					}
				});
		statusColumn.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));
		dateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));

		refreshTable();
	}

	public void setTableForCashier() {
		TableColumn<Order, Integer> idColumn = new TableColumn<Order, Integer>("ID");
		TableColumn<Order, String> nameColumn = new TableColumn<Order, String>("User Name");
		TableColumn<Order, String> statusColumn = new TableColumn<Order, String>("Status");
		TableColumn<Order, Double> priceColumn = new TableColumn<Order, Double>("Total Price");
		TableColumn<Order, Date> dateColumn = new TableColumn<Order, Date>("Order Date");

		viewOrder.getColumns().addAll(idColumn, nameColumn, statusColumn, dateColumn, priceColumn);

		idColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));

		// Accessing value in nested objects, we need to make custom for the
		// PropertyValueFactory
		nameColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Order, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<Order, String> param) {
						return new SimpleObjectProperty<>(param.getValue().getOrderUser().getUserName());

					}
				});

		statusColumn.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));
		priceColumn.setCellValueFactory(new PropertyValueFactory<>("orderTotal"));
		dateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));

		refreshTable();
	}

	public void refreshTable() {
		orders = orderController.getAllOrders(globalState.getCurrentLoggedInUser().getUserRole());
		viewOrder.getItems().clear();
		viewOrder.getItems().addAll(orders);

		viewOrder.setOnMouseClicked(tableMouseEvent());
	}

	// to handle if user press a row in table
	private EventHandler<MouseEvent> tableMouseEvent() {
		return new EventHandler<MouseEvent>() {

			// get order data from pressed table row
			@Override
			public void handle(MouseEvent event) {
				TableSelectionModel<Order> tableSelectionModel = viewOrder.getSelectionModel();
				tableSelectionModel.setSelectionMode(SelectionMode.SINGLE);

				// get data from selected model
				orderSelected = tableSelectionModel.getSelectedItem();

				if (orderSelected != null) {
					System.out.println(orderSelected.getOrderId().toString());
				}

			}
		};
	}

	public void eventListener(Stage stage) {
		// Process Order Listener
		processOrderButton.setOnMouseClicked(e -> {
			if (orderSelected == null) {
				dg.informationDialog("Information", "Information", "Choose the data");
			} else {
				// confirmation to delete the data
				Boolean confirmationDelete = dg.confirmationDialog("Confirmation Dialog", "Confirmation", String.format(
						"Are you sure you want to process this order (OrderId: %d)?", orderSelected.getOrderId()));

				// if user click sure to delete the data
				if (confirmationDelete) {
//					System.out.println("Yes");
					if(orderController.updateOrder(globalState.getCurrentLoggedInUser().getUserRole(), orderSelected.getOrderId())) {
						dg.informationDialog("Information", "Information", "Process Order");
						refreshTable();
					}
				}
			}
		});

		// Update Order Listener
		updateOrderButton.setOnMouseClicked(e -> {
			if (orderSelected == null) {
				dg.informationDialog("Information", "Information", "Choose the data");
			} else {
				// confirmation to delete the data
				Boolean confirmationDelete = dg.confirmationDialog("Confirmation Dialog", "Confirmation", String.format(
						"Are you sure you want to update this order (OrderId: %d)?", orderSelected.getOrderId()));

				// if user click sure to delete the data
				if (confirmationDelete) {
					System.out.println("Yes");
				}
			}
		});

		// Delete Button Listener
		deleteButton.setOnMouseClicked(e -> {

			// if user not choose the data
			if (orderSelected == null) {
				dg.informationDialog("Information", "Information", "Choose the data");
			} else {
				// confirmation to delete the data
				Boolean confirmationDelete = dg.confirmationDialog("Confirmation Dialog", "Confirmation",
						"Are you sure you want to delete this Order?");

				// if user click sure to delete the data
				if (confirmationDelete) {
					System.out.println("Yes");
				}
			}

		});

		// Back to home customer menu
		backButton.setOnMouseClicked(e -> {
			stage.setScene(new Scene(new KitchenHome(stage), 1366, 768));
		});
	}

	public ViewOrder(Stage stage) {
		stage.setTitle("View Order");
		initialize();
		style();
		eventListener(stage);
		setCenter(container);
	}

}
