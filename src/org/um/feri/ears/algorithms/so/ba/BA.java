package org.um.feri.ears.algorithms.so.ba;

import java.util.ArrayList;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

public class BA extends Algorithm{

	int pop_size; //typically 10 to 40
	Task task;
	
    double A; //Loudness  (constant or decreasing)
    double r; //Pulse rate (constant or decreasing)
    double Qmin, Qmax; //min and max frequency
    double alpha, gamma; // 0 < alpha < 1, gamma > 0

    BatSolution best;
	
	
	ArrayList<BatSolution> population;
	
	public BA()
	{
		this(30, 0.5, 0.5, 0, 2, 0.9, 0.9);
	}
	
	public BA(int pop_size, double A, double r, double  Qmin, double Qmax, double alpha, double gamma)
	{
		super();
		this.pop_size = pop_size;
		this.A = A;
		this.r = r;
		this.Qmin = Qmin;
		this.Qmax = Qmax;
		this.alpha = alpha;
		this.gamma = gamma;
		
		au = new Author("miha", "miha.ravber@um.si");
		ai = new AlgorithmInfo("BA",
				"@article{yang2010new,"
				+ "title={A new metaheuristic bat-inspired algorithm},"
				+ "author={Yang, Xin-She},"
				+ "booktitle={Nature inspired cooperative strategies for optimization (NICSO 2010)},"
				+ "pages={65--74},"
				+ "year={2010},"
				+ "publisher={Springer}}",
				"BA", "Bat Algorithm");
		ai.addParameter(EnumAlgorithmParameters.POP_SIZE, pop_size + "");
	}

	@Override
	public DoubleSolution execute(Task taskProblem) throws StopCriteriaException {
		
		task = taskProblem;
		initPopulation();
		
		double[] S = new double[task.getNumberOfDimensions()];

		while (!task.isStopCriteria()) {
			
			double avgA; //average loudness
			double sumA = 0.0;
			for (int i = 0; i < pop_size; i++) {
				
				
				//Generate new solutions by adjusting frequency, and updating velocities and locations/solutions [Eq.(2) to(4)]
				population.get(i).Q = Util.nextDouble(Qmin, Qmax);
				
				for(int j = 0 ; j < task.getNumberOfDimensions() ; j++){
					population.get(i).v[j] += (population.get(i).getValue(j) - best.getValue(j)) * population.get(i).Q;
					S[j] =  population.get(i).getValue(j) + population.get(i).v[j];
				}
				
				sumA += population.get(i).Q;
				avgA = sumA / (i+1);
				
				if(Util.nextDouble() > r)
				{
					//Select a solution among the best solutions and generate a local solution around the best solution
					//TODO how to select from best solutions?
					for(int j = 0 ; j < task.getNumberOfDimensions() ; j++){
						S[j] = best.getValue(j) + 0.001 * (2 * Util.nextDouble() - 1);
						//S[j] = best.getValue(j) + 0.001 * population.get(i).A * (2 * Util.nextDouble() - 1);
						//S[j] = population.get(i).getValue(j) + Util.nextDouble() * avgA;
					}
				}
				
				S = task.setFeasible(S);
				if(task.isStopCriteria())
					break;
				BatSolution newBat = new BatSolution(task.eval(S));
				newBat.v = population.get(i).v;
				newBat.Q = population.get(i).Q;
				newBat.A = population.get(i).A;
				newBat.r = population.get(i).r;
				
				
				if(task.isFirstBetter(newBat, population.get(i)) && Util.nextDouble() < A)
				{
					//Update loudness and pulse rate
					newBat.A = alpha * newBat.A;
					newBat.r = newBat.r * (1 - Math.exp(-gamma * task.getNumberOfIterations()));
					//Replace old solution with new
					population.set(i, newBat);
				}
				
				if(task.isFirstBetter(newBat, best))
				{
					//System.out.println(best.getEval());
					best = new BatSolution(newBat);
				}
				
			}
			
		}
		
		return best;
	}

	private void initPopulation() throws StopCriteriaException {
		
		population = new ArrayList<BatSolution>();
		best = new BatSolution(task.getRandomSolution());
		best.v = new double[task.getNumberOfDimensions()];
		population.add(best);
		for (int i = 1; i < pop_size; i++) {
			
			BatSolution newSolution = new BatSolution(task.getRandomSolution());
			newSolution.v = new double[task.getNumberOfDimensions()];
			newSolution.A = Util.nextDouble(1,2);
			newSolution.r = Util.nextDouble();
			if(task.isFirstBetter(newSolution, best))
				best = new BatSolution(newSolution);
			
			population.add(newSolution);
			if (task.isStopCriteria())
				break;
		}
	}

	@Override
	public void resetDefaultsBeforNewRun() {

	}

}
