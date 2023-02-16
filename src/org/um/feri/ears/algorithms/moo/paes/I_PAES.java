package org.um.feri.ears.algorithms.moo.paes;

import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.operators.PermutationSwapMutation;
import org.um.feri.ears.problems.CombinatorialProblem;
import org.um.feri.ears.problems.MOTask;

public class I_PAES extends PAES<Integer, CombinatorialProblem, MOTask<Integer>> {
	
	public I_PAES() {
		this(new PermutationSwapMutation(0.2), 100);
	}
	
	public I_PAES(int populationSize) {
		this(new PermutationSwapMutation(0.2), populationSize);
	}

	public I_PAES(MutationOperator mutation, int populationSize) {
		super(mutation, populationSize);
	}
	
}