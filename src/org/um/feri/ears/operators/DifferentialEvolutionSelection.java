//  DifferentialEvolutionSelection.java
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

package org.um.feri.ears.operators;

import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.random.RNG;

/**
 * Class implementing the selection operator used in DE: three different
 * solutions are returned from a population.
 */
public class DifferentialEvolutionSelection<N extends Number> implements SelectionOperator<NumberSolution<N>[], ParetoSolution<N>, Problem<NumberSolution<N>>>{

	private int currentIndex;

	public DifferentialEvolutionSelection() {
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	/**
	 * Executes the operation
	 * @param population containing the population and the position (index) of the current individual
	 * @return An object containing the three selected parents
	 */
	@Override
	public NumberSolution<N>[] execute(ParetoSolution<N> population, Problem<NumberSolution<N>> problem) {

		int index = currentIndex;
		NumberSolution<N>[] parents = new NumberSolution[3];
		int r1, r2, r3;

		if (population.size() < 4)
		{
			System.err.println("DifferentialEvolutionSelection: the population has less than four solutions");
			return null;
		}

		do {
			r1 = RNG.nextInt(population.size());
		} while (r1 == index);
		do {
			r2 = RNG.nextInt(population.size());
		} while (r2 == index || r2 == r1);
		do {
			r3 = RNG.nextInt(population.size());
		} while (r3 == index || r3 == r1 || r3 == r2);

		parents[0] = population.get(r1);
		parents[1] = population.get(r2);
		parents[2] = population.get(r3);

		return parents;
	}
}
