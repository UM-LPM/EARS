package org.um.feri.ears.problems.moo.unconstrained.cec2009;

import java.util.ArrayList;
import java.util.Collections;

import org.um.feri.ears.problems.moo.DoubleMOProblem;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.functions.UP1_F2_1;
import org.um.feri.ears.problems.moo.functions.UP1_F2_2;


public class UnconstrainedProblem1 extends CEC2009 {
    

	public UnconstrainedProblem1() {
		this(30); // 30 variables by default
	}
	 
	public UnconstrainedProblem1(Integer numberOfVariables) {
		
		super(numberOfVariables, 0, 2);

		file_name = "UF1";
		name = "UF1";
		
		upperLimit = new ArrayList<Double>(numberOfDimensions);
		lowerLimit = new ArrayList<Double>(numberOfDimensions);


		lowerLimit.add(0.0);
		upperLimit.add(1.0);
		for (int i = 1; i < numberOfDimensions; i++) {
			lowerLimit.add(-1.0);
			upperLimit.add(1.0);
		}

		this.addObjective(new UP1_F2_1(numberOfDimensions));
		this.addObjective(new UP1_F2_2(numberOfDimensions));
	}

	@Override
	public void evaluateConstraints(MOSolutionBase<Double> solution) {
	}
}