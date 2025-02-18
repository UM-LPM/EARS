package org.um.feri.ears.algorithms.so.mrfo;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;

public class MRFO extends NumberAlgorithm {

    @AlgorithmParameter(name = "population size")
    private final int popSize;

    private ArrayList<NumberSolution<Double>> population;
    private NumberSolution<Double> bestSolution;

    //defaults construct
    public MRFO() {
        this(30);
    }

    //construct
    public MRFO(int popSize) {
        super();
        this.popSize = popSize;
        au = new Author("Dejan", "dejan.turnsek1@student.um.si");
        ai = new AlgorithmInfo("MRFO", "Manta Ray Foraging Optimization",
                "@article{zhao2020manta,"
                        + "  title={Manta ray foraging optimization: An effective bio-inspired optimizer for engineering applications},"
                        + "  author={Zhao, Wen-Tao and Wang, Liang and Zhang, Xiao-Yong},"
                        + "  journal={Engineering Applications of Artificial Intelligence},"
                        + "  volume={87},"
                        + "  pages={103300},"
                        + "  year={2020},"
                        + "  publisher={Elsevier}}"
        );

    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {

        this.task = task;
        int dimensions = task.problem.getNumberOfDimensions();

        initPopulation();

        int maxIt = 10000;
        if (task.getStopCriterion() == StopCriterion.ITERATIONS) {
            maxIt = task.getMaxIterations();
        }
        if (task.getStopCriterion() == StopCriterion.EVALUATIONS) {
            maxIt = (task.getMaxEvaluations() - popSize) / popSize;
        }

        while (!task.isStopCriterion()) {
            //+1.0 because source code is in matlab which start at index 1
            double coef = (task.getNumberOfIterations() + 1.0) / maxIt;
            ArrayList<NumberSolution<Double>> newPopulation = new ArrayList<>(popSize); // to update pop. after all iterations

            for (int i = 0; i < popSize; i++) {
                NumberSolution<Double> manta = population.get(i);
                double[] newPosition = new double[dimensions];
                //cyclone foraging
                if (RNG.nextDouble() < 0.5) {

                    double r1 = RNG.nextDouble();
                    //beta = 2 * exp(r1 * ((MaxIt - It + 1) / MaxIt)) * (sin(2 * pi * r1));
                    double beta = 2 * Math.exp(r1 * ((double) (maxIt - task.getNumberOfIterations()) / maxIt)) * Math.sin(2 * Math.PI * r1);
                    // Cyclone foraging equation(3.3)
                    //explore best solution space
                    if (coef > RNG.nextDouble()) {
                        //first agent
                        if (i == 0) {
                            for (int d = 0; d < dimensions; d++) {
                                newPosition[d] = bestSolution.getValue(d) +
                                        RNG.nextDouble() * (bestSolution.getValue(d) - manta.getValue(d)) +
                                        beta * (bestSolution.getValue(d) - manta.getValue(d));
                            }
                        } //all other
                        else {
                            for (int d = 0; d < dimensions; d++) {
                                newPosition[d] = bestSolution.getValue(d) +
                                        RNG.nextDouble() * (population.get(i - 1).getValue(d) - manta.getValue(d)) +
                                        beta * (bestSolution.getValue(d) - manta.getValue(d));
                            }
                        }
                        //Cyclone foraging
                    } else {
                        //exploration a random point in search space
                        // Reference random solution //equation(7)
                        if (i == 0) {
                            for (int d = 0; d < dimensions; d++) {
                                double randomSol = RNG.nextDouble(task.problem.lowerLimit.get(d), task.problem.upperLimit.get(d));
                                newPosition[d] = randomSol +
                                        RNG.nextDouble() * (randomSol - manta.getValue(d)) +
                                        beta * (randomSol - manta.getValue(d));
                            }
                        } else {
                            for (int d = 0; d < dimensions; d++) {
                                double randomSol = RNG.nextDouble(task.problem.lowerLimit.get(d), task.problem.upperLimit.get(d));
                                newPosition[d] = randomSol +
                                        RNG.nextDouble() * (population.get(i - 1).getValue(d) - manta.getValue(d)) +
                                        beta * (randomSol - manta.getValue(d));
                            }
                        }
                    }
                }
                // Chain foraging //equation(1)
                else {
                    //alpha needs to be calculated each time to match the source code
                    if (i == 0) {
                        for (int j = 0; j < dimensions; j++) {
                            double alpha = 2 * RNG.nextDouble() * Math.sqrt(-Math.log(RNG.nextDouble()));
                            newPosition[j] = manta.getValue(j) +
                                    RNG.nextDouble() * (bestSolution.getValue(j) - manta.getValue(j)) +
                                    alpha * (bestSolution.getValue(j) - manta.getValue(j));
                        }
                    } else {
                        for (int j = 0; j < dimensions; j++) {
                            double alpha = 2 * RNG.nextDouble() * Math.sqrt(-Math.log(RNG.nextDouble()));
                            newPosition[j] = manta.getValue(j) +
                                    RNG.nextDouble() * (population.get(i - 1).getValue(j) - manta.getValue(j)) +
                                    alpha * (bestSolution.getValue(j) - manta.getValue(j));
                        }
                    }
                }
                //sets limits inside the predefined limits
                task.problem.makeFeasible(newPosition);

                if (task.isStopCriterion()) break;
                NumberSolution<Double> newSolution = new NumberSolution<>(Util.toDoubleArrayList(newPosition));
                task.eval(newSolution);
                newPopulation.add(newSolution);
            }

            if (!task.isStopCriterion()) {
                for (int i = 0; i < popSize; i++) {
                    if (task.problem.isFirstBetter(newPopulation.get(i), population.get(i))) {
                        population.set(i, newPopulation.get(i));
                    }
                }
            }

            // Somersault foraging
            if (!task.isStopCriterion()) {
                ArrayList<NumberSolution<Double>> somersaultPopulation = new ArrayList<>(popSize);
                for (int i = 0; i < popSize; i++) {
                    NumberSolution<Double> manta = population.get(i);
                    double[] newPosition = new double[dimensions];

                    for (int d = 0; d < dimensions; d++) {
                        double somersaultFactor = 2.0; //range size
                        newPosition[d] = manta.getValue(d) +
                                somersaultFactor * (RNG.nextDouble() * bestSolution.getValue(d) -
                                        RNG.nextDouble() * manta.getValue(d));
                    }

                    task.problem.makeFeasible(newPosition);

                    if (task.isStopCriterion()) break;

                    NumberSolution<Double> newSolution = new NumberSolution<>(Util.toDoubleArrayList(newPosition));
                    task.eval(newSolution);
                    somersaultPopulation.add(newSolution);
                }

                for (int i = 0; i < somersaultPopulation.size(); i++) { //if breaks in the middle of iteration can be less than pop_size
                    if (task.problem.isFirstBetter(somersaultPopulation.get(i), population.get(i))) {
                        population.set(i, somersaultPopulation.get(i));
                    }
                }
            }

            for (NumberSolution<Double> solution : population) {
                if (task.problem.isFirstBetter(solution, bestSolution)) {
                    bestSolution = solution;
                }
            }
            task.incrementNumberOfIterations();
        }
        return bestSolution;
    }

    private void initPopulation() throws StopCriterionException {
        this.population = new ArrayList<>();
        this.bestSolution = null;

        for (int i = 0; i < popSize; i++) {
            if (task.isStopCriterion())
                break;
            NumberSolution<Double> solution = task.generateRandomEvaluatedSolution();
            population.add(solution);
            updateBestSolution(solution);
        }
    }

    private void updateBestSolution(NumberSolution<Double> solution) {
        if (bestSolution == null || task.problem.isFirstBetter(solution, bestSolution)) {
            bestSolution = solution;
        }
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }
}