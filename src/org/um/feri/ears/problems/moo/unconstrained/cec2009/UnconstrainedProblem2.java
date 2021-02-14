package org.um.feri.ears.problems.moo.unconstrained.cec2009;

import java.util.ArrayList;

import org.um.feri.ears.problems.moo.MOSolutionBase;
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
		super(numberOfVariables,0, 2);

		name = "UF2";
		fileName = "UF2";
		
		upperLimit = new ArrayList<Double>(numberOfDimensions);
		lowerLimit = new ArrayList<Double>(numberOfDimensions);
		
		lowerLimit.add(0.0);
		upperLimit.add(1.0);

		for (int var = 1; var < numberOfVariables; var++) {
			lowerLimit.add(-1.0);
			upperLimit.add(1.0);
		}

		this.addObjective(new UP2_F5_1(numberOfDimensions));
		this.addObjective(new UP2_F5_2(numberOfDimensions));
	}
	
	@Override
	public void evaluateConstraints(MOSolutionBase<Double> solution) {
	
	}
}
