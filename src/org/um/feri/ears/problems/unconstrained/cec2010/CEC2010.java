package org.um.feri.ears.problems.unconstrained.cec2010;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.um.feri.ears.problems.Problem;

public abstract class CEC2010 extends Problem {

	double[] OShift,M,y,z,x_bound;
	int func_num;
	
	int[] P;
	int m = 50;
	
	public CEC2010(int d, int func_num) {
		super(d, 0);
		
		shortName = "F"+func_num;
		benchmarkName = "CEC2010";
		
		
	    //Search Range
	    if (func_num == 1 | func_num == 4 | func_num == 7 | func_num == 8 | func_num == 9 | func_num == 12 | 
	    	func_num == 13 | func_num == 14 | func_num == 17 | func_num == 18 | func_num == 19 | func_num == 20){
	    	
			lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -100.0));
			upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 100.0));
	    }
	    if (func_num == 2 | func_num == 5 | func_num == 10 | func_num == 15){
			lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -5.0));
			upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 5.0));
	    }
	    if (func_num == 3 | func_num == 6 | func_num == 11 | func_num == 16){
			lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -32.0));
			upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 32.0));
	    }
		

	}
	
	protected List<Double> getPermutatedIndices(List<Double> x, int[] perm, int start, int length) {
		List<Double> s =  new ArrayList<Double>();
		
		for (int i = start; i < start + length; i++) {
			s.add(x.get(perm[i]));
		}
		
		return s;
	}
	
	
	protected double[] getPermutatedIndices(double[] x, int[] perm, int start, int length) {
		double[] s = new double[length];
		int k = 0;
		for (int i = start; i < start + length; i++) {
			s[k++] = x[perm[i]];
		}
		
		return s;
	}
	
	@Override
	public double getOptimumEval() {

		return 0;
	}

	@Override
	public double[][] getOptimalVector() {
		
		double[][] optimal = new double [1][numberOfDimensions];
		optimal[0] = OShift;
		return optimal;
	}

}
