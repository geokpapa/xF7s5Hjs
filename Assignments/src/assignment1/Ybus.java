package assignment1;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Ybus {
	String From, To;
	Double R, X;
	Double Gch, Bch;
	String devType, dev;
	
	Ybus(String From, String To, Double R, Double X, Double Gch, Double Bch, String devType, String dev) {
		this.From = From;
		this.To= To;
		this.R = R;
		this.X = X;
		this.Gch = Gch;
		this.Bch = Bch;
		this.devType = devType;
		this.dev = dev;
	}
	
	@SuppressWarnings("unused")
	void intodb(Connection conn){
		try {				
			// Create table if it doesn't already exist.
			Statement query = conn.createStatement();
			String createTable = "CREATE TABLE IF NOT EXISTS ybus(" 
		            + "From_Bus VARCHAR(50),"  
		            + "To_Bus VARCHAR(50),"
		            + "R DECIMAL(10,4),"  
		            + "X DECIMAL(10,4),"
		            + "Gch DECIMAL(10,4),"
		            + "BcH DECIMAL(10,4),"
		            + "DevType VARCHAR(50)," 
		            + "Device VARCHAR(50))"; 
			boolean ResultSet = query.execute(createTable);
			
			// Insert record into table.
			String insertTable = "INSERT INTO ybus VALUES('" 
					+ this.From + "','" 
					+ this.To + "',"
					+ this.R + "," 
					+ this.X + "," 
					+ this.Gch + "," 
					+ this.Bch + ",'"
					+ this.devType + "','" 
					+ this.dev + "')" ;
			int RowCount = query.executeUpdate(insertTable);
			query.close(); //Close query.
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
}
