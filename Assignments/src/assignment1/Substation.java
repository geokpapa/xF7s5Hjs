package assignment1;

import java.sql.Connection;
import java.sql.DriverManager;
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
		this.id = element.getAttribute("rdf:ID");
		this.name = element.getElementsByTagName("cim:IdentifiedObject.name").item(0).getTextContent();
		Node subnode1 = element.getElementsByTagName("cim:Substation.Region").item(0);
		Element subelement1 = (Element)subnode1;
		this.region_id = subelement1.getAttribute("rdf:resource").replace("#","");
	}
	
	void print(){
		//Print out all the information in the object (used for debugging mainly).
		System.out.println("Type: Substation");
		System.out.println("ID:" + this.id);
		System.out.println("Name:" + this.name);
		System.out.println("Region ID:" + this.region_id + "\n");
	}
	
	void intodb(){
		try {
			// Establish connection with database via JDBC.
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/assignment_1?useSSL=false", "root", "xxxx");
			Statement query = conn.createStatement();						
			
			// Create table if it doesn't already exist.
			String createTable = "CREATE TABLE IF NOT EXISTS substation(" 
		            + "id VARCHAR(50),"  
		            + "name VARCHAR(50)," 
		            + "region_id VARCHAR(50))"; 
			boolean error = query.execute(createTable);
			
			// Insert record into table.
			String insertTable = "INSERT INTO substation VALUES('" 
					+ this.id + "','" 
					+ this.name + "','" 
					+ this.region_id + "');";
			int records_inserted = query.executeUpdate(insertTable);
			
			//Close connection.
			conn.close();		
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
}
