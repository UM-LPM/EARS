//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.

package org.um.feri.ears.util;

import java.util.Comparator;

import org.um.feri.ears.problems.moo.MOSolutionBase;

public class FitnessComparator<Type> implements Comparator<MOSolutionBase<Type>>{
   
    /**
	* Compares two solutions.
	* @param o1 Object representing the first <code>Solution</code>.
	* @param o2 Object representing the second <code>Solution</code>.
	* @return -1, or 0, or 1 if o1 is less than, equal, or greater than o2,
	* respectively.
	*/
	public int compare(MOSolutionBase<Type> o1, MOSolutionBase<Type> o2) {
		if (o1 == null)
			return 1;
		else if (o2 == null)
			return -1;

		double fitness1 = o1.getFitness();
		double fitness2 = o2.getFitness();
		if (fitness1 < fitness2) {
			return -1;
		}

		if (fitness1 > fitness2) {
			return 1;
		}

		return 0;
	}
}
