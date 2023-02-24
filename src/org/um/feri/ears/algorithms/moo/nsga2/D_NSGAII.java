package org.um.feri.ears.algorithms.moo.nsga2;

import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.operators.PolynomialMutation;
import org.um.feri.ears.operators.SBXCrossover;
import org.um.feri.ears.problems.*;

public class D_NSGAII extends NSGAII<Double, DoubleProblem, Task<NumberSolution<Double>,DoubleProblem>> {
	
	public D_NSGAII() {
		this(new SBXCrossover(0.9, 20.0), new PolynomialMutation(1.0 / 10, 20.0), 100);
	}
	
	public D_NSGAII(int populationSize) {
		this(new SBXCrossover(0.9, 20.0), new PolynomialMutation(1.0 / 10, 20.0), populationSize);
	}

	public D_NSGAII(CrossoverOperator<DoubleProblem, NumberSolution<Double>> crossover, MutationOperator<DoubleProblem, NumberSolution<Double>> mutation, int populationSize) {
		super(crossover, mutation, populationSize);
	}

	@Override
	public void start() throws StopCriterionException {
		super.start();
		mut.setProbability(1.0 / task.problem.getNumberOfDimensions());
	}
	
}
