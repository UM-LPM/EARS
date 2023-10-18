package org.um.feri.ears.problems;

import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public abstract class CombinatorialProblem extends NumberProblem<Integer> {

    public CombinatorialProblem(String name, int numberOfDimensions, int numberOfGlobalOptima, int numberOfObjectives, int numberOfConstraints) {
        super(name, numberOfDimensions, numberOfGlobalOptima, numberOfObjectives, numberOfConstraints);
    }

    @Override
    public NumberSolution<Integer> getRandomSolution() {

        List<Integer> var = new ArrayList<>(numberOfDimensions);

        for (int j = 0; j < numberOfDimensions; j++) {
            var.add(j);
        }

        RNG.shuffle(var);

        NumberSolution<Integer> sol = new NumberSolution<>(numberOfObjectives, var);
        evaluate(sol);
        if(numberOfConstraints > 0)
            evaluateConstraints(sol);
        return sol;
    }

    @Override
    public void makeFeasible(NumberSolution<Integer> solution) {

    }

    @Override
    public boolean isFeasible(NumberSolution<Integer> solution) {
        ArrayList<Integer> var = solution.getVariables();

        HashSet<Integer> set = new HashSet<>();
        for (int i = 0; i < numberOfDimensions; i++) {
            if (!var.contains(i)) {
                return false;
            }
            if (set.contains(i)) {
                return false;
            }
            set.add(i);
        }
        return true;
    }
}
