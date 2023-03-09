package org.um.feri.ears.algorithms.moo.nsga3;

import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.operators.PMXCrossover;
import org.um.feri.ears.operators.PermutationSwapMutation;
import org.um.feri.ears.problems.CombinatorialProblem;
import org.um.feri.ears.problems.NumberProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.Task;

public class I_NSGAIII extends NSGAIII<Integer, NumberProblem<Integer>> {
	
	public I_NSGAIII() {
		this(new PMXCrossover(), new PermutationSwapMutation(0.2));
	}

	public I_NSGAIII(CrossoverOperator<NumberProblem<Integer>, NumberSolution<Integer>> crossover, MutationOperator<NumberProblem<Integer>, NumberSolution<Integer>> mutation) {
		super(crossover, mutation);
	}
	
}
