package org.um.feri.ears.algorithms.moo.demo;

import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.PMXCrossover;
import org.um.feri.ears.problems.CombinatorialProblem;
import org.um.feri.ears.problems.NumberProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.Task;

public class I_DEMO extends DEMO<Integer, NumberProblem<Integer>> {
	
	public I_DEMO() {
		this(new PMXCrossover(), 100, 1);
	}
	
	public I_DEMO(int populationSize, int selectionProcedure) {
		this(new PMXCrossover(), populationSize, selectionProcedure);
	}

	public I_DEMO(CrossoverOperator<NumberProblem<Integer>, NumberSolution<Integer>> crossover, int populationSize, int selectionProcedure) {
		super(crossover, populationSize, selectionProcedure);
	}
	
}