package org.um.feri.ears.util.comparator;

import java.util.Comparator;

import org.um.feri.ears.problems.moo.MOSolutionBase;

public class MOFitnessComparator<Type extends Number> implements Comparator<MOSolutionBase<Type>> {

    /**
     * Compares two solutions.
     *
     * @param solution1 Object representing the first <code>Solution</code>.
     * @param solution2 Object representing the second <code>Solution</code>.
     * @return -1, or 0, or 1 if solution1 is less than, equal, or greater than solution2,
     * respectively.
     */
    public int compare(MOSolutionBase<Type> solution1, MOSolutionBase<Type> solution2) {
        if (solution1 == null)
            return 1;
        else if (solution2 == null)
            return -1;
		return Double.compare(solution1.getFitness(), solution2.getFitness());

	}
}
