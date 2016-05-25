//Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU Lesser General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.
package org.um.feri.ears.algorithms.moo.nsga3;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.operators.BinaryTournament2;
import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.DifferentialEvolutionCrossover;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.operators.PolynomialMutation;
import org.um.feri.ears.operators.SBXCrossover;
import org.um.feri.ears.problems.DoubleMOTask;
import org.um.feri.ears.problems.MOTask;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.TaskBase;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.Cache;
import org.um.feri.ears.util.Distance;
import org.um.feri.ears.util.Ranking;
import org.um.feri.ears.util.Util;


/**
 * Created by ajnebro on 30/10/14.
 * Modified by Juanjo on 13/11/14
 *
 * This implementation is based on the code of Tsung-Che Chiang
 * http://web.ntnu.edu.tw/~tcchiang/publications/nsga3cpp/nsga3cpp.htm
 * 
 * Reference:
 * Deb, K., & Jain, H. (2014). An evolutionary many-objective optimization 
 * algorithm using reference-point-based nondominated sorting approach, part I: 
 * Solving problems with box constraints. 
 * Evolutionary Computation, IEEE Transactions on, 18(4), 577-601.
 * 
 */
public class NSGAIII<T extends MOTask, Type extends Number> extends MOAlgorithm<T, Type> {

	int populationSize;
	int num_var;
	int num_obj;

	double[][] lambda_; // reference points

	ParetoSolution<Type> population;
	ParetoSolution<Type> offspringPopulation;
	ParetoSolution<Type> union;
	
	protected Vector<Integer> numberOfDivisions  ;
	protected List<ReferencePoint<Type>> referencePoints;
	
	Distance distance;
	BinaryTournament2<Type> bt2 ;
	SBXCrossover sbx ;
	PolynomialMutation plm;
	
	CrossoverOperator<Type, MOTask> cross;
	MutationOperator<Type, MOTask> mut;


	public NSGAIII(CrossoverOperator crossover, MutationOperator mutation) {
		
		this.cross = crossover;
		this.mut = mutation;
		
		referencePoints = new Vector<>();
		au = new Author("miha", "miha.ravber at gamil.com");
		ai = new AlgorithmInfo(
				"NSGAIII",
				"\\bibitem{Deb2014}\nK.~Deb, H.~Jain\n\\newblock An evolutionary many-objective optimization algorithm using reference-point-based nondominated sorting approach, part I: Solving problems with box constraints.\n\\newblock \\emph{IEEE Transactions on Evolutionary Computation}, 18(4):577--601, 2014.\n",
				"NSGAIII", "Nondominated Sorting Genetic Algorithm III ");
	}

	@Override
	public ParetoSolution<Type> run(T taskProblem) throws StopCriteriaException {
		task = taskProblem;
		num_var = task.getDimensions();
		num_obj = task.getNumberOfObjectives();
		
		if(caching != Cache.None)
		{
			//TODO èe je seznam rešitev prazen za to rešitev ga naloži iz json in nastavi index na 0
			//metoda get random in metoda get next iz seznama rešitev 
			//metoda get hash saveID+="v"+this.version + this.getID()+task.getProblem().name +"v"+ task.getProblem().getVersion()+"obj"+num_obj +"var"+ num_var;
		}
		
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

		long initTime = System.currentTimeMillis();
		init();
		start();
		long estimatedTime = System.currentTimeMillis() - initTime;
		System.out.println("Total execution time: "+estimatedTime + "ms");

		// Return the first non-dominated front
		Ranking<Type> ranking = new Ranking<Type>(population);
		ParetoSolution best = ranking.getSubfront(0);
		if(save_data)
		{
			best.saveParetoImage(this.getAlgorithmInfo().getPublishedAcronym(),task.getProblemShortName());
			best.printFeasibleFUN("FUN_NSGAII");
			best.printVariablesToFile("VAR");
			best.printObjectivesToCSVFile("FUN");
		}
		if(display_data)
		{
			best.displayAllUnaryQulaityIndicators(task.getProblem());
			best.displayData(this.getAlgorithmInfo().getPublishedAcronym(),task.getProblemShortName(), task.getProblem());
		}
		
		if(caching == Cache.Save)
		{
			String saveID ="";
			saveID+="v"+this.version + this.getID()+task.getProblem().name +"v"+ task.getProblem().getVersion()+"obj"+num_obj +"var"+ num_var;
			Util.addParetoToJSON(saveID, best);
		}
		
		return best;
	}

	public void start() throws StopCriteriaException {
		// Create the initial population
		MOSolutionBase<Type> newSolution;
		for (int i = 0; i < populationSize; i++) {
			if (task.isStopCriteria())
				return;
			newSolution = new MOSolutionBase<Type>(task.getRandomMOIndividual());
			// problem.evaluateConstraints(newSolution);
			population.add(newSolution);
		}	
		
	    ParetoSolution<Type> offspringPopulation;
	    ParetoSolution<Type> matingPopulation;

		while (!task.isStopCriteria()) {
		      matingPopulation = selection(population);
		      offspringPopulation = reproduction(matingPopulation);
		      population = replacement(population, offspringPopulation);
		}
	}

	private ParetoSolution<Type> selection(ParetoSolution<Type> population) {
		
		ParetoSolution<Type> matingPopulation = new ParetoSolution(population.size()) ;
	    for (int i = 0; i < population.size(); i++) {
	      MOSolutionBase<Type> solution = bt2.execute(population);
	      matingPopulation.add(solution) ;
	    }
	    return matingPopulation;
	}
	
	protected ParetoSolution<Type> reproduction(ParetoSolution<Type> population) throws StopCriteriaException {
		ParetoSolution<Type> offspringPopulation = new ParetoSolution(population.size());
		for (int i = 0; i < population.size(); i+=2) {
			MOSolutionBase<Type>[] parents = new MOSolutionBase[2];
			parents[0] = population.get(i);
			parents[1] = (population.get(Math.min(i + 1, population.size()-1)));

			MOSolutionBase<Type>[] offspring = cross.execute(parents, task);

			mut.execute(offspring[0], task);
			mut.execute(offspring[1], task);

			if (task.isStopCriteria())
				break;
			task.eval(offspring[0]);
			if (task.isStopCriteria())
				break;
			task.eval(offspring[1]);
			
			offspringPopulation.add(offspring[0]);
			offspringPopulation.add(offspring[1]);
		}
		return offspringPopulation;
	}
	
	protected ParetoSolution<Type> replacement(ParetoSolution<Type> population, ParetoSolution<Type> offspringPopulation) {

		ParetoSolution<Type> jointPopulation = new ParetoSolution(population.getCapacity() + offspringPopulation.getCapacity());
		jointPopulation.addAll(population) ;
		jointPopulation.addAll(offspringPopulation) ;

		Ranking<Type> ranking = new Ranking<Type>(jointPopulation);


		List<MOSolutionBase<Type>> pop = new ArrayList<>();
		List<List<MOSolutionBase<Type>>> fronts = new ArrayList<>();
		int rankingIndex = 0;
		int candidateSolutions = 0;
		while (candidateSolutions < populationSize) {
			fronts.add(ranking.getSubfront(rankingIndex).solutions);
			candidateSolutions += ranking.getSubfront(rankingIndex).size();
			if ((pop.size() + ranking.getSubfront(rankingIndex).size()) <= populationSize)
				addRankedSolutionsToPopulation(ranking, rankingIndex, pop);
			rankingIndex++;
		}

		// A copy of the reference list should be used as parameter of the environmental selection
		EnvironmentalSelection<Type> selection = new EnvironmentalSelection(fronts, populationSize, getReferencePointsCopy(), num_obj);

		pop = selection.execute(pop);

		return new ParetoSolution(pop);
	}
	
	private void addRankedSolutionsToPopulation(Ranking<Type> ranking, int rank, List<MOSolutionBase<Type>> population) {
		List<MOSolutionBase<Type>> front ;

		front = ranking.getSubfront(rank).solutions;

		for (int i = 0 ; i < front.size(); i++) {
			population.add(front.get(i));
		}
	}
	
	private List<ReferencePoint<Type>> getReferencePointsCopy() {
		List<ReferencePoint<Type>> copy = new ArrayList<>();
		for (ReferencePoint<Type> r : this.referencePoints) {
			copy.add(new ReferencePoint(r));
		}
		return copy;
	}

	private void init() {
		
		distance = new Distance();
		bt2 = new BinaryTournament2<Type>();
		sbx = new SBXCrossover(0.9, 20.0);
		plm = new PolynomialMutation(1.0 / num_var, 20.0);
		referencePoints.clear();
		switch (num_obj) {
		case 2:
			numberOfDivisions = new Vector<>(1) ;
			numberOfDivisions.add(99);
			break;
		case 3:
			numberOfDivisions = new Vector<>(1) ;
			numberOfDivisions.add(12);
			break;
		case 5:
			numberOfDivisions = new Vector<>(1) ;
			numberOfDivisions.add(6);
			break;
		case 8:
			numberOfDivisions = new Vector<>(3) ;
			numberOfDivisions.add(2);
			break;
		case 10:
			numberOfDivisions = new Vector<>(3) ;
			numberOfDivisions.add(2);
			break;
		case 15:
			numberOfDivisions = new Vector<>(2) ;
			numberOfDivisions.add(1);
			break;
			
		default:
			numberOfDivisions = new Vector<>(1) ;
			numberOfDivisions.add(12);
			break;
		}

	    (new ReferencePoint()).generateReferencePoints(referencePoints, num_obj , numberOfDivisions);
	    System.out.println(populationSize);
	    populationSize = referencePoints.size();
	    while (populationSize%4>0) {
	      populationSize++;
	    }

	    population = new ParetoSolution(populationSize);
	}
	
	

	@Override
	public void resetDefaultsBeforNewRun() {

	}
}
