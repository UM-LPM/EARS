package org.um.feri.ears.algorithms.gp;

import org.um.feri.ears.algorithms.GPAlgorithm;
import org.um.feri.ears.individual.generations.gp.GPProgramSolution;
import org.um.feri.ears.individual.generations.gp.GPRampedHalfAndHalf;
import org.um.feri.ears.individual.generations.gp.GPRandomProgramSolution;
import org.um.feri.ears.individual.representations.gp.Node;
import org.um.feri.ears.individual.representations.gp.Target;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.Selector;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.Sequencer;
import org.um.feri.ears.individual.representations.gp.symbolic.regression.VarNode;
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

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class GPAlgorithmExecutor implements Serializable {

    public static final long serialVersionUID = -4197237531018661888L;
    public static GPAlgorithmExecutor Instance;
    private GPAlgorithm gpAlgorithm;
    private Configuration configuration;

    private ArrayList<GPAlgorithmRunStats> gpAlgorithmRunStats;

    // Constructors
    public GPAlgorithmExecutor(boolean createInstance) {
        if(createInstance) {
            if (Instance != null) {
                throw new RuntimeException("GPAlgorithmExecutor already initialized");
            }
            Instance = this;
        }

        this.gpAlgorithmRunStats = new ArrayList<>();
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
            // TODO Add support for Symbolic regression
            //programProblem = new SymbolicRegressionProblem(problemName, baseFunctionNodeTypes, baseTerminalNodeTypes, minTreeDepth, maxTreeStartDepth, maxTreeEndDepth, maxTreeSize, feasibilityGPOperators, bloatControlOperators, programSolutionGenerator, evalData);
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
        else if (gpAlgorithmClass == PredefinedEncapsNodesGPAlgorithm.class){
            if(gpAlgorithmArgs.length != 5){
                throw new IllegalArgumentException("gpAlgorithmArgs must have 5 values");
            }
            this.gpAlgorithm = new PredefinedEncapsNodesGPAlgorithm((int)gpAlgorithmArgs[0], (double)gpAlgorithmArgs[1], (double)gpAlgorithmArgs[2], (double)gpAlgorithmArgs[3], (int)gpAlgorithmArgs[4], task);
        }
        else {
            throw new IllegalArgumentException("gpAlgorithmClass not supported");
        }

        this.gpAlgorithm.setDebug(debugMode);
    }

    public int setEARSConfiguration(RunConfiguration runConfiguration){
        EARSConfiguration earsConfiguration = runConfiguration.EARSConfiguration;
        int generations = 0;
        ProgramProblem programProblem = runConfiguration.EARSConfiguration.ProblemType == GPProblemType.SYMBOLIC? new SymbolicRegressionProblem() : new UnityBTProblem();
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
        // If algorithm is already initialized, set only the task
        if(this.gpAlgorithm == null) {
            if (earsConfiguration.AlgorithmType == GPAlgorithmType.DGP) {
                this.gpAlgorithm = new DefaultGPAlgorithm(100, 0.9, 0.1, 4, task, null);
            } else if (earsConfiguration.AlgorithmType == GPAlgorithmType.EGP) {
                this.gpAlgorithm = new ElitismGPAlgorithm(100, 0.9, 0.05, 0.1, 4, task, null);
            } else if (earsConfiguration.AlgorithmType == GPAlgorithmType.PENGP) {
                this.gpAlgorithm = new PredefinedEncapsNodesGPAlgorithm(100, 0.9, 0.05, 0.1, 4, task);
            }
            this.gpAlgorithm.setDebug(true);
        }else{
            // set task for the algorithm
            this.gpAlgorithm.setTask(task);
        }

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
            this.gpAlgorithm.setElitismProbability(earsConfiguration.ElitismProb);
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

        // RequestBodyParams
        RequestBodyParams requestBodyParams = new RequestBodyParams(configuration.CoordinatorURI, configuration.EvalEnvInstanceURIs.split(","),
                configuration.JsonBodyDestFilePath, configuration.DestinationFilePath);

        programProblem.setRequestBodyParams(requestBodyParams);
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

        // EvalData (For symbolic regression only)
        if(programProblem instanceof SymbolicRegressionProblem){
            ((SymbolicRegressionProblem)programProblem).setEvaluationSet(Arrays.asList(earsConfiguration.EvalData));

            // find unique indexes in the evalData contextState
            List<String> uniqueIndexes = new ArrayList<>();
            for (Target target : ((SymbolicRegressionProblem) programProblem).getEvalData()) {
                for (String key : target.getContextState().keySet()) {
                    if (!uniqueIndexes.contains(key)) {
                        uniqueIndexes.add(key);
                    }
                }
            }

            // Set unique indexes to VarNode
            VarNode.variables = uniqueIndexes;
        }

        return generations;
    }

    public void runConfigurations(String configurationFile, String saveGPAlgorithmStateFilename){
        if(configuration == null && configurationFile != null){
            configuration = Configuration.deserializeFromFile(configurationFile);
        }

        gpAlgorithmRunStats.clear();
        try {
            for (int i = 0; i < configuration.Configurations.size(); i++) {
                for (int j = 0; j < configuration.Configurations.get(i).NumberOfReruns; j++) {
                    gpAlgorithm.execute(this, configuration.Configurations.get(i), saveGPAlgorithmStateFilename);
                    gpAlgorithmRunStats.add(gpAlgorithm.getStats());

                    // Save final GPAlgorithm run state to file for each configuration
                    GPAlgorithm.serializeAlgorithmState(gpAlgorithm, i + "_" + j + "_RunConfigurationGPAlgorithm.ser" );

                    // Save current GPAlgorithmExecutor state to file
                    serializeGPAlgorithmExecutorState(this, "GPAlgorithmExecutor.ser");
                }
                restartUnityInstances(false);
            }

        } catch (StopCriterionException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void execute(int numOfGens, String saveGPAlgorithmStateFilename) {
        gpAlgorithm.execute(numOfGens, saveGPAlgorithmStateFilename);
    }

    /**
     * Closes all existing Unity instance and restarts them if @restartInstances is true
     * @param restartInstances
     */
    public void restartUnityInstances(boolean restartInstances){
        // 1. Close all running GeneralTrainingPlatformForMAS instances
        try {
            Runtime.getRuntime().exec("taskkill /F /IM " + configuration.UnityGameFile);
            Thread.sleep(5000);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        if(restartInstances) {
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
        return gpAlgorithm.getDefaultGPAlgorithmStateFilename();
    }

    public ArrayList<GPAlgorithmRunStats> getGpAlgorithmRunStats() {
        return gpAlgorithmRunStats;
    }

    public static void serializeGPAlgorithmExecutorState(GPAlgorithmExecutor gpAlgorithmExecutor, String filename) {
        System.out.println("Serializing current gpAlgorithmExecutor");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(gpAlgorithmExecutor);
        } catch (IOException e) {
            e.printStackTrace();
        }

        GPAlgorithm.CAN_RUN = true;
    }

    public static GPAlgorithmExecutor deserializeGPAlgorithmExecutorState(String filename){
        GPAlgorithmExecutor algExecutor = new GPAlgorithmExecutor(false);
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            algExecutor = (GPAlgorithmExecutor) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return algExecutor;
    }

}
