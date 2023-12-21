package mysticgrills.view.order;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TableView;
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
import mysticgrills.model.OrderItem;
import mysticgrills.utils.Dialog;

public class ViewOrderDetail extends BorderPane {
	
	VBox container, topContainer, titleContainer, buttonContainer, cartContainer;
	HBox buttonBox, tableCart;
	ScrollPane containerTable;
	TableView<OrderItem> viewOrderDetail;
	ArrayList<OrderItem> orderDetails;
	Button deleteButton, updateOrderButton, backButton;
	GridPane formBox;
	Dialog dg;
	Text title;
	int total;

	private OrderController orderController = OrderController.getInstance();
	private OrderItemController orderItemController = OrderItemController.getInstance();
	private GlobalState globalState = GlobalState.getInstance();

	private OrderItem orderItemSelected;
	private Integer id;

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
		title = new Text(String.format("View Order Detail (Order ID:%s)", id));

		viewOrderDetail = new TableView<OrderItem>();
		orderDetails = new ArrayList<OrderItem>();
		dg = new Dialog();

		setTable();
		
		total = 0;
	}


	public void style() {
		title.setFont(new Font(24));
		title.setTextAlignment(TextAlignment.CENTER);

		titleContainer.getChildren().add(title);
		titleContainer.setAlignment(Pos.CENTER);

		viewOrderDetail.setMaxHeight(600);
		viewOrderDetail.setPadding(new Insets(10, 10, 10, 10));

		formBox.setPadding(new Insets(10, 10, 10, 10));

		formBox.setHgap(100);
		formBox.setVgap(10);

		formBox.setAlignment(Pos.CENTER);

		formBox.setVisible(false);

		deleteButton.setMinWidth(150);
		updateOrderButton.setMinWidth(150);

		buttonBox.getChildren().addAll(updateOrderButton, deleteButton);
		buttonBox.setAlignment(Pos.CENTER);

		containerTable.setContent(viewOrderDetail);
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


	public void setTable() {
		TableColumn<OrderItem, Integer> idColumn = new TableColumn<OrderItem, Integer>("ID");
		TableColumn<OrderItem, String> menuNameColumn = new TableColumn<OrderItem, String>("Menu Item Name");
		TableColumn<OrderItem, Integer> qtyColumn = new TableColumn<OrderItem, Integer>("Item Quantity");

		viewOrderDetail.getColumns().addAll(idColumn, menuNameColumn, qtyColumn);

		idColumn.setCellValueFactory(new PropertyValueFactory<>("orderItemId"));
		menuNameColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<OrderItem, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<OrderItem, String> param) {
						return new SimpleObjectProperty<>(param.getValue().getMenuItem().getMenuItemName());
						
					}
				}
				);
		
		qtyColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		
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

				if (orderItemSelected != null) {
					System.out.println(orderItemSelected.getOrderId().toString());
					System.out.println(orderItemSelected.getOrderItemId());
				}

			}
		};
	}

	public void eventListener(Stage stage) {

		// Update Order Listener
		updateOrderButton.setOnMouseClicked(e -> {
			if (orderItemSelected == null) {
				dg.informationDialog("Information", "Information", "Choose the data");
			} else {
				// confirmation to delete the data
				Boolean confirmationDelete = dg.confirmationDialog("Confirmation Dialog", "Confirmation", String.format(
						"Are you sure you want to update this order (OrderId: %d)?", orderItemSelected.getOrderId()));

				// if user click sure to delete the data
				if (confirmationDelete) {
					
					// ini order ID sama order Item Id buat update
					System.out.println(orderItemSelected.getOrderId().toString());
					System.out.println(orderItemSelected.getOrderItemId());
				}
			}
		});

		// Delete Button Listener
		deleteButton.setOnMouseClicked(e -> {

			// if user not choose the data
			if (orderItemSelected == null) {
				dg.informationDialog("Information", "Information", "Choose the data");
			} else {
				// confirmation to delete the data
				Boolean confirmationDelete = dg.confirmationDialog("Confirmation Dialog", "Confirmation",
						"Are you sure you want to delete this Order?");

				// if user click sure to delete the data
				if (confirmationDelete) {
					if(orderItemController.deleteOrderItem(orderItemSelected.getOrderItemId())) {
						dg.informationDialog("Information", "Information", "Order Item deleted");
						refreshTable();
					}
				}
			}

		});

		// Back to viewOrder page
		backButton.setOnMouseClicked(e -> {
			stage.setScene(new Scene(new ViewOrder(stage), 1366, 768));
		});
	}


	public ViewOrderDetail(Stage stage, Integer id) {
		// TODO Auto-generated constructor stub
		stage.setTitle("View Order Detail");
		this.id = id;
		initialize();
		style();
		eventListener(stage);
		setCenter(container);
	}

}
