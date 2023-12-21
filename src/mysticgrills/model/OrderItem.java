package mysticgrills.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import mysticgrills.DatabaseConnection;

public class OrderItem {

	private Integer orderItemId;
	private Integer orderId;
	private MenuItem menuItem;
	private Integer quantity;

	private DatabaseConnection db = DatabaseConnection.getInstance();

	public OrderItem(Integer orderId, MenuItem menuItem, Integer quantity) {
		this.orderId = orderId;
		this.menuItem = menuItem;
		this.quantity = quantity;
	}

	public OrderItem(Integer orderItemId, Integer orderId, MenuItem menuItem, Integer quantity) {
		this.orderItemId = orderItemId;
		this.orderId = orderId;
		this.menuItem = menuItem;
		this.quantity = quantity;
	}

	public OrderItem() {

	}

	public Integer getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Integer orderItemId) {
		this.orderItemId = orderItemId;
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

	public ArrayList<OrderItem> getOrderItemsByOrderId(Integer orderId) {
//		SELECT * FROM `orders` INNER JOIN `orderItems` ON orders.orderId = orderItems.orderId INNER JOIN `menuItems` ON menuItems.menuItemId = orderItems.menuItem WHERE orders.orderId = "1";
		ArrayList<OrderItem> orderDetails = new ArrayList<OrderItem>();

		String query = String.format(
				"SELECT * FROM `orders` INNER JOIN `orderItems` ON orders.orderId = orderItems.orderId INNER JOIN `menuItems` ON menuItems.menuItemId = orderItems.menuItem WHERE orders.orderId = \"%s\"",
				orderId);

		System.out.println(query);

		ResultSet rs = db.selectData(query);

		try {
			while (rs.next()) {
				Integer id = rs.getInt("orderItemId");
				Integer idOrder = rs.getInt("orderId");
				Integer qty = rs.getInt("quantity");
				Integer menuItemId = rs.getInt("menuItemId");
				String menuItemName = rs.getString("menuItemName");
				String menuItemDescription = rs.getString("menuItemDescription");
				Double menuItemPrice = rs.getDouble("menuItemPrice");

				MenuItem menuItem = new MenuItem(menuItemId, menuItemName, menuItemDescription, menuItemPrice);
				System.out.println(menuItemId + menuItemName + menuItemDescription + menuItemPrice);
				System.out.println(id + idOrder + qty);
				orderDetails.add(new OrderItem(id, idOrder, menuItem, qty));

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return orderDetails;
	}

	public Boolean deleteOrderItem(Integer orderItemId) {

		String query = String.format("DELETE FROM `orderItems` WHERE `orderItemId` = \"%s\"", orderItemId);
		return db.execute(query);
	}
}
