package org.um.feri.ears.algorithms.gp;

import org.um.feri.ears.algorithms.GPAlgorithm;
import org.um.feri.ears.individual.generations.gp.GPProgramSolution;
import org.um.feri.ears.individual.generations.gp.GPRampedHalfAndHalf;
import org.um.feri.ears.individual.generations.gp.GPRandomProgramSolution;
import org.um.feri.ears.individual.representations.gp.Node;
import org.um.feri.ears.individual.representations.gp.Target;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.Selector;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.Sequencer;
import org.um.feri.ears.operators.gp.FeasibilityGPOperator;
import org.um.feri.ears.operators.gp.GPOperator;
import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.gp.ProgramProblem;
import org.um.feri.ears.problems.gp.ProgramSolution;
import org.um.feri.ears.problems.gp.SymbolicRegressionProblem;
import org.um.feri.ears.problems.gp.UnityBTProblem;
import org.um.feri.ears.util.*;

import javax.swing.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class GPAlgorithmExecutor {
    public static GPAlgorithmExecutor Instance;
    private GPAlgorithm gpAlgorithm;
    private Configuration configuration;

    // Constructors
    public GPAlgorithmExecutor() {
        if(Instance != null)
        {
            throw new RuntimeException("GPAlgorithmExecutor already initialized");
        }
        Instance = this;
    }

    public GPAlgorithmExecutor(GPAlgorithm gpAlgorithm) {
        this.gpAlgorithm = gpAlgorithm;
    }
    public GPAlgorithmExecutor(GPAlgorithm gpAlgorithm, Configuration configuration) {
        this.gpAlgorithm = gpAlgorithm;
        this.configuration = configuration;
    }

    // Methods
    public void initializeGpAlgorithmStateFromFile(String initialStateFile){
        if(initialStateFile != null){
            this.gpAlgorithm = GPAlgorithm.deserializeAlgorithmState(initialStateFile);
        }
        else {
            throw new IllegalArgumentException("initialStateFile cannot be null");
        }
    }

    public void initializeGpAlgorithmState(String problemName, List<Class<? extends Node>> baseFunctionNodeTypes, List<Class<? extends Node>> baseTerminalNodeTypes, List<Target> evalData, int minTreeDepth, int maxTreeStartDepth, int maxTreeEndDepth, int maxTreeSize, FeasibilityGPOperator[] feasibilityGPOperators, GPOperator[] bloatControlOperators, GPProgramSolution programSolutionGenerator, int maxEvaluations, boolean debugMode, Class<? extends GPAlgorithm> gpAlgorithmClass, Object ...gpAlgorithmArgs){
        ProgramProblem programProblem = null;
        if(evalData != null && evalData.size() > 0){
            // Symbolic regression problem
            programProblem = new SymbolicRegressionProblem(problemName, baseFunctionNodeTypes, baseTerminalNodeTypes, minTreeDepth, maxTreeStartDepth, maxTreeEndDepth, maxTreeSize, feasibilityGPOperators, bloatControlOperators, programSolutionGenerator, evalData);
        }
        else{
            // Behavior tree problem
            programProblem = new UnityBTProblem(problemName, baseFunctionNodeTypes, baseTerminalNodeTypes, minTreeDepth, maxTreeStartDepth, maxTreeEndDepth, maxTreeSize, feasibilityGPOperators, bloatControlOperators, programSolutionGenerator);
        }

        Task<ProgramSolution, ProgramProblem> task = new Task<>(programProblem, StopCriterion.EVALUATIONS, maxEvaluations, 0, 0);

        if(gpAlgorithmClass == DefaultGPAlgorithm.class){
            //this.gpAlgorithm =  new DefaultGPAlgorithm(100, 0.9, 0.05, 4, task, null); // Collector_conf_4
            if(gpAlgorithmArgs.length != 4){
                throw new IllegalArgumentException("gpAlgorithmArgs must have 4 values");
            }
            this.gpAlgorithm =  new DefaultGPAlgorithm((int)gpAlgorithmArgs[0], (double)gpAlgorithmArgs[1], (double)gpAlgorithmArgs[2], (int)gpAlgorithmArgs[3], task, null); // Collector_conf_4
        }
        else if (gpAlgorithmClass == ElitismGPAlgorithm.class){
            if(gpAlgorithmArgs.length != 5){
                throw new IllegalArgumentException("gpAlgorithmArgs must have 5 values");
            }
            this.gpAlgorithm = new ElitismGPAlgorithm((int)gpAlgorithmArgs[0], (double)gpAlgorithmArgs[1], (double)gpAlgorithmArgs[2], (double)gpAlgorithmArgs[3], (int)gpAlgorithmArgs[4], task, null);
        }
        else {
            throw new IllegalArgumentException("gpAlgorithmClass not supported");
        }

        this.gpAlgorithm.setDebug(debugMode);
    }

    public int setEARSConfiguration(RunConfiguration runConfiguration){
        EARSConfiguration earsConfiguration = runConfiguration.EARSConfiguration;
        int generations = 0;
        ProgramProblem programProblem = this.gpAlgorithm.getTask().problem;
        Task<ProgramSolution, ProgramProblem> task = new Task<>(programProblem, StopCriterion.EVALUATIONS, 0, 0, 0);

        if(earsConfiguration.FitnessEvaluations > 0){
            task = new Task<>(programProblem, StopCriterion.EVALUATIONS, earsConfiguration.FitnessEvaluations, 0, 0);
            generations = ((int) Math.ceil((float)earsConfiguration.FitnessEvaluations / earsConfiguration.PopSize)) - 1;  // -1 because init population evaluation takes one generation of evals
        }
        else if(earsConfiguration.Generations > 0){
            task = new Task<>(programProblem, StopCriterion.ITERATIONS, earsConfiguration.Generations, 0, 0);
            generations = earsConfiguration.Generations;
        }

        // GPAlgorithmType
        if(earsConfiguration.AlgorithmType == GPAlgorithmType.DGP){
            this.gpAlgorithm =  new DefaultGPAlgorithm(100, 0.9, 0.1, 4, task, null); // Collector_conf_4
        }
        else if(earsConfiguration.AlgorithmType == GPAlgorithmType.EGP){
            this.gpAlgorithm =  new ElitismGPAlgorithm(100, 0.9,0.05, 0.1, 4, task, null); // Collector_conf_4
        }
        this.gpAlgorithm.setDebug(true);
        // ProblemName
        programProblem.setName(earsConfiguration.ProblemName);
        // PopSize
        this.gpAlgorithm.setPopSize(earsConfiguration.PopSize);
        // CrossoverProb
        this.gpAlgorithm.setCrossoverProbability(earsConfiguration.CrossoverProb);
        // MutationProb
        this.gpAlgorithm.setMutationProbability(earsConfiguration.MutationProb);
        // ElitisProb
        if(this.gpAlgorithm instanceof ElitismGPAlgorithm)
            ((ElitismGPAlgorithm)this.gpAlgorithm).setElitismProbability(earsConfiguration.ElitismProb);
        // NumOfTournaments
        this.gpAlgorithm.setNumberOfTournaments(earsConfiguration.NumOfTournaments);
        // MinTreeDepth
        programProblem.setMinTreeDepth(earsConfiguration.MinTreeDepth);
        // MaxTreeStartDepth
        programProblem.setMaxTreeStartDepth(earsConfiguration.MaxTreeStartDepth);
        // MaxTreeEndDepth
        programProblem.setMaxTreeEndDepth(earsConfiguration.MaxTreeEndDepth);
        // MaxTreeSize
        programProblem.setMaxTreeSize(earsConfiguration.MaxTreeSize);
        // InitPopGeneratorMethod
        if(Objects.equals(earsConfiguration.InitPopGeneratorMethod, Configuration.InitPopGeneratorMethod.Random.toString())){
            programProblem.setProgramSolutionGenerator(new GPRandomProgramSolution());
        }
        else if(Objects.equals(earsConfiguration.InitPopGeneratorMethod, Configuration.InitPopGeneratorMethod.RampedHalfAndHalfMethod.toString())){
            programProblem.setProgramSolutionGenerator(new GPRampedHalfAndHalf());
        }
        // EvalEnvInstanceURIs
        programProblem.setEvalEnvInstanceURIs(configuration.EvalEnvInstanceURIs.split(","));
        // JsonBodyDestFolderPath
        programProblem.setJsonBodyDestFolderPath(configuration.JsonBodyDestFilePath);

        // Seq, Sel number of children
        Selector.MAX_CHILDREN = earsConfiguration.SeqSelNumOfChildren;
        Sequencer.MAX_CHILDREN = earsConfiguration.SeqSelNumOfChildren;

        // Functions
        programProblem.setBaseFunctionNodeTypesFromStringList(Arrays.asList(earsConfiguration.Functions.split(",")));
        // Terminals
        programProblem.setBaseTerminalNodeTypesFromStringList(Arrays.asList(earsConfiguration.Terminals.split(",")));

        // FeasibilityControlOperators
        programProblem.setFeasibilityControlOperatorsFromStringArray(earsConfiguration.FeasibilityControlOperators);
        // BloatControlOperators
        programProblem.setBloatControlOperatorsFromStringArray(earsConfiguration.BloatControlOperators);

        return generations;
    }

    public void runConfigurations(String configurationFile, String saveGPAlgorithmStateFilename){
        if(configuration == null && configurationFile != null){
            configuration = Configuration.deserialize(configurationFile);
        }

        // Run final configurations with meta nodes
        for (int i = 0; i < configuration.Configurations.size(); i++) {
            RunConfiguration runConfiguration = configuration.Configurations.get(i);
            System.out.println("Run configuration: " + i + " (" + runConfiguration.Name + ")");

            // 1. Set EARS configuration
            int generations = setEARSConfiguration(runConfiguration);

            // 2. Save Unity configuration
            Configuration.serializeUnityConfig(runConfiguration, configuration.UnityConfigDestFilePath);

            // 3 Start Unity Instances
            restartUnityInstances();

            // 6. Run algorithm
            runGPAlgorithm(generations, saveGPAlgorithmStateFilename);

            System.out.println("Run configuration: " + i + " (" + runConfiguration.Name + ") done");
        }
    }

    public void runGPAlgorithm(int numOfGens, String saveGPAlgorithmStateFilename) {
        try {
            try {
                if (numOfGens <= 0)
                    numOfGens = Integer.MAX_VALUE;

                for (int i = 0; i < numOfGens; i++) {
                    ProgramSolution sol = gpAlgorithm.executeGeneration();
                    // Print current gpAlgorithm statistics to console
                    if(this.gpAlgorithm.isDebug()){
                        System.out.println("Generation: " + gpAlgorithm.getTask().getNumberOfIterations() + ", Best Fitness: " + gpAlgorithm.getBest().getEval() + ", Avg Fitness: " + gpAlgorithm.getAvgGenFitnesses().get(gpAlgorithm.getAvgGenFitnesses().size() - 1) + ", Avg Tree Depth: " + gpAlgorithm.getAvgGenTreeDepths().get(gpAlgorithm.getAvgGenTreeDepths().size() - 1) + ", Avg Tree Size: " + gpAlgorithm.getAvgGenTreeSizes().get(gpAlgorithm.getAvgGenTreeSizes().size() - 1));
                        System.out.println("Best Individual: " + gpAlgorithm.getBest().getTree().toJsonString());
                    }

                    if (saveGPAlgorithmStateFilename == null || saveGPAlgorithmStateFilename.length() == 0) {
                        // Serialize algorithm state
                        GPAlgorithm.serializeAlgorithmState(gpAlgorithm, getDefaultGPAlgorithmStateFilename());
                    } else {
                        // Serialize algorithm state
                        GPAlgorithm.serializeAlgorithmState(gpAlgorithm, saveGPAlgorithmStateFilename);
                    }
                    if (sol != null)
                        break;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number of generations to run (-1 for run to the end).", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (StopCriterionException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void restartUnityInstances(){
        // 1. Close all running GeneralTrainingPlatformForMAS instances
        try {
            Runtime.getRuntime().exec("taskkill /F /IM " + configuration.UnityGameFile);
            Thread.sleep(5000);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


        // 2. Rerun GeneralTrainingPlatformForMAS (Unity)
        for (int instances = 0; instances < configuration.EvalEnvInstanceURIs.split(",").length; instances++) {
            try {
                Runtime.getRuntime().exec("cmd /c start " + configuration.UnityExeLocation + configuration.UnityGameFile);
                Thread.sleep(2000);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 3. Wait for Unity instances to start
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void loadDefaultConfiguration(){
        // Load first configuration in the list
        if(configuration != null){
            setEARSConfiguration(configuration.Configurations.get(0));

        }
    }

    // Getters and Setters
    public Task<ProgramSolution, ProgramProblem> getTask() {
        return gpAlgorithm.getTask();
    }

    public GPAlgorithm getGpAlgorithm() {
        return gpAlgorithm;
    }

    public void setGpAlgorithm(GPAlgorithm gpAlgorithm) {
        this.gpAlgorithm = gpAlgorithm;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public ProgramProblem getProgramProblem() {
        return this.gpAlgorithm.getTask().problem;
    }

    public String getDefaultGPAlgorithmStateFilename(){
        return getProgramProblem().getName() + "_" + getGpAlgorithm().getPopSize() + "_" + getGpAlgorithm().getCrossoverProbability() + "_" + getGpAlgorithm().getElitismProbability() + "_" + getGpAlgorithm().getMutationProbability() + "_" + getGpAlgorithm().getNumberOfTournaments() + "_" + getProgramProblem().getMinTreeDepth() + "_" + getProgramProblem().getMaxTreeStartDepth() + "_" + getProgramProblem().getMaxTreeEndDepth() + "_" + getProgramProblem().getMaxTreeSize() + "_gpAlgorithmState_" + getFormattedDate() + ".ser";
    }

    public String getFormattedDate() {
        // Get current date
        LocalDate date = LocalDate.now();
        // Create a new string builder
        // Append the day
        String formattedDate = String.format("%02d", date.getDayOfMonth()) +
                // Append the month
                String.format("%02d", date.getMonthValue()) +
                // Append the year
                date.getYear();

        return formattedDate;
    }

}
