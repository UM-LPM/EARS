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

public class PSODOP extends Algorithm {

	int populationSize;
	ArrayList<PSOoriginalIndividual> population;
	PSOoriginalIndividual PgBest;
	double c1, c2, w, cr;

	public PSODOP() {
		this(20, 1.49445, 1.49445, 0.723);
	}

	public PSODOP(int populationSize, double c1, double c2, double w) {
		super();
		this.populationSize = populationSize;
		this.c1 = c1;
		this.c2 = c2;
		this.w = w;
		this.cr = 0.9;
		setDebug(debug);
		ai = new AlgorithmInfo("PSODOP", "PSODOP", "PSODOP", "PSODOP");
		ai.addParameter(EnumAlgorithmParameters.POP_SIZE, populationSize + "");
		ai.addParameter(EnumAlgorithmParameters.C1, c1 + "");
		ai.addParameter(EnumAlgorithmParameters.C2, c2 + "");
		ai.addParameter(EnumAlgorithmParameters.W_INTERIA, w + "");
		au = new Author("Robnik", "aleksander.robnik@student.um.si");
	}
	// Algoritem PSO z uporabo diferencialnega operatorja

	@Override
	public DoubleSolution execute(Task taskProblem) throws StopCriteriaException {
		initPopulation(taskProblem);
		double v[];
		while (!taskProblem.isStopCriteria()) {
			for (int i = 0; i < populationSize; i++) {
				PSOoriginalIndividual P = population.get(i);
				int jj = Util.rnd.nextInt(populationSize);
				int kk = Util.rnd.nextInt(populationSize);
				
				while((i != jj) && (i != kk))
				{
					jj = Util.rnd.nextInt(populationSize);
					kk = Util.rnd.nextInt(populationSize);
				}
				PSOoriginalIndividual Pj = population.get(jj);
				PSOoriginalIndividual Pk = population.get(kk);

				double Pd[] = new double[taskProblem.getDimensions()];
				for (int d = 0; d < taskProblem.getDimensions(); d++) {
					Pd[d] = Pk.getDoubleVariables()[d] - Pj.getDoubleVariables()[d];
				}

				v = new double[taskProblem.getDimensions()];
				for (int d = 0; d < taskProblem.getDimensions(); d++) {
					double r1 = Util.rnd.nextDouble();
					double r2 = Util.rnd.nextDouble();
					double sigma = c1 * (P.getPbest().getDoubleVariables()[d] - P.getDoubleVariables()[d]);
					if (Util.rnd.nextDouble() < cr) {
						v[d] = w * (P.getV()[d]) + sigma * r1 + c2 * r2 * (PgBest.getDoubleVariables()[d] - P.getDoubleVariables()[d]);
					} else {
						v[d] = P.getV()[d];
					}

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