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

import java.util.Arrays;
import java.util.Iterator;

import org.apache.commons.math3.util.ArithmeticUtils;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.util.comparator.DominanceComparator;
import org.um.feri.ears.util.NondominatedPopulation;

/**
 * Adaptive grid archive. Divides objective space into a number of grid cells,
 * maintaining a count of the number of solutions within each grid cell. When
 * the size of the archive exceeds a specified capacity, a solution from the
 * most crowded grid cell is selected and removed from the archive.
 * <p>
 * References:
 * <ol>
 * <li>Knowles, J.D. and Corne, D.W., "Approximating the Nondominated Front
 * using the Pareto Archived Evolution Strategy," Evolutionary Computation, vol.
 * 8, no. 2, pp. 149-172, 2000.
 * <li>Knowles, J.D. and Corne, D.W., "Properties of an Adaptive Archiving for
 * Storing Nondominated Vectors," IEEE Transactions on Evolutionary Computation,
 * vol. 7, no. 2, pp. 100-116, 2003.
 * </ol>
 */
public class AdaptiveGridArchive<Type extends Number> extends NondominatedPopulation<Type> {

	/**
	 * The maximum capacity of this archive.
	 */
	protected final int capacity;

	/**
	 * The number of objectives of the problem .
	 */
	protected final int num_obj;

	/**
	 * The number of divisions this archive uses to split each objective.
	 */
	protected final int numberOfDivisions;

	/**
	 * The minimum objective value for each dimension.
	 */
	protected double[] minimum;

	/**
	 * The maximum objective value for each dimension.
	 */
	protected double[] maximum;

	/**
	 * The number of solutions in each grid cell.
	 */
	protected int[] density;

	/**
	 * Constructs an adaptive grid archive with the specified capacity with the
	 * specified number of divisions along each objective.
	 *
	 * @param capacity the capacity of this archive
	 * @param numObj the number of objectives
	 * @param numberOfDivisions the number of divisions this archive uses to
	 *        split each objective
	 */
	public AdaptiveGridArchive(int capacity, int numObj,
			int numberOfDivisions) {
		super(new DominanceComparator());
		this.capacity = capacity;
		this.numberOfDivisions = numberOfDivisions;
		this.num_obj = numObj;

		minimum = new double[numObj];
		maximum = new double[numObj];
		density = new int[ArithmeticUtils.pow(numberOfDivisions, numObj)];

		adaptGrid();
	}

	/**
	 * Returns the maximum number of solutions stored in this archive.
	 * 
	 * @return the maximum number of solutions stored in this archive
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * Returns the number of divisions this archive uses to split each
	 * objective.
	 * 
	 * @return the number of divisions this archive uses to split each objective
	 */
	public int getNumberOfDivisions() {
		return numberOfDivisions;
	}


	public boolean add(NumberSolution<Type> solution) {
		// check if the candidate dominates or is dominated by any member in
		// the archive
		Iterator<NumberSolution<Type>> iterator = iterator();

		while (iterator.hasNext()) {
			NumberSolution<Type> oldSolution = iterator.next();
			int flag = comparator.compare(solution, oldSolution);

			if (flag < 0) {
				// candidate dominates a member of the archive
				iterator.remove();
			} else if (flag > 0) {
				// candidate is dominated by a member of the archive
				return false;
			}
		}
		
		// if archive is empty, add the candidate
		if (isEmpty()) {
			super.forceAddWithoutCheck(solution);
			adaptGrid();
			return true;
		}
		
		// temporarily add the candidate solution
		super.forceAddWithoutCheck(solution);
		int index = findIndex(solution);
		
		if (index < 0) {
			adaptGrid();
			index = findIndex(solution);
		} else {
			density[index]++;
		}
		
		if (size() <= capacity) {
			// if archive is not exceeding capacity, keep the candidate
			return true;
		} else if (density[index] == density[findDensestCell()]) {
			// if the candidate is in the most dense cell, reject the candidate
			remove(solution);
			return false;
		} else {
			// otherwise keep the candidate and remove a solution from the most
			// dense cell
			remove(pickSolutionFromDensestCell());
			return true;
		}
	}

	@Override
	public void remove(int index) {
		int gridIndex = findIndex(get(index));

		super.remove(index);

		if (density[gridIndex] > 1) {
			density[gridIndex]--;
		} else {
			adaptGrid();
		}
	}
	
	@Override
	public boolean remove(NumberSolution<Type> solution) {
		boolean removed = super.remove(solution);

		if (removed) {
			int index = findIndex(solution);
			
			if (density[index] > 1) {
				density[index]--;
			} else {
				adaptGrid();
			}
		}

		return removed;
	}

	@Override
	public void clear() {
		super.clear();
		adaptGrid();
	}
	
	/**
	 * Returns the index of the grid cell with the largest density.
	 * 
	 * @return the index of the grid cell with the largest density
	 */
	protected int findDensestCell() {
		int index = -1;
		int value = -1;
		
		for (int i = 0; i < size(); i++) {
			int tempIndex = findIndex(get(i));
			int tempValue = density[tempIndex];
			
			if (tempValue > value) {
				value = tempValue;
				index = tempIndex;
			}
		}
		
		return index;
	}

	/**
	 * Returns a solution residing in the densest grid cell. If there are more
	 * than one such solution or multiple cells with the same density, the first
	 * solution encountered is returned.
	 * 
	 * @return a solution residing in the densest grid cell
	 */
	protected NumberSolution<Type> pickSolutionFromDensestCell() {
		NumberSolution<Type> solution = null;
		int value = -1;

		for (int i = 0; i < size(); i++) {
			int tempValue = density[findIndex(get(i))];

			if (tempValue > value) {
				solution = get(i);
				value = tempValue;
			}
		}

		return solution;
	}

	/**
	 * Computes new lower and upper bounds and recalculates the densities of
	 * each grid cell.
	 */
	protected void adaptGrid() {
		Arrays.fill(minimum, Double.POSITIVE_INFINITY);
		Arrays.fill(maximum, Double.NEGATIVE_INFINITY);
		Arrays.fill(density, 0);

		for (NumberSolution<Type> solution : this) {
			for (int i = 0; i < num_obj; i++) {
				minimum[i] = Math.min(minimum[i], solution.getObjective(i));
				maximum[i] = Math.max(maximum[i], solution.getObjective(i));
			}
		}

		for (NumberSolution<Type> solution : this) {
			density[findIndex(solution)]++;
		}
	}

	/**
	 * Returns the index of the specified solution in this adaptive grid
	 * archive, or {@code -1} if the solution is not within the current lower
	 * and upper bounds.
	 * 
	 * @param solution the specified solution
	 * @return the index of the specified solution in this adaptive grid
	 *         archive, or {@code -1} if the solution is not within the current
	 *         lower and upper bounds
	 */
	public int findIndex(NumberSolution<Type> solution) {
		int index = 0;

		for (int i = 0; i < num_obj; i++) {
			double value = solution.getObjective(i);

			if ((value < minimum[i]) || (value > maximum[i])) {
				return -1;
			} else {
				int tempIndex = (int)(numberOfDivisions * 
						((value - minimum[i]) / (maximum[i] - minimum[i])));

				// handle special case where value = maximum[i]
				if (tempIndex == numberOfDivisions) {
					tempIndex--;
				}

				index += tempIndex * ArithmeticUtils.pow(numberOfDivisions, i);
			}
		}

		return index;
	}
	
	/**
	 * Returns the density of the solution at the given index.
	 * 
	 * @param index the solution index
	 * @return the density of the solution at the given index
	 */
	public int getDensity(int index) {
		return density[index];
	}

}
