package org.um.feri.ears.operators;

import java.util.Comparator;
import java.util.List;

import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.SolutionListUtils;


public class TournamentSelection implements SelectionOperator<DoubleSolution, List<DoubleSolution>, Task> {

    private int numberOfTournaments;
    private Comparator<DoubleSolution> comparator;


    public TournamentSelection(int numberOfTournaments, Comparator<DoubleSolution> comparator) {
        super();
        this.numberOfTournaments = numberOfTournaments;
        this.comparator = comparator;
    }

    @Override
    public DoubleSolution execute(List<DoubleSolution> source, Task tb) {

        DoubleSolution result;
        if (source.size() == 1) {
            result = source.get(0);
        } else {
            result = SolutionListUtils.selectNRandomDifferentSolutions(1, source).get(0);
            int count = 1; // at least 2 solutions are compared
            do {
                DoubleSolution candidate = SolutionListUtils.selectNRandomDifferentSolutions(1, source).get(0);
                result = SolutionListUtils.getBestSolution(result, candidate, comparator);
            } while (++count < this.numberOfTournaments);
        }

        return result;
    }
}
