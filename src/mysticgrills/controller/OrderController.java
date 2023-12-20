package mysticgrills.controller;

import java.sql.Date;
import java.util.ArrayList;

import mysticgrills.DatabaseConnection;
import mysticgrills.model.Order;
import mysticgrills.model.OrderItem;
import mysticgrills.model.User;

public class OrderController {
	private DatabaseConnection db;
	private Order order;
	private static OrderController oc;

	public OrderController() {
		order = new Order();
		db = DatabaseConnection.getInstance();
	}

	public static OrderController getInstance() {
		if (oc == null) {
			oc = new OrderController();
		}

		return oc;
	}

	// From visual paradigm there is one more parameter OrderItem, but no need for
	// creating order so we remove it because when creating OrderItem we call it
	// from OrderItemController
	public int createOrder(User orderUser, Date orderDate) {
		int orderId = order.createOrder(orderUser, orderDate);

		return orderId;
	}

	public ArrayList<Order> getAllOrders(String role) {
		return order.getAllOrders(role);
	}
	
	public Boolean updateOrder(String role, Integer orderId) {
		return order.updateOrder(role, orderId);
	}

}
