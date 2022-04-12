package org.um.feri.ears.util.comparator;

import org.um.feri.ears.problems.moo.MOSolutionBase;

import java.util.Comparator;

/**
 * This class implements a <code>Comparator</code> (a method for comparing
 * <code>Solution</code> objects) based on a objective values.
 */
public class ObjectiveComparator<Type> implements Comparator<MOSolutionBase<Type>> {

    /**
     * Stores the index of the objective to compare
     */
    private int objective;

    /**
     * Constructor.
     *
     * @param index The index of the objective to compare
     */
    public ObjectiveComparator(int index) {
        this.objective = index;
    }

    /**
     * Compares two solutions.
     *
     * @param solution1 the first <code>Solution</code>.
     * @param solution2 the second <code>Solution</code>.
     * @return -1, or 0, or 1 if o1 is less than, equal, or greater than o2,
     * respectively.
     */
    public int compare(MOSolutionBase<Type> solution1, MOSolutionBase<Type> solution2) {
        if (solution1 == null)
            return 1;
        else if (solution2 == null)
            return -1;
        return Double.compare(solution1.getObjective(objective), solution2.getObjective(objective));
    }
}
