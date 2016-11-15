package org.um.feri.ears.experiment.so.pso;

import java.util.Arrays;

import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

public class PSOMSolution extends DoubleSolution {
	PSOMSolution Pbest;
	double v[];

	public PSOMSolution getPbest() {
		return Pbest;
	}

	public void setPbest(PSOMSolution Pbest) {
		this.Pbest = Pbest;
	}

	public double[] getV() {
		return v;
	}

	public PSOMSolution(Task t) throws StopCriteriaException {
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

	public PSOMSolution(DoubleSolution eval) {
		super(eval);

	}

	@Override
	public String toString() {
		return super.toString() + " v:" + (Arrays.toString(v) + " p:" + Pbest.getEval());
	}

	public PSOMSolution update(Task t, double v[]) throws StopCriteriaException {
		double x[] = getNewVariables();
		for (int i = 0; i < x.length; i++) {
			x[i] = t.setFeasible(x[i] + v[i], i);
		}
		PSOMSolution tmp = new PSOMSolution(t.eval(x));
		if (t.isFirstBetter(tmp, Pbest)) {
			tmp.Pbest = tmp;
		} else
			tmp.Pbest = Pbest;
		tmp.v = v;
		return tmp;

	}
}