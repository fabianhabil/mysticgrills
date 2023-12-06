package mysticgrills.model;

public class User {
	
	private Integer userId;
	private String userRole;
	private String userName;
	private String userEmail;
	private String userPassword;
	
	
	public User(Integer userId, String userRole, String userName, String userEmail, String userPassword) {
		this.userId = userId;
		this.userRole = userRole;
		this.userName = userName;
		this.userEmail = userEmail;
		this.userPassword = userPassword;
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
	
	

}
