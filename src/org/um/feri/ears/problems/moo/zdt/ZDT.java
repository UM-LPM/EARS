package org.um.feri.ears.problems.moo.zdt;

import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.util.Util;

public abstract class ZDT extends DoubleProblem {

	public ZDT(int numberOfDimensions, int numberOfConstraints, int numberOfObjectives) {
		super(numberOfDimensions, 1, numberOfConstraints, numberOfObjectives);
		benchmarkName = "ZDT";
	}

	@Override
	public void evaluate(NumberSolution<Double> solution) {

		double[] x = Util.toDoubleArray(solution.getVariables());

		double[] obj = new double[objectives.size()];
		for (int i = 0; i < obj.length; i++) {
			obj[i] = objectives.get(i).eval(x);
		}
		solution.setObjectives(obj);
	}
}
