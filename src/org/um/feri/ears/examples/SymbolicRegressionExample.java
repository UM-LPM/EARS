package org.um.feri.ears.examples;

import org.um.feri.ears.algorithms.GPAlgorithm;
import org.um.feri.ears.algorithms.gp.ElitismGPAlgorithm;
import org.um.feri.ears.individual.representations.gp.Node;
import org.um.feri.ears.individual.representations.gp.Target;
import org.um.feri.ears.individual.representations.gp.symbolic.regression.*;
import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.gp.ProgramProblem;
import org.um.feri.ears.problems.gp.ProgramSolution;
import org.um.feri.ears.problems.gp.SymbolicRegressionProblem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SymbolicRegressionExample {

    public static void main(String[] args) {

        //Define base function node types
        List<Class<? extends Node>> baseFunctionNodeTypes = Arrays.asList(
                AddNode.class,
                DivNode.class,
                MulNode.class,
                SubNode.class
        );

        //Define base terminal node types
        List<Class<? extends Node>> baseTerminalNodeTypes = Arrays.asList(
                ConstNode.class,
                PiNode.class,
                VarNode.class
        );

        //Define variables
        VarNode.variables = Arrays.asList("x");

        //Define training data for symbolic regression problems
        //eq4: x^3 + x^2 + x
        List<Target> trainingDataEq4 = new ArrayList<>();
        int numPoints = 20;
        double start = -1.0;
        double end = 1.0;

        //sample 20 data points in range [-1, 1]
        for (int i = 0; i < numPoints; i++) {
            double x = start + i * (end - start) / (numPoints - 1);
            double targetValue = Math.pow(x, 3) + Math.pow(x, 2) + x;
            trainingDataEq4.add(new Target().when("x", x).targetIs(targetValue));
        }

        //eq10: sqrt(x)
        List<Target> trainingSetEq10 = new ArrayList<>();
        numPoints = 20;
        start = 0.0;
        end = 4.0;

        //sample 20 data points in range [0, 4]
        for (int i = 0; i < numPoints; i++) {
            double x = start + i * (end - start) / (numPoints - 1);
            double targetValue = Math.sqrt(x);
            trainingSetEq10.add(new Target().when("x", x).targetIs(targetValue));
        }

        //eq29: 0.3 * x * sin(2 * pi * x)
        List<Target> trainingDataEq29 = new ArrayList<>();
        double step = 0.1;
        start = -1.0;
        end = 1.0;

        //sample E[-1.0, 1.0, 0.1]
        while (start <= end) {
            double targetValue = 0.3 * start * Math.sin(2 * Math.PI * start);
            trainingDataEq29.add(new Target().when("x", start).targetIs(targetValue));
            start += step;
        }

        //Define testing data for symbolic regression problems
        List<Target> testingDataEq29 = new ArrayList<>();
        step = 0.001;
        start = -1.0;
        end = 1.0;

        //sample E[-1.0, 1.0, 0.001]
        while (start <= end) {
            double targetValue = 0.3 * start * Math.sin(2 * Math.PI * start);
            testingDataEq29.add(new Target().when("x", start).targetIs(targetValue));
            start += step;
        }

        SymbolicRegressionProblem sgpTrainingData = new SymbolicRegressionProblem(baseFunctionNodeTypes, baseTerminalNodeTypes, trainingDataEq29);

        SymbolicRegressionProblem sgpTestingData = new SymbolicRegressionProblem(baseFunctionNodeTypes, baseTerminalNodeTypes, testingDataEq29);

        Task<ProgramSolution, ProgramProblem> symbolicRegressionTask = new Task<>(sgpTrainingData, StopCriterion.EVALUATIONS, 10000, 0, 0);

        GPAlgorithm alg = new ElitismGPAlgorithm();

        try {
            ProgramSolution solution = alg.execute(symbolicRegressionTask);
            //Print solution fitness on training data
            System.out.println("Fitness on Training Data -> " + solution.getEval());
            //Print solution tree
            solution.getTree().displayTree("Equation4", true);
            //Print solution equation
            System.out.println(solution);

            //Evaluate solution on testing data
            sgpTestingData.evaluate(solution);
            //Print solution fitness on testing data if available
            System.out.println("Fitness on Testing Data -> " + solution.getEval());

        } catch (StopCriterionException e) {
            e.printStackTrace();
        }
    }
}
