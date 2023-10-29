package org.um.feri.ears.examples;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.um.feri.ears.algorithms.GPAlgorithm;
import org.um.feri.ears.algorithms.GPAlgorithm2;
import org.um.feri.ears.algorithms.gp.DefaultGPAlgorithm;
import org.um.feri.ears.algorithms.gp.DefaultGPAlgorithm2;
import org.um.feri.ears.algorithms.gp.RandomWalkGPAlgorithm;
import org.um.feri.ears.individual.btdemo.gp.*;
import org.um.feri.ears.individual.btdemo.gp.behaviour.soccer.MoveForward;
import org.um.feri.ears.individual.btdemo.gp.behaviour.soccer.MoveSide;
import org.um.feri.ears.individual.btdemo.gp.behaviour.soccer.Rotate;
import org.um.feri.ears.individual.btdemo.gp.symbolic.*;
import org.um.feri.ears.individual.btdemo.gp.behaviour.*;
import org.um.feri.ears.individual.generations.gp.GPRandomProgramSolution;
import org.um.feri.ears.individual.generations.gp.GPRandomProgramSolution2;
import org.um.feri.ears.individual.representations.gp.Target;
import org.um.feri.ears.operators.gp.GPDepthBasedTreePruningOperator;
import org.um.feri.ears.operators.gp.GPDepthBasedTreePruningOperator2;
import org.um.feri.ears.operators.gp.GPTreeExpansionOperator;
import org.um.feri.ears.operators.gp.GPTreeExpansionOperator2;
import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.gp.*;
import org.um.feri.ears.util.Util;

import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class SymbolicRegressionBtExample {
    public static void main(String[] args) throws IOException {
        // srExample();
        // btExample();
        //treeGenEvalSymbolicExample();
        //treeGenBtExample();

        symbolicRegressionDefaultAlgorithmRunExample();
    }

    public static void symbolicRegressionDefaultAlgorithmRunExample(){
        List<Class<? extends Node>> baseFunctionNodeTypes = Arrays.asList(
                AddNode.class,
                DivNode.class,
                MulNode.class,
                SubNode.class
        );

        List<Class<? extends Node>> baseTerminalNodeTypes = Arrays.asList(
                ConstNode.class,
                VarNode.class
        );

        VarNode.variables = Arrays.asList("x");

        List<Target> evalData = Util.list( new Target().when("x", 0).targetIs(0),
                new Target().when("x", 1).targetIs(11),
                new Target().when("x", 2).targetIs(24),
                new Target().when("x", 3).targetIs(39),
                new Target().when("x", 4).targetIs(56),
                new Target().when("x", 5).targetIs(75),
                new Target().when("x", 6).targetIs(96),
                new Target().when("x", 7).targetIs(119),
                new Target().when("x", 8).targetIs(144),
                new Target().when("x", 9).targetIs(171),
                new Target().when("x", 10).targetIs(200));

        SymbolicRegressionProblem2 sgp2 = new SymbolicRegressionProblem2(baseFunctionNodeTypes, baseTerminalNodeTypes, 3, 8, 200, new GPDepthBasedTreePruningOperator2(),
                new GPTreeExpansionOperator2(), new GPRandomProgramSolution2(), evalData);

        //GP algorithm execution example
        Task<ProgramSolution2, ProgramProblem2> symbolicRegressionTask = new Task<>(sgp2, StopCriterion.EVALUATIONS, 10000, 0, 0);


        GPAlgorithm2 alg = new DefaultGPAlgorithm2(100, 0.95, 0.025, 2);
        RandomWalkGPAlgorithm rndAlg = new RandomWalkGPAlgorithm();

        try {
            long startTime = System.currentTimeMillis();
            System.out.println("Starting DefaultGpAlgorithm");
            ArrayList<ProgramSolution2> solutions = new ArrayList<>();
            ArrayList<Double> solutionsRnd = new ArrayList<>();
            ProgramSolution2 sol;
            for (int i = 0; i < 100; i++){
                sol = alg.execute(symbolicRegressionTask);
                solutions.add(sol);
                System.out.println("Best fitness (DefaultGpAlgorithm) (for i = " + i + ") -> " + sol.getEval());
                alg.resetToDefaultsBeforeNewRun();

                sol = rndAlg.execute(symbolicRegressionTask);
                solutionsRnd.add(sol.getEval());
                System.out.println("Best fitness (RandomWalkAlgorithm) (for i = " + i + ") -> " + sol.getEval());
                rndAlg.resetToDefaultsBeforeNewRun();
                alg.resetToDefaultsBeforeNewRun();
            }

            ProgramSolution2 maxSol = solutions.get(0);
            double maxRnd = Double.MAX_VALUE;
            int wins = 0;
            for (int i = 0; i < solutions.size(); i++){
                if(solutions.get(i).getEval() < maxSol.getEval())
                    maxSol = solutions.get(i);
                if(solutionsRnd.get(i) < maxRnd)
                    maxRnd = solutionsRnd.get(i);

                if(solutions.get(i).getEval() < solutionsRnd.get(i))
                    wins++;
            }

            System.out.println("=====================================");
            System.out.println("Best fitness (DefaultGpAlgorithm)  -> " + maxSol.getEval());
            System.out.println("Best fitness (RandomWalkAlgorithm) -> " + maxRnd);
            System.out.println("Wins (DefaultGpAlgorithm) -> " + wins);

            long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;

            System.out.println("Elapsed time: " + elapsedTime + " s");
            //maxSol.getTree().getRootNode().displayTree("TestBTree", true);
            /*ProgramSolution<Double> sol = alg.execute(symbolicRegression);
            System.out.println("Best fitness: " + sol.getEval());
            System.out.println("AncestorCount: " + sol.getProgram().ancestors().getAncestorCount());
            System.out.println("Tree Depth: " + sol.getProgram().treeHeight());
            sol.getProgram().displayTree("TestBTree");*/
        } catch (StopCriterionException e) {
            e.printStackTrace();
        }
    }

    public static void treeGenBtExample(){
        List<Class<? extends Node>> baseFunctionNodeTypes = Arrays.asList(
                Repeat.class,
                Sequencer.class,
                Selector.class,
                Inverter.class
        );

        List<Class<? extends Node>> baseTerminalNodeTypes = Arrays.asList(
                MoveForward.class,
                MoveSide.class,
                Rotate.class
        );

        SoccerBTProblem2 sgp2 = new SoccerBTProblem2(baseFunctionNodeTypes, baseTerminalNodeTypes, 3, 8, 200, new GPDepthBasedTreePruningOperator2(),
                new GPTreeExpansionOperator2(), new GPRandomProgramSolution2());

        ProgramSolution2 programSolution2 = sgp2.getRandomSolution();
        programSolution2.getTree().printTree();
    }

    public static void treeGenEvalSymbolicExample(){
        List<Class<? extends Node>> baseFunctionNodeTypes = Arrays.asList(
                AddNode.class,
                DivNode.class,
                MulNode.class,
                SubNode.class
        );

        List<Class<? extends Node>> baseTerminalNodeTypes = Arrays.asList(
                ConstNode.class,
                VarNode.class
        );

        VarNode.variables = Arrays.asList("x");

        List<Target> evalData = Util.list( new Target().when("x", 0).targetIs(0),
                new Target().when("x", 1).targetIs(11),
                new Target().when("x", 2).targetIs(24),
                new Target().when("x", 3).targetIs(39),
                new Target().when("x", 4).targetIs(56),
                new Target().when("x", 5).targetIs(75),
                new Target().when("x", 6).targetIs(96),
                new Target().when("x", 7).targetIs(119),
                new Target().when("x", 8).targetIs(144),
                new Target().when("x", 9).targetIs(171),
                new Target().when("x", 10).targetIs(200));

        SymbolicRegressionProblem2 sgp2 = new SymbolicRegressionProblem2(baseFunctionNodeTypes, baseTerminalNodeTypes, 3, 8, 200, new GPDepthBasedTreePruningOperator2(),
                new GPTreeExpansionOperator2(), new GPRandomProgramSolution2(), evalData);

        ProgramSolution2 programSolution2 = sgp2.getRandomSolution();
        sgp2.evaluate(programSolution2);
        programSolution2.getTree().printTree();
        System.out.println(programSolution2.getEval());
    }

    public static void btExample(){
        List<Class<? extends Node>> baseFunctionNodeTypes = Arrays.asList(
                Repeat.class,
                Sequencer.class,
                Selector.class,
                Inverter.class
        );

        List<Class<? extends Node>> baseTerminalNodeTypes = Arrays.asList(
                MoveForward.class,
                MoveSide.class,
                Rotate.class
        );

        TreeGenerator treeGenerator = new TreeGenerator(baseFunctionNodeTypes, baseTerminalNodeTypes, 2);

        Tree tree = new BehaviourTree("demo", treeGenerator.generateRandomTree(0, 5));
        tree.printTree();
    }

    public static void srExample(){
        List<Class<? extends Node>> baseFunctionNodeTypes = Arrays.asList(
                AddNode.class,
                DivNode.class,
                MulNode.class,
                SubNode.class
        );

        List<Class<? extends Node>> baseTerminalNodeTypes = Arrays.asList(
                ConstNode.class,
                VarNode.class
        );

        VarNode.variables = Arrays.asList("x");

        TreeGenerator treeGenerator = new TreeGenerator(baseFunctionNodeTypes, baseTerminalNodeTypes, 2);

        Tree tree = new SymbolicRegressionTree("demo", treeGenerator.generateRandomTree(0, 3));
        tree.printTree();
        System.out.println(tree.evaluate(Map.of("x", 1.0)));
    }

    public static String sendPostRequest(String apiUrl, String jsonBody) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // Set the request method to POST
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        // Write the JSON payload to the request body
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonBody.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                return response.toString();
            }
        } else {
            throw new RuntimeException("HTTP POST request failed with response code: " + responseCode);
        }
    }
}
