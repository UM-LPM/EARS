package org.um.feri.ears.algorithms.so.pso.variations;

import java.util.Arrays;

import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

public class PSOFSIndividual extends DoubleSolution{
	PSOFSIndividual p; //personal best
	double v[];
	int rank;
	
	
	/**
	 * @return the rank
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * @param rank the rank to set
	 */
	public void setRank(int rank) {
		this.rank = rank;
	}

	/**
	 * @param v the v to set
	 */
	public void setV(double[] v) {
		this.v = v;
	}

	public PSOFSIndividual getP() {
		return p;
	}

	public void setP(PSOFSIndividual p) {
		this.p = p;
	}

	public double[] getV() {
		return v;
	}

	public PSOFSIndividual(Task t) throws StopCriteriaException {
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
	
	public PSOFSIndividual(DoubleSolution eval) {
		super(eval);
		
	}

	@Override
	public String toString() {
		return super.toString()+" v:"+(Arrays.toString(v)+" p:"+p.getEval());
	}

	public PSOFSIndividual update(Task t, double v[]) throws StopCriteriaException {
		double x[] = getNewVariables();
		for (int i=0; i<x.length; i++) {
			x[i]=t.feasible(x[i]+v[i],i);
		}
		PSOFSIndividual tmp = new PSOFSIndividual(t.eval(x));
		if (t.isFirstBetter(tmp, p)) {
			tmp.p = tmp;
            
		} else
			tmp.p = p;
		tmp.v = v;
		return tmp;
		
	}


}

