package org.um.feri.ears.problems.unconstrained.cec2010;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.RandomMatrices;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.unconstrained.cec2010.base.AckleyRotated;
import org.um.feri.ears.problems.unconstrained.cec2010.base.AckleyShifted;

/**
 * Problem function!
 * 
 * @author Niki Vecek
 * @version 1
 * 
 **/

public class F11 extends Problem {
	
	int[] P;
	int m;
	public double[][] rot_matrix;
	AckleyShifted ackley_shifted;
	AckleyRotated ackley_rotated;
	
	// F11 CEC 2010
	// D/2m-group Shifted and m-rotated Ackley's Function
	public F11(int d) {
		super(d,0);
		ackley_shifted = new AckleyShifted(numberOfDimensions);
		ackley_rotated= new AckleyRotated(numberOfDimensions);
		
		upperLimit = new ArrayList<Double>(d);
		lowerLimit = new ArrayList<Double>(d);
		Collections.fill(lowerLimit, -32.0);
		Collections.fill(upperLimit, 64.0);
		
		name = "F11 D/2m-group Shifted and m-rotated Ackley's Function";
		
		P = new int[numberOfDimensions];
		Random rand = new Random();
		int rand_place = 0;
		for (int i=numberOfDimensions-1; i>0; i--){
			rand_place = rand.nextInt(numberOfDimensions);
			P[i] = rand_place;			
		}
		
		m = 2;
		
		rot_matrix = new double[m][m];
		
		Random rand1 = new Random();
		DenseMatrix64F A = RandomMatrices.createOrthogonal(m, m, rand1);
		
		for (int i=0; i<m; i++){
			for (int j=0; j<m; j++){
				rot_matrix[i][j] = A.get(i, j);
			}
		}
	}
	
	public double eval(double x[]) {
		double F = 0;
		for (int k=0; k<numberOfDimensions/(2*m); k++){
			F = F + ackley_rotated.eval(x,P,k*m+1,(k+1)*m,rot_matrix);
		}
		F = F + ackley_shifted.eval(x,P,numberOfDimensions/2,numberOfDimensions);
		return F;
	}
	
	@Override
	public double eval(List<Double> ds) {
		double F = 0;
		for (int k=0; k<numberOfDimensions/(2*m); k++){
			F = F + ackley_rotated.eval(ds,P,k*m+1,(k+1)*m,rot_matrix);
		}
		F = F + ackley_shifted.eval(ds,P,numberOfDimensions/2,numberOfDimensions);
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