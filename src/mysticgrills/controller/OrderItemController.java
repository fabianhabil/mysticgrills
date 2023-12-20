package mysticgrills.controller;

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

}
