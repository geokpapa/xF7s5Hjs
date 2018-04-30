package assignment1;

import org.w3c.dom.Element;

public class BreakerStatus{
	String about;
	String open;
	
	BreakerStatus(Element element) {
		//Extract information from the CIM XML element into the object.
		this.about = element.getAttribute("rdf:about").replace("#",""); //Target breaker ID
		this.open = element.getElementsByTagName("cim:Switch.open").item(0).getTextContent(); //Breaker status
	} 
}
