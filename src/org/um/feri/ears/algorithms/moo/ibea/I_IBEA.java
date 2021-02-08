package org.um.feri.ears.algorithms.moo.ibea;

import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.operators.PMXCrossover;
import org.um.feri.ears.operators.PermutationSwapMutation;
import org.um.feri.ears.problems.IntegerMOTask;

public class I_IBEA extends IBEA<IntegerMOTask, Integer> {
	
	public I_IBEA() {
		this(new PMXCrossover(), new PermutationSwapMutation(0.2), 100, 100);
		//this(new PMXCrossover(0.63), new PermutationSwapMutation(0.9), 173, 100);
		//IBEA_populationSize(173.0)crossoverProbability(0.6276531739700488)mutationProbability(0.9068948829446266)
		//this(new PMXCrossover(0.49008654478399677), new PermutationSwapMutation(0.8441849543755583), 176, 100);
	}
	
	public I_IBEA(int populationSize) {
		this(new PMXCrossover(), new PermutationSwapMutation(0.2), populationSize, populationSize);
	}
	
	public I_IBEA(int populationSize, int archiveSize) {
		this(new PMXCrossover(), new PermutationSwapMutation(0.2), populationSize, archiveSize);
	}

	public I_IBEA(CrossoverOperator crossover, MutationOperator mutation, int populationSize, int archiveSize) {
		super(crossover, mutation, populationSize, archiveSize);
	}
	
	public I_IBEA(double crossoverProbability, double mutationProbability, int populationSize, int archiveSize) {
		super(new PMXCrossover(crossoverProbability), new PermutationSwapMutation(mutationProbability), populationSize, archiveSize);
	}
}
