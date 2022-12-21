package org.um.feri.ears.algorithms.so.pso;

import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

public class PsoSolution extends NumberSolution<Double> {
    public PsoSolution pBest;
    public double[] velocity;

    public PsoSolution(Task t) throws StopCriterionException {
        super(t.getRandomEvaluatedSolution());
        velocity = new double[t.getNumberOfDimensions()];
        double l;
        double r;
        for (int i = 0; i < t.getNumberOfDimensions(); i++) {
            l = -Math.abs(t.getUpperLimit()[i] - t.getLowerLimit()[i]) / 4;
            r = Math.abs(t.getUpperLimit()[i] - t.getLowerLimit()[i]) / 4;
            velocity[i] = Util.nextDouble(l, r);
        }
        pBest = new PsoSolution(this);
    }

    public PsoSolution(NumberSolution<Double> solution) {
        super(solution);
    }

    public void updatePosition(double[] velocity) {
        for (int i = 0; i < velocity.length; i++) {
            setValue(i, getValue(i) + velocity[i]);
        }
        this.velocity = velocity;
    }
}
