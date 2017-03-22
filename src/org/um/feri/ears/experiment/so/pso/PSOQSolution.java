package org.um.feri.ears.experiment.so.pso;

import java.util.Arrays;

import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

public class PSOQSolution extends DoubleSolution {
	PSOQSolution Pbest;
	double v[];

	public PSOQSolution getPbest() {
		return Pbest;
	}

	public void setPbest(PSOQSolution Pbest) {
		this.Pbest = Pbest;
	}

	public double[] getV() {
		return v;
	}

	public PSOQSolution(Task t) throws StopCriteriaException {
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

	public PSOQSolution(DoubleSolution eval) {
		super(eval);

	}

	@Override
	public String toString() {
		return super.toString() + " v:" + (Arrays.toString(v) + " p:" + Pbest.getEval());
	}

	public PSOQSolution update(Task t, double v[]) throws StopCriteriaException {
		double x[] = getDoubleVariables();
		for (int i = 0; i < x.length; i++) {
			x[i] = t.setFeasible(x[i] + v[i], i);
		}
		PSOQSolution tmp = new PSOQSolution(t.eval(x));
		if (t.isFirstBetter(tmp, Pbest)) {
			tmp.Pbest = tmp;
		} else
			tmp.Pbest = Pbest;
		tmp.v = v;
		return tmp;
	}

	public PSOQSolution updateP(Task taskProblem, double[] v2, double[] mBest, double w) {
		double x[] = getDoubleVariables();
		for (int i = 0; i < x.length; i++) {
			if(Util.rnd.nextDouble() >= 0.5) {
				double newX = v2[i] + w* Math.abs(x[i] - mBest[i]*Math.log(1/Util.rnd.nextDouble()));
				x[i] = taskProblem.setFeasible(newX, i);
			}
			else {
				double newX = v2[i] - w* Math.abs(x[i] - mBest[i]*Math.log(1/Util.rnd.nextDouble()));
				x[i] = taskProblem.setFeasible(newX, i);
			}
		}
		PSOQSolution tmp = null;
		try {
			tmp = new PSOQSolution(taskProblem.eval(x));
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
