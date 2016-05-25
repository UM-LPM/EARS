package org.um.feri.ears.algorithms.moo.demo;

import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.PMXCrossover;
import org.um.feri.ears.problems.IntegerMOTask;

public class I_DEMO extends DEMO<IntegerMOTask, Integer> {
	
	public I_DEMO() {
		this(new PMXCrossover(), 100, 1);
	}
	
	public I_DEMO(int populationSize, int selectionProcedure) {
		this(new PMXCrossover(), populationSize, selectionProcedure);
	}

	public I_DEMO(CrossoverOperator crossover, int populationSize, int selectionProcedure) {
		super(crossover, populationSize, selectionProcedure);
	}
	
}