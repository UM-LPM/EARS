package org.um.feri.ears.problems.moo.unconstrained.cec2009;

import java.util.ArrayList;

import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.functions.UP7_1;
import org.um.feri.ears.problems.moo.functions.UP7_2;


public class UnconstrainedProblem7 extends CEC2009 {

	/**
	 * Constructor. Creates a default instance of problem CEC2009_UnconstrainedProblem7 (30 decision variables)
	 */
	public UnconstrainedProblem7() {
		this(30); // 30 variables by default
	}

	/**
	 * Creates a new instance of problem CEC2009_UnconstrainedProblem7.
	 * @param numberOfVariables Number of variables.
	 */
	public UnconstrainedProblem7(Integer numberOfVariables) {
		super(numberOfVariables,0, 2);

		name = "UF7";
		fileName = "UF7";
		
		upperLimit = new ArrayList<Double>(numberOfDimensions);
		lowerLimit = new ArrayList<Double>(numberOfDimensions);
		
		lowerLimit.add(0.0);
		upperLimit.add(1.0);

		for (int var = 1; var < numberOfVariables; var++) {
			lowerLimit.add(-1.0);
			upperLimit.add(1.0);
		}

		this.addObjective(new UP7_1(numberOfDimensions));
		this.addObjective(new UP7_2(numberOfDimensions));

	}

	@Override
	public void evaluateConstraints(MOSolutionBase<Double> solution) {
		
	}
}
