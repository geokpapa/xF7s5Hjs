package assignment1;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.w3c.dom.Element;

public class BaseVoltage {
	String id, name, shortName;
	Double nominalVoltage;
	
	BaseVoltage(Element element){
		//Extract information from the CIM XML element into the object.
		this.id = element.getAttribute("rdf:ID");
		this.name = element.getElementsByTagName("cim:IdentifiedObject.name").item(0).getTextContent();
		this.shortName = element.getElementsByTagName("entsoe:IdentifiedObject.shortName").item(0).getTextContent();
		this.nominalVoltage = Double.parseDouble(element.getElementsByTagName("cim:BaseVoltage.nominalVoltage").item(0).getTextContent());
	}
	
	@SuppressWarnings("unused")
	void intodb(Connection conn){
		try {								
			// Create table if it doesn't already exist.			
			Statement query = conn.createStatement();
			String createTable = "CREATE TABLE IF NOT EXISTS base_voltage(" 
		            + "id VARCHAR(50),"  
		            + "name VARCHAR(50)," 
		            + "nominalVoltage DECIMAL(10,4))"; 
			boolean ResultSet = query.execute(createTable);
			
			
			// Insert record into table.
			String insertTable = "INSERT INTO base_voltage VALUES('" 
					+ this.id + "','" 
					+ this.name + "',"
					+ this.nominalVoltage + ");";
			int RowCount = query.executeUpdate(insertTable);
			query.close(); //Close query.			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
}
