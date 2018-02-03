package org.um.feri.ears.experiment.so.tk;

import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;

public class CSSSolution extends DoubleSolution
{
	double Q; //magnitude of charge, quality of the movement
	double [] v; //Velocity
	double [] F; //force, ki deluje na delec
	
	public CSSSolution(DoubleSolution i) 
	{
		super(i);
		v = new double[i.getVariables().size()]; //zacetni velocity na nulo
		F = new double[i.getVariables().size()];
		Q = 0.0;
	}
	
	public CSSSolution(Task t) throws StopCriteriaException {
		super(t.getRandomSolution());
		v = new double[t.getNumberOfDimensions()]; //začetni velocity na 0
		F = new double[t.getNumberOfDimensions()];  //init force na 0
		Q =  0.0; // masa nabitega delca
	
	}
	
	public CSSSolution(DoubleSolution i, double[] vi) throws StopCriteriaException {
		super(i);
		
		v = new double[vi.length];
		System.arraycopy(vi, 0, this.v, 0, vi.length);	
		
		F = new double[vi.length]; 
		Q =  0.0;
	
	}
	
	
	public CSSSolution (CSSSolution kopija)
	{
		super(kopija);
		
		v = new double[kopija.v.length];
		F = new double[kopija.F.length];
		
		System.arraycopy(kopija.v, 0, this.v, 0, kopija.v.length);	
		System.arraycopy(kopija.F, 0, this.F, 0, kopija.F.length);	
		this.Q = kopija.Q;		
	}
	
}