package org.um.feri.ears.algorithms.moo.nsga3;

import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.operators.PolynomialMutation;
import org.um.feri.ears.operators.SBXCrossover;
import org.um.feri.ears.problems.*;

public class D_NSGAIII extends NSGAIII<Double, NumberProblem<Double>> {
	
	public D_NSGAIII() {
		this(new SBXCrossover(0.9, 20.0), new PolynomialMutation(1.0 / 10, 20.0));
	}
	
	public D_NSGAIII(CrossoverOperator<NumberProblem<Double>, NumberSolution<Double>> crossover, MutationOperator<NumberProblem<Double>, NumberSolution<Double>> mutation) {
		super(crossover, mutation);
	}

	@Override
	public void start() throws StopCriterionException {
		super.start();
		mut.setProbability(1.0 / task.problem.getNumberOfDimensions());
	}
}
