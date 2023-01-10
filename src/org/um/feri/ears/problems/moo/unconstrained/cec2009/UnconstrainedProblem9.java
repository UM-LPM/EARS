package org.um.feri.ears.problems.moo.unconstrained.cec2009;

import java.util.ArrayList;

import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.moo.functions.UP9_1;
import org.um.feri.ears.problems.moo.functions.UP9_2;
import org.um.feri.ears.problems.moo.functions.UP9_3;

public class UnconstrainedProblem9 extends CEC2009 {

	double epsilon;

	/**
	 * Constructor. Creates a default instance of problem CEC2009_UnconstrainedProblem9 (30 decision variables)
	 */
	public UnconstrainedProblem9() {
		this(30, 0.1); // 30 variables by default
	}

	/**
	 * Creates a new instance of problem CEC2009_UnconstrainedProblem9.
	 * @param numberOfVariables Number of variables.
	 */
	public UnconstrainedProblem9(Integer numberOfVariables, double epsilon) {
		super(numberOfVariables,0, 3);

		name = "UF9";
		fileName = "UF9";

		this.epsilon = epsilon;
		
		upperLimit = new ArrayList<Double>(numberOfDimensions);
		lowerLimit = new ArrayList<Double>(numberOfDimensions);
		
		lowerLimit.add(0.0);
		upperLimit.add(1.0);
		lowerLimit.add(0.0);
		upperLimit.add(1.0);

		for (int var = 2; var < numberOfVariables; var++) {
			lowerLimit.add(-2.0);
			upperLimit.add(2.0);
		}

		this.addObjective(new UP9_1(numberOfDimensions, epsilon));
		this.addObjective(new UP9_2(numberOfDimensions, epsilon));
		this.addObjective(new UP9_3(numberOfDimensions, epsilon));
	}

	@Override
	public void evaluateConstraints(NumberSolution<Double> solution) {
	}
}
