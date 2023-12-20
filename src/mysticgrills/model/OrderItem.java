package mysticgrills.model;

import mysticgrills.DatabaseConnection;

public class OrderItem {

	private Integer orderId;
	private MenuItem menuItem;
	private Integer quantity;

	private DatabaseConnection db = DatabaseConnection.getInstance();

	public OrderItem(Integer orderId, MenuItem menuItem, Integer quantity) {
		this.orderId = orderId;
		this.menuItem = menuItem;
		this.quantity = quantity;
	}

	public OrderItem() {

	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public MenuItem getMenuItem() {
		return menuItem;
	}

	public void setMenuItem(MenuItem menuItem) {
		this.menuItem = menuItem;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Boolean createOrderItem(int orderId, MenuItem menuItem, int quantity) {
		String query = String.format(
				"INSERT INTO `orderItems`(`orderId`, `menuItem`, `quantity`) VALUES ('%s','%s','%s')", orderId,
				menuItem.getMenuItemId(), quantity);

		if (!db.execute(query)) {
			return false;
		}

		return true;
	}

}
