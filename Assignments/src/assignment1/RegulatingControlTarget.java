package assignment1;

import org.w3c.dom.Element;

public class RegulatingControlTarget {
	String about;
	Double targetValue;

	RegulatingControlTarget(Element element){
		//Extract information from the CIM XML element into the object.
		this.about = element.getAttribute("rdf:about").replace("#",""); //ID
		this.targetValue = Double.parseDouble(element.getElementsByTagName("cim:RegulatingControl.targetValue").item(0).getTextContent()); //Name
	}
}
