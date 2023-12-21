package mysticgrills.view.cashier;

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
import mysticgrills.controller.OrderController;
import mysticgrills.controller.OrderItemController;
import mysticgrills.model.Order;
import mysticgrills.model.OrderItem;
import mysticgrills.model.Receipt;
import mysticgrills.utils.Dialog;

public class ViewDetailedReceipt extends BorderPane {

	VBox container, topContainer, titleContainer, buttonContainer;
	HBox buttonBox, tableOrder;
	ScrollPane containerTable;
	GridPane formBox;
	TableView<OrderItem> viewOrderDetail;
	ArrayList<OrderItem> orderDetails;
	Button backButton;
	Dialog dg;
	Text title;
	Double total;
	TextField paymentAmount;
	Label typeLbl, amountLbl;
	ComboBox<String> type;

	private OrderController orderController = OrderController.getInstance();
	private OrderItemController orderItemController = OrderItemController.getInstance();

	private Integer id;
	private Order order;
	private Receipt receipt;

	public void initialize(Stage stage) {
		containerTable = new ScrollPane();
		topContainer = new VBox(8);
		container = new VBox(8);
		titleContainer = new VBox();
		buttonBox = new HBox(8);
		buttonContainer = new VBox(8);
		tableOrder = new HBox(16);

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

		getGrandTotal();
		setTable();
	}

	public void style() {
		title.setFont(new Font(24));
		title.setTextAlignment(TextAlignment.CENTER);
		title.setText("View Order Detail (Order ID):" + id + "\nStatus: " + order.getOrderStatus() + "\nTotal: Rp "
				+ total + "\nReceipt ID: " + receipt.getReceiptId());

		titleContainer.getChildren().add(title);
		titleContainer.setAlignment(Pos.CENTER);

		viewOrderDetail.setMaxHeight(600);
		viewOrderDetail.setPadding(new Insets(10, 10, 10, 10));

		backButton.setMinWidth(150);

		buttonBox.setAlignment(Pos.CENTER);

		containerTable.setContent(viewOrderDetail);
		containerTable.setHbarPolicy(ScrollBarPolicy.NEVER);
		containerTable.setMinWidth(700);
		containerTable.setFitToWidth(true);

		buttonContainer.setAlignment(Pos.CENTER);
		buttonContainer.getChildren().setAll(backButton);

		tableOrder.getChildren().setAll(containerTable);
		tableOrder.setAlignment(Pos.CENTER);

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

	public void eventListener(Stage stage) {
		// Back to viewOrder page
		backButton.setOnMouseClicked(e -> {
			stage.setScene(new Scene(new ViewReceipt(stage), 1366, 768));
		});
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

	public ViewDetailedReceipt(Stage stage, Integer id, Receipt receipt) {
		// TODO Auto-generated constructor stub
		stage.setTitle("View Receipt Detail");
		this.id = id;
		this.receipt = receipt;
		initialize(stage);
		style();
		eventListener(stage);
		setCenter(container);
	}

}
