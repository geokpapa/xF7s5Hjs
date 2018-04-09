package assignment1;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

class Terminal {
	String id;
	String name;
	String ConductingEquipment;
	String ConnectivityNode;
	
	Terminal(Element element){
		//Extract information from the CIM XML element into the object.
		this.id = element.getAttribute("rdf:ID");
		this.name = element.getElementsByTagName("cim:IdentifiedObject.name").item(0).getTextContent();
		Node subnode1 = element.getElementsByTagName("cim:Terminal.ConductingEquipment").item(0);
		Element subelement1 = (Element)subnode1;
		this.ConductingEquipment = subelement1.getAttribute("rdf:resource").replace("#","");
		Node subnode2 = element.getElementsByTagName("cim:Terminal.ConnectivityNode").item(0);
		Element subelement2 = (Element)subnode2;
		this.ConnectivityNode = subelement2.getAttribute("rdf:resource").replace("#","");
	}
}
