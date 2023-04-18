package org.um.feri.ears.operators;

import java.util.Comparator;
import java.util.List;

import org.um.feri.ears.problems.*;
import org.um.feri.ears.util.SolutionListUtils;

public class TournamentSelection<Source extends Solution, P extends Problem> extends Selection<Source, P>{

    private final int numberOfTournaments;
    private final Comparator<Source> comparator;


    public TournamentSelection(int numberOfTournaments, Comparator<Source> comparator) {
        super();
        this.numberOfTournaments = numberOfTournaments;
        this.comparator = comparator;
    }

    @Override
    public Source execute(List<Source> source, P problem) {

        Source result;
        if (source.size() == 1) {
            result = source.get(0);
        } else {
            result = SolutionListUtils.selectNRandomDifferentSolutions(1, source).get(0);
            int count = 1; // at least 2 solutions are compared
            do {
                Source candidate = SolutionListUtils.selectNRandomDifferentSolutions(1, source).get(0);
                result = SolutionListUtils.getBestSolution(result, candidate, comparator);
            } while (++count < this.numberOfTournaments);
        }
        return result;
    }
}
