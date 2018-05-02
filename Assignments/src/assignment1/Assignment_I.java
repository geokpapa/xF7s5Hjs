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
	static ArrayList<RegulatingControl> regControl_list = new ArrayList<RegulatingControl>();
	static ArrayList<RatioTapChanger> tapChanger_list = new ArrayList<RatioTapChanger>();
	static ArrayList<RegulatingControlTarget> regControlTgt_list = new ArrayList<RegulatingControlTarget>();
	static ArrayList<EnergyConsumer> energy_list = new ArrayList<EnergyConsumer>();
	static ArrayList<BreakerStatus> cbs_list = new ArrayList<BreakerStatus>();
	static ArrayList<SynchronousState> synchState_list = new ArrayList<SynchronousState>();
	static ArrayList<EnergyConsumerState> energyState_list = new ArrayList<EnergyConsumerState>();
	static ArrayList<RatioTapChangerStep> tapChangerStep_list = new ArrayList<RatioTapChangerStep>();
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
		createdb("root","xxxx"); //Build SQL database (OBS: Comment this line to work without SQL for debugging purposes!).
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
				case "cim:RegulatingControl" : regControl_list.add(new RegulatingControl(element)); break;
				case "cim:EnergyConsumer" : energy_list.add(new EnergyConsumer(element)); break;
				case "cim:RatioTapChanger" : tapChanger_list.add(new RatioTapChanger(element)); break;
			}
		}
		if (profile=="SSH") {
			switch (tagname) {
				case "cim:Breaker" : cbs_list.add(new BreakerStatus(element)); break;
				case "cim:SynchronousMachine" : synchState_list.add(new SynchronousState(element)); break;
				case "cim:RegulatingControl" : regControlTgt_list.add(new RegulatingControlTarget(element)); break;
				case "cim:EnergyConsumer" : energyState_list.add(new EnergyConsumerState(element)); break;
				case "cim:RatioTapChangerStep" : tapChangerStep_list.add(new RatioTapChangerStep(element)); break;
			}
		}
	}
	
	//*** AUGMENT EQ PROFILE OBJECTS WITH DATA FROM SSH PROFILE ***
	public static void augmentObjects() {
		AugmentObjects.augmentBreakers(breaker_list,cbs_list); //Include breaker status into each breaker object.
		AugmentObjects.augmentSynchMachines(synch_list,synchState_list); //Include P,Q values into each synchronous machine object.
		AugmentObjects.augmentRegulatingControls(regControl_list, regControlTgt_list); //Include target value into regulating control object.
		AugmentObjects.augmentEnergyConsumers(energy_list, energyState_list); //Include P,Q values into each energy consumer.
		AugmentObjects.augmentRatioTapChangers(tapChanger_list, tapChangerStep_list); //Include current tap position (step) into each tap changer object.
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
		System.out.println("     From      " + "     To   " + "     R/G (p.u)  " + "   X/B (p.u)    ");
		System.out.println("-----------------------------------------------------");
		for (Ybus branch : ybus_list) {
			System.out.format(" %s   %s     %.4f      %.4f\n",branch.From,branch.To,branch.Real,branch.Imag);
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
			for (RegulatingControl regCtl : regControl_list) {regCtl.intodb(conn);}
			for (PowerTransformer trafo : trafo_list) {trafo.intodb(conn);}
			for (EnergyConsumer energy : energy_list) {energy.intodb(conn);}
			for (PowerTransformerEnd trafoEnd : trafoEnd_list) {trafoEnd.intodb(conn);}
			for (Breaker breaker : breaker_list) {breaker.intodb(conn);}
			for (RatioTapChanger tapChanger : tapChanger_list) {tapChanger.intodb(conn);}
			for (Ybus branch : ybus_list) {branch.intodb(conn);}	
			
			//Close connection to database.			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
