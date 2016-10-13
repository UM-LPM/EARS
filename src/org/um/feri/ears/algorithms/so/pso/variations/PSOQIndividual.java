package org.um.feri.ears.algorithms.so.pso.variations;

import java.util.Arrays;

import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

public class PSOQIndividual extends DoubleSolution {
	PSOQIndividual Pbest;
	double v[];

	public PSOQIndividual getPbest() {
		return Pbest;
	}

	public void setPbest(PSOQIndividual Pbest) {
		this.Pbest = Pbest;
	}

	public double[] getV() {
		return v;
	}

	public PSOQIndividual(Task t) throws StopCriteriaException {
		super(t.getRandomSolution());
		v = new double[t.getDimensions()];
		double l;
		double r;
		for (int i = 0; i < t.getDimensions(); i++) {
			l = -Math.abs(t.getUpperLimit()[i] - t.getLowerLimit()[i]) / 4;
			r = Math.abs(t.getUpperLimit()[i] - t.getLowerLimit()[i]) / 4;
			v[i] = Util.nextDouble(l,r);
		}
		Pbest = this;
	}

	public PSOQIndividual(DoubleSolution eval) {
		super(eval);

	}

	@Override
	public String toString() {
		return super.toString() + " v:" + (Arrays.toString(v) + " p:" + Pbest.getEval());
	}

	public PSOQIndividual update(Task t, double v[]) throws StopCriteriaException {
		double x[] = getNewVariables();
		for (int i = 0; i < x.length; i++) {
			x[i] = t.feasible(x[i] + v[i], i);
		}
		PSOQIndividual tmp = new PSOQIndividual(t.eval(x));
		if (t.isFirstBetter(tmp, Pbest)) {
			tmp.Pbest = tmp;
		} else
			tmp.Pbest = Pbest;
		tmp.v = v;
		return tmp;
	}

	public PSOQIndividual updateP(Task taskProblem, double[] v2, double[] mBest, double w) {
		double x[] = getNewVariables();
		for (int i = 0; i < x.length; i++) {
			if(Util.rnd.nextDouble() >= 0.5) {
				double newX = v2[i] + w* Math.abs(x[i] - mBest[i]*Math.log(1/Util.rnd.nextDouble()));
				x[i] = taskProblem.feasible(newX, i);
			}
			else {
				double newX = v2[i] - w* Math.abs(x[i] - mBest[i]*Math.log(1/Util.rnd.nextDouble()));
				x[i] = taskProblem.feasible(newX, i);
			}
		}
		PSOQIndividual tmp = null;
		try {
			tmp = new PSOQIndividual(taskProblem.eval(x));
		} catch (StopCriteriaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (taskProblem.isFirstBetter(tmp, Pbest)) {
			tmp.Pbest = tmp;
		} else
			tmp.Pbest = Pbest;
		tmp.v = v2;
		return tmp;
	}
}
