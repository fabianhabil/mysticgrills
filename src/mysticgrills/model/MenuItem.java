package mysticgrills.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import mysticgrills.DatabaseConnection;
import mysticgrills.GlobalState;

public class MenuItem {

	private Integer menuItemId;
	private String menuItemName;
	private String menuItemDescription;
	private double menuItemPrice;
	
	private DatabaseConnection db = DatabaseConnection.getInstance();
	private GlobalState global = GlobalState.getInstance();
	
	public MenuItem() {
		
	}

	public MenuItem(Integer menuItemId, String menuItemName, String menuItemDescription, double menuItemPrice) {
		this.menuItemId = menuItemId;
		this.menuItemName = menuItemName;
		this.menuItemDescription = menuItemDescription;
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
		return menuItemDescription;
	}

	public void setMenuItemDesc(String menuItemDescription) {
		this.menuItemDescription = menuItemDescription;
	}

	public double getMenuItemPrice() {
		return menuItemPrice;
	}

	public void setMenuItemPrice(Float menuItemPrice) {
		this.menuItemPrice = menuItemPrice;
	}
	
	public String createMenuItem(String menuItemName, String menuItemDescription, double menuItemPrice) {
		String query = String.format("INSERT INTO `menuItems`(`menuItemName`, `menuItemDescription`, `menuItemPrice`) VALUES ('%s','%s','%s')", menuItemName, menuItemDescription, menuItemPrice);
		if(!db.execute(query)) {
			return "Error insert to DB";
		}
		return "Menu Item has been successfully created";
	}
	
	public ArrayList<MenuItem> getAllMenuItems() {
		ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();

		ResultSet rs = db.selectData("SELECT * FROM menuItems");

		try {
			while (rs.next()) {
				Integer id = rs.getInt("menuItemId");
				String name = rs.getString("menuItemName");
				String desc = rs.getString("menuItemDescription");
				Float price = rs.getFloat("MenuItemPrice");
				System.out.println(id + " " + name + " " + desc + " " + price);
				menuItems.add(new MenuItem(id, name, desc, price));

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return menuItems;
	}
	
	public Integer getCountUniqueItemName(String menuItemName) {
		String query = String.format("SELECT COUNT(`menuItemName`) FROM `menuItems` WHERE `menuItemName` = \"%s\"", menuItemName);
		ResultSet rs = db.selectData(query);
		int count = 0;
		try {
			if(rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return count;
	}

}
