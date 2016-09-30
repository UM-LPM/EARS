package org.um.feri.ears.problems.moo.dtlz;


import org.um.feri.ears.problems.moo.DoubleMOProblem;

public abstract class DTLZ extends DoubleMOProblem {

	public DTLZ(int numberOfDimensions, int numberOfConstraints, int numberOfObjectives) {
		super(numberOfDimensions, numberOfConstraints, numberOfObjectives);
		benchmarkName = "DTLZ";
	}
}
