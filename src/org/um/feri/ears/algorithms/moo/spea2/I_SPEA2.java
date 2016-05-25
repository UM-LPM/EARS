package org.um.feri.ears.algorithms.moo.spea2;

import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.operators.PMXCrossover;
import org.um.feri.ears.operators.PermutationSwapMutation;
import org.um.feri.ears.problems.IntegerMOTask;

public class I_SPEA2 extends SPEA2<IntegerMOTask, Integer> {
	
	public I_SPEA2() {
		this(new PMXCrossover(), new PermutationSwapMutation(0.2), 100, 100);
	}
	
	public I_SPEA2(int populationSize) {
		this(new PMXCrossover(), new PermutationSwapMutation(0.2), populationSize, populationSize);
	}
	
	public I_SPEA2(int populationSize, int archiveSize) {
		this(new PMXCrossover(), new PermutationSwapMutation(0.2), populationSize, archiveSize);
	}

	public I_SPEA2(CrossoverOperator crossover, MutationOperator mutation, int populationSize, int archiveSize) {
		super(crossover, mutation, populationSize, archiveSize);
	}
	
}