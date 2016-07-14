//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.

package org.um.feri.ears.operators;

import javax.management.JMException;

import org.um.feri.ears.algorithms.moo.pesa2.AdaptiveGridArchive;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.util.Util;

public class PESA2Selection<Type extends Number> {

	  /**
	  * Performs the operation
	  * @param object Object representing a SolutionSet. This solution set
	  * must be an instancen <code>AdaptiveGridArchive</code>
	  * @return the selected solution
	   * @throws JMException 
	  */
	public MOSolutionBase<Type> execute(Object object) {
		try {
			AdaptiveGridArchive<Type> archive = (AdaptiveGridArchive<Type>) object;
			int selected;
			int hypercube1 = archive.getGrid().randomOccupiedHypercube();
			int hypercube2 = archive.getGrid().randomOccupiedHypercube();

			if (hypercube1 != hypercube2) {
				if (archive.getGrid().getLocationDensity(hypercube1) < archive.getGrid().getLocationDensity(hypercube2)) {
					selected = hypercube1;

				} else if (archive.getGrid().getLocationDensity(hypercube2) < archive.getGrid().getLocationDensity(hypercube1)) {
					selected = hypercube2;
				} else {
					if (Util.rnd.nextDouble() < 0.5) {
						selected = hypercube2;
					} else {
						selected = hypercube1;
					}
				}
			} else {
				selected = hypercube1;
			}
			int base = Util.rnd.nextInt(archive.size());
			int cnt = 0;
			while (cnt < archive.size()) {
				MOSolutionBase<Type> individual = archive.get((base + cnt) % archive.size());
				if (archive.getGrid().location(individual) != selected) {
					cnt++;
				} else {
					return individual;
				}
			}
			return archive.get((base + cnt) % archive.size());
		} catch (ClassCastException e) {
			System.err.println("Exception: " + e.getMessage());
			return null;
		}
	}
}
