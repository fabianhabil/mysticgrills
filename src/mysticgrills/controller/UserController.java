package mysticgrills.controller;

import java.sql.ResultSet;
import java.sql.SQLException;

import mysticgrills.DatabaseConnection;

public class UserController {
	private DatabaseConnection db = DatabaseConnection.getInstance();
	
	public String createUser(String userRole, String userName, String userEmail, String userPassword, String passwordConfirm) {
		Validation validate = new Validation();
		
		String validation = validate.validationUser(userName, userEmail, userPassword, passwordConfirm);
		
		if(!validation.equals("Success")) {
			return validation;
		}
		
		String query = String.format("INSERT INTO `users`(`userRole`, `userName`, `userEmail`, `userPassword`) VALUES ('user','%s','%s','%s')", userName, userEmail, userPassword);
		if(!db.execute(query)) {
			return "Error insert";
		}
		return "DB Insert";
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
