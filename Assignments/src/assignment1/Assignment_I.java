package assignment1;

import java.util.ArrayList;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.sql.*;

public class Assignment_I {
	//*** DEFINITION OF ARRAY LISTS ***
	static ArrayList<VoltageLevel> voltlvl_list = new ArrayList<VoltageLevel>();	
	static ArrayList<Substation> substation_list = new ArrayList<Substation>();
	static ArrayList<SynchronousMachine> synch_list = new ArrayList<SynchronousMachine>();
	static ArrayList<PowerTransformer> trafo_list = new ArrayList<PowerTransformer>();
	static ArrayList<BusbarSection> busbar_list = new ArrayList<BusbarSection>();
	static ArrayList<ACLineSegment> line_list = new ArrayList<ACLineSegment>();
	static ArrayList<Terminal> terminal_list = new ArrayList<Terminal>();
	static ArrayList<ConnectivityNode> cnode_list = new ArrayList<ConnectivityNode>();
	static ArrayList<Breaker> breaker_list = new ArrayList<Breaker>();
	static ArrayList<BreakerStatus> cbs_list = new ArrayList<BreakerStatus>();
	static ArrayList<String> ybus = new ArrayList<String>();
	
	//*** MAIN ROUTINE ***
	public static void main(String[] args) {		
		//cleardb(); //Clear SQL database content (OBS: Comment this line to work without SQL for debugging !).
		NodeList eq_profile = ReadXML.ToNodeList("xml/Assignment_EQ_reduced.xml"); //Read CIM EQ profile into Node List.
		NodeList ssh_profile = ReadXML.ToNodeList("xml/Assignment_SSH_reduced.xml"); //Read CIM SSH profile into Node List.
		for (int i = 0; i < eq_profile.getLength(); i++) {
			extractNode(eq_profile.item(i),"EQ"); //Extract EQ profile node list into database.
		}
		for (int i = 0; i < ssh_profile.getLength(); i++) {
			extractNode(ssh_profile.item(i),"SSH"); //Extract SSH profile node list into database.
		}
		augmentBreakers(); //Include breaker status into each breaker object.
		//filldb(); //Push elements into SQL database (OBS: Comment this line to work without SQL for debugging !).
		create_ybus(); //Create Y-Bus matrix.
		print_ybus(); //Print Y-Bus matrix.
	}	
	
	//*** EXTRACT PROFILE NODES INTO ARRAY LISTS FOR EASIER MANIPULATION ***
	public static void extractNode(Node node,String profile){
		Element element = (Element)node;
		String tagname = element.getTagName();
		if (profile=="EQ") {
			switch (tagname) {
				case "cim:VoltageLevel" : voltlvl_list.add(new VoltageLevel(element)); break;
				case "cim:Substation" : substation_list.add(new Substation(element)); break;
				case "cim:SynchronousMachine" : synch_list.add(new SynchronousMachine(element)); break;
				case "cim:PowerTransformer" : trafo_list.add(new PowerTransformer(element)); break;
				case "cim:BusbarSection" : busbar_list.add(new BusbarSection(element)); break;
				case "cim:ACLineSegment" : line_list.add(new ACLineSegment(element)); break;
				case "cim:Terminal" : terminal_list.add(new Terminal(element)); break;
				case "cim:ConnectivityNode" : cnode_list.add(new ConnectivityNode(element)); break;
				case "cim:Breaker" : breaker_list.add(new Breaker(element)); break;
			}
		}
		if (profile=="SSH") {
			if (tagname == "cim:Breaker"){
				cbs_list.add(new BreakerStatus(element));
			}
		}
	}
	
	//*** AUGMENT BREAKERS WITH SSH STATUS ***
	public static void augmentBreakers() {
		for (BreakerStatus cbs : cbs_list) {
			for (int i = 0; i < breaker_list.size(); i++) {
				Breaker breaker = breaker_list.get(i);
				if (cbs.about.equals(breaker.id)) {
					breaker.open = cbs.open;
					breaker_list.set(i, breaker);
				}
			}
		}
	}
	
	//*** FILL SQL DATABASE WITH ARRAY LIST ELEMENTS ***
	public static void filldb() {
		for (VoltageLevel voltlvl : voltlvl_list) {voltlvl.intodb();}		
		for (Substation substation : substation_list) {substation.intodb();}
		for (SynchronousMachine synch : synch_list) {synch.intodb();}
		for (PowerTransformer trafo : trafo_list) {trafo.intodb();}
	}
	
	//*** CLEAR SQL DATABASE ***
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
	
	//*** ALGORITHM FOR Y-BUS MATRIX CREATION ***
	public static void create_ybus() {
		//Option Long: ACLineSegment -> Terminal - Cnode -> Terminal -> BK -> Terminal -> Cnode -> Terminal -> Busbar
		//Option Short: ACLineSegment -> Terminal - Cnode -> Terminal -> BK -> Busbar (through equipment container)
		String from = null, to = null;
		String bus_branch = null;
		String r, x;
		int n=0;
		for (ACLineSegment line : line_list) {
			for (Terminal terminal1 : terminal_list) {
				if (line.id.equals(terminal1.ConductingEquipment)) {			
					for (ConnectivityNode cnode : cnode_list) {
						if (cnode.id.equals(terminal1.ConnectivityNode)) {
							for (Terminal terminal2 : terminal_list) {
								if (!terminal2.id.equals(terminal1.id) && cnode.id.equals(terminal2.ConnectivityNode)) {
									for (Breaker breaker : breaker_list) {
										if (breaker.id.equals(terminal2.ConductingEquipment)) {
											for (BusbarSection busbar : busbar_list) {
												if (busbar.EquipmentContainer.equals(breaker.EquipmentContainer)) {
													if (breaker.open.equals("false")){
														if (n==0) {
															from = busbar.name;
															n++;														
														}
														else {
															to = busbar.name;
															r = Double.toString(line.rtot);
															x = Double.toString(line.xtot);
															bus_branch = from + "   " + to + "   " + r + "   " + x;
															ybus.add(bus_branch);
															n=0;
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}	
		}
	}
	
	//*** OUTPUT Y-BUS MATRIX ***
	public static void print_ybus() {
		System.out.println("   From         " + "   To        " + "R (ohms)       " + "X (ohms)       ");
		System.out.println("---------------------------------------------------");
		for (String bus_branch : ybus) {
			System.out.println(bus_branch);
		}		
	}
}
