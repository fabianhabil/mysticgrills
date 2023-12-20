package mysticgrills.model;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import mysticgrills.DatabaseConnection;

public class Order {

	private Integer orderId;
	private User orderUser;
	private ArrayList<OrderItem> orderItems;

	// Pending, Prepared, Served, Paid
	private String orderStatus;
	private Date orderDate;
	private Integer orderTotal;

	private DatabaseConnection db = DatabaseConnection.getInstance();

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

	public ArrayList<Order> getAllOrders(String role) {
		// TODO Auto-generated method stub
		ArrayList<Order> orders = new ArrayList<>();

		ResultSet rs = null;

		if (role.equals("Chef")) {
			rs = db.selectData(
					"SELECT * FROM `orders` INNER JOIN `users` ON orders.orderUser = users.userId WHERE orderStatus = \"Pending\"");
		}

		try {
			while (rs.next()) {
				Integer id = rs.getInt("orderId");
				String status = rs.getString("OrderStatus");
				Date date = rs.getDate("OrderDate");
				Integer total = rs.getInt("orderTotal");
				Integer userId = rs.getInt("userId");
				String userRole = rs.getString("userRole");
				String userName = rs.getString("userName");
				String userEmail = rs.getString("userEmail");
				System.out.println("order: " + id + " " + status + " " + date + " " + total);
				System.out.println("user: " + userId + " " + userRole + " " + userName + " " + userEmail);
				User user = new User(userId, userRole, userName, userEmail);
				orders.add(new Order(id, user, status, date, total));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return orders;
	}

}
