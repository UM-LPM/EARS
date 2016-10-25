package org.um.feri.ears.experiment.so.pso;

import java.util.ArrayList;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.experiment.ee.so.PSOoriginalSolution;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

public class PSOIWD extends Algorithm {

	int populationSize;
	ArrayList<PSOoriginalSolution> population;
	PSOoriginalSolution PgBest;
	double c1, c2, w;
	Task task;

	public PSOIWD() {
		this(20, 1.49445, 1.49445, 0.723);
	}

	public PSOIWD(int populationSize, double c1, double c2, double w) {
		super();
		this.populationSize = populationSize;
		this.c1 = c1;
		this.c2 = c2;
		this.w = w;
		setDebug(debug);
		ai = new AlgorithmInfo("PSOIWD", "PSOIWD", "PSOIWD", "PSOIWD");
		ai.addParameter(EnumAlgorithmParameters.POP_SIZE, populationSize + "");
		ai.addParameter(EnumAlgorithmParameters.C1, c1 + "");
		ai.addParameter(EnumAlgorithmParameters.C2, c2 + "");
		ai.addParameter(EnumAlgorithmParameters.W_INTERIA, w + "");
		au = new Author("Robnik", "aleksander.robnik@student.um.si");
	}
//Algoritem PSO z uporabo dinamiène vztrajnostne uteži
	
	@Override
	public DoubleSolution execute(Task taskProblem) throws StopCriteriaException {
		task = taskProblem;
		initPopulation();
		double v[];
		while (!task.isStopCriteria()) {
			for (int i = 0; i < populationSize; i++) {
				v = new double[task.getDimensions()];
				for (int d = 0; d < task.getDimensions(); d++) {
					PSOoriginalSolution P = population.get(i);
					double r1 = Util.rnd.nextDouble();
					double r2 = Util.rnd.nextDouble();
					w = 0.5 * (Util.rnd.nextDouble() / 2.0);
					v[d] = w * (P.getV()[d]) + c1 * r1 * (P.getPbest().getVariables().get(d) - P.getVariables().get(d))
							+ c2 * r2 * (PgBest.getVariables().get(d) - P.getVariables().get(d));
				}
				
				if (taskProblem.isStopCriteria())
					break;
				population.set(i, population.get(i).update(taskProblem, v));
				
				if (taskProblem.isFirstBetter(population.get(i), PgBest))
					PgBest = population.get(i);

			}
			task.incrementNumberOfIterations();
		}
		return PgBest;
	}

	private void initPopulation() throws StopCriteriaException {
		population = new ArrayList<>();
		for (int i = 0; i < populationSize; i++) {
			population.add(new PSOoriginalSolution(task));
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