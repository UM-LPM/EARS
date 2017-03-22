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
import org.um.feri.ears.util.Util;

public class PSOPBC extends Algorithm {

	int populationSize, Crossiter;
	Task task;
	double c1, c2, w;
	PSOPBCSolution Gbest;
	List<Integer> permutations = new ArrayList<Integer>();
	ArrayList<PSOPBCSolution> population;

	public PSOPBC() {
		this(20, 0.723, 1.49445, 1.49445, 100);
	}

	public PSOPBC(int populationSize, double w, double c1, double c2, int crossiter) {
		super();
		this.populationSize = populationSize;
		this.w = w;
		this.c1 = c1;
		this.c2 = c2;
		this.Crossiter = crossiter;
		setDebug(debug); // EARS prints some debug info
		ai = new AlgorithmInfo("PSOPBC", "PSOPBC", "PSOPBC", "PSOPBC");
		ai.addParameter(EnumAlgorithmParameters.POP_SIZE, populationSize + "");
		ai.addParameter(EnumAlgorithmParameters.C1, c1 + "");
		ai.addParameter(EnumAlgorithmParameters.C2, c2 + "");
		ai.addParameter(EnumAlgorithmParameters.UNNAMED1, w + "");
		ai.addParameter(EnumAlgorithmParameters.UNNAMED2, Crossiter + "");
		au = new Author("Leon", "Merc");
		for (int i = 0; i < populationSize; i++) {
			permutations.add(i);
		}
	}
//Algoritem PSO z uporabo križanja
	
	@Override
	public DoubleSolution execute(Task taskProblem) throws StopCriteriaException {
		
		task = taskProblem;
		
		initPopulation();

		int counter = 0;
		double RAND1, RAND2;
		double velocity[];

		while (!task.isStopCriteria()) {
			if (counter % Crossiter == 0) {
				Util.shuffle(permutations);
				for (int i = 0; i < populationSize; i++) {
					
					if (task.isStopCriteria()) {
						break;
					}
					
					population.set(i, population.get(i).Cross(population.get(permutations.get(i)), task));

					if (task.isFirstBetter(population.get(i), Gbest)) {
						Gbest = population.get(i);
					}

				}

			} else {
				for (int i = 0; i < populationSize; i++) {
					velocity = new double[task.getNumberOfDimensions()];
					RAND1 = Util.rnd.nextDouble();
					RAND2 = Util.rnd.nextDouble();

					for (int j = 0; j < task.getNumberOfDimensions(); j++) {
						velocity[j] = w * population.get(i).getV()[j]
								+ c1 * RAND1 * (population.get(i).getPbest().getVariables()[j] - population.get(i).getVariables()[j])
								+ c2 * RAND2 * (Gbest.getVariables()[j] - population.get(i).getVariables()[j]);
					}
					
					if (task.isStopCriteria()) {
						break;
					}

					population.set(i, population.get(i).update(task, velocity));
					if (task.isFirstBetter(population.get(i), Gbest)) {
						Gbest = population.get(i);
					}


				}
			}
			counter++;
			task.incrementNumberOfIterations();
		}
		return Gbest;
	}
	
	private void initPopulation() throws StopCriteriaException {
		population = new ArrayList<PSOPBCSolution>();
		for (int i = 0; i < populationSize; i++) {
			population.add(new PSOPBCSolution(task));
			if (i == 0) {
				Gbest = population.get(0);
			} else if (task.isFirstBetter(population.get(i), Gbest)) {
				Gbest = population.get(i);
			}
		}
	}

	@Override
	public List<AlgorithmBase> getAlgorithmParameterTest(EnumMap<EnumBenchmarkInfoParameters, String> param,
			int maxCombinations) {
		List<AlgorithmBase> alternative = new ArrayList<AlgorithmBase>();
		alternative.add(new PSOPBC(40, 0.792, 1.887, 1.887, 50));
		alternative.add(new PSOPBC(40, 0.792, 1.887, 1.887, 100));
		alternative.add(new PSOPBC(40, 0.792, 1.887, 1.887, 200));
		alternative.add(new PSOPBC(50, 0.95, 1.887, 1.887, 50));
		alternative.add(new PSOPBC(50, 0.95, 1.887, 1.887, 100));
		alternative.add(new PSOPBC(50, 0.95, 1.887, 1.887, 200));
		return alternative;
	}

	@Override
	public void resetDefaultsBeforNewRun() {
		// TODO Auto-generated method stub

	}
}
