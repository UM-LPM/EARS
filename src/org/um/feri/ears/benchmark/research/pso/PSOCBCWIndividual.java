package org.um.feri.ears.benchmark.research.pso;

import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;

public class PSOCBCWIndividual extends DoubleSolution
{
	PSOCBCWIndividual p;
	double v[];
	
	
	public PSOCBCWIndividual getP()
	{
		return p;
	}

	public void setP(PSOCBCWIndividual p)
	{
		this.p = p;
	}

	public double[] getV()
	{
		return v;
	}
	
	public PSOCBCWIndividual(Task t) throws StopCriteriaException
	{
		super(t.getRandomIndividual());
		v = new double[t.getDimensions()];
		
		for (int i=0; i<t.getDimensions(); i++)
		{			
			v[i] = 0;
		}
		
		p = this;
	}
	
	public PSOCBCWIndividual(DoubleSolution eval)
	{
		super(eval);		
	}

	@Override
	public String toString()
	{
		return super.toString() + " eval:" + p.getEval();
	}

	public PSOCBCWIndividual update(Task t, double v[]) throws StopCriteriaException
	{
		double x[] = getNewVariables();
		
		for (int i=0; i<x.length; i++)
		{
			x[i]=t.feasible(x[i]+v[i],i);
		}
		
		PSOCBCWIndividual tmp = new PSOCBCWIndividual(t.eval(x));
		
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
