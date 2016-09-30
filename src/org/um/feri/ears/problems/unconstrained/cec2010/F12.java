package org.um.feri.ears.problems.unconstrained.cec2010;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.unconstrained.cec2010.base.SchwefelShifted;
import org.um.feri.ears.problems.unconstrained.cec2010.base.SphereShifted;
import org.um.feri.ears.util.Util;

/**
 * Problem function!
 * 
 * @author Niki Vecek
 * @version 1
 * 
 **/

public class F12 extends Problem {
	
	int[] P;
	int m;
	SchwefelShifted schwefel_shifted;
	SphereShifted sphere_shifted;
	
	// F12 CEC 2010
	// D/2m-group Shifted m-dimensional Schwefel's Problem 1.2
	public F12(int d) {
		super(d,0);
		schwefel_shifted = new SchwefelShifted(numberOfDimensions);
		sphere_shifted= new SphereShifted(numberOfDimensions);
		
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -100.0));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 200.0));
		
		name = "F12 D/2m-group Shifted m-dimensional Schwefel's Problem 1.2";
		
		P = new int[numberOfDimensions];
		int rand_place = 0;
		for (int i=numberOfDimensions-1; i>0; i--){
			rand_place = Util.nextInt(numberOfDimensions);
			P[i] = rand_place;			
		}
		
		m = 2;
	}
	
	public double eval(double x[]) {
		double F = 0;
		for (int k=0; k<numberOfDimensions/(2*m); k++){
			F = F + schwefel_shifted.eval(x,P,k*m+1,(k+1)*m);
		}
		F = F + sphere_shifted.eval(x,P,numberOfDimensions/2,numberOfDimensions);
		return F;
	}
	
	@Override
	public double eval(List<Double> ds) {
		double F = 0;
		for (int k=0; k<numberOfDimensions/(2*m); k++){
			F = F + schwefel_shifted.eval(ds,P,k*m+1,(k+1)*m);
		}
		F = F + sphere_shifted.eval(ds,P,numberOfDimensions/2,numberOfDimensions);
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