/*
 * Flower Pollination Algorithm (FPA)
 * Flower Pollination Algorithm by Xin-She Yang in Java.
 */
package org.um.feri.ears.algorithms.so.fpa;

import org.apache.commons.math3.special.Gamma;
import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.TaskComparator;
import org.um.feri.ears.util.Util;

import java.util.ArrayList;

public class FPA extends Algorithm{
	
	DoubleSolution best;
	
	int pop_size;
	Task task;
	
	private double lambda;
	private double switchProbability;
	// ND: Normal distribution
	private static final double meanND = 0.0;
	private static final double stdDevND = 1.0;
	
	ArrayList<DoubleSolution> population;
	ArrayList<DoubleSolution> offspringPopulation;
	
	public FPA()
	{
		this(20,1.5,0.8);
	}
	
	public FPA(int pop_size, double lambda, double switchProbability)
	{
		super();
		this.pop_size = pop_size;
		this.lambda = lambda;
		this.switchProbability = switchProbability;
		
		au = new Author("miha", "miha.ravber@um.si");
		ai = new AlgorithmInfo("FPA",
				"@inproceedings{yang2012flower,"
				+ "title={Flower pollination algorithm for global optimization},"
				+ "author={Yang, Xin-She},"
				+ "booktitle={International Conference on Unconventional Computing and Natural Computation},"
				+ "pages={240--249},"
				+ "year={2012},"
				+ "organization={Springer}}",
				"FPA", "Flower Pollination Algorithm");
		ai.addParameter(EnumAlgorithmParameters.POP_SIZE, pop_size + "");
	}

	@Override
	public DoubleSolution execute(Task taskProblem) throws StopCriteriaException {
		task = taskProblem;
		initPopulation();
		
		double[] candidate;
		double[] levy = new double[task.getNumberOfDimensions()];
		
		int rand1, rand2;
		double epsilon;
		while (!task.isStopCriteria()) {
			
			for(int i = 0; i < pop_size; i++) {
			
				candidate = new double[task.getNumberOfDimensions()];
				if (Util.nextDouble() > switchProbability) {
					/* Global Pollination */
					levy = levy();

					for (int j = 0; j < task.getNumberOfDimensions(); j++) {
						candidate[j] = population.get(i).getValue(j) + levy[j] * (best.getValue(j) - population.get(i).getValue(j));
					}
				} else {
					/* Local Pollination */
					epsilon = Util.nextDouble();

					do {
						rand1 = Util.nextInt(pop_size);
					} while (rand1 == i);
					do {
						rand2 = Util.nextInt(pop_size);
					} while (rand2 == rand1);

					for (int j = 0; j < task.getNumberOfDimensions(); j++)
						candidate[j] += epsilon * (population.get(rand1).getValue(j) - population.get(rand2).getValue(j));
				}
				
				// Check bounds
				candidate = task.setFeasible(candidate);

				// Evaluate new solution
				if(task.isStopCriteria())
					break;
				DoubleSolution newSolution = task.eval(candidate);

				// If the new solution is better: Replace
				if(task.isFirstBetter(newSolution, population.get(i))){
					population.set(i, newSolution);
				}

				// Update best solution
				if (task.isFirstBetter(newSolution, best)) {
					best = new DoubleSolution(newSolution);
				}
				
			}
						
			task.incrementNumberOfIterations();
		}
		
		return best;
	}
	
	/** creates Levy flight samples */
	private double[] levy() {
		double[] step = new double[task.getNumberOfDimensions()];
		
		double sigma = Math.pow(Gamma.gamma(1 + lambda) * Math.sin(Math.PI * lambda / 2)
				/ (Gamma.gamma((1 + lambda) / 2) * lambda * Math.pow(2, (lambda - 1) / 2)), (1 / lambda));

		for (int i = 0; i < task.getNumberOfDimensions(); i++) {

			double u = Distribution.normal(Util.rnd, meanND, stdDevND) * sigma;
			double v = Distribution.normal(Util.rnd, meanND, stdDevND);

			step[i] = 0.01 * u / (Math.pow(Math.abs(v), (1 - lambda)));
		}
		return step;
	}

	private void initPopulation() throws StopCriteriaException {
		population = new ArrayList<DoubleSolution>();

		for (int i = 0; i < pop_size; i++) {
			if (task.isStopCriteria())
				break;
			DoubleSolution newSolution = task.getRandomSolution();
			population.add(newSolution);
		}
		
		population.sort(new TaskComparator(task));
		best = new DoubleSolution(population.get(0));
	}
	

	@Override
	public void resetToDefaultsBeforeNewRun() {
	}

}
