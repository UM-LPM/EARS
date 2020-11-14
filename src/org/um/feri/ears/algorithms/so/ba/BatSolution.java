package org.um.feri.ears.algorithms.so.ba;

import org.um.feri.ears.problems.DoubleSolution;

public class BatSolution extends DoubleSolution{
	
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
	
	public BatSolution(DoubleSolution s) {
		super(s);
		Q = 0.0;
	}
}
