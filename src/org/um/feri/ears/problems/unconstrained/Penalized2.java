package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

public class Penalized2 extends Problem {

	public Penalized2(int d) {
		super(d,0);
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -50.0));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 50.0));
		name = "Penalized2";
		
		Arrays.fill(optimum[0], 1);
	}
	
	public double eval(double x[]) {
		double v = 0;
		double v_1=0,v_2=0,v_3=0,v_4=0;
		v_1 = Math.pow(Math.sin(Math.PI*x[0]),2);
		for (int i=0; i<numberOfDimensions-1; i++){
			v_2 = v_2 + Math.pow(x[i]-1, 2)*(1+Math.pow(Math.sin(3*Math.PI*x[i+1]),2));
		}
		v_3 = Math.pow(x[numberOfDimensions-1]-1,2)*(1+Math.pow(Math.sin(1*Math.PI*x[numberOfDimensions-1]),2));
		for (int i=0; i<numberOfDimensions; i++){
			v_4 = v_4 + u(x[i],5,100,4);
		}
		v = Math.PI/numberOfDimensions*(v_1  + v_2  + v_3) + v_4;
		return v;
	}
	
	private double u(double x, double a, double k, double m){
		if (x>a) return  k*Math.pow(x-a,m);
		else if (x<a && x>-1*a) return 0;
		else  return  k*Math.pow(-1*x-a,m);
	}

	public double getOptimumEval() {
		return 0;
	}

	@Override
	public double eval(Double[] ds) {
		return eval(ArrayUtils.toPrimitive(ds));
	}

}
