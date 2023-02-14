package org.um.feri.ears.problems.real_world.cec2011;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.util.Util;

/**
 * Problem function!
 * 
 * @author Matej Črepinšek
 * @version 1
 * 
 **/	
public class CEC2011_Problem1 extends DoubleProblem {
	/*
	 * fun_num=1   Parameter Estimation for Frequency-Modulated (FM) Sound Waves,initialization range=[0,6.35], bound=[-6.4,6.35] , length of x=6. 
	 * 
	 */
	public CEC2011_Problem1() {
		super(6, 1, 1, 0);
		lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -6.4));
		upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 6.35));
				
		//Arrays.fill(interval, 12.75);
		//Arrays.fill(intervalL, -6.4);//6.4 + 6.35
		name = "RWP_1";
		description = "RWP_1 Parameter Estimation for Frequency-Modulated (FM) Sound Waves";
	}
	
	public double eval(double[] x) {
	      double theta=2.*Math.PI/100;
	      double f=0;
	      double y_t, y_0_t;
	        for (int t=0; t<=100; t++){
	            y_t=x[0]*Math.sin(x[1]*t*theta+x[2]*Math.sin(x[3]*t*theta+x[4]*Math.sin(x[5]*t*theta)));
	            y_0_t=1*Math.sin(5*t*theta-1.5*Math.sin(4.8*t*theta+2*Math.sin(4.9*t*theta)));
	            f=f+(y_t-y_0_t)*(y_t-y_0_t);
	        }
		return f;
	}

	@Override
	public double[] getRandomVariables() {
		//initialization range=[0,6.35]
		double[] var=new double[numberOfDimensions];
		for (int j = 0; j < numberOfDimensions; j++) {
			var[j] = Util.nextDouble(0, 6.35);
		}
		return var;
	}
	
	@Override
	public NumberSolution getRandomEvaluatedSolution() {
		//initialization range=[0,6.35]
		List<Double> var=new ArrayList<Double>();
		for (int j = 0; j < numberOfDimensions; j++) {
			var.add(Util.nextDouble(0, 6.35));
		}
		NumberSolution sol = new NumberSolution(var, eval(var), evaluateConstrains(var));
		return sol;
	}
}