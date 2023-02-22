package org.um.feri.ears.algorithms.moo.moead;

import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.operators.PMXCrossover;
import org.um.feri.ears.operators.PermutationSwapMutation;
import org.um.feri.ears.problems.CombinatorialProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.Task;

public class I_MOEAD extends MOEAD<Integer, CombinatorialProblem, Task<NumberSolution<Integer>, CombinatorialProblem>> {
	
	public I_MOEAD() {
		this(new PMXCrossover(), new PermutationSwapMutation(0.2), 100);
		//this(new PMXCrossover(0.5), new PermutationSwapMutation(0.98), 192);
		//MOEAD_populationSize(192.0)crossoverProbability(0.5079362798258964)mutationProbability(0.9842448640408455)
	}
	
	public I_MOEAD(int populationSize) {
		this(new PMXCrossover(), new PermutationSwapMutation(0.2), populationSize);
	}


	public I_MOEAD(CrossoverOperator crossover, MutationOperator mutation, int populationSize) {
		super(crossover, mutation, populationSize);
	}
	
	public I_MOEAD(double crossoverProbability, double mutationProbability, int populationSize) {
		super(new PMXCrossover(crossoverProbability), new PermutationSwapMutation(mutationProbability), populationSize);
	}
	
}
