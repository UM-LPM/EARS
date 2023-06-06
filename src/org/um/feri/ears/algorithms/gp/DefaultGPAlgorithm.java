package org.um.feri.ears.algorithms.gp;


import org.eclipse.swt.program.Program;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.AlgorithmStepper;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.GPAlgorithm;
import org.um.feri.ears.algorithms.so.abc.ABCSolution;
import org.um.feri.ears.operators.Selection;
import org.um.feri.ears.operators.gp.GPCrossover;
import org.um.feri.ears.operators.gp.GPMutation;
import org.um.feri.ears.operators.gp.SinglePointCrossover;
import org.um.feri.ears.operators.gp.SubtreeMutation;
import org.um.feri.ears.operators.TournamentSelection;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.gp.ProgramProblem;
import org.um.feri.ears.problems.gp.ProgramSolution;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.comparator.ProblemComparator;

import java.util.ArrayList;
import java.util.List;

import static org.um.feri.ears.algorithms.gp.GPAlgorithmStep.INITIALIZATION;

public class DefaultGPAlgorithm extends GPAlgorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;

    @AlgorithmParameter(name = "crossover probability")
    private double crossoverProbability;

    @AlgorithmParameter(name = "mutation probability")
    private double mutationProbability;

    @AlgorithmParameter(name = "number of tournaments")
    private int numberOfTournaments;

    private List<ProgramSolution<Double>> population;
    private List<ProgramSolution<Double>> currentPopulation;

    private ProblemComparator<ProgramSolution<Double>> comparator;
    private Selection<ProgramSolution<Double>, ProgramProblem<Double>> selectionOperator;
    private final GPCrossover<Double> crossoverOperator;
    private final GPMutation<Double> mutationOperator;
    protected AlgorithmStepper<GPAlgorithmStep> algorithmStepper;

    private ProgramSolution<Double> best;

    public DefaultGPAlgorithm() {
        this(100, 0.90, 0.025, 2, null);
    }

    public DefaultGPAlgorithm(int popSize, double crossoverProbability, double mutationProbability, int numberOfTournaments) {
        this(popSize,crossoverProbability,mutationProbability,numberOfTournaments,null);
    }

    public DefaultGPAlgorithm(int popSize, double crossoverProbability, double mutationProbability, int numberOfTournaments, Task<ProgramSolution<Double>, ProgramProblem<Double>> task) {
        this.popSize = popSize;
        this.crossoverProbability = crossoverProbability;
        this.mutationProbability = mutationProbability;
        this.numberOfTournaments = numberOfTournaments;

        this.crossoverOperator = new SinglePointCrossover<>(this.crossoverProbability);
        this.mutationOperator = new SubtreeMutation<>(this.mutationProbability);

        au = new Author("marko", "marko.smid2@um.si");
        ai = new AlgorithmInfo("DGP", "Default GP Algorithm",
                ""
        );

        this.algorithmStepper = new AlgorithmStepper<>(GPAlgorithmStep.class, true);

        this.task = task;
        this.bestGenFitness = new ArrayList<>();
        this.avgGenFitness = new ArrayList<>();
    }

    @Override
    public ProgramSolution<Double> execute(Task<ProgramSolution<Double>, ProgramProblem<Double>> task) throws StopCriterionException {
        // Algorithm initialization
        algorithmInitialization(task, new ProblemComparator<>(task.problem), null); // TODO add support for selection operator

        // Population initialization and evaluation
        populationInitialization();

        while (!task.isStopCriterion()) {
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
    }

    public void algorithmInitialization(Task<ProgramSolution<Double>, ProgramProblem<Double>> task, ProblemComparator<ProgramSolution<Double>> comparator, Selection<ProgramSolution<Double>, ProgramProblem<Double>> selectionOperator) {
        this.task = task;
        this.comparator = comparator;
        this.selectionOperator = new TournamentSelection<>(this.numberOfTournaments, comparator);
    }

    /**
     * Initialize @popSize individuals and evaluate them. Best random generated solution is saved to @best
     */
    private void populationInitialization() throws StopCriterionException {
        population = new ArrayList<>();
        ProgramSolution<Double> sol = new ProgramSolution<Double>(task.getRandomEvaluatedSolution());
        population.add(sol);
        best = sol;
        for (int i = 0; i < popSize; i++) {
            ProgramSolution<Double> newSolution = task.getRandomEvaluatedSolution();
            population.add(newSolution);

            if (task.problem.isFirstBetter(newSolution, best))
                best = new ProgramSolution<Double>(newSolution);
            if (task.isStopCriterion())
                break;
        }
    }

    public void performSelectionAndCrossover() throws StopCriterionException {
        ProgramSolution<Double>[] parents = new ProgramSolution[2];
        this.currentPopulation = new ArrayList<>(population.size());
        // Selection and Crossover
        for (int i = 0; i < this.popSize / 2; i++) {
            parents[0] = this.selectionOperator.execute(population, task.problem);
            parents[1] = this.selectionOperator.execute(population, task.problem);

            //selectedIndividuals.add(parents[0]);
            //selectedIndividuals.add(parents[1]);
            try {
                ProgramSolution<Double>[] newSolution = this.crossoverOperator.execute(parents, task.problem);
                currentPopulation.add(newSolution[0]);
                currentPopulation.add(newSolution[1]);
            } catch (Exception ex) {
                throw new StopCriterionException("Crossover error");
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
        for (int i = 0; i < this.population.size(); i++) {
            this.task.eval(this.population.get(i));

            if (task.problem.isFirstBetter(this.population.get(i), best))
                best = new ProgramSolution<Double>(this.population.get(i));
            if (this.task.isStopCriterion())
                break;
        }

        this.population = this.currentPopulation;
    }

    @Override
    public ProgramSolution<Double> getBest() {
        return best;
    }

    @Override
    public ProgramSolution<Double> executeStep() throws StopCriterionException {
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
                this.bestGenFitness.add(this.best.getEval());
                double sum = 0;
                for (ProgramSolution<Double> sol: this.population) {
                    sum += sol.getEval();
                }
                this.avgGenFitness.add(sum / this.population.size());
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
    public ProgramSolution<Double> executeGeneration() throws StopCriterionException {
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
    public List<ProgramSolution<Double>> getPopulation() {
        return population;
    }
}