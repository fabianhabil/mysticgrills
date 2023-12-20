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

}
