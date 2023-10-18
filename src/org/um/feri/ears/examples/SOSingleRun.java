package org.um.feri.ears.examples;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.so.de.DEAlgorithm;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.problems.unconstrained.Sphere;

public class SOSingleRun {

    public static void main(String[] args) {

        //RNG.setSeed(100); // set a specific seed for the random generator

        DoubleProblem problem = new Sphere(5); // problem Sphere with five dimensions

        Task sphere = new Task(problem, StopCriterion.EVALUATIONS, 10000, 0, 0); // set the stopping criterion to max 10000 evaluations

        NumberAlgorithm alg = new DEAlgorithm(DEAlgorithm.Strategy.JDE_RAND_1_BIN);
        NumberSolution<Double> best;
        try {
            best = alg.execute(sphere);
            System.out.println("Best solution found = " + best); // print the best solution found after 10000 evaluations
        } catch (StopCriterionException e) {
            e.printStackTrace();
        }
    }
}
