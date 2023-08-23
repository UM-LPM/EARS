package org.um.feri.ears.problems;

import java.util.List;

public class DummyProblem extends DoubleProblem {


	public DummyProblem (String name){
		this(name, false);
	}

	public DummyProblem (String name, boolean objectiveMaximizationFlag){
		super(name, 0, 1, 1, 0);
		objectiveMaximizationFlags[0] = objectiveMaximizationFlag;
	}

	@Override
	public double eval(double[] ds) {
		return 0;
	}

}
