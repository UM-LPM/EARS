package org.um.feri.ears.algorithms.so.pso;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

import java.util.ArrayList;

public class PSO extends Algorithm {

    private int popSize;
    private ArrayList<MyPSOSolution> population;
    private MyPSOSolution g; //global best
    // C1- cognitive coefficient, C2 - social coefficient, omega- inertia weight
    private double omega, phiG, phiP;
    private Task task;

    public PSO() {
        this(10, 0.7, 2, 2);
    }

    public PSO(int popSize, double om, double c1, double c2) {
        super();
        this.popSize = popSize;
        this.omega = om;
        this.phiP = c1;
        this.phiG = c2;
        setDebug(debug);  //EARS prints some debug info
        ai = new AlgorithmInfo("Wiki", "Wiki", "PSO", "My Wiki PSO");  //EARS add algorithm name
        ai.addParameter(EnumAlgorithmParameters.POP_SIZE, popSize + "");
        ai.addParameter(EnumAlgorithmParameters.C1, c1 + "");
        ai.addParameter(EnumAlgorithmParameters.C2, c2 + "");
        ai.addParameter(EnumAlgorithmParameters.UNNAMED1, om + "");
        //ai.addParameter(EnumAlgorithmParameters., F + "");
        au = new Author("Matej", "matej.crepinsek at um.si"); //EARS author info
    }

    @Override
    public DoubleSolution execute(Task taskProblem) throws StopCriterionException {
        task = taskProblem;
        initPopulation();
        //double rp, rg;
        double[] v;
        while (!task.isStopCriterion()) {
            for (int i = 0; i < popSize; i++) {
                //rp = Util.rnd.nextDouble(); better to use vector of real numbers
                //rg = Util.rnd.nextDouble();
                v = new double[task.getNumberOfDimensions()];
                // r*vec(x) double r = Util.rnd.nextDouble();
                for (int d = 0; d < task.getNumberOfDimensions(); d++) {
                    v[d] = omega * (
                            population.get(i).getV()[d]) +
                            phiP * Util.rnd.nextDouble() * (population.get(i).getP().getValue(d) - population.get(i).getValue(d)) +
                            phiG * Util.rnd.nextDouble() * (g.getValue(d) - population.get(i).getValue(d));
                    //if (v[d]>(taskProblem.getIntervalLength()[d])) v[d]=taskProblem.getIntervalLength()[d];
                    //if (v[d]<(taskProblem.getIntervalLength()[d])) v[d]=-taskProblem.getIntervalLength()[d];
                }
                population.set(i, population.get(i).update(taskProblem, v));
                if (task.isFirstBetter(population.get(i), g)) g = population.get(i);
                if (task.isStopCriterion()) break;
            }
            task.incrementNumberOfIterations();
        }
        return g;
    }

    private void initPopulation() throws StopCriterionException {
        population = new ArrayList<>();
        for (int i = 0; i < popSize; i++) {
            population.add(new MyPSOSolution(task));
            if (i == 0) g = population.get(0);
            else if (task.isFirstBetter(population.get(i), g)) g = population.get(i);
            if (task.isStopCriterion()) break;
        }
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {

    }
}