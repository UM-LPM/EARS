package org.um.feri.ears.problems.moo.zdt;

import org.um.feri.ears.problems.moo.DoubleMOProblem;

public abstract class ZDT extends DoubleMOProblem{

	public ZDT(int numberOfDimensions, int numberOfConstraints, int numberOfObjectives) {
		super(numberOfDimensions, numberOfConstraints, numberOfObjectives);
		benchmarkName = "ZDT";
	}

}
