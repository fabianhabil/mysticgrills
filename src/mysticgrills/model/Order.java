package mysticgrills.model;

import java.sql.Date;
import java.util.ArrayList;

public class Order {

	private Integer orderId;
	private User orderUser;
	private ArrayList<OrderItem> orderItems;
	private String orderStatus;
	private Date orderDate;
	private Integer orderTotal;
	
	public Order(Integer orderId, User orderUser, ArrayList<OrderItem> orderItems, String orderStatus, Date orderDate,
			Integer orderTotal) {
		this.orderId = orderId;
		this.orderUser = orderUser;
		this.orderItems = orderItems;
		this.orderStatus = orderStatus;
		this.orderDate = orderDate;
		this.orderTotal = orderTotal;
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
	
	

	
	
}
