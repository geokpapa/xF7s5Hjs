package assignment1;

import java.util.ArrayList;

public class SearchRoutines {
	
	//*** LINE SEARCH ALGORITHM ***
	public static void line_search(Double SB, 
			ArrayList<ACLineSegment> line_list,
			ArrayList<Terminal> terminal_list,
			ArrayList<ConnectivityNode> cnode_list,
			ArrayList<Breaker> breaker_list,
			ArrayList<BusbarSection> busbar_list,
			ArrayList<VoltageLevel> voltlvl_list,
			ArrayList<BaseVoltage> basevolt_list,
			ArrayList<Ybus> ybus_list) {
		
		//Search algorithm to connect lines to buses:
		//ACLineSegment => Terminal => CNode => Terminal => Breaker => Busbar (through equipment container)		
		String From = null, To = null;
		Double R, X, G, B;
		Double VB = null; //Voltage base (kV).
		Double ZB = null; //Impedance base (ohm).

		boolean ft = true; //From-To flag.
		
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
														if (ft) {
															From = busbar.name; //From bus.
															ft = false; //Switch to To bus.													
														}
														else {														
															VB = busbar.getBaseVoltage(voltlvl_list,basevolt_list); //Get base voltage of bus.
															ZB = Math.pow(VB,2)/SB; //Calculate base impedance at node.
															To = busbar.name; //To bus.															
															R = line.rtot/ZB; //Per unit resistance.
															X = line.xtot/ZB; //Per unit reactance.
															G = line.gtot*ZB; //Per unit addmitance.
															B = line.btot*ZB; //Per unit susceptance.
															ybus_list.add(new Ybus(From,To,R,X,G,B)); //Add branch to Y-Bus.
															ft = true; //Switch to From bus.
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
	
	//*** TRANSFORMER SEARCH ALGORITHM ***
	public static void trafo_search(Double SB, 
			ArrayList<PowerTransformerEnd> trafoEnd_list,
			ArrayList<Terminal> terminal_list,
			ArrayList<ConnectivityNode> cnode_list,
			ArrayList<Breaker> breaker_list,
			ArrayList<BusbarSection> busbar_list,
			ArrayList<VoltageLevel> voltlvl_list,
			ArrayList<BaseVoltage> basevolt_list,
			ArrayList<Ybus> ybus_list) {
		
		//Search algorithm to connect transformers to buses:
		//TransformerEnd => Terminal => CNode => Terminal => Breaker => Busbar (through equipment container)
		String From = null, To = null;
		Double R = null, X = null;
		Double VB = null; //Voltage base (kV).
		Double ZB = null; //Impedance base (ohm).
		Double VBn = null; //nominal Voltage base (kV).
		Double ZBn = null; //nominal Impedance base (ohm).
		Double SBn = null; //nominal power base
		boolean ft = true; //From-To flag.
		
		for (PowerTransformerEnd trafoEnd : trafoEnd_list) {
			for (Terminal terminal1 : terminal_list) {
				if (trafoEnd.terminal_id.equals(terminal1.id)) {			
					for (ConnectivityNode cnode : cnode_list) {
						if (cnode.id.equals(terminal1.ConnectivityNode)) {
							for (Terminal terminal2 : terminal_list) {
								if (!terminal2.id.equals(terminal1.id) && cnode.id.equals(terminal2.ConnectivityNode)) {
									for (Breaker breaker : breaker_list) {
										if (breaker.id.equals(terminal2.ConductingEquipment)) {
											for (BusbarSection busbar : busbar_list) {
												if (busbar.EquipmentContainer.equals(breaker.EquipmentContainer)) {
													if (breaker.open.equals("false")){
														if (ft) {
															VB = busbar.getBaseVoltage(voltlvl_list,basevolt_list); //Get base voltage of bus.
															ZB = Math.pow(VB,2)/SB; //Calculate base impedance at node.
															VBn = trafoEnd.VBn; //Get nominal base voltage of the Transformer.
															SBn = trafoEnd.SBn; //Get nominal base power of the Transformer.
															ZBn = Math.pow(VBn,2)/SBn; //Calculate base impedance at node.
															From = busbar.name; //From bus.
															R = trafoEnd.rtot*ZBn/ZB; //Per unit resistance.
															X = trafoEnd.xtot*ZBn/ZB; //Per unit reactance.	
															ft = false; //Switch to To bus.													
														}
														else {														
															To = busbar.name; //To bus.														
															ybus_list.add(new Ybus(From,To,R,X,0.0,0.0)); //Add branch to Y-Bus.
															ft = true; //Switch to From bus.															
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
	
	//*** SHUNT COMPENSATOR ALGORITHM ***
	public static void scomp_search(Double SB, 
			ArrayList<ShuntCompensator> scomp_list,
			ArrayList<Terminal> terminal_list,
			ArrayList<ConnectivityNode> cnode_list,
			ArrayList<BusbarSection> busbar_list,
			ArrayList<VoltageLevel> voltlvl_list,
			ArrayList<BaseVoltage> basevolt_list,
			ArrayList<Ybus> ybus_list) {
		
		//Search algorithm to connect compensators to buses:
		//Compensator => Terminal => CNode => Terminal => Busbar		
		String From = null, To = null;
		Double G, B;
		Double VB = null; //Voltage base (kV).
		Double YB = null; //Admittance base (ohm).
		
		for (ShuntCompensator scomp : scomp_list) {
			for (Terminal terminal1 : terminal_list) {
				if (scomp.id.equals(terminal1.ConductingEquipment)) {			
					for (ConnectivityNode cnode : cnode_list) {
						if (cnode.id.equals(terminal1.ConnectivityNode)) {
							for (Terminal terminal2 : terminal_list) {
								if (!terminal2.id.equals(terminal1.id) && cnode.id.equals(terminal2.ConnectivityNode)) {									
									for (BusbarSection busbar : busbar_list) {
										if (busbar.id.equals(terminal2.ConductingEquipment)) {														
											VB = busbar.getBaseVoltage(voltlvl_list,basevolt_list); //Get base voltage of bus.
											YB = 1/Math.pow(VB,2)*SB; //Calculate base admittance at node.
											From = "Ground";
											To = busbar.name; //To bus.
											G = scomp.gs/YB; //Per unit conductance.
											B = scomp.bs/YB; //Per unit susceptance.
											ybus_list.add(new Ybus(From,To,0.0,0.0,G,B)); //Add branch to Y-Bus.																				
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
