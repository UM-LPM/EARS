package org.um.feri.ears.algorithms.moo.paes;

import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.operators.PermutationSwapMutation;
import org.um.feri.ears.problems.IntegerMOTask;
import org.um.feri.ears.problems.IntegerProblem;

public class I_PAES extends PAES<Integer, IntegerProblem, IntegerMOTask> {
	
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