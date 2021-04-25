import java.sql.*;


public class DBApp {

	public static void main(String[] args) throws SQLException {
		
		String user = "postgres", password = "1234";
		
		// Create a table
		try {
			
			String userTable = " Create table user " +
								"(CP_Number VARCHAR(8) NOT NULL," +
								"First_Name VARCHAR(20) NOT NULL," +
								"Last_Name VARCHAR(20) NOT NULL," +
								"Monthly_Mem_Fee INT NOT NULL," +
								"Temp_CP_Number VARCHAR(8)," +
								"PRIMARY KEY (CP_Number))";
			
			// Create connection
			Connection conn = DriverManager.getConnection("jdbc:postgressql://localhost:5432/postgres", user, password);
			
			// Create statement
			Statement stmt = conn.createStatement();
			
			// Execute statement
			stmt.executeUpdate(userTable);
			
			// Close connections
			stmt.close();
			conn.close();
		}
		catch(SQLException sqle) {
			// Handle exceptions
			System.out.println("SQLException: " + sqle);
		}
		
		
		// Update a table
		try {
			
			String update = "insert into instructor" +
							"values('77987', 'Kim', 'Physics', 98000)";
			
			// Create connection
			Connection conn = DriverManager.getConnection("jdbc:postgressql://localhost:5432/postgres", user, password);
			
			// Create statement
			Statement stmt = conn.createStatement();
			
			// Execute statement
			stmt.executeUpdate(update);
			
			// Close connections
			stmt.close();
			conn.close();
		}
		catch(SQLException sqle) {
			// Handle exceptions
			System.out.println("SQLException: " + sqle);
		}
		
		
		// Execute a query
		try {
			
			String query = "select dept_name, avg(salary)" +
							"from instructor" +
							"group by dept_name";
			
			// Create connection
			Connection conn = DriverManager.getConnection("jdbc:postgressql://localhost:5432/postgres", user, password);
			
			// Create statement
			Statement stmt = conn.createStatement();
			
			// Execute statement
			ResultSet rset = stmt.executeQuery(query);
			
			while(rset.next()) {
				System.out.println(rset.getString("dept_name") + " " + rset.getFloat(2));
			}
			
			// Close connections
			stmt.close();
			conn.close();
		}
		catch(SQLException sqle) {
			// Handle exceptions
			System.out.println("SQLException: " + sqle);
		}
		
		
	}
	
}
