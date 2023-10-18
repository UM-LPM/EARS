//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.

package org.um.feri.ears.operators;

import org.um.feri.ears.algorithms.moo.pesa2.AdaptiveGridArchive;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.util.random.RNG;

import javax.management.JMException;

public class PESA2Selection<N extends Number> {

	  /**
	  * Performs the operation
	  * @param object Object representing a SolutionSet. This solution set
	  * must be an instancen <code>AdaptiveGridArchive</code>
	  * @return the selected solution
	   * @throws JMException 
	  */
	public NumberSolution<N> execute(Object object) {
		try {
			AdaptiveGridArchive<N> archive = (AdaptiveGridArchive<N>) object;
			int selected;
			int hypercube1 = archive.getGrid().randomOccupiedHypercube();
			int hypercube2 = archive.getGrid().randomOccupiedHypercube();

			if (hypercube1 != hypercube2) {
				if (archive.getGrid().getLocationDensity(hypercube1) < archive.getGrid().getLocationDensity(hypercube2)) {
					selected = hypercube1;

				} else if (archive.getGrid().getLocationDensity(hypercube2) < archive.getGrid().getLocationDensity(hypercube1)) {
					selected = hypercube2;
				} else {
					if (RNG.nextDouble() < 0.5) {
						selected = hypercube2;
					} else {
						selected = hypercube1;
					}
				}
			} else {
				selected = hypercube1;
			}
			int base = RNG.nextInt(archive.size());
			int cnt = 0;
			while (cnt < archive.size()) {
				NumberSolution<N> individual = archive.get((base + cnt) % archive.size());
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
