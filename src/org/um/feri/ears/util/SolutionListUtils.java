//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU Lesser General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU Lesser General Public License for more details.
//
//You should have received a copy of the GNU Lesser General Public License
//along with this program.  If not, see <http://www.gnu.org/licenses/>.

package org.um.feri.ears.util;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.ParetoSolution;

/**
 * Created by Antonio J. Nebro on 04/10/14.
 * Modified by Juanjo 13/03/15
 */
public class SolutionListUtils {

	public static <T extends Number> ParetoSolution<T> getNondominatedSolutions(ParetoSolution<T> solutionList) {
		Ranking<T> ranking = new Ranking<T>(solutionList) ;
		return ranking.getSubfront(0);
	}

	public <T extends Number> MOSolutionBase<T> findWorstSolution(ParetoSolution<T> solutionList, Comparator comparator) {
		if ((solutionList == null) || (solutionList.isEmpty())) {
			throw new IllegalArgumentException("No solution provided: "+solutionList);
		}

		MOSolutionBase<T> worstKnown = solutionList.iterator().next();
		for (MOSolutionBase<T> candidateSolution : solutionList) {
			if (comparator.compare(worstKnown, candidateSolution) < 0) {
				worstKnown = candidateSolution;
			}
		}

		return worstKnown;
	}

	/**
	 * Finds the index of the best solution in the list according to a comparator
	 * @param solutionList
	 * @param comparator
	 * @return The index of the best solution
	 */
	public static <T extends Number> int findIndexOfBestSolution(ParetoSolution<T> solutionList, Comparator comparator) {

		int index = 0;
		MOSolutionBase<T> bestKnown = solutionList.get(0) ;
		MOSolutionBase<T> candidateSolution ;

		int flag;
		for (int i = 1; i < solutionList.size(); i++) {
			candidateSolution = solutionList.get(i);
			flag = comparator.compare(bestKnown, candidateSolution);
			if (flag == 1) {
				index = i;
				bestKnown = candidateSolution;
			}
		}

		return index;
	}

	/**
	 * Finds the index of the worst solution in the list according to a comparator
	 * @param solutionList
	 * @param comparator
	 * @return The index of the best solution
	 */
	public static <T extends Number> int findIndexOfWorstSolution(ParetoSolution<T> solutionList, Comparator comparator) {

		int index = 0;
		MOSolutionBase<T> worstKnown = solutionList.get(0) ;
		MOSolutionBase<T> candidateSolution ;

		int flag;
		for (int i = 1; i < solutionList.size(); i++) {
			candidateSolution = solutionList.get(i);
			flag = comparator.compare(worstKnown, candidateSolution);
			if (flag == -1) {
				index = i;
				worstKnown = candidateSolution;
			}
		}

		return index;
	}

	public static <T extends Number> MOSolutionBase<T> findBestSolution(ParetoSolution<T> solutionList, Comparator comparator) {
		return solutionList.get(findIndexOfBestSolution(solutionList, comparator)) ;
	}

	public static double[][] writeObjectivesToMatrix(ParetoSolution solutionList) {
		if (solutionList.size() == 0) {
			return new double[0][0];
		}

		int numberOfObjectives = solutionList.get(0).numberOfObjectives();
		int solutionListSize = solutionList.size();

		double[][] objectives;
		objectives = new double[solutionListSize][numberOfObjectives];
		for (int i = 0; i < solutionListSize; i++) {
			for (int j = 0; j < numberOfObjectives; j++) {
				objectives[i][j] = solutionList.get(i).getObjective(j);
			}
		}
		return objectives;
	}

	/**
	 * This method receives a list of non-dominated solutions and maximum and minimum values of the
	 * objectives, and returns a the normalized set of solutions.
	 *
	 * @param solutionList A list of non-dominated solutions
	 * @param maximumValue The maximum values of the objectives
	 * @param minimumValue The minimum values of the objectives
	 * @return the normalized list of non-dominated solutions
	 */
	public static <T extends Number> ParetoSolution<T> getNormalizedFront(ParetoSolution<T> solutionList,
			List<Double> maximumValue,
			List<Double> minimumValue) {

		ParetoSolution<T> normalizedSolutionSet = new ParetoSolution<T>(solutionList.size()) ;

		int numberOfObjectives = solutionList.get(0).numberOfObjectives() ;
		for (int i = 0; i < solutionList.size(); i++) {
			MOSolutionBase<T> solution = solutionList.get(i).copy() ;
			for (int j = 0; j < numberOfObjectives; j++) {
				double normalizedValue = (solutionList.get(i).getObjective(j) - minimumValue.get(j)) /
						(maximumValue.get(j) - minimumValue.get(j));
				solution.setObjective(j, normalizedValue);
			}
		}
		return normalizedSolutionSet;
	}

	/**
	 * This method receives a normalized list of non-dominated solutions and return the inverted one.
	 * This operation is needed for minimization problem
	 *
	 * @param solutionSet The front to invert
	 * @return The inverted front
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Number> ParetoSolution<T> getInvertedFront(ParetoSolution<T> solutionSet) {
		ParetoSolution<T> invertedFront = new ParetoSolution<T>(solutionSet.size()) ;
		int numberOfObjectives = solutionSet.get(0).numberOfObjectives() ;

		for (int i = 0; i < solutionSet.size(); i++) {
			invertedFront.add(solutionSet.get(i).copy()) ;
			for (int j = 0; j < numberOfObjectives; j++) {
				if (solutionSet.get(i).getObjective(j) <= 1.0 &&
						solutionSet.get(i).getObjective(j) >= 0.0) {
					invertedFront.get(i).setObjective(j, 1.0 - solutionSet.get(i).getObjective(j));
				} else if (solutionSet.get(i).getObjective(j) > 1.0) {
					invertedFront.get(i).setObjective(j, 0.0);
				} else if (solutionSet.get(i).getObjective(j) < 0.0) {
					invertedFront.get(i).setObjective(j, 1.0);
				}
			}
		}
		return invertedFront;
	}

	public static <T extends Number> boolean isSolutionDominatedBySolutionList(MOSolutionBase<T> solution, ParetoSolution<T> solutionSet) {
		boolean result = false ;
		Comparator dominance = new DominanceComparator() ;

		int i = 0 ;

		while (!result && (i < solutionSet.size())) {
			if (dominance.compare(solution, solutionSet.get(i)) == 1) {
				result = true ;
			}
			i++ ;
		}

		return result ;
	}

	/**
	 * This method receives a normalized list of non-dominated solutions and return the inverted one.
	 * This operation is needed for minimization problem
	 *
	 * @param solutionList The front to invert
	 * @return The inverted front
	 */
	public static <T extends Number> ParetoSolution<T> selectNRandomDifferentSolutions(
			int numberOfSolutionsToBeReturned, ParetoSolution<T> solutionList) {


		ParetoSolution<T> resultList = new ParetoSolution<T>(numberOfSolutionsToBeReturned);

		if (solutionList.size() == 1) {
			resultList.add(solutionList.get(0));
		} else {
			Collection<Integer> positions = new HashSet<>(numberOfSolutionsToBeReturned);
			while (positions.size() < numberOfSolutionsToBeReturned) {
				int nextPosition = Util.nextInt(0, solutionList.size() - 1);
				if (!positions.contains(nextPosition)) {
					positions.add(nextPosition);
					resultList.add(solutionList.get(nextPosition));
				}
			}
		}

		return resultList ;
	}

	/**
	 * Returns a matrix with the euclidean distance between each pair of solutions in the population.
	 * Distances are measured in the objective space
	 * @param solutionSet
	 * @return
	 */
	public static <T extends Number> double [][] distanceMatrix(ParetoSolution<T> solutionSet) {
		double [][] distance = new double [solutionSet.size()][solutionSet.size()];
		for (int i = 0; i < solutionSet.size(); i++){
			distance[i][i] = 0.0;
			for (int j = i + 1; j < solutionSet.size(); j++){
				distance[i][j] = SolutionUtils.distanceBetweenObjectives(solutionSet.get(i),solutionSet.get(j));                
				distance[j][i] = distance[i][j];            
			}
		}
		return distance;
	}

	/**
	 * Compares two solution lists to determine if both are equals
	 *
	 * @param solutionList    A <code>Solution list</code>
	 * @param newSolutionList A <code>Solution list</code>
	 * @return true if both are contains the same solutions, false in other case
	 */
	public static <T extends Number> boolean solutionListsAreEquals(ParetoSolution<T> solutionList,
			ParetoSolution<T> newSolutionList) {
		boolean found;
		for (int i = 0; i < solutionList.size(); i++) {

			int j = 0;
			found = false;
			while (j < newSolutionList.size()) {
				if (solutionList.get(i).equals(newSolutionList.get(j))) {
					found = true;
				}
				j++;
			}
			if (!found) {
				return false;
			}
		}
		return true;

	}
}

