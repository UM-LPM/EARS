package org.um.feri.ears.algorithms.moo.nsga3;

import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.operators.PMXCrossover;
import org.um.feri.ears.operators.PermutationSwapMutation;
import org.um.feri.ears.problems.CombinatorialProblem;
import org.um.feri.ears.problems.MOTask;

public class I_NSGAIII extends NSGAIII<Integer, CombinatorialProblem, MOTask<Integer>> {
	
	public I_NSGAIII() {
		this(new PMXCrossover(), new PermutationSwapMutation(0.2));
	}

	public I_NSGAIII(CrossoverOperator crossover, MutationOperator mutation) {
		super(crossover, mutation);
	}
	
}
