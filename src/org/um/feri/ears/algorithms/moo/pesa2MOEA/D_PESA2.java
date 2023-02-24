package org.um.feri.ears.algorithms.moo.pesa2MOEA;

import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.operators.PolynomialMutation;
import org.um.feri.ears.operators.SBXCrossover;
import org.um.feri.ears.problems.*;

public class D_PESA2 extends PESA2<Double, DoubleProblem, Task<NumberSolution<Double>,DoubleProblem>> {
	
	public D_PESA2() {
		this(new SBXCrossover(0.9, 20.0), new PolynomialMutation(1.0 / 10, 20.0), 100);
	}
	
	public D_PESA2(int populationSize) {
		this(new SBXCrossover(0.9, 20.0), new PolynomialMutation(1.0 / 10, 20.0), populationSize);
	}

	public D_PESA2(CrossoverOperator<DoubleProblem, NumberSolution<Double>> crossover, MutationOperator<DoubleProblem, NumberSolution<Double>> mutation, int populationSize) {
		super(crossover, mutation, populationSize);
	}

	@Override
	public void start() throws StopCriterionException {
		super.start();
		mut.setProbability(1.0 / task.problem.getNumberOfDimensions());
	}
	
}
