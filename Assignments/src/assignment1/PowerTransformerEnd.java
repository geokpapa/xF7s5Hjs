package assignment1;

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
		
		//Base Voltage ID.
		Node subnode1 = element.getElementsByTagName("cim:TransformerEnd.BaseVoltage").item(0);
		Element subelement1 = (Element)subnode1;
		this.baseVoltage_id = subelement1.getAttribute("rdf:resource").replace("#","");
		
		//Power Transformer ID.
		Node subnode2 = element.getElementsByTagName("cim:PowerTransformerEnd.PowerTransformer").item(0);
		Element subelement2 = (Element)subnode2; 
		this.powerTransformer_id = subelement2.getAttribute("rdf:resource").replace("#","");
		
		//Terminal ID.
		Node subnode3 = element.getElementsByTagName("cim:TransformerEnd.Terminal").item(0);
		Element subelement3 = (Element)subnode3; 
		this.terminal_id = subelement3.getAttribute("rdf:resource").replace("#","");
		
		this.rtot = Double.parseDouble(element.getElementsByTagName("cim:PowerTransformerEnd.r").item(0).getTextContent());
		this.xtot = Double.parseDouble(element.getElementsByTagName("cim:PowerTransformerEnd.x").item(0).getTextContent());
	}	
}
