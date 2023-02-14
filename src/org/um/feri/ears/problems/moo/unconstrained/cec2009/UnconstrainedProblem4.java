package org.um.feri.ears.problems.moo.unconstrained.cec2009;

import java.util.ArrayList;

import org.um.feri.ears.problems.moo.functions.UP4_1;
import org.um.feri.ears.problems.moo.functions.UP4_2;

public class UnconstrainedProblem4 extends CEC2009 {
	
	/**
	 * Constructor. Creates a default instance of problem CEC2009_UnconstrainedProblem4 (30 decision variables)
	 */
	public UnconstrainedProblem4() {
		this(30); // 30 variables by default
	}

	/**
	 * Creates a new instance of problem CEC2009_UnconstrainedProblem4.
	 * @param numberOfVariables Number of variables.
	 */
	public UnconstrainedProblem4(Integer numberOfVariables) {
		super("UF4", numberOfVariables,0, 2);

		referenceSetFileName = "UF4";

		upperLimit = new ArrayList<>(numberOfDimensions);
		lowerLimit = new ArrayList<>(numberOfDimensions);
		
		lowerLimit.add(0.0);
		upperLimit.add(1.0);

		for (int var = 1; var < numberOfVariables; var++) {
			lowerLimit.add(-2.0);
			upperLimit.add(2.0);
		}

		addObjective(new UP4_1(numberOfDimensions));
		addObjective(new UP4_2(numberOfDimensions));

	}
}
