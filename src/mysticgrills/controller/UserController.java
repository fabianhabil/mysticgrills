package mysticgrills.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mysticgrills.DatabaseConnection;
import mysticgrills.model.User;

public class UserController {

	private DatabaseConnection db;
	private static UserController uc;
	User user;

	private UserController() {
		user = new User();
		db = DatabaseConnection.getInstance();
	}

	public static UserController getUC() {
		if (uc == null) {
			uc = new UserController();
		}

		return uc;
	}

	// Helper to check if a string is an email
	public Boolean isValidEmail(String checkEmail) {
		// For Email we check the @ character and make sure not on the first or last
		// character
		int atIndex = checkEmail.indexOf('@');
		if (atIndex <= 0 || atIndex == checkEmail.length() - 1) {
			return false;
		}

		// Check if . character exists and not comes after @ character
		int dotIndex = checkEmail.indexOf('.', atIndex);
		if (dotIndex == -1 || dotIndex == checkEmail.length() - 1) {
			return false;
		}

		return true;
	}

	public String createUser(String userRole, String userName, String userEmail, String userPassword,
			String passwordConfirm) {

		if (userName.isBlank()) {
			return "Username cannot be empty";
		}

		if (userEmail.isBlank()) {
			return "Email cannot be empty";
		}

		if (getCountUserEmail(userEmail) != 0) {
			return "Email is not unique";
		}

		if (!isValidEmail(userEmail)) {
			return "Invalid Email!";
		}

		if (userPassword.length() < 6 || userPassword.isBlank() || passwordConfirm.isBlank()) {
			return "Password must be 6 characters long";
		}

		if (!userPassword.equals(passwordConfirm)) {
			return "Password must be same with confirm password";
		}

		String status = user.createUser(userRole, userName, userEmail, userPassword, passwordConfirm);

		return status;
	}

	public String authenticateUser(String userEmail, String userPassword) {
		if (userEmail.isBlank()) {
			return "Email must be filled";
		}

		if (userPassword.isBlank()) {
			return "Password must be filled";
		}

		if (getCountUserEmail(userEmail) < 1) {
			return "Email is not registered";
		}

		String status = user.authenticateUser(userEmail, userPassword);

		return status;
	}

	public int getCountUserEmail(String userEmail) {

		String query = String.format("SELECT COUNT(`userEmail`) FROM `users` WHERE `userEmail` = \"%s\"", userEmail);

		ResultSet rs = db.selectData(query);
		int count = 0;
		try {
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return count;
	}

	public Boolean updateUser(Integer userId, String newUserRole) {
		if (user.updateUser(userId, newUserRole)) {
			return true;
		}
		return false;
	}

	public Boolean deleteUser(Integer userId) {

		// check userId from DB and Delete from DB
		if (getCountUserId(userId) == 1 && user.deleteUser(userId)) {
			return true;
		}

		return false;
	}

	public int getCountUserId(Integer userId) {
		String query = String.format("SELECT COUNT(`userId`) FROM `users` WHERE `userId` = \"%s\"", userId);
		int count = 0;
		ResultSet rs = db.selectData(query);
		try {
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		System.out.println(count);
		return count;
	}

	public ArrayList<User> getAllUser() {
		return user.getAllUser();
	}

}
