package org.um.feri.ears.benchmark;

import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.quality_indicator.IndicatorFactory;
import org.um.feri.ears.quality_indicator.QualityIndicator;
import org.um.feri.ears.quality_indicator.QualityIndicator.IndicatorName;
import org.um.feri.ears.quality_indicator.QualityIndicator.IndicatorType;
import org.um.feri.ears.statistic.rating_system.GameResult;
import org.um.feri.ears.util.comparator.QualityIndicatorComparator;
import org.um.feri.ears.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class MOBenchmark<N extends Number, S extends Solution, P extends NumberProblem<N>, T extends TaskBase<P>> extends BenchmarkBase<T, ParetoSolution<N>, MOAlgorithm<T, N>> {

    protected List<IndicatorName> indicators;
    private double[] indicatorWeights;
    protected boolean randomIndicator;

    public MOBenchmark(List<IndicatorName> indicators) {
        super();
        this.indicators = indicators;
    }

    public MOBenchmark(ArrayList<IndicatorName> indicators, double[] weights) {
        super();
        this.indicators = indicators;
        indicatorWeights = weights;
    }

    public boolean isRandomIndicator() {
        return randomIndicator;
    }

    public void setRandomIndicator(boolean randomIndicator) {
        this.randomIndicator = randomIndicator;
    }

    public boolean resultEqual(ParetoSolution<N> a, ParetoSolution<N> b, QualityIndicator<N> qi) {
        if ((a == null) && (b == null)) return true;
        if (a == null) return false;
        if (b == null) return false;
        if (qi.getIndicatorType() == IndicatorType.UNARY)
            return qi.isEqual(a, b, drawLimit);
        else if (qi.getIndicatorType() == IndicatorType.BINARY) {
            return qi.compare(a, b, drawLimit) == 0;
        }
        return false;
    }

    protected abstract void addTask(StopCriterion stopCriterion, int maxEvaluations, long allowedTime, int maxIterations, P problem);

    protected IndicatorName getRandomIndicator() {
        if (indicatorWeights != null) {
            double rand = Util.rnd.nextDouble();
            for (int i = 0; i < indicatorWeights.length; i++) {
                if (rand < indicatorWeights[i])
                    return indicators.get(i);
            }
        }
        return indicators.get(Util.nextInt(indicators.size()));
    }

    @Override
    protected void performTournament(int evaluationNumber) {

        for (HashMap<T, ArrayList<AlgorithmRunResult<ParetoSolution<N>, MOAlgorithm<T, N>, T>>> problemMap : benchmarkResults.getResultsByRun()) {
            for (ArrayList<AlgorithmRunResult<ParetoSolution<N>, MOAlgorithm<T, N>, T>> results : problemMap.values()) {
                T t = results.get(0).task;
                AlgorithmRunResult<ParetoSolution<N>, MOAlgorithm<T, N>, T> first;
                AlgorithmRunResult<ParetoSolution<N>, MOAlgorithm<T, N>, T> second;
                QualityIndicator<N> qi;

                if (randomIndicator) {
                    IndicatorName indicatorName;
                    for (int i = 0; i < results.size(); i++) {
                        first = results.get(i);
                        for (int j = i + 1; j < results.size(); j++) {
                            second = results.get(j);
                            indicatorName = getRandomIndicator();
                            qi = IndicatorFactory.createIndicator(indicatorName, t.problem.getNumberOfObjectives(), t.problem.getReferenceSetFileName());

                            try {
                                if (qi.getIndicatorType() == IndicatorType.UNARY) {
                                    first.solution.evaluate(qi);
                                    second.solution.evaluate(qi);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (resultEqual(first.solution, second.solution, qi)) {
                                tournamentResults.addGameResult(GameResult.DRAW, first.algorithm.getId(), second.algorithm.getId(), t.getProblemName(), indicatorName.toString());
                            } else if (t.problem.isFirstBetter(first.solution, second.solution, qi)) {
                                tournamentResults.addGameResult(GameResult.WIN, first.algorithm.getId(), second.algorithm.getId(), t.getProblemName(), indicatorName.toString());
                            } else {
                                tournamentResults.addGameResult(GameResult.WIN, second.algorithm.getId(), first.algorithm.getId(), t.getProblemName(), indicatorName.toString());
                            }
                        }
                    }
                } else {
                    for (IndicatorName indicatorName : indicators) {
                        QualityIndicatorComparator<N, T, S, P> qic;
                        qi = IndicatorFactory.<N>createIndicator(indicatorName, t.problem.getNumberOfObjectives(), t.problem.getReferenceSetFileName());
                        qic = new QualityIndicatorComparator<>(t.problem, qi);
                        results.sort(qic); //best first
                        for (int i = 0; i < results.size() - 1; i++) {
                            first = results.get(i);
                            for (int j = i + 1; j < results.size(); j++) {
                                second = results.get(j);
                                if (resultEqual(first.solution, second.solution, qi)) {
                                    tournamentResults.addGameResult(GameResult.DRAW, first.algorithm.getId(), second.algorithm.getId(), t.getProblemName(), indicatorName.toString());
                                } else {
                                    if (first.solution == null) {
                                        System.out.println(first.algorithm.getId() + " NULL");
                                    }
                                    if (second.solution == null) {
                                        System.out.println(second.algorithm.getId() + " NULL");
                                    }
                                    tournamentResults.addGameResult(GameResult.WIN, first.algorithm.getId(), second.algorithm.getId(), t.getProblemName(), indicatorName.toString());
                                }
                            }
                        }
                    }
                }
            }
        }
        tournamentResults.calculateRatings();
    }
}
