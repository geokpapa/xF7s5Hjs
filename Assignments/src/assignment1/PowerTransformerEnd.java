package assignment1;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class PowerTransformerEnd {	
	String id;
	String name;
	String powerTransformer_id;
	String baseVoltage_id;
	String terminal_id;
	Double rtot, xtot;	
	
	PowerTransformerEnd(Element element){
		//Extract information from the CIM XML element into the object.
		this.id = element.getAttribute("rdf:ID");
		this.name = element.getElementsByTagName("cim:IdentifiedObject.name").item(0).getTextContent();
		this.rtot = Double.parseDouble(element.getElementsByTagName("cim:PowerTransformerEnd.r").item(0).getTextContent());
		this.xtot = Double.parseDouble(element.getElementsByTagName("cim:PowerTransformerEnd.x").item(0).getTextContent());
		
		//Power Transformer ID.
		Node subnode2 = element.getElementsByTagName("cim:PowerTransformerEnd.PowerTransformer").item(0);
		Element subelement2 = (Element)subnode2; 
		this.powerTransformer_id = subelement2.getAttribute("rdf:resource").replace("#","");
		
		//Terminal ID.
		Node subnode3 = element.getElementsByTagName("cim:TransformerEnd.Terminal").item(0);
		Element subelement3 = (Element)subnode3; 
		this.terminal_id = subelement3.getAttribute("rdf:resource").replace("#","");	
	}
	
	@SuppressWarnings("unused")
	void intodb(Connection conn){
		try {
			// Create table if it doesn't already exist.
			Statement query = conn.createStatement();
			String createTable = "CREATE TABLE IF NOT EXISTS power_transformer_end(" 
		            + "id VARCHAR(50),"  
		            + "name VARCHAR(50)," 
		            + "R DECIMAL,"
		            + "X DECIMAL,"
		            + "powerTransformer_id VARCHAR(50),"
		            + "baseVoltage_id VARCHAR(50))"; 
			boolean ResultSet = query.execute(createTable);
			
			// Insert record into table.
			String insertTable = "INSERT INTO power_transformer_end VALUES('" 
					+ this.id + "','" 
					+ this.name + "',"
					+ this.rtot + ","
					+ this.xtot + ",'"
					+ this.powerTransformer_id + "','"
					+ this.baseVoltage_id +"');";
			int RowCount = query.executeUpdate(insertTable);
			query.close(); //Close query.
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
}
