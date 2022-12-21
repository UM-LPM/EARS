package org.um.feri.ears.experiment.ee;

import java.util.Arrays;

import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

public class PSOoriginalSolution extends NumberSolution<Double> {
	PSOoriginalSolution Pbest;
	double[] v;

	public PSOoriginalSolution getPbest() {
		return Pbest;
	}

	public void setPbest(PSOoriginalSolution Pbest) {
		this.Pbest = Pbest;
	}

	public double[] getV() {
		return v;
	}

	public PSOoriginalSolution(Task t) throws StopCriterionException {
		super(t.getRandomEvaluatedSolution());
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

	public PSOoriginalSolution(NumberSolution<Double> eval) {
		super(eval);

	}

	@Override
	public String toString() {
		return super.toString() + " v:" + (Arrays.toString(v) + " p:" + Pbest.getEval());
	}

	public PSOoriginalSolution update(Task t, double[] v) throws StopCriterionException {
		double[] x = Util.toDoubleArray(getVariables());
		for (int i = 0; i < x.length; i++) {
			x[i] = t.setFeasible(x[i] + v[i], i);
		}
		PSOoriginalSolution tmp = new PSOoriginalSolution(t.eval(x));
		if (t.isFirstBetter(tmp, Pbest)) {
			tmp.Pbest = tmp;
		} else
			tmp.Pbest = Pbest;
		tmp.v = v;
		return tmp;

	}
	
	public PSOoriginalSolution updateP(Task t, double sigma) throws StopCriterionException {
		double[] x = Util.toDoubleArray(getVariables());
		for (int i = 0; i < x.length; i++) {
			x[i] = t.setFeasible(x[i] + sigma * Util.rnd.nextDouble(), i);
		}
		
		PSOoriginalSolution tmp = new PSOoriginalSolution(t.eval(x));
		if (t.isFirstBetter(tmp, Pbest)) {
			tmp.Pbest = tmp;
		} else
			tmp.Pbest = Pbest;
		tmp.v = v;
		return tmp;

	}
	
	public PSOoriginalSolution updatePa(Task t, double sigma) throws StopCriterionException {
		double[] x = Util.toDoubleArray(getVariables());
		for (int i = 0; i < x.length; i++) {
			x[i] = t.setFeasible(x[i] + sigma * Util.rnd.nextDouble(), i);
		}
		
		PSOoriginalSolution tmp = new PSOoriginalSolution(t.eval(x));
		if (t.isFirstBetter(tmp, Pbest)) {
			tmp.Pbest = tmp;
		} else
			tmp.Pbest = Pbest;
		tmp.v = v;
		return tmp;

	}
}
