package mysticgrills;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

	private final String username = "root";
	private final String password = "";
	private final String database = "mysticgrills";
	private final String host = "localhost:3306";
	private final String connection = String.format(
			"jdbc:mysql://%s/%s", host, database);
	
	private Connection con;
	private Statement st;
	
	public ResultSet rs;
	public static DatabaseConnection connect;
	
	public  static DatabaseConnection getInstance() {
		if(connect == null) {
			System.out.println("Database connect dibuat");
			return new DatabaseConnection();
		}
		return connect;
	}
	
	private DatabaseConnection() {
		try {
			con = DriverManager.getConnection(connection, username, password);
			st = con.createStatement();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public ResultSet selectData(String query) {
		try {
			rs = st.executeQuery(query);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return rs;
	}
	
	public void execute(String query) {
		try {
			st.executeUpdate(query);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
//	for avoid sql injection
	public PreparedStatement preparedStatement(String query) {
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ps;
		
	}
}
