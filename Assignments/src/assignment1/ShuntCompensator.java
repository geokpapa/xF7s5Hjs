package assignment1;

import org.w3c.dom.Element;

public class ShuntCompensator {
	
	String id, name;
	Double gs, bs;
	
	ShuntCompensator(Element element){
		//Extract information from the CIM XML element into the object.
		this.id = element.getAttribute("rdf:ID");
		this.name = element.getElementsByTagName("cim:IdentifiedObject.name").item(0).getTextContent();
		this.gs = Double.parseDouble(element.getElementsByTagName("cim:LinearShuntCompensator.gPerSection").item(0).getTextContent());
		this.bs = Double.parseDouble(element.getElementsByTagName("cim:LinearShuntCompensator.bPerSection").item(0).getTextContent());
	}
}
