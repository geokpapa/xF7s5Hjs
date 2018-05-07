package assignment1;

import java.util.ArrayList;

public class PrintYbus {
	//*** OUTPUT Y-BUS MATRIX IN BUS-BRANCH FORMAT ***
	public static void printBusBranch(ArrayList<Ybus> ybus_list) {
		System.out.println("The Y-bus in the bus-branch is the following:\n");
		System.out.println("     From      " + "     To   " + "     R (p.u)  " + "   X (p.u)    " + "   G (p.u)    " + "   B (p.u)    "+ "Device Type          " + "Device  ");
		System.out.println("--------------------------------------------------------------------------------------------------------------");
		for (Ybus branch : ybus_list) {
			if (branch.devType=="Shunt Capacitor")
			{
				System.out.format(" %s        %s     %.4f      %.4f        %.4f        %.4f     %s         %s\n",
						branch.From,branch.To,branch.R,branch.X,branch.Gch,branch.Bch,branch.devType,branch.dev);
			}
			else {
				System.out.format(" %s   %s     %.4f      %.4f        %.4f        %.4f     %s              %s\n",
						branch.From,branch.To,branch.R,branch.X,branch.Gch,branch.Bch,branch.devType,branch.dev);	
			}	
		}		
	}

	//*** OUTPUT Y-BUS MATRIX IN TABLE FORMAT ***
	public static void printYbusMatrix(ArrayList<Ybus> ybus_list, ArrayList<BusbarSection> busbar_list) {
		int Ybus_number=0;
		Complex temp1, temp2;
		Complex[][] Ybus_elements;
		ArrayList<BusbarSection> temp_busbar_list = new ArrayList<BusbarSection>();
		for (Ybus branch : ybus_list) {
			for (BusbarSection busbar : busbar_list) {
				if (branch.From.equals(busbar.name)) {
					busbar.connected=true;
				}
				if (branch.To.equals(busbar.name)) {
					busbar.connected=true;
				}	
			}
		}
		for (BusbarSection busbar : busbar_list) {
				if(busbar.connected) {
					temp_busbar_list.add(busbar);
					busbar.number_in_Ybus=Ybus_number;
					Ybus_number=Ybus_number+1;
				}
		}
		Ybus_elements = new Complex[temp_busbar_list.size()][temp_busbar_list.size()];
		for (int i=0;i<temp_busbar_list.size();i++) {
			for (int j=0;j<temp_busbar_list.size();j++) {
				Ybus_elements[i][j] = new Complex(0.0,0.0);	
			}
		}

		for (Ybus branch : ybus_list) {
			if (branch.devType=="Shunt Capacitor") {
				for (BusbarSection busbar : temp_busbar_list) {
					if (branch.From.equals(busbar.name)||branch.To.equals(busbar.name)) {								
						temp2 = new Complex(branch.Gch, branch.Bch);
						Ybus_elements[busbar.number_in_Ybus][busbar.number_in_Ybus]=Ybus_elements[busbar.number_in_Ybus][busbar.number_in_Ybus].plus(temp2);
					}
				}
			}
			else {
				for (BusbarSection busbar : temp_busbar_list) {
					if (branch.From.equals(busbar.name)) {
						for (BusbarSection busbar2 : temp_busbar_list) {
							if (branch.To.equals(busbar2.name)) {	
								temp1 = new Complex(branch.R, branch.X);
								temp1=temp1.reciprocal();								
								if (branch.devType=="Line") {
									temp2 = new Complex(branch.Gch/2, branch.Bch/2);				
									Ybus_elements[busbar.number_in_Ybus][busbar.number_in_Ybus]=Ybus_elements[busbar.number_in_Ybus][busbar.number_in_Ybus].plus(temp1);
									Ybus_elements[busbar.number_in_Ybus][busbar.number_in_Ybus]=Ybus_elements[busbar.number_in_Ybus][busbar.number_in_Ybus].plus(temp2);
									Ybus_elements[busbar2.number_in_Ybus][busbar2.number_in_Ybus]=Ybus_elements[busbar2.number_in_Ybus][busbar2.number_in_Ybus].plus(temp1);
									Ybus_elements[busbar2.number_in_Ybus][busbar2.number_in_Ybus]=Ybus_elements[busbar2.number_in_Ybus][busbar2.number_in_Ybus].plus(temp2);
									Ybus_elements[busbar.number_in_Ybus][busbar2.number_in_Ybus]=Ybus_elements[busbar.number_in_Ybus][busbar2.number_in_Ybus].minus(temp1);
									Ybus_elements[busbar2.number_in_Ybus][busbar.number_in_Ybus]=Ybus_elements[busbar2.number_in_Ybus][busbar.number_in_Ybus].minus(temp1);
								}
								else {
									Ybus_elements[busbar.number_in_Ybus][busbar.number_in_Ybus]=Ybus_elements[busbar.number_in_Ybus][busbar.number_in_Ybus].plus(temp1);
									Ybus_elements[busbar2.number_in_Ybus][busbar2.number_in_Ybus]=Ybus_elements[busbar2.number_in_Ybus][busbar2.number_in_Ybus].plus(temp1);
									Ybus_elements[busbar.number_in_Ybus][busbar2.number_in_Ybus]=Ybus_elements[busbar.number_in_Ybus][busbar2.number_in_Ybus].minus(temp1);
									Ybus_elements[busbar2.number_in_Ybus][busbar.number_in_Ybus]=Ybus_elements[busbar2.number_in_Ybus][busbar.number_in_Ybus].minus(temp1);
								}	
							}
						}
					}
				}	
			}
		}	
		System.out.println("\n\nThe Y-bus in the matrix format is the following:\n");
		for (int i=0;i<temp_busbar_list.size();i++) {
			for (int j=0;j<temp_busbar_list.size();j++) {
				System.out.format("   %.4f %.4fi   |",Ybus_elements[i][j].re,Ybus_elements[i][j].im);
			}
			System.out.println();
			System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
			System.out.println();
		}
	}
}
