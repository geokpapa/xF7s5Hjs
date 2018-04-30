package assignment1;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import java.sql.*;

public class VoltageLevel {
	String id;
	String name;
	String substation_id;
	String basevoltage_id;
	
	VoltageLevel(Element element){
		//Extract information from the CIM XML element into the object.
		this.id = element.getAttribute("rdf:ID"); //ID
		this.name = element.getElementsByTagName("cim:IdentifiedObject.name").item(0).getTextContent(); //Name
		
		//Substation
		Node subnode1 = element.getElementsByTagName("cim:VoltageLevel.Substation").item(0);
		Element subelement1 = (Element)subnode1;
		this.substation_id = subelement1.getAttribute("rdf:resource").replace("#","");
		
		//Base Voltage
		Node subnode2 = element.getElementsByTagName("cim:VoltageLevel.BaseVoltage").item(0);
		Element subelement2 = (Element)subnode2;
		this.basevoltage_id = subelement2.getAttribute("rdf:resource").replace("#","");
	}
	
	@SuppressWarnings("unused")
	void intodb(Connection conn){
		try {				
			// Create table if it doesn't already exist.
			Statement query = conn.createStatement();
			String createTable = "CREATE TABLE IF NOT EXISTS voltagelevel(" 
		            + "id VARCHAR(50),"  
		            + "name VARCHAR(50)," 
		            + "substation_id VARCHAR(50),"  
		            + "basevoltage_id VARCHAR(50))"; 
			boolean ResultSet = query.execute(createTable);
			
			// Insert record into table.
			String insertTable = "INSERT INTO voltagelevel VALUES('" 
					+ this.id + "','" 
					+ this.name + "','" 
					+ this.substation_id + "','" 
					+ this.basevoltage_id + "');";
			int RowCount = query.executeUpdate(insertTable);
			query.close(); //Close query.
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
}
