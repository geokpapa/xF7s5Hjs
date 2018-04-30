package assignment1;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Ybus {
	String From;
	String To;
	Double Real;
	Double Imag;
	
	Ybus(String From, String To, Double Real, Double Imag) {
		this.From = From;
		this.To= To;
		this.Real = Real;
		this.Imag = Imag;
	}
	
	@SuppressWarnings("unused")
	void intodb(Connection conn){
		try {				
			// Create table if it doesn't already exist.
			Statement query = conn.createStatement();
			String createTable = "CREATE TABLE IF NOT EXISTS ybus(" 
		            + "From_Bus VARCHAR(50),"  
		            + "To_Bus VARCHAR(50)," 
		            + "RG DECIMAL,"  
		            + "XB DECIMAL)"; 
			boolean ResultSet = query.execute(createTable);
			
			// Insert record into table.
			String insertTable = "INSERT INTO ybus VALUES('" 
					+ this.From + "','" 
					+ this.To + "','" 
					+ this.Real + "','" 
					+ this.Imag + "');";
			int RowCount = query.executeUpdate(insertTable);
			query.close(); //Close query.
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
}
