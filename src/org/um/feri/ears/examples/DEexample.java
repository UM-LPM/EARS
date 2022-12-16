package org.um.feri.ears.examples;

import java.util.ArrayList;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

public class DEexample extends Algorithm {
    int popSize;
    double CR, F;
    ArrayList<DoubleSolution> pop;
    DoubleSolution best;

    //Initialize all agents {\displaystyle \mathbf {x} } \mathbf {x}  with random positions in the search-space.
    public DEexample(int ps, double CR, double F) {
        popSize = ps;
        this.CR = CR;
        this.F = F;
        ai = new AlgorithmInfo("DE", " Deferential Evolution", "");
        au = new Author("author name", "author email");

    }

    public void init(Task taskProblem) throws StopCriterionException {
        pop = new ArrayList<>();
        DoubleSolution tmp;
        for (int i = 0; i < popSize; i++) {
            if (taskProblem.isStopCriterion()) break;
            tmp = taskProblem.getRandomEvaluatedSolution();
            if (i == 0) best = tmp;
            else if (taskProblem.isFirstBetter(tmp, best)) best = tmp;
            pop.add(tmp);
        }
    }

    @Override
    public DoubleSolution execute(Task taskProblem) throws StopCriterionException {
        init(taskProblem);
        int a, b, c, R;
        DoubleSolution newSolution;
        while (!taskProblem.isStopCriterion()) {
            for (int i = 0; i < popSize; i++) {
                if (taskProblem.isStopCriterion()) break;
                do
                    a = Util.rnd.nextInt(popSize);
                while (a == i);
                do
                    b = Util.rnd.nextInt(popSize);
                while ((b == i) || (b == a));
                do
                    c = Util.rnd.nextInt(popSize);
                while ((c == i) || (c == a) || (c == b));
                R = Util.rnd.nextInt(taskProblem.getNumberOfDimensions());
                double[] x = new double[taskProblem.getNumberOfDimensions()];
                for (int j = 0; j < taskProblem.getNumberOfDimensions(); j++) {
                    if ((Util.nextDouble() < CR) || (j == R)) {
                        x[j] = taskProblem.setFeasible(pop.get(a).getValue(j) + F * (pop.get(b).getValue(j) - pop.get(c).getValue(j)), j);
                    } else x[j] = pop.get(i).getValue(j);
                }
                newSolution = taskProblem.eval(x);
                if (taskProblem.isFirstBetter(newSolution, pop.get(i))) {
                    pop.set(i, newSolution);
                    if (taskProblem.isFirstBetter(newSolution, best)) best = newSolution;
                }
            }
        }
        return best;
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }
}
