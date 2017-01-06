package org.um.feri.ears.experiment.so.tk;

import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;

public class EMSolution extends DoubleSolution
{
	double [] Fi;  // Total Force
	double Q;  // Charge

	 // The index of the current best point in the population
    int index;
	
    // The number of function evalautions to find the
    // current best objective function value.
    int count;
	
	
	//navadni	
	public EMSolution(DoubleSolution eval)
	{
		super(eval);
		Q = 0.0;
		Fi = new double[eval.getVariables().size()];
		
	}
	
	//kopirni konstruktor
	public EMSolution(EMSolution copy)
	{
		super(copy);
		
		Fi = new double[copy.Fi.length];
		System.arraycopy(copy.Fi, 0, this.Fi, 0, copy.Fi.length);
		
		this.Q = copy.Q;
	}

	public EMSolution(Task t) throws StopCriteriaException {
		super(t.getRandomSolution());
		
		Q = 0.0;
		Fi = new double[t.getDimensions()];
	}
	
}
