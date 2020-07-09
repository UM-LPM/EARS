package org.um.feri.ears.algorithms.so.woa;

import java.math.BigDecimal;
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
	
	boolean useFakeGenerator = false;
	
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
	
	FakeRandomGenerator FakeGenerator;
	
	public WOA() {
		this(30);
	}
	
	public WOA(int popSize) {
		this(popSize, false, false);
	}
	
	public WOA(int pop_size, boolean useFakeRandom, boolean debug) {
		super();
		this.pop_size = pop_size;
		this.useFakeGenerator = useFakeRandom;
		setDebug(debug);
		
		au = new Author("janez", "janezk7@gmail.com");
		ai = new AlgorithmInfo("WOA", "mirjalili stuff article", "WOA", "Whale Optimization Algorithm");
		ai.addParameter(EnumAlgorithmParameters.POP_SIZE, pop_size + "");	
		
		// Initialize fake random generator
		FakeGenerator = new FakeRandomGenerator();
	}
	
	@Override
	public DoubleSolution execute(Task taskProblem) throws StopCriteriaException {
		task = taskProblem;
		
		initPopulation();
		
		//getHardcodedRastriginPopulation();
		int maxIt = 200;
		
		//bestSolution = population.get(0);
		updateBest();
		
		if(task.getStopCriteria() == EnumStopCriteria.ITERATIONS)
		{
			maxIt = task.getMaxIteratirons();
		}
		
		if(task.getStopCriteria() == EnumStopCriteria.EVALUATIONS)
		{
			maxIt = task.getMaxEvaluations() / pop_size;
		}
		
		if (debug)
			System.out.println("E: " + bestSolution.getEval());
		while(!task.isStopCriteria()) {
			a = 2.0 - task.getNumberOfIterations() * (2.0 / maxIt);
			a2= -1.0 + task.getNumberOfIterations() * ((-1.0) / maxIt);
			
			// For each search agent
			for(int index = 0; index < pop_size; index++ ){
				DoubleSolution CurrentAgent = population.get(index);
				double[] newPosition = new double[task.getNumberOfDimensions()];
				
				// Randoms for A and C 
				r1 = nextDouble();
				r2 = nextDouble();
				
				A = (2.0*a*r1)-a; // Random value on the interval of shrinking a 
				C = 2.0*r2;
				
				// Eq 2.5 parameters
				b = 1.0;
				l = (a2-1.0) * nextDouble() + 1.0;
				
				// Get p 
				p = nextDouble();
				
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
							int randAgentIndex = (int)(pop_size*nextDouble());//nextInt(0, pop_size-1);
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
						newPosition[i] = D_X_leader * Math.exp(b*l) * Math.cos(l*2.0*Math.PI) + bestSolution.getValue(i);
						
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
			if(debug)
				System.out.println(bestSolution.getEval());
			task.incrementNumberOfIterations();
		}
		
		return bestSolution;
	}
	
	private void getHardcodedSpherePopulation() throws StopCriteriaException {
		// Dimensions = 2
		// Popsize = 10
		population = new ArrayList<DoubleSolution>();
		population.add(task.eval(new double[] {62.9400, -68.4800}));
		population.add(task.eval(new double[] {81.1600, 94.1200}));
		population.add(task.eval(new double[] {-74.6000, 91.4300}));
		population.add(task.eval(new double[] {82.6800, -2.9200}));
		population.add(task.eval(new double[] {26.4700, 60.0600}));
		population.add(task.eval(new double[] {-80.4900, -71.6200}));
		population.add(task.eval(new double[] {-44.3000, -15.6500}));
		population.add(task.eval(new double[] {9.3800, 83.1500}));
		population.add(task.eval(new double[] {91.5000, 58.4400}));
		population.add(task.eval(new double[] {92.9800, 91.9000}));
	}
	
	private void getHardcodedRastriginPopulation() throws StopCriteriaException {
		// Dimensions = 2
		// Popsize = 10
		population = new ArrayList<DoubleSolution>();
		population.add(task.eval(new double[] {-3.34450060166138,	-2.13008302119204}));
		population.add(task.eval(new double[] {-1.11679690420495,	-0.699892016653105 }));
		population.add(task.eval(new double[] {3.39332856667207,	-4.96141183348717 }));
		population.add(task.eval(new double[] {3.10645137000899,	4.95681253764254 }));
		population.add(task.eval(new double[] {-4.50077512530029,	-3.40819548247392 }));
		population.add(task.eval(new double[] {-1.03160042891698,	-4.03234462793048 }));
		population.add(task.eval(new double[] {0.275208504404950,	-1.30652426183130 }));
		population.add(task.eval(new double[] {-0.851973448388742,	-3.09126755795994 }));
		population.add(task.eval(new double[] {1.60624528357076,	-0.105598586715915 }));
		population.add(task.eval(new double[] {1.31044719810667,	-1.64358744687864 }));
	}

	private void initPopulation() throws StopCriteriaException {
		int numberOfDimensions = task.getNumberOfDimensions();
		double[] lowerLimit = task.getLowerLimit();
		double[] upperLimit = task.getUpperLimit();
		population = new ArrayList<DoubleSolution>();
	
		for (int i = 0; i < pop_size; i++) {
			if(this.useFakeGenerator){
				// Generate solution via fake random generator
				double[] pos = new double[numberOfDimensions];
				for (int j = 0; j < numberOfDimensions; j++) {
					pos[j] = nextDouble(lowerLimit[j], upperLimit[j]);
				}
				DoubleSolution solution = task.eval(pos);
				population.add(solution);
			}
			else {
				// Generate random solution
				population.add(task.getRandomSolution());
			}
			
			if (task.isStopCriteria())
				break;
		}
	}
	
	/**
	 * Get random numbers
	 */
	
	private double nextDouble() {
		double r = this.useFakeGenerator ? 
				FakeGenerator.nextDouble() 
				: org.um.feri.ears.util.Util.nextDouble();
		return r;
	}
	
	private double nextDouble(double lowerBound, double upperBound) {
		double r = this.useFakeGenerator ? 
				FakeGenerator.nextDouble(lowerBound, upperBound) 
				: org.um.feri.ears.util.Util.nextDouble();
		return r;
	}
	
	private int nextInt(int lowerBound, int upperBound) {
		int r = this.useFakeGenerator ? 
				FakeGenerator.nextInt(lowerBound, upperBound) 
				: org.um.feri.ears.util.Util.nextInt(lowerBound, upperBound);
		return r;
	}
	
	/**
	 * Update best solution so far 
	 */
	private void updateBest() {
		ArrayList<DoubleSolution> popCopy = new ArrayList<DoubleSolution>(population);
		popCopy.sort(new TaskComparator(task));
		if(bestSolution == null || popCopy.get(0).getEval() < bestSolution.getEval())
			bestSolution = popCopy.get(0);
	}
	
	@Override
	public void resetToDefaultsBeforeNewRun() {
	}
}
