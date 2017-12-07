package org.um.feri.ears.tuning;

import java.util.ArrayList;
import java.util.logging.Level;

import org.um.feri.ears.algorithms.moo.moead.I_MOEAD;
import org.um.feri.ears.algorithms.moo.nsga2.I_NSGAII;
import org.um.feri.ears.algorithms.moo.pesa2.I_PESAII;
import org.um.feri.ears.algorithms.tuning.GSA_Tuning;

public class TestMOCRSTuning {

	public static void main(String[] args) {
		
    	ArrayList<ControlParameter> control_parameters = new ArrayList<ControlParameter>();
        control_parameters.add(new ControlParameter("populationSize", "int", 10, 200));
        control_parameters.add(new ControlParameter("crossoverProbability", "double", 0.1, 1));
        control_parameters.add(new ControlParameter("mutationProbability", "double", 0.1, 1));
        
        long initTime = System.currentTimeMillis();
        try {
        	MOCRSTuning m = new MOCRSTuning();        	
        	m.tune(I_PESAII.class, "PESAII", control_parameters);
        	//m.tune(runs,control_parameters,"org.um.feri.ears.algorithms.de3.DEAlgorithm","DE",decimals);
        	
		} catch (Exception e) {
			e.printStackTrace();
		}
        
		long estimatedTime = (System.currentTimeMillis() - initTime) / 1000;
		System.out.println("Total execution time: "+estimatedTime + "s");

	}

}
