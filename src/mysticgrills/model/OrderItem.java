package mysticgrills.model;

public class OrderItem {
	
	private Integer orderId;
	private MenuItem menuItem;
	private Integer quantity;
	
	public OrderItem(Integer orderId, MenuItem menuItem, Integer quantity) {
		this.orderId = orderId;
		this.menuItem = menuItem;
		this.quantity = quantity;
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
	
	
	
}
