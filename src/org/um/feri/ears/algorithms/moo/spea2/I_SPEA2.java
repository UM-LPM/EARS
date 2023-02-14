package org.um.feri.ears.algorithms.moo.spea2;

import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.operators.PMXCrossover;
import org.um.feri.ears.operators.PermutationSwapMutation;
import org.um.feri.ears.problems.IntegerMOTask;
import org.um.feri.ears.problems.IntegerProblem;

public class I_SPEA2 extends SPEA2<Integer, IntegerProblem, IntegerMOTask> {
	
	public I_SPEA2() {
		this(new PMXCrossover(), new PermutationSwapMutation(0.2), 100, 100);
		//this(new PMXCrossover(0.74), new PermutationSwapMutation(0.87), 190, 190);
		//SPEA2_populationSize(190.0)crossoverProbability(0.7432950241136862)mutationProbability(0.8702682108514614)
		
		//this(new PMXCrossover(0.5266200868145975), new PermutationSwapMutation(0.8673844622829247), 199, 100);
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
	
	public I_SPEA2(double crossoverProbability, double mutationProbability, int populationSize, int archiveSize) {
		super(new PMXCrossover(crossoverProbability), new PermutationSwapMutation(mutationProbability), populationSize, archiveSize);
	}
	
}