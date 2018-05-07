package assignment1;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class SynchronousMachine {
	String id, name;
	String genUnit_id, regulatingControl_id,equipmentContainer_id, basevoltage_id;
	Double ratedS, P, Q;
	
	SynchronousMachine(Element element){
		//Extract information from the CIM XML element into the object.
		this.id = element.getAttribute("rdf:ID"); //ID
		this.name = element.getElementsByTagName("cim:IdentifiedObject.name").item(0).getTextContent(); //Name
		this.ratedS = Double.parseDouble(element.getElementsByTagName("cim:RotatingMachine.ratedS").item(0).getTextContent()); //Rated S
		
		//Generating Unit ID
		Node subnode1 = element.getElementsByTagName("cim:RotatingMachine.GeneratingUnit").item(0);
		Element subelement1 = (Element)subnode1;
		this.genUnit_id = subelement1.getAttribute("rdf:resource").replace("#","");
		
		//Regulating Control ID
		Node subnode2 = element.getElementsByTagName("cim:RegulatingCondEq.RegulatingControl").item(0);
		Element subelement2 = (Element)subnode2;
		this.regulatingControl_id = subelement2.getAttribute("rdf:resource").replace("#","");
		
		//Equipment Container ID
		Node subnode3 = element.getElementsByTagName("cim:Equipment.EquipmentContainer").item(0);
		Element subelement3 = (Element)subnode3;
		this.equipmentContainer_id = subelement3.getAttribute("rdf:resource").replace("#","");
	}
	
	@SuppressWarnings("unused")
	void intodb(Connection conn){
		try {					
			// Create table if it doesn't already exist.
			Statement query = conn.createStatement();
			String createTable = "CREATE TABLE IF NOT EXISTS synch_machine(" 
		            + "id VARCHAR(50),"  
		            + "name VARCHAR(50)," 
		            + "ratedS VARCHAR(50),"
		            + "P DECIMAL(10,4),"
		            + "Q DECIMAL(10,4),"
		            + "genUnit_ID VARCHAR(50),"
		            + "regulatingControl_id VARCHAR(50),"
		            + "baseVoltage_id VARCHAR(50),"
		            + "equipmentContainer_id VARCHAR(50))"; 
			boolean ResultSet = query.execute(createTable);
			
			// Insert record into table.
			String insertTable = "INSERT INTO synch_machine VALUES('" 
					+ this.id + "','" 
					+ this.name + "',"
					+ this.ratedS + ","
					+ this.P + "," 
					+ this.Q + ",'" 
					+ this.genUnit_id + "','"
					+ this.regulatingControl_id + "','"
					+ this.basevoltage_id + "','"
					+ this.equipmentContainer_id + "');";
			int RowCount = query.executeUpdate(insertTable);
			query.close(); //Close query.
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
}
