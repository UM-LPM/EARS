package org.um.feri.ears.experiment.so.pso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmBase;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.benchmark.EnumBenchmarkInfoParameters;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.MersenneTwister;
import org.um.feri.ears.util.Util;

public class PSOS extends Algorithm {

	int populationSize, k;

	ArrayList<PSOSSolution> population;
	PSOSSolution PgBest;
	Task task;

	double w, p1, p2;

	public PSOS() {
		this(20, 0.723, 1.49445, 1.49445);
	}

	public PSOS(int populationSize, double w, double p1, double p2) {
		super();
		this.populationSize = populationSize;
		this.w = w;
		this.p1 = p1;
		this.p2 = p2;
		k = 7;

		setDebug(false);
		ai = new AlgorithmInfo("PSOS", "PSOS", "PSOS", "PSOS");
		ai.addParameter(EnumAlgorithmParameters.POP_SIZE, populationSize + "");
		ai.addParameter(EnumAlgorithmParameters.C1, p1 + "");
		ai.addParameter(EnumAlgorithmParameters.C2, p2 + "");
		ai.addParameter(EnumAlgorithmParameters.UNNAMED1, w + "");
		au = new Author("Aleksander Robnik", "aleksander.robnik@student.um.si");
	}
//Algoritem PSO z uporabo selekcije
	
	@Override
	public DoubleSolution execute(Task taskProblem) throws StopCriteriaException {
		task = taskProblem;
		initPopulation();
		double rp, rg;
		double v[];
		int index;
		while (!task.isStopCriteria()) {
			for (int i = 0; i < populationSize; i++) {
				for (int j = 0; j < k; j++) {
					index = Util.rnd.nextInt(populationSize);
					if (task.isFirstBetter(population.get(index), population.get(i)))
						population.get(i).turnamentScore++;
				}
			}
			Collections.sort(population);
			for (int i = 0; i < (populationSize / 2); i++) {
				int j = (i + populationSize / 2);
				
				PSOSSolution newInd = PSOSSolution.update(population.get(i).getVariables(), population.get(i).getV(), population.get(j).p,population.get(j).getEval());
				population.set(j, newInd);
			}
			for (int i = 0; i < populationSize; i++) {
				rp = Util.rnd.nextDouble();
				rg = Util.rnd.nextDouble();
				v = new double[task.getNumberOfDimensions()];
				for (int d = 0; d < task.getNumberOfDimensions(); d++) {
					v[d] = w * population.get(i).getV()[d] + p1 * rp * (population.get(i).getP().getVariables()[d] - population.get(i).getVariables()[d])
							+ p2 * rg * (PgBest.getVariables()[d] - population.get(i).getVariables()[d]);
				}
				if (task.isStopCriteria())
					break;
				
				//population.set(i, population.get(i).update(task, v));
				
				population.set(i, update(population.get(i), v));
				
				if (task.isFirstBetter(population.get(i), PgBest))
					PgBest = population.get(i);

			}
			task.incrementNumberOfIterations();
		}

		return PgBest;
	}

	private void initPopulation() throws StopCriteriaException {
		population = new ArrayList<>();
		for (int i = 0; i < populationSize; i++) {
			population.add(new PSOSSolution(task));
			if (i == 0)
				PgBest = population.get(0);
			else if (task.isFirstBetter(population.get(i), PgBest))
				PgBest = population.get(i);
		}
	}
	
	public PSOSSolution update(PSOSSolution sol, double v[]) throws StopCriteriaException {
		double x[] = sol.getDoubleVariables();
		for (int i=0; i<x.length; i++) {
			x[i]=task.setFeasible(x[i]+v[i],i);
		}
		PSOSSolution tmp = new PSOSSolution(task.eval(x));
		sol.turnamentScore = 0;
		if (task.isFirstBetter(tmp, sol.p)) {
			tmp.p = tmp;
		} else
			tmp.p = sol.p;
		tmp.v = v;
		return tmp;
	
	}

	@Override
	public void resetDefaultsBeforNewRun() {
	}

	@Override
	public List<AlgorithmBase> getAlgorithmParameterTest(EnumMap<EnumBenchmarkInfoParameters, String> param,
			int maxCombinations) {
		List<AlgorithmBase> alternative = new ArrayList<AlgorithmBase>();
		// alternative.add(new SPSOAlgorithm());
		return alternative;
	}
}