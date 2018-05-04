package assignment1;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Breaker {
	String id;
	String name;
	String EquipmentContainer;
	String open;
	String basevoltage_id;
	
	Breaker(Element element){
		//Extract information from the CIM XML element into the object.
		this.id = element.getAttribute("rdf:ID");
		this.name = element.getElementsByTagName("cim:IdentifiedObject.name").item(0).getTextContent();
		Node subnode1 = element.getElementsByTagName("cim:Equipment.EquipmentContainer").item(0);
		Element subelement1 = (Element)subnode1;
		this.EquipmentContainer = subelement1.getAttribute("rdf:resource").replace("#","");
	}
	
	@SuppressWarnings("unused")
	void intodb(Connection conn){
		try {
			// Create table if it doesn't already exist.
			Statement query = conn.createStatement();
			String createTable = "CREATE TABLE IF NOT EXISTS breaker(" 
		            + "id VARCHAR(50),"  
		            + "name VARCHAR(50),"
		            + "open VARCHAR(50),"
		            + "baseVoltage_id VARCHAR(50)," 
		            + "EquipmentContainer VARCHAR(50))"; 
			boolean ResultSet = query.execute(createTable);
			
			// Insert record into table.
			String insertTable = "INSERT INTO breaker VALUES('" 
					+ this.id + "','"
					+ this.name + "','"
					+ this.open + "','"
					+ this.basevoltage_id + "','"
					+ this.EquipmentContainer + "');";
			int RowCount = query.executeUpdate(insertTable);
			query.close(); //Close query.
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
