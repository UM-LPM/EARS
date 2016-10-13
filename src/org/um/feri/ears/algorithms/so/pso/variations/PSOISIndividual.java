package org.um.feri.ears.algorithms.so.pso.variations;

import java.util.Arrays;

import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

public class PSOISIndividual extends DoubleSolution {
	PSOISIndividual Pbest;
	double v[];

	public PSOISIndividual getPbest() {
		return Pbest;
	}

	public void setPbest(PSOISIndividual Pbest) {
		this.Pbest = Pbest;
	}

	public double[] getV() {
		return v;
	}

	public PSOISIndividual(Task t) throws StopCriteriaException {
		super(t.getRandomSolution());
		v = new double[t.getDimensions()];
		double l;
		double r;
		for (int i = 0; i < t.getDimensions(); i++) {
			l = -Math.abs(t.getUpperLimit()[i] - t.getLowerLimit()[i]) / 4;
			r = Math.abs(t.getUpperLimit()[i] - t.getLowerLimit()[i]) / 4;
			v[i] = Util.rnd.nextDouble() * (r - l) + l;
		}
		Pbest = this;
	}

	public PSOISIndividual(DoubleSolution eval) {
		super(eval);

	}

	@Override
	public String toString() {
		return super.toString() + " v:" + (Arrays.toString(v) + " p:" + Pbest.getEval());
	}

	public PSOISIndividual update(Task t, double v[]) throws StopCriteriaException {
		double x[] = getNewVariables();
		for (int i = 0; i < x.length; i++) {
			x[i] = t.feasible(x[i] + v[i], i);
		}
		PSOISIndividual tmp = new PSOISIndividual(t.eval(x));
		if (t.isFirstBetter(tmp, Pbest)) {
			tmp.Pbest = tmp;
		} else
			tmp.Pbest = Pbest;
		tmp.v = v;
		return tmp;

	}
	
	public PSOISIndividual updateP(Task t, double v[], PSOISIndividual oldx) throws StopCriteriaException {
		double x[] = getNewVariables();
		boolean improved = false;
		for (int i = 0; i < x.length; i++) {
			x[i] = t.feasible(x[i] + v[i], i);
		}
		
		for (int i = 0; i < x.length; i++) {
			if(t.isFirstBetter(x[i], oldx.getNewVariables()[i])) {
				oldx.getNewVariables()[i] = t.feasible(x[i] + v[i], i);
				oldx.getV()[i] = v[i];
				improved = true;
			}
		}
		PSOISIndividual tmp;
		if(improved) {
			tmp = new PSOISIndividual(t.eval(oldx.getNewVariables()));
			if (t.isFirstBetter(tmp, Pbest)) {
				tmp.Pbest = tmp;
			} else
				tmp.Pbest = Pbest;
			tmp.v = v;
		}
		else {
		tmp = new PSOISIndividual(t.eval(x));
		if (t.isFirstBetter(tmp, Pbest)) {
			tmp.Pbest = tmp;
		} else
			tmp.Pbest = Pbest;
		tmp.v = v;
		}
		return tmp;

	}
}