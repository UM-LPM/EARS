package org.um.feri.ears.problems;

import java.util.Arrays;

import org.um.feri.ears.problems.unconstrained.cec2014.F1;

public class Testing {

	public static void main(String[] args) {
		
		F1 p = new F1(2);
		
		double[] t = {50.3,64.9};
		
		System.out.println(Arrays.toString(p.getOptimalVector()[0]));
		
		System.out.println(p.eval(p.getOptimalVector()[0]));
		
		System.out.println(p.eval(t));
		

	}

}
