package org.um.feri.ears.algorithms.so.rmo;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.random.RNG;

public class RMO extends NumberAlgorithm {

	@AlgorithmParameter(name = "population size")
    private int popSize;
    private double C1, C2, k;
    private double[][] X;
    private double[] cp;
    private NumberSolution<Double> cpS;
    private double[][] V;

    public RMO() {
        this(100, 0.7, 0.8, 10);
    }

    public RMO(int popSize, double C1, double C2, double k) {
        this.popSize = 100;
        this.C1 = 0.7;
        this.C2 = 0.8;
        this.k = 10;

        ai = new AlgorithmInfo("RMO", "Radial Movement Optimization", "@article{rahmani2014new,title={A new simple, fast and efficient algorithm for global optimization over continuous search-space problems: Radial movement optimization},author={Rahmani, Rasoul and Yusof, Rubiyah},journal={Applied Mathematics and Computation},volume={248},pages={287--300},year={2014},publisher={Elsevier}}");
        au = new Author("Luka", "luka.horvat@student.um.si");
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {

        double[] globalBestX = null;
        NumberSolution<Double> globalBest = null;

        X = new double[popSize][task.problem.getNumberOfDimensions()];
        V = new double[popSize][task.problem.getNumberOfDimensions()];

        for (int i = 0; i < popSize; ++i) {

            for (int j = 0; j < X[i].length; ++j) {
                X[i][j] = RNG.nextDouble(task.problem.getLowerLimit(j), task.problem.getUpperLimit(j));
            }

            NumberSolution<Double> newSolution = new NumberSolution<>(Util.toDoubleArrayList(X[i]));
            task.eval(newSolution);

            //Pick best starting center
            if (i == 0) {
                cp = X[i];
                cpS = new NumberSolution<>(newSolution);

                //System.out.println(task.getNumberOfEvaluations()+" "+ cp_s);
            } else if (task.problem.isFirstBetter(newSolution, cpS)) {
                cp = X[i];
                cpS = new NumberSolution<>(newSolution);

            }

            if (task.isStopCriterion()) {
                return cpS;
            }
        }

        cp = cp.clone();

        //Main loop
        while (!task.isStopCriterion()) {
            //Calculate W
            double W = 1.0 - (1.0 / task.getMaxEvaluations()) * task.getNumberOfEvaluations();
            //W = 1;
            double[] currentBest = new double[task.problem.getNumberOfDimensions()];
            NumberSolution<Double> currentBestS = null;

            //Calculate velocity vectors and move particles
            for (int i = 0; i < popSize; ++i) {
                for (int j = 0; j < V[i].length; ++j) {
                    //Velocity vector
                    V[i][j] = RNG.nextDouble(-1, 1) * ((task.problem.getUpperLimit(j) - task.problem.getLowerLimit(j)) / k);//RNG.nextDouble(task.getLowerLimit()[j], task.problem.getUpperLimit()[j]) / 100.0;

                    //Move particle and check constrains
                    X[i][j] = task.problem.setFeasible(V[i][j] * W + cp[j], j);
                }

                if (task.isStopCriterion()) {
                    if (globalBest != null)
                        return globalBest;
                    else
                        return currentBestS;
                }

                NumberSolution<Double> newSolution = new NumberSolution<>(Util.toDoubleArrayList(X[i]));
                task.eval(newSolution);

                //Check if particles is better
                if (i == 0) {
                    currentBestS = newSolution;
                    currentBest = X[i];
                } else if (task.problem.isFirstBetter(newSolution, currentBestS)) {
                    currentBestS = newSolution;
                    currentBest = X[i];
                }
            }

            //Move the center
            if (globalBestX == null) {
                for (int j = 0; j < cp.length; ++j) {
                    cp[j] = cp[j] + C2 * (currentBest[j] - cp[j]);
                }

                globalBestX = currentBest.clone();
                globalBest = new NumberSolution<>(currentBestS);
            } else {
                for (int j = 0; j < cp.length; ++j) {
                    cp[j] = cp[j] + C1 * (globalBestX[j] - cp[j]) + C2 * (currentBest[j] - cp[j]);
                }

                if (task.problem.isFirstBetter(currentBestS, globalBest)) {
                    globalBestX = currentBest.clone();
                    globalBest = new NumberSolution<>(currentBestS);

                    //System.out.println(task.getNumberOfEvaluations()+" "+ globalBest);
                }
            }

            task.incrementNumberOfIterations();
        }
        return globalBest;
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {

    }
}