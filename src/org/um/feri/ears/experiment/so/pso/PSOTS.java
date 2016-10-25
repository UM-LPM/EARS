package org.um.feri.ears.experiment.so.pso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

public class PSOTS extends Algorithm {

	int populationSize;
	Task task;
	ArrayList<PSOTSSolution> populationA, populationB;
	PSOTSSolution PgBestA, PgBestB;
	double c1, c2, w;
	List<Integer> permutacijaA = new ArrayList<Integer>();
	List<Integer> permutacijaB = new ArrayList<Integer>();

	public PSOTS() {
		this(20, 1.49445, 1.49445, 0.723);
	}

	public PSOTS(int populationSize, double c1, double c2, double w) {
		super();
		this.populationSize = populationSize;
		this.c1 = c1;
		this.c2 = c2;
		this.w = w;
		for (int i = 0; i < populationSize; i++) {
			permutacijaA.add(i);
			permutacijaB.add(i);
		}
		setDebug(debug);
		ai = new AlgorithmInfo("PSOTS", "PSOTS", "PSOTS", "PSOTS");
		ai.addParameter(EnumAlgorithmParameters.POP_SIZE, populationSize + "");
		ai.addParameter(EnumAlgorithmParameters.C1, c1 + "");
		ai.addParameter(EnumAlgorithmParameters.C2, c2 + "");
		ai.addParameter(EnumAlgorithmParameters.W_INTERIA, w + "");
		au = new Author("Robnik", "aleksander.robnik@student.um.si");
	}
	// Algoritem PSO z uporabo dveh rojev

	@Override
	public DoubleSolution execute(Task taskProblem) throws StopCriteriaException {
		task = taskProblem;
		initPopulation();
		double vA[];
		double vB[];
		while (!task.isStopCriteria()) {
			Collections.shuffle(permutacijaA);
			Collections.shuffle(permutacijaB);
			for (int i = 0; i < populationSize; i++) {
				vA = new double[task.getDimensions()];
				vB = new double[task.getDimensions()];

				for (int d = 0; d < task.getDimensions(); d++) {
					PSOTSSolution PA = populationA.get(i);
					PSOTSSolution PB = populationB.get(i);
					double r1A = Util.rnd.nextDouble();
					double r2A = Util.rnd.nextDouble();
					double r1B = Util.rnd.nextDouble();
					double r2B = Util.rnd.nextDouble();
					vA[d] = w * (PA.getV()[d]) + c1 * r1A * (PA.getPbest().getVariables().get(d) - PA.getVariables().get(d))
							+ c2 * r2A * (PgBestA.getVariables().get(d) - PA.getVariables().get(d));
					vB[d] = w * (PB.getV()[d]) + c1 * r1B * (PB.getPbest().getVariables().get(d) - PB.getVariables().get(d))
							+ c2 * r2B * (PgBestB.getVariables().get(d) - PB.getVariables().get(d));
				}
				
				if (task.isStopCriteria())
					break;
				populationA.set(i, populationA.get(i).update(task, vA));
				if (task.isStopCriteria())
					break;
				populationB.set(i, populationB.get(i).update(task, vB));

				/*
				if(Util.rnd.nextDouble() < 0.5)
					populationA.set(i, populationA.get(i).Cross(populationB.get(permutacijaB.get(i)), taskProblem));

				else
					populationB.set(i, populationB.get(i).Cross(populationA.get(permutacijaA.get(i)), taskProblem));
				*/
				if (task.isFirstBetter(populationA.get(i), PgBestA)) {
					PgBestA = populationA.get(i);
				}


				if (task.isFirstBetter(populationB.get(i), PgBestB)) {
					PgBestB = populationB.get(i);
				}

			}

			if (task.isFirstBetter(PgBestA, PgBestB)) {
				PgBestB = PgBestA;
			}
			
			task.incrementNumberOfIterations();
			
			if (task.isStopCriteria())
				break;
		}
		return PgBestA;
	}

	private void initPopulation() throws StopCriteriaException {
		populationA = new ArrayList<>();
		populationB = new ArrayList<>();
		for (int i = 0; i < populationSize; i++) {
			populationA.add(new PSOTSSolution(task));
			populationB.add(new PSOTSSolution(task));
			if (i == 0) {
				PgBestA = populationA.get(0);
			} else if (task.isFirstBetter(populationA.get(i), PgBestA))
				PgBestA = populationA.get(i);
			if (task.isStopCriteria())
				break;

			if (i == 0) {
				PgBestB = populationB.get(0);
			} else if (task.isFirstBetter(populationB.get(i), PgBestB))
				PgBestB = populationB.get(i);
			if (task.isStopCriteria())
				break;
		}
	}

	@Override
	public void resetDefaultsBeforNewRun() {
	}
}