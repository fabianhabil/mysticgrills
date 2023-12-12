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

	public String createUser(String userRole, String userName, String userEmail, String userPassword,
			String passwordConfirm) {

		Pattern regex = Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
				+ "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");

		if (userName.isBlank()) {
			return "Username cannot be empty";
		}

		if (userEmail.isBlank()) {
			return "Email cannot be empty";
		}

		if (getCountUserEmail(userEmail) != 0) {
			return "Email is not unique";
		}

		Matcher matcher = regex.matcher(userEmail);

		if (!matcher.matches()) {
			return "Email is not valid!";
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
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return count;
	}

	public ArrayList<User> getAllUser() {
		return user.getAllUser();
	}

}
