package assignment1;

import org.w3c.dom.Element;

public class RatioTapChangerStep {
	String about;
	Integer step;
	
	RatioTapChangerStep(Element element){
		//Extract information from the CIM XML element into the object.
		this.about = element.getAttribute("rdf:about"); //ID
		this.step = Integer.parseInt(element.getElementsByTagName("cim:TapChanger.step").item(0).getTextContent()); //Step
	}
}
