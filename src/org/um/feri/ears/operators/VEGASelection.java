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
package org.um.feri.ears.operators;

import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.comparator.ObjectiveComparator;
import org.um.feri.ears.util.random.RNG;

public class VEGASelection<N extends Number> {
	
	private final ObjectiveComparator<N> comparator;
	
	/**
	 * The tournament size. This is the number of solutions sampled from which
	 * the tournament winner is selected.
	 */
	private int size;

	/**
	 * Constructs a binary tournament selection operator using the specified
	 * dominance comparator.
	 * 
	 * @param comparator the comparator used to determine the tournament winner
	 */
	public VEGASelection(ObjectiveComparator<N>  comparator) {
		this(2, comparator);
	}

	/**
	 * Constructs a tournament selection operator of the specified size and
	 * using the specified dominance comparator.
	 * 
	 * @param size the tournament size
	 * @param comparator the comparator used to determine the tournament winner
	 */
	public VEGASelection(int size, ObjectiveComparator<N>  comparator) {
		this.size = size;
		this.comparator = comparator;
	}
	
	/**
	 * Performs deterministic tournament selection with the specified
	 * population, returning the tournament winner. If more than one solution is
	 * a winner, one of the winners is returned with equal probability.
	 * 
	 * @param coralReef the population from which candidate solutions are
	 *        selected
	 * @return the winner of tournament selection
	 */
	public NumberSolution<N> execute(Object object) {
		ParetoSolution<N> population = (ParetoSolution<N>) object;
		NumberSolution<N> winner = population.get(RNG.nextInt(population.size()));

		for (int i = 1; i < size; i++) {
			NumberSolution<N> candidate = population
					.get(RNG.nextInt(population.size()));

			int flag = comparator.compare(winner, candidate);

			if (flag > 0) {
				winner = candidate;
			}
		}

		return winner;
	}
	

}
