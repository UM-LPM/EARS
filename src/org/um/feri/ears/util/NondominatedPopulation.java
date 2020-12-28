package org.um.feri.ears.util;

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

import java.util.Iterator;

import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.Comparator.DominanceComparator;

/**
 * A population that maintains the property of pair-wise non-dominance between
 * all solutions. When the {@code add} method is invoked with a new solution,
 * all solutions currently in the population that are dominated by the new
 * solution are removed. If the new solution is dominated by any member of the
 * population, the new solution is not added.
 */
public class NondominatedPopulation<T extends Number> extends ParetoSolution<T> {

    /**
     * The dominance comparator used by this non-dominated population.
     */
    protected final DominanceComparator<T> comparator;

    /**
     * If {@code true}, allow duplicate solutions in this non-dominated
     * population.  Duplicate solutions are those whose Euclidean distance
     * is smaller than EPSILON.
     */
    protected final boolean allowDuplicates;

    /**
     * Constructs an empty non-dominated population using the Pareto dominance
     * relation.
     */
    public NondominatedPopulation() {
        this(new DominanceComparator<T>());
    }

    /**
     * Constructs an empty non-dominated population using the specified
     * dominance relation.
     *
     * @param comparator the dominance relation used by this non-dominated
     *                   population
     */
    public NondominatedPopulation(DominanceComparator<T> comparator) {
        this(comparator, false);
    }

    /**
     * Constructs an empty non-dominated population using the specified
     * dominance relation.
     *
     * @param comparator      the dominance relation used by this non-dominated
     *                        population
     * @param allowDuplicates allow duplicate solutions into the non-dominated
     *                        population
     */
    public NondominatedPopulation(DominanceComparator<T> comparator,
                                  boolean allowDuplicates) {
        super();
        this.comparator = comparator;
        this.allowDuplicates = allowDuplicates;
    }

    /**
     * Constructs a non-dominated population using the Pareto dominance relation
     * and initialized with the specified solutions.
     */
    public NondominatedPopulation(ParetoSolution<T> population) {
        this();
        addAll(population);
    }

    /**
     * Constructs a non-dominated population using the specified dominance
     * comparator and initialized with the specified solutions.
     *
     * @param comparator the dominance relation used by this non-dominated
     *                   population
     */
    public NondominatedPopulation(DominanceComparator<T> comparator,
                                  ParetoSolution<T> population) {
        this(comparator);
        addAll(population);
    }

    public NondominatedPopulation(int capacity) {
        super(capacity);
        comparator = new DominanceComparator<T>();
        allowDuplicates = false;
    }

    /**
     * If {@code newSolution} is dominates any solution or is non-dominated with
     * all solutions in this population, the dominated solutions are removed and
     * {@code newSolution} is added to this population. Otherwise,
     * {@code newSolution} is dominated and is not added to this population.
     */
    @Override
    public boolean add(MOSolutionBase<T> newSolution) {
        Iterator<MOSolutionBase<T>> iterator = iterator();

        while (iterator.hasNext()) {
            MOSolutionBase<T> oldSolution = iterator.next();
            int flag = comparator.compare(newSolution, oldSolution);

            if (flag < 0) {
                iterator.remove();
            } else if (flag > 0) {
                return false;
            } else if (!allowDuplicates &&
                    distance(newSolution, oldSolution) < 1e-10) {
                return false;
            }
        }

        return super.add(newSolution);
    }

    /**
     * Replace the solution at the given index with the new solution, but only
     * if the new solution is non-dominated.  To maintain non-dominance within
     * this population, any solutions dominated by the new solution will also
     * be replaced.
     */
    @Override
    public void replace(int index, MOSolutionBase<T> newSolution) {
        Iterator<MOSolutionBase<T>> iterator = iterator();

        while (iterator.hasNext()) {
            MOSolutionBase<T> oldSolution = iterator.next();
            int flag = comparator.compare(newSolution, oldSolution);

            if (flag < 0) {
                iterator.remove();
            } else if (flag > 0) {
                return;
            } else if (!allowDuplicates &&
                    distance(newSolution, oldSolution) < 1e-10) {
                return;
            }
        }

        super.replace(index, newSolution);
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
    protected boolean forceAddWithoutCheck(MOSolutionBase<T> newSolution) {
        return super.add(newSolution);
    }

    /**
     * Returns the Euclidean distance between two solutions in objective space.
     *
     * @param s1 the first solution
     * @param s2 the second solution
     * @return the distance between the two solutions in objective space
     */
    protected double distance(MOSolutionBase<T> s1, MOSolutionBase<T> s2) {
        double distance = 0.0;

        for (int i = 0; i < s1.numberOfObjectives(); i++) {
            distance += Math.pow(s1.getObjective(i) - s2.getObjective(i), 2.0);
        }

        return Math.sqrt(distance);
    }

    /**
     * Returns the dominance comparator used by this non-dominated population.
     *
     * @return the dominance comparator used by this non-dominated population
     */
    public DominanceComparator<T> getComparator() {
        return comparator;
    }
}
