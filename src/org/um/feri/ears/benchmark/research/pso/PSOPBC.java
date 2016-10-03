package org.um.feri.ears.benchmark.research.pso;

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
	double c1, c2, w;
	PSOPBCIndividual Gbest;
	List<Integer> permutacija = new ArrayList<Integer>();
	ArrayList<PSOPBCIndividual> seznam;

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
			permutacija.add(i);
		}
	}
//Algoritem PSO z uporabo križanja
	
	private void NastaviPopulacijo(Task Problem) throws StopCriteriaException {
		seznam = new ArrayList<PSOPBCIndividual>();
		for (int i = 0; i < populationSize; i++) {
			seznam.add(new PSOPBCIndividual(Problem));
			if (i == 0) {
				Gbest = seznam.get(0);
			} else if (Problem.isFirstBetter(seznam.get(i), Gbest)) {
				Gbest = seznam.get(i);
			}
		}
	}

	@Override
	public DoubleSolution execute(Task taskProblem) throws StopCriteriaException {

		NastaviPopulacijo(taskProblem);

		int stevec = 0;
		double RAND1, RAND2;
		double hitrosti[];

		while (!taskProblem.isStopCriteria()) {
			if (stevec % Crossiter == 0) {
				Collections.shuffle(permutacija);
				for (int i = 0; i < populationSize; i++) {
					seznam.set(i, seznam.get(i).Cross(seznam.get(permutacija.get(i)), taskProblem));

					if (taskProblem.isFirstBetter(seznam.get(i), Gbest)) {
						Gbest = seznam.get(i);
					}

					if (taskProblem.isStopCriteria()) {
						break;
					}
				}

			} else {
				for (int i = 0; i < populationSize; i++) {
					hitrosti = new double[taskProblem.getDimensions()];
					RAND1 = Util.rnd.nextDouble();
					RAND2 = Util.rnd.nextDouble();

					for (int j = 0; j < taskProblem.getDimensions(); j++) {
						hitrosti[j] = w * seznam.get(i).getV()[j]
								+ c1 * RAND1 * (seznam.get(i).getPbest().getVariables().get(j) - seznam.get(i).getVariables().get(j))
								+ c2 * RAND2 * (Gbest.getVariables().get(j) - seznam.get(i).getVariables().get(j));
					}

					seznam.set(i, seznam.get(i).update(taskProblem, hitrosti));
					if (taskProblem.isFirstBetter(seznam.get(i), Gbest)) {
						Gbest = seznam.get(i);
					}

					if (taskProblem.isStopCriteria()) {
						break;
					}
				}
			}
			stevec++;
		}
		return Gbest;
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
