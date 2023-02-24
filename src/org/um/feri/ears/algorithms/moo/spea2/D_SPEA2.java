package org.um.feri.ears.algorithms.moo.spea2;

import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.operators.PolynomialMutation;
import org.um.feri.ears.operators.SBXCrossover;
import org.um.feri.ears.problems.*;

public class D_SPEA2 extends SPEA2<Double, DoubleProblem, Task<NumberSolution<Double>,DoubleProblem>> {

	public D_SPEA2() {
		this(new SBXCrossover(0.9, 20.0), new PolynomialMutation(1.0 / 10, 20.0), 100, 100);
	}
	
	public D_SPEA2(int populationSize, int archiveSize) {
		this(new SBXCrossover(0.9, 20.0), new PolynomialMutation(1.0 / 10, 20.0), populationSize, archiveSize);
	}
	
	public D_SPEA2(int populationSize) {
		this(new SBXCrossover(0.9, 20.0), new PolynomialMutation(1.0 / 10, 20.0), populationSize, populationSize);
	}

	public D_SPEA2(CrossoverOperator<DoubleProblem, NumberSolution<Double>> crossover, MutationOperator<DoubleProblem, NumberSolution<Double>> mutation, int populationSize, int archiveSize) {
		super(crossover, mutation, populationSize, archiveSize);
	}

	@Override
	public void start() throws StopCriterionException {
		super.start();
		mut.setProbability(1.0 / task.problem.getNumberOfDimensions());
	}
}