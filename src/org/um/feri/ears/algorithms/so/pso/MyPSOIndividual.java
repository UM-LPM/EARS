package org.um.feri.ears.algorithms.so.pso;

import java.util.Arrays;

import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

public class MyPSOIndividual extends DoubleSolution{
	MyPSOIndividual p; //personal best
	double v[];
	
	
	public MyPSOIndividual getP() {
		return p;
	}

	public void setP(MyPSOIndividual p) {
		this.p = p;
	}

	public double[] getV() {
		return v;
	}

	public MyPSOIndividual(Task t) throws StopCriteriaException {
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
	
	public MyPSOIndividual(DoubleSolution eval) {
		super(eval);
		
	}

	@Override
	public String toString() {
		return super.toString()+" v:"+(Arrays.toString(v)+" p:"+p.getEval());
	}

	public MyPSOIndividual update(Task t, double v[]) throws StopCriteriaException {
		double x[] = getNewVariables();
		for (int i=0; i<x.length; i++) {
			x[i]=t.feasible(x[i]+v[i],i);
		}
		MyPSOIndividual tmp = new MyPSOIndividual(t.eval(x));
		if (t.isFirstBetter(tmp, p)) {
			tmp.p = tmp;
		} else
			tmp.p = p;
		tmp.v = v;
		return tmp;
		
	}


}
