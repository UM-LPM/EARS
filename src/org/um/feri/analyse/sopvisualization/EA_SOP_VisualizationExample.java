package org.um.feri.analyse.sopvisualization;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.so.abc.ABC;
import org.um.feri.ears.algorithms.so.de.DEAlgorithm;
import org.um.feri.ears.algorithms.so.ff.FireflyAlgorithm;
import org.um.feri.ears.algorithms.so.gwo.GWO;
import org.um.feri.ears.algorithms.so.random.RandomWalkAlgorithm;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.problems.unconstrained.Griewank;
import org.um.feri.ears.problems.unconstrained.Schwefel226;
import org.um.feri.ears.problems.unconstrained.Sphere;
import org.um.feri.ears.util.Util;

import java.nio.file.Path;
import java.nio.file.Paths;
/*
Demo for creating files that are used for dynamic visualization of alg. search space.
Project https://github.com/UM-LPM/EA_SOP_Visualization
 */
public class EA_SOP_VisualizationExample {

    public static String GetUsersProjectRootDirectory() {
        String envRootDir = System.getProperty("user.dir");
        Path rootDir = Paths.get(".").normalize().toAbsolutePath();
        if (rootDir.startsWith(envRootDir)) {
            return rootDir.toString();
        } else {
            throw new RuntimeException("Root dir not found in user directory.");
        }
    }
    public  static String PATH = "C:\\tmp"; //changed in main


    public static void run(Algorithm alg, Task task) {
        Util.rnd.setSeed(System.currentTimeMillis()); // set a new seed for the random generator for each run
        task.enableAncestorLogging();
        DoubleSolution best;
        try {
            best = alg.execute(task);
            System.out.println("Best found solution :" + best); // print the best solution found after 10000 evaluations
            task.saveAncestorLogging4Visualization(PATH, alg, 1);
        } catch (StopCriterionException e) {
            e.printStackTrace();
        }
    }

    public static void run4Task(Task task) {
        task.resetCounter();
        run(new RandomWalkAlgorithm(), task);
        task.resetCounter();
        run(new GWO(), task);
        task.resetCounter();
        run(new ABC(), task);
        task.resetCounter();
        run(new DEAlgorithm(DEAlgorithm.Strategy.JDE_RAND_1_BIN), task);
        task.resetCounter();
        run(new FireflyAlgorithm(), task);

    }

    public static void main(String[] args) {
        PATH = GetUsersProjectRootDirectory();
        Task task;
        Problem problem;
        problem = new Sphere(6); // problem Sphere with five dimensions
        task = new Task(EnumStopCriterion.EVALUATIONS, 10000, 0, 0, 0.001, problem); // set the stopping criterion to max 10000 evaluations
        run4Task(task);
        problem = new Griewank(6);
        task = new Task(EnumStopCriterion.EVALUATIONS, 10000, 0, 0, 0.001, problem); // set the stopping criterion to max 10000 evaluations
        run4Task(task);
        problem = new Schwefel226(6);
        task = new Task(EnumStopCriterion.EVALUATIONS, 10000, 0, 0, 0.001, problem); // set the stopping criterion to max 10000 evaluations
        run4Task(task);
    }
}