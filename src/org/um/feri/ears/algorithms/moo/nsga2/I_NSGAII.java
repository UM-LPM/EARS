package org.um.feri.ears.algorithms.moo.nsga2;

import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.operators.PMXCrossover;
import org.um.feri.ears.operators.PermutationSwapMutation;
import org.um.feri.ears.problems.CombinatorialProblem;
import org.um.feri.ears.problems.NumberProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.Task;

public class I_NSGAII extends NSGAII<Integer, NumberProblem<Integer>> {
	
	public I_NSGAII() {
		//this(new PMXCrossover(1.0), new PermutationSwapMutation(0.86), 108);
		this(new PMXCrossover(), new PermutationSwapMutation(0.2), 100);
	}
	
	public I_NSGAII(int populationSize) {
		this(new PMXCrossover(), new PermutationSwapMutation(0.2), populationSize);
	}

	public I_NSGAII(CrossoverOperator<NumberProblem<Integer>, NumberSolution<Integer>> crossover, MutationOperator<NumberProblem<Integer>, NumberSolution<Integer>> mutation, int populationSize) {
		super(crossover, mutation, populationSize);
	}
	
	public I_NSGAII(double crossoverProbability, double mutationProbability, int populationSize) {
		super(new PMXCrossover(crossoverProbability), new PermutationSwapMutation(mutationProbability), populationSize);
	}
	
}
