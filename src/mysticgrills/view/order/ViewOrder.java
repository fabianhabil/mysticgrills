package mysticgrills.view.order;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public class ViewOrder extends BorderPane {

	VBox container, topContainer, titleContainer, buttonContainer, cartContainer;
	HBox buttonBox, tableCart;
	ScrollPane containerTable;
	ScrollPane scrollableCart;
	TableView<Object> viewOrder;
	ArrayList<Object> orders;
	Button addCartButton, calculateButton, createOrderButton, backButton;
	GridPane formBox;
	TextField quantityTextField;
	Label nameLbl, quantityLbl, priceLbl, totalLbl;
	Dialog dg;
	Text title, cartTitle, itemName, itemPrice, itemTotal, grandTotal;
	int total;

	private OrderController orderController = OrderController.getInstance();
	private OrderItemController orderItemController = OrderItemController.getInstance();
	private GlobalState globalState = GlobalState.getInstance();

	// We will use hashmap for temporary cart system, since we dont want two items
	// with two different quantity for better complexity and looping behaviour
//	private HashMap<Integer, Order> newOrderItems;
//	private MenuItem menuItemSelected;

	public void initialize() {
//		newOrderItems = new HashMap<Integer, OrderItem>();

		scrollableCart = new ScrollPane();
		containerTable = new ScrollPane();

		formBox = new GridPane();
		nameLbl = new Label("Name");
		quantityLbl = new Label("Quantity");
		priceLbl = new Label("Price");
		totalLbl = new Label("Total");

		quantityTextField = new TextField();

		topContainer = new VBox(8);
		container = new VBox(8);
		titleContainer = new VBox();
		buttonBox = new HBox(8);
		buttonContainer = new VBox(8);
		tableCart = new HBox(16);
		cartContainer = new VBox(8);

		addCartButton = new Button("Add To Cart");
		calculateButton = new Button("Calculate Price");
		createOrderButton = new Button("Create Order");
		backButton = new Button("Back");

		title = new Text("View Order");
		cartTitle = new Text("Your Cart");
		itemName = new Text("");
		itemPrice = new Text("");
		itemTotal = new Text("");
		grandTotal = new Text("");

		viewOrder = new TableView<Object>();
		orders = new ArrayList<Object>();
		dg = new Dialog();

		setTable();

		total = 0;
	}

	public void style() {
		title.setFont(new Font(24));
		title.setTextAlignment(TextAlignment.CENTER);
		cartTitle.setFont(new Font(18));
		cartTitle.setTextAlignment(TextAlignment.CENTER);
		cartTitle.minWidth(600);

		titleContainer.getChildren().add(title);
		titleContainer.setAlignment(Pos.CENTER);

		viewOrder.setMaxHeight(600);
		viewOrder.setPadding(new Insets(10, 10, 10, 10));

		formBox.setPadding(new Insets(10, 10, 10, 10));
		formBox.add(nameLbl, 0, 0);
		formBox.add(quantityLbl, 0, 1);
		formBox.add(priceLbl, 0, 2);
		formBox.add(totalLbl, 0, 3);

		formBox.setHgap(100);
		formBox.setVgap(10);

		formBox.add(itemName, 1, 0);
		formBox.add(quantityTextField, 1, 1);
		formBox.add(itemPrice, 1, 2);
		formBox.add(itemTotal, 1, 3);

		formBox.setAlignment(Pos.CENTER);

		formBox.setVisible(false);

		addCartButton.setMinWidth(150);
		calculateButton.setMinWidth(150);
		createOrderButton.setMinWidth(350);

		buttonBox.getChildren().addAll(addCartButton, calculateButton);
		buttonBox.setAlignment(Pos.CENTER);

		containerTable.setContent(viewOrder);
		containerTable.setHbarPolicy(ScrollBarPolicy.NEVER);
		containerTable.setMinWidth(700);
		containerTable.setFitToWidth(true);

		buttonContainer.getChildren().setAll(buttonBox, backButton);
		buttonContainer.setAlignment(Pos.CENTER);

		tableCart.getChildren().setAll(containerTable, scrollableCart);
		tableCart.setAlignment(Pos.CENTER);

		cartContainer.getChildren().setAll(cartTitle);
		cartContainer.setMinWidth(500);

		scrollableCart.setContent(cartContainer);
		scrollableCart.setHbarPolicy(ScrollBarPolicy.NEVER);
		scrollableCart.setMinWidth(500);
		scrollableCart.setFitToWidth(true);
		scrollableCart.setMaxHeight(500);
		scrollableCart.getStyleClass().clear();

		container.getChildren().setAll(titleContainer, tableCart, formBox, buttonContainer);
		container.setPadding(new Insets(16));

	}
	
	public void setTable() {
		TableColumn<Object, Integer> idColumn = new TableColumn<Object, Integer>("ID");
		TableColumn<Object, User> nameColumn = new TableColumn<Object, User>("User Name");
		TableColumn<Object, String> statusColumn = new TableColumn<Object, String>("Status");
		TableColumn<Object, Double> priceColumn = new TableColumn<Object, Double>("Total Price");

		viewOrder.getColumns().addAll(idColumn, nameColumn, statusColumn, priceColumn);
		
//		viewOrder.getColumns().addAll(idColumn, statusColumn, priceColumn);

		idColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("orderUser"));
		statusColumn.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));
		priceColumn.setCellValueFactory(new PropertyValueFactory<>("orderTotal"));

		refreshTable();
	}

	public void refreshTable() {
		orders = orderController.getAllOrders(globalState.getCurrentLoggedInUser().getUserRole());
		viewOrder.getItems().clear();
		viewOrder.getItems().addAll(orders);
//		viewOrder.setOnMouseClicked(tableMouseEvent());
	}

	// Iterate through the hashmap and render all the items in cart
//	public void refreshCart() {
//		cartContainer.getChildren().setAll(cartTitle);
//		Text grandTotal = new Text("");
//
//		for (Integer id : newOrderItems.keySet()) {
//			VBox container = new VBox(4);
//
//			OrderItem item = newOrderItems.get(id);
//			MenuItem menu = item.getMenuItem();
//			Button deleteMenu = new Button("Delete Item");
//
//			container.maxWidth(350);
//
//			Text name = new Text("Menu Name: " + menu.getMenuItemName());
//			Text quantity = new Text("Quantity: " + item.getQuantity() + " pcs");
//			Text price = new Text("Price: Rp " + menu.getMenuItemPrice());
//			Text totalText = new Text("Total: Rp " + (item.getQuantity() * menu.getMenuItemPrice()));
//
//			// Remove items from the hashmap and rerender the cart
//			deleteMenu.setOnMouseClicked(e -> {
//				// Count the grand total of the cart
//				total = (int) (total - menu.getMenuItemPrice() * item.getQuantity());
//
//				newOrderItems.remove(id);
//				refreshCart();
//			});
//
//			container.getChildren().addAll(name, quantity, price, totalText, deleteMenu);
//
//			cartContainer.getChildren().add(container);
//		}
//
//		grandTotal.setText("Grand Total of the Cart: Rp " + total);
//		grandTotal.setFont(new Font(16));
//
//		cartContainer.getChildren().addAll(grandTotal, createOrderButton);
//	}

	// to handle if user press a row in table
//	private EventHandler<MouseEvent> tableMouseEvent() {
//		return new EventHandler<MouseEvent>() {
//
//			// get user data from pressed table row
//			@Override
//			public void handle(MouseEvent event) {
//				TableSelectionModel<MenuItem> tableSelectionModel = viewMenuItem.getSelectionModel();
//				tableSelectionModel.setSelectionMode(SelectionMode.SINGLE);
//
//				// get data from selected model
//				// hide first the calculate price x quantity because if clicked, reset the
//				// quantity
//				menuItemSelected = tableSelectionModel.getSelectedItem();
//				quantityTextField.setText("");
//				totalLbl.setVisible(false);
//				itemTotal.setVisible(false);
//
//				// display data in box to notice if user want to add to cart or no
//				if (menuItemSelected != null) {
//					itemName.setText(menuItemSelected.getMenuItemName());
//					itemPrice.setText(menuItemSelected.getMenuItemPrice().toString());
//					formBox.setVisible(true);
//				}
//
//			}
//		};
//	}

//	public void eventListener(Stage stage) {
//		// create the order and save all the order item to the database
//		createOrderButton.setOnMouseClicked(e -> {
//			long millis = System.currentTimeMillis();
//			Date date = new java.sql.Date(millis);
//			int newOrderId = orderController.createOrder(globalState.getCurrentLoggedInUser(), date);
//
//			for (Integer id : newOrderItems.keySet()) {
//				OrderItem item = newOrderItems.get(id);
//				MenuItem menu = item.getMenuItem();
//
//				Boolean isSuccess = orderItemController.createOrderItem(newOrderId, menu, item.getQuantity());
//
//				if (!isSuccess) {
//					dg.informationDialog("Error", "Error Message", "Error adding new order!");
//					return;
//				}
//			}
//
//			if (dg.informationDialog("Success", "Success", "Order Created!")) {
//				stage.setScene(new Scene(new CustomerHome(stage), 1366, 768));
//			}
//		});
//
//		// add selected item and quantity that user input to the cart
//		addCartButton.setOnMouseClicked(e -> {
//			Boolean quantityValidated = validateQuantity(quantityTextField.getText());
//
//			if (menuItemSelected == null) {
//				dg.informationDialog("Error", "Error Message", "Please choose an item first!");
//			}
//
//			else {
//				if (quantityValidated) {
//					// Input from quantity textfield is a number and we add to cart
//					int quantity = Integer.parseInt(quantityTextField.getText());
//
//					if (quantity == 0) {
//						dg.informationDialog("Error", "Error Message", "Quantity cant be 0!");
//						return;
//					}
//
//					OrderItem orderItem = newOrderItems.get(menuItemSelected.getMenuItemId());
//
//					if (orderItem != null) {
//						quantity += orderItem.getQuantity();
//					}
//
//					OrderItem newOrderItem = new OrderItem(menuItemSelected.getMenuItemId(), menuItemSelected,
//							quantity);
//
//					newOrderItems.put(menuItemSelected.getMenuItemId(), newOrderItem);
//
//					// Reset the form box and the selected item after add to cart
//					quantityTextField.setText("");
//					itemName.setText("");
//					itemPrice.setText("");
//					formBox.setVisible(false);
//					menuItemSelected = null;
//
//					// Count the grand total of the cart
//					total += newOrderItem.getMenuItem().getMenuItemPrice() * newOrderItem.getQuantity();
//
//					// Refresh the cart
//					refreshCart();
//				} else {
//					// Input from quantity textfield is invalid, return notice error
//					dg.informationDialog("Error", "Error Message", "Quantity cant be empty and must be a number!");
//				}
//			}
//		});
//
//		// Calculate Button Listener
//		calculateButton.setOnMouseClicked(e -> {
//			Boolean quantityValidated = validateQuantity(quantityTextField.getText());
//
//			if (quantityValidated) {
//				// Input from quantity textfield is a number and we show the calculated price
//				Double total = Integer.parseInt(quantityTextField.getText()) * menuItemSelected.getMenuItemPrice();
//				itemTotal.setText(total.toString());
//				totalLbl.setVisible(true);
//				itemTotal.setVisible(true);
//			} else {
//				// Input from quantity textfield is invalid, return notice error
//				dg.informationDialog("Error", "Error Message", "Quantity cant be empty and must be a number!");
//			}
//
//		});
//
//		// Back to home customer menu
//		backButton.setOnMouseClicked(e -> {
//			stage.setScene(new Scene(new CustomerHome(stage), 1366, 768));
//		});
//	}

	// Helper to check if quantity not empty and must be a number
//	public boolean validateQuantity(String input) {
//		Pattern regex = Pattern.compile("\\d+(\\.\\d+)?");
//		if (!input.isBlank()) {
//			Matcher matcher = regex.matcher(input);
//			return matcher.matches();
//		}
//		return false;
//	}

	public ViewOrder(Stage stage) {
		stage.setTitle("View Order");
		initialize();
		style();
//		eventListener(stage);
		setCenter(container);
	}

}
