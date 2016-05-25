package org.um.feri.ears.problems.unconstrained.cec2010;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.RandomMatrices;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.unconstrained.cec2010.base.RastriginRotated;
import org.um.feri.ears.problems.unconstrained.cec2010.base.RastriginShifted;

/**
 * Problem function!
 * 
 * @author Niki Vecek
 * @version 1
 * 
 **/

public class F10 extends Problem {
	
	int[] P;
	int m;
	public double[][] rot_matrix;
	RastriginShifted rastrigin_shifted;
	RastriginRotated rastrigin_rotated;
	
	// F10 CEC 2010
	// D/2m-group Shifted and m-rotated Rastrigin's Function
	public F10(int d) {
		super(d,0);
		rastrigin_shifted = new RastriginShifted(numberOfDimensions);
		rastrigin_rotated = new RastriginRotated(numberOfDimensions);
		
		upperLimit = new ArrayList<Double>(d);
		lowerLimit = new ArrayList<Double>(d);
		Collections.fill(lowerLimit, -5.0);
		Collections.fill(upperLimit, 10.0);
		
		name = "F10 D/2m-group Shifted and m-rotated Rastrigin's Function";
		
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
			F = F + rastrigin_rotated.eval(x,P,k*m+1,(k+1)*m,rot_matrix);
		}
		F = F + rastrigin_shifted.eval(x,P,numberOfDimensions/2,numberOfDimensions);
		return F;
	}
	
	@Override
	public double eval(List<Double> ds) {
		double F = 0;
		for (int k=0; k<numberOfDimensions/(2*m); k++){
			F = F + rastrigin_rotated.eval(ds,P,k*m+1,(k+1)*m,rot_matrix);
		}
		F = F + rastrigin_shifted.eval(ds,P,numberOfDimensions/2,numberOfDimensions);
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