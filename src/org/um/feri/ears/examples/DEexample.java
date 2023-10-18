package org.um.feri.ears.examples;

import java.util.ArrayList;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.random.RNG;

public class DEexample extends NumberAlgorithm {
    int popSize;
    double CR, F;
    ArrayList<NumberSolution<Double>> pop;
    NumberSolution<Double> best;

    //Initialize all agents {\displaystyle \mathbf {x} } \mathbf {x}  with random positions in the search-space.
    public DEexample(int ps, double CR, double F) {
        popSize = ps;
        this.CR = CR;
        this.F = F;
        ai = new AlgorithmInfo("DE", " Deferential Evolution", "");
        au = new Author("author name", "author email");

    }

    public void init() throws StopCriterionException {
        pop = new ArrayList<>();
        NumberSolution<Double> tmp;
        for (int i = 0; i < popSize; i++) {
            if (task.isStopCriterion()) break;
            tmp = task.getRandomEvaluatedSolution();
            if (i == 0) best = tmp;
            else if (task.problem.isFirstBetter(tmp, best)) best = tmp;
            pop.add(tmp);
        }
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;
        init();
        int a, b, c, R;
        NumberSolution<Double> newSolution;
        while (!task.isStopCriterion()) {
            for (int i = 0; i < popSize; i++) {
                if (task.isStopCriterion()) break;
                do
                    a = RNG.nextInt(popSize);
                while (a == i);
                do
                    b = RNG.nextInt(popSize);
                while ((b == i) || (b == a));
                do
                    c = RNG.nextInt(popSize);
                while ((c == i) || (c == a) || (c == b));
                R = RNG.nextInt(task.problem.getNumberOfDimensions());
                double[] x = new double[task.problem.getNumberOfDimensions()];
                for (int j = 0; j < task.problem.getNumberOfDimensions(); j++) {
                    if ((RNG.nextDouble() < CR) || (j == R)) {
                        x[j] = task.problem.setFeasible(pop.get(a).getValue(j) + F * (pop.get(b).getValue(j) - pop.get(c).getValue(j)), j);
                    } else x[j] = pop.get(i).getValue(j);
                }

                newSolution = new NumberSolution<>(Util.toDoubleArrayList(x));
                task.eval(newSolution);

                if (task.problem.isFirstBetter(newSolution, pop.get(i))) {
                    pop.set(i, newSolution);
                    if (task.problem.isFirstBetter(newSolution, best)) best = newSolution;
                }
            }
        }
        return best;
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }
}
