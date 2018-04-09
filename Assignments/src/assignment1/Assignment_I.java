package assignment1;

import java.util.ArrayList;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.sql.*;

public class Assignment_I {
	static ArrayList<VoltageLevel> voltlvl_list = new ArrayList<VoltageLevel>();	
	static ArrayList<Substation> substation_list = new ArrayList<Substation>();
	static ArrayList<SynchronousMachine> synch_list = new ArrayList<SynchronousMachine>();
	static ArrayList<PowerTransformer> trafo_list = new ArrayList<PowerTransformer>();
	static ArrayList<BusbarSection> busbar_list = new ArrayList<BusbarSection>();
	static ArrayList<ACLineSegment> line_list = new ArrayList<ACLineSegment>();
	static ArrayList<Terminal> terminal_list = new ArrayList<Terminal>();
	static ArrayList<ConnectivityNode> cnode_list = new ArrayList<ConnectivityNode>();
	
	public static void main(String[] args) {		
		cleardb(); //Clear SQL database content.
		NodeList sublist = ReadXML.ToNodeList("xml/Assignment_EQ_reduced.xml"); //Read XML file into Node List.
		for (int i = 0; i < sublist.getLength(); i++) {
			extractNode(sublist.item(i)); //Extract node from Node List into database.
		}	
		filldb(); //Push elements into SQL database.
		ybus(); //Create Y-Bus matrix.
		print_ybus(); //Print Y-Bus matrix.
	}	
	
	public static void extractNode(Node node){
		/* … remember to convert node to element in order to search for the data inside it.
		element.getElementsByTagName("cim:IdentifiedObject.name").item(0).getTextContent 
		can be used to read specific attribute of XML node.*/
		Element element = (Element)node;
		String tagname = element.getTagName();
		switch (tagname) {
			case "cim:VoltageLevel" : voltlvl_list.add(new VoltageLevel(element)); break;
			case "cim:Substation" : substation_list.add(new Substation(element)); break;
			case "cim:SynchronousMachine" : synch_list.add(new SynchronousMachine(element)); break;
			case "cim:PowerTransformer" : trafo_list.add(new PowerTransformer(element)); break;
			case "cim:BusbarSection" : busbar_list.add(new BusbarSection(element)); break;
			case "cim:ACLineSegment" : line_list.add(new ACLineSegment(element)); break;
			case "cim:Terminal" : terminal_list.add(new Terminal(element)); break;
			case "cim:ConnectivityNode" : cnode_list.add(new ConnectivityNode(element)); break;
		}
	}
	
	public static void filldb() {
		for (VoltageLevel voltlvl : voltlvl_list) {voltlvl.intodb();}		
		for (Substation substation : substation_list) {substation.intodb();}
		for (SynchronousMachine synch : synch_list) {synch.intodb();}
		for (PowerTransformer trafo : trafo_list) {trafo.intodb();}
	}
	
	public static void cleardb() {
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/assignment_1?useSSL=false", "root", "xxxx");
			Statement query = conn.createStatement();
			query.execute("DROP DATABASE IF EXISTS assignment_1");
			query.execute("CREATE DATABASE IF NOT EXISTS assignment_1");
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void ybus() {
		String irn = null;
		for (ACLineSegment line : line_list) {
			for (Terminal terminal : terminal_list) {
				if (line.id.equals(terminal.ConductingEquipment)) {
					irn = terminal.ConnectivityNode;
					for (ConnectivityNode cnode : cnode_list) {
						if (cnode.id.equals(irn)) {
							irn = cnode.ConnectivityNodeContainer;
							for (int i = 0; i < busbar_list.size(); i++) {
								BusbarSection busbar = busbar_list.get(i);
								if (busbar.EquipmentContainer.equals(irn)) {
									busbar.y = busbar.y + 1/(line.rtot + line.xtot);
									busbar_list.set(i, busbar);
								}
							}
						}
					}
				}
			}	
		}
	}
	
	public static void print_ybus() {
		for (BusbarSection busbar : busbar_list) {
			System.out.println(busbar.name + " = " + busbar.y);
		}
	}
}
