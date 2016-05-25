package org.um.feri.ears.problems.unconstrained.cec2010;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.unconstrained.cec2010.base.RosenbrockShifted;

/**
 * Problem function!
 * 
 * @author Niki Vecek
 * @version 1
 * 
 **/

public class F20 extends Problem {
	
	int[] P;
	RosenbrockShifted rosenbrock_shifted;
	
	// F20 CEC 2010
	// Shifted Rosenbrock's Function
	public F20(int d) {
		super(d,0);
		rosenbrock_shifted = new RosenbrockShifted(numberOfDimensions);
		
		upperLimit = new ArrayList<Double>(d);
		lowerLimit = new ArrayList<Double>(d);
		Collections.fill(lowerLimit, -100.0);
		Collections.fill(upperLimit, 200.0);
		
		name = "F20 Shifted Rosenbrock's Function";
		
		P = new int[numberOfDimensions];
		Random rand = new Random();
		int rand_place = 0;
		for (int i=numberOfDimensions-1; i>0; i--){
			rand_place = rand.nextInt(numberOfDimensions);
			P[i] = rand_place;			
		}
	}
	
	public double eval(double x[]) {
		double F = 0;
		F = rosenbrock_shifted.eval(x, P, 0, numberOfDimensions);
		return F;
	}
	
	@Override
	public double eval(List<Double> ds) {
		double F = 0;
		F = rosenbrock_shifted.eval(ds, P, 0, numberOfDimensions);
		return F;
	}

	public double getOptimumEval() {
		return 0;
	}

	@Override
	public boolean isFirstBetter(List<Double> x, double eval_x, List<Double> y,
			double eval_y) {
		return eval_x < eval_y;
	}
}