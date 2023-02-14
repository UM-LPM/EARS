package org.um.feri.ears.problems;

import org.um.feri.ears.util.Util;

import java.util.ArrayList;
import java.util.List;

public abstract class CombinatorialProblem extends NumberProblem<Integer> {

    public CombinatorialProblem(String name, int numberOfDimensions, int numberOfGlobalOptima, int numberOfObjectives, int numberOfConstraints) {
        super(name, numberOfDimensions, numberOfGlobalOptima, numberOfObjectives, numberOfConstraints);
    }

    @Override
    public NumberSolution<Integer> getRandomSolution() {

        List<Integer> var = new ArrayList<Integer>(numberOfDimensions);

        for (int j = 0; j < numberOfDimensions; j++) {
            var.add(j);
        }

        Util.shuffle(var);

        NumberSolution<Integer> sol = new NumberSolution<>(numberOfObjectives, var);
        evaluate(sol);
        evaluateConstraints(sol);
        return sol;
    }

    @Override
    public void makeFeasible(NumberSolution<Integer> solution) {

    }

    @Override
    public boolean isFeasible(NumberSolution<Integer> solution) {
        //TODO check if each number appears once
        return false;
    }

    @Override
    public boolean isFirstBetter(NumberSolution<Integer> solution1, NumberSolution<Integer> solution2) {
        //TODO replace with comparator
        return false;
    }
}
