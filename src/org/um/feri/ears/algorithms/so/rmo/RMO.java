package org.um.feri.ears.algorithms.so.rmo;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.annotation.AlgorithmParameter;

public class RMO extends Algorithm {

	@AlgorithmParameter(name = "population size")
    private int popSize;
    private double C1, C2, k;
    private double[][] X;
    private double[] cp;
    private DoubleSolution cpS;
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
    public DoubleSolution execute(Task task) throws StopCriterionException {

        double[] globalBestX = null;
        DoubleSolution globalBest = null;

        X = new double[popSize][task.getNumberOfDimensions()];
        V = new double[popSize][task.getNumberOfDimensions()];

        for (int i = 0; i < popSize; ++i) {

            for (int j = 0; j < X[i].length; ++j) {
                X[i][j] = Util.nextDouble(task.getLowerLimit()[j], task.getUpperLimit()[j]);
            }

            DoubleSolution eval = task.eval(X[i]);

            //Pick best starting center
            if (i == 0) {
                cp = X[i];
                cpS = new DoubleSolution(eval);

                //System.out.println(task.getNumberOfEvaluations()+" "+ cp_s);
            } else if (task.isFirstBetter(eval, cpS)) {
                cp = X[i];
                cpS = new DoubleSolution(eval);

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
            double[] currentBest = new double[task.getNumberOfDimensions()];
            DoubleSolution currentBestS = null;

            //Calculate velocity vectors and move particles
            for (int i = 0; i < popSize; ++i) {
                for (int j = 0; j < V[i].length; ++j) {
                    //Velocity vector
                    V[i][j] = Util.nextDouble(-1, 1) * ((task.getUpperLimit()[j] - task.getLowerLimit()[j]) / k);//Util.nextDouble(task.getLowerLimit()[j], task.getUpperLimit()[j]) / 100.0;

                    //Move particle and check constrains
                    X[i][j] = task.setFeasible(V[i][j] * W + cp[j], j);
                }

                DoubleSolution eval;
                if (task.isStopCriterion()) {
                    if (globalBest != null)
                        return globalBest;
                    else
                        return currentBestS;
                }
                eval = task.eval(X[i]);


                //Check if particles is better
                if (i == 0) {
                    currentBestS = eval;
                    currentBest = X[i];
                } else if (task.isFirstBetter(eval, currentBestS)) {
                    currentBestS = eval;
                    currentBest = X[i];
                }
            }

            //Move the center
            if (globalBestX == null) {
                for (int j = 0; j < cp.length; ++j) {
                    cp[j] = cp[j] + C2 * (currentBest[j] - cp[j]);
                }

                globalBestX = currentBest.clone();
                globalBest = new DoubleSolution(currentBestS);
            } else {
                for (int j = 0; j < cp.length; ++j) {
                    cp[j] = cp[j] + C1 * (globalBestX[j] - cp[j]) + C2 * (currentBest[j] - cp[j]);
                }

                if (task.isFirstBetter(currentBestS, globalBest)) {
                    globalBestX = currentBest.clone();
                    globalBest = new DoubleSolution(currentBestS);

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