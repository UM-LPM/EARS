package org.um.feri.ears.problems.unconstrained.cec2010;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.RandomMatrices;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.unconstrained.cec2010.base.EllipticRotated;
import org.um.feri.ears.problems.unconstrained.cec2010.base.EllipticShifted;
import org.um.feri.ears.util.Util;

/**
 * Problem function!
 * 
 * @author Niki Vecek
 * @version 1
 * 
 **/

public class F9 extends Problem {
	
	int[] P;
	int m;
	public double[][] rot_matrix;
	EllipticShifted elliptic_shifted;
	EllipticRotated elliptic_rotated;
	
	// F9 CEC 2010
	// D/2m-group Shifted and m-rotated Elliptic Function
	public F9(int d) {
		super(d,0);
		elliptic_shifted = new EllipticShifted(numberOfDimensions);
		elliptic_rotated= new EllipticRotated(numberOfDimensions);
		
		upperLimit = new ArrayList<Double>(d);
		lowerLimit = new ArrayList<Double>(d);
		Collections.fill(lowerLimit, -100.0);
		Collections.fill(upperLimit, 200.0);
		
		name = "F09 D/2m-group Shifted and m-rotated Elliptic Function";
		
		P = new int[numberOfDimensions];
		int rand_place = 0;
		for (int i=numberOfDimensions-1; i>0; i--){
			rand_place = Util.nextInt(numberOfDimensions);
			P[i] = rand_place;			
		}
		
		m = 2;
		
		rot_matrix = new double[m][m];
		
		DenseMatrix64F A = RandomMatrices.createOrthogonal(m, m, Util.rnd);
		
		for (int i=0; i<m; i++){
			for (int j=0; j<m; j++){
				rot_matrix[i][j] = A.get(i, j);
			}
		}
	}
	
	public double eval(double x[]) {
		double F = 0;
		for (int k=0; k<numberOfDimensions/(2*m); k++){
			F = F + elliptic_rotated.eval(x,P,k*m+1,(k+1)*m, rot_matrix);
		}
		F = F + elliptic_shifted.eval(x,P,numberOfDimensions/2,numberOfDimensions);
		return F;
	}
	
	@Override
	public double eval(List<Double> ds) {
		double F = 0;
		for (int k=0; k<numberOfDimensions/(2*m); k++){
			F = F + elliptic_rotated.eval(ds,P,k*m+1,(k+1)*m, rot_matrix);
		}
		F = F + elliptic_shifted.eval(ds,P,numberOfDimensions/2,numberOfDimensions);
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