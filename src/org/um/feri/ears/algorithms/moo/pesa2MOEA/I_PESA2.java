package org.um.feri.ears.algorithms.moo.pesa2MOEA;

import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.operators.PMXCrossover;
import org.um.feri.ears.operators.PermutationSwapMutation;
import org.um.feri.ears.problems.CombinatorialProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.Task;

public class I_PESA2 extends PESA2<Integer, CombinatorialProblem, Task<NumberSolution<Integer>, CombinatorialProblem>> {
	
	public I_PESA2() {
		this(new PMXCrossover(), new PermutationSwapMutation(0.2), 100);
	}
	
	public I_PESA2(int populationSize) {
		this(new PMXCrossover(), new PermutationSwapMutation(0.2), populationSize);
	}

	public I_PESA2(CrossoverOperator<CombinatorialProblem, NumberSolution<Integer>> crossover, MutationOperator<CombinatorialProblem, NumberSolution<Integer>> mutation, int populationSize) {
		super(crossover, mutation, populationSize);
	}
	
}