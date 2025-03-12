package org.um.feri.ears.algorithms.so.avoa;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.random.RNG;
import org.apache.commons.math3.special.Gamma;

import java.util.ArrayList;

public class AVOA extends NumberAlgorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;
    private NumberSolution<Double> bestVulture1;
    private NumberSolution<Double> bestVulture2;
    private ArrayList<NumberSolution<Double>> population;

    // Control parameters according to source code
    private final double p1 = 0.6;
    private final double p2 = 0.4;
    private final double p3 = 0.6;
    private final double alpha = 0.8;

    public AVOA() {
        this(30); //paper and source code: 30
    }

    public AVOA(int popSize) {
        super();
        this.popSize = popSize;

        au = new Author("marko", "marko.bakan@student.um.si");
        ai = new AlgorithmInfo("AVOA", "African Vultures Optimization Algorithm", "@article{abdollahzadeh2021african,\n"
                + "  title={African vultures optimization algorithm: A new nature-inspired metaheuristic algorithm for global optimization problems},\n"
                + "  author={Abdollahzadeh, Benyamin and Gharehchopogh, Farhad Soleimanian and Mirjalili, Seyedali},\n"
                + "  journal={Computers & Industrial Engineering},\n"
                + "  pages={107408},\n" + "  year={2021},\n" + "  publisher={Elsevier}}");
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;
        initPopulation();

        int maxIt;
        if (task.getStopCriterion() == StopCriterion.ITERATIONS) {
            maxIt = task.getMaxIterations();
        } else {
            maxIt = (task.getMaxEvaluations() - popSize) / popSize;
        }

        while (!task.isStopCriterion()) {
            // Update coefficient vectors using bounded progress calculation
            double progress = Math.min(1.0, Math.max(0.0, (double) task.getNumberOfIterations() / maxIt));
            double a = RNG.nextDouble(-2, 2) * (Math.sin((Math.PI / 2) * progress) + Math.cos((Math.PI / 2) * progress) - 1);
            double P1 = (2 * RNG.nextDouble() + 1) * (1 - progress) + a;

            // Update the location
            for (int i = 0; i < popSize; i++) {
                NumberSolution<Double> currentVulture = population.get(i);
                double F = P1 * (2 * RNG.nextDouble() - 1);

                NumberSolution<Double> randomVulture;
                if (RNG.nextDouble() < alpha) {
                    randomVulture = bestVulture1;
                } else {
                    randomVulture = bestVulture2;
                }

                // Exploration phase
                if (Math.abs(F) >= 1) {
                    updateVultureExploration(currentVulture, randomVulture, F, i);
                }
                // Exploitation phase
                else {
                    updateVultureExploitation(currentVulture, randomVulture, F, i);
                }
            }

            updateBestVultures();
            task.incrementNumberOfIterations();
        }

        return bestVulture1;
    }

    private void initPopulation() throws StopCriterionException {
        population = new ArrayList<>();

        // Initialize Best_vulture1, Best_vulture2
        bestVulture1 = task.generateRandomEvaluatedSolution();
        population.add(bestVulture1);

        if (task.isStopCriterion()) return;

        bestVulture2 = task.generateRandomEvaluatedSolution();
        population.add(bestVulture2);

        if (task.problem.isFirstBetter(bestVulture2, bestVulture1)) {
            NumberSolution<Double> temp = bestVulture1;
            bestVulture1 = bestVulture2;
            bestVulture2 = temp;
        }

        // Initialize the remaining population
        for (int i = 2; i < popSize; i++) {
            if (task.isStopCriterion()) break;
            NumberSolution<Double> newSolution = task.generateRandomEvaluatedSolution();
            population.add(newSolution);

            if (task.problem.isFirstBetter(newSolution, bestVulture1)) {
                bestVulture2 = bestVulture1;
                bestVulture1 = newSolution;
            } else if (task.problem.isFirstBetter(newSolution, bestVulture2)) {
                bestVulture2 = newSolution;
            }
        }
    }

    private void updateVultureExploration(NumberSolution<Double> currentVulture, NumberSolution<Double> randomVulture, double F, int index) throws StopCriterionException {
        ArrayList<Double> newPosition = new ArrayList<>();

        if (RNG.nextDouble() < p1) {
            // F1 strategy
            for (int j = 0; j < task.problem.getNumberOfDimensions(); j++) {
                double value = randomVulture.getValue(j) - (Math.abs((2 * RNG.nextDouble()) * randomVulture.getValue(j) - currentVulture.getValue(j))) * F;
                newPosition.add(value);
            }
        } else {
            // F2 strategy
            for (int j = 0; j < task.problem.getNumberOfDimensions(); j++) {
                double value = randomVulture.getValue(j) - F + RNG.nextDouble() * ((task.problem.upperLimit.get(j) - task.problem.lowerLimit.get(j)) * RNG.nextDouble() + task.problem.lowerLimit.get(j));
                newPosition.add(value);
            }
        }

        if (task.isStopCriterion()) return;

        NumberSolution<Double> newSolution = new NumberSolution<>(newPosition);
        task.problem.makeFeasible(newSolution);
        task.eval(newSolution);

        if (task.problem.isFirstBetter(newSolution, population.get(index))) {
            population.set(index, newSolution);
        }
    }

    private void updateVultureExploitation(NumberSolution<Double> currentVulture, NumberSolution<Double> randomVulture, double F, int index) throws StopCriterionException {
        ArrayList<Double> newPosition = new ArrayList<>();

        if (Math.abs(F) < 0.5) {
            if (RNG.nextDouble() < p2) {
                // Phase 1 strategy
                for (int j = 0; j < task.problem.getNumberOfDimensions(); j++) {
                    double value1 = bestVulture1.getValue(j) - ((bestVulture1.getValue(j) * currentVulture.getValue(j)) / (bestVulture1.getValue(j) - Math.pow(currentVulture.getValue(j), 2))) * F;
                    double value2 = bestVulture2.getValue(j) - ((bestVulture2.getValue(j) * currentVulture.getValue(j)) / (bestVulture2.getValue(j) - Math.pow(currentVulture.getValue(j), 2))) * F;
                    newPosition.add((value1 + value2) / 2);
                }
            } else {
                // Using Levy flight
                ArrayList<Double> levySteps = generateLevySteps(task.problem.getNumberOfDimensions());
                for (int j = 0; j < task.problem.getNumberOfDimensions(); j++) {
                    double value = randomVulture.getValue(j) - Math.abs(randomVulture.getValue(j) - currentVulture.getValue(j)) * F * levySteps.get(j);
                    newPosition.add(value);
                }
            }
        } else {
            if (RNG.nextDouble() < p3) {
                // Phase 2 strategy 1
                for (int j = 0; j < task.problem.getNumberOfDimensions(); j++) {
                    double value = (Math.abs((2 * RNG.nextDouble()) * randomVulture.getValue(j) - currentVulture.getValue(j))) * (F + RNG.nextDouble()) - (randomVulture.getValue(j) - currentVulture.getValue(j));
                    newPosition.add(value);
                }
            } else {
                // Phase 2 strategy 2
                for (int j = 0; j < task.problem.getNumberOfDimensions(); j++) {
                    double s1 = randomVulture.getValue(j) * (RNG.nextDouble() * currentVulture.getValue(j) / (2 * Math.PI)) * Math.cos(currentVulture.getValue(j));
                    double s2 = randomVulture.getValue(j) * (RNG.nextDouble() * currentVulture.getValue(j) / (2 * Math.PI)) * Math.sin(currentVulture.getValue(j));
                    double value = randomVulture.getValue(j) - (s1 + s2);
                    newPosition.add(value);
                }
            }
        }

        if (task.isStopCriterion()) return;

        NumberSolution<Double> newSolution = new NumberSolution<>(newPosition);
        task.problem.makeFeasible(newSolution);
        task.eval(newSolution);

        if (task.problem.isFirstBetter(newSolution, population.get(index))) {
            population.set(index, newSolution);
        }
    }

    private ArrayList<Double> generateLevySteps(int dimensions) {
        ArrayList<Double> steps = new ArrayList<>();
        double beta = 3.0 / 2.0;

        // Calculate sigma using the Levy flight formula
        double sigma = Math.pow((Gamma.gamma(1 + beta) * Math.sin(Math.PI * beta / 2)) / (Gamma.gamma((1 + beta) / 2) * beta * Math.pow(2, (beta - 1) / 2)), 1 / beta);

        for (int i = 0; i < dimensions; i++) {
            double u = RNG.nextGaussian() * sigma;
            double v = RNG.nextGaussian();
            double step = u / Math.pow(Math.abs(v), 1 / beta);
            steps.add(step);
        }
        return steps;
    }

    private void updateBestVultures() {
        for (NumberSolution<Double> vulture : population) {
            if (task.problem.isFirstBetter(vulture, bestVulture1)) {
                bestVulture2 = bestVulture1;
                bestVulture1 = vulture;
            } else if (task.problem.isFirstBetter(vulture, bestVulture2)) {
                bestVulture2 = vulture;
            }
        }
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
        popSize = 30;
    }
}