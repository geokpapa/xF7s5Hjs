package assignment1;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

class BusbarSection {
	String id;
	String name;
	String EquipmentContainer;
	
	BusbarSection(Element element){
		//Extract information from the CIM XML element into the object.
		this.id = element.getAttribute("rdf:ID");
		this.name = element.getElementsByTagName("cim:IdentifiedObject.name").item(0).getTextContent();
		
		//Equipment Container (Voltage Level)
		Node subnode1 = element.getElementsByTagName("cim:Equipment.EquipmentContainer").item(0);
		Element subelement1 = (Element)subnode1;
		this.EquipmentContainer = subelement1.getAttribute("rdf:resource").replace("#","");
	}
	
	public double getBaseVoltage(ArrayList<VoltageLevel> voltlvl_list, ArrayList<BaseVoltage> basevolt_list) {
		Double VB = null; //Base Voltage
		for (VoltageLevel voltlevel : voltlvl_list) {
			if (this.EquipmentContainer.equals(voltlevel.id)){
				for (BaseVoltage basevolt : basevolt_list) {
					if (voltlevel.basevoltage_id.equals(basevolt.id)){						
						VB = basevolt.nominalVoltage;
					}					
				}				
			}
		}
		return VB;
	}
}
