package org.um.feri.ears.algorithms.gp;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.eclipse.swt.program.Program;
import org.um.feri.ears.algorithms.GPAlgorithm;
import org.um.feri.ears.individual.generations.gp.GPProgramSolution;
import org.um.feri.ears.individual.generations.gp.GPRampedHalfAndHalf;
import org.um.feri.ears.individual.generations.gp.GPRandomProgramSolution;
import org.um.feri.ears.individual.representations.gp.Node;
import org.um.feri.ears.individual.representations.gp.Target;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.Selector;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.Sequencer;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.sensors.GridCellContainsObject;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.sensors.RayHitObject;
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
import org.um.feri.ears.util.gp_stats.GPAlgorithmMultiConfigurationsProgressData;
import org.um.feri.ears.util.gp_stats.GPAlgorithmMultiRunProgressData;
import org.um.feri.ears.util.gp_stats.GPAlgorithmRunProgressData;
import org.um.feri.ears.util.random.RNG;
import org.yaml.snakeyaml.util.Tuple;

import java.io.*;
import java.util.*;

public class GPAlgorithmExecutor implements Serializable {

    public static final long serialVersionUID = -4197237531018661888L;
    public static GPAlgorithmExecutor Instance;
    protected GPAlgorithm gpAlgorithm;
    protected Configuration configuration;

    protected int configurationIndex;
    protected int runIndex;

    protected GPAlgorithmMultiConfigurationsProgressData multiConfigurationsProgressData;
    protected ArrayList<GPAlgorithmConfigurationRunStats> gpAlgorithmConfigurationsRunStats;

    // Constructors
    public GPAlgorithmExecutor(boolean createInstance) {
        if(createInstance) {
            if (Instance != null) {
                throw new RuntimeException("GPAlgorithmExecutor already initialized");
            }
            Instance = this;
        }

        this.gpAlgorithmConfigurationsRunStats = new ArrayList<>();
        configurationIndex = 0;
        runIndex = 0;
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

    public void initializeGpAlgorithmState(String problemName, List<Class<? extends Node>> baseFunctionNodeTypes, List<Class<? extends Node>> baseTerminalNodeTypes, List<Target> evalData, int minTreeDepth, int maxTreeStartDepth, int maxTreeEndDepth, int maxTreeSize, FeasibilityGPOperator[] feasibilityGPOperators, GPOperator[] bloatControlOperators, GPProblemEvaluatorType problemEvaluatorType, LastEvalIndividualFitnessesRatingCompositionType lastEvalIndividualFitnessesRatingCompositionType, GPProgramSolution programSolutionGenerator, int maxEvaluations, boolean debugMode, Class<? extends GPAlgorithm> gpAlgorithmClass, Object ...gpAlgorithmArgs){
        ProgramProblem programProblem = null;
        if(evalData != null && evalData.size() > 0){
            // Symbolic regression problem
            // TODO Add support for Symbolic regression
            //programProblem = new SymbolicRegressionProblem(problemName, baseFunctionNodeTypes, baseTerminalNodeTypes, minTreeDepth, maxTreeStartDepth, maxTreeEndDepth, maxTreeSize, feasibilityGPOperators, bloatControlOperators, programSolutionGenerator, evalData);
        }
        else{
            // Behavior tree problem
            programProblem = new UnityBTProblem(problemName, baseFunctionNodeTypes, baseTerminalNodeTypes, minTreeDepth, maxTreeStartDepth, maxTreeEndDepth, maxTreeSize, feasibilityGPOperators, bloatControlOperators, problemEvaluatorType, lastEvalIndividualFitnessesRatingCompositionType, programSolutionGenerator);
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
            if(gpAlgorithmArgs.length != 6){
                throw new IllegalArgumentException("gpAlgorithmArgs must have 5 values");
            }
            this.gpAlgorithm = new PredefinedEncapsNodesGPAlgorithm((int)gpAlgorithmArgs[0], (double)gpAlgorithmArgs[1], (double)gpAlgorithmArgs[2], (double)gpAlgorithmArgs[3], (int)gpAlgorithmArgs[4], (int)gpAlgorithmArgs[5], RequiredEvalsCalcMethod.POP_SIZE, null, task);
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
                this.gpAlgorithm = new PredefinedEncapsNodesGPAlgorithm(100, 0.9, 0.05, 0.1, 4, 0, RequiredEvalsCalcMethod.POP_SIZE, null, task);
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

        // RayHitObjectTargetGameObjectCount, RayHitObjectRayCount
        if(earsConfiguration.TargetGameObjectCount > 0){
            RayHitObject.TARGET_GAME_OBJECT_COUNT = earsConfiguration.TargetGameObjectCount;
            GridCellContainsObject.TARGET_GAME_OBJECT_COUNT = earsConfiguration.TargetGameObjectCount;
        }
        if(earsConfiguration.RayHitObjectRayCount > 0)
            RayHitObject.RAY_INDEX_COUNT = earsConfiguration.RayHitObjectRayCount;

        // GridCellContainsObjectGridSizeX, GridCellContainsObjectGridSizeY
        if(earsConfiguration.GridCellContainsObjectGridSizeX > 0)
            GridCellContainsObject.GRID_SIZE_X = earsConfiguration.GridCellContainsObjectGridSizeX;
        if(earsConfiguration.GridCellContainsObjectGridSizeY > 0)
            GridCellContainsObject.GRID_SIZE_Y = earsConfiguration.GridCellContainsObjectGridSizeY;

        // Functions
        programProblem.setBaseFunctionNodeTypesFromStringList(Arrays.asList(earsConfiguration.Functions.split(",")));
        // Terminals
        programProblem.setBaseTerminalNodeTypesFromStringList(Arrays.asList(earsConfiguration.Terminals.split(",")));

        // FeasibilityControlOperators
        programProblem.setFeasibilityControlOperatorsFromStringArray(earsConfiguration.FeasibilityControlOperators);
        // BloatControlOperators
        programProblem.setBloatControlOperatorsFromStringArray(earsConfiguration.BloatControlOperators);

        // ProblemEvaluatorType
        programProblem.setProblemEvaluatorType(earsConfiguration.ProblemEvaluatorType);

        // LastEvalIndividualFitnessesRatingCompositionType
        programProblem.setLastEvalIndividualFitnessesRatingCompositionType(earsConfiguration.LastEvalIndividualFitnessesRatingCompositionType);

        // Hall of Fame
        this.gpAlgorithm.setHallOfFameSize(earsConfiguration.HallOfFameSize);

        // Required Evals Calc Method
        if (configuration.FinalMasterTournamentsConfiguration != null && configuration.FinalMasterTournamentsConfiguration.FinalMasterTournamentsComparisonType == FinalMasterTournamentsComparisonType.Evaluations) {
            this.gpAlgorithm.setRequiredEvalsCalcMethod(earsConfiguration.RequiredEvalsCalcMethod);
            this.gpAlgorithm.setRequiredEvalsCalcMethodParams(earsConfiguration.RequiredEvalsCalcMethodParams);
            System.out.println("Setting RequiredEvalsCalcMethod to: " + earsConfiguration.RequiredEvalsCalcMethod + ", For " + runConfiguration.Name);
        }
        else {
            this.gpAlgorithm.setRequiredEvalsCalcMethod(RequiredEvalsCalcMethod.POP_SIZE);
            this.gpAlgorithm.setRequiredEvalsCalcMethodParams(new HashMap<>());
            System.out.println("Setting RequiredEvalsCalcMethod to: POP_SIZE for " + runConfiguration.Name);
        }

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

        if(configurationIndex ==  0 && runIndex == 0) {
            gpAlgorithmConfigurationsRunStats.clear();
            multiConfigurationsProgressData = new GPAlgorithmMultiConfigurationsProgressData(configuration.MultiConfigurationPrograssDataFilePath);
        }

        try {
            for (; configurationIndex < configuration.Configurations.size(); configurationIndex++) {
                GPAlgorithmConfigurationRunStats gpAlgorithmConfigurationRunStats;

                if(gpAlgorithmConfigurationsRunStats.size() == configurationIndex){
                    gpAlgorithmConfigurationRunStats = new GPAlgorithmConfigurationRunStats(configuration.Configurations.get(configurationIndex).Name);
                    gpAlgorithmConfigurationsRunStats.add(gpAlgorithmConfigurationRunStats);
                    multiConfigurationsProgressData.addMultiConfigurationProgressData(new GPAlgorithmMultiRunProgressData());

                    runIndex = 0;
                }
                else {
                    gpAlgorithmConfigurationRunStats = gpAlgorithmConfigurationsRunStats.get(configurationIndex);
                }

                for (; runIndex < configuration.Configurations.get(configurationIndex).NumberOfReruns; ) {
                    // TODO Remove this ??? (temp solution to start from the beggining but leave the existing runs)
                    //if(multiConfigurationsProgressData.getMultiConfigurationProgressData().get(configurationIndex).getMultiRunProgressData().size() -1 < runIndex)
                    //    continue;

                    // Set initial seed
                    if(configuration.SetInitialSeedForEachConfigurationRun){
                        RNG.setSeed(configuration.InitialSeed + runIndex);
                    }

                    multiConfigurationsProgressData.getMultiConfigurationProgressData().get(configurationIndex).addMultiRunProgressData(new GPAlgorithmRunProgressData());

                    gpAlgorithm.execute(this, configuration.Configurations.get(configurationIndex), saveGPAlgorithmStateFilename, multiConfigurationsProgressData);
                    gpAlgorithmConfigurationRunStats.addGpAlgorithmRunStats(gpAlgorithm.getStats());

                    runIndex++;

                    // Save final GPAlgorithm run state to file for each configuration
                    GPAlgorithm.serializeAlgorithmState(gpAlgorithm, configurationIndex + "_" + runIndex + "_RunConfigurationGPAlgorithm.ser" );

                    // Save current GPAlgorithmExecutor state to file
                    serializeGPAlgorithmExecutorState(this, "GPAlgorithmExecutor.ser");

                }
                restartUnityInstances(false);
            }

            if(configuration.ExecuteFinalMasterTournaments){
               executeFinalTournaments();

               multiConfigurationsProgressData.saveProgressData();
               GPAlgorithmMultiConfigurationsProgressData.serializeState(multiConfigurationsProgressData);

               // Save current GPAlgorithmExecutor state to file
               serializeGPAlgorithmExecutorState(this, "GPAlgorithmExecutor.ser");
               restartUnityInstances(false);
            }
        } catch (StopCriterionException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void executeFinalTournaments() {
        // Execute final master tournaments
        System.out.println("Executing FinalMasterTournaments");

        // 1. Execute configuration master master tournament for each configuration
        // Should this be named Great Master tournament?

        // 1.1 Prepare configuration master tournament individuals
        if(configuration.FinalMasterTournamentsConfiguration == null){
            throw new IllegalArgumentException("ExecuteFinalMasterTournaments is null");
        }

        // EARS Configuration
        setEARSConfiguration(configuration.FinalMasterTournamentsConfiguration);

        // Set Unity configuration & start Unity instances
        if(configuration.FinalMasterTournamentsConfiguration.EARSConfiguration.ProblemType == GPProblemType.BEHAVIOR){
            // Save Unity configuration
            Configuration.serializeUnityConfig(configuration.FinalMasterTournamentsConfiguration.UnityConfiguration, getConfiguration().UnityConfigDestFilePath);

            // Start Unity Instances
            restartUnityInstances(true);
        }

        // 2 Execute master tournament for each configuration
        for (int i = 0; i < gpAlgorithmConfigurationsRunStats.size(); i++) {
            // 2.1 Prepare master tournament individuals
            List<ProgramSolution> masterTournamentIndividuals = new ArrayList<>();
            for (int j = 0; j < gpAlgorithmConfigurationsRunStats.get(i).getGpAlgorithmRunStats().size(); j++) {
                masterTournamentIndividuals.add(new ProgramSolution(gpAlgorithmConfigurationsRunStats.get(i).getGpAlgorithmRunStats().get(j).getBestRunSolution().right, gpAlgorithmConfigurationsRunStats.get(i).getConfigurationName()));
            }

            // Restart masterTournamentIndividuals ids
            for (int j = 0; j < masterTournamentIndividuals.size(); j++) {
                masterTournamentIndividuals.get(j).setID(j);
            }

            // 2.2 Execute master tournament
            this.gpAlgorithm.getTask().problem.bulkEvaluate(masterTournamentIndividuals);

            // 2.3 Save master master tournament results
            multiConfigurationsProgressData.getMultiConfigurationProgressData().get(i).setMasterMasterTournamentGraphData(masterTournamentIndividuals);
        }

        // Set Unity configuration & start Unity instances
        if(configuration.FinalMasterTournamentsConfiguration.EARSConfiguration.ProblemType == GPProblemType.BEHAVIOR){
            // Unity configuration stays the same so no need to save it again
            // Start Unity Instances
            restartUnityInstances(true);
        }

        // TODO: Execute finalMasterTournament for each best individual from the
        // 3. Execute final master tournament for all configurations (tournament for each best solution each X gens)
        multiConfigurationsProgressData.clearFinalMasterTournamentGraphData();

        List<ProgramSolution> finalMasterTournamentIndividuals;
        if(gpAlgorithmConfigurationsRunStats.size() > 0) {
            if(configuration.FinalMasterTournamentsConfiguration.FinalMasterTournamentsComparisonType == FinalMasterTournamentsComparisonType.Generations) {
                // TODO: Option 1 - Compare solutions based on the number of generations
                System.out.println("Executing FinalMasterTournaments based on generations");
                int executeFinalMasterTournamentEveryXGen = configuration.FinalMasterTournamentsConfiguration.ExecuteFinalMasterTournamentEveryXGen;
                for (int i = 0; i < gpAlgorithmConfigurationsRunStats.get(0).getGpAlgorithmRunStats().get(0).getBestGenSolutionSize(); i += executeFinalMasterTournamentEveryXGen) {
                    finalMasterTournamentIndividuals = new ArrayList<>();
                    // 3.1 Prepare final master tournament individuals (all best solutions from all configurations and all runs)
                    for (GPAlgorithmConfigurationRunStats gpAlgorithmConfigurationRunStats : gpAlgorithmConfigurationsRunStats) {
                        for (GPAlgorithmRunStats gpAlgorithmRunStats : gpAlgorithmConfigurationRunStats.getGpAlgorithmRunStats()) {
                            finalMasterTournamentIndividuals.add(new ProgramSolution(gpAlgorithmRunStats.getBestRunSolution(i).right, gpAlgorithmConfigurationRunStats.getConfigurationName()));
                        }
                    }

                    // Restart finalMasterTournamentIndividuals ids
                    for (int j = 0; j < finalMasterTournamentIndividuals.size(); j++) {
                        finalMasterTournamentIndividuals.get(j).setID(j);
                    }

                    // 3.2 Execute final master tournament
                    this.gpAlgorithm.getTask().problem.bulkEvaluate(finalMasterTournamentIndividuals);

                    // 3.3 Save final master tournament results
                    multiConfigurationsProgressData.addFinalMasterTournamentGraphData(finalMasterTournamentIndividuals);
                }
            }
            else {
                // TODO: Option 2 - Compare solutions based on the number of evaluations spent
                System.out.println("Executing FinalMasterTournaments based on evaluations");
                // Step 1: Find the configuration that spent the most evaluations per generation
                GPAlgorithmConfigurationRunStats gpAlgorithmConfigurationRunStatsMax = gpAlgorithmConfigurationsRunStats.get(0);
                int maxEvaluationsSpentPerGen = gpAlgorithmConfigurationRunStatsMax.getGpAlgorithmRunStats().get(0).getBestRunSolution(0).left;

                for (GPAlgorithmConfigurationRunStats gpAlgorithmConfigurationRunStats : gpAlgorithmConfigurationsRunStats) {
                    ImmutablePair<Integer, ProgramSolution> bestRunSolution = gpAlgorithmConfigurationRunStats.getGpAlgorithmRunStats().get(0).getBestRunSolution(0);
                    if(bestRunSolution.left > maxEvaluationsSpentPerGen) {
                        maxEvaluationsSpentPerGen = bestRunSolution.left;
                        gpAlgorithmConfigurationRunStatsMax = gpAlgorithmConfigurationRunStats;
                    }
                }

                // Step 1: Compare solutions based on the number of evaluations spent for each configuration and each run
                int i = 0;
                for(ImmutablePair<Integer, ProgramSolution> genSolution : gpAlgorithmConfigurationRunStatsMax.getGpAlgorithmRunStats().get(0).getBestGenSolutions()) {
                    if(i % configuration.FinalMasterTournamentsConfiguration.ExecuteFinalMasterTournamentEveryXGen == 0 || ((i == gpAlgorithmConfigurationRunStatsMax.getGpAlgorithmRunStats().get(0).getBestGenSolutions().size() - 1) && i % configuration.FinalMasterTournamentsConfiguration.ExecuteFinalMasterTournamentEveryXGen != 0)) {
                        int evals = genSolution.left;
                        System.out.println("Executing FinalMasterTournaments at evaluations: " + evals);

                        finalMasterTournamentIndividuals = new ArrayList<>();
                        // 3.1 Prepare final master tournament individuals (all best solutions from all configurations and all runs)
                        for (GPAlgorithmConfigurationRunStats gpAlgorithmConfigurationRunStats : gpAlgorithmConfigurationsRunStats) {
                            for (GPAlgorithmRunStats gpAlgorithmRunStats : gpAlgorithmConfigurationRunStats.getGpAlgorithmRunStats()) {
                                ImmutablePair<Integer, ProgramSolution> bestRunSolution = gpAlgorithmRunStats.getBestRunSolution(-1, evals);
                                bestRunSolution.right.setEvaluationCount(evals);
                                finalMasterTournamentIndividuals.add(new ProgramSolution(bestRunSolution.right, gpAlgorithmConfigurationRunStats.getConfigurationName()));
                            }
                        }

                        // Restart finalMasterTournamentIndividuals ids
                        for (int j = 0; j < finalMasterTournamentIndividuals.size(); j++) {
                            finalMasterTournamentIndividuals.get(j).setID(j);
                        }

                        // 3.2 Execute final master tournament
                        this.gpAlgorithm.getTask().problem.bulkEvaluate(finalMasterTournamentIndividuals);

                        // 3.3 Save final master tournament results
                        multiConfigurationsProgressData.addFinalMasterTournamentGraphData(finalMasterTournamentIndividuals);
                    }

                    i++;
                }
            }
        }

        System.out.println("FinalMasterTournaments finished");
    }

    /**
     * Closes all existing Unity instance and restarts them if @restartInstances is true
     * @param restartInstances
     */
    public void restartUnityInstances(boolean restartInstances){
        // 1. Close all running GeneralTrainingPlatformForMAS instances
        try {
            executeCommand("taskkill /F /IM " + configuration.UnityGameFile, "pkill -f " + configuration.UnityGameFile);
            System.out.println("Closing Unity instance: " + configuration.UnityExeLocation + configuration.UnityGameFile);
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(restartInstances) {
            // 2. Rerun GeneralTrainingPlatformForMAS (Unity)
            for (int instances = 0; instances < configuration.EvalEnvInstanceURIs.split(",").length; instances++) {
                try {
                    executeCommand("start " + configuration.UnityExeLocation + configuration.UnityGameFile + " -batchmode -nographics", configuration.UnityExeLocation + configuration.UnityGameFile + " -batchmode -nographics");
                    System.out.println("Starting Unity instance: " + configuration.UnityExeLocation + configuration.UnityGameFile + " -batchmode -nographics");
                    Thread.sleep(2000);
                } catch (Exception e) {
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

    public void executeCommand(String commandWin, String commandLinux) throws  Exception{
        String[] command;

        String os = System.getProperty("os.name").toLowerCase();
        // Example command: list directory contents
        if (os.contains("win")) {
            // Windows uses 'cmd /c'
            command = new String[]{"cmd.exe", "/c", commandWin};
        } else {
            // Unix-like systems use 'bash -c'
            command = new String[]{"bash", "-c", commandLinux};
        }

        ProcessBuilder builder = new ProcessBuilder(command);
        builder.start();
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

    public ArrayList<GPAlgorithmConfigurationRunStats> getGpAlgorithmRunStats() {
        return gpAlgorithmConfigurationsRunStats;
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

    public List<ProgramSolution> getBestConfigurationsRunSolutions() {
        List<ProgramSolution> bestRunSolutions = new ArrayList<>();
        for (GPAlgorithmConfigurationRunStats gpAlgorithmConfigurationRunStats : gpAlgorithmConfigurationsRunStats) {
            bestRunSolutions.add(gpAlgorithmConfigurationRunStats.getBestRunSolution().right);
        }

        return bestRunSolutions;
    }

    public GPAlgorithmMultiConfigurationsProgressData getMultiConfigurationsProgressData() {
        return multiConfigurationsProgressData;
    }

    public void setMultiConfigurationsProgressData(GPAlgorithmMultiConfigurationsProgressData multiConfigurationsProgressData) {
        this.multiConfigurationsProgressData = multiConfigurationsProgressData;
    }

    public void setConfigurationIndex(int configurationIndex) {
        this.configurationIndex = configurationIndex;
    }

    public int getConfigurationIndex() {
        return configurationIndex;
    }

    public void setRunIndex(int runIndex) {
        this.runIndex = runIndex;
    }

    public int getRunIndex() {
        return runIndex;
    }

}
