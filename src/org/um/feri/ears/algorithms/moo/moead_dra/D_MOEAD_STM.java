package org.um.feri.ears.algorithms.moo.moead_dra;

import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.DifferentialEvolutionCrossover;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.operators.PolynomialMutation;
import org.um.feri.ears.problems.DoubleMOTask;
import org.um.feri.ears.problems.StopCriterionException;

public class D_MOEAD_STM extends MOEAD_STM<DoubleMOTask, Double> {
	
	public D_MOEAD_STM() {
		this(new DifferentialEvolutionCrossover(), new PolynomialMutation(1.0 / 10, 20.0), 100);
	}
	
	public D_MOEAD_STM(int populationSize) {
		this(new DifferentialEvolutionCrossover(), new PolynomialMutation(1.0 / 10, 20.0), populationSize);
	}

	public D_MOEAD_STM(CrossoverOperator crossover, MutationOperator mutation, int populationSize) {
		super(crossover, mutation, populationSize);
	}

	@Override
	public void start() throws StopCriterionException {
		super.start();
		mut.setProbability(1.0 / numVar);
	}
	
}
