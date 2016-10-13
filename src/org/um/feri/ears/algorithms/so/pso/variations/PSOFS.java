package org.um.feri.ears.algorithms.so.pso.variations;

import java.util.ArrayList;
import java.util.Collections;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

public class PSOFS extends Algorithm {

	int populationSize;
	ArrayList<PSOFSIndividual> population;
	PSOFSIndividual PgBest;
	double w, c2, c1;
	int m;

	public PSOFS() {
		this(20, 8, 0.723, 1.49445, 1.49445);
	}

	public PSOFS(int populationSize, int M, double w, double p1, double p2) {
		super();
		this.populationSize = populationSize;
		this.w = w;
		this.c1 = p1;
		this.c2 = p2;
		setDebug(debug);
		ai = new AlgorithmInfo("PSOFS", "PSOFS", "PSOFS", "PSOFS");
		ai.addParameter(EnumAlgorithmParameters.POP_SIZE, populationSize + "");
		ai.addParameter(EnumAlgorithmParameters.C1, p1 + "");
		ai.addParameter(EnumAlgorithmParameters.C2, p2 + "");
		ai.addParameter(EnumAlgorithmParameters.UNNAMED1, w + "");
		au = new Author("Robnik", "aleksander.robnik@student.um.si");
		this.m = M;

	}
//Algoritem PSO z uporabo fleksibilnega roja
	
	@Override
	public DoubleSolution execute(Task taskProblem) throws StopCriteriaException {
		initPopulation(taskProblem);
		double rp, rg;
		double v[];
		int iter = 0;
		while (!taskProblem.isStopCriteria()) {

			for (int i = 0; i < populationSize; i++) {

				v = new double[taskProblem.getDimensions()];
				for (int d = 0; d < taskProblem.getDimensions(); d++) {
					rp = Util.rnd.nextDouble();
					rg = Util.rnd.nextDouble();

					v[d] = w * population.get(i).getV()[d]
							+ c1 * rp * (population.get(i).getP().getVariables().get(d) - population.get(i).getVariables().get(d))
							+ c2 * rg * (PgBest.getDoubleVariables()[d] - population.get(i).getVariables().get(d));

				}
				population.set(i, population.get(i).update(taskProblem, v));
				if (taskProblem.isFirstBetter(population.get(i), PgBest))
					PgBest = population.get(i);
				if (taskProblem.isStopCriteria())
					break;
			}

			Collections.sort(population, new IndividualComparator());
			for (int i = 0; i < populationSize; i++) {
				population.get(i).setRank(populationSize - i);
			}

			if (iter == m) {
				iter = 0;
				double vs = Math.sqrt((population.get(0).getEval() * population.get(population.size() - 1).getEval()));
				for (int i = 0; i < populationSize; i++) {
					if (population.get(i).getEval() < vs) {
						PSOFSIndividual tt;
						if (!taskProblem.isStopCriteria()) {
							tt = new PSOFSIndividual(taskProblem);
						} else {
							tt = PgBest;
						}

						population.remove(i);
						population.add(tt);
					}
				}
			}
			iter++;
		}
		return PgBest;
	}

	private void initPopulation(Task taskProblem) throws StopCriteriaException {
		population = new ArrayList<>();
		for (int i = 0; i < populationSize; i++) {
			population.add(new PSOFSIndividual(taskProblem));
			if (i == 0)
				PgBest = population.get(0);
			else if (taskProblem.isFirstBetter(population.get(i), PgBest))
				PgBest = population.get(i);
		}
	}

	@Override
	public void resetDefaultsBeforNewRun() {
		// TODO Auto-generated method stub

	}
}
