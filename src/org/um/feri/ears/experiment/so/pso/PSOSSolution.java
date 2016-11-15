package org.um.feri.ears.experiment.so.pso;

import java.util.Arrays;
import java.util.List;

import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

public class PSOSSolution extends DoubleSolution implements Comparable<PSOSSolution>{
	PSOSSolution p; //personal best
	double v[];
	public int turnamentScore = 0;
	
	public PSOSSolution getP() {
		return p;
	}

	public void setP(PSOSSolution p) {
		this.p = p;
	}

	public double[] getV() {
		return v;
	}

	@SuppressWarnings("deprecation")
	public PSOSSolution(List<Double> list, double eval)
	{
		//super(x, eval,new double[0]);
		super(list, eval);
	}
	public PSOSSolution(Task t) throws StopCriteriaException {
		super(t.getRandomSolution());
		v = new double[t.getDimensions()];
		double l; double r;
		for (int i=0; i<t.getDimensions(); i++) {
			l = -Math.abs(t.getUpperLimit()[i]-t.getLowerLimit()[i])/4; 
			r = Math.abs(t.getUpperLimit()[i]-t.getLowerLimit()[i])/4; 
		    v[i] = Util.nextDouble(l,r);
		}
		p = this;
	}
	
	public PSOSSolution(DoubleSolution eval) {
		super(eval);
		
	}

	@Override
	public String toString() {
		return super.toString()+" v:"+(Arrays.toString(v)+" p:"+p.getEval());
	}

	public PSOSSolution update(Task t, double v[]) throws StopCriteriaException {
		double x[] = getNewVariables();
		for (int i=0; i<x.length; i++) {
			x[i]=t.setFeasible(x[i]+v[i],i);
		}
		PSOSSolution tmp = new PSOSSolution(t.eval(x));
		this.turnamentScore = 0;
		if (t.isFirstBetter(tmp, p)) {
			tmp.p = tmp;
		} else
			tmp.p = p;
		tmp.v = v;
		return tmp;
	
	}
	
	public static PSOSSolution update(List<Double> list, double v[], PSOSSolution p, double eval) throws StopCriteriaException
	{
		PSOSSolution tmp = new PSOSSolution(list, eval);
		tmp.v = v;
		tmp.p = p;
		return tmp;
	}

	@Override
	public int compareTo(PSOSSolution individual) {
		return Integer.compare(this.turnamentScore, individual.turnamentScore);
	}


}
