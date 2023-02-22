package org.um.feri.ears.util.comparator;

import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.benchmark.AlgorithmRunResult;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.quality_indicator.QualityIndicator;

import java.util.Comparator;

public class QualityIndicatorComparator<N extends Number, T extends TaskBase<P>, S extends Solution, P extends NumberProblem<N>> implements Comparator<AlgorithmRunResult<ParetoSolution<N>, MOAlgorithm<T, N>, T>> {
    P problem;
    QualityIndicator<N> qi;

    public QualityIndicatorComparator(P problem, QualityIndicator<N> qi) {
        this.problem = problem;
        this.qi = qi;
    }

    @Override
    public int compare(AlgorithmRunResult<ParetoSolution<N>, MOAlgorithm<T, N>, T> r1, AlgorithmRunResult<ParetoSolution<N>, MOAlgorithm<T, N>, T> r2) {
        if (r1.solution != null) {
            if (r2.solution != null) {
                // if (resultEqual(r1.getBest(), r2.getBest())) return 0; Normal sor later!
                if (qi.getIndicatorType() == QualityIndicator.IndicatorType.UNARY) {
                    try {
                        r1.solution.evaluate(qi);
                        r2.solution.evaluate(qi);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    if (problem.isFirstBetter(r1.solution, r2.solution, qi)) return -1;
                    else return 1;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else return -1; //second is null
        } else if (r2.solution != null) return 1; //first null
        return 0; //both equal
    }
}
