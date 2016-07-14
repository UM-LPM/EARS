//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.

package org.um.feri.ears.algorithms.moo.nsga2;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.operators.BinaryTournament2;
import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.operators.PolynomialMutation;
import org.um.feri.ears.operators.SBXCrossover;
import org.um.feri.ears.problems.MOTask;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.Cache;
import org.um.feri.ears.util.CrowdingComparator;
import org.um.feri.ears.util.Distance;
import org.um.feri.ears.util.Ranking;
import org.um.feri.ears.util.Util;


/** 
 *  Implementation of NSGA-II.
 *  This implementation of NSGA-II makes use of a QualityIndicator object
 *  to obtained the convergence speed of the algorithm. This version is used
 *  in the paper:
 *     A.J. Nebro, J.J. Durillo, C.A. Coello Coello, F. Luna, E. Alba 
 *     "A Study of Convergence Speed in Multi-Objective Metaheuristics." 
 *     To be presented in: PPSN'08. Dortmund. September 2008.
 */
public class NSGAII<T extends MOTask, Type extends Number> extends MOAlgorithm<T, Type> {

	int populationSize = 100;
	int num_var;
	int num_obj;

	ParetoSolution<Type> population;
	ParetoSolution<Type> offspringPopulation;
	ParetoSolution<Type> union;

	
	CrossoverOperator<Type, MOTask> cross;
	MutationOperator<Type, MOTask> mut;

	public NSGAII(CrossoverOperator crossover, MutationOperator mutation, int populationSize) {

		this.cross = crossover;
		this.mut = mutation;
		this.populationSize = populationSize;

		au = new Author("miha", "miha.ravber at gamil.com");
		ai = new AlgorithmInfo(
				"NSGAII",
				"\\bibitem{Deb2002}\nK.~Deb, S.~Agrawal, A.~Pratap, T.~Meyarivan\n\\newblock A fast and elitist multiobjective genetic algorithm: {NSGA-II}.\n\\newblock \\emph{IEEE Transactions on Evolutionary Computation}, 6(2):182--197, 2002.\n",
				"NSGAII", "Nondominated Sorting Genetic Algorithm II ");
		
		ai.addParameters(crossover.getOperatorParameters());
		ai.addParameters(mutation.getOperatorParameters());
		ai.addParameter(EnumAlgorithmParameters.POP_SIZE, populationSize+"");
	}

	@Override
	public ParetoSolution<Type> run(T taskProblem) throws StopCriteriaException {
		task = taskProblem;
		num_var = task.getDimensions();
		num_obj = task.getNumberOfObjectives();

		if(optimalParam)
		{
			switch(num_obj){
			case 1:
			{
				populationSize = 100;
				break;
			}
			case 2:
			{
				populationSize = 100;
				break;
			}
			case 3:
			{
				populationSize = 300;
				break;
			}
			default:
			{
				populationSize = 500;
				break;
			}
			}
		}
		
		ai.addParameter(EnumAlgorithmParameters.POP_SIZE, populationSize+"");

		if(caching != Cache.None && caching != Cache.Save)
		{
			ParetoSolution<Type> next = returnNext(task.taskInfo());
			if(next != null)
				return next;
			else
				System.out.println("No solution found in chache for algorithm: "+ai.getPublishedAcronym()+" on problem: "+task.getProblemName());
		}
		
		long initTime = System.currentTimeMillis();
		init();
		start();
		long estimatedTime = System.currentTimeMillis() - initTime;
		System.out.println("Total execution time: "+estimatedTime + "ms");
		
		// Return the first non-dominated front
		Ranking<Type> ranking = new Ranking<Type>(population);
		ParetoSolution<Type> best = ranking.getSubfront(0);
		if(save_data)
		{
			best.saveParetoImage(this.getAlgorithmInfo().getPublishedAcronym(),task.getProblemName());
			best.printFeasibleFUN("FUN_NSGAII");
			best.printVariablesToFile("VAR");
			best.printObjectivesToCSVFile("FUN");
		}
		if(display_data)
		{
			best.displayAllUnaryQulaityIndicators(task.getNumberOfObjectives(), task.getProblemFileName());
			best.displayData(this.getAlgorithmInfo().getPublishedAcronym(),task.getProblemName());
		}
		
		if(caching == Cache.Save)
		{
			Util.<Type>addParetoToJSON(getCacheKey(task.taskInfo()),ai.getPublishedAcronym(), best);
		}
		
		return best;
	}

	public void start() throws StopCriteriaException {
		Distance<Type> distance = new Distance<Type>();
		BinaryTournament2<Type> bt2 = new BinaryTournament2<Type>();

		// Create the initial population
		MOSolutionBase<Type> newSolution;
		for (int i = 0; i < populationSize; i++) {
			if (task.isStopCriteria())
				return;
			newSolution = task.getRandomMOIndividual();
			// problem.evaluateConstraints(newSolution);
			population.add(newSolution);
		}

		// Generations
		while (!task.isStopCriteria()) {
			// Create the offSpring solutionSet
			offspringPopulation = new ParetoSolution(populationSize);
			MOSolutionBase<Type>[] parents = new MOSolutionBase[2];

			for (int i = 0; i < (populationSize / 2); i++) {
				if (!task.isStopCriteria()) {
					// obtain parents
					parents[0] = bt2.execute(population);
					parents[1] = bt2.execute(population);
					MOSolutionBase<Type>[] offSpring = cross.execute(parents, task);
					
					mut.execute(offSpring[0], task);
					mut.execute(offSpring[1], task);
					if (task.isStopCriteria())
						break;
					task.eval(offSpring[0]);
					offspringPopulation.add(offSpring[0]);
					// problem.evaluateConstraints(offSpring[0]);
					if (task.isStopCriteria())
						break;
					task.eval(offSpring[1]);
					// problem.evaluateConstraints(offSpring[1]);
					offspringPopulation.add(offSpring[1]);
				}
			}

			// Create the solutionSet union of solutionSet and offSpring
			union = population.union(offspringPopulation);

			// Ranking the union
			Ranking<Type> ranking = new Ranking<Type>(union);

			int remain = populationSize;
			int index = 0;
			ParetoSolution<Type> front = null;
			population.clear();

			// Obtain the next front
			front = ranking.getSubfront(index);

			while ((remain > 0) && (remain >= front.size())) {
				// Assign crowding distance to individuals
				distance.crowdingDistanceAssignment(front, num_obj);
				// Add the individuals of this front
				for (int k = 0; k < front.size(); k++) {
					population.add(front.get(k));
				}

				// Decrement remain
				remain = remain - front.size();

				// Obtain the next front
				index++;
				if (remain > 0) {
					front = ranking.getSubfront(index);
				}
			}

			// Remain is less than front(index).size, insert only the best one
			if (remain > 0) { // front contains individuals to insert
				distance.crowdingDistanceAssignment(front, num_obj);
				front.sort(new CrowdingComparator());
				for (int k = 0; k < remain; k++) {
					population.add(front.get(k));
				}
				remain = 0;
			}
		}
	}

	protected void init() {
		population = new ParetoSolution<Type>(populationSize);
	}

	@Override
	public void resetDefaultsBeforNewRun() {

	}
}