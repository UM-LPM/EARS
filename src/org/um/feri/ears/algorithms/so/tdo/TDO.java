package org.um.feri.ears.algorithms.so.tdo;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;

public class TDO extends NumberAlgorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;

    private ArrayList<NumberSolution<Double>> population;
    private NumberSolution<Double> best;

    public TDO() {
        this(30); // default population size (from paper / common choice)
    }

    public TDO(int popSize) {
        super();
        this.popSize = popSize;

        au = new Author("Vid Beranič", "vid.beranic@um.si");
        ai = new AlgorithmInfo(
                "TDO",
                "Tasmanian Devil Optimization",
                "@article{dehghani2022tdo, title={Tasmanian Devil Optimization}, year={2022}}"
        );
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task)
            throws StopCriterionException {

        this.task = task;
        initPopulation();
        updateBest();

        int maxIt = 10000;
        if (task.getStopCriterion() == StopCriterion.ITERATIONS) {
            maxIt = task.getMaxIterations();
        }

        if (task.getStopCriterion() == StopCriterion.EVALUATIONS) {
            maxIt = (task.getMaxEvaluations() - popSize) / popSize;
        }

        while (!task.isStopCriterion()) {
            updateBest();

            // PHASE 1: Hunting Feeding
            for (int i = 0; i < popSize; i++) {
                if (task.isStopCriterion())
                    break;

                double pr = RNG.nextDouble();

                if (pr > 0.5) {
                    // STRATEGY 1: FEEDING BY EATING CARRION (EXPLORATION PHASE)
                    calculate(i);
                } else {
                    // STRATEGY 2: FEEDING BY EATING PREY (EXPLOITATION PHASE)
                    applyPreyStrategy(i, maxIt);
                }
            }
            task.incrementNumberOfIterations();
        }
        return best;
    }

    private void initPopulation() throws StopCriterionException {
        population = new ArrayList<>();
        for (int i = 0; i < popSize; i++) {
            if (task.isStopCriterion()) break;
            population.add(task.generateRandomEvaluatedSolution());
        }
    }

    private void updateBest() {
        best = population.get(0);
        for (int i = 1; i < population.size(); i++) {
            if (task.problem.isFirstBetter(population.get(i), best)) {
                best = population.get(i);
            }
        }
    }

    private void applyPreyStrategy(int i, int maxIt) throws StopCriterionException {
        // Stage 1: Prey selection and attack
        calculate(i);

        // Stage 2: Prey chasing
        // Calculate neighborhood radius R = 0.01 * (1 - t / Max_iterations)
        double R = 0.01 * (1.0 - (double) task.getNumberOfIterations() / maxIt);
        if (R < 0) R = 0; // Ensure R is non-negative

        // Get current solution (may have been updated in stage 1)
        NumberSolution<Double> currentSolution = population.get(i);
        double[] newPosition2 = new double[task.problem.getNumberOfDimensions()];

        for (int d = 0; d < task.problem.getNumberOfDimensions(); d++) {
            double currentVal = currentSolution.getValue(d);

            double rand = RNG.nextDouble();

            newPosition2[d] = currentVal + (-R + 2 * R * rand) * currentVal;
        }

        task.problem.makeFeasible(newPosition2);

        if (task.isStopCriterion())
            return;

        NumberSolution<Double> newSolution2 = new NumberSolution<>(Util.toDoubleArrayList(newPosition2));
        task.eval(newSolution2);

        // Greedy replacement
        if (task.problem.isFirstBetter(newSolution2, currentSolution)) {
            population.set(i, newSolution2);
        }
    }

    private void calculate(int i) throws StopCriterionException {
        int k = RNG.nextInt(popSize);

        if (k == i) {
            k = (i + 1) % popSize;
        }

        NumberSolution<Double> carrion = population.get(k);
        double fitCarrion = carrion.getEval();
        NumberSolution<Double> current = population.get(i);
        double fitCurrent = current.getEval();

        // Calculate I = round(1 + rand)
        double randNum = RNG.nextDouble();

        int I = (int) Math.round(1 + randNum);

        // Calculate new position
        double[] newPosition = new double[task.problem.getNumberOfDimensions()];
        for (int d = 0; d < newPosition.length; d++) {
            double currentVal = current.getValue(d);
            double carrionVal = carrion.getValue(d);

            double rand = RNG.nextDouble();

            if (fitCurrent > fitCarrion) {
                newPosition[d] = currentVal + rand * (carrionVal - I * currentVal);
            } else {
                newPosition[d] = currentVal + rand * (currentVal - carrionVal);
            }
        }

        task.problem.makeFeasible(newPosition);

        if (task.isStopCriterion())
            return;

        NumberSolution<Double> newSolution = new NumberSolution<>(Util.toDoubleArrayList(newPosition));
        task.eval(newSolution);

        // Greedy replacement
        if (task.problem.isFirstBetter(newSolution, population.get(i))) {
            population.set(i, newSolution);
        }
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }
}

