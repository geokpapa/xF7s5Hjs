package assignment1;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

<<<<<<< HEAD
public class PowerTransformerEnd {	
	String id, name;
	String powerTransformer_id, basevoltage_id, terminal_id;
	Double rtot, xtot;	
=======
public class PowerTransformerEnd {
	
	String id;
	String name;
	String powerTransformer_id;
	String baseVoltage_id;
	String terminal_id;
	Double rtot, xtot, VBn, SBn;	
>>>>>>> 7600349488d7551c8d7e2b65bc0df6346020194b
	
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
		
<<<<<<< HEAD
		//Base Voltage ID.
		Node subnode4 = element.getElementsByTagName("cim:TransformerEnd.BaseVoltage").item(0);
		Element subelement4 = (Element)subnode4; 
		this.basevoltage_id = subelement4.getAttribute("rdf:resource").replace("#","");
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
					+ this.basevoltage_id +"');";
			int RowCount = query.executeUpdate(insertTable);
			query.close(); //Close query.
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
=======
		this.rtot = Double.parseDouble(element.getElementsByTagName("cim:PowerTransformerEnd.r").item(0).getTextContent());
		this.xtot = Double.parseDouble(element.getElementsByTagName("cim:PowerTransformerEnd.x").item(0).getTextContent());
		this.VBn = Double.parseDouble(element.getElementsByTagName("cim:PowerTransformerEnd.ratedU").item(0).getTextContent());
		this.SBn = Double.parseDouble(element.getElementsByTagName("cim:PowerTransformerEnd.ratedS").item(0).getTextContent());
	}	
>>>>>>> 7600349488d7551c8d7e2b65bc0df6346020194b
}
