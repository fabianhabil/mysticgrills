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
		Pattern regex = Pattern.compile("\\d+(\\.\\d+)?");
		
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
	
	public String updateMenuItem(Integer menuItemId, String menuItemName, String menuItemDescription, String menuItemPrice) {
		Pattern regex = Pattern.compile("\\d+(\\.\\d+)?");

		if(menuItemName.isBlank()) {
			return "Menu Item Name cannot be empty";
		}
		
//		if(!oldName.equals(menuItemName) || )
		
		// check if user 
		// ganti nama ke username yang udh ada
		// namanya tetep ga berubah
		
//		if(menuItem.getCountUniqueItemName(menuItemName) != 0) {
//			return "Menu Item Name already in Database (Must be Unique)";
//		}
		
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
		
		if(menuItem.updateMenuItem(menuItemId, menuItemName, menuItemDescription, menuItemPrice)) {
			return "Your menu item has been successfully updated!!";
		} else {
			return "Error update to DB";
		}
	}
	
	public Boolean deleteMenuItem(Integer menuItemId) {
		return menuItem.deleteMenuItem(menuItemId);
		
	}
	
	public ArrayList<MenuItem> getAllMenuItems() {
		return menuItem.getAllMenuItems();
	}
}
