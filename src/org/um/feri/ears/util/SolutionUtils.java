package org.um.feri.ears.util;

import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.random.RNG;

import java.util.Comparator;

public class SolutionUtils {

    /**
     * Return the best solution between those passed as arguments. If they are equal or incomparable
     * one of them is chosen randomly.
     *
     * @param solution1
     * @param solution2
     * @return The best solution
     */
    public static <T extends Number> NumberSolution<T> getBestSolution(NumberSolution<T> solution1, NumberSolution<T> solution2, Comparator<NumberSolution<T>> comparator) {
        NumberSolution<T> result;
        int flag = comparator.compare(solution1, solution2);
        if (flag == -1) {
            result = solution1;
        } else if (flag == 1) {
            result = solution2;
        } else {
            if (RNG.nextDouble() < 0.5) {
                result = solution1;
            } else {
                result = solution2;
            }
        }
        return result;
    }


    /**
     * Returns the euclidean distance between a pair of solutions in the objective space
     *
     * @param firstSolution
     * @param secondSolution
     * @return
     */
    static <T extends Number> double distanceBetweenObjectives(NumberSolution<T> firstSolution, NumberSolution<T> secondSolution) {

        double diff;
        double distance = 0.0;
        //euclidean distance
        for (int nObj = 0; nObj < firstSolution.getNumberOfObjectives(); nObj++) {
            diff = firstSolution.getObjective(nObj) - secondSolution.getObjective(nObj);
            distance += Math.pow(diff, 2.0);
        }

        return Math.sqrt(distance);
    }

    /**
     * Returns the minimum distance from a <code>Solution</code> to a
     * <code>SolutionSet according to the encodings.variable values</code>.
     *
     * @param solution     The <code>Solution</code>.
     * @param solutionList The <code>List<Solution></></code>.
     * @return The minimum distance between solution and the set.
     */
    public static <T extends Number> double distanceToSolutionListInSolutionSpace(NumberSolution<T> solution,
                                                                                  ParetoSolution<T> solutionList) {
        //At start point the distance is the max
        double distance = Double.MAX_VALUE;

        // found the min distance respect to population
        for (int i = 0; i < solutionList.size(); i++) {
            double aux = distanceBetweenSolutions(solution, solutionList.get(i));
            if (aux < distance)
                distance = aux;
        }
        return distance;
    }

    /**
     * Returns the distance between two solutions in the search space.
     *
     * @param solutionI The first <code>Solution</code>.
     * @param solutionJ The second <code>Solution</code>.
     * @return the distance between solutions.
     */
    public static <T extends Number> double distanceBetweenSolutions(NumberSolution<T> solutionI, NumberSolution<T> solutionJ) {
        double distance = 0.0;

        double diff;
        for (int i = 0; i < solutionI.numberOfVariables(); i++) {
            diff = solutionI.getValue(i).doubleValue() - solutionJ.getValue(i).doubleValue();
            distance += Math.pow(diff, 2.0);
        }
        return Math.sqrt(distance);
    }
}

