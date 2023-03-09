package org.um.feri.ears.algorithms.moo.demo;

import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.DifferentialEvolutionCrossover;
import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.Task;

public class D_DEMO extends DEMO<Double, NumberProblem<Double>> {
	
	public D_DEMO() {
		this(new DifferentialEvolutionCrossover(), 100, 1);
	}
	
	public D_DEMO(int populationSize, int selectionProcedure) {
		this(new DifferentialEvolutionCrossover(), populationSize, selectionProcedure);
	}

	public D_DEMO(CrossoverOperator<NumberProblem<Double>, NumberSolution<Double>> crossover, int populationSize, int selectionProcedure) {
		super(crossover, populationSize, selectionProcedure);
	}

}
