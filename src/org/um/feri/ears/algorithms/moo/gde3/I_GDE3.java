package org.um.feri.ears.algorithms.moo.gde3;

import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.PMXCrossover;
import org.um.feri.ears.problems.IntegerMOTask;
import org.um.feri.ears.problems.moo.MOSolutionBase;

public class I_GDE3 extends GDE3<IntegerMOTask, Integer> {
	
	public I_GDE3() {
		this(new PMXCrossover(), 100);
	}
	
	public I_GDE3(int populationSize) {
		this(new PMXCrossover(), populationSize);
	}

	public I_GDE3(CrossoverOperator<Integer, IntegerMOTask, MOSolutionBase<Integer>> crossover, int populationSize) {
		super(crossover, populationSize);
	}
	
}
