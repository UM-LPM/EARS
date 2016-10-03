package org.um.feri.ears.benchmark.research.pso;

import java.util.ArrayList;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

public class PSOoriginal extends Algorithm {

	int populationSize;
	ArrayList<PSOoriginalIndividual> population;
	PSOoriginalIndividual PgBest;
	double c1, c2;

	public PSOoriginal() {
		this(20, 1.49445, 1.49445);
	}

	public PSOoriginal(int populationSize, double c1, double c2) {
		super();
		this.populationSize = populationSize;
		this.c1 = c1;
		this.c2 = c2;
		setDebug(debug);
		ai = new AlgorithmInfo("PSOO", "PSOO", "PSOO", "PSOO");
		ai.addParameter(EnumAlgorithmParameters.POP_SIZE, populationSize + "");
		ai.addParameter(EnumAlgorithmParameters.C1, c1 + "");
		ai.addParameter(EnumAlgorithmParameters.C2, c2 + "");
		au = new Author("Robnik", "aleksander.robnik@student.um.si");
	}
//Originalni algoritem PSO
	
	@Override
	public DoubleSolution execute(Task taskProblem) throws StopCriteriaException {
		initPopulation(taskProblem);
		double v[];
		while (!taskProblem.isStopCriteria()) {
			for (int i = 0; i < populationSize; i++) {
				v = new double[taskProblem.getDimensions()];
				for (int d = 0; d < taskProblem.getDimensions(); d++) {
					PSOoriginalIndividual P = population.get(i);
					double r1 = Util.rnd.nextDouble();
					double r2 = Util.rnd.nextDouble();
					v[d] = (P.getV()[d]) + c1 * r1 * (P.getPbest().getVariables().get(d) - P.getVariables().get(d))
							+ c2 * r2 * (PgBest.getVariables().get(d) - P.getVariables().get(d));
				}
				population.set(i, population.get(i).update(taskProblem, v));
				if (taskProblem.isFirstBetter(population.get(i), PgBest))
					PgBest = population.get(i);
				if (taskProblem.isStopCriteria())
					break;
			}
		}
		return PgBest;
	}

	private void initPopulation(Task taskProblem) throws StopCriteriaException {
		population = new ArrayList<>();
		for (int i = 0; i < populationSize; i++) {
			population.add(new PSOoriginalIndividual(taskProblem));
			if (i == 0)
				PgBest = population.get(0);
			else if (taskProblem.isFirstBetter(population.get(i), PgBest))
				PgBest = population.get(i);
			if (taskProblem.isStopCriteria())
				break;
		}
	}

	@Override
	public void resetDefaultsBeforNewRun() {
	}
}