package org.um.feri.ears.algorithms.gp;

import com.google.gson.Gson;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.AlgorithmStepper;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.GPAlgorithm;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.Encapsulator;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.EncapsulatedNodeDefinition;
import org.um.feri.ears.operators.Selection;
import org.um.feri.ears.operators.TournamentSelection;
import org.um.feri.ears.operators.gp.*;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.gp.ProgramProblem;
import org.um.feri.ears.problems.gp.ProgramSolution;
import org.um.feri.ears.util.*;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.comparator.ProblemComparator;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;
import java.util.List;

public class PredefinedEncapsNodesGPAlgorithm extends GPAlgorithm {
    public static final long serialVersionUID = 5702986569144275111L;

    @AlgorithmParameter(name = "population size")
    private int popSize;

    @AlgorithmParameter(name = "crossover probability")
    private double crossoverProbability;

    @AlgorithmParameter(name = "mutation probability")
    private double mutationProbability;

    @AlgorithmParameter(name = "elitism percent")
    private double elitismProbability; // How many best individuals are copied to the next generation

    @AlgorithmParameter(name = "number of tournaments")
    private int numberOfTournaments;

    private List<ProgramSolution> population;
    private List<ProgramSolution> currentPopulation;
    private List<ProgramSolution> parentPopulation;

    private ProblemComparator<ProgramSolution> comparator;
    private Selection<ProgramSolution, ProgramProblem> selectionOperator;
    private final GPCrossover crossoverOperator;
    private final GPMutation mutationOperator;
    protected AlgorithmStepper<GPAlgorithmStep> algorithmStepper;

    private ProgramSolution best;

    private int eliteCount;
    private int offspringCount;

    List<EncapsulatedNodeDefinition> encapsulatedNodeDefinitions;

    /**
     * Pruning operators used after the evolution of encapsulated node
     */
    private GPOperator[] pruningOperators;

    public PredefinedEncapsNodesGPAlgorithm() {
        this(100, 0.90, 0.05, 0.025, 2, null);
    }

    public PredefinedEncapsNodesGPAlgorithm(int popSize, double crossoverProbability, double elitismProbability, double mutationProbability, int numberOfTournaments) {
        this(popSize,crossoverProbability, elitismProbability,mutationProbability,numberOfTournaments, null);
    }

    public PredefinedEncapsNodesGPAlgorithm(int popSize, double crossoverProbability, double elitismProbability, double mutationProbability, int numberOfTournaments, Task<ProgramSolution,  ProgramProblem> task) {
        this.popSize = popSize;
        this.crossoverProbability = crossoverProbability;
        this.elitismProbability = elitismProbability;
        this.mutationProbability = mutationProbability;
        this.numberOfTournaments = numberOfTournaments;

        // Calculate number of elite individuals and offspring
        setElitismParams();

        this.crossoverOperator = new GPSinglePointCrossover(this.crossoverProbability);
        this.mutationOperator = new GPSubtreeMutation(this.mutationProbability);

        au = new Author("marko", "marko.smid2@um.si");
        ai = new AlgorithmInfo("PENA", "Predefined Encapsulated Nodes Algorithm",
                ""
        );

        this.algorithmStepper = new AlgorithmStepper<>(GPAlgorithmStep.class, true);

        this.task = task;
        this.bestOverallFitnesses = new ArrayList<>();
        this.avgGenFitnesses = new ArrayList<>();
        this.avgGenTreeDepths = new ArrayList<>();
        this.avgGenTreeSizes = new ArrayList<>();
        this.bestGenFitnesses = new ArrayList<>();

        this.encapsulatedNodeDefinitions = new ArrayList<>();
    }

    @Override
    public ProgramSolution execute(Task<ProgramSolution, ProgramProblem> task) throws StopCriterionException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ProgramSolution execute(GPAlgorithmExecutor gpAlgorithmExecutor, RunConfiguration runConfiguration,  String saveGPAlgorithmStateFilename) throws StopCriterionException {
        // Clear encapsulated node definitions & reset to defaults
        encapsulatedNodeDefinitions.clear();
        resetToDefaultsBeforeNewRun();

        // 1. Run evolution for predefined encapsulated node definitions to find corresponding behavior trees
        if(runConfiguration.EncapsulatedNodeDefinitions.size() > 0) {
            for (int i = 0; i < runConfiguration.EncapsulatedNodeDefinitions.size(); i++) {
                EncapsulatedNodeConfigDefinition encapsNodeConfigDef = runConfiguration.EncapsulatedNodeDefinitions.get(i);

                RunConfiguration encapsNodeRunConf = encapsNodeConfigDef.RunConfiguration;
                System.out.println("Run configuration (Encapsulated node): " + encapsNodeRunConf.Name);

                // Set EARS configuration
                int generations = gpAlgorithmExecutor.setEARSConfiguration(encapsNodeRunConf);

                // Set Pruning operators for encapsulated node
                setPruningOperatorsFromStringArray(encapsNodeConfigDef.PruningOperators);

                // Add encapsulated nodes to terminal set immediately
                if(encapsulatedNodeDefinitions.size() > 0 && runConfiguration.EncapsulatedNodeDefinitions.get(i - 1).AddToTerminalSetImmediately) {
                    // 2. Extend terminal set with encapsulated node
                    task.problem.getBaseTerminalNodeTypes().add(Encapsulator.class);
                    task.problem.getProgramSolutionGenerator().addEncapsulatedNodeDefinition(encapsulatedNodeDefinitions);
                }

                if(runConfiguration.EARSConfiguration.ProblemType == GPProblemType.BEHAVIOR) {
                    // Save Unity configuration
                    Configuration.serializeUnityConfig(encapsNodeRunConf, gpAlgorithmExecutor.getConfiguration().UnityConfigDestFilePath);

                    // Start Unity Instances
                    gpAlgorithmExecutor.restartUnityInstances(true);
                }

                // Run algorithm for X generations
                execute(generations, null);

                // Create encapsulated node and define its behavior with gpAlgorithm runs best solution
                createEncapsulatedNode(encapsNodeConfigDef);

                System.out.println("Run configuration (Encapsulated node): " + encapsNodeRunConf.Name + " done");

                resetToDefaultsBeforeNewRun();
            }
        }

        // 3. Run GP algorithm with extended terminal set
        System.out.println("Run configuration: (" + runConfiguration.Name + ")");

        // Set EARS configuration
        int generations = gpAlgorithmExecutor.setEARSConfiguration(runConfiguration);

        // Add encapsulated nodes to terminal set
        if(encapsulatedNodeDefinitions.size() > 0) {
            // 2. Extend terminal set with encapsulated node
            task.problem.getBaseTerminalNodeTypes().add(Encapsulator.class);
            task.problem.getProgramSolutionGenerator().addEncapsulatedNodeDefinition(encapsulatedNodeDefinitions);
        }

        if(runConfiguration.EARSConfiguration.ProblemType == GPProblemType.BEHAVIOR){
            // Save Unity configuration
            Configuration.serializeUnityConfig(runConfiguration, gpAlgorithmExecutor.getConfiguration().UnityConfigDestFilePath);

            // Start Unity Instances
            gpAlgorithmExecutor.restartUnityInstances(true);
        }

        // Run algorithm for X generations
        execute(generations, null);

        System.out.println("Run configuration: (" + runConfiguration.Name + ") done");

        // 3. Return best solution
        return this.best;
    }


    @Override
    public ProgramSolution executeStep() throws StopCriterionException {
        if (this.task.isStopCriterion()){
            return this.best;
        }

        switch (this.algorithmStepper.getCurrentValue()){
            case INITIALIZATION:
                if(this.task == null)
                    throw new StopCriterionException("Algorithm task not set");;
                algorithmInitialization(this.task, new ProblemComparator<>(this.task.problem), null);
                populationInitialization();

                break;
            case ELITISM:
                // Initialize the population of current generation
                this.currentPopulation = new ArrayList<>(this.population.size());

                // Elitism phase
                // Sort population by fitness
                this.population.sort(this.comparator);

                // Copy eliteCount best individuals to the next generation
                this.parentPopulation = new ArrayList<>();

                int eliteCountMod = this.eliteCount;

                if(this.best.getObjective(0) <= this.population.get(0).getObjective(0)){
                    this.currentPopulation.add(new ProgramSolution(this.best));
                    eliteCountMod--;
                }

                for (int i = 0; i < eliteCountMod; i++) {
                    this.currentPopulation.add(new ProgramSolution(population.get(i)));
                }
                break;
            case SELECTION_AND_CROSSOVER:
                // Selection phase
                this.parentPopulation.addAll(performselection(this.offspringCount));

                // Crossover phase
                performCrossover();

                break;
            case MUTATION:
                performMutation();

                break;
            case EVALUATION:
                ProgramSolution currentGenBest = performEvaluation();

                updateStatistics();

                // Bloat control - Remove all redundant nodes (needs to be evaluated again after methods are executed)
                if (this.task.problem.getBloatControlOperators().length > 0) {
                    for (ProgramSolution solution : this.population) {
                        this.task.problem.executeBloatedControlOperators(solution);
                    }

                    if (this.task.isStopCriterion()) {
                        return this.best;
                    }

                    // Reevaluate population
                    currentGenBest = performEvaluation();
                }

                if (this.isDebug())
                    this.bestGenFitnesses.add(currentGenBest.getEval());

                break;
            default:
                System.out.println("Unknown algorithm step, skipping...");
        }
        if(this.algorithmStepper.isLastStep()){
            this.task.incrementNumberOfIterations();
        }
        this.algorithmStepper.stepForward();
        return null;
    }

    @Override
    public ProgramSolution executeGeneration() throws StopCriterionException {
        int currentGeneration = this.task.getNumberOfIterations();
        while(!this.task.isStopCriterion() && this.task.getNumberOfIterations() == currentGeneration){
            executeStep();
        }
        if(this.task.isStopCriterion()){
            return this.best;
        }
        return null;
    }

    @Override
    public ProgramSolution getBest() {
        return best;
    }

    @Override
    public ProblemComparator<ProgramSolution> getComparator() {
        return comparator;
    }

    @Override
    public List<ProgramSolution> getPopulation() {
        return population;
    }

    @Override
    public void setPopulation(List<ProgramSolution> population) {
        this.population = population;
    }

    @Override
    public void setPopSize(int popSize) {
        this.popSize = popSize;
        setElitismParams();
    }

    @Override
    public int getPopSize() {
        return popSize;
    }

    @Override
    public void setCrossoverProbability(double crossoverProbability) {
        this.crossoverProbability = Util.roundDouble(crossoverProbability,3);
    }

    @Override
    public void setElitismProbability(double elitismProbability) {
        this.elitismProbability = Util.roundDouble(elitismProbability,3);
        setElitismParams();
    }

    @Override
    public double getCrossoverProbability() {
        return crossoverProbability;
    }

    @Override
    public void setMutationProbability(double mutationProbability) {
        this.mutationProbability = Util.roundDouble(mutationProbability,3);
    }

    @Override
    public double getMutationProbability() {
        return mutationProbability;
    }

    @Override
    public void setNumberOfTournaments(int numberOfTournaments) {
        this.numberOfTournaments = numberOfTournaments;
    }

    @Override
    public int getNumberOfTournaments() {
        return numberOfTournaments;
    }

    @Override
    public double getElitismProbability() {
        return elitismProbability;
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
        this.algorithmStepper.reset();
        if(this.task != null){
            this.task.resetCounter();
        }

        this.best = null;
        this.population = new ArrayList<>();
        this.bestOverallFitnesses = new ArrayList<>();
        this.avgGenFitnesses = new ArrayList<>();
        this.avgGenTreeDepths = new ArrayList<>();
        this.avgGenTreeSizes = new ArrayList<>();
        this.bestGenFitnesses = new ArrayList<>();
    }

    public void algorithmInitialization(Task<ProgramSolution, ProgramProblem> task, ProblemComparator<ProgramSolution> comparator, Selection<ProgramSolution, ProgramProblem> selectionOperator) {
        this.task = task;
        this.comparator = comparator;
        this.selectionOperator = new TournamentSelection<>(this.numberOfTournaments, comparator);
    }

    /**
     * Initialize @popSize individuals and evaluate them. Best random generated solution is saved to @best
     */
    private void populationInitialization() throws StopCriterionException {
        population = this.task.generateRandomEvaluatedSolution(this.popSize);

        for(ProgramSolution sol : population){
            if (task.problem.isFirstBetter(sol, best))
                best = new ProgramSolution(sol);
        }
    }

    public List<ProgramSolution> performselection(int parentCount){
        List<ProgramSolution> parentSolutions = new ArrayList<>();
        // Execute selection operator to get all parents
        for (int i = 0; i < parentCount; i++) {
            parentSolutions.add(new ProgramSolution(this.selectionOperator.execute(population, task.problem)));
        }

        return parentSolutions;
    }

    public void performCrossover() throws StopCriterionException {
        // Shuffle parentSolutions
        RNG.shuffle(this.parentPopulation);

        ProgramSolution[] parents = new ProgramSolution[2];
        // Selection and Crossover
        for (int i = 0; i < this.parentPopulation.size(); i += 2) {
            parents[0] = this.parentPopulation.get(i);
            parents[1] = this.parentPopulation.get(i + 1);
            try {
                ProgramSolution[] newSolution = this.crossoverOperator.execute(parents, task.problem);
                currentPopulation.add(newSolution[0]);
                currentPopulation.add(newSolution[1]);

            } catch (Exception ex) {
                throw new StopCriterionException(ex.toString());
            }
        }
    }

    public void performMutation()  throws StopCriterionException {
        for (int i = this.eliteCount; i < currentPopulation.size(); i++) {
            try {
                currentPopulation.set(i, this.mutationOperator.execute(currentPopulation.get(i), task.problem));
            } catch (Exception ex) {
                throw new StopCriterionException("Mutation error");
            }
        }
    }

    public ProgramSolution performEvaluation()  throws StopCriterionException {
        ProgramSolution currentGenBest = null;
        population = new ArrayList<>(this.currentPopulation);

        // If the number of evaluations is greater than the maximum number of evaluations, we need to remove the last individuals
        if(this.task.getNumberOfEvaluations() + this.population.size() >= this.task.getMaxEvaluations()){
            int evals = this.task.getMaxEvaluations() - this.task.getNumberOfEvaluations();
            population = new ArrayList<>(this.population.subList(0, evals));
        }

        this.task.bulkEval(this.population);

        currentGenBest = new ProgramSolution(this.population.get(0));
        for(ProgramSolution sol : population){
            if (task.problem.isFirstBetter(sol, best))
                best = new ProgramSolution(sol);

            if (task.problem.isFirstBetter(sol, currentGenBest))
                currentGenBest = new ProgramSolution(sol);
        }

        return currentGenBest;
    }

    public void setElitismParams(){
        this.eliteCount = (int) Math.round(this.elitismProbability * this.popSize);
        if((this.eliteCount % 2) != 0)
            this.eliteCount++; // Because of crossover operator we need to have even number of elite individuals
        this.offspringCount = this.popSize - this.eliteCount;
    }

    /**
     * Creates encapsulated node definitions based on the best individuals from the population. The number of encapsulated nodes is defined in the configuration.
     * Before creating encapsulated node, the best individuals are pruned with pruning operators.
     * @param encapsNodeConfigDef Encapsulated node configuration definition
     */
    private void createEncapsulatedNode(EncapsulatedNodeConfigDefinition encapsNodeConfigDef) {
        int encapsulatedNodeFrequency = encapsNodeConfigDef.EncapsulatedNodeFrequency;

        // Sort population by fitness
        this.population.sort(this.comparator);

        // Create encapsulated node definitions
        for (int i = 0; i < encapsulatedNodeFrequency; i++){
            ProgramSolution solution = new ProgramSolution(this.population.get(i));
            // Prune tree with pruning operators
            for (int j = 0; j < pruningOperators.length; j++) {
                pruningOperators[j].execute(solution, this.task.problem);
            }
            encapsulatedNodeDefinitions.add(new EncapsulatedNodeDefinition(encapsNodeConfigDef.EncapsulatedNodeName, solution.getTree().getRootNode().clone()));
        }
    }

    /**
     * Sets pruning operators from string array
     * @param pruningOperatorsString Array of pruning operators in json format
     */
    public void setPruningOperatorsFromStringArray(String[] pruningOperatorsString){
        this.pruningOperators = new GPOperator[pruningOperatorsString.length];
        String packagePrefix = "org.um.feri.ears.operators.gp.";
        Gson gson = new Gson();

        for (int i = 0; i < pruningOperatorsString.length; i++) {
            try {
                String[] operatorParts = pruningOperatorsString[i].split("-");
                this.pruningOperators[i] = (GPOperator) gson.fromJson(operatorParts[1], Class.forName(packagePrefix + operatorParts[0].trim()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateStatistics(){
        if (this.isDebug()) {
            this.bestOverallFitnesses.add(this.best.getEval());
            double sum = 0;
            for (ProgramSolution sol : this.population) {
                sum += sol.getEval();
            }
            this.avgGenFitnesses.add(sum / this.population.size());

            // add current avg tree depth to list
            double avgDepth = 0;
            for (ProgramSolution sol : this.population) {
                avgDepth += sol.getTree().treeMaxDepth();
            }
            this.avgGenTreeDepths.add(avgDepth / this.population.size());

            // add current avg tree size to list
            double avgSize = 0;
            for (ProgramSolution sol : this.population) {
                avgSize += sol.getTree().treeSize();
            }
            this.avgGenTreeSizes.add(avgSize / this.population.size());

        }
    }
}
