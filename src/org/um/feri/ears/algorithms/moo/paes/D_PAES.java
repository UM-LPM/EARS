package org.um.feri.ears.algorithms.moo.paes;

import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.operators.PolynomialMutation;
import org.um.feri.ears.problems.DoubleMOTask;
import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.StopCriterionException;

public class D_PAES extends PAES<Double, DoubleProblem, DoubleMOTask> {
	
	public D_PAES() {
		this(new PolynomialMutation(1.0 / 10, 20.0), 100);
	}
	
	public D_PAES(int populationSize) {
		this(new PolynomialMutation(1.0 / 10, 20.0), populationSize);
	}

	public D_PAES(MutationOperator mutation, int populationSize) {
		super(mutation, populationSize);
	}

	@Override
	public void start() throws StopCriterionException {
		super.start();
		mut.setProbability(1.0 / numVar);
	}
	
}
