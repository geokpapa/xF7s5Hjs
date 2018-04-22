package assignment1;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

class ACLineSegment {
	String id, name;
	String baseVoltage_id;
	Double baseVoltage;
	Double r, x, length;
	Double rtot, xtot;
	
	ACLineSegment(Element element){
		//Extract information from the CIM XML element into the object.
		this.id = element.getAttribute("rdf:ID");
		this.name = element.getElementsByTagName("cim:IdentifiedObject.name").item(0).getTextContent();
		Node subnode1 = element.getElementsByTagName("cim:ConductingEquipment.BaseVoltage").item(0);
		Element subelement1 = (Element)subnode1;
		this.baseVoltage_id = subelement1.getAttribute("rdf:resource").replace("#","");
		this.r = Double.parseDouble(element.getElementsByTagName("cim:ACLineSegment.r").item(0).getTextContent());
		this.x = Double.parseDouble(element.getElementsByTagName("cim:ACLineSegment.x").item(0).getTextContent());
		this.length = Double.parseDouble(element.getElementsByTagName("cim:Conductor.length").item(0).getTextContent());
		this.rtot = r*length;
		this.xtot = x*length;
	}
}
