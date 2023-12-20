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
	private final String connection = String.format("jdbc:mysql://%s/%s", host, database);

	private Connection con;
	private Statement st;

	public ResultSet rs;
	public static DatabaseConnection connect = null;

	public static DatabaseConnection getInstance() {
		if (connect == null) {
			System.out.println("Database Connected");
			connect = new DatabaseConnection();
			return connect;
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

	// For SELECT query
	public ResultSet selectData(String query) {
		try {
			rs = st.executeQuery(query);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return rs;
	}

	// For UPDATE, DELETE query
	public Boolean execute(String query) {
		try {
			st.executeUpdate(query);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	// For all query and avoid SQL Injection
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

	// For all query and avoid SQL Injection return the created ID
	public PreparedStatement preparedStatement(String query, Boolean returnId) {
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ps;
	}
}
