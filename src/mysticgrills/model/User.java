package mysticgrills.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

	public String createUser(String userRole, String userName, String userEmail, String userPassword,
			String passwordConfirm) {
		String query = String.format(
				"INSERT INTO `users`(`userRole`, `userName`, `userEmail`, `userPassword`) VALUES ('user','%s','%s','%s')",
				userName, userEmail, userPassword);
		if (!db.execute(query)) {
			return "Error insert to DB";
		}
		return "Your account has been successfully created. Please log in to access your account.";
	}

	public String authenticateUser(String userEmail, String userPassword) {
		String query = String.format("SELECT * FROM `users` WHERE `userEmail` = \"%s\" AND `userPassword` = \"%s\"",
				userEmail, userPassword);
		ResultSet rs = db.selectData(query);
		try {
			if (rs.next()) {
				if (rs.getString("userEmail").equals(userEmail) && rs.getString("userPassword").equals(userPassword)) {
					System.out.println(
							rs.getInt(1) + rs.getString(2) + rs.getString(3) + rs.getString(4) + rs.getString(5));
					global.addUser(new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
					return "Login success";
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Username or Password wrong";
	}

	public ArrayList<User> getUser() {
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
