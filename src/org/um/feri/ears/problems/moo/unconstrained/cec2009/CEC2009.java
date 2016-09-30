package org.um.feri.ears.problems.moo.unconstrained.cec2009;

import org.um.feri.ears.problems.moo.DoubleMOProblem;

public abstract class CEC2009 extends DoubleMOProblem{

	public CEC2009(int numberOfDimensions, int numberOfConstraints, int numberOfObjectives) {
		super(numberOfDimensions, numberOfConstraints, numberOfObjectives);
		benchmarkName = "CEC2009";
	}

}
