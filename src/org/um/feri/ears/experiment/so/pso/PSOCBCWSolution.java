package org.um.feri.ears.experiment.so.pso;

import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;

public class PSOCBCWSolution extends DoubleSolution
{
	PSOCBCWSolution p;
	double v[];
	
	
	public PSOCBCWSolution getP()
	{
		return p;
	}

	public void setP(PSOCBCWSolution p)
	{
		this.p = p;
	}

	public double[] getV()
	{
		return v;
	}
	
	public PSOCBCWSolution(Task t) throws StopCriteriaException
	{
		super(t.getRandomSolution());
		v = new double[t.getNumberOfDimensions()];
		
		for (int i=0; i<t.getNumberOfDimensions(); i++)
		{			
			v[i] = 0;
		}
		
		p = this;
	}
	
	public PSOCBCWSolution(DoubleSolution eval)
	{
		super(eval);		
	}

	@Override
	public String toString()
	{
		return super.toString() + " eval:" + p.getEval();
	}

	public PSOCBCWSolution update(Task t, double v[]) throws StopCriteriaException
	{
		double x[] = getDoubleVariables();
		
		for (int i=0; i<x.length; i++)
		{
			x[i]=t.setFeasible(x[i]+v[i],i);
		}
		
		PSOCBCWSolution tmp = new PSOCBCWSolution(t.eval(x));
		
		if (t.isFirstBetter(tmp, p))
		{
			tmp.p = tmp;
		} 
		else
		{
			tmp.p = p;
		}
		
		tmp.v = v;
		
		return tmp;		
	}


}
