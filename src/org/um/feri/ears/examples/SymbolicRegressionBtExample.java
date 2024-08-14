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
import org.um.feri.ears.individual.representations.gp.behaviour.tree.*;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.movement.MoveForward;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.movement.MoveSide;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.robostrike.RotateTurret;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.robostrike.Shoot;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.sensors.RayHitObject;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.movement.Rotate;
import org.um.feri.ears.individual.representations.gp.Target;
import org.um.feri.ears.individual.representations.gp.symbolic.regression.*;
import org.um.feri.ears.operators.gp.*;
import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.gp.*;
import org.um.feri.ears.util.Util;

import java.net.HttpURLConnection;
import java.net.URL;

public class SymbolicRegressionBtExample { // TODO remove
    public static void main(String[] args) throws IOException {
        // srExample();
        // btExample();

        // treeGenEvalSymbolicExample();
        // treeGenBtExample();

        //symbolicRegressionDefaultAlgorithmRunExample(null);
        //behaviourTreeDefaultAlgorithmRunExample(null);

        // serializationTest();
        // deserealizationTest("treePopulation.ser");

        goalBtNodeExample();
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

        UnityBTProblem sgp2 = new UnityBTProblem(baseFunctionNodeTypes, baseTerminalNodeTypes, 3, 5, 5, 200, new FeasibilityGPOperator[]{ new GPTreeExpansionOperator(), new GPDepthBasedTreePruningOperator()},
                new GPOperator[]{}, new GPRandomProgramSolution());

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


        SymbolicRegressionProblem sgp2 = new SymbolicRegressionProblem(baseFunctionNodeTypes, baseTerminalNodeTypes, 3, 8, 8, 200, new FeasibilityGPOperator[]{ new GPTreeExpansionOperator(), new GPDepthBasedTreePruningOperator()},
                new GPOperator[]{}, new GPRandomProgramSolution(), evalData);

        //GP algorithm execution example
        Task<ProgramSolution, ProgramProblem> symbolicRegressionTask = new Task<>(sgp2, StopCriterion.EVALUATIONS, 10000, 0, 0);


        GPAlgorithm alg = new DefaultGPAlgorithm(100, 0.95, 0.025, 2, initialPopulationFilename);
        RandomWalkGPAlgorithm rndAlg = new RandomWalkGPAlgorithm();

        try {
            long startTime = System.currentTimeMillis();
            System.out.println("Starting DefaultGpAlgorithm");
            ArrayList<ProgramSolution> solutions = new ArrayList<>();
            ArrayList<Double> solutionsRnd = new ArrayList<>();
            ProgramSolution sol;
            for (int i = 0; i < 20; i++){
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

        UnityBTProblem sgp2 = new UnityBTProblem(baseFunctionNodeTypes, baseTerminalNodeTypes, 3, 8, 8, 100, new FeasibilityGPOperator[]{ new GPTreeExpansionOperator(), new GPDepthBasedTreePruningOperator()},
                new GPOperator[]{}, new GPRandomProgramSolution());

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

        UnityBTProblem sgp2 = new UnityBTProblem(baseFunctionNodeTypes, baseTerminalNodeTypes, 3, 5, 5, 200, new FeasibilityGPOperator[]{ new GPTreeExpansionOperator(), new GPDepthBasedTreePruningOperator()},
                new GPOperator[]{}, new GPRandomProgramSolution());

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

        SymbolicRegressionProblem sgp2 = new SymbolicRegressionProblem(baseFunctionNodeTypes, baseTerminalNodeTypes, 3, 8, 8, 200,new FeasibilityGPOperator[]{ new GPTreeExpansionOperator(), new GPDepthBasedTreePruningOperator()},
                new GPOperator[]{}, new GPRandomProgramSolution(), evalData);

        ProgramSolution programSolution = sgp2.getRandomSolution();
        sgp2.evaluate(programSolution);
        programSolution.getTree().printTree();
        System.out.println(programSolution.getEval());
    }

    /*public static void btExample(){
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
    }*/

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

    public static void goalBtNodeExample(){
        //sol.getProgram().displayTree("TestBTree");

        List<Class<? extends Node>> baseFunctionNodeTypes = Arrays.asList(
                //Repeat.class,
                Sequencer.class,
                Selector.class,
                Inverter.class
        );

        List<Class<? extends Node>> baseTerminalNodeTypes = Arrays.asList(
                RayHitObject.class,
                MoveForward.class,
                Rotate.class,
                Shoot.class,
                RotateTurret.class
        );

        List<Class<? extends Node>> baseTerminalNodeTypes2 = Arrays.asList(
                RayHitObject.class,
                MoveForward.class,
                Rotate.class,
                Shoot.class,
                RotateTurret.class,
                EncapsulatedNode.class
        );

        for (int i =0; i < 1; i++) {

            GPRandomProgramSolution randomProgramSolution = new GPRandomProgramSolution();
            UnityBTProblem sgp2 = new UnityBTProblem(baseFunctionNodeTypes, baseTerminalNodeTypes, 3, 4, 5, 15, new FeasibilityGPOperator[]{new GPTreeExpansionOperator(), new GPDepthBasedTreePruningOperator()},
                    new GPOperator[]{}, randomProgramSolution);

            List<ProgramSolution> programSolutions = new ArrayList<>();

            programSolutions.add(sgp2.getRandomSolution()); // Generate program solution

            List<EncapsulatedNodeDefinition> encapsulatedNodeDefinitions = new ArrayList<>(); // Define goal nodes
            encapsulatedNodeDefinitions.add(new EncapsulatedNodeDefinition("GoalNode1", programSolutions.get(0).getTree().getRootNode()));
            randomProgramSolution.setGoalNodeDefinitions(encapsulatedNodeDefinitions);

            sgp2.setBaseTerminalNodeTypes(baseTerminalNodeTypes2);

            programSolutions.add(sgp2.getRandomSolution()); // Generate program solution

            programSolutions.get(0).getTree().displayTree("TestBTree1", false);
            programSolutions.get(1).getTree().displayTree("TestBTree2", false);

            // Test GP Operators with EncapsulatedNode
            // Mutation operator
            if(false) {
                System.out.println("GPSubtreeMutation execution");
                GPSubtreeMutation gpsm = new GPSubtreeMutation(1.0);
                ProgramSolution mutationSolution = gpsm.execute(programSolutions.get(1), sgp2);

                mutationSolution.getTree().displayTree("TestBTree3", false);
            }

            // Crossover operator
            if(false) {
                System.out.println("GPSinglePointCrossover execution");
                GPSinglePointCrossover gpspc = new GPSinglePointCrossover(1.0);
                ProgramSolution[] crossoverSolutions = gpspc.execute(new ProgramSolution[]{programSolutions.get(0), programSolutions.get(1)}, sgp2);

                crossoverSolutions[0].getTree().displayTree("TestBTree3", false);
                crossoverSolutions[1].getTree().displayTree("TestBTree4", false);
            }

            // Expansion operator
            // Tested in the previous examples of Crossover & Mutation
            // Depth based operator
            // Tested in the previous examples of Crossover & Mutation

            // Tree size pruning operator
            if(false) {
                System.out.println("GPTreeSizePruningOperator execution");
                GPTreeSizePruningOperator gpspo = new GPTreeSizePruningOperator(GPTreeSizePruningOperator.OperatorType.CLOSEST_TO_MAX_TREE_NODES);
                ProgramSolution pruningSolution = gpspo.execute(programSolutions.get(1), sgp2);

                pruningSolution.getTree().displayTree("TestBTree3", false);
            }
            // Node call frequency operator
            if(false) {
                System.out.println("NodeCallFrequencyCountPruningOperator execution");
                int treeSize = programSolutions.get(1).getTree().treeSize();
                int[] nodeCallFrequency = new int[treeSize];
                nodeCallFrequency[0] = 100;
                nodeCallFrequency[1] = 100;
                nodeCallFrequency[2] = 100;
                //nodeCallFrequency[3] = 100;
                //nodeCallFrequency[4] = 100;
                for (int j = 3; j < treeSize; j++) {
                    nodeCallFrequency[j] = (25 + (treeSize - j)) * 2;
                    //System.out.println("Node call frequency (" + j + "): " + nodeCallFrequency[j]);
                }

                programSolutions.get(1).setNodeCallFrequencyCount(nodeCallFrequency);
                GPNodeCallFrequencyCountPruningOperator gpnf = new GPNodeCallFrequencyCountPruningOperator(60,0.5);
                ProgramSolution nodeCallFrequencySolution = gpnf.execute(programSolutions.get(1), sgp2);

                nodeCallFrequencySolution.getTree().displayTree("TestBTree3", false);
            }
        }
    }
}
