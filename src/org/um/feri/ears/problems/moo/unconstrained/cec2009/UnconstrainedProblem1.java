package org.um.feri.ears.problems.moo.unconstrained.cec2009;

import java.util.ArrayList;

import org.um.feri.ears.problems.moo.functions.UP1_F2_1;
import org.um.feri.ears.problems.moo.functions.UP1_F2_2;


public class UnconstrainedProblem1 extends CEC2009 {
    

	public UnconstrainedProblem1() {
		this(30);
	}
	 
	public UnconstrainedProblem1(Integer numberOfVariables) {
		
		super("UF1", numberOfVariables, 2, 0);

		referenceSetFileName = "UF1";

		upperLimit = new ArrayList<>(numberOfDimensions);
		lowerLimit = new ArrayList<>(numberOfDimensions);


		lowerLimit.add(0.0);
		upperLimit.add(1.0);
		for (int i = 1; i < numberOfDimensions; i++) {
			lowerLimit.add(-1.0);
			upperLimit.add(1.0);
		}

		addObjective(new UP1_F2_1(numberOfDimensions));
		addObjective(new UP1_F2_2(numberOfDimensions));
	}
}