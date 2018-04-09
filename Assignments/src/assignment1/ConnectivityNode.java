package assignment1;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

class ConnectivityNode {
	String id;
	String name;
	String ConnectivityNodeContainer;
	
	ConnectivityNode(Element element) {
		//Extract information from the CIM XML element into the object.
		this.id = element.getAttribute("rdf:ID");
		this.name = element.getElementsByTagName("cim:IdentifiedObject.name").item(0).getTextContent();
		Node subnode1 = element.getElementsByTagName("cim:ConnectivityNode.ConnectivityNodeContainer").item(0);
		Element subelement1 = (Element)subnode1;
		this.ConnectivityNodeContainer = subelement1.getAttribute("rdf:resource").replace("#","");
	}

}
