package org.um.feri.ears.algorithms.so.abc;

import org.um.feri.ears.problems.DoubleSolution;

public class ABCSolution extends DoubleSolution{
	
	public int trials = 0;
	double prob;
	
	public double getProb() {
		return prob;
	}

	public void setProb(double prob) {
		this.prob = prob;
	}

	public ABCSolution(DoubleSolution s)
	{
		super(s);
	}
	
	public ABCSolution(ABCSolution s)
	{
		super(s);
	}

	public double getABCEval() {
		if(this.getEval() >= 0)
			return 1.0/(1.0+this.getEval());
		
		return 1.0+Math.abs(this.getEval());
	}
	
	

}
