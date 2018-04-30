package assignment1;

import org.w3c.dom.Element;

public class SynchronousState {
	String about;
	Double P, Q;
	
	SynchronousState(Element element){
		//Extract information from the CIM XML element into the object.
		this.about = element.getAttribute("rdf:about").replace("#",""); //Target synchronous machine ID
		this.P = Double.parseDouble(element.getElementsByTagName("cim:RotatingMachine.p").item(0).getTextContent()); //Current P
		this.Q = Double.parseDouble(element.getElementsByTagName("cim:RotatingMachine.q").item(0).getTextContent()); //Current Q
	}
}
