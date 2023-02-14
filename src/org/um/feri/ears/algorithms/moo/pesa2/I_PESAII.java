package org.um.feri.ears.algorithms.moo.pesa2;

import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.operators.PMXCrossover;
import org.um.feri.ears.operators.PermutationSwapMutation;
import org.um.feri.ears.problems.IntegerMOTask;
import org.um.feri.ears.problems.IntegerProblem;

public class I_PESAII extends PESAII<Integer, IntegerProblem, IntegerMOTask> {
	
	public I_PESAII() {
		this(new PMXCrossover(), new PermutationSwapMutation(0.2), 100, 100);
		//this(new PMXCrossover(0.37), new PermutationSwapMutation(1.0), 110, 110);
		//PESAII_populationSize(110.0)crossoverProbability(0.3716959021384395)mutationProbability(1.0)
		//this(new PMXCrossover(0.20599903582237256), new PermutationSwapMutation(0.9973160787260817), 90, 100);
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
	
	public I_PESAII(double crossoverProbability, double mutationProbability, int populationSize, int archiveSize) {
		super(new PMXCrossover(crossoverProbability), new PermutationSwapMutation(mutationProbability), populationSize, archiveSize);
	}
}