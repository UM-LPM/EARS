package org.um.feri.ears.algorithms.moo.nsga2;

import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.operators.PolynomialMutation;
import org.um.feri.ears.operators.SBXCrossover;
import org.um.feri.ears.problems.DoubleMOTask;
import org.um.feri.ears.problems.StopCriteriaException;

public class D_NSGAII extends NSGAII<DoubleMOTask, Double> {
	
	public D_NSGAII() {
		this(new SBXCrossover(0.9, 20.0), new PolynomialMutation(1.0 / 10, 20.0), 100);
	}
	
	public D_NSGAII(int populationSize) {
		this(new SBXCrossover(0.9, 20.0), new PolynomialMutation(1.0 / 10, 20.0), populationSize);
	}

	public D_NSGAII(CrossoverOperator crossover, MutationOperator mutation, int populationSize) {
		super(crossover, mutation, populationSize);
	}

	@Override
	public void start() throws StopCriteriaException {
		super.start();
		mut.setProbability(1.0 / num_var);
	}
	
}
