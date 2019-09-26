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
package org.um.feri.ears.algorithms.moo.pesa2MOEA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import org.apache.commons.math3.util.ArithmeticUtils;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.operators.PolynomialMutation;
import org.um.feri.ears.operators.SBXCrossover;
import org.um.feri.ears.problems.MOTask;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.Cache;
import org.um.feri.ears.util.Util;

/**
 * Implementation of the Pareto Envelope-based Selection Algorithm (PESA2).
 * <p>
 * References:
 * <ol>
 *   <li>Corne, D. W., N. R. Jerram, J. D. Knowles, and M. J. Oates (2001).
 *       PESA-II: Region-based Selection in Evolutionary Multiobjective
 *       Optimization.  Proceedings of the Genetic and Evolutionary
 *       Computation Conference (GECCO 2001), pp. 283-290.
 *   <li>Corne, D. W., J. D. Knowles, and M. J. Oates (2000).  The Pareto
 *       Envelope-based Selection Algorithm for Multiobjective Optimization.
 *       Parallel Problem Solving from Nature PPSN VI, pp. 839-848.
 * </ol>
 */
// Very slow
public class PESA2<T extends MOTask, Type extends Number> extends MOAlgorithm<T, Type>{

	int populationSize = 100;
	int archiveSize = 100;
	int bisections = 8;
	ParetoSolution<Type> population;
	AdaptiveGridArchive<Type> archive;
	
	/**
	 * A mapping from grid index to the solutions occupying that grid index.
	 * This enables PESA2's region-based selection.
	 */
	protected Map<Integer, List<MOSolutionBase<Type>>> gridMap;
	
	CrossoverOperator<Type, T, MOSolutionBase<Type>> cross;
	MutationOperator<Type, T, MOSolutionBase<Type>> mut;

	public PESA2(CrossoverOperator crossover, MutationOperator mutation, int populationSize) {
		this.populationSize = populationSize;
		
		this.cross = crossover;
		this.mut = mutation;

		au = new Author("miha", "miha.ravber at gamil.com");
		ai = new AlgorithmInfo(
				"PESA2",
				"\\bibitem{corne2001}\nD.W.~Corne,N.R.~Jerram,J.D.~Knowles,M.J.~Oates\n\\newblock PESA-II: Region-based Selection in Evolutionary Multiobjective Optimization.\n\\newblock \\emph{Proceedings of the Genetic and Evolutionary Computation Conference (GECCO-2001)}, 283--290, 2001.\n",
				"PESA2", "Pareto Envelope-Based Selection Algorithm");
		ai.addParameters(crossover.getOperatorParameters());
		ai.addParameters(mutation.getOperatorParameters());
		ai.addParameter(EnumAlgorithmParameters.POP_SIZE, populationSize+"");
		ai.addParameter(EnumAlgorithmParameters.ARCHIVE_SIZE, archiveSize+"");
	}
	
	@Override
	protected void start() throws StopCriteriaException {
		
		RegionBasedSelection selection = new RegionBasedSelection();
		
		// Create the initial individual and evaluate it and his constraints
		for (int i = 0; i < populationSize; i++) {
			if (task.isStopCriteria())
				return;
			MOSolutionBase<Type> solution = new MOSolutionBase<Type>(task.getRandomMOSolution());
			// problem.evaluateConstraints(solution);
			population.add(solution);
		}
		
		
		if (archive != null) {
			archive.addAll(population);
		}
		
		do {
			// clear the population; selection draws from archive only
			population.clear();
			
			// generate the mapping between grid indices and solutions
			gridMap = createGridMap();
	
			while (population.size() < populationSize) {
				MOSolutionBase<Type>[] parents = selection.select(2, archive);
				MOSolutionBase<Type>[] offSpring = cross.execute(parents, task);
				mut.execute(offSpring[0], task);
				if (task.isStopCriteria())
					break;
				task.eval(offSpring[0]);
				// problem.evaluateConstraints(offSpring[0]);
				population.add(offSpring[0]);
			}
			
			archive.addAll(population);
			task.incrementNumberOfIterations();
		} while (!task.isStopCriteria());
		
		best = archive;
	}
	
	/**
	 * Returns a mapping from grid index to the solutions occupying that grid
	 * index.  The key is the grid index, and the value is the list of solutions
	 * occupying that index.
	 * 
	 * @return a mapping from grid index to the solutions occupying that grid
	 *         index
	 */
	protected Map<Integer, List<MOSolutionBase<Type>>> createGridMap() {
		Map<Integer, List<MOSolutionBase<Type>>> result = new HashMap<Integer, List<MOSolutionBase<Type>>>();
		
		for (MOSolutionBase<Type> solution : archive) {
			int index = archive.findIndex(solution);
			List<MOSolutionBase<Type>> solutions = result.get(index);
			
			if (solutions == null) {
				solutions = new ArrayList<MOSolutionBase<Type>>();
				result.put(index, solutions);
			}
			
			solutions.add(solution);
		}
		
		return result;
	}
	
	/**
	 * Region-based selection.  Instead of selecting individual solutions,
	 * PESA2 first selects hyperboxes using binary tournament selection to
	 * favor hyperboxes with lower density.  Then, one solution from the
	 * selected hyperbox is returned.
	 */
	public class RegionBasedSelection {
		
		/**
		 * Constructs a new region-based selection instance.
		 */
		public RegionBasedSelection() {
			super();
		}

		public MOSolutionBase<Type>[] select(int arity, ParetoSolution<Type> population) {
			MOSolutionBase<Type>[] result = new MOSolutionBase[arity];
			
			for (int i = 0; i < arity; i++) {
				result[i] = select();
			}
			
			return result;
		}
		
		/**
		 * Draws a random entry from the map.
		 * 
		 * @return the randomly selected map entry
		 */
		protected Entry<Integer, List<MOSolutionBase<Type>>> draw() {
			int index = Util.nextInt(gridMap.size());
			Iterator<Entry<Integer, List<MOSolutionBase<Type>>>> iterator = gridMap.entrySet().iterator();
			
			while (iterator.hasNext()) {
				Entry<Integer, List<MOSolutionBase<Type>>> entry = iterator.next();
				
				if (index == 0) {
					return entry;
				} else {
					index--;
				}
			}
			
			throw new NoSuchElementException();
		}
		
		/**
		 * Selects one solution using PESA2's region-based selection scheme.
		 * 
		 * @return the selected solution
		 */
		protected MOSolutionBase<Type> select() {
			Entry<Integer, List<MOSolutionBase<Type>>> entry1 = draw();
			Entry<Integer, List<MOSolutionBase<Type>>> entry2 = draw();
			Entry<Integer, List<MOSolutionBase<Type>>> selection = entry1;
			
			// pick the grid index with smaller density
			if (entry1 != entry2) {
				if ((archive.getDensity(entry2.getKey()) < archive.getDensity(entry1.getKey())) ||
						(archive.getDensity(entry2.getKey()) == archive.getDensity(entry1.getKey()) && Util.nextBoolean())) {	
					selection = entry2;
				}
			}
			
			// randomly pick a solution from the selected grid index
			return Util.nextItem(selection.getValue());
		}
		
	}
	
	@Override
	protected void init() {
		
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
		
		ai.addParameter(EnumAlgorithmParameters.POP_SIZE, populationSize+"");
		ai.addParameter(EnumAlgorithmParameters.ARCHIVE_SIZE, archiveSize+"");
		
		archive = new AdaptiveGridArchive<Type>(archiveSize, num_obj, ArithmeticUtils.pow(2, bisections));
		population = new ParetoSolution<Type>(populationSize);
	}


	@Override
	public void resetToDefaultsBeforeNewRun() {
		
	}

}
