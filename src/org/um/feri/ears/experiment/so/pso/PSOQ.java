package org.um.feri.ears.experiment.so.pso;

import java.util.ArrayList;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

public class PSOQ extends Algorithm {

	int populationSize;
	ArrayList<PSOQSolution> population;
	PSOQSolution PgBest;
	double c1, c2, w;
	Task task;

	public PSOQ() {
		this(20, 1.49445, 1.49445, 0.723);
	}

	public PSOQ(int populationSize, double c1, double c2, double w) {
		super();
		this.populationSize = populationSize;
		this.c1 = c1;
		this.c2 = c2;
		this.w = w;
		setDebug(debug);
		ai = new AlgorithmInfo("PSOQ", "PSOQ", "PSOQ", "PSOQ");
		ai.addParameter(EnumAlgorithmParameters.POP_SIZE, populationSize + "");
		ai.addParameter(EnumAlgorithmParameters.C1, c1 + "");
		ai.addParameter(EnumAlgorithmParameters.C2, c2 + "");
		au = new Author("Robnik", "aleksander.robnik@student.um.si");
	}
//Originalni algoritem PSO
	
	@Override
	public DoubleSolution execute(Task taskProblem) throws StopCriteriaException {
		task = taskProblem;
		initPopulation();
		double v[];
		double mBest[];
		while (!task.isStopCriteria()) {
			if (task.isFirstBetter(population.get(0).getPbest(), PgBest))
				PgBest = population.get(0).getPbest();
			if (task.isStopCriteria())
				break;
			
			mBest = new double[task.getNumberOfDimensions()];
			double sumpBest = 0;
			for (int d = 0; d < task.getNumberOfDimensions(); d++) {
				for (int i = 0; i < populationSize; i++) {
					sumpBest += population.get(i).getPbest().getVariables()[d];
				}
				mBest[d] = 1/populationSize * sumpBest;
			}
			for (int i = 0; i < populationSize; i++) {
				v = new double[task.getNumberOfDimensions()];
				
				for (int d = 0; d < task.getNumberOfDimensions(); d++) {
					PSOQSolution P = population.get(i);
					double r1 = Util.rnd.nextDouble();
					double r2 = Util.rnd.nextDouble();
					double phi = c1*r1/(c1*r1 + c2*r2);
					
					v[d] = phi * P.getPbest().getVariables()[d] + (1-phi) * PgBest.getVariables()[d];
				}
				
				if (task.isStopCriteria())
					break;
				
				population.set(i, population.get(i).updateP(task, v, mBest, w));
				
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
			population.add(new PSOQSolution(task));
			if (i == 0)
				PgBest = population.get(0);
			else if (task.isFirstBetter(population.get(i), PgBest))
				PgBest = population.get(i);
			if (task.isStopCriteria())
				break;
		}
	}

	@Override
	public void resetDefaultsBeforNewRun() {
	}
}