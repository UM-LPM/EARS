package org.um.feri.ears.algorithms.so.woa;

import java.util.ArrayList;
import java.util.Arrays;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.algorithms.so.jade.JADESolution;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.EnumStopCriteria;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.TaskComparator;
//import org.um.feri.ears.util.Util; // Random numbers
import org.um.feri.ears.util.FakeRandomGenerator; // Fake Random numbers

public class WOA extends Algorithm{
	DoubleSolution bestSolution;
	
	int pop_size;
	Task task;
	
	// Parameters
	double A;
	double C;
	double r1;
	double r2;
	
	double a; // Decreases linearly from 2 to 0 over iterations (Eq 2.3)
	double a2; // Linearly decreases from -1 to -2 to calculate t in Eq 3.12
	
	double b; // Parameter for Eq 2.5
	double l; // Parameter for Eq 2.5
	
	double p; // 50% whether to choose Shrinking encircling mechanism or the spiral model to update the position of whale
	
	ArrayList<DoubleSolution> population;
	
	FakeRandomGenerator Util;
	
	public WOA(boolean d)
	{
		this(30);
		setDebug(d);
	}
	
	public WOA(int pop_size)
	{
		super();
		this.pop_size = pop_size;
		
		au = new Author("janez", "janezk7@gmail.com");
		ai = new AlgorithmInfo("WOA", "mirjalili stuff article", "WOA", "Whale Optimization Algorithm");
		ai.addParameter(EnumAlgorithmParameters.POP_SIZE, pop_size + "");	
		
		Util = new FakeRandomGenerator();
	}
	
	@Override
	public DoubleSolution execute(Task taskProblem) throws StopCriteriaException {
		task = taskProblem;
		initPopulation();
		int maxIt = 10000;
		
		
		bestSolution = taskProblem.getRandomSolution();
		
		if(task.getStopCriteria() == EnumStopCriteria.ITERATIONS)
		{
			maxIt = task.getMaxIteratirons();
		}
		
		if(task.getStopCriteria() == EnumStopCriteria.EVALUATIONS)
		{
			maxIt = task.getMaxEvaluations() / pop_size;
		}
		
		if (debug)
			System.out.println(taskProblem.getNumberOfEvaluations() + " start " + bestSolution);
		while(!task.isStopCriteria()) {
			a = 2.0 - task.getNumberOfIterations() * (2.0 / maxIt);
			a2= -1 + task.getNumberOfIterations() * ((-1) / maxIt);
			
			// For each search agent
			for(int index = 0; index < pop_size; index++ )
			{
				DoubleSolution CurrentAgent = population.get(index);
				double[] newPosition = new double[task.getNumberOfDimensions()];
				
				
				// Randoms for A and C 
				r1 = Util.nextDouble();
				r2 = Util.nextDouble();
				
				A = (2*a*r1)-a; // Random value on the interval of shrinking a 
				C = 2*r2;
				
				// Eq 2.5 parameters
				b = 1;
				l = (a2-1) * (Util.nextDouble() + 1);
				
				// Get p 
				p = Util.nextDouble();
				
				// For each dimension 
				for(int i = 0; i < task.getNumberOfDimensions(); i++) 
				{
					if(p < 0.5)
					{
						// Shrinking encircling mechanism
						if(Math.abs(A) >= 1)
						{
							// Exploration
							// Select random agent and update position of current (Eq. 2.8)
							int randAgentIndex = Util.nextInt(0, pop_size-1);
							DoubleSolution X_rand = population.get(randAgentIndex);
							double D_X_rand = Math.abs(C * X_rand.getValue(i) - CurrentAgent.getValue(i));
							newPosition[i] = X_rand.getValue(i) - A * D_X_rand;
						}
						else if (Math.abs(A) < 1)
						{
							// Exploitation
							// Select best agent and Update position of current (Eq. 2.1)
							// Search in a shrinking (A) spiral. 
							double D_best = Math.abs(C * bestSolution.getValue(i)-CurrentAgent.getValue(i));
							newPosition[i] = bestSolution.getValue(i) - A * D_best;
						}
					}
					else
					{
						// Spiral model (Eq 2.5)
						double D_X_leader = Math.abs(bestSolution.getValue(i)-CurrentAgent.getValue(i));
						newPosition[i] = D_X_leader * Math.exp(b*l) * Math.cos(l*2*Math.PI) + bestSolution.getValue(i);
						
					}
				}
				
				newPosition = task.setFeasible(newPosition);
				
				if(task.isStopCriteria())
					break;
				
				DoubleSolution newWhale = task.eval(newPosition);
				population.set(index,  newWhale);
				
				// Check if the changed is better ? 
				//if(task.isFirstBetter(newWhale,  population.get(index)))
				//	population.set(index,  newWhale);
			}
			updateBest();
			task.incrementNumberOfIterations();
		}
		
		return bestSolution;
	}

	private void initPopulation() throws StopCriteriaException {
		population = new ArrayList<DoubleSolution>();
	
		for (int i = 0; i < pop_size; i++) {
			population.add(task.getRandomSolution());
			if (task.isStopCriteria())
				break;
		}
	}
	
	/**
	 * Update best solution so far 
	 */
	private void updateBest() {
		population.sort(new TaskComparator(task));
		bestSolution = population.get(0);
	}
	
	@Override
	public void resetDefaultsBeforNewRun() {
	}
}
