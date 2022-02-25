//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.

package org.um.feri.ears.algorithms.moo.pesa2;

import java.util.Comparator;
import java.util.Iterator;

import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.comparator.DominanceComparator;

public class AdaptiveGridArchive<Type extends Number> extends ParetoSolution<Type> {

    private AdaptiveGrid<Type> grid;

    private int maxSize;

    private Comparator<MOSolutionBase<Type>> dominance;

    public AdaptiveGridArchive(int maxSize, int bisections, int objectives) {
        super(maxSize);
        this.maxSize = maxSize;
        dominance = new DominanceComparator<>();
        grid = new AdaptiveGrid<>(bisections, objectives);
    }

    /**
     * Adds a <code>Solution</code> to the archive. If the <code>Solution</code>
     * is dominated by any member of the archive then it is discarded. If the
     * <code>Solution</code> dominates some members of the archive, these are
     * removed. If the archive is full and the <code>Solution</code> has to be
     * inserted, one <code>Solution</code> of the most populated hypercube of the
     * adaptive grid is removed.
     *
     * @param solution The <code>Solution</code>
     * @return true if the <code>Solution</code> has been inserted, false
     * otherwise.
     */
    public boolean add(MOSolutionBase<Type> solution) {
        // Iterator of individuals over the list
        Iterator<MOSolutionBase<Type>> iterator = solutions.iterator();

        while (iterator.hasNext()) {
            MOSolutionBase<Type> element = iterator.next();
            int flag = dominance.compare(solution, element);
            if (flag == -1) { // The Individual to insert dominates other individuals in the archive
                iterator.remove(); // Delete it from the archive
                int location = grid.location(element);
                if (grid.getLocationDensity(location) > 1) {// The hypercube contains
                    grid.removeSolution(location); // more than one individual
                } else {
                    grid.updateGrid(this);
                }
            } else if (flag == 1) { // An Individual into the file dominates the solution to insert
                return false; // The solution will not be inserted
            }
        }

        // At this point, the solution may be inserted
        if (size() == 0) { // The archive is empty
            solutions.add(solution);
            grid.updateGrid(this);
            return true;
        }

        if (size() < maxSize) { // The archive is not full
            grid.updateGrid(solution, this); // Update the grid if applicable
            int location;
            location = grid.location(solution); // Get the location of the solution
            grid.addSolution(location); // Increment the density of the hypercube
            solutions.add(solution); // Add the solution to the list
            return true;
        }

        // At this point, the solution has to be inserted and the archive is full
        grid.updateGrid(solution, this);
        int location = grid.location(solution);
        if (location == grid.getMostPopulated()) { // The solution is in the most populated hypercube
            return false; // Not inserted
        } else {
            // Remove an solution from most populated area
            iterator = solutions.iterator();
            boolean removed = false;
            while (iterator.hasNext()) {
                if (!removed) {
                    MOSolutionBase<Type> element = iterator.next();
                    int location2 = grid.location(element);
                    if (location2 == grid.getMostPopulated()) {
                        iterator.remove();
                        grid.removeSolution(location2);
                    }
                }
            }
            // A solution from most populated hypercube has been removed, insert now the solution
            grid.addSolution(location);
            solutions.add(solution);
        }
        return true;
    }

    /**
     * Returns the AdaptativeGrid used
     *
     * @return the AdaptativeGrid
     */
    public AdaptiveGrid getGrid() {
        return grid;
    }
}
