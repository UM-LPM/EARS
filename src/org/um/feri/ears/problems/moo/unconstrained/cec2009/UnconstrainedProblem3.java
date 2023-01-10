package org.um.feri.ears.problems.moo.unconstrained.cec2009;

import java.util.ArrayList;

import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.moo.functions.UP3_1;
import org.um.feri.ears.problems.moo.functions.UP3_2;


public class UnconstrainedProblem3 extends CEC2009 {

	/**
	 * Constructor. Creates a default instance of problem CEC2009_UnconstrainedProblem3 (30 decision variables)
	 */
	public UnconstrainedProblem3() {
		this(30); // 30 variables by default
	}

	/**
	 * Creates a new instance of problem CEC2009_UnconstrainedProblem3.
	 * @param numberOfVariables Number of variables.
	 */
	public UnconstrainedProblem3(Integer numberOfVariables) {
		super(numberOfVariables,0, 2);

		name = "UF3";
		fileName = "UF3";

		upperLimit = new ArrayList<Double>(numberOfDimensions);
		lowerLimit = new ArrayList<Double>(numberOfDimensions);

		for (int var = 0; var < numberOfVariables; var++) {
			lowerLimit.add(0.0);
			upperLimit.add(1.0);
		}

		this.addObjective(new UP3_1(numberOfDimensions));
		this.addObjective(new UP3_2(numberOfDimensions));

	}

	@Override
	public void evaluateConstraints(NumberSolution<Double> solution) {
		
	}
}
