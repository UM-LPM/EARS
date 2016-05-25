package org.um.feri.ears.problems.moo.unconstrained.cec2009;

import java.util.ArrayList;

import org.um.feri.ears.problems.moo.DoubleMOProblem;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.functions.UP5_1;
import org.um.feri.ears.problems.moo.functions.UP5_2;

public class UnconstrainedProblem5 extends DoubleMOProblem {

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

		super(numberOfVariables,0, 2);

		name = "UF5";
		file_name = "UF5";

		this.N = N;
		this.epsilon = epsilon;

		upperLimit = new ArrayList<Double>(numberOfDimensions);
		lowerLimit = new ArrayList<Double>(numberOfDimensions);
		
		lowerLimit.add(0.0);
		upperLimit.add(1.0);

		for (int var = 1; var < numberOfVariables; var++) {
			lowerLimit.add(-1.0);
			upperLimit.add(1.0);
		}

		this.addObjective(new UP5_1(numberOfDimensions, N, epsilon));
		this.addObjective(new UP5_2(numberOfDimensions, N, epsilon));

	}
	
	@Override
	public void evaluateConstraints(MOSolutionBase<Double> solution) {
		// TODO Auto-generated method stub
		
	}
}
