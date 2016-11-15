package org.um.feri.ears.algorithms.so.cro;

import org.um.feri.ears.problems.DoubleSolution;

public class CoralSolution extends DoubleSolution{
	
	/**
	 * The position of the coral on the coral reef
	 */
	Coordinate coralPosition;

	public CoralSolution(CoralSolution coralSolution) {
		super(coralSolution);
		this.coralPosition = coralSolution.coralPosition;
	}

	public CoralSolution(DoubleSolution solution) {
		super(solution);
	}

	public Coordinate getCoralPosition() {
		return coralPosition;
	}

	public void setCoralPosition(Coordinate coralPosition) {
		this.coralPosition = coralPosition;
	}
	
}
