//  NonDominatedSolutionList.java
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
//TODO duplicate nondominatedpopulation
package org.um.feri.ears.util;

import java.util.Comparator;
import java.util.Iterator;

import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.ParetoSolution;

/** 
 * This class implements an unbound list of non-dominated solutions
 */
public class NonDominatedSolutionList<Type extends Number> extends ParetoSolution<Type>{

	/**
	* Stores a <code>Comparator</code> for dominance checking
	*/
	protected Comparator<MOSolutionBase<Type>> dominanceComparator = new DominanceComparator<>();

	/**
	* Stores a <code>Comparator</code> for checking if two solutions are equal
	*/
	private static final Comparator equalSolutions = new SolutionComparator();

	/** 
	* Constructor.
	* The objects of this class are lists of non-dominated solutions according to
	* a Pareto dominance comparator. 
	*/
	public NonDominatedSolutionList() {
		super();
	}

	/**
	* Constructor.
	* This constructor creates a list of non-dominated individuals using a
	* comparator object.
	* @param dominance The comparator for dominance checking.
	*/
	public NonDominatedSolutionList(Comparator<MOSolutionBase<Type>> dominance) {
		super();
		dominanceComparator = dominance;
	}
	
	/**
	 * Adds the specified solution to the population, bypassing the
	 * non-domination check. This method should only be used when a
	 * non-domination check has been performed elsewhere, such as in a subclass.
	 * <p>
	 * <b>This method should only be used internally, and should never be made
	 * public by any subclasses.</b>
	 * 
	 * @param newSolution the solution to be added
	 * @return true if the population was modified as a result of this operation
	 */
	protected boolean forceAddWithoutCheck(MOSolutionBase<Type> newSolution) {
		return super.add(newSolution);
	}

	/** Inserts a solution in the list
	* @param solution The solution to be inserted.
	* @return true if the operation success, and false if the solution is 
	* dominated or if an identical individual exists.
	* The decision variables can be null if the solution is read from a file; in
	* that case, the domination tests are omitted
	*/
	public boolean add(MOSolutionBase<Type> solution) {
		
		if (solutions.size() == 0) {
			solutions.add(solution);
			return true;
		} else {
			Iterator<MOSolutionBase<Type>> iterator = solutions.iterator();

			// if (solution.getDecisionVariables() != null) {
			while (iterator.hasNext()) {
				MOSolutionBase<Type> listIndividual = iterator.next();
				int flag = dominanceComparator.compare(solution, listIndividual);

				if (flag == -1) { // A solution in the list is dominated by the
									// new one
					iterator.remove();
				} else if (flag == 0) { // Non-dominated solutions
					// flag = equal_.compare(solution,listIndividual);
					// if (flag == 0) {
					// return false; // The new solution is in the list
					// }
				} else if (flag == 1) { // The new solution is dominated
					return false;
				}
			}
			// } // if

			// At this point, the solution is inserted into the list
			solutions.add(solution);
			return true;
		}
		/*
	    boolean solutionInserted = false ;
	    if (solutions.size() == 0) {
	    	solutions.add(solution) ;
	      solutionInserted = true ;
	    } else {
	      Iterator<MOSolution> iterator = solutions.iterator();
	      boolean isDominated = false;
	      
	      boolean isContained = false;
	      while (((!isDominated) && (!isContained)) && (iterator.hasNext())) {
	        MOSolution listIndividual = iterator.next();
	        int flag = dominanceComparator.compare(solution, listIndividual);
	        if (flag == -1) {
	          iterator.remove();
	        }  else if (flag == 1) {
	          isDominated = true; // dominated by one in the list
	        } else if (flag == 0) {
	        	int equalflag = equalSolutions.compare(solution, listIndividual);
	        	if (equalflag==0) // solutions are equals
	        		isContained = true;
	        }
	        	
	      }
	      
	      if (!isDominated && !isContained) {
	    	  solutions.add(solution);
	    	  solutionInserted = true;
	      }
	      
	      return solutionInserted;
	    }

	    return solutionInserted ;*/
	}
}
