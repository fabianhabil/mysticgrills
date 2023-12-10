package mysticgrills.model;

public class MenuItem {

	private Integer menuItemId;
	private String menuItemName;
	private String menuItemDesc;
	private String menuItemPrice;

	public MenuItem(Integer menuItemId, String menuItemName, String menuItemDesc, String menuItemPrice) {
		this.menuItemId = menuItemId;
		this.menuItemName = menuItemName;
		this.menuItemDesc = menuItemDesc;
		this.menuItemPrice = menuItemPrice;
	}

	public Integer getMenuItemId() {
		return menuItemId;
	}

	public void setMenuItemId(Integer menuItemId) {
		this.menuItemId = menuItemId;
	}

	public String getMenuItemName() {
		return menuItemName;
	}

	public void setMenuItemName(String menuItemName) {
		this.menuItemName = menuItemName;
	}

	public String getMenuItemDesc() {
		return menuItemDesc;
	}

	public void setMenuItemDesc(String menuItemDesc) {
		this.menuItemDesc = menuItemDesc;
	}

	public String getMenuItemPrice() {
		return menuItemPrice;
	}

	public void setMenuItemPrice(String menuItemPrice) {
		this.menuItemPrice = menuItemPrice;
	}

}
