package org.um.feri.ears.examples;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.so.de.DEAlgorithm;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.problems.unconstrained.Sphere;
import org.um.feri.ears.util.Util;

public class SOSingleRun {

    public static void main(String[] args) {

        Util.rnd.setSeed(System.currentTimeMillis()); // set a new seed for the random generator for each run

        Problem problem = new Sphere(5); // problem Sphere with five dimensions

        Task sphere = new Task(problem, StopCriterion.EVALUATIONS, 10000, 0, 0); // set the stopping criterion to max 10000 evaluations

        Algorithm alg = new DEAlgorithm(DEAlgorithm.Strategy.JDE_RAND_1_BIN);
        NumberSolution<Double> best;
        try {
            best = alg.execute(sphere);
            System.out.println("Best solution found = " + best); // print the best solution found after 10000 evaluations
        } catch (StopCriterionException e) {
            e.printStackTrace();
        }
    }
}
