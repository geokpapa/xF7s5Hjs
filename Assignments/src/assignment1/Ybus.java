package assignment1;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Ybus {
<<<<<<< HEAD
	String From, To;
	String devType, dev;
	Double Real, Imag;
	
	Ybus(String From, String To, String devType, String dev, Double Real, Double Imag) {
=======
	String From;
	String To;
	Double Real;
	Double Imag;
	Double Gch;
	Double Bch;
	String Element;
	
	Ybus(String From, String To, Double Real, Double Imag, Double Gch, Double Bch, String Element) {
>>>>>>> 7600349488d7551c8d7e2b65bc0df6346020194b
		this.From = From;
		this.To= To;
		this.Real = Real;
		this.Imag = Imag;
<<<<<<< HEAD
		this.devType = devType;
		this.dev = dev;
=======
		this.Gch = Gch;
		this.Bch = Bch;
		this.Element= Element;
>>>>>>> 7600349488d7551c8d7e2b65bc0df6346020194b
	}
	
	@SuppressWarnings("unused")
	void intodb(Connection conn){
		try {				
			// Create table if it doesn't already exist.
			Statement query = conn.createStatement();
			String createTable = "CREATE TABLE IF NOT EXISTS ybus(" 
		            + "From_Bus VARCHAR(50),"  
		            + "To_Bus VARCHAR(50),"
		            + "DevType VARCHAR(50)," 
		            + "Device VARCHAR(50)," 
		            + "RG DECIMAL,"  
		            + "XB DECIMAL,"
		            + "Gch DEMICAL,"
		            + "BcH DEMICAL,"
		            + "Element VARCHAR(50))"; 
			boolean ResultSet = query.execute(createTable);
			
			// Insert record into table.
			String insertTable = "INSERT INTO ybus VALUES('" 
					+ this.From + "','" 
					+ this.To + "','"
					+ this.devType + "','" 
					+ this.dev + "','" 
					+ this.Real + "','" 
					+ this.Imag + "','" 
					+ this.Bch + "','" 
					+ this.Gch + "','" 
					+ this.Element + "');";
			int RowCount = query.executeUpdate(insertTable);
			query.close(); //Close query.
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
}
