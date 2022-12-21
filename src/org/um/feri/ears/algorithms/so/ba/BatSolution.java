package org.um.feri.ears.algorithms.so.ba;

import org.um.feri.ears.problems.NumberSolution;

public class BatSolution extends NumberSolution<Double> {
	
	public double[] v; //Velocity
	public double Q; //Frequency
	public double A; //Loudness
	public double r; //pulse rate
	
	public BatSolution(BatSolution s) {
		super(s);
		this.v = s.v;
		this.Q = s.Q;
		this.A = s.A;
		this.r = s.r;
	}
	
	public BatSolution(NumberSolution<Double> s) {
		super(s);
		Q = 0.0;
	}
}
