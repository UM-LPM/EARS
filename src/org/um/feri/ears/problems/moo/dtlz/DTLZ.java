package org.um.feri.ears.problems.moo.dtlz;


import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.util.Util;

public abstract class DTLZ extends DoubleProblem {

	public DTLZ(int numberOfDimensions, int numberOfConstraints, int numberOfObjectives) {
		super(numberOfDimensions, 1, numberOfConstraints, numberOfObjectives);
		benchmarkName = "DTLZ";
	}

	public void evaluate(NumberSolution<Double> solution) {
		double[] obj = evaluate(Util.toDoubleArray(solution.getVariables()));
		solution.setObjectives(obj);
	}

	public abstract double[] evaluate(double[] z);
}
