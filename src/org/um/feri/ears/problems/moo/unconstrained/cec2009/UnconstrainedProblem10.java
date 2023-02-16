package org.um.feri.ears.problems.moo.unconstrained.cec2009;

import java.util.ArrayList;

import org.um.feri.ears.problems.moo.functions.UP10_1;
import org.um.feri.ears.problems.moo.functions.UP10_2;
import org.um.feri.ears.problems.moo.functions.UP10_3;

public class UnconstrainedProblem10 extends CEC2009 {

	
	/**
	 * Constructor. Creates a default instance of problem CEC2009_UnconstrainedProblem10 (30 decision variables)
	 */
	public UnconstrainedProblem10() {
		this(30); // 30 variables by default
	}

	 /**
	  * Creates a new instance of problem CEC2009_UnconstrainedProblem10.
	  * @param numberOfVariables Number of variables.
	  */
	public UnconstrainedProblem10(Integer numberOfVariables) {
		
		super("UF10", numberOfVariables,3, 0);

		referenceSetFileName = "UF10";

		upperLimit = new ArrayList<>(numberOfDimensions);
		lowerLimit = new ArrayList<>(numberOfDimensions);
		
		lowerLimit.add(0.0);
		upperLimit.add(1.0);
		lowerLimit.add(0.0);
		upperLimit.add(1.0);

		for (int var = 2; var < numberOfVariables; var++) {
			lowerLimit.add(-2.0);
			upperLimit.add(2.0);
		}

		addObjective(new UP10_1(numberOfDimensions));
		addObjective(new UP10_2(numberOfDimensions));
		addObjective(new UP10_3(numberOfDimensions));
	}
}
