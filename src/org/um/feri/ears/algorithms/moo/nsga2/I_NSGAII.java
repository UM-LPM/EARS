package org.um.feri.ears.algorithms.moo.nsga2;

import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.operators.PMXCrossover;
import org.um.feri.ears.operators.PermutationSwapMutation;
import org.um.feri.ears.problems.IntegerMOTask;
import org.um.feri.ears.problems.IntegerProblem;

public class I_NSGAII extends NSGAII<Integer, IntegerProblem, IntegerMOTask> {
	
	public I_NSGAII() {
		//this(new PMXCrossover(1.0), new PermutationSwapMutation(0.86), 108);
		this(new PMXCrossover(), new PermutationSwapMutation(0.2), 100);
	}
	
	public I_NSGAII(int populationSize) {
		this(new PMXCrossover(), new PermutationSwapMutation(0.2), populationSize);
	}

	public I_NSGAII(CrossoverOperator crossover, MutationOperator mutation, int populationSize) {
		super(crossover, mutation, populationSize);
	}
	
	public I_NSGAII(double crossoverProbability, double mutationProbability, int populationSize) {
		super(new PMXCrossover(crossoverProbability), new PermutationSwapMutation(mutationProbability), populationSize);
	}
	
}
