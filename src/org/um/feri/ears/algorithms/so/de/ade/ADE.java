package org.um.feri.ears.algorithms.so.de.ade;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.comparator.ProblemComparator;
import org.um.feri.ears.util.random.RNG;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class ADE extends NumberAlgorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;
    @AlgorithmParameter(description = "minimum population size")
    private int popSizeMin;
    @AlgorithmParameter(description = "initial population size")
    private int initPopSize;
    @AlgorithmParameter(description = "size of the first population pop1Size = NPopinit", isTunable = false)
    private int pop1Size;
    @AlgorithmParameter(description = "size of the second population pop2Size = popSize - pop1Size", isTunable = false)
    private int pop2Size;
    @AlgorithmParameter(description = "minimum value of scaling factor")
    private double betaMin;
    @AlgorithmParameter(description = "maximum value of scaling factor")
    private double betaMax;
    @AlgorithmParameter(description = "crossover probability")
    private double pCR;

    private ArrayList<NumberSolution<Double>> population;
    private ArrayList<ArrayList<Double>> chaoticPop;

    public ADE() { this(30, 4, 28, 0.2, 0.8, 0.2);}

    /**
     * Constructor
     *
     * @param popSizeMin
     *                  minimum population size
     * @param initPopSize
     *                  initial population size
     * @param betaMin
     *                  minimum value of scaling factor
     * @param betaMax
     *                  maximum value of scaling factor
     * @param pCR
     *                  crossover probability
     */
    public ADE(int popSize, int popSizeMin, int initPopSize, double betaMin, double betaMax, double pCR) {
        super();
        this.popSize = popSize;
        this.popSizeMin = popSizeMin;
        this.initPopSize = initPopSize;
        this.betaMin = betaMin;
        this.betaMax = betaMax;
        this.pCR = pCR;

        au = new Author("ziga", "ziga.pusnik@student.um.si");
        ai = new AlgorithmInfo("ADE", "Advanced Differential Evolution",
                "@article{abbasi2024ade,"
                        + "  title={ADE: Advanced Differential Evolution},"
                        + "  author={Abbasi, Behzad and Majidnezhad, Vahid and Mirjalili, Seyedali},"
                        + "  journal={Neural Computing and Applications},"
                        + "  volume={36},"
                        + "  issue={25},"
                        + "  pages={1-32},"
                        + "  year={2024},"
                        + "  publisher={Springer}}"
        );
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;
        this.pop1Size = initPopSize;
        this.pop2Size = popSize - pop1Size;
        ArrayList<Double> lb = (ArrayList<Double>) task.problem.lowerLimit;
        ArrayList<Double> ub = (ArrayList<Double>) task.problem.upperLimit;
        int maxIt = 10000;
        if (task.getStopCriterion() == StopCriterion.ITERATIONS) {
            maxIt = task.getMaxIterations();
        }

        if (task.getStopCriterion() == StopCriterion.EVALUATIONS) {
            maxIt = (task.getMaxEvaluations() - popSize) / popSize;
        }

        // Create chaotic population of solutions
        population = new ArrayList<>();
        chaoticInitialization();
        for (ArrayList<Double> position : chaoticPop) {
            if (task.isStopCriterion())
                break;
            NumberSolution<Double> newSolution = new NumberSolution<>(position);
            task.eval(newSolution);
            population.add(newSolution);
        }

        NumberSolution<Double> bestSol = population.get(0).copy();
        for (NumberSolution<Double> curSolution : population) {
            if (task.problem.isFirstBetter(curSolution, bestSol)) {
                bestSol = curSolution.copy();
            }
        }
        // Create two subpopulations
        ArrayList<NumberSolution<Double>> pop1 = new ArrayList<>(population.subList(0, pop1Size));
        ArrayList<NumberSolution<Double>> pop2 = new ArrayList<>(population.subList(pop1Size, population.size()));

        while (!task.isStopCriterion()) {
            pop1Size = (int) Math.round((((double) (popSizeMin - initPopSize) / maxIt) * task.getNumberOfIterations()) + initPopSize);
            if (pop1Size < pop1.size()) {
                pop2Size = popSize - pop1Size;
                pop2.add(bestSol.copy());
                // Find and remove the worst solution in pop1
                int worstSolutionIndex = findWorstSolutionIndex(pop1);
                if (worstSolutionIndex != -1) {
                    pop1.remove(worstSolutionIndex);
                }
            }

            // Phase 1: Update pop1
            for (int i = 0; i < pop1Size; i++) {
                ArrayList<Double> x1 = pop1.get(i).getVariables();

                int[] permutation = RNG.randomPermutation(pop1Size);
                List<Integer> A = new ArrayList<>(Arrays.stream(permutation).boxed().toList());
                int finalI = i;
                A.removeIf(x -> x == finalI);
                int a = A.get(0);
                int b = A.get(1);
                int c = A.get(2);

                // Scale Factor (beta1)
                ArrayList<Double> positionB = pop1.get(b).getVariables();
                ArrayList<Double> positionC = pop1.get(c).getVariables();
                double normBC = calculateNorm(subtractVectors(positionB, positionC));
                double normBounds = calculateNorm(subtractVectors(ub, lb)) / 10.0;
                ArrayList<Double> beta1 = new ArrayList<>();
                if (normBC > normBounds) {
                    for (int temp = 0; temp < task.problem.getNumberOfDimensions(); temp++) {
                        beta1.add(RNG.nextDouble(betaMin, betaMax));
                    }
                } else {
                    for (int temp = 0; temp < task.problem.getNumberOfDimensions(); temp++) {
                        beta1.add(RNG.nextDouble(betaMin, betaMax * (1 + RNG.nextDouble())));
                    }
                }

                ArrayList<Double> positionA = pop1.get(a).getVariables();
                ArrayList<Double> y1 = addVectors(positionA, multiplyVector(subtractVectors(positionB, positionC), beta1));
                if (!task.problem.isFeasible(y1)) {
                    task.problem.makeFeasible(y1);
                }
                ArrayList<Double> z1 = crossover(x1, y1);

                if (task.isStopCriterion())
                    break;
                NumberSolution<Double> newSol = new NumberSolution<>(z1);
                task.eval(newSol);
                if (task.problem.isFirstBetter(newSol, pop1.get(i))) {
                    pop1.set(i, newSol);
                }

                if (task.problem.isFirstBetter(pop1.get(i), bestSol)) {
                    bestSol = pop1.get(i).copy();
                }
            }

            // Phase 2: Update pop2
            if (pop2Size >= (2 * popSizeMin)) {
                ArrayList<NumberSolution<Double>> temp = new ArrayList<>();
                for (NumberSolution<Double> sol : pop1) {
                    temp.add(sol.copy());
                }
                // Sort the temporary copy of solutions with ProblemComparator
                temp.sort(new ProblemComparator<>(task.problem));
                // Replace first popSizeMin solutions in pop2 with sorted solutions
                for (int i = 0; i < popSizeMin; i++) {
                    pop2.set(i, temp.get(i));
                }
            }

            for (int j = 0; j < pop2Size; j++) {
                ArrayList<Double> x2 = pop2.get(j).getVariables();

                // Calculate resOfTop2
                ArrayList<Double> position0 = pop2.get(0).getVariables();
                ArrayList<Double> position1 = pop2.get(1).getVariables();
                ArrayList<Double> temp1 = new ArrayList<>();
                for (int i = 0; i < task.problem.getNumberOfDimensions(); i++) {
                    temp1.add(RNG.nextDouble());
                }
                ArrayList<Double> temp2 = new ArrayList<>();
                for (int i = 0; i < task.problem.getNumberOfDimensions(); i++) {
                    temp2.add(RNG.nextDouble());
                }
                ArrayList<Double> resOfTop2 = addVectors(multiplyVector(temp1, subtractVectors(x2, position0)), multiplyVector(temp2, subtractVectors(x2, position1)));

                ArrayList<Double> beta2 = new ArrayList<>();
                for (int temp = 0; temp < task.problem.getNumberOfDimensions(); temp++) {
                    beta2.add(RNG.nextDouble(betaMin, betaMax));
                }

                ArrayList<Double> y2 = multiplyVector(resOfTop2, beta2);
                if (!task.problem.isFeasible(y2)) {
                    task.problem.makeFeasible(y2);
                }
                ArrayList<Double> z2 = crossover(x2, y2);

                if (task.isStopCriterion())
                    break;
                NumberSolution<Double> newSol2 = new NumberSolution<>(z2);
                task.eval(newSol2);
                if (task.problem.isFirstBetter(newSol2, bestSol)) {
                    bestSol = newSol2.copy();
                }
                pop2.add(newSol2);
            }

            // Sort pop2 and retain only Pop2Size solutions
            pop2.sort(new ProblemComparator<>(task.problem));
            pop2 = new ArrayList<>(pop2.subList(0, pop2Size));

            task.incrementNumberOfIterations();
        }

        return bestSol;
    }

    private void chaoticInitialization() {
        chaoticPop = new ArrayList<>();
        ArrayList<Double> x = new ArrayList<>();
        for (int i = 0; i < task.problem.getNumberOfDimensions(); i++) {
            x.add(RNG.nextDouble());
        }

        for (int i = 0; i < popSize; i++) {
            x = logisticMap(x);
            if (!task.problem.isFeasible(x)) {
                task.problem.makeFeasible(x);
            }
            chaoticPop.add(x);
        }
    }

    public static ArrayList<Double> logisticMap(ArrayList<Double> x) {
        return logisticMap(x, 4.0);
    }

    public static ArrayList<Double> logisticMap(ArrayList<Double> x, double mu) {
        ArrayList<Double> result = new ArrayList<>();
        for (double xi : x) {
            result.add(mu * xi * (1 - xi));
        }
        return result;
    }

    private int findWorstSolutionIndex(ArrayList<NumberSolution<Double>> population) {
        NumberSolution<Double> worstSolution = population.get(0);
        int worstIndex = 0;

        for (int i = 1; i < population.size(); i++) {
            if (task.problem.isFirstBetter(worstSolution, population.get(i))) {
                worstSolution = population.get(i);
                worstIndex = i;
            }
        }
        return worstIndex;
    }

    private static ArrayList<Double> subtractVectors(ArrayList<Double> vectorA, ArrayList<Double> vectorB) {
        if (vectorA.size() != vectorB.size()) {
            throw new IllegalArgumentException("Vectors must have the same size for subtraction.");
        }
        ArrayList<Double> result = new ArrayList<>(vectorA.size());
        for (int i = 0; i < vectorA.size(); i++) {
            result.add(vectorA.get(i) - vectorB.get(i));
        }
        return result;
    }

    private static ArrayList<Double> addVectors(ArrayList<Double> vectorA, ArrayList<Double> vectorB) {
        if (vectorA.size() != vectorB.size()) {
            throw new IllegalArgumentException("Vectors must have the same size.");
        }
        ArrayList<Double> result = new ArrayList<>();
        for (int i = 0; i < vectorA.size(); i++) {
            result.add(vectorA.get(i) + vectorB.get(i));
        }
        return result;
    }

    private static ArrayList<Double> multiplyVector(ArrayList<Double> vector, ArrayList<Double> scalar) {
        ArrayList<Double> result = new ArrayList<>();
        for (int i = 0; i < vector.size(); i++) {
            result.add(vector.get(i) * scalar.get(i));
        }
        return result;
    }


    private static double calculateNorm(ArrayList<Double> vector) {
        double sum = 0.0;
        for (double v : vector) {
            sum += v * v;
        }
        return Math.sqrt(sum);
    }

    private ArrayList<Double> crossover(ArrayList<Double> x, ArrayList<Double> y) {
        if (x.size() != y.size()) {
            throw new IllegalArgumentException("Vectors x and y must have the same size.");
        }
        ArrayList<Double> z = new ArrayList<>(x.size());
        int j0 = RNG.nextInt(x.size());
        for (int jj = 0; jj < x.size(); jj++) {
            if (jj == j0 || RNG.nextDouble() <= pCR) {
                z.add(y.get(jj));
            } else {
                z.add(x.get(jj));
            }
        }
        return z;
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }

}
