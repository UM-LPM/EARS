//  BinaryTournament2.java
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
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.comparator.SolutionDominanceComparator;
import org.um.feri.ears.util.random.RNG;


/**
 * This class implements an operator for binary selections using the same code
 * in Deb's NSGA-II implementation
 */
public class BinaryTournament2<N extends Number> {
  
  /**
   * dominance_ store the <code>Comparator</code> for check dominance_
   */
  private final SolutionDominanceComparator dominance = new SolutionDominanceComparator();
  
  /**
   * a stores a permutation of the solutions in the solutionSet used
   */
  private int[] a;
  
  /**
   *  index_ stores the actual index for selection
   */
  private int index = 0;
    
  /**
   * Constructor
   * Creates a new instance of the Binary tournament operator (Deb's
   * NSGA-II implementation version)
   */
  public BinaryTournament2()
  {

  }
    
  /**
  * Performs the operation
  * @param solution Object representing a SolutionSet
  * @return the selected solution
  */
	public NumberSolution<N> execute(ParetoSolution<N> solution) {
		ParetoSolution<N> population = solution;

		if (index == 0) // generate a permutation of indices
		{
			a = RNG.randomPermutation(population.size());
		}

		NumberSolution<N> solution1, solution2;
		solution1 = population.get(a[index]);
		if(index + 1 == population.size())
			solution2 = population.get(a[1]);
		else
			solution2 = population.get(a[index + 1]);

		index = (index + 2) % population.size();
		
		int flag = dominance.compare(solution1, solution2);
		if (flag == -1)
			return solution1;
		else if (flag == 1)
			return solution2;
		else if (solution1.getCrowdingDistance() > solution2.getCrowdingDistance())
			return solution1;
		else if (solution2.getCrowdingDistance() > solution1.getCrowdingDistance())
			return solution2;
		else if (RNG.nextDouble() < 0.5)
			return solution1;
		else
			return solution2;
	}
}
