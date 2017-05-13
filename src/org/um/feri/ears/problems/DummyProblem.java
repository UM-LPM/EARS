package org.um.feri.ears.problems;

import java.util.List;

public class DummyProblem extends Problem{

	
	public DummyProblem (String name){
		super(0,0);
		this.name = name;
	}

	@Override
	public double eval(Double[] ds) {
		return 0;
	}

}
