package assignment1;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class EnergyConsumer {
	String id, name;
	String equipmentContainer_id, basevoltage_id;
	Double targetValue, P, Q;	

	EnergyConsumer(Element element){
		//Extract information from the CIM XML element into the object.
		this.id = element.getAttribute("rdf:ID"); //ID
		this.name = element.getElementsByTagName("cim:IdentifiedObject.name").item(0).getTextContent(); //Name
		
		//Equipment Container
		Node subnode1 = element.getElementsByTagName("cim:Equipment.EquipmentContainer").item(0);
		Element subelement1 = (Element)subnode1;
		this.equipmentContainer_id = subelement1.getAttribute("rdf:resource").replace("#","");
	}
	
	@SuppressWarnings("unused")
	void intodb(Connection conn){
		try {					
			// Create table if it doesn't already exist.
			Statement query = conn.createStatement();
			String createTable = "CREATE TABLE IF NOT EXISTS energy_consumer(" 
		            + "id VARCHAR(50),"  
		            + "name VARCHAR(50)," 
		            + "P DECIMAL,"
		            + "Q DECIMAL,"
		            + "baseVoltage_id VARCHAR(50)," 
		            + "equipmentContaider_id VARCHAR(50))"; 
			boolean ResultSet = query.execute(createTable);
			
			// Insert record into table.
			String insertTable = "INSERT INTO energy_consumer VALUES('" 
					+ this.id + "','" 
					+ this.name + "',"
					+ this.P + ","
					+ this.Q + ",'"
					+ this.basevoltage_id + "','"
					+ this.equipmentContainer_id + "');";
			int RowCount = query.executeUpdate(insertTable);
			query.close(); //Close query.
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
}
