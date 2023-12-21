package mysticgrills.controller;

import java.util.ArrayList;

import mysticgrills.model.MenuItem;
import mysticgrills.model.OrderItem;

public class OrderItemController {

	private OrderItem orderItem;
	private static OrderItemController oic;

	public OrderItemController() {
		orderItem = new OrderItem();
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

	public Boolean updateOrderItem(Integer orderItemId, Integer quantity) {
		return orderItem.updateOrderItem(orderItemId, quantity);
	}
}
