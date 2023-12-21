package mysticgrills.controller;

import java.util.ArrayList;

import mysticgrills.DatabaseConnection;
import mysticgrills.model.MenuItem;
import mysticgrills.model.OrderItem;

public class OrderItemController {

	private DatabaseConnection db;
	private OrderItem orderItem;
	private static OrderItemController oic;

	public OrderItemController() {
		orderItem = new OrderItem();
		db = DatabaseConnection.getInstance();
	}

	public static OrderItemController getInstance() {
		if (oic == null) {
			oic = new OrderItemController();
		}

		return oic;
	}

	public Boolean createOrderItem(int orderId, MenuItem menuItem, int quantity) {
		return orderItem.createOrderItem(orderId, menuItem, quantity);
	}

	public ArrayList<OrderItem> getOrderItemsByOrderId(Integer orderId) {
		return orderItem.getOrderItemsByOrderId(orderId);
	}
	
	public Boolean deleteOrderItem(Integer orderItemId) {
		return orderItem.deleteOrderItem(orderItemId);
	}
}
