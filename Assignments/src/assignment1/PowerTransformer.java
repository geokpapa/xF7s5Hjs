package assignment1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class PowerTransformer {
	String id;
	String name;
	String equipmentContainer_id;
	
	PowerTransformer(Element element){
		//Extract information from the CIM XML element into the object.
		this.id = element.getAttribute("rdf:ID");
		this.name = element.getElementsByTagName("cim:IdentifiedObject.name").item(0).getTextContent();
		Node subnode1 = element.getElementsByTagName("cim:Equipment.EquipmentContainer").item(0);
		Element subelement1 = (Element)subnode1;
		this.equipmentContainer_id = subelement1.getAttribute("rdf:resource").replace("#","");
	}
	
	void print(){
		//Print out all the information in the object (used for debugging mainly).
		System.out.println("Type: Synchronous Machine");
		System.out.println("ID:" + this.id);
		System.out.println("Name:" + this.name);
		System.out.println("Equipment Container ID:" + this.equipmentContainer_id + "\n");
	}
	
	void intodb(){
		try {
			// Establish connection with database via JDBC.
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/assignment_1?useSSL=false", "root", "xxxx");
			Statement query = conn.createStatement();						
			
			// Create table if it doesn't already exist.
			String createTable = "CREATE TABLE IF NOT EXISTS power_transformer(" 
		            + "id VARCHAR(50),"  
		            + "name VARCHAR(50)," 
		            + "equipmentContainer_id VARCHAR(50))"; 
			boolean error = query.execute(createTable);
			
			// Insert record into table.
			String insertTable = "INSERT INTO power_transformer VALUES('" 
					+ this.id + "','" 
					+ this.name + "','"
					+ this.equipmentContainer_id + "');";
			int records_inserted = query.executeUpdate(insertTable);
			
			//Close connection.
			conn.close();		
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
}
