package org.um.feri.ears.algorithms.so.pso;

import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.random.RNG;

public class PsoSolution extends NumberSolution<Double> {
    public PsoSolution pBest;
    public double[] velocity;

    public PsoSolution(Task<NumberSolution<Double>, DoubleProblem> t) throws StopCriterionException {
        super(t.getRandomEvaluatedSolution());
        velocity = new double[t.problem.getNumberOfDimensions()];
        double l;
        double r;
        for (int i = 0; i < t.problem.getNumberOfDimensions(); i++) {
            l = -Math.abs(t.problem.getUpperLimit(i) - t.problem.getLowerLimit(i)) / 4;
            r = Math.abs(t.problem.getUpperLimit(i) - t.problem.getLowerLimit(i)) / 4;
            velocity[i] = RNG.nextDouble(l, r);
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
