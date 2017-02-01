package org.um.feri.ears.experiment.so.pso;

import java.util.Arrays;

import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

public class PSOISSolution extends DoubleSolution {
	PSOISSolution Pbest;
	double v[];

	public PSOISSolution getPbest() {
		return Pbest;
	}

	public void setPbest(PSOISSolution Pbest) {
		this.Pbest = Pbest;
	}

	public double[] getV() {
		return v;
	}

	public PSOISSolution(Task t) throws StopCriteriaException {
		super(t.getRandomSolution());
		v = new double[t.getNumberOfDimensions()];
		double l;
		double r;
		for (int i = 0; i < t.getNumberOfDimensions(); i++) {
			l = -Math.abs(t.getUpperLimit()[i] - t.getLowerLimit()[i]) / 4;
			r = Math.abs(t.getUpperLimit()[i] - t.getLowerLimit()[i]) / 4;
			v[i] = Util.nextDouble(l,r);
		}
		Pbest = this;
	}

	public PSOISSolution(DoubleSolution eval) {
		super(eval);

	}

	@Override
	public String toString() {
		return super.toString() + " v:" + (Arrays.toString(v) + " p:" + Pbest.getEval());
	}

	public PSOISSolution update(Task t, double v[]) throws StopCriteriaException {
		double x[] = getNewVariables();
		for (int i = 0; i < x.length; i++) {
			x[i] = t.setFeasible(x[i] + v[i], i);
		}
		PSOISSolution tmp = new PSOISSolution(t.eval(x));
		if (t.isFirstBetter(tmp, Pbest)) {
			tmp.Pbest = tmp;
		} else
			tmp.Pbest = Pbest;
		tmp.v = v;
		return tmp;

	}
	
	public PSOISSolution updateP(Task t, double v[], PSOISSolution oldx) throws StopCriteriaException {
		double x[] = getNewVariables();
		boolean improved = false;
		for (int i = 0; i < x.length; i++) {
			x[i] = t.setFeasible(x[i] + v[i], i);
		}
		
		for (int i = 0; i < x.length; i++) {
			if(t.isFirstBetter(x[i], oldx.getNewVariables()[i])) {
				oldx.getNewVariables()[i] = t.setFeasible(x[i] + v[i], i);
				oldx.getV()[i] = v[i];
				improved = true;
			}
		}
		PSOISSolution tmp;
		if(improved) {
			tmp = new PSOISSolution(t.eval(oldx.getNewVariables()));
			if (t.isFirstBetter(tmp, Pbest)) {
				tmp.Pbest = tmp;
			} else
				tmp.Pbest = Pbest;
			tmp.v = v;
		}
		else {
		tmp = new PSOISSolution(t.eval(x));
		if (t.isFirstBetter(tmp, Pbest)) {
			tmp.Pbest = tmp;
		} else
			tmp.Pbest = Pbest;
		tmp.v = v;
		}
		return tmp;

	}
}