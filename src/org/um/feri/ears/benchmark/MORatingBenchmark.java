package org.um.feri.ears.benchmark;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.graphing.recording.GraphDataRecorder;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.problems.moo.MOProblemBase;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.problems.results.BankOfResults;
import org.um.feri.ears.qualityIndicator.IndicatorFactory;
import org.um.feri.ears.qualityIndicator.QualityIndicator;
import org.um.feri.ears.qualityIndicator.QualityIndicator.IndicatorName;
import org.um.feri.ears.qualityIndicator.QualityIndicator.IndicatorType;
import org.um.feri.ears.rating.Game;
import org.um.feri.ears.rating.ResultArena;
import org.um.feri.ears.util.Cache;
import org.um.feri.ears.util.Util;

public abstract class MORatingBenchmark<T extends Number, Task extends MOTask<T, P>, P extends MOProblemBase<T>> extends RatingBenchmarkBase<Task,ParetoSolution<T>, MOAlgorithm<Task, T>> {

    protected List<IndicatorName> indicators;
    private double[] indicatorWeights;
    protected boolean runInParalel = false;


    /**
     * This indicator is only used when comparing two approximation sets
     */

    public MORatingBenchmark(List<IndicatorName> indicators) {
        super();
        this.indicators = indicators;
    }

    public MORatingBenchmark(ArrayList<IndicatorName> indicators, double[] weights) {
        super();
        this.indicators = indicators;
        indicatorWeights = weights;
    }

    public abstract boolean resultEqual(ParetoSolution<T> a, ParetoSolution<T> b, QualityIndicator<T> qi);

    protected abstract void registerTask(EnumStopCriterion sc, int eval, long allowedTime, int maxIterations, double epsilon, P p);

    @Override
    public void registerAlgorithm(MOAlgorithm<Task, T> al) {
        listOfAlgorithmsPlayers.add(al);
    }

    @Override
    public void registerAlgorithms(ArrayList<MOAlgorithm<Task, T>> algorithms) {
        listOfAlgorithmsPlayers.addAll(algorithms);
    }

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

    class FitnessComparator implements Comparator<AlgorithmRunResult<ParetoSolution<T>, MOAlgorithm<Task, T>, Task>> {
        MOTask<T, P> t;
        QualityIndicator<T> qi;

        public FitnessComparator(MOTask<T, P> t, QualityIndicator<T> qi) {
            this.t = t;
            this.qi = qi;
        }

        @Override
        public int compare(AlgorithmRunResult<ParetoSolution<T>, MOAlgorithm<Task, T>, Task> r1, AlgorithmRunResult<ParetoSolution<T>, MOAlgorithm<Task, T>, Task> r2) {
            if (r1.getSolution() != null) {
                if (r2.getSolution() != null) {
                    // if (resultEqual(r1.getBest(), r2.getBest())) return 0; Normal sor later!
                    if (qi.getIndicatorType() == IndicatorType.UNARY) {
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

    @Override
    protected void setWinLoseFromResultList(ResultArena arena, Task t) {

        for (IndicatorName indicatorName : indicators) {

            AlgorithmRunResult<ParetoSolution<T>, MOAlgorithm<Task, T>, Task> win;
            AlgorithmRunResult<ParetoSolution<T>, MOAlgorithm<Task, T>, Task> lose;
            FitnessComparator fc;
            QualityIndicator<T> qi = IndicatorFactory.<T>createIndicator(indicatorName, t.getNumberOfObjectives(), t.getProblemFileName());
            fc = new FitnessComparator(t, qi);
            results.sort(fc); //best first
            for (int i = 0; i < results.size() - 1; i++) {
                win = results.get(i);
                for (int j = i + 1; j < results.size(); j++) {
                    lose = results.get(j);
                    if (resultEqual(win.getSolution(), lose.getSolution(), qi)) {
                        arena.addGameResult(Game.DRAW, win.getAlgorithm().getID(), lose.getAlgorithm().getID(), t.getProblemName(), indicatorName.toString());
                    } else {
                        if (win.getAlgorithm() == null) {
                            System.out.println("NULL ID " + win.getClass().getName());
                        }
                        if (win.getSolution() == null) {
                            System.out.println(win.getAlgorithm().getID() + " NULL");
                        }
                        if (lose.getAlgorithm() == null) {
                            System.out.println("NULL ID " + lose.getClass().getName());
                        }
                        if (lose.getSolution() == null) {
                            System.out.println(lose.getAlgorithm().getID() + " NULL");
                        }
                        arena.addGameResult(Game.WIN, win.getAlgorithm().getID(), lose.getAlgorithm().getID(), t.getProblemName(), indicatorName.toString());
                    }

                }
            }
        }
    }

    /**
     * Run the benchmark
     *
     * @param arena needs to be filed with players and their ratings
     * @param allSingleProblemRunResults
     */
    @Override
    public void run(ResultArena arena, BankOfResults allSingleProblemRunResults) {
        duelNumber = numberOfRuns;
        parameters.put(EnumBenchmarkInfoParameters.NUMBER_OF_DUELS, "" + numberOfRuns);
        long start = System.nanoTime();
        for (Task t : listOfProblems) {
            System.out.println("Current problem: " + t.getProblemName());
            for (int i = 0; i < numberOfRuns; i++) {
                System.out.println("Current duel: " + (i + 1));
                runOneProblem(t, allSingleProblemRunResults);
                setWinLoseFromResultList(arena, t);
                results.clear();
            }
        }
        addParameter(EnumBenchmarkInfoParameters.BENCHMARK_RUN_DURATION, "" + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start));

        if (isCheating()) {
            System.out.println("The reset count does not match!");
        }
        // Recalculate ratings after tournament
        arena.calculateRatings();

        if (displayRatingIntervalChart) {
            displayRatingIntervalsChart(arena.getPlayers());
        }
    }

    @Override
    protected void runOneProblem(Task task, BankOfResults allSingleProblemRunResults) {

        long start = 0;
        long duration = 0;
        for (MOAlgorithm<Task, T> al : listOfAlgorithmsPlayers) {

            reset(task); //number of evaluations
            try {
                start = System.nanoTime();

                GraphDataRecorder.SetContext(al, task);

                ParetoSolution<T> bestByALg = al.execute(task); //check if result is fake!

                GraphDataRecorder.SetParetoSolution(bestByALg);

                duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
                if (printSingleRunDuration) {
                    System.out.println(al.getID() + ": " + (duration / 1000.0));
                }
                al.addRunDuration(duration, duration - task.getEvaluationTimeMs());

                reset(task); //for one eval!

                if (MOAlgorithm.getCaching() != Cache.NONE || task.areDimensionsInFeasibleInterval(bestByALg)) {

                    results.add(new AlgorithmRunResult(bestByALg, al, task));
                    allSingleProblemRunResults.add(task, bestByALg, al);
                } else {
                    System.err.println(al.getAlgorithmInfo().getAcronym() + " result " + bestByALg + " is out of intervals! For task:" + task.getProblemName());
                    results.add(new AlgorithmRunResult(null, al, task)); // this can be done parallel - asynchrony
                }
            } catch (StopCriterionException e) {
                System.err.println(al.getAlgorithmInfo().getAcronym() + " StopCriterionException for:" + task + "\n" + e);
                results.add(new AlgorithmRunResult(null, al, task));
            }
        }
    }
}
