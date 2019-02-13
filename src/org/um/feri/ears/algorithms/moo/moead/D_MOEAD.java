package org.um.feri.ears.algorithms.moo.moead;

import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.DifferentialEvolutionCrossover;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.operators.PolynomialMutation;
import org.um.feri.ears.problems.DoubleMOTask;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.moo.MOSolutionBase;

public class D_MOEAD extends MOEAD<DoubleMOTask, Double> {
	
	public D_MOEAD() {
		this(new DifferentialEvolutionCrossover(), new PolynomialMutation(1.0 / 10, 20.0), 100);
	}
	
	public D_MOEAD(int populationSize) {
		this(new DifferentialEvolutionCrossover(), new PolynomialMutation(1.0 / 10, 20.0), populationSize);
	}

	public D_MOEAD(CrossoverOperator<Double, DoubleMOTask, MOSolutionBase<Double>> crossover, MutationOperator<Double, DoubleMOTask, MOSolutionBase<Double>> mutation, int populationSize) {
		super(crossover, mutation, populationSize);
	}

	@Override
	public void start() throws StopCriteriaException {
		super.start();
		mut.setProbability(1.0 / num_var);
	}
	
}