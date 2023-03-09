package org.um.feri.ears.problems.moo.unconstrained.cec2009;

import java.util.ArrayList;

import org.um.feri.ears.problems.moo.functions.UP2_F5_1;
import org.um.feri.ears.problems.moo.functions.UP2_F5_2;


public class UnconstrainedProblem2 extends CEC2009 {
	

	/**
	 * Constructor. Creates a default instance of problem CEC2009_UnconstrainedProblem2 (30 decision variables)
	 */
	public UnconstrainedProblem2() {
		this(30); // 30 variables by default
	}

	/**
	 * Creates a new instance of problem CEC2009_UnconstrainedProblem2.
	 * @param numberOfVariables Number of variables.
	 */
	public UnconstrainedProblem2(Integer numberOfVariables) {
		super("UF2", numberOfVariables,2, 0);

		referenceSetFileName = "UF2";
		
		upperLimit = new ArrayList<>(numberOfDimensions);
		lowerLimit = new ArrayList<>(numberOfDimensions);
		
		lowerLimit.add(0.0);
		upperLimit.add(1.0);

		for (int var = 1; var < numberOfVariables; var++) {
			lowerLimit.add(-1.0);
			upperLimit.add(1.0);
		}

		addObjective(new UP2_F5_1(numberOfDimensions));
		addObjective(new UP2_F5_2(numberOfDimensions));
	}
}
