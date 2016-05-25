//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.

package org.um.feri.ears.algorithms.moo.pesa2;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.operators.PESA2Selection;
import org.um.feri.ears.operators.PolynomialMutation;
import org.um.feri.ears.operators.SBXCrossover;
import org.um.feri.ears.problems.MOTask;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.ParetoSolution;
public class PESAII<T extends MOTask, Type extends Number> extends MOAlgorithm<T, Type>{

	int populationSize = 100;
	int archiveSize = 100;
	int bisections = 5;
	ParetoSolution<Type> population;
	AdaptiveGridArchive<Type> archive;
	int num_var;
	int num_obj;
	
	CrossoverOperator<Type, MOTask> cross;
	MutationOperator<Type, MOTask> mut;
	
	public PESAII(CrossoverOperator crossover, MutationOperator mutation, int populationSize, int archiveSize) {
		this.populationSize = populationSize;
		this.archiveSize = archiveSize;
		
		this.cross = crossover;
		this.mut = mutation;

		au = new Author("miha", "miha.ravber at gamil.com");
		ai = new AlgorithmInfo(
				"PESAII",
				"\\bibitem{corne2001}\nD.W.~Corne,N.R.~Jerram,J.D.~Knowles,M.J.~Oates\n\\newblock PESA-II: Region-based Selection in Evolutionary Multiobjective Optimization.\n\\newblock \\emph{Proceedings of the Genetic and Evolutionary Computation Conference (GECCO-2001)}, 283--290, 2001.\n",
				"PESAII", "Pareto Envelope-Based Selection Algorithm");
		ai.addParameter(EnumAlgorithmParameters.POP_SIZE, populationSize + "");
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
				archiveSize = 100;
				break;
			}
			case 2:
			{
				populationSize = 100;
				archiveSize = 100;
				break;
			}
			case 3:
			{
				populationSize = 300;
				archiveSize = 300;
				break;
			}
			default:
			{
				populationSize = 500;
				archiveSize = 500;
				break;
			}
			}
		}

		long initTime = System.currentTimeMillis();
		init();
		start();
		long estimatedTime = System.currentTimeMillis() - initTime;
		System.out.println("Total execution time: "+estimatedTime + "ms");
		
		if(display_data)
		{
			archive.displayAllUnaryQulaityIndicators(task.getProblem());
			archive.displayData(this.getAlgorithmInfo().getPublishedAcronym(),task.getProblemShortName(), task.getProblem());
		}
		if(save_data)
		{
			archive.saveParetoImage(this.getAlgorithmInfo().getPublishedAcronym(),task.getProblemShortName());
			archive.printFeasibleFUN("FUN_PESAII");
			archive.printVariablesToFile("VAR");
			archive.printObjectivesToCSVFile("FUN");
		}

		return archive;
	}

	@Override
	public void resetDefaultsBeforNewRun() {
	}

	private void init() {
		archive = new AdaptiveGridArchive<Type>(archiveSize, bisections, num_obj);
		population = new ParetoSolution<Type>(populationSize);
	}

	public void start() throws StopCriteriaException {

		SBXCrossover sbx = new SBXCrossover(0.9, 20.0);
		PolynomialMutation plm = new PolynomialMutation(1.0 / num_var, 20.0);
		PESA2Selection<Type> selection = new PESA2Selection<Type>();

		// Create the initial individual and evaluate it and his constraints
		for (int i = 0; i < populationSize; i++) {
			if (task.isStopCriteria())
				return;
			MOSolutionBase<Type> solution = new MOSolutionBase<Type>(task.getRandomMOIndividual());
			// problem.evaluateConstraints(solution);
			population.add(solution);
		}

		// Incorporate non-dominated solution to the archive
		for (int i = 0; i < population.size(); i++) {
			archive.add(population.get(i)); // Only non dominated are accepted by the archive
		}

		// Clear the init solutionSet
		population.clear();

		// Iterations....
		MOSolutionBase<Type>[] parents = new MOSolutionBase[2];

		do {
			// -> Create the offSpring solutionSet
			while (population.size() < populationSize) {
				parents[0] = selection.execute(archive);
				parents[1] = selection.execute(archive);

				MOSolutionBase<Type>[] offSpring = cross.execute(parents, task);
				mut.execute(offSpring[0], task);
				if (task.isStopCriteria())
					break;
				task.eval(offSpring[0]);
				// problem.evaluateConstraints(offSpring[0]);
				population.add(offSpring[0]);
			}

			for (int i = 0; i < population.size(); i++)
				archive.add(population.get(i));

			// Clear the solutionSet
			population.clear();

		} while (!task.isStopCriteria());
	}
}
