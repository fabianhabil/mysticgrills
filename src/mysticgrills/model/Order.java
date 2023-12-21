package mysticgrills.model;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import mysticgrills.DatabaseConnection;
import mysticgrills.GlobalState;

public class Order {

	private Integer orderId;
	private User orderUser;
	private ArrayList<OrderItem> orderItems;

	// Pending, Prepared, Served, Paid
	private String orderStatus;
	private Date orderDate;
	private Integer orderTotal;

	private DatabaseConnection db = DatabaseConnection.getInstance();
	private GlobalState globalState = GlobalState.getInstance();

	public Order(Integer orderId, User orderUser, ArrayList<OrderItem> orderItems, String orderStatus, Date orderDate,
			Integer orderTotal) {
		this.orderId = orderId;
		this.orderUser = orderUser;
		this.orderItems = orderItems;
		this.orderStatus = orderStatus;
		this.orderDate = orderDate;
		this.orderTotal = orderTotal;
	}

	public Order(Integer orderId, User orderUser, String orderStatus, Date orderDate, Integer orderTotal) {
		this.orderId = orderId;
		this.orderUser = orderUser;
		this.orderStatus = orderStatus;
		this.orderDate = orderDate;
		this.orderTotal = orderTotal;
		this.orderItems = new ArrayList<OrderItem>();
	}

	public Order() {

	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public User getOrderUser() {
		return orderUser;
	}

	public void setOrderUser(User orderUser) {
		this.orderUser = orderUser;
	}

	public ArrayList<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(ArrayList<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Integer getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(Integer orderTotal) {
		this.orderTotal = orderTotal;
	}

	// From visual paradigm there is one more parameter OrderItem, but no need for
	// creating order so we remove it because when creating OrderItem we call it
	// from OrderItemController
	public int createOrder(User orderUser, Date orderDate) {
		String query = String.format("INSERT INTO `orders`(`orderUser`, `orderStatus`, `orderDate`) VALUES (?,?,?)");
		PreparedStatement ps = db.preparedStatement(query, true);
		ResultSet rs;

		try {
			ps.setString(1, orderUser.getUserId().toString());
			ps.setString(2, "Pending");
			ps.setString(3, orderDate.toString());

			ps.executeUpdate();
			rs = ps.getGeneratedKeys();

			// Return the auto incremented ID
			if (rs.next()) {
				int idCreated = rs.getInt(1);
				return idCreated;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}

		return -1;
	}

	// So on the visual paradigm, we only get the OrderItemId and query all of them
	// one by one
	// For improving and saving resources to our databases, we just get all the
	// OrderItem and JOIN with all the relational table, so we get the OrderItem,
	// User and the MenuItem of the order
	public ArrayList<Order> getAllOrders(String role) {
		ArrayList<Order> orders = new ArrayList<>();
		Order currOrder = null;
		User currUser = globalState.getCurrentLoggedInUser();

		ResultSet rs = null;

		if (role.equals("Customer")) {
			rs = db.selectData(
					"SELECT * FROM `orders` INNER JOIN `users` ON orders.orderUser = users.userId INNER JOIN orderItems ON orders.orderId = orderItems.orderId INNER JOIN menuItems ON menuItems.menuItemId = orderItems.menuItem WHERE userId="
							+ currUser.getUserId() + " ORDER BY orders.orderId ASC");
		}

		if (role.equals("Chef")) {
			rs = db.selectData(
					"SELECT * FROM `orders` INNER JOIN `users` ON orders.orderUser = users.userId INNER JOIN orderItems ON orders.orderId = orderItems.orderId INNER JOIN menuItems ON menuItems.menuItemId = orderItems.menuItem WHERE orderStatus = \"Pending\"  ORDER BY orders.orderId ASC");
		}

		if (role.equals("Waiter")) {
			rs = db.selectData(
					"SELECT * FROM `orders` INNER JOIN `users` ON orders.orderUser = users.userId INNER JOIN orderItems ON orders.orderId = orderItems.orderId INNER JOIN menuItems ON menuItems.menuItemId = orderItems.menuItem WHERE orderStatus = \"Prepared\"  ORDER BY orders.orderId ASC");
		}

		if (role.equals("Cashier")) {
			rs = db.selectData(
					"SELECT * FROM `orders` INNER JOIN `users` ON orders.orderUser = users.userId INNER JOIN orderItems ON orders.orderId = orderItems.orderId INNER JOIN menuItems ON menuItems.menuItemId = orderItems.menuItem WHERE orderStatus = \"Served\"  ORDER BY orders.orderId ASC");
		}

		try {
			while (rs.next()) {
				// Order
				Integer orderId = rs.getInt("orderId");
				String status = rs.getString("orderStatus");
				Date date = rs.getDate("orderDate");
				Integer total = rs.getInt("orderTotal");

				// User
				Integer userId = rs.getInt("userId");
				String userRole = rs.getString("userRole");
				String userName = rs.getString("userName");
				String userEmail = rs.getString("userEmail");

				// Order Item
				Integer orderItemId = rs.getInt("orderItemId");
				Integer quantity = rs.getInt("quantity");

				// Menu Item
				Integer menuItemId = rs.getInt("menuItemId");
				String menuItemName = rs.getString("menuItemName");
				String menuItemDescription = rs.getString("menuItemDescription");
				Double menuItemPrice = rs.getDouble("menuItemPrice");

				// Construct Menu Item and Order Item to be added to the orderItems
				MenuItem menuItem = new MenuItem(menuItemId, menuItemName, menuItemDescription, menuItemPrice);
				OrderItem orderItem = new OrderItem(orderItemId, orderId, menuItem, quantity);

				// So on the visual paradigm, we only get the OrderItemId and query all of them
				// one by one
				// For improving and saving resources to our databases, we just get all the
				// OrderItem and JOIN with all the relational table, so we get the OrderItem,
				// User and the MenuItem of the order

				// If the current order pointer is null, initialize a new current order
				if (currOrder == null) {
					User user = new User(userId, userRole, userName, userEmail);
					currOrder = new Order(orderId, user, status, date, total);
				}

				else {
					// If the pointer is not null and the orderId is different (its the next row
					// that different orderId)

					if (currOrder.getOrderId() != orderId) {
						// Add the current pointer to the Array List
						orders.add(currOrder);

						// Initialize a new current order and loop again until all the orderItem is
						// added
						User user = new User(userId, userRole, userName, userEmail);
						currOrder = new Order(orderId, user, status, date, total);
					}
				}

				// Append orderItem to the currOrder
				currOrder.getOrderItems().add(orderItem);

			}

			// Last item need to be appended because its rs.next() and the last condition on
			// line 206 is not called, but check incase no data so its remains null
			if (currOrder != null) {
				orders.add(currOrder);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return orders;
	}

	public Boolean updateOrder(String role, Integer orderId) {
		String query = null;

		if (role.equals("Chef")) {
			query = String.format("UPDATE `orders` SET `orderStatus`= \"%s\" WHERE `orderId` = \"%s\"", "Prepared",
					orderId.toString());
		} else if (role.equals("Waiter")) {
			query = String.format("UPDATE `orders` SET `orderStatus`= \"%s\" WHERE `orderId` = \"%s\"", "Served",
					orderId.toString());
		} else if (role.equals("Cashier")) {
			query = String.format("UPDATE `orders` SET `orderStatus`= \"%s\" WHERE `orderId` = \"%s\"", "Paid",
					orderId.toString());
		}

		return db.execute(query);
	}

	public Boolean updateOrder(Integer orderId, Double total) {
		String query = String.format(
				"UPDATE `orders` SET `orderTotal`= \"%f\", `orderStatus`= \"%s\" WHERE `orderId` = \"%s\"", total,
				"Paid", orderId.toString());
		return db.execute(query);
	}

	public Boolean deleteOrder(Integer orderId) {
		String query = String.format("DELETE FROM `orders` WHERE `orderId` = \"%s\"", orderId);
		return db.execute(query);
	}

	// So on the visual paradigm, we only get the OrderItemId and query all of them
	// one by one
	// For improving and saving resources to our databases, we just get all the
	// OrderItem and JOIN with all the relational table, so we get the OrderItem,
	// User and the MenuItem of the order
	public Order getOrderByOrderId(Integer orderId) {
		Order order = null;

		ResultSet rs = db.selectData(
				"SELECT * FROM `orders` INNER JOIN `users` ON orders.orderUser = users.userId INNER JOIN orderItems ON orders.orderId = orderItems.orderId INNER JOIN menuItems ON menuItems.menuItemId = orderItems.menuItem WHERE orders.orderId="
						+ orderId);

		try {
			while (rs.next()) {
				// Order
				String status = rs.getString("orderStatus");
				Date date = rs.getDate("orderDate");
				Integer total = rs.getInt("orderTotal");

				// User
				Integer userId = rs.getInt("userId");
				String userRole = rs.getString("userRole");
				String userName = rs.getString("userName");
				String userEmail = rs.getString("userEmail");

				// Order Item
				Integer orderItemId = rs.getInt("orderItemId");
				Integer quantity = rs.getInt("quantity");

				// Menu Item
				Integer menuItemId = rs.getInt("menuItemId");
				String menuItemName = rs.getString("menuItemName");
				String menuItemDescription = rs.getString("menuItemDescription");
				Double menuItemPrice = rs.getDouble("menuItemPrice");

				// Construct Menu Item and Order Item to be added to the orderItems
				MenuItem menuItem = new MenuItem(menuItemId, menuItemName, menuItemDescription, menuItemPrice);
				OrderItem orderItem = new OrderItem(orderItemId, orderId, menuItem, quantity);

				// So on the visual paradigm, we only get the OrderItemId and query all of them
				// one by one
				// For improving and saving resources to our databases, we just get all the
				// OrderItem and JOIN with all the relational table, so we get the OrderItem,
				// User and the MenuItem of the order

				// If the current order pointer is null, initialize a new current order
				if (order == null) {
					User user = new User(userId, userRole, userName, userEmail);
					order = new Order(orderId, user, status, date, total);
				}

				// Append orderItem to the currOrder
				order.getOrderItems().add(orderItem);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return order;
	}

}
