package org.um.feri.ears.algorithms.moo.paes;

import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.operators.PermutationSwapMutation;
import org.um.feri.ears.problems.CombinatorialProblem;
import org.um.feri.ears.problems.NumberProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.Task;

public class I_PAES extends PAES<Integer, NumberProblem<Integer>> {
	
	public I_PAES() {
		this(new PermutationSwapMutation(0.2), 100);
	}
	
	public I_PAES(int populationSize) {
		this(new PermutationSwapMutation(0.2), populationSize);
	}

	public I_PAES(MutationOperator<NumberProblem<Integer>, NumberSolution<Integer>> mutation, int populationSize) {
		super(mutation, populationSize);
	}
	
}