package org.um.feri.ears.problems;

import java.util.List;

public class DummyProblem extends DoubleProblem {


	public DummyProblem (String name){
		this(name, true);
	}

	public DummyProblem (String name, boolean minimize){
		super(name, 0, 1, 1, 0);
		this.minimize = minimize;
	}

	@Override
	public double eval(double[] ds) {
		return 0;
	}

}
