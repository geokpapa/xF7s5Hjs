package assignment1;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Substation {
	String id;
	String name;
	String region_id;

	Substation(Element element){
		//Extract information from the CIM XML element into the object.
		this.id = element.getAttribute("rdf:ID"); //ID
		this.name = element.getElementsByTagName("cim:IdentifiedObject.name").item(0).getTextContent(); //Name
		
		//Region
		Node subnode1 = element.getElementsByTagName("cim:Substation.Region").item(0);
		Element subelement1 = (Element)subnode1;
		this.region_id = subelement1.getAttribute("rdf:resource").replace("#","");
	}
	
	@SuppressWarnings("unused")
	void intodb(Connection conn){
		try {				
			// Create table if it doesn't already exist.
			Statement query = conn.createStatement();
			String createTable = "CREATE TABLE IF NOT EXISTS substation(" 
		            + "id VARCHAR(50),"  
		            + "name VARCHAR(50)," 
		            + "region_id VARCHAR(50))"; 
			boolean ResultSet = query.execute(createTable);
			
			// Insert record into table.
			String insertTable = "INSERT INTO substation VALUES('" 
					+ this.id + "','" 
					+ this.name + "','" 
					+ this.region_id + "');";
			int RowCount = query.executeUpdate(insertTable);
			query.close(); //Close query.
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
}
