package assignment1;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.w3c.dom.Element;

public class RatioTapChanger {
	String id, name;
	Integer step;
	
	RatioTapChanger(Element element){
		//Extract information from the CIM XML element into the object.
		this.id = element.getAttribute("rdf:ID"); //ID
		this.name = element.getElementsByTagName("cim:IdentifiedObject.name").item(0).getTextContent(); //Name
	}
	
	@SuppressWarnings("unused")
	void intodb(Connection conn){
		try {
			// Create table if it doesn't already exist.
			Statement query = conn.createStatement();
			String createTable = "CREATE TABLE IF NOT EXISTS tapchanger(" 
		            + "id VARCHAR(50),"  
		            + "name VARCHAR(50)," 
		            + "step DECIMAL)"; 
			boolean ResultSet = query.execute(createTable);
			
			// Insert record into table.
			String insertTable = "INSERT INTO tapchanger VALUES('" 
					+ this.id + "','"
					+ this.name + "',"
					+ this.step + ");";
			int RowCount = query.executeUpdate(insertTable);
			query.close(); //Close query.
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
