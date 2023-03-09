package org.um.feri.ears.problems.moo.unconstrained.cec2009;

import java.util.ArrayList;

import org.um.feri.ears.problems.moo.functions.UP5_1;
import org.um.feri.ears.problems.moo.functions.UP5_2;

public class UnconstrainedProblem5 extends CEC2009 {

	int N;
	double epsilon;
	
	/**
	 * Constructor. Creates a default instance of problem CEC2009_UnconstrainedProblem5 (30 decision variables)
	 */
	public UnconstrainedProblem5() {
		this(30, 10, 0.1); // 30 variables, N =10, epsilon = 0.1
	}

	/**
	 * Creates a new instance of problem CEC2009_UnconstrainedProblem5.
	 * @param numberOfVariables Number of variables.
	 */
	public UnconstrainedProblem5(Integer numberOfVariables, int N, double epsilon) {
		super("UF5", numberOfVariables,2, 0);

		referenceSetFileName = "UF5";

		this.N = N;
		this.epsilon = epsilon;

		upperLimit = new ArrayList<>(numberOfDimensions);
		lowerLimit = new ArrayList<>(numberOfDimensions);
		
		lowerLimit.add(0.0);
		upperLimit.add(1.0);

		for (int var = 1; var < numberOfVariables; var++) {
			lowerLimit.add(-1.0);
			upperLimit.add(1.0);
		}

		addObjective(new UP5_1(numberOfDimensions, N, epsilon));
		addObjective(new UP5_2(numberOfDimensions, N, epsilon));

	}
}
