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

public class DefaultGPAlgorithm2 extends GPAlgorithm2 {

    @AlgorithmParameter(name = "population size")
    private int popSize;

    @AlgorithmParameter(name = "crossover probability")
    private double crossoverProbability;

    @AlgorithmParameter(name = "mutation probability")
    private double mutationProbability;

    @AlgorithmParameter(name = "number of tournaments")
    private int numberOfTournaments;

    private List<ProgramSolution2> population;
    private List<ProgramSolution2> currentPopulation;

    private ProblemComparator<ProgramSolution2> comparator;
    private Selection<ProgramSolution2, ProgramProblem2> selectionOperator;
    private final GPCrossover2 crossoverOperator;
    private final GPMutation2 mutationOperator;
    protected AlgorithmStepper<GPAlgorithmStep> algorithmStepper;

    private ProgramSolution2 best;

    private String initialAlgorithmStateFilename;

    public DefaultGPAlgorithm2() {
        this(100, 0.90, 0.025, 2, null, null);
    }

    public DefaultGPAlgorithm2(int popSize, double crossoverProbability, double mutationProbability, int numberOfTournaments, String initialAlgorithmStateFilename) {
        this(popSize,crossoverProbability,mutationProbability,numberOfTournaments,null, initialAlgorithmStateFilename);
    }

    public DefaultGPAlgorithm2(int popSize, double crossoverProbability, double mutationProbability, int numberOfTournaments, Task<ProgramSolution2, ProgramProblem2> task, String initialAlgorithmStateFilename) {
        this.popSize = popSize;
        this.crossoverProbability = crossoverProbability;
        this.mutationProbability = mutationProbability;
        this.numberOfTournaments = numberOfTournaments;

        this.crossoverOperator = new SinglePointCrossover2(this.crossoverProbability);
        this.mutationOperator = new SubtreeMutation2(this.mutationProbability);

        au = new Author("marko", "marko.smid2@um.si");
        ai = new AlgorithmInfo("DGP", "Default GP Algorithm",
                ""
        );

        this.algorithmStepper = new AlgorithmStepper<>(GPAlgorithmStep.class, true);

        this.task = task;
        this.bestGenFitness = new ArrayList<>();
        this.avgGenFitness = new ArrayList<>();
        this.avgGenTreeHeight = new ArrayList<>();
        this.avgGenTreeSize = new ArrayList<>();

        this.initialAlgorithmStateFilename = initialAlgorithmStateFilename;
    }

    @Override
    public ProgramSolution2 execute(Task<ProgramSolution2, ProgramProblem2> task) throws StopCriterionException {
        GPAlgorithm2.addInterruptKeyListener();


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
            if(!DefaultGPAlgorithm2.CAN_RUN){
                // Serialize current state
                GPAlgorithm2.serializeAlgorithmState(this, "gpAlgorithmState.ser");
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
        this.bestGenFitness = new ArrayList<>();
        this.avgGenFitness = new ArrayList<>();
        this.avgGenTreeHeight = new ArrayList<>();
        this.avgGenTreeSize = new ArrayList<>();
    }

    public void algorithmInitialization(Task<ProgramSolution2, ProgramProblem2> task, ProblemComparator<ProgramSolution2> comparator, Selection<ProgramSolution2, ProgramProblem2> selectionOperator) {
        this.task = task;
        this.comparator = comparator;
        this.selectionOperator = new TournamentSelection<>(this.numberOfTournaments, comparator);
    }

    /**
     * Initialize @popSize individuals and evaluate them. Best random generated solution is saved to @best
     */
    private void populationInitialization() throws StopCriterionException {
        population = this.task.getRandomEvaluatedSolution(this.popSize);

        for(ProgramSolution2 sol : population){
            if (task.problem.isFirstBetter(sol, best))
                best = new ProgramSolution2(sol);
        }
    }

    /**
     * Load current state of population and task from file
     */
    private void initializeAlgorithmStateFromFile() {
        GPAlgorithm2 alg = GPAlgorithm2.deserializeAlgorithmState(this.initialAlgorithmStateFilename);
        this.population = alg.getPopulation();
        this.task = alg.getTask();

        for(ProgramSolution2 sol : population){
            if (task.problem.isFirstBetter(sol, best))
                best = new ProgramSolution2(sol);
        }

        algorithmInitialization(task, new ProblemComparator<>(task.problem), null); // TODO add support for selection operator
    }

    public void performSelectionAndCrossover() throws StopCriterionException {
        ProgramSolution2[] parents = new ProgramSolution2[2];
        this.currentPopulation = new ArrayList<>(population.size());
        // Selection and Crossover
        for (int i = 0; i < this.popSize / 2; i++) {
            parents[0] = this.selectionOperator.execute(population, task.problem);
            parents[1] = this.selectionOperator.execute(population, task.problem);

            //selectedIndividuals.add(parents[0]);
            //selectedIndividuals.add(parents[1]);
            try {
                ProgramSolution2[] newSolution = this.crossoverOperator.execute(parents, task.problem);
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

    public void performEvaluation()  throws StopCriterionException {
        population = new ArrayList<>(this.currentPopulation);

        this.task.bulkEval(this.population);
        for(ProgramSolution2 sol : population){
            if (task.problem.isFirstBetter(sol, best))
                best = new ProgramSolution2(sol);
        }

        this.population = this.currentPopulation;
    }

    @Override
    public ProgramSolution2 getBest() {
        return best;
    }

    @Override
    public ProgramSolution2 executeStep() throws StopCriterionException {
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
                performEvaluation();
                if(this.isDebug()){
                    this.bestGenFitness.add(this.best.getEval());
                    double sum = 0;
                    for (ProgramSolution2 sol: this.population) {
                        sum += sol.getEval();
                    }
                    this.avgGenFitness.add(sum / this.population.size());

                    // add current avg tree height to list
                    double avgHeight = 0;
                    for (ProgramSolution2 sol: this.population) {
                        avgHeight += sol.getTree().treeHeight();
                    }
                    this.avgGenTreeHeight.add(avgHeight / this.population.size());

                    // add current avg tree height to list
                    double avgSize = 0;
                    for (ProgramSolution2 sol: this.population) {
                        avgSize += sol.getTree().treeSize();
                    }
                    this.avgGenTreeSize.add(avgSize / this.population.size());
                }
                break;
            default:
                throw new StopCriterionException("Unknown algorithm step");
        }
        if(this.algorithmStepper.isLastStep()){
            this.task.incrementNumberOfIterations();
        }
        this.algorithmStepper.stepForward();
        return null;
    }

    @Override
    public ProgramSolution2 executeGeneration() throws StopCriterionException {
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
    public List<ProgramSolution2> getPopulation() {
        return population;
    }

    @Override
    public void setPopulation(List<ProgramSolution2> population) {
        this.population = population;
    }
}