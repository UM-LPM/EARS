package org.um.feri.ears.algorithms.moo.pesa2;

import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.operators.PMXCrossover;
import org.um.feri.ears.operators.PermutationSwapMutation;
import org.um.feri.ears.problems.IntegerMOTask;

public class I_PESAII extends PESAII<IntegerMOTask, Integer> {
	
	public I_PESAII() {
		this(new PMXCrossover(), new PermutationSwapMutation(0.2), 100, 100);
	}
	
	public I_PESAII(int populationSize) {
		this(new PMXCrossover(), new PermutationSwapMutation(0.2), populationSize, populationSize);
	}
	
	public I_PESAII(int populationSize, int archiveSize) {
		this(new PMXCrossover(), new PermutationSwapMutation(0.2), populationSize, archiveSize);
	}

	public I_PESAII(CrossoverOperator crossover, MutationOperator mutation, int populationSize, int archiveSize) {
		super(crossover, mutation, populationSize, archiveSize);
	}
	
}