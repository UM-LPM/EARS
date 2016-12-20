package org.um.feri.ears.problems.unconstrained.cec2010;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.unconstrained.cec2010.base.AckleyShifted;
import org.um.feri.ears.util.Util;

/**
 * Problem function!
 * 
 * @author Niki Vecek
 * @version 1
 * 
 **/

public class F3 extends Problem {
	
	int[] P;
	AckleyShifted ackley_shifted;
	
	// F3 CEC 2010
	// Shifted Ackley's Function
	public F3(int d) {
		super(d,0);
		ackley_shifted = new AckleyShifted(numberOfDimensions);
		
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -32.0));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 32.0));
		
		name = "F03 Shifted Ackley's Function";
		
		P = new int[numberOfDimensions];
		int rand_place = 0;
		for (int i=numberOfDimensions-1; i>0; i--){
			rand_place = Util.nextInt(numberOfDimensions);
			P[i] = rand_place;			
		}
	}
	
	public double eval(double x[]) {
		double F = 0;
		F = ackley_shifted.eval(x,P,0,numberOfDimensions);
		return F;
	}
	
	@Override
	public double eval(List<Double> ds) {
		double F = 0;
		F = ackley_shifted.eval(ds,P,0,numberOfDimensions);
		return F;
	}

	public double getOptimumEval() {
		return 0;
	}
	
	/*@Override
	public double[][] getOptimalVector() {

		
	}*/

	@Override
	public boolean isFirstBetter(List<Double> x, double eval_x, List<Double> y,
			double eval_y) {
		return eval_x < eval_y;
	}
}