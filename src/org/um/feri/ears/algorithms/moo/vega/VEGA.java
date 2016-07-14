/* Copyright 2009-2015 David Hadka
 *
 * This file is part of the MOEA Framework.
 *
 * The MOEA Framework is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * The MOEA Framework is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the MOEA Framework.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.um.feri.ears.algorithms.moo.vega;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.operators.PolynomialMutation;
import org.um.feri.ears.operators.SBXCrossover;
import org.um.feri.ears.operators.VEGASelection;
import org.um.feri.ears.problems.MOTask;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.Cache;
import org.um.feri.ears.util.ObjectiveComparator;
import org.um.feri.ears.util.Util;

/**
 * Implementation of the Vector Evaluated Genetic Algorithm (VEGA).  VEGA should
 * be avoided in practice, since many modern algorithms outperform it and
 * exhibit better convergence properties, but is included due to its historical
 * significance.  VEGA is considered the earliest MOEA.  It supports M
 * objectives during the selection phase by selecting M different subgroups,
 * each selected based on the i-th objective value, for i=1,...,M.
 * <p>
 * There is one small algorithmic difference between this implementation and
 * [1].  In [1], applying the genetic operators fills the entire population.
 * However, since custom variation operators can be specified, it is possible
 * that the population will not be filled completely.  As a result, this
 * implementation will continue selecting parents until the population is full.
 * <p>
 * References:
 * <ol>
 *   <li>Schaffer, D. (1985).  Multiple Objective Optimization with Vector
 *       Evaluated Genetic Algorithms.  Proceedings of the 1st International
 *       Conference on Genetic Algorithms, pp. 93-100.
 * </ol>
 */
public class VEGA<T extends MOTask, Type extends Number> extends MOAlgorithm<T, Type> {

	int populationSize;
	int num_var;
	int num_obj;
	ParetoSolution<Type> population;
	
	CrossoverOperator<Type, MOTask> cross;
	MutationOperator<Type, MOTask> mut;

	public VEGA(CrossoverOperator crossover, MutationOperator mutation, int pop_size) {
		this.populationSize = pop_size;
		
		this.cross = crossover;
		this.mut = mutation;

		au = new Author("miha", "miha.ravber at gamil.com");
		ai = new AlgorithmInfo(
				"VEGA",
				"\\bibitem{Schaffer1985}\nD.~Schaffer.\n\\newblock Multiple Objective Optimization with Vector Evaluated Genetic Algorithms.\n\\newblock \\emph{Proceedings of the 1st International Conference on Genetic Algorithms}, 93--100, 1985.\n",
				"VEGA", "Vector Evaluated Genetic Algorithm");
		ai.addParameters(crossover.getOperatorParameters());
		ai.addParameters(mutation.getOperatorParameters());
		ai.addParameter(EnumAlgorithmParameters.POP_SIZE, populationSize+"");
	}
	
	@Override
	public ParetoSolution<Type>  run(T taskProblem) throws StopCriteriaException {
		
		task = taskProblem;
		num_var = task.getDimensions();
		num_obj = task.getNumberOfObjectives();
		
		if(caching != Cache.None && caching != Cache.Save)
		{
			return returnNext(task.taskInfo());
		}
		
		long initTime = System.currentTimeMillis();
		init();
		start();
		long estimatedTime = System.currentTimeMillis() - initTime;
		System.out.println("Total execution time: "+estimatedTime + "ms");
		
		
		
		if(caching == Cache.Save)
		{
			Util.<Type>addParetoToJSON(getCacheKey(task.taskInfo()),ai.getPublishedAcronym(), population);
		}
		
		return population;
	}
	
	public void start() throws StopCriteriaException {
		
		// Create the initial solutionSet
		MOSolutionBase<Type> newSolution;
		for (int i = 0; i < populationSize; i++) {
			if (task.isStopCriteria())
				return;
			newSolution = new MOSolutionBase<Type>(task.getRandomMOIndividual());
			// problem.evaluateConstraints(newSolution);
			population.add(newSolution);
		}
		
		SBXCrossover sbx = new SBXCrossover();
		PolynomialMutation plm = new PolynomialMutation(1.0 / num_var, 20.0);
		
		do{
		// select the parents from the M different subgroups
			MOSolutionBase<Type>[] parents = performSelection(populationSize, population);
		
		// shuffle the parents
		Util.shuffle(parents);
		
		// loop until the next generation is filled
		int index = 0;
		boolean filled = false;
		
		population.clear();
		
		while (!filled) {
			
			MOSolutionBase<Type>[] offSpring = cross.execute(select(parents, index, 2), task);
			mut.execute(offSpring[0], task);
			mut.execute(offSpring[1], task);
			
			for (int i = 0; i < offSpring.length; i++) {
				population.add(offSpring[i]);
				
				if (population.size() >= populationSize) {
					filled = true;
					break;
				}
			}
			
			index += 2 % populationSize;
		}
		
		// evaluate the offspring
		for (MOSolutionBase<Type> ind : population.solutions) {
			if (task.isStopCriteria())
				return;
			task.eval(ind);
		}
		
	} while (!task.isStopCriteria());
	
	}
	
	private void init() {
		population = new ParetoSolution<Type>(populationSize);
	}

	@Override
	public void resetDefaultsBeforNewRun() {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Returns the subset of parents for the next variation operator.
	 * 
	 * @param parents all parents
	 * @param index the starting index
	 * @param size the size of the subset
	 * @return the subset of parents
	 */
	private MOSolutionBase<Type>[] select(MOSolutionBase<Type>[] parents, int index, int size) {
		MOSolutionBase<Type>[] result = new MOSolutionBase[size];
		
		for (int i = 0; i < size; i++) {
			result[i] = parents[(index+i) % parents.length];
		}
		
		return result;
	}

	/**
	 * VEGA selection operator that selects parents based on only one of the
	 * objectives.  
	 */
	
	private MOSolutionBase<Type>[] performSelection(int pop_size, ParetoSolution<Type> population)
	{
		VEGASelection<Type>[] selectors = new VEGASelection[num_obj];
		for (int i = 0; i < num_obj; i++) {
			selectors[i] = new VEGASelection<Type>(
					new ObjectiveComparator(i));
		}
		
		MOSolutionBase<Type>[] result = new MOSolutionBase[pop_size];
		
		for (int i = 0; i < pop_size; i++) {
			VEGASelection<Type> selector = selectors[i % num_obj];
			result[i] = new MOSolutionBase<Type>(selector.execute(population));
		}
		
		return result;
		
	}

}
