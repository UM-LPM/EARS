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

public class PSOQ extends Algorithm {

	int populationSize;
	ArrayList<PSOQIndividual> population;
	PSOQIndividual PgBest;
	double c1, c2, w;

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
		initPopulation(taskProblem);
		double v[];
		double mBest[];
		while (!taskProblem.isStopCriteria()) {
			if (taskProblem.isFirstBetter(population.get(0).getPbest(), PgBest))
				PgBest = population.get(0).getPbest();
			if (taskProblem.isStopCriteria())
				break;
			
			mBest = new double[taskProblem.getDimensions()];
			double sumpBest = 0;
			for (int d = 0; d < taskProblem.getDimensions(); d++) {
				for (int i = 0; i < populationSize; i++) {
					sumpBest += population.get(i).getPbest().getVariables().get(d);
				}
				mBest[d] = 1/populationSize * sumpBest;
			}
			for (int i = 0; i < populationSize; i++) {
				v = new double[taskProblem.getDimensions()];
				
				for (int d = 0; d < taskProblem.getDimensions(); d++) {
					PSOQIndividual P = population.get(i);
					double r1 = Util.rnd.nextDouble();
					double r2 = Util.rnd.nextDouble();
					double phi = c1*r1/(c1*r1 + c2*r2);
					
					v[d] = phi * P.getPbest().getVariables().get(d) + (1-phi) * PgBest.getVariables().get(d);
				}
				
				population.set(i, population.get(i).updateP(taskProblem, v, mBest, w));
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
			population.add(new PSOQIndividual(taskProblem));
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