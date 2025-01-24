package org.um.feri.ears.algorithms.so.ao;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.random.PredefinedRandom;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;

import org.apache.commons.math3.special.Gamma;

public class AO extends NumberAlgorithm {

    private boolean usePredefinedRandom = false;

    @AlgorithmParameter(name = "population size")
    private int popSize;

    private ArrayList<NumberSolution<Double>> population;
    private double alpha, beta, delta;
    private double U, r0, omega, phi0;

    NumberSolution<Double> bestIndividual;

    public AO()
    {
        this(20);
    }

    public AO(int popSize) {

        this.popSize = popSize;

        this.alpha  = 0.1;
        this.beta   = 1.5;
        this.delta  = 0.1;
        this.U      = 0.0265;
        this.r0     = 10.0;
        this.omega  = 0.005;
        this.phi0   = 3 * Math.PI / 2;          //Eq. (12)

        au = new Author("viktorija", "viktorija.stevanoska@student.um.si");
        ai = new AlgorithmInfo("AO", "Aquila Optimizer",
                "@article{abualigah2021aquila,"
                        + "  title={Aquila Optimizer},"
                        + "  author={Abualigah, L., Yousri, D., Elaziz, M.A., Ewees, A.A., A. Al-qaness, M.A., Gandomi, A.H.},"
                        + "  journal={Computers & Industrial Engineering},"
                        + "  volume={157},"
                        + "  year={2021},"
                        + "  publisher={Elsevier}}"
        );
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException
    {
        this.task = task;

        initPopulation();

        double[] phi = new double[task.problem.getNumberOfDimensions()];
        double[] r   = new double[task.problem.getNumberOfDimensions()];
        double[] x   = new double[task.problem.getNumberOfDimensions()];
        double[] y   = new double[task.problem.getNumberOfDimensions()];

        for (int i = 0; i < phi.length; ++i) {

            phi[i] = ((-1) * omega * (i + 1)) + phi0;   //Eq. (11)
            r[i] = r0 + (U * (i + 1));                  //Eq. (10)

            x[i] = r[i] * Math.sin(phi[i]);             //Eq. (9)
            y[i] = r[i] * Math.cos(phi[i]);             //Eq. (8)
        }

        while (!task.isStopCriterion()) {
            double G1 = (2 * RNG.nextDouble()) - 1; //Eq. (16)
            double G2 = 2.0 * (1.0 - ((task.getNumberOfIterations() + 1.0)  / task.getMaxIterations()));    //Eq. (17)

            double QF = Math.pow(task.getNumberOfIterations() + 1, (((2 * RNG.nextDouble()) - 1) / (Math.pow(1 - task.getMaxIterations(), 2))));   //Eq. (15)

            for (int i = 0; i < popSize; ++i) {

                double[] newIndividual = new double[task.problem.getNumberOfDimensions()];

                if (task.getNumberOfIterations() + 1 <= ((2.0 / 3.0) * task.getMaxIterations())) {

                    if (RNG.nextDouble() < 0.5) {  //Expanded exploration X1

                        double randNumber = RNG.nextDouble();

                        for (int j = 0; j < task.problem.getNumberOfDimensions(); ++j) {

                            newIndividual[j] = bestIndividual.getValue(j) * (1.0 - ((task.getNumberOfIterations() + 1.0) / task.getMaxIterations())) +
                                    (mean(i) - bestIndividual.getValue(j)) * randNumber;        //Eq. (3)
                        }
                    } else { //Narrowed exploration X2
                        double[] levyVector = Levy();

                        double randNumber1 = RNG.nextDouble();
                        double randNumber2 = RNG.nextDouble();

                        for (int j = 0; j < task.problem.getNumberOfDimensions(); ++j) {
                            newIndividual[j] = (bestIndividual.getValue(j) * levyVector[j]) +
                                    population.get((int) Math.floor((population.size() * randNumber1))).getValue(j) + ((y[j] - x[j]) * randNumber2);    //Eq. (5)
                        }
                    }
                } else {
                    if (RNG.nextDouble() < 0.5) { //Expanded exploitation X3
                        double randNumber1 = RNG.nextDouble();
                        double randNumber2 = RNG.nextDouble();

                        for (int j = 0; j < task.problem.getNumberOfDimensions(); ++j) {

                            newIndividual[j] = (bestIndividual.getValue(j) - mean(j)) * alpha - randNumber1 +
                                    (randNumber2 * (task.problem.getUpperLimit(j) - task.problem.getLowerLimit(j)) + task.problem.getLowerLimit(j)) * delta;    //Eq. (13)
                        }

                    } else { //Narrowed exploitation X4
                        double randNumber1 = RNG.nextDouble();
                        double randNumber2 = RNG.nextDouble();
                        double[] levyVector = Levy();

                        for (int j = 0; j < task.problem.getNumberOfDimensions(); ++j) {
                            newIndividual[j] = QF * bestIndividual.getValue(j) - (G1 * population.get(i).getValue(j) * randNumber1) - G2 * levyVector[j] + randNumber2 * G1; //Eq. (14)
                        }
                    }
                }

                NumberSolution<Double> newSolution = new NumberSolution<>(Util.toDoubleArrayList(newIndividual));

                if (!task.problem.isFeasible(newSolution))
                    task.problem.makeFeasible(newSolution);

                if (task.isStopCriterion())
                    break;

                task.eval(newSolution);

                if (task.problem.isFirstBetter(newSolution, population.get(i)))
                    population.set(i, newSolution);
            }

            for (int i = 0; i < popSize; ++i) {
                if (task.problem.isFirstBetter(population.get(i), bestIndividual))
                    bestIndividual = population.get(i);
            }
            task.incrementNumberOfIterations();
        }
        return bestIndividual;
    }

    private void initPopulation() throws StopCriterionException
    {
        population = new ArrayList<>();

        for (int i = 0; i < popSize; i++) {
            if (task.isStopCriterion())
                break;
            population.add(task.generateRandomEvaluatedSolution());
            if (task.problem.isFirstBetter(population.get(i), bestIndividual))
                bestIndividual = population.get(i);
        }
    }

    private double[] Levy()
    {
        double sigma = Math.pow(((Gamma.gamma(1 + beta) * Math.sin((Math.PI * beta) / 2)) / (Gamma.gamma((1 + beta) / 2) * beta * Math.pow(2.0, ((beta - 1) / 2)))), (1.0 / beta));     //Eq. (7)

        double[] result = new double[task.problem.getNumberOfDimensions()];

        for (int i = 0; i < task.problem.getNumberOfDimensions(); ++i) {
            result[i] = RNG.nextGaussian() * sigma / (Math.pow(Math.abs(RNG.nextGaussian()), (1.0 / beta)));
        }
        return result;
    }

    private double mean(int individual)
    {
        double mean = 0.0;

        for (int i = 0; i < task.problem.getNumberOfDimensions(); ++i)
            mean += population.get(individual).getValue(i);

        return mean / task.problem.getNumberOfDimensions();
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }
}