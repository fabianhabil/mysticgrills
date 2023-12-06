package mysticgrills.controller;

public class Validation {

	private UserController userController = new UserController();			
	
	public String validationUser(String username, String email, String password, String passwordConf) {
		if(username.isBlank()) {
			return "Username cannot be empty";
		}
		
		if(email.isBlank()) {
			return "Email cannot be empty";
		}
		
		System.out.println(userController.getCountUserEmail(email));
		
		if(userController.getCountUserEmail(email) != 0) {
			return "Email is not unique";
		}
		
		if(password.length() < 6 || password.isBlank() || passwordConf.isBlank()) {
			return "Password must be 6 characters long";
		}
		
		if(!password.equals(passwordConf)) {
			return "Password must be same with confirm password";
		}
		
		return "Success";
	}

}
