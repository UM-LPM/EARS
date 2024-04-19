package org.um.feri.ears.algorithms.gp;


import org.um.feri.ears.algorithms.*;
import org.um.feri.ears.operators.Selection;
import org.um.feri.ears.operators.TournamentSelection;
import org.um.feri.ears.operators.gp.*;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.gp.*;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.comparator.ProblemComparator;

import java.util.ArrayList;
import java.util.List;

public class DefaultGPAlgorithm extends GPAlgorithm {

    private static final long serialVersionUID = -1275148524824281908L;

    @AlgorithmParameter(name = "population size")
    private int popSize;

    @AlgorithmParameter(name = "crossover probability")
    private double crossoverProbability;

    @AlgorithmParameter(name = "mutation probability")
    private double mutationProbability;

    @AlgorithmParameter(name = "number of tournaments")
    private int numberOfTournaments;

    private List<ProgramSolution> population;
    private List<ProgramSolution> currentPopulation;

    private ProblemComparator<ProgramSolution> comparator;
    private Selection<ProgramSolution, ProgramProblem> selectionOperator;
    private final GPCrossover crossoverOperator;
    private final GPMutation mutationOperator;
    protected AlgorithmStepper<GPAlgorithmStep> algorithmStepper;

    private ProgramSolution best;

    private String initialAlgorithmStateFilename;

    public DefaultGPAlgorithm() {
        this(100, 0.90, 0.025, 2, null, null);
    }

    public DefaultGPAlgorithm(int popSize, double crossoverProbability, double mutationProbability, int numberOfTournaments, String initialAlgorithmStateFilename) {
        this(popSize,crossoverProbability,mutationProbability,numberOfTournaments,null, initialAlgorithmStateFilename);
    }

    public DefaultGPAlgorithm(int popSize, double crossoverProbability, double mutationProbability, int numberOfTournaments, Task<ProgramSolution, ProgramProblem> task, String initialAlgorithmStateFilename) {
        this.popSize = popSize;
        this.crossoverProbability = crossoverProbability;
        this.mutationProbability = mutationProbability;
        this.numberOfTournaments = numberOfTournaments;

        this.crossoverOperator = new SinglePointCrossover(this.crossoverProbability);
        this.mutationOperator = new SubtreeMutation(this.mutationProbability);

        au = new Author("marko", "marko.smid2@um.si");
        ai = new AlgorithmInfo("DGP", "Default GP Algorithm",
                ""
        );

        this.algorithmStepper = new AlgorithmStepper<>(GPAlgorithmStep.class, true);

        this.task = task;
        this.bestOverallFitnesses = new ArrayList<>();
        this.avgGenFitnesses = new ArrayList<>();
        this.avgGenTreeDepths = new ArrayList<>();
        this.avgGenTreeSizes = new ArrayList<>();
        this.bestGenFitnesses = new ArrayList<>();

        this.initialAlgorithmStateFilename = initialAlgorithmStateFilename;
    }

    @Override
    public ProgramSolution execute(Task<ProgramSolution, ProgramProblem> task) throws StopCriterionException {
        GPAlgorithm.addInterruptKeyListener();


        if(this.initialAlgorithmStateFilename != null) {
            initializeAlgorithmStateFromFile();
        }else{
            // Algorithm initialization
            algorithmInitialization(task, new ProblemComparator<>(task.problem), null); // TODO add support for selection operator

            // Population initialization and evaluation
            populationInitialization();
        }

        while (!task.isStopCriterion()) {
            // Check if application can still run
            if(!DefaultGPAlgorithm.CAN_RUN){
                // Serialize current state
                GPAlgorithm.serializeAlgorithmState(this, "gpAlgorithmState.ser");
                break;
            }
            // Selection and Crossover
            performSelectionAndCrossover();

            // Mutation
            performMutation();

            // Evaluate
            performEvaluation();

            // Bloat control - Remove all redundant nodes (needs to be evaluated again after methods are executed)
            // TODO

            // Check stop criterion and increment number of iterations
            if (this.task.isStopCriterion())
                break;

            this.task.incrementNumberOfIterations();
        }
        //this.population.sort(this.comparator);
        //System.out.println("Best solution (Pop): " + this.population.get(0).getEval());
        //System.out.println("Best solution (Task): " + this.task.bestSolution.getEval());
        //return this.population.get(0);
        return this.best;
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
        population = this.task.getRandomEvaluatedSolution(this.popSize);

        for(ProgramSolution sol : population){
            if (task.problem.isFirstBetter(sol, best))
                best = new ProgramSolution(sol);
        }
    }

    /**
     * Load current state of population and task from file
     */
    private void initializeAlgorithmStateFromFile() {
        GPAlgorithm alg = GPAlgorithm.deserializeAlgorithmState(this.initialAlgorithmStateFilename);
        this.population = alg.getPopulation();
        this.task = alg.getTask();

        for(ProgramSolution sol : population){
            if (task.problem.isFirstBetter(sol, best))
                best = new ProgramSolution(sol);
        }

        algorithmInitialization(task, new ProblemComparator<>(task.problem), null); // TODO add support for selection operator
    }

    public void performSelectionAndCrossover() throws StopCriterionException {
        ProgramSolution[] parents = new ProgramSolution[2];
        this.currentPopulation = new ArrayList<>(population.size());
        // Selection and Crossover
        for (int i = 0; i < this.popSize / 2; i++) {
            parents[0] = this.selectionOperator.execute(population, task.problem);
            parents[1] = this.selectionOperator.execute(population, task.problem);

            //selectedIndividuals.add(parents[0]);
            //selectedIndividuals.add(parents[1]);
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
        for (int i = 0; i < currentPopulation.size(); i++) {
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

        this.task.bulkEval(this.population);

        currentGenBest = new ProgramSolution(this.population.get(0));
        for(ProgramSolution sol : population){
            if (task.problem.isFirstBetter(sol, best))
                best = new ProgramSolution(sol);

            if (task.problem.isFirstBetter(sol, currentGenBest))
                currentGenBest = new ProgramSolution(sol);
        }

        this.population = this.currentPopulation;

        return currentGenBest;
    }

    @Override
    public ProgramSolution getBest() {
        return best;
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
            case SELECTION_AND_CROSSOVER:
                performSelectionAndCrossover();
                break;
            case MUTATION:
                performMutation();
                break;
            case EVALUATION:
                ProgramSolution currentGenBest = performEvaluation();
                this.bestGenFitnesses.add(currentGenBest.getEval());
                if(this.isDebug()){
                    this.bestOverallFitnesses.add(this.best.getEval());
                    double sum = 0;
                    for (ProgramSolution sol: this.population) {
                        sum += sol.getEval();
                    }
                    this.avgGenFitnesses.add(sum / this.population.size());

                    // add current avg tree depth to list
                    double avgDepth = 0;
                    for (ProgramSolution sol: this.population) {
                        avgDepth += sol.getTree().treeDepth();
                    }
                    this.avgGenTreeDepths.add(avgDepth / this.population.size());

                    // add current avg tree size to list
                    double avgSize = 0;
                    for (ProgramSolution sol: this.population) {
                        avgSize += sol.getTree().treeSize();
                    }
                    this.avgGenTreeSizes.add(avgSize / this.population.size());

                }
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
    }

    @Override
    public int getPopSize() {
        return popSize;
    }

    @Override
    public void setCrossoverProbability(double crossoverProbability) {
        this.crossoverProbability = crossoverProbability;
    }

    @Override
    public double getCrossoverProbability() {
        return crossoverProbability;
    }

    @Override
    public void setMutationProbability(double mutationProbability) {
        this.mutationProbability = mutationProbability;
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
}