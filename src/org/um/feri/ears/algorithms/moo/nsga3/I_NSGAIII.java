package org.um.feri.ears.algorithms.moo.nsga3;

import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.operators.PMXCrossover;
import org.um.feri.ears.operators.PermutationSwapMutation;
import org.um.feri.ears.problems.CombinatorialProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.Task;

public class I_NSGAIII extends NSGAIII<Integer, CombinatorialProblem, Task<NumberSolution<Integer>,CombinatorialProblem>> {
	
	public I_NSGAIII() {
		this(new PMXCrossover(), new PermutationSwapMutation(0.2));
	}

	public I_NSGAIII(CrossoverOperator<CombinatorialProblem, NumberSolution<Integer>> crossover, MutationOperator<CombinatorialProblem, NumberSolution<Integer>> mutation) {
		super(crossover, mutation);
	}
	
}
