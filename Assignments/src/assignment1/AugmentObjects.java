package assignment1;

import java.util.ArrayList;

public class AugmentObjects {
	//*** AUGMENT BREAKERS WITH SSH STATUS ***
	public static void augmentBreakers(ArrayList<Breaker> breaker_list, 
			ArrayList<BreakerStatus> cbs_list) {
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
	public static void augmentSynchMachines(ArrayList<SynchronousMachine> synch_list, 
			ArrayList<SynchronousState> synchState_list) {
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
	
	//*** AUGMENT REGULATING CONTROLS WITH SSH STATUS ***
	public static void augmentRegulatingControls(ArrayList<RegulatingControl> regControl_list, 
			ArrayList<RegulatingControlTarget> regControlTgt_list) {
		for (RegulatingControlTarget regCtlTgt : regControlTgt_list) {
			for (int i = 0; i < regControl_list.size(); i++) {
				RegulatingControl regCtl = regControl_list.get(i);
				if (regCtlTgt.about.equals(regCtl.id)) {
					regCtl.targetValue = regCtlTgt.targetValue;
					regControl_list.set(i, regCtl);
				}
			}
		}
	}

	//*** AUGMENT ENERGY CONSUMERS WITH SSH STATUS ***
	public static void augmentEnergyConsumers(ArrayList<EnergyConsumer> energy_list,
			ArrayList<EnergyConsumerState> energyState_list) {
		for (EnergyConsumerState energyState : energyState_list) {
			for (int i = 0; i < energy_list.size(); i++) {
				EnergyConsumer energy = energy_list.get(i);
				if (energyState.about.equals(energy.id)) {
					energy.P = energyState.P;
					energy.Q = energyState.Q;
					energy_list.set(i, energy);
				}
			}
		}
	}
}