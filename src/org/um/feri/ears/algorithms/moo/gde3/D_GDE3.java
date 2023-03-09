package org.um.feri.ears.algorithms.moo.gde3;

import org.um.feri.ears.algorithms.moo.nsga2.NSGAII;
import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.DifferentialEvolutionCrossover;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.operators.PolynomialMutation;
import org.um.feri.ears.operators.SBXCrossover;
import org.um.feri.ears.problems.*;

public class D_GDE3 extends GDE3<Double, NumberProblem<Double>> {
	
	public D_GDE3() {
		this(new DifferentialEvolutionCrossover(), 100);
	}
	
	public D_GDE3(int populationSize) {
		this(new DifferentialEvolutionCrossover(), populationSize);
	}

	public D_GDE3(CrossoverOperator<NumberProblem<Double>, NumberSolution<Double>> crossover, int populationSize) {
		super(crossover, populationSize);
	}
	
}
