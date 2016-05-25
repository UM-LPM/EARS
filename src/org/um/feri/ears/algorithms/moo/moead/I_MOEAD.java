package org.um.feri.ears.algorithms.moo.moead;

import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.operators.PMXCrossover;
import org.um.feri.ears.operators.PermutationSwapMutation;
import org.um.feri.ears.problems.IntegerMOTask;

public class I_MOEAD extends MOEAD<IntegerMOTask, Integer> {
	
	public I_MOEAD() {
		this(new PMXCrossover(), new PermutationSwapMutation(0.2), 100);
	}
	
	public I_MOEAD(int populationSize) {
		this(new PMXCrossover(), new PermutationSwapMutation(0.2), populationSize);
	}


	public I_MOEAD(CrossoverOperator crossover, MutationOperator mutation, int populationSize) {
		super(crossover, mutation, populationSize);
	}
	
}
