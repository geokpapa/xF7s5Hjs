package assignment1;

import org.w3c.dom.Element;

public class BaseVoltage {
	String id, name, shortName, description;
	Double nominalVoltage;
	
	BaseVoltage(Element element){
		//Extract information from the CIM XML element into the object.
		this.id = element.getAttribute("rdf:ID");
		this.name = element.getElementsByTagName("cim:IdentifiedObject.name").item(0).getTextContent();
		this.shortName = element.getElementsByTagName("entsoe:IdentifiedObject.shortName").item(0).getTextContent();
		this.nominalVoltage = Double.parseDouble(element.getElementsByTagName("cim:BaseVoltage.nominalVoltage").item(0).getTextContent());
	}
}
