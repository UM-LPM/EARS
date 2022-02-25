package org.um.feri.ears.experiment.ee;

import java.util.ArrayList;
import java.util.List;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

public class PSOoriginalLogging extends Algorithm {

	int populationSize;
	ArrayList<PSOoriginalSolution> population;
	PSOoriginalSolution PgBest;
	double c1, c2;
	Task task;

	public PSOoriginalLogging() {
		this(20, 1.49445, 1.49445);
	}

	public PSOoriginalLogging(int populationSize, double c1, double c2) {
		super();
		this.populationSize = populationSize;
		this.c1 = c1;
		this.c2 = c2;
		setDebug(debug);
		ai = new AlgorithmInfo("PSO", "Particle Swarm Optimization", "");
		au = new Author("Matej", "matej.crepinsek@um.si");
	}
//Originalni algoritem PSO
	
	@Override
	public DoubleSolution execute(Task taskProblem) throws StopCriterionException {
		
		task = taskProblem;
		taskProblem.enableAncestorLogging();
		
		if (taskProblem.isStopCriterion())
			return null;
		initPopulation(taskProblem);
		double v[];
		while (!taskProblem.isStopCriterion()) {
			for (int i = 0; i < populationSize; i++) {
				v = new double[taskProblem.getNumberOfDimensions()];
				for (int d = 0; d < taskProblem.getNumberOfDimensions(); d++) {
					PSOoriginalSolution P = population.get(i);
					double r1 = Util.rnd.nextDouble();
					double r2 = Util.rnd.nextDouble();
					v[d] = (P.getV()[d]) + c1 * r1 * (P.getPbest().getValue(d) - P.getValue(d))
							+ c2 * r2 * (PgBest.getValue(d) - P.getValue(d));
				}
				population.set(i, update(population.get(i), v));
				if (taskProblem.isFirstBetter(population.get(i), PgBest))
					PgBest = population.get(i);
				if (taskProblem.isStopCriterion())
					break;
			}
			task.incrementNumberOfIterations();
		}
		
		return PgBest;
	}

	private PSOoriginalSolution update(PSOoriginalSolution sol, double[] v) throws StopCriterionException {
		
		double x[] = sol.getDoubleVariables();
		for (int i = 0; i < x.length; i++) {
			x[i] = task.setFeasible(x[i] + v[i], i);
		}
		List<DoubleSolution> parents = new ArrayList<DoubleSolution>();
		parents.add(sol.Pbest);
		parents.add(PgBest);
		parents.add(sol);
		
		PSOoriginalSolution tmp = new PSOoriginalSolution(task.eval(x,parents));
		
		if (task.isFirstBetter(tmp, sol.Pbest)) {
			tmp.Pbest = tmp;
		} else
			tmp.Pbest = sol.Pbest;
		tmp.v = v;
		return tmp;

	}

	private void initPopulation(Task taskProblem) throws StopCriterionException {
		population = new ArrayList<>();
		for (int i = 0; i < populationSize; i++) {
			
			population.add(new PSOoriginalSolution(taskProblem));
			if (i == 0)
				PgBest = population.get(0);
			else if (taskProblem.isFirstBetter(population.get(i), PgBest))
				PgBest = population.get(i);
			
			if (taskProblem.isStopCriterion())
				break;

		}
	}

	@Override
	public void resetToDefaultsBeforeNewRun() {
	}
}