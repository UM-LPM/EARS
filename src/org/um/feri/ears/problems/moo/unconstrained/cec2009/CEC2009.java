package org.um.feri.ears.problems.moo.unconstrained.cec2009;

import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.util.Util;

public abstract class CEC2009 extends DoubleProblem {

	public CEC2009(String name, int numberOfDimensions, int numberOfConstraints, int numberOfObjectives) {
		super(name, numberOfDimensions, 1, numberOfConstraints, numberOfObjectives);
		benchmarkName = "CEC2009";
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
