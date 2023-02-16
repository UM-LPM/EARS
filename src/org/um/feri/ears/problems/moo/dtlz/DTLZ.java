package org.um.feri.ears.problems.moo.dtlz;


import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.util.Util;

public abstract class DTLZ extends DoubleProblem {

	public DTLZ(String name, int numberOfDimensions, int numberOfObjectives, int numberOfConstraints) {
		super(name, numberOfDimensions, 1, numberOfObjectives, numberOfConstraints);
		benchmarkName = "DTLZ";
	}

	public void evaluate(NumberSolution<Double> solution) {
		double[] obj = evaluate(Util.toDoubleArray(solution.getVariables()));
		solution.setObjectives(obj);
	}

	public abstract double[] evaluate(double[] z);
}
