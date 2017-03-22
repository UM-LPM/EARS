package org.um.feri.ears.algorithms.so.cs;

import java.util.ArrayList;

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

public class CS extends Algorithm{
	
	DoubleSolution best;
	
	int pop_size;
	Task task;
	double pa = 0.25; //Discovery rate of alien eggs/solutions

	ArrayList<DoubleSolution> nest;
	
	public CS()
	{
		this(25);
	}
	
	public CS(int pop_size)
	{
		super();
		this.pop_size = pop_size;
		
		au = new Author("miha", "miha.ravber@um.si");
		ai = new AlgorithmInfo("CS",
				"@inproceedings{yang2009cuckoo,"
				+ "title={Cuckoo search via L{\'e}vy flights},"
				+ "author={Yang, Xin-She and Deb, Suash},"
				+ "booktitle={Nature & Biologically Inspired Computing, 2009. NaBIC 2009. World Congress on},"
				+ "pages={210--214},"
				+ "year={2009},"
				+ "organization={IEEE}}",
				"CS", "Cuckoo Search");
		ai.addParameter(EnumAlgorithmParameters.POP_SIZE, pop_size + "");
	}

	@Override
	public DoubleSolution execute(Task taskProblem) throws StopCriteriaException {
		task = taskProblem;
		initPopulation();
	
		while (!task.isStopCriteria()) {

			//Generate new solutions (but keep the current best)
			getCuckoos();
			//System.out.println(best.getEval());
			
			task.incrementNumberOfIterations();
			
			//Discovery and randomization
			emptyNests();

			task.incrementNumberOfIterations();
		}
		return best;
	}
	
	private void setBest(ArrayList<DoubleSolution> offspringPopulation) {
		
		for(int i = 0; i < pop_size; i++)
		{
			if(i >= offspringPopulation.size())
				return;
			
			if(task.isFirstBetter(offspringPopulation.get(i),nest.get(i)))
			{
				nest.set(i, offspringPopulation.get(i));
			}
				
			if(task.isFirstBetter(offspringPopulation.get(i), best))
			{
				best = new DoubleSolution(offspringPopulation.get(i));
			}
		}
	}

	/**
	 *  Replace some nests by constructing new solutions/nests
	 * @throws StopCriteriaException 
	 */
	private void emptyNests() throws StopCriteriaException {
		
		//A fraction of worse nests are discovered with probability pa
		ArrayList<DoubleSolution> offspringPopulation = new ArrayList<DoubleSolution>();
		
		int[] per1 = Util.randomPermutation(pop_size);
		int[] per2 = Util.randomPermutation(pop_size);
		
		double stepsize;
		for (int i = 0; i < pop_size; i++) {
			
			if(Util.nextDouble() > pa)
			{
				double[] newSolution = new double[task.getNumberOfDimensions()];
				for (int j = 0; j < task.getNumberOfDimensions(); j++) {
					stepsize = Util.nextDouble() * (nest.get(per1[i]).getValue(j) - (nest.get(per2[i]).getValue(j)));
					newSolution[j] = nest.get(i).getValue(j) + stepsize;
				}
				newSolution = task.setFeasible(newSolution);
				if(task.isStopCriteria())
					break;
				DoubleSolution newC = task.eval(newSolution);
				offspringPopulation.add(newC);
			}
			else
			{
				offspringPopulation.add(nest.get(i));
			}
		}

		setBest(offspringPopulation);
	}

	/**
	 * Get cuckoos by random walk
	 * @throws StopCriteriaException 
	 */
	private void getCuckoos() throws StopCriteriaException {
		
		//Levy flights
		//Levy exponent and coefficient
		//For details, see equation (2.21), Page 16 (chapter 2) of the book
		//X. S. Yang, Nature-Inspired Metaheuristic Algorithms, 2nd Edition, Luniver Press, (2010).
		
		ArrayList<DoubleSolution> offspringPopulation = new ArrayList<DoubleSolution>();
		double beta = 3.0/2.0;
		double sigma = Math.pow((Gamma.gamma(1+beta)*Math.sin(Math.PI*beta/2)/(Gamma.gamma((1+beta)/2) * beta * Math.pow(2, (beta-1)/2) )),(1/beta));
		
		for (int i = 0; i < pop_size; i++) {
			
			DoubleSolution s = nest.get(i);
			double u,v,step,stepsize;
			double[] newSolution = new double[task.getNumberOfDimensions()];
			for (int j = 0; j < task.getNumberOfDimensions(); j++) {
				u = Util.nextDouble() * sigma;
				v = Util.nextDouble();
				
				step = u / Math.pow(Math.abs(v), 1 / beta);
				
				stepsize = 0.01 * step * (s.getValue(j) - best.getValue(j));
				newSolution[j] = s.getValue(j) + stepsize * Util.nextDouble();
			}
			
			newSolution = task.setFeasible(newSolution);
			if(task.isStopCriteria())
				break;
			DoubleSolution newC = task.eval(newSolution);
			offspringPopulation.add(newC);
		}
		setBest(offspringPopulation);
	}

	private void initPopulation() throws StopCriteriaException {
		nest = new ArrayList<DoubleSolution>();
	
		for (int i = 0; i < pop_size; i++) {
			nest.add(task.getRandomSolution());
			if (task.isStopCriteria())
				break;
		}
		nest.sort(new TaskComparator(task));
		best = nest.get(0);
	}

	@Override
	public void resetDefaultsBeforNewRun() {
	}

}
