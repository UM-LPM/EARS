package org.um.feri.ears.algorithms.so.sho;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.math3.special.Gamma;

public class SHO extends NumberAlgorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;

    private ArrayList<NumberSolution<Double>> seahorses;
    private NumberSolution<Double> bestSeahorse;

    public SHO() {
        this(30);
    }

    public SHO(int popSize) {
        super();
        this.popSize = popSize;

        au = new Author("Hana Zadravec", "hana.zadravec@student.um.si");
        ai = new AlgorithmInfo("SHO", "Sea-horse Optimizer",
                "@article{zhao2022seahorse,"
                        + "  title={Sea-horse optimizer: a novel nature-inspired meta-heuristic for global optimization problems},"
                        + "  author={Zhao, Shijie and Zhang, Tianran and Ma, Shilin and Wang, Mengchen},"
                        + "  journal={Applied Intelligence},"
                        + "  volume={53},"
                        + "  pages={11833--11860},"
                        + "  year={2023},"
                        + "  publisher={Springer}}"
        );
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;

        int dim = task.problem.getNumberOfDimensions();

        int maxIt;
        if (task.getStopCriterion() == StopCriterion.ITERATIONS) {
            maxIt = task.getMaxIterations();
        } else if (task.getStopCriterion() == StopCriterion.EVALUATIONS) {
            maxIt = (task.getMaxEvaluations() - popSize) / popSize;
        } else {
            maxIt = 10000;
        }

        initPopulation();

        double[] seahorsesFitness = new double[popSize];
        for (int i = 0; i < popSize; i++) {

            if (task.isStopCriterion())
                break;
            task.eval(seahorses.get(i));

            seahorsesFitness[i] = seahorses.get(i).getObjective(0);

            if (task.problem.isFirstBetter(seahorses.get(i), bestSeahorse)) {
                bestSeahorse = seahorses.get(i);
            }

        }

        Integer[] sortedIndexes = new Integer[popSize];
        for (int i = 0; i < popSize; i++) {
            sortedIndexes[i] = i;
        }

        Arrays.sort(sortedIndexes, Comparator.comparingDouble(i -> seahorsesFitness[i]));

        bestSeahorse = new NumberSolution<>(seahorses.get(sortedIndexes[0]));

        final double u = 0.05;
        final double v = 0.05;
        final double l = 0.05;

        while (!task.isStopCriterion()) {
            // Generate beta
            double[][] beta = new double[popSize][dim];
            for (int i = 0; i < popSize; i++) {
                for (int j = 0; j < dim; j++) {
                    beta[i][j] = RNG.nextGaussian();
                }
            }

            // generate Elite
            NumberSolution<Double>[] elite = new NumberSolution[popSize];
            for (int i = 0; i < popSize; i++) {
                elite[i] = new NumberSolution<>(bestSeahorse);
            }

            // 1. The motor behavior of seahorses
            double[] r1 = new double[popSize];
            for (int i = 0; i < popSize; i++) {
                r1[i] = RNG.nextGaussian();
            }

            double[][] stepLength = levy(popSize, dim, 1.5);

            // 2. Motor behavior
            ArrayList<NumberSolution<Double>> newSeahorses1 = new ArrayList<>();
            for (int i = 0; i < popSize; i++) {
                NumberSolution<Double> newSolution = new NumberSolution<>(new Double[dim]);
                for (int j = 0; j < dim; j++) {
                    if (r1[i] > 0) {
                        // Spiral movement (Eq.4)
                        double r = RNG.nextDouble();
                        double theta = r * 2 * Math.PI;
                        double row = u * Math.exp(theta * v);
                        double x = row * Math.cos(theta);
                        double y = row * Math.sin(theta);
                        double z = row * theta;

                        double currentVal = seahorses.get(i).getValue(j);
                        double eliteVal = elite[i].getValue(j);
                        double newValue = currentVal + stepLength[i][j] *
                                ((eliteVal - currentVal) * x * y * z + eliteVal);
                        newSolution.setValue(j, newValue);
                    } else {
                        // Random movement (Eq.7)
                        double currentVal = seahorses.get(i).getValue(j);
                        double eliteVal = elite[i].getValue(j);
                        double newValue = currentVal + RNG.nextDouble() * l * beta[i][j] *
                                (currentVal - beta[i][j] * eliteVal);
                        newSolution.setValue(j, newValue);
                    }
                }
                newSeahorses1.add(newSolution);
            }

            for (NumberSolution<Double> solution : newSeahorses1) {
                task.problem.makeFeasible(solution);
            }

            // 2. Predation behaviour
            ArrayList<NumberSolution<Double>> newSeahorses2 = new ArrayList<>(popSize);
            double[] seahorsesFitness1 = new double[popSize];
            int t = task.getNumberOfIterations() + 1;
            double alpha = StrictMath.pow(1 - (double) t / maxIt, 2 * (double) t / maxIt);

            for (int i = 0; i < popSize; i++) {
                NumberSolution<Double> newSolution = new NumberSolution<>(new Double[dim]);

                for (int j = 0; j < dim; j++) {
                    double r2 = RNG.nextDouble();
                    double eliteVal = elite[i].getValue(j);
                    double currentVal = newSeahorses1.get(i).getValue(j);

                    if (r2 >= 0.1) {
                        // Eq.10 - first variant
                        double randVal = RNG.nextDouble();
                        double newValue = alpha * (eliteVal - randVal * currentVal) + (1 - alpha) * eliteVal;
                        newSolution.setValue(j, newValue);
                    } else {
                        // Eq.10 - second variant
                        double randVal = RNG.nextDouble();
                        double newValue = (1 - alpha) * (currentVal - randVal * eliteVal) + alpha * currentVal;
                        newSolution.setValue(j, newValue);
                    }
                }

                task.problem.makeFeasible(newSolution);
                newSeahorses2.add(newSolution);

                if (task.isStopCriterion()) {
                    while (newSeahorses2.size() < popSize) {
                        newSeahorses2.add(newSeahorses2.get(newSeahorses2.size() - 1).copy());
                    }
                    break;
                }

                task.eval(newSolution);
                seahorsesFitness1[i] = newSolution.getObjective(0);

                if (task.problem.isFirstBetter(newSeahorses2.get(i), bestSeahorse)) {
                    bestSeahorse = newSeahorses2.get(i);
                }
            }

            // Sort based on fitness
            Integer[] indexes = new Integer[popSize];
            for (int i = 0; i < popSize; i++) {
                indexes[i] = i;
            }
            Arrays.sort(indexes, Comparator.comparingDouble(i -> seahorsesFitness1[i]));

            // 3. Reproductive behavior

            // Eq.2
            int halfPop = popSize / 2;
            double[] seahorsesFitness2 = new double[halfPop];
            List<NumberSolution<Double>> fathers = new ArrayList<>();
            List<NumberSolution<Double>> mothers = new ArrayList<>();

            // Select fathers (best half) and mothers (second best half)
            for (int i = 0; i < halfPop; i++) {
                fathers.add(newSeahorses2.get(indexes[i]).copy());
                mothers.add(newSeahorses2.get(indexes[halfPop + i]).copy());
            }
            // Create offspring
            List<NumberSolution<Double>> seahorsesOffspring = new ArrayList<>();
            for (int k = 0; k < halfPop; k++) {
                NumberSolution<Double> child = new NumberSolution<>(new Double[dim]);
                double r3 = RNG.nextDouble();

                for (int j = 0; j < dim; j++) {
                    double value = r3 * fathers.get(k).getValue(j) + (1 - r3) * mothers.get(k).getValue(j);
                    child.setValue(j, value);
                }

                task.problem.makeFeasible(child);

                if (task.isStopCriterion()) {

                    while (seahorsesOffspring.size() < halfPop) {
                        if (!seahorsesOffspring.isEmpty()) {
                            seahorsesOffspring.add(seahorsesOffspring.get(seahorsesOffspring.size() - 1).copy());
                        } else {
                            seahorsesOffspring.add(fathers.get(0).copy());
                        }
                    }
                    break;
                }

                task.eval(child);
                seahorsesFitness2[k] = child.getObjective(0);
                if (task.problem.isFirstBetter(child, bestSeahorse)) {
                    bestSeahorse = child;
                }
                seahorsesOffspring.add(child);
            }

            // Combine populations and select best
            double[] combinedFitness = new double[seahorsesFitness1.length + seahorsesFitness2.length];

            System.arraycopy(seahorsesFitness1, 0, combinedFitness, 0, seahorsesFitness1.length);
            System.arraycopy(seahorsesFitness2, 0, combinedFitness, seahorsesFitness1.length, seahorsesFitness2.length);


            ArrayList<NumberSolution<Double>> combinedPopulation = new ArrayList<>(popSize + popSize / 2);
            combinedPopulation.addAll(newSeahorses2);
            combinedPopulation.addAll(seahorsesOffspring);

            // Sort combined population
            Integer[] combinedIndexes = new Integer[combinedPopulation.size()];
            for (int i = 0; i < combinedPopulation.size(); i++) {
                combinedIndexes[i] = i;
            }
            Arrays.sort(combinedIndexes, Comparator.comparingDouble(i -> combinedFitness[i]));

            // Create new population
            List<NumberSolution<Double>> newPopulation = new ArrayList<>();
            for (int i = 0; i < popSize; i++) {
                newPopulation.add(new NumberSolution<>(combinedPopulation.get(combinedIndexes[i])));
            }
            seahorses = new ArrayList<>(newPopulation);

            double[] sortFitBestN = new double[popSize];
            for (int i = 0; i < popSize; i++) {
                sortFitBestN[i] = combinedFitness[combinedIndexes[i]];
            }

            if (sortFitBestN[0] < bestSeahorse.getObjective(0)) {
                bestSeahorse = new NumberSolution<>(seahorses.get(0));
            }
            task.incrementNumberOfIterations();
        }
        return bestSeahorse;
    }

    private void initPopulation() throws StopCriterionException {
        seahorses = new ArrayList<>();

        for (int i = 0; i < popSize; i++) {
            if (task.isStopCriterion())
                break;
            seahorses.add(task.generateRandomEvaluatedSolution());
        }
    }

    // Eq. 5 - levy flight
    public double[][] levy(int pop, int m, double omega) {
        double num = StrictMath.sin(StrictMath.PI * omega / 2) * Gamma.gamma(1 + omega);
        double den = Gamma.gamma((1 + omega) / 2) * omega * StrictMath.pow(2, (omega - 1) / 2);
        double sigma_u = StrictMath.pow(num / den, 1 / omega);

        double[][] z = new double[pop][m];
        for (int i = 0; i < pop; i++) {
            for (int j = 0; j < m; j++) {
                double u = sigma_u * RNG.nextGaussian();
                double v = RNG.nextGaussian();
                z[i][j] = u / StrictMath.pow(StrictMath.abs(v), 1 / omega);
            }
        }
        return z;
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }
}