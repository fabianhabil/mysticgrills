package mysticgrills.controller;

import java.sql.Date;
import java.util.ArrayList;

import mysticgrills.model.Order;
import mysticgrills.model.User;

public class OrderController {
	private Order order;
	private static OrderController oc;

	public OrderController() {
		order = new Order();
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

	// So on the visual paradigm, we only get the OrderItemId and query all of them
	// one by one
	// For improving and saving resources to our databases, we just get all the
	// OrderItem and JOIN with all the relational table, so we get the OrderItem,
	// User and the MenuItem of the order
	public ArrayList<Order> getAllOrders(String role) {
		return order.getAllOrders(role);
	}

	public Boolean updateOrder(String role, Integer orderId) {
		return order.updateOrder(role, orderId);
	}

	public Boolean deleteOrder(Integer orderId) {
		return order.deleteOrder(orderId);
	}

	// So on the visual paradigm, we only get the OrderItemId and query all of them
	// one by one
	// For improving and saving resources to our databases, we just get all the
	// OrderItem and JOIN with all the relational table, so we get the OrderItem,
	// User and the MenuItem of the order
	public Order getOrderByOrderId(Integer orderId) {
		return order.getOrderByOrderId(orderId);
	}

	public Boolean updateOrder(Integer orderId, Double total) {
		return order.updateOrder(orderId, total);
	}

}
