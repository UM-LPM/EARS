package org.um.feri.ears.algorithms.so.sma;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.util.Point;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import static org.um.feri.ears.util.SpecialFunction.atanh;

public class SMA extends NumberAlgorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize; // alias N or number of Searching Agents
    private double z;

    private ArrayList<NumberSolution<Double>> population; // population is equivalent to Searching Agents
    private Point<Double> bestPositions;
    private NumberSolution<Double> bestSlimeMould;

    public SMA() {
        this(30);
    }

    public SMA(int popSize) {
        this(popSize, 0.03);
    }

    public SMA(int popSize, double z) {
        super();
        this.popSize = popSize;
        this.z = z;

        au = new Author("anej", "anej.krajnc2@student.um.si");
        ai = new AlgorithmInfo("SMA", "Slime Mould Algorithm",
                "@article{article," +
                        "   author = {Li, Shimin and Chen, Huiling and Wang, Mingjing and Heidari, Ali Asghar}," +
                        "   year = {2020}," +
                        "   month = {04}," +
                        "   pages = {300-323}," +
                        "   title = {Slime mould algorithm: A new method for stochastic optimization}," +
                        "   volume = {https://aliasgharheidari.com/SMA.html}," +
                        "   journal = {Future Generation Computer Systems}," +
                        "   doi = {10.1016/j.future.2020.03.055}" +
                        "}");
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;
        initPopulation();
        int maxIt = 10000;

        double destinationFitness;
        if(task.problem.getObjectiveMaximizationFlags()[0])
            destinationFitness = Double.NEGATIVE_INFINITY;
        else
            destinationFitness = Double.POSITIVE_INFINITY;

        double[] allFitness = new double[popSize];
        double[][] weights = new double[popSize][task.problem.getNumberOfDimensions()];

        if (task.getStopCriterion() == StopCriterion.ITERATIONS)
            maxIt = task.getMaxIterations();

        if (task.getStopCriterion() == StopCriterion.EVALUATIONS)
            maxIt = (task.getMaxEvaluations() - popSize) / popSize;

        while (!task.isStopCriterion()) {

            for (int index = 0; index < popSize; index++) {
                NumberSolution<Double> currentSlimeMould = population.get(index);

                if (!task.problem.isFeasible(currentSlimeMould)) {
                    task.problem.makeFeasible(currentSlimeMould);
                }

                if (task.isStopCriterion()) break;
                task.eval(currentSlimeMould);

                allFitness[index] = currentSlimeMould.getEval();
            }

            Integer[] sortedFitnessIndexes = sortArrayWithIndexes(allFitness); // Eq. (2.6)

            double worstFitness = allFitness[popSize - 1];
            double bestFitness = allFitness[sortedFitnessIndexes[0]];

            double S = bestFitness - worstFitness + Double.MIN_VALUE; // + Double.MIN_Value to avoid determination zero

            // Calculate Fitness weight of each slime mold
            for (int i = 0; i < popSize; i++) {
                for (int j = 0; j < task.problem.getNumberOfDimensions(); j++) {
                    if (i <= (popSize / 2) - 1) // Eq. (2.5)
                        weights[sortedFitnessIndexes[i]][j] = 1 + RNG.nextDouble() * Math.log10(((bestFitness - allFitness[sortedFitnessIndexes[i]]) / S) + 1);
                    else
                        weights[sortedFitnessIndexes[i]][j] = 1 - RNG.nextDouble() * Math.log10(((bestFitness - allFitness[sortedFitnessIndexes[i]]) / S) + 1);
                }
            }

            // Update the best fitness value and best position
            if (task.problem.isFirstBetter(bestFitness, destinationFitness, 0)) {
                destinationFitness = bestFitness;
            }
            if (task.problem.isFirstBetter(population.get(sortedFitnessIndexes[0]), bestSlimeMould)) {
                bestSlimeMould = population.get(sortedFitnessIndexes[0]);
            }

            double a = atanh(-((double) (task.getNumberOfIterations() + 1) / (double) maxIt) + 1.0); // Eq. (2.6)
            double b = 1.0 - (((double) task.getNumberOfIterations() + 1) / maxIt);

            // Update the positions of search agents in population
            for (int i = 0; i < popSize; i++) {
                NumberSolution<Double> currentSlimMould = population.get(i);
                Point<Double> newPosition = currentSlimMould.getVariablesAsPoint();

                if (RNG.nextDouble() < z) { // Eq. (2.7)
                    for (int j = 0; j < task.problem.getNumberOfDimensions(); j++)
                        newPosition.set(j, ((task.problem.getUpperLimit(j) - task.problem.getLowerLimit(j)) * RNG.nextDouble()) + task.problem.getLowerLimit(j));
                } else {
                    double p = Math.tanh(Math.abs(allFitness[i] - destinationFitness));
                    double[] vb = new double[task.problem.getNumberOfDimensions()];
                    double[] vc = new double[task.problem.getNumberOfDimensions()];

                    for (int j = 0; j < task.problem.getNumberOfDimensions(); j++) { // Since there is seperated for loop for generating vb and vc we should do here same as well
                        vb[j] = RNG.nextDouble(-a, a); // Eq. (2.3)
                        vc[j] = RNG.nextDouble(-b, b);
                    }

                    for (int j = 0; j < task.problem.getNumberOfDimensions(); j++) {
                        double r = RNG.nextDouble();
                        int A = RNG.nextInt(0, popSize); // two positions randomly selected from population
                        int B = RNG.nextInt(0, popSize);
                        if (r < p) // Eq. (2.1)
                            newPosition.set(j, bestSlimeMould.getVariables().get(j) + vb[j] * (weights[i][j] * population.get(A).getValue(j) - population.get(B).getValue(j)));
                        else
                            newPosition.set(j, vc[j] * population.get(i).getValue(j));
                    }
                }

                NumberSolution<Double> newSolution = new NumberSolution<>(newPosition);
                population.set(i, newSolution);
            }

            task.incrementNumberOfIterations();
        }

        return bestSlimeMould;
    }

    private void initPopulation() {
        population = new ArrayList<>();
        for (int i = 0; i < popSize; i++) {
            population.add(task.problem.generateRandomSolution());
        }
    }

    public Integer[] sortArrayWithIndexes(double[] array) {
        Integer[] indexes = new Integer[array.length];
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = i;
        }

        Arrays.sort(indexes, new Comparator<Integer>() {
            @Override
            public int compare(final Integer o1, final Integer o2) {
                return task.problem.isFirstBetter(array[o1], array[o2], 0) ? -1 : 1;
            }
        });
        return indexes;
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }
}
