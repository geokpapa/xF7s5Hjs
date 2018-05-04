package assignment1;

import java.util.ArrayList;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.sql.*;

public class Assignment_I {
	//*** ARRAY LIST DECLARATION ***
	static ArrayList<VoltageLevel> voltlvl_list = new ArrayList<VoltageLevel>();	
	static ArrayList<Substation> substation_list = new ArrayList<Substation>();
	static ArrayList<GeneratingUnit> gen_list = new ArrayList<GeneratingUnit>();
	static ArrayList<SynchronousMachine> synch_list = new ArrayList<SynchronousMachine>();
	static ArrayList<ShuntCompensator> scomp_list = new ArrayList<ShuntCompensator>();
	static ArrayList<PowerTransformer> trafo_list = new ArrayList<PowerTransformer>();
	static ArrayList<PowerTransformerEnd> trafoEnd_list = new ArrayList<PowerTransformerEnd>();
	static ArrayList<BusbarSection> busbar_list = new ArrayList<BusbarSection>();
	static ArrayList<ACLineSegment> line_list = new ArrayList<ACLineSegment>();
	static ArrayList<Terminal> terminal_list = new ArrayList<Terminal>();
	static ArrayList<ConnectivityNode> cnode_list = new ArrayList<ConnectivityNode>();
	static ArrayList<Breaker> breaker_list = new ArrayList<Breaker>();
	static ArrayList<BaseVoltage> basevolt_list = new ArrayList<BaseVoltage>();
	static ArrayList<BreakerStatus> cbs_list = new ArrayList<BreakerStatus>();
	static ArrayList<SynchronousState> synchState_list = new ArrayList<SynchronousState>();
	static ArrayList<Ybus> ybus_list = new ArrayList<Ybus>();
	
	//*** MAIN ROUTINE ***
	public static void main(String[] args) {
		NodeList eq_profile = ReadXML.ToNodeList("xml/MicroGridTestConfiguration_T1_BE_EQ_V2.xml"); //Read CIM EQ profile into Node List.
		NodeList ssh_profile = ReadXML.ToNodeList("xml/MicroGridTestConfiguration_T1_BE_SSH_V2.xml"); //Read CIM SSH profile into Node List.
		for (int i = 0; i < eq_profile.getLength(); i++) {
			extractNode(eq_profile.item(i),"EQ"); //Extract EQ profile node list into database.
		}
		for (int i = 0; i < ssh_profile.getLength(); i++) {
			extractNode(ssh_profile.item(i),"SSH"); //Extract SSH profile node list into database.
		}
		augmentObjects(); //Augment EQ objects with SSH data.
		createYbus(); //Create Y-Bus matrix.
		printYbus(); //Print Y-Bus matrix.
		//createdb("root","xxxx"); //Build SQL database (OBS: Comment this line to work without SQL for debugging purposes!).
	}	
	
	//*** EXTRACT PROFILE NODES INTO ARRAY LISTS FOR EASIER MANIPULATION ***
	public static void extractNode(Node node,String profile){
		Element element = (Element)node;
		String tagname = element.getTagName();
		if (profile=="EQ") {
			switch (tagname) {
				case "cim:VoltageLevel" : voltlvl_list.add(new VoltageLevel(element)); break;
				case "cim:Substation" : substation_list.add(new Substation(element)); break;
				case "cim:GeneratingUnit" : gen_list.add(new GeneratingUnit(element)); break;
				case "cim:SynchronousMachine" : synch_list.add(new SynchronousMachine(element)); break;
				case "cim:LinearShuntCompensator" : scomp_list.add(new ShuntCompensator(element)); break;
				case "cim:PowerTransformer" : trafo_list.add(new PowerTransformer(element)); break;
				case "cim:PowerTransformerEnd" : trafoEnd_list.add(new PowerTransformerEnd(element)); break;
				case "cim:BusbarSection" : busbar_list.add(new BusbarSection(element)); break;
				case "cim:ACLineSegment" : line_list.add(new ACLineSegment(element)); break;
				case "cim:Terminal" : terminal_list.add(new Terminal(element)); break;
				case "cim:ConnectivityNode" : cnode_list.add(new ConnectivityNode(element)); break;
				case "cim:Breaker" : breaker_list.add(new Breaker(element)); break;
				case "cim:BaseVoltage" : basevolt_list.add(new BaseVoltage(element)); break;
			}
		}
		if (profile=="SSH") {
			switch (tagname) {
				case "cim:Breaker" : cbs_list.add(new BreakerStatus(element)); break;
				case "cim:SynchronousMachine" : synchState_list.add(new SynchronousState(element));
			}
		}
	}
	
	//*** AUGMENT EQ PROFILE OBJECTS WITH DATA FROM SSH PROFILE ***
	public static void augmentObjects() {
		augmentBreakers(); //Include breaker status into each breaker object.
		augmentSynchMachines(); //Include P,Q values into each synchronous machine object.
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
	
	//*** AUGMENT SYNCHRONOUS MACHINES WITH SSH STATUS ***
	public static void augmentSynchMachines() {
		for (SynchronousState synchState : synchState_list) {
			for (int i = 0; i < synch_list.size(); i++) {
				SynchronousMachine synch = synch_list.get(i);
				if (synchState.about.equals(synch.id)) {
					synch.P = synchState.P;
					synch.Q = synchState.Q;
					synch_list.set(i, synch);
				}
			}
		}
	}
	
	//*** ALGORITHM FOR Y-BUS MATRIX CREATION ***
	public static void createYbus() {
		Double SB = 100.0; //System power base (MVA).
		SearchRoutines.line_search(SB,line_list,terminal_list,cnode_list,breaker_list,busbar_list,voltlvl_list,basevolt_list,ybus_list); //Connect line to buses.
		SearchRoutines.trafo_search(SB,trafoEnd_list,terminal_list,cnode_list,breaker_list,busbar_list,voltlvl_list,basevolt_list,ybus_list); //Connect transformers to buses.
		SearchRoutines.scomp_search(SB,scomp_list,terminal_list,cnode_list,busbar_list,voltlvl_list,basevolt_list,ybus_list); //Connect transformers to buses.
	}
	
	//*** OUTPUT Y-BUS MATRIX ***
	public static void printYbus() {
	
		System.out.println("     From      " + "     To   " + "     R (p.u)  " + "   X (p.u)    " + "   G (p.u)    " + "   B (p.u)    ");
		System.out.println("-------------------------------------------------------------------------------");
		for (Ybus branch : ybus_list) {
			System.out.format(" %s   %s     %.4f      %.4f      %.4f      %.4f\n",branch.From,branch.To,branch.Real,branch.Imag,branch.Gch,branch.Bch);
		}		
	}
	
	//*** CONNECT TO SQL DATABASE ***
	public static void connectdb(String user, String psswd, Connection conn) {
		try {
			String jdbcString = "jdbc:mysql://localhost:3306/assignment_1?useSSL=false";
			conn = DriverManager.getConnection(jdbcString, user, psswd);
			Statement query = conn.createStatement();
			query.execute("DROP DATABASE IF EXISTS assignment_1"); //Clear SQL database.
			query.execute("CREATE DATABASE IF NOT EXISTS assignment_1"); //Create new SQL database.	
			query.close(); //Close query.
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//*** BUILD SQL DATABASE WITH ARRAY LIST ELEMENTS ***
	public static void createdb(String user, String psswd) {
		try {
			//Connect to database.
			String jdbcString = "jdbc:mysql://localhost:3306/assignment_1?useSSL=false";
			Connection conn = DriverManager.getConnection(jdbcString, user, psswd);
			Statement query = conn.createStatement();
			query.execute("DROP DATABASE IF EXISTS assignment_1"); //Clear SQL database.
			query.execute("CREATE DATABASE IF NOT EXISTS assignment_1"); //Create new SQL database.			
			query.close(); //Close query.
			
			//Fill database with array list elements.
			conn.setCatalog("assignment_1"); //Insure connection to correct database.
			for (BaseVoltage basevolt : basevolt_list) {basevolt.intodb(conn);}
			for (Substation substation : substation_list) {substation.intodb(conn);}
			for (VoltageLevel voltlvl : voltlvl_list) {voltlvl.intodb(conn);}		
			for (GeneratingUnit gen : gen_list) {gen.intodb(conn);}
			for (SynchronousMachine synch : synch_list) {synch.intodb(conn);}
			for (PowerTransformer trafo : trafo_list) {trafo.intodb(conn);}
			for (Ybus branch : ybus_list) {branch.intodb(conn);}	
			
			//Close connection to database.			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
