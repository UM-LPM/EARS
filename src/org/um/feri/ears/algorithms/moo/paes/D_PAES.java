package org.um.feri.ears.algorithms.moo.paes;

import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.operators.PolynomialMutation;
import org.um.feri.ears.problems.*;

public class D_PAES extends PAES<Double, DoubleProblem, Task<NumberSolution<Double>,DoubleProblem>> {
	
	public D_PAES() {
		this(new PolynomialMutation(1.0 / 10, 20.0), 100);
	}
	
	public D_PAES(int populationSize) {
		this(new PolynomialMutation(1.0 / 10, 20.0), populationSize);
	}

	public D_PAES(MutationOperator mutation, int populationSize) {
		super(mutation, populationSize);
	}

	@Override
	public void start() throws StopCriterionException {
		super.start();
		mut.setProbability(1.0 / task.problem.getNumberOfDimensions());
	}
	
}
