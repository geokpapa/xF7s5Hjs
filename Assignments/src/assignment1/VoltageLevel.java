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
		this.id = element.getAttribute("rdf:ID");
		this.name = element.getElementsByTagName("cim:IdentifiedObject.name").item(0).getTextContent();
		Node subnode1 = element.getElementsByTagName("cim:VoltageLevel.Substation").item(0);
		Element subelement1 = (Element)subnode1;
		this.substation_id = subelement1.getAttribute("rdf:resource").replace("#","");
		Node subnode2 = element.getElementsByTagName("cim:VoltageLevel.BaseVoltage").item(0);
		Element subelement2 = (Element)subnode2;
		this.basevoltage_id = subelement2.getAttribute("rdf:resource").replace("#","");
	}
	
	void print(){
		//Print out all the information in the object (used for debugging mainly).
		System.out.println("Type: Voltage Level");
		System.out.println("ID:" + this.id);
		System.out.println("Name:" + this.name);
		System.out.println("Substation ID:" + this.substation_id);
		System.out.println("Base Voltage ID:" + this.basevoltage_id + "\n");
	}
	
	void intodb(){
		try {
			// Establish connection with database via JDBC.
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/assignment_1?useSSL=false", "root", "xxxx");
			Statement query = conn.createStatement();						
			
			// Create table if it doesn't already exist.
			String createTable = "CREATE TABLE IF NOT EXISTS voltagelevel(" 
		            + "id VARCHAR(50),"  
		            + "name VARCHAR(50)," 
		            + "substation_id VARCHAR(50),"  
		            + "basevoltage_id VARCHAR(50))"; 
			boolean error = query.execute(createTable);
			
			// Insert record into table.
			String insertTable = "INSERT INTO voltagelevel VALUES('" 
					+ this.id + "','" 
					+ this.name + "','" 
					+ this.substation_id + "','" 
					+ this.basevoltage_id + "');";
			int records_inserted = query.executeUpdate(insertTable);
			
			//Close connection.
			conn.close();		
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
}
