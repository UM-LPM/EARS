package org.um.feri.ears.algorithms.moo.nsga3;

import org.um.feri.ears.algorithms.moo.nsga2.NSGAII;
import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.operators.PolynomialMutation;
import org.um.feri.ears.operators.SBXCrossover;
import org.um.feri.ears.problems.DoubleMOTask;
import org.um.feri.ears.problems.StopCriteriaException;

public class D_NSGAIII extends NSGAIII<DoubleMOTask, Double> {
	
	public D_NSGAIII() {
		this(new SBXCrossover(0.9, 20.0), new PolynomialMutation(1.0 / 10, 20.0));
	}
	
	public D_NSGAIII(CrossoverOperator crossover, MutationOperator mutation) {
		super(crossover, mutation);
	}

	@Override
	public void start() throws StopCriteriaException {
		super.start();
		mut.setProbability(1.0 / num_var);
	}
}
