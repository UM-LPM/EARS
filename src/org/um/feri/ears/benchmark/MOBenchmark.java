package org.um.feri.ears.benchmark;

import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.MOTask;
import org.um.feri.ears.problems.moo.MOProblemBase;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.qualityIndicator.IndicatorFactory;
import org.um.feri.ears.qualityIndicator.QualityIndicator;
import org.um.feri.ears.qualityIndicator.QualityIndicator.IndicatorName;
import org.um.feri.ears.qualityIndicator.QualityIndicator.IndicatorType;
import org.um.feri.ears.rating.Game;
import org.um.feri.ears.util.Comparator.QualityIndicatorComparator;
import org.um.feri.ears.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class MOBenchmark<T extends Number, Task extends MOTask<T, P>, P extends MOProblemBase<T>> extends BenchmarkBase<Task, ParetoSolution<T>, MOAlgorithm<Task, T>> {

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

    public boolean resultEqual(ParetoSolution<T> a, ParetoSolution<T> b, QualityIndicator<T> qi) {
        if ((a == null) && (b == null)) return true;
        if (a == null) return false;
        if (b == null) return false;
        if (qi.getIndicatorType() == IndicatorType.UNARY)
            return a.isEqual(b, drawLimit); //TODO Quality indicator get eps instead of draw limit
        else if (qi.getIndicatorType() == IndicatorType.BINARY) {
            return qi.compare(a, b, drawLimit) == 0;
        }
        return false;
    }

    protected abstract void addTask(StopCriterion stopCriterion, int maxEvaluations, long allowedTime, int maxIterations, double epsilon, P problem);

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
    protected void performTournament() {

        for (HashMap<Task, ArrayList<AlgorithmRunResult<ParetoSolution<T>, MOAlgorithm<Task, T>, Task>>> problemMap : benchmarkResults.getResultsByRun()) {
            for (ArrayList<AlgorithmRunResult<ParetoSolution<T>, MOAlgorithm<Task, T>, Task>> results : problemMap.values()) {
                Task t = results.get(0).task;
                AlgorithmRunResult<ParetoSolution<T>, MOAlgorithm<Task, T>, Task> first;
                AlgorithmRunResult<ParetoSolution<T>, MOAlgorithm<Task, T>, Task> second;
                QualityIndicator<T> qi;

                if (randomIndicator) {
                    IndicatorName indicatorName;
                    for (int i = 0; i < results.size(); i++) {
                        first = results.get(i);
                        for (int j = i + 1; j < results.size(); j++) {
                            second = results.get(j);
                            indicatorName = getRandomIndicator();
                            qi = IndicatorFactory.createIndicator(indicatorName, t.getNumberOfObjectives(), t.getProblemFileName());

                            try {
                                if (qi.getIndicatorType() == IndicatorType.UNARY) {
                                    first.solution.evaluate(qi);
                                    second.solution.evaluate(qi);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (resultEqual(first.solution, second.solution, qi)) {
                                resultArena.addGameResult(Game.DRAW, first.algorithm.getID(), second.algorithm.getID(), t.getProblemName(), indicatorName.toString());
                            } else if (t.isFirstBetter(first.solution, second.solution, qi)) {
                                resultArena.addGameResult(Game.WIN, first.algorithm.getID(), second.algorithm.getID(), t.getProblemName(), indicatorName.toString());
                            } else {
                                resultArena.addGameResult(Game.WIN, second.algorithm.getID(), first.algorithm.getID(), t.getProblemName(), indicatorName.toString());
                            }
                        }
                    }
                } else {
                    for (IndicatorName indicatorName : indicators) {
                        QualityIndicatorComparator<T, Task, P> qic;
                        qi = IndicatorFactory.<T>createIndicator(indicatorName, t.getNumberOfObjectives(), t.getProblemFileName());
                        qic = new QualityIndicatorComparator<T, Task, P>(t, qi);
                        results.sort(qic); //best first
                        for (int i = 0; i < results.size() - 1; i++) {
                            first = results.get(i);
                            for (int j = i + 1; j < results.size(); j++) {
                                second = results.get(j);
                                if (resultEqual(first.solution, second.solution, qi)) {
                                    resultArena.addGameResult(Game.DRAW, first.algorithm.getID(), second.algorithm.getID(), t.getProblemName(), indicatorName.toString());
                                } else {
                                    if (first.solution == null) {
                                        System.out.println(first.algorithm.getID() + " NULL");
                                    }
                                    if (second.solution == null) {
                                        System.out.println(second.algorithm.getID() + " NULL");
                                    }
                                    resultArena.addGameResult(Game.WIN, first.algorithm.getID(), second.algorithm.getID(), t.getProblemName(), indicatorName.toString());
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
