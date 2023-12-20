package mysticgrills.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.scene.Scene;
import mysticgrills.DatabaseConnection;
import mysticgrills.GlobalState;

public class User {

	private Integer userId;
	private String userRole;
	private String userName;
	private String userEmail;
	private String userPassword;

	private DatabaseConnection db = DatabaseConnection.getInstance();
	private GlobalState global = GlobalState.getInstance();

	public User() {

	}

	public User(Integer userId, String userRole, String userName, String userEmail) {
		super();
		this.userId = userId;
		this.userRole = userRole;
		this.userName = userName;
		this.userEmail = userEmail;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	// Insert user to database
	public String createUser(String userRole, String userName, String userEmail, String userPassword,
			String passwordConfirm) {
		String query = String.format(
				"INSERT INTO `users`(`userRole`, `userName`, `userEmail`, `userPassword`) VALUES ('Customer','%s','%s','%s')",
				userName, userEmail, userPassword);
		if (!db.execute(query)) {
			return "Error insert to DB";
		}
		return "Your account has been successfully created. Please log in to access your account.";
	}

	// Login function for user
	public String authenticateUser(String userEmail, String userPassword) {
		String query = String.format("SELECT * FROM `users` WHERE `userEmail` = ? AND `userPassword` = ?");
		PreparedStatement ps = db.preparedStatement(query);
		ResultSet rs;

		try {
			ps.setString(1, userEmail);
			ps.setString(2, userPassword);
			rs = ps.executeQuery();
			// If match 100% its the same username and password, so user successfully logged
			// in if the data is present on the database
			if (rs.next()) {
				global.addUser(new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
				return "Login Success";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return "Wrong Password";
	}

	// Update User
	public Boolean updateUser(Integer userId, String newUserRole) {
		String query = String.format("UPDATE `users` SET `userRole`= \"%s\" WHERE `userId` = \"%s\"", newUserRole,
				userId);
		return db.execute(query);
	}

	// Delete User
	public Boolean deleteUser(Integer userId) {
		String query = String.format("DELETE FROM `users` WHERE `userId` = \"%s\"", userId);
		return db.execute(query);
	}

	// Get All User
	public ArrayList<User> getAllUser() {
		ArrayList<User> users = new ArrayList<User>();

		ResultSet rs = db.selectData("SELECT * FROM users");

		try {
			while (rs.next()) {
				Integer id = rs.getInt("userId");
				String role = rs.getString("userRole");
				String name = rs.getString("userName");
				String email = rs.getString("userEmail");
				System.out.println(id + " " + role + " " + name + " " + email);
				users.add(new User(id, role, name, email));

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return users;
	}

}
