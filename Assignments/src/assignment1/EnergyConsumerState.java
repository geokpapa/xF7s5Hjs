package assignment1;

import org.w3c.dom.Element;

public class EnergyConsumerState {
	String about;
	Double P, Q;

	EnergyConsumerState(Element element){
		//Extract information from the CIM XML element into the object.
		this.about = element.getAttribute("rdf:about").replace("#",""); //ID
		this.P = Double.parseDouble(element.getElementsByTagName("cim:EnergyConsumer.p").item(0).getTextContent()); //P
		this.Q = Double.parseDouble(element.getElementsByTagName("cim:EnergyConsumer.q").item(0).getTextContent()); //Q
	}
}
