package org.um.feri.ears.algorithms.so.ba;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;

public class BA extends NumberAlgorithm {

	@AlgorithmParameter(name = "population size")
	private int popSize; //typically 10 to 40
	@AlgorithmParameter(name = "loudness")
	private double A; // (constant or decreasing)
	@AlgorithmParameter(name = "pulse rate")
	private double r; // (constant or decreasing)
	private double Qmin, Qmax; //min and max frequency
	private double alpha, gamma; // 0 < alpha < 1, gamma > 0

	private BatSolution best;
	private ArrayList<BatSolution> population;
	
	public BA()
	{
		this(30, 0.5, 0.5, 0, 2, 0.9, 0.9);
	}
	
	public BA(int popSize, double A, double r, double  Qmin, double Qmax, double alpha, double gamma)
	{
		super();
		this.popSize = popSize;
		this.A = A;
		this.r = r;
		this.Qmin = Qmin;
		this.Qmax = Qmax;
		this.alpha = alpha;
		this.gamma = gamma;
		
		au = new Author("miha", "miha.ravber@um.si");
		ai = new AlgorithmInfo("BA", "Bat Algorithm",
				"@article{yang2010new,"
				+ "title={A new metaheuristic bat-inspired algorithm},"
				+ "author={Yang, Xin-She},"
				+ "booktitle={Nature inspired cooperative strategies for optimization (NICSO 2010)},"
				+ "pages={65--74},"
				+ "year={2010},"
				+ "publisher={Springer}}"
		);
	}

	@Override
	public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
		
		this.task = task;
		initPopulation();
		
		double[] S = new double[task.problem.getNumberOfDimensions()];

		while (!task.isStopCriterion()) {

			for (int i = 0; i < popSize; i++) {
				//Generate new solutions by adjusting frequency, and updating velocities and locations/solutions [Eq.(2) to(4)]
				population.get(i).Q = RNG.nextDouble(Qmin, Qmax);
				
				for(int j = 0; j < task.problem.getNumberOfDimensions() ; j++){
					population.get(i).v[j] += (population.get(i).getValue(j) - best.getValue(j)) * population.get(i).Q;
					S[j] =  population.get(i).getValue(j) + population.get(i).v[j];
				}
				
				if(RNG.nextDouble() > r)
				{
					//Select a solution among the best solutions and generate a local solution around the best solution
					//TODO how to select from best solutions?
					for(int j = 0; j < task.problem.getNumberOfDimensions() ; j++){
						S[j] = best.getValue(j) + 0.001 * (2 * RNG.nextDouble() - 1);
						//S[j] = best.getValue(j) + 0.001 * population.get(i).A * (2 * RNG.nextDouble() - 1);
						//S[j] = population.get(i).getValue(j) + RNG.nextDouble() * avgA;
					}
				}
				
				task.problem.setFeasible(S);
				if(task.isStopCriterion())
					break;

				NumberSolution<Double> newSolution = new NumberSolution<>(Util.toDoubleArrayList(S));
				task.eval(newSolution);

				BatSolution newBat = new BatSolution(newSolution);
				newBat.v = population.get(i).v;
				newBat.Q = population.get(i).Q;
				newBat.A = population.get(i).A;
				newBat.r = population.get(i).r;
				
				
				if(task.problem.isFirstBetter(newBat, population.get(i)) && RNG.nextDouble() < A)
				{
					//Update loudness and pulse rate
					newBat.A = alpha * newBat.A;
					newBat.r = newBat.r * (1 - Math.exp(-gamma * task.getNumberOfIterations()));
					//Replace old solution with new
					population.set(i, newBat);
				}
				
				if(task.problem.isFirstBetter(newBat, best))
				{
					//System.out.println(best.getEval());
					best = new BatSolution(newBat);
				}
			}
		}
		return best;
	}

	private void initPopulation() throws StopCriterionException {
		
		population = new ArrayList<BatSolution>();
		best = new BatSolution(task.getRandomEvaluatedSolution());
		best.v = new double[task.problem.getNumberOfDimensions()];
		population.add(best);
		for (int i = 1; i < popSize; i++) {
			
			BatSolution newSolution = new BatSolution(task.getRandomEvaluatedSolution());
			newSolution.v = new double[task.problem.getNumberOfDimensions()];
			newSolution.A = RNG.nextDouble(1,2);
			newSolution.r = RNG.nextDouble();
			if(task.problem.isFirstBetter(newSolution, best))
				best = new BatSolution(newSolution);
			
			population.add(newSolution);
			if (task.isStopCriterion())
				break;
		}
	}

	@Override
	public void resetToDefaultsBeforeNewRun() {

	}
}
