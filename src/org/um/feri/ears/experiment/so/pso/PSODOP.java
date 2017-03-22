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

public class PSODOP extends Algorithm {

	int populationSize;
	ArrayList<PSOoriginalSolution> population;
	PSOoriginalSolution PgBest;
	double c1, c2, w, cr;
	Task task;

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
		ai = new AlgorithmInfo("PSO-DV", 
				"@incollection{das2008particle,\n"+
						"title={Particle swarm optimization and differential evolution algorithms: technical analysis, applications and hybridization perspectives},\n"+
						"author={Das, Swagatam and Abraham, Ajith and Konar, Amit},\n"+
						"booktitle={Advances of Computational Intelligence in Industrial Systems},\n"+
						"pages={1--38},\n"+
						"year={2008},\n"+
						"publisher={Springer}}", 
						"PSO-DV",
				"Particle Swarm Optimization with Differentially perturbed Velocity");
		ai.addParameter(EnumAlgorithmParameters.POP_SIZE, populationSize + "");
		ai.addParameter(EnumAlgorithmParameters.C1, c1 + "");
		ai.addParameter(EnumAlgorithmParameters.C2, c2 + "");
		ai.addParameter(EnumAlgorithmParameters.W_INTERIA, w + "");
		au = new Author("Robnik", "aleksander.robnik@student.um.si");
	}
	// Algoritem PSO z uporabo diferencialnega operatorja

	@Override
	public DoubleSolution execute(Task taskProblem) throws StopCriteriaException {
		task = taskProblem;
		initPopulation();
		
		double v[];
		while (!taskProblem.isStopCriteria()) {
			for (int i = 0; i < populationSize; i++) {
				PSOoriginalSolution P = population.get(i);
				int jj = Util.rnd.nextInt(populationSize);
				int kk = Util.rnd.nextInt(populationSize);
				
				while((i != jj) && (i != kk))
				{
					jj = Util.rnd.nextInt(populationSize);
					kk = Util.rnd.nextInt(populationSize);
				}
				PSOoriginalSolution Pj = population.get(jj);
				PSOoriginalSolution Pk = population.get(kk);

				double Pd[] = new double[taskProblem.getNumberOfDimensions()];
				for (int d = 0; d < taskProblem.getNumberOfDimensions(); d++) {
					Pd[d] = Pk.getVariables()[d] - Pj.getVariables()[d];
				}

				v = new double[taskProblem.getNumberOfDimensions()];
				for (int d = 0; d < taskProblem.getNumberOfDimensions(); d++) {
					double r1 = Util.rnd.nextDouble();
					double r2 = Util.rnd.nextDouble();
					double sigma = c1 * (P.getPbest().getVariables()[d] - P.getVariables()[d]);
					if (Util.rnd.nextDouble() < cr) {
						v[d] = w * (P.getV()[d]) + sigma * r1 + c2 * r2 * (PgBest.getVariables()[d] - P.getVariables()[d]);
					} else {
						v[d] = P.getV()[d];
					}

				}
				
				if(task.isStopCriteria())
					break;
				population.set(i, population.get(i).update(task, v));
				
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