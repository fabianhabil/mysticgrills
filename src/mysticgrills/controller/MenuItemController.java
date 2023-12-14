package mysticgrills.controller;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mysticgrills.DatabaseConnection;
import mysticgrills.model.MenuItem;

public class MenuItemController {
	private DatabaseConnection db;
	private static MenuItemController mc;
	MenuItem menuItem;

	private MenuItemController() {
		menuItem = new MenuItem();
		db = DatabaseConnection.getInstance();
	}

	public static MenuItemController getMC() {
		if (mc == null) {
			mc = new MenuItemController();
		}

		return mc;
	}
	
	public String createMenuItem(String menuItemName, String menuItemDescription, String menuItemPrice) {
		Pattern regex = Pattern.compile("^(?:\\d{1,2})?(?:\\.\\d{1,2})?");
		
		if(menuItemName.isBlank()) {
			return "Menu Item Name cannot be empty";
		}
		
		if(menuItem.getCountUniqueItemName(menuItemName) != 0) {
			return "Menu Item Name already in Database (Must be Unique)";
		}
		
		if(menuItemDescription.length() <= 10) {
			return "Menu Item Desc must be more than 10 characters";
		}
		
		if(menuItemPrice.isBlank()) {
			return "Price cannot be empty";
		}
		
		Matcher matcher = regex.matcher(menuItemPrice);

		if (!matcher.matches()) {
			return "Price must be number";
		}
		
		if(Double.parseDouble(menuItemPrice) < 2.5) {
			return "Menu Item Price must be more than or equal 2.5";
		}
		
		return menuItem.createMenuItem(menuItemName, menuItemDescription, Double.parseDouble(menuItemPrice));
	}
	
	public ArrayList<MenuItem> getAllMenuItems() {
		return menuItem.getAllMenuItems();
	}
}
