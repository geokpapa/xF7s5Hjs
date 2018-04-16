package assignment1;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Breaker {
	String id;
	String name;
	String EquipmentContainer;
	String open;
	
	Breaker(Element element){
		//Extract information from the CIM XML element into the object.
		this.id = element.getAttribute("rdf:ID");
		this.name = element.getElementsByTagName("cim:IdentifiedObject.name").item(0).getTextContent();
		Node subnode1 = element.getElementsByTagName("cim:Equipment.EquipmentContainer").item(0);
		Element subelement1 = (Element)subnode1;
		this.EquipmentContainer = subelement1.getAttribute("rdf:resource").replace("#","");
	}
}
