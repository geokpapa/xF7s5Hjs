package assignment1;

import org.w3c.dom.Element;

class ACLineSegment {
	String id, name;
	Double r, x, length;
	Double rtot, xtot;
	
	ACLineSegment(Element element){
		//Extract information from the CIM XML element into the object.
		this.id = element.getAttribute("rdf:ID");
		this.name = element.getElementsByTagName("cim:IdentifiedObject.name").item(0).getTextContent();
		this.r = Double.parseDouble(element.getElementsByTagName("cim:ACLineSegment.r").item(0).getTextContent());
		this.x = Double.parseDouble(element.getElementsByTagName("cim:ACLineSegment.x").item(0).getTextContent());
		this.length = Double.parseDouble(element.getElementsByTagName("cim:Conductor.length").item(0).getTextContent());
		this.rtot = r*length;
		this.xtot = x*length;
	}
}
