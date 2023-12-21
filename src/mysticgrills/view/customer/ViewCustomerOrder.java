package mysticgrills.view.customer;

import java.sql.Date;
import java.util.ArrayList;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Callback;
import mysticgrills.controller.OrderController;
import mysticgrills.model.Order;
import mysticgrills.utils.Dialog;
import mysticgrills.view.home.CustomerHome;

public class ViewCustomerOrder extends BorderPane {
	VBox container, titleContainer, buttonContainer;
	HBox buttonBox;

	ScrollPane containerTable;
	TableView<Order> tableOrder;
	ArrayList<Order> orders;
	Button viewOrderButton, editOrderButton, backButton;
	Text title;

	Dialog dg;

	private OrderController orderController = OrderController.getInstance();
	private Order orderSelected;

	public void initialize() {
		container = new VBox(8);
		buttonContainer = new VBox(4);
		titleContainer = new VBox();

		buttonBox = new HBox(8);

		containerTable = new ScrollPane();
		tableOrder = new TableView<Order>();

		viewOrderButton = new Button("View Detail Order");
		editOrderButton = new Button("Edit Order");
		backButton = new Button("Back");

		title = new Text("View Order");

		dg = new Dialog();
	}

	public void style() {
		title.setFont(new Font(24));
		title.setTextAlignment(TextAlignment.CENTER);

		titleContainer.getChildren().add(title);
		titleContainer.setAlignment(Pos.CENTER);

		tableOrder.setMaxHeight(600);
		tableOrder.setPadding(new Insets(10, 10, 10, 10));

		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setVisible(false);

		buttonBox.getChildren().setAll(viewOrderButton, editOrderButton);
		buttonBox.setAlignment(Pos.CENTER);

		containerTable.setContent(tableOrder);
		containerTable.setHbarPolicy(ScrollBarPolicy.NEVER);
		containerTable.setMinWidth(700);
		containerTable.setFitToWidth(true);

		buttonContainer.getChildren().setAll(buttonBox, backButton);
		buttonContainer.setAlignment(Pos.CENTER);

		container.getChildren().setAll(titleContainer, containerTable, buttonContainer);
		container.setPadding(new Insets(16));

		setTable();
	}

	public void setTable() {
		TableColumn<Order, Integer> idColumn = new TableColumn<Order, Integer>("ID");
		TableColumn<Order, String> statusColumn = new TableColumn<Order, String>("Status");
		TableColumn<Order, Date> dateColumn = new TableColumn<Order, Date>("Order Date");
		TableColumn<Order, String> totalColumn = new TableColumn<Order, String>("Total");
		TableColumn<Order, String> totalItemColumn = new TableColumn<Order, String>("Item");

		tableOrder.getColumns().addAll(idColumn, statusColumn, dateColumn, totalItemColumn, totalColumn);

		idColumn.setCellValueFactory(new PropertyValueFactory("orderId"));
		statusColumn.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));
		dateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));

		// Accessing value in nested objects, we need to make custom for the
		// PropertyValueFactory
		totalColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Order, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<Order, String> param) {
						if (!param.getValue().getOrderStatus().equals("Paid")) {
							return new SimpleObjectProperty<>("Pending");
						}
						return new SimpleObjectProperty<>("Rp " + param.getValue().getOrderTotal());

					}
				});

		// Accessing value in by calling an operation, we need to make custom for the
		// PropertyValueFactory
		totalItemColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Order, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<Order, String> param) {

						return new SimpleObjectProperty<>(param.getValue().getOrderItems().size() + " pcs");

					}
				});

		refreshTable();
	}

	public void refreshTable() {
		orders = orderController.getAllOrders("Customer");
		tableOrder.getItems().clear();
		tableOrder.getItems().addAll(orders);

		tableOrder.setOnMouseClicked(tableMouseEvent());
	}

	// to handle if user press a row in table
	private EventHandler<MouseEvent> tableMouseEvent() {
		return new EventHandler<MouseEvent>() {

			// get order data from pressed table row
			@Override
			public void handle(MouseEvent event) {
				TableSelectionModel<Order> tableSelectionModel = tableOrder.getSelectionModel();
				tableSelectionModel.setSelectionMode(SelectionMode.SINGLE);

				// get data from selected model
				orderSelected = tableSelectionModel.getSelectedItem();
			}
		};
	}

	public void eventListener(Stage stage) {
		backButton.setOnMouseClicked(e -> {
			stage.setScene(new Scene(new CustomerHome(stage), 1366, 768));
		});
	}

	public ViewCustomerOrder(Stage stage) {
		stage.setTitle("View Order");
		initialize();
		style();
		setCenter(container);
	}

}
