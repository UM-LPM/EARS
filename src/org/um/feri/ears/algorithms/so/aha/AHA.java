package org.um.feri.ears.algorithms.so.aha;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.random.RNG;
import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.annotation.AlgorithmParameter;

import java.util.ArrayList;
import java.util.List;

public class AHA extends NumberAlgorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;
    private NumberSolution<Double> best;
    private ArrayList<NumberSolution<Double>> population;

    public AHA() {
        this(50);
    }

    public AHA(int popSize) {
        super();
        this.popSize = popSize;

        au = new Author("tilen", "tilen.heric@student.um.si");
        ai = new AlgorithmInfo("AHA", "Artificial Hummingbird Algorithm",
                "@article{ZHAO2022114194,\n" +
                        "title = {Artificial hummingbird algorithm: A new bio-inspired optimizer with its engineering applications},\n" +
                        "author = {Weiguo Zhao and Liying Wang and Seyedali Mirjalili},\n" +
                        "journal = {Computer Methods in Applied Mechanics and Engineering},\n" +
                        "volume = {388},\n" +
                        "pages = {114194},\n" +
                        "year = {2022},"
        );
    }

    private void initPopulation() throws StopCriterionException {
        population = new ArrayList<>();
        population.add(task.generateRandomEvaluatedSolution());
        best = population.get(0);

        for (int i = 1; i < popSize; i++) {
            if (task.isStopCriterion())
                break;
            population.add(task.generateRandomEvaluatedSolution());
            if (task.problem.isFirstBetter(population.get(i), best)) {
                best = population.get(i);
            }
        }
    }

    private double[][] setDiagonalValues(double[][] visitTable) {
        for (int i = 0; i < popSize; i++) {
            visitTable[i][i] = -Double.MAX_VALUE;
        }
        return visitTable;
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;
        initPopulation();
        double[][] visitTable = new double[popSize][popSize];
        for (int i = 0; i < popSize; i++) {
            for (int j = 0; j < popSize; j++) {
                visitTable[i][j] = 0.0;
            }
        }

        while (!task.isStopCriterion()) {
            // Direction
            visitTable = setDiagonalValues(visitTable);
            long start = System.currentTimeMillis();
            for (int i = 0; i < popSize; i++) {
                double[][] directVector = new double[popSize][task.problem.getNumberOfDimensions()];

                double r = RNG.nextDouble();
                // Diagonal flight
                if (r < 1.0 / 3.0) {
                    int[] randomDimensions = new int[task.problem.getNumberOfDimensions()];
                    double randomNumber;
                    randomDimensions = RNG.randomPermutation(task.problem.getNumberOfDimensions());
                    if (task.problem.getNumberOfDimensions() >= 3)
                        randomNumber = Math.ceil(RNG.nextDouble() * (task.problem.getNumberOfDimensions() - 2));
                    else
                        randomNumber = Math.ceil(RNG.nextDouble() * (task.problem.getNumberOfDimensions() - 1));
                    for (int a = 0; a <= randomNumber; a++)
                        directVector[i][randomDimensions[a]] = 1.0;
                } else if (r > 2.0 / 3.0) { // Omnidirectional flight
                    for (int a = 0; a < task.problem.getNumberOfDimensions(); a++)
                        directVector[i][a] = 1.0;
                } else { // Axial flight
                    int randomNumber = (int) Math.ceil(RNG.nextDouble() * (task.problem.getNumberOfDimensions() - 1));
                    directVector[i][randomNumber] = 1.0;
                }

                // Guided foraging
                if (RNG.nextDouble() < 0.5) {
                    double maxUnvisitedTime = visitTable[i][0];
                    int targetFoodIndex = 0;

                    int[] mutIndex = new int[visitTable[i].length];
                    int mutIndexCount = 0;
                    mutIndex[mutIndexCount++] = 0;

                    for (int a = 1; a < visitTable[i].length; a++) {
                        if (maxUnvisitedTime < visitTable[i][a]) {
                            maxUnvisitedTime = visitTable[i][a];
                            targetFoodIndex = a;
                            mutIndexCount = 0;  // Reset the count
                            mutIndex[mutIndexCount++] = a;
                        } else if (maxUnvisitedTime == visitTable[i][a]) {
                            mutIndex[mutIndexCount++] = a;
                        }
                    }
                    if (mutIndexCount > 1) {
                        int ind = mutIndex[0];  // Start with the first index in mutIndex
                        for (int a = 1; a < mutIndexCount; a++) {
                            if (task.problem.isFirstBetter(population.get(mutIndex[a]), population.get(ind))) {
                                ind = mutIndex[a];
                            }
                        }
                        targetFoodIndex = ind;
                    }

                    ArrayList<Double> newSolutionVariables = new ArrayList<Double>();
                    double gauss = RNG.nextGaussian();
                    for (int a = 0; a < population.get(targetFoodIndex).numberOfVariables(); a++) {
                        newSolutionVariables.add(
                                population.get(targetFoodIndex).getValue(a) + gauss * directVector[i][a] * (
                                        population.get(i).getValue(a) - population.get(targetFoodIndex).getValue(a)
                                )
                        );
                    }
                    if (task.isStopCriterion())
                        break;
                    NumberSolution<Double> newSolution = new NumberSolution<Double>(newSolutionVariables);
                    task.problem.makeFeasible(newSolution);
                    task.eval(newSolution);

                    if (task.problem.isFirstBetter(newSolution, population.get(i))) {
                        population.set(i, newSolution);
                        for (int a = 0; a < visitTable[i].length; a++)
                            visitTable[i][a] += 1;
                        //  assign i-th column max value +1 in each row
                        for (int a = 0; a < popSize; a++) {
                            visitTable[a][i] = Util.max(visitTable[a]) + 1;
                        }
                        visitTable[i][i] = -Double.MAX_VALUE;
                    } else {
                        for (int a = 0; a < visitTable[i].length; a++)
                            visitTable[i][a] += 1;
                    }
                    visitTable[i][targetFoodIndex] = 0;
                } else {// Territorial foraging
                    ArrayList<Double> newSolutionVariables = new ArrayList<Double>();
                    double gauss = RNG.nextGaussian();
                    for (int a = 0; a < population.get(i).numberOfVariables(); a++) {
                        newSolutionVariables.add(
                                population.get(i).getValue(a) + gauss * directVector[i][a] * population.get(i).getValue(a)
                        );
                    }

                    if (task.isStopCriterion())
                        break;
                    NumberSolution<Double> newSolution = new NumberSolution<Double>(newSolutionVariables);
                    task.problem.makeFeasible(newSolution);
                    task.eval(newSolution);
                    if (task.problem.isFirstBetter(newSolution, population.get(i))) {
                        population.set(i, newSolution);
                        for (int a = 0; a < visitTable[i].length; a++)
                            visitTable[i][a] += 1;
                        //  assign i-th column max value +1 in each row
                        for (int a = 0; a < popSize; a++) {
                            visitTable[a][i] = Util.max(visitTable[a]) + 1;
                        }
                        visitTable[i][i] = -Double.MAX_VALUE;
                    } else {
                        for (int a = 0; a < visitTable[i].length; a++)
                            visitTable[i][a] += 1;
                    }
                }
            }


            // Migration foraging
            if (task.getNumberOfIterations() % (2 * popSize) == 0) {
                setDiagonalValues(visitTable);

                List<Integer> migrationIndex = new ArrayList<Integer>();
                double temp = population.get(0).getEval();
                migrationIndex.add(0);
                for (int a = 1; a < popSize; a++) {
                    if (temp < population.get(a).getEval()) {
                        temp = population.get(a).getEval();
                        migrationIndex = new ArrayList<Integer>();
                        migrationIndex.add(a);
                    }
                }

                for (int index : migrationIndex) {
                    for (int a = 0; a < population.get(index).numberOfVariables(); a++) {
                        population.get(index).setValue(a,
                                RNG.nextDouble() * (task.problem.upperLimit.get(a) - task.problem.lowerLimit.get(a)) + task.problem.lowerLimit.get(a)
                        );
                    }
                    for (int a = 0; a < visitTable[index].length; a++) {
                        visitTable[index][a] += 1;
                    }
                }

                for (int a = 0; a < visitTable.length; a++) {
                    for (int index : migrationIndex) {
                        visitTable[a][index] = Util.max(visitTable[a]) + 1;
                    }
                }

                for (int a : migrationIndex) {
                    for (int b : migrationIndex) {
                        visitTable[a][b] = -Double.MAX_VALUE;
                    }
                }

                for (int index : migrationIndex) {
                    task.eval(population.get(index));
                }

                setDiagonalValues(visitTable);
            }

            for (int a = 0; a < popSize; a++) {
                if (task.problem.isFirstBetter(population.get(a), best))
                    best = population.get(a);
            }

            task.incrementNumberOfIterations();
        }
        return best;
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {

    }
}
