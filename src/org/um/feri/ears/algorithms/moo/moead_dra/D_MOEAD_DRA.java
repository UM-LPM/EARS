package org.um.feri.ears.algorithms.moo.moead_dra;

import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.DifferentialEvolutionCrossover;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.operators.PolynomialMutation;
import org.um.feri.ears.problems.*;

public class D_MOEAD_DRA extends MOEAD_DRA<Double, DoubleProblem, Task<NumberSolution<Double>, DoubleProblem>> {
	
	public D_MOEAD_DRA() {
		this(new DifferentialEvolutionCrossover(), new PolynomialMutation(1.0 / 10, 20.0), 100);
	}
	
	public D_MOEAD_DRA(int populationSize) {
		this(new DifferentialEvolutionCrossover(), new PolynomialMutation(1.0 / 10, 20.0), populationSize);
	}

	public D_MOEAD_DRA(CrossoverOperator<DoubleProblem, NumberSolution<Double>> crossover, MutationOperator<DoubleProblem, NumberSolution<Double>> mutation, int populationSize) {
		super(crossover, mutation, populationSize);
	}

	@Override
	public void start() throws StopCriterionException {
		super.start();
		mut.setProbability(1.0 / task.problem.getNumberOfDimensions());
	}
}
