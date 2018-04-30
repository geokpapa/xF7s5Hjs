package assignment1;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class GeneratingUnit {
	String id, name, equipmentContainer_id;
	Double maxP, minP; 

	GeneratingUnit(Element element){
		//Extract information from the CIM XML element into the object.
		this.id = element.getAttribute("rdf:ID"); //ID
		this.name = element.getElementsByTagName("cim:IdentifiedObject.name").item(0).getTextContent(); //Name
		this.maxP = Double.parseDouble(element.getElementsByTagName("cim:GeneratingUnit.maxOperatingP").item(0).getTextContent()); //Max P
		this.minP = Double.parseDouble(element.getElementsByTagName("cim:GeneratingUnit.minOperatingP").item(0).getTextContent()); //Min P
		
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
			String createTable = "CREATE TABLE IF NOT EXISTS generating_unit(" 
		            + "id VARCHAR(50),"  
		            + "name VARCHAR(50),"
		            + "max_p DECIMAL,"
		            + "min_p DECIMAL,"
		            + "equipmentContainer_id VARCHAR(50))"; 
			boolean ResultSet = query.execute(createTable);
			
			// Insert record into table.
			String insertTable = "INSERT INTO generating_unit VALUES('" 
					+ this.id + "','" 
					+ this.name + "'," 
					+ this.maxP + ","
					+ this.minP + ",'"
					+ this.equipmentContainer_id + "');";
			int RowCount = query.executeUpdate(insertTable);
			query.close(); //Close query.
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
}
