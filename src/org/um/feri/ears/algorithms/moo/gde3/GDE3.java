//  GDE3.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package org.um.feri.ears.algorithms.moo.gde3;

import java.util.Comparator;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.DifferentialEvolutionCrossover;
import org.um.feri.ears.operators.DifferentialEvolutionSelection;
import org.um.feri.ears.problems.MOTask;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.CrowdingComparator;
import org.um.feri.ears.util.Distance;
import org.um.feri.ears.util.DominanceComparator;
import org.um.feri.ears.util.Ranking;

/**
 * This class implements the GDE3 algorithm. 
 */
public class GDE3<T extends MOTask, Type extends Number> extends MOAlgorithm<T, Type> {
    
	ParetoSolution<Type> population          ;
	ParetoSolution<Type> offspringPopulation ;
	ParetoSolution<Type> union               ;

	int num_var;
	int num_obj;
	
	int populationSize;
	
	CrossoverOperator<Type, MOTask> cross;

	public GDE3(CrossoverOperator crossover, int populationSize) {
		this.populationSize = populationSize;
		this.cross = crossover;

		au = new Author("miha", "miha.ravber at gamil.com");
		ai = new AlgorithmInfo(
				"GDE3",
				"\\bibitem{Kukkonen2009}\nS.~Kukkonen, J.~Lampinen\n\\newblock Performance Assessment of Generalized Differential Evolution 3 with a Given Set of Constrained Multi-Objective Test Problems.\n\\newblock \\emph{2009 IEEE Congress on Evolutionary Computation}, 1943--1950, 2009.\n",
				"GDE3", "Generalized Differential Evolution 3");
		ai.addParameter(EnumAlgorithmParameters.POP_SIZE, populationSize + "");
	}
  
  
	@Override
	public ParetoSolution<Type> run(T taskProblem) throws StopCriteriaException {

		task = taskProblem;
		num_var = task.getDimensions();
		num_obj = task.getNumberOfObjectives();

		long initTime = System.currentTimeMillis();
		init();
		start();
		long estimatedTime = System.currentTimeMillis() - initTime;
		System.out.println("Total execution time: "+estimatedTime + "ms");

		// Return the first non-dominated front
		Ranking ranking = new Ranking(population);
		ParetoSolution best = ranking.getSubfront(0);

		if(display_data)
		{
			best.displayAllUnaryQulaityIndicators(task.getProblem());
			best.displayData(this.getAlgorithmInfo().getPublishedAcronym(),task.getProblemShortName(), task.getProblem());
		}
		if(save_data)
		{
			best.saveParetoImage(this.getAlgorithmInfo().getPublishedAcronym(),task.getProblemShortName());
			best.printFeasibleFUN("FUN_GDE3");
			best.printVariablesToFile("VAR");
			best.printObjectivesToCSVFile("FUN");
		}
		return best;
	}

	private void init() {
		population = new ParetoSolution<Type>(populationSize);
	}

	@Override
	public void resetDefaultsBeforNewRun() {
	}
  
	/**
	 * Runs of the GDE3 algorithm.
	 * @return a <code>SolutionSet</code> that is a set of non dominated solutions as a result of the algorithm execution
	 * @throws StopCriteriaException
	 */
	public void start() throws StopCriteriaException {

		Distance<Type> distance;
		Comparator<MOSolutionBase<Type>> dominance;

		distance = new Distance();
		dominance = new DominanceComparator();

		MOSolutionBase<Type> parent[] = null;

		DifferentialEvolutionCrossover dec = new DifferentialEvolutionCrossover();
		DifferentialEvolutionSelection<Type> des = new DifferentialEvolutionSelection<Type>();

		// Create the initial solutionSet
		MOSolutionBase<Type> newSolution;
		for (int i = 0; i < populationSize; i++) {
			if (task.isStopCriteria())
				return;
			newSolution = new MOSolutionBase<Type>(task.getRandomMOIndividual());
			// problem.evaluateConstraints(newSolution);
			population.add(newSolution);
		}

		// Generations ...
		while (!task.isStopCriteria()) {
			// Create the offSpring solutionSet
			offspringPopulation = new ParetoSolution(populationSize * 2);

			for (int i = 0; i < populationSize; i++) {
				// Obtain parents. Two parameters are required: the population and the index of the current individual
				try {
					des.setCurrentIndex(i);
					parent = des.execute(population, task);
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("The population has less than four solutions");
					break;
				}

				MOSolutionBase<Type> child;
				// Crossover. Two parameters are required: the current
				// individual and the array of parents
				cross.setCurrentSolution(population.get(i));
				child = cross.execute(parent, task)[0];

				if (task.isStopCriteria())
					break;
				task.eval(child);

				// Dominance test
				int result;
				result = dominance.compare(population.get(i), child);
				if (result == -1) { // Solution i dominates child
					offspringPopulation.add(population.get(i));
				} else if (result == 1) { // child dominates
					offspringPopulation.add(child);
				} else { // the two solutions are non-dominated
					offspringPopulation.add(child);
					offspringPopulation.add(population.get(i));
				}
			}

			// Ranking the offspring population
			Ranking<Type> ranking = new Ranking<Type>(offspringPopulation);

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

			// remain is less than front(index).size, insert only the best one
			if (remain > 0) { // front contains individuals to insert
				while (front.size() > remain) {
					distance.crowdingDistanceAssignment(front, num_obj);
					front.remove(front.indexWorst(new CrowdingComparator()));
				}
				for (int k = 0; k < front.size(); k++) {
					population.add(front.get(k));
				}
				remain = 0;
			}
		}
	}
}
