package org.um.feri.ears.algorithms.so.pso.variations;

import java.util.Arrays;
import java.util.List;

import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

public class PSOSIndividual extends DoubleSolution implements Comparable<PSOSIndividual>{
	PSOSIndividual p; //personal best
	double v[];
	public int turnamentScore = 0;
	
	public PSOSIndividual getP() {
		return p;
	}

	public void setP(PSOSIndividual p) {
		this.p = p;
	}

	public double[] getV() {
		return v;
	}

	@SuppressWarnings("deprecation")
	public PSOSIndividual(List<Double> list, double eval)
	{
		//super(x, eval,new double[0]);
		super(list, eval);
	}
	public PSOSIndividual(Task t) throws StopCriteriaException {
		super(t.getRandomSolution());
		v = new double[t.getDimensions()];
		double l; double r;
		for (int i=0; i<t.getDimensions(); i++) {
			l = -Math.abs(t.getUpperLimit()[i]-t.getLowerLimit()[i])/4; 
			r = Math.abs(t.getUpperLimit()[i]-t.getLowerLimit()[i])/4; 
		    v[i] = Util.rnd.nextDouble()*(r-l)+l;
		}
		p = this;
	}
	
	public PSOSIndividual(DoubleSolution eval) {
		super(eval);
		
	}

	@Override
	public String toString() {
		return super.toString()+" v:"+(Arrays.toString(v)+" p:"+p.getEval());
	}

	public PSOSIndividual update(Task t, double v[]) throws StopCriteriaException {
		double x[] = getNewVariables();
		for (int i=0; i<x.length; i++) {
			x[i]=t.feasible(x[i]+v[i],i);
		}
		PSOSIndividual tmp = new PSOSIndividual(t.eval(x));
		this.turnamentScore = 0;
		if (t.isFirstBetter(tmp, p)) {
			tmp.p = tmp;
		} else
			tmp.p = p;
		tmp.v = v;
		return tmp;
	
	}
	
	public static PSOSIndividual update(List<Double> list, double v[], PSOSIndividual p, double eval) throws StopCriteriaException
	{
		PSOSIndividual tmp = new PSOSIndividual(list, eval);
		tmp.v = v;
		tmp.p = p;
		return tmp;
	}

	@Override
	public int compareTo(PSOSIndividual individual) {
		return Integer.compare(this.turnamentScore, individual.turnamentScore);
	}


}
