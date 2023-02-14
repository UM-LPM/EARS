package org.um.feri.ears.algorithms.moo.moead_dra;

import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.operators.PMXCrossover;
import org.um.feri.ears.operators.PermutationSwapMutation;
import org.um.feri.ears.problems.IntegerMOTask;
import org.um.feri.ears.problems.IntegerProblem;

public class I_MOEAD_STM extends MOEAD_DRA<Integer, IntegerProblem, IntegerMOTask> {
	
	public I_MOEAD_STM() {
		this(new PMXCrossover(), new PermutationSwapMutation(0.2), 100);
	}
	
	public I_MOEAD_STM(int populationSize) {
		this(new PMXCrossover(), new PermutationSwapMutation(0.2), populationSize);
	}

	public I_MOEAD_STM(CrossoverOperator crossover, MutationOperator mutation, int populationSize) {
		super(crossover, mutation, populationSize);
	}
	
}