package org.um.feri.ears.operators;

import java.util.Comparator;
import java.util.List;

import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.SolutionListUtils;


public class TournamentSelection implements SelectionOperator<NumberSolution<Double>, List<NumberSolution<Double>>, Task> {

    private int numberOfTournaments;
    private Comparator<NumberSolution<Double>> comparator;


    public TournamentSelection(int numberOfTournaments, Comparator<NumberSolution<Double>> comparator) {
        super();
        this.numberOfTournaments = numberOfTournaments;
        this.comparator = comparator;
    }

    @Override
    public NumberSolution<Double> execute(List<NumberSolution<Double>> source, Task tb) {

        NumberSolution<Double> result;
        if (source.size() == 1) {
            result = source.get(0);
        } else {
            result = SolutionListUtils.selectNRandomDifferentSolutions(1, source).get(0);
            int count = 1; // at least 2 solutions are compared
            do {
                NumberSolution<Double> candidate = SolutionListUtils.selectNRandomDifferentSolutions(1, source).get(0);
                result = SolutionListUtils.getBestSolution(result, candidate, comparator);
            } while (++count < this.numberOfTournaments);
        }

        return result;
    }
}
