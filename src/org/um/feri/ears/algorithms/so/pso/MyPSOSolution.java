package org.um.feri.ears.algorithms.so.pso;

import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

import java.util.Arrays;

public class MyPSOSolution extends DoubleSolution {
    private MyPSOSolution p; //personal best
    private double[] v;


    public MyPSOSolution getP() {
        return p;
    }

    public void setP(MyPSOSolution p) {
        this.p = p;
    }

    public double[] getV() {
        return v;
    }

    public MyPSOSolution(Task t) throws StopCriteriaException {
        super(t.getRandomSolution());
        v = new double[t.getNumberOfDimensions()];
        double l;
        double r;
        for (int i = 0; i < t.getNumberOfDimensions(); i++) {
            l = -Math.abs(t.getUpperLimit()[i] - t.getLowerLimit()[i]) / 4;
            r = Math.abs(t.getUpperLimit()[i] - t.getLowerLimit()[i]) / 4;
            v[i] = Util.nextDouble(l, r);
        }
        p = this;
    }

    public MyPSOSolution(DoubleSolution eval) {
        super(eval);

    }

    @Override
    public String toString() {
        return super.toString() + " v:" + (Arrays.toString(v) + " p:" + p.getEval());
    }

    public MyPSOSolution update(Task t, double[] v) throws StopCriteriaException {
        double[] x = getDoubleVariables();
        for (int i = 0; i < x.length; i++) {
            x[i] = t.setFeasible(x[i] + v[i], i);
        }
        MyPSOSolution tmp = new MyPSOSolution(t.eval(x));
        if (t.isFirstBetter(tmp, p)) {
            tmp.p = tmp;
        } else
            tmp.p = p;
        tmp.v = v;
        return tmp;
    }
}
