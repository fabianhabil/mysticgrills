package mysticgrills.controller;

import java.sql.ResultSet;
import java.sql.SQLException;

import mysticgrills.DatabaseConnection;
import mysticgrills.model.User;

public class UserController {
	
	private DatabaseConnection db = DatabaseConnection.getInstance();
	User user = new User();
	
	public String createUser(String userRole, String userName, String userEmail, String userPassword, String passwordConfirm) {
		
		if(userName.isBlank()) {
			return "Username cannot be empty";
		}
		
		if(userEmail.isBlank()) {
			return "Email cannot be empty";
		}
		
		if(getCountUserEmail(userEmail) != 0) {
			return "Email is not unique";
		}
		
		if(userPassword.length() < 6 || userPassword.isBlank() || passwordConfirm.isBlank()) {
			return "Password must be 6 characters long";
		}
		
		if(!userPassword.equals(passwordConfirm)) {
			return "Password must be same with confirm password";
		}
		
		String status = user.createUser(userRole, userName, userEmail, userPassword, passwordConfirm);
		return status;
	}
	
	public String authenticateUser(String userEmail, String userPassword) {
		if(userEmail.isBlank()) {
			return "Email must be filled";
		}
		
		if(userPassword.isBlank()) {
			return "Password must be filled";
		}
		
		if(getCountUserEmail(userEmail) < 1) {
			return "Email doesn't exist";
		}
		
		String status = user.authenticateUser(userEmail, userPassword);
	
		return status;
	}
	
	public int getCountUserEmail(String userEmail) {

		String query = String.format("SELECT COUNT(`userEmail`) FROM `users` WHERE `userEmail` = \"%s\"", userEmail);
		
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
