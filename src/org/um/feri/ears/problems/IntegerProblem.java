package org.um.feri.ears.problems;

import org.um.feri.ears.util.Util;

import java.util.ArrayList;
import java.util.List;

public abstract class IntegerProblem extends NumberProblem<Integer> {

    public IntegerProblem(String name, int numberOfDimensions, int numberOfGlobalOptima, int numberOfObjectives, int numberOfConstraints) {
        super(name, numberOfDimensions, numberOfGlobalOptima, numberOfObjectives, numberOfConstraints);
    }

    @Override
    public NumberSolution<Integer> getRandomSolution() {

        List<Integer> var = new ArrayList<Integer>(numberOfDimensions);
        //Integer[] var = new Integer[numberOfDimensions];
        List<Integer> randomSequence = new ArrayList<>(numberOfDimensions);

        for (int j = 0; j < numberOfDimensions; j++) {
            randomSequence.add(j);
        }

        Util.shuffle(randomSequence);

        for (int i = 0; i < numberOfDimensions; i++) {
            //var[i]= randomSequence.get(i);
            var.add(randomSequence.get(i));
        }

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
        return false;
    }

    @Override
    public boolean isFirstBetter(NumberSolution<Integer> solution1, NumberSolution<Integer> solution2) {
        return false;
    }
}
