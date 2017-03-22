package org.um.feri.ears.experiment.so.pso;

import java.util.Arrays;

import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

public class PSOIWDSolution extends DoubleSolution {
	PSOIWDSolution Pbest;
	double v[];

	public PSOIWDSolution getPbest() {
		return Pbest;
	}

	public void setPbest(PSOIWDSolution Pbest) {
		this.Pbest = Pbest;
	}

	public double[] getV() {
		return v;
	}

	public PSOIWDSolution(Task t) throws StopCriteriaException {
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

	public PSOIWDSolution(DoubleSolution eval) {
		super(eval);

	}

	@Override
	public String toString() {
		return super.toString() + " v:" + (Arrays.toString(v) + " p:" + Pbest.getEval());
	}

	public PSOIWDSolution update(Task t, double v[]) throws StopCriteriaException {
		double x[] = getDoubleVariables();
		for (int i = 0; i < x.length; i++) {
			x[i] = t.setFeasible(x[i] + v[i], i);
		}
		PSOIWDSolution tmp = new PSOIWDSolution(t.eval(x));
		if (t.isFirstBetter(tmp, Pbest)) {
			tmp.Pbest = tmp;
		} else
			tmp.Pbest = Pbest;
		tmp.v = v;
		return tmp;

	}
}