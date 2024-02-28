package org.um.feri.ears.examples;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.um.feri.ears.algorithms.GPAlgorithm;
import org.um.feri.ears.algorithms.gp.DefaultGPAlgorithm;
import org.um.feri.ears.algorithms.gp.RandomWalkGPAlgorithm;
import org.um.feri.ears.individual.generations.gp.GPRandomProgramSolution;
import org.um.feri.ears.individual.representations.gp.Node;
import org.um.feri.ears.individual.representations.gp.Tree;
import org.um.feri.ears.individual.representations.gp.TreeGenerator;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.*;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.soccer.MoveForward;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.soccer.MoveSide;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.soccer.RayHitObject;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.soccer.Rotate;
import org.um.feri.ears.individual.representations.gp.Target;
import org.um.feri.ears.individual.representations.gp.symbolic.regression.*;
import org.um.feri.ears.operators.gp.GPDepthBasedTreePruningOperator;
import org.um.feri.ears.operators.gp.GPOperator;
import org.um.feri.ears.operators.gp.GPTreeExpansionOperator;
import org.um.feri.ears.operators.gp.GPTreeSizePruningOperator;
import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.gp.*;
import org.um.feri.ears.util.Util;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class GeneticProgrammingExample {
    public static void main(String[] args) throws IOException {
        // srExample();
        // btExample();

        // treeGenEvalSymbolicExample();
        // treeGenBtExample();

        // symbolicRegressionDefaultAlgorithmRunExample(null);
        // behaviourTreeDefaultAlgorithmRunExample(null);

        // serializationTest();
        // deserealizationTest("gpAlgorithmState.ser");

        treeSizePruneExample();

    }

    public static void serializationTest(){
        List<Class<? extends Node>> baseFunctionNodeTypes = Arrays.asList(
                Repeat.class,
                Sequencer.class,
                Selector.class,
                Inverter.class
        );

        List<Class<? extends Node>> baseTerminalNodeTypes = Arrays.asList(
                RayHitObject.class,
                MoveForward.class,
                MoveSide.class,
                Rotate.class
        );

        UnityBTProblem sgp2 = new UnityBTProblem(baseFunctionNodeTypes, baseTerminalNodeTypes, 3, 5, 200, new GPDepthBasedTreePruningOperator(),
                new GPTreeExpansionOperator(), new GPTreeSizePruningOperator(), new GPRandomProgramSolution());

        List<ProgramSolution> programSolutions = new ArrayList<>();
        for (int i = 0; i < 300; i++){
            programSolutions.add(sgp2.getRandomSolution());
        }

        sgp2.bulkEvaluate(programSolutions);

        for (int i = 0; i < programSolutions.size(); i++){
            System.out.println("Fitness (" + i + "): " + programSolutions.get(i).getEval());
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("treePopulation.ser"))) {
            oos.writeObject(programSolutions);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deserealizationTest(String file){
        GPAlgorithm alg =  GPAlgorithm.deserializeAlgorithmState(file);
        alg.getPopulation().get(0).getTree().displayTree("test_bt", true);
    }

    public static void symbolicRegressionDefaultAlgorithmRunExample(String initialPopulationFilename){
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

        List<Target> evalData = Util.list( new Target().when("x", 0).targetIs(12),
                new Target().when("x", 1).targetIs(23),
                new Target().when("x", 2).targetIs(44),
                new Target().when("x", 3).targetIs(81),
                new Target().when("x", 4).targetIs(140),
                new Target().when("x", 5).targetIs(227),
                new Target().when("x", 6).targetIs(348),
                new Target().when("x", 7).targetIs(509),
                new Target().when("x", 8).targetIs(716),
                new Target().when("x", 9).targetIs(975),
                new Target().when("x", 10).targetIs(1292));

        List<Target> evalData2 = Util.list( new Target().when("x", 0).targetIs(0),
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

        List<Target> evalData3 = Util.list( new Target().when("x", 0).targetIs(-12),
                new Target().when("x", 1).targetIs(-15),
                new Target().when("x", 2).targetIs(-4),
                new Target().when("x", 3).targetIs(27),
                new Target().when("x", 4).targetIs(100),
                new Target().when("x", 5).targetIs(203),
                new Target().when("x", 6).targetIs(340),
                new Target().when("x", 7).targetIs(515),
                new Target().when("x", 8).targetIs(723),
                new Target().when("x", 9).targetIs(995),
                new Target().when("x", 10).targetIs(1308));


        SymbolicRegressionProblem sgp2 = new SymbolicRegressionProblem(baseFunctionNodeTypes, baseTerminalNodeTypes, 3, 8, 200, new GPDepthBasedTreePruningOperator(),
                new GPTreeExpansionOperator(), new GPTreeSizePruningOperator(), new GPRandomProgramSolution(), evalData);

        //GP algorithm execution example
        Task<ProgramSolution, ProgramProblem> symbolicRegressionTask = new Task<>(sgp2, StopCriterion.EVALUATIONS, 500000, 0, 0);


        GPAlgorithm alg = new DefaultGPAlgorithm(100, 0.95, 0.025, 2, initialPopulationFilename);
        RandomWalkGPAlgorithm rndAlg = new RandomWalkGPAlgorithm();

        try {
            long startTime = System.currentTimeMillis();
            System.out.println("Starting DefaultGpAlgorithm");
            ArrayList<ProgramSolution> solutions = new ArrayList<>();
            ArrayList<Double> solutionsRnd = new ArrayList<>();
            ProgramSolution sol;
            for (int i = 0; i < 1; i++){
                sol = alg.execute(symbolicRegressionTask);
                solutions.add(sol);
                System.out.println("Best fitness (DefaultGpAlgorithm) (for i = " + i + ") -> " + sol.getEval());
                alg.getPopulation().get(0).getTree().displayTree("test_bt", true);

                alg.resetToDefaultsBeforeNewRun();

                sol = rndAlg.execute(symbolicRegressionTask);
                solutionsRnd.add(sol.getEval());
                System.out.println("Best fitness (RandomWalkAlgorithm) (for i = " + i + ") -> " + sol.getEval());
                rndAlg.resetToDefaultsBeforeNewRun();
                alg.resetToDefaultsBeforeNewRun();
            }

            ProgramSolution maxSol = solutions.get(0);
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
            System.out.println("Tree Depth: " + sol.getProgram().treeDepth());
            sol.getProgram().displayTree("TestBTree");*/
        } catch (StopCriterionException e) {
            e.printStackTrace();
        }
    }

    public static void behaviourTreeDefaultAlgorithmRunExample(String initialPopulationFilename){
        List<Class<? extends Node>> baseFunctionNodeTypes = Arrays.asList(
                Repeat.class,
                Sequencer.class,
                Selector.class,
                Inverter.class
        );

        List<Class<? extends Node>> baseTerminalNodeTypes = Arrays.asList(
                RayHitObject.class,
                MoveForward.class,
                MoveSide.class,
                Rotate.class
        );

        UnityBTProblem sgp2 = new UnityBTProblem(baseFunctionNodeTypes, baseTerminalNodeTypes, 3, 8, 100, new GPDepthBasedTreePruningOperator(),
                new GPTreeExpansionOperator(), new GPTreeSizePruningOperator(), new GPRandomProgramSolution());

        //GP algorithm execution example
        Task<ProgramSolution, ProgramProblem> soccerTask = new Task<>(sgp2, StopCriterion.EVALUATIONS, 40000, 0, 0);


        GPAlgorithm alg = new DefaultGPAlgorithm(80, 0.95, 0.025, 2, initialPopulationFilename);

        try {
            long startTime = System.currentTimeMillis();
            System.out.println("Starting DefaultGpAlgorithm");
            ArrayList<ProgramSolution> solutions = new ArrayList<>();
            ProgramSolution sol;
            for (int i = 0; i < 1; i++){
                sol = alg.execute(soccerTask);
                solutions.add(sol);
                System.out.println("Best fitness (DefaultGpAlgorithm) (for i = " + i + ") -> " + sol.getEval());
                alg.resetToDefaultsBeforeNewRun();
            }

            ProgramSolution maxSol = solutions.get(0);
            for (int i = 0; i < solutions.size(); i++){
                if(solutions.get(i).getEval() < maxSol.getEval())
                    maxSol = solutions.get(i);
            }

            System.out.println("=====================================");
            System.out.println("Best fitness (DefaultGpAlgorithm)  -> " + maxSol.getEval());
            System.out.println("Best solution JSON: " + maxSol.getTree().toJsonString());

            long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;

            System.out.println("Elapsed time: " + elapsedTime + " s");
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
                RayHitObject.class,
                MoveForward.class,
                MoveSide.class,
                Rotate.class
        );

        UnityBTProblem sgp2 = new UnityBTProblem(baseFunctionNodeTypes, baseTerminalNodeTypes, 3, 5, 200, new GPDepthBasedTreePruningOperator(),
                new GPTreeExpansionOperator(), new GPTreeSizePruningOperator(), new GPRandomProgramSolution());

        //get current time
        long startTime = System.currentTimeMillis();
        List<ProgramSolution> programSolutions = new ArrayList<>();
        for (int i = 0; i < 30; i++){
            programSolutions.add(sgp2.getRandomSolution());
        }

        sgp2.bulkEvaluate(programSolutions);

        for (int i = 0; i < programSolutions.size(); i++){
            System.out.println("Fitness (" + i + "): " + programSolutions.get(i).getEval());
        }
        /*System.out.println("FinalFitness: " + sum);
        System.out.println("Average fitness: " + sum / programSolutions.size());
        //calculate elapsed time
        long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
        System.out.println("Elapsed time: " + elapsedTime + " s");*/
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

        SymbolicRegressionProblem sgp2 = new SymbolicRegressionProblem(baseFunctionNodeTypes, baseTerminalNodeTypes, 3, 8, 200, new GPDepthBasedTreePruningOperator(),
                new GPTreeExpansionOperator(), new GPTreeSizePruningOperator(), new GPRandomProgramSolution(), evalData);

        ProgramSolution programSolution = sgp2.getRandomSolution();
        sgp2.evaluate(programSolution);
        programSolution.getTree().printTree();
        System.out.println(programSolution.getEval());
    }

    public static void btExample(){
        List<Class<? extends Node>> baseFunctionNodeTypes = Arrays.asList(
                Repeat.class,
                Sequencer.class,
                Selector.class,
                Inverter.class
        );

        List<Class<? extends Node>> baseTerminalNodeTypes = Arrays.asList(
                RayHitObject.class,
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

    public static void treeSizePruneExample(){
        List<Class<? extends Node>> baseFunctionNodeTypes = Arrays.asList(
                Repeat.class,
                Sequencer.class,
                Selector.class,
                Inverter.class
        );

        List<Class<? extends Node>> baseTerminalNodeTypes = Arrays.asList(
                RayHitObject.class,
                MoveForward.class,
                MoveSide.class,
                Rotate.class
        );

        int maxNumOfNodes = 20;
        GPOperator treeSizePruningOperator = new GPTreeSizePruningOperator();

        UnityBTProblem sgp2 = new UnityBTProblem(baseFunctionNodeTypes, baseTerminalNodeTypes, 3, 8, maxNumOfNodes, new GPDepthBasedTreePruningOperator(),
                new GPTreeExpansionOperator(), new GPTreeSizePruningOperator(), new GPRandomProgramSolution());

        ProgramSolution programSolution = sgp2.getRandomSolution();

        programSolution.getTree().displayTree("test_operator1", true);

        if(programSolution.getTree().numberOfNodes() > maxNumOfNodes){
            programSolution = treeSizePruningOperator.execute(programSolution, sgp2);
        }

        programSolution.getTree().displayTree("test_operator2", true);
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
