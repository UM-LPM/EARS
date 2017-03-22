package org.um.feri.ears.experiment.so.pso;

import java.util.Arrays;

import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

public class PSOFSSolution extends DoubleSolution{
	PSOFSSolution p; //personal best
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

	public PSOFSSolution getP() {
		return p;
	}

	public void setP(PSOFSSolution p) {
		this.p = p;
	}

	public double[] getV() {
		return v;
	}

	public PSOFSSolution(Task t) throws StopCriteriaException {
		super(t.getRandomSolution());
		v = new double[t.getNumberOfDimensions()];
		double l; double r;
		for (int i=0; i<t.getNumberOfDimensions(); i++) {
			l = -Math.abs(t.getUpperLimit()[i]-t.getLowerLimit()[i])/4; 
			r = Math.abs(t.getUpperLimit()[i]-t.getLowerLimit()[i])/4; 
		    v[i] = Util.nextDouble(l,r);
		}
		p = this;
	}
	
	public PSOFSSolution(DoubleSolution eval) {
		super(eval);
		
	}

	@Override
	public String toString() {
		return super.toString()+" v:"+(Arrays.toString(v)+" p:"+p.getEval());
	}

	public PSOFSSolution update(Task t, double v[]) throws StopCriteriaException {
		double x[] = getDoubleVariables();
		for (int i=0; i<x.length; i++) {
			x[i]=t.setFeasible(x[i]+v[i],i);
		}
		PSOFSSolution tmp = new PSOFSSolution(t.eval(x));
		if (t.isFirstBetter(tmp, p)) {
			tmp.p = tmp;
            
		} else
			tmp.p = p;
		tmp.v = v;
		return tmp;
		
	}


}

