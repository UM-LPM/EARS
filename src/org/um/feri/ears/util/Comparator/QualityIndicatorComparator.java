package org.um.feri.ears.util.Comparator;

import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.benchmark.AlgorithmRunResult;
import org.um.feri.ears.problems.MOTask;
import org.um.feri.ears.problems.moo.MOProblemBase;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.qualityIndicator.QualityIndicator;

import java.util.Comparator;

public class QualityIndicatorComparator<T extends Number, Task extends MOTask<T, P>, P extends MOProblemBase<T>> implements Comparator<AlgorithmRunResult<ParetoSolution<T>, MOAlgorithm<Task, T>, Task>> {
    MOTask<T, P> t;
    QualityIndicator<T> qi;

    public QualityIndicatorComparator(MOTask<T, P> t, QualityIndicator<T> qi) {
        this.t = t;
        this.qi = qi;
    }

    @Override
    public int compare(AlgorithmRunResult<ParetoSolution<T>, MOAlgorithm<Task, T>, Task> r1, AlgorithmRunResult<ParetoSolution<T>, MOAlgorithm<Task, T>, Task> r2) {
        if (r1.getSolution() != null) {
            if (r2.getSolution() != null) {
                // if (resultEqual(r1.getBest(), r2.getBest())) return 0; Normal sor later!
                if (qi.getIndicatorType() == QualityIndicator.IndicatorType.UNARY) {
                    try {
                        r1.getSolution().evaluate(qi);
                        r2.getSolution().evaluate(qi);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    if (t.isFirstBetter(r1.getSolution(), r2.getSolution(), qi)) return -1;
                    else return 1;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else return -1; //second is null
        } else if (r2.getSolution() != null) return 1; //first null
        return 0; //both equal
    }
}
