package org.um.feri.ears.problems;

public class DummySolution extends DoubleSolution {
	
	private double value;
	
	public DummySolution(double value) {
		this.value = value;
	}

	@Override
	public double getEval() {
		return value;
	}

}
