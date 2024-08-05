package org.um.feri.ears.algorithms.so.pso;

import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;
import java.util.List;

public class PsoSolutionv2 extends NumberSolution<Double> {
    public PsoSolutionv2 pBest;
    public PsoSolutionv2 lBest;
    public List<Double> velocity;

    public PsoSolutionv2(Task<NumberSolution<Double>, DoubleProblem> t) throws StopCriterionException {
        super(t.getRandomEvaluatedSolution());
        velocity = new ArrayList<>(t.problem.getNumberOfDimensions());
        for (int i = 0; i < t.problem.getNumberOfDimensions(); i++) {
            velocity.add(0.0);
        }
        pBest = new PsoSolutionv2(this);
    }

    public PsoSolutionv2(NumberSolution<Double> solution) {
        super(solution);
    }

    public void updatePosition(List<Double> velocity) {
        for (int i = 0; i < velocity.size(); i++) {
            setValue(i, getValue(i) + velocity.get(i));
        }
        this.velocity = velocity;
    }
}
