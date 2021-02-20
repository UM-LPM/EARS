package org.um.feri.ears.benchmark;

import org.jfree.ui.RefineryUtilities;
import org.um.feri.ears.algorithms.AlgorithmBase;
import org.um.feri.ears.graphing.recording.GraphDataRecorder;
import org.um.feri.ears.problems.EnumStopCriterion;
import org.um.feri.ears.problems.SolutionBase;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.TaskBase;
import org.um.feri.ears.problems.results.BankOfResults;
import org.um.feri.ears.rating.Player;
import org.um.feri.ears.rating.ResultArena;
import org.um.feri.ears.visualization.rating.RatingIntervalPlot;

import java.util.*;
import java.util.concurrent.*;

public abstract class RatingBenchmarkBase<T extends TaskBase<?>, S extends SolutionBase<?>, A extends AlgorithmBase<T, S>> {
    public static boolean printInfo = false;
    protected ArrayList<T> tasks;
    protected ArrayList<A> algorithms;
    protected String name;
    protected String shortName;
    protected String info;

    // Default benchmark settings
    protected EnumStopCriterion stopCriterion = EnumStopCriterion.EVALUATIONS;
    protected int maxEvaluations = 1500;
    protected long timeLimit = TimeUnit.MILLISECONDS.toNanos(500); //milliseconds
    protected int maxIterations = 500;
    public double drawLimit = 1e-7;
    protected int dimension = 2;
    protected int numberOfRuns = 15;
    protected boolean runInParallel = false;
    protected boolean displayRatingIntervalChart = true;

    ResultArena resultArena = new ResultArena();
    ArrayList<HashMap<String, ArrayList<AlgorithmRunResult<S, A, T>>>> benchmarkResults;
    protected EnumMap<EnumBenchmarkInfoParameters, String> parameters; //add all specific parameters

    public RatingBenchmarkBase() {
        tasks = new ArrayList<>();
        algorithms = new ArrayList<>();
        parameters = new EnumMap<>(EnumBenchmarkInfoParameters.class);
    }

    public boolean isRunInParallel() {
        return runInParallel;
    }

    public void setRunInParallel(boolean runInParallel) {
        this.runInParallel = runInParallel;
    }

    public ResultArena getResultArena() {
        return resultArena;
    }

    public void addParameter(EnumBenchmarkInfoParameters id, String value) {
        parameters.put(id, value);
    }

    public EnumMap<EnumBenchmarkInfoParameters, String> getParameters() {
        return parameters;
    }

    public void clearPlayers() {
        algorithms.clear();
        benchmarkResults.clear();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (T tw : tasks) {
            sb.append(tw.toString());
        }
        return sb.toString();
    }

    public ArrayList<T> getAllTasks() {
        return new ArrayList<T>(tasks);
    }

    public int getNumberOfRuns() {
        return numberOfRuns;
    }

    public EnumStopCriterion getStopCriterion() {
        return stopCriterion;
    }

    /**
     * Remove algorithm from benchmark
     *
     * @param algorithm to be removed from the benchmark
     */
    public void unregisterAlgorithm(AlgorithmBase<T, S> algorithm) {
        algorithms.remove(algorithm);
        benchmarkResults.remove(algorithm);
    }

    public String getParams() {
        StringBuilder sb = new StringBuilder();
        sb.append("Parameters:\n");
        for (EnumBenchmarkInfoParameters a : parameters.keySet()) {
            sb.append(a.getShortName()).append(" = ").append(parameters.get(a)).append("\t").append("(").append(a.getDescription()).append(")\n");
        }
        return sb.toString();
    }

    public void updateParameters() {
        parameters.put(EnumBenchmarkInfoParameters.NUMBER_OF_TASKS, "" + tasks.size());
        addParameter(EnumBenchmarkInfoParameters.STOPPING_CRITERIA, "" + stopCriterion);
        addParameter(EnumBenchmarkInfoParameters.DIMENSION, "" + dimension);
        addParameter(EnumBenchmarkInfoParameters.EVAL, String.valueOf(maxEvaluations));
        addParameter(EnumBenchmarkInfoParameters.CPU_TIME, String.valueOf(timeLimit));
        addParameter(EnumBenchmarkInfoParameters.ITTERATIONS, String.valueOf(maxIterations));
        addParameter(EnumBenchmarkInfoParameters.DRAW_PARAM, "abs(evaluation_diff) < " + drawLimit);
    }

    public boolean isDisplayRatingIntervalChart() {
        return displayRatingIntervalChart;
    }

    public void setDisplayRatingIntervalChart(boolean displayRatingIntervalChart) {
        this.displayRatingIntervalChart = displayRatingIntervalChart;
    }

    protected abstract void initFullProblemList();

    public void addAlgorithm(A al) {
        algorithms.add(al);
    }

    public void addAlgorithms(ArrayList<A> algorithms) {
        this.algorithms.addAll(algorithms);
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName.isEmpty() ? name : shortName;
    }

    public String getInfo() {
        return info;
    }

    /**
     * Run the benchmark
     *
     * @param allSingleProblemRunResults
     * @param numberOfRuns
     */
    public void run(BankOfResults allSingleProblemRunResults, int numberOfRuns) {
        this.numberOfRuns = numberOfRuns;
        initFullProblemList();
        benchmarkResults = new ArrayList<>(numberOfRuns);
        for (int i = 0; i < numberOfRuns; i++) {
            benchmarkResults.add(new HashMap<>());
        }

        addParameter(EnumBenchmarkInfoParameters.NUMBER_OF_DUELS, "" + numberOfRuns);
        long start = System.nanoTime();
        for (int i = 0; i < numberOfRuns; i++) {
            if (printInfo) System.out.println("Current run: " + (i + 1));
            for (T task : tasks) {
                if (printInfo) System.out.println("Current problem: " + task.getProblemName());
                ArrayList<AlgorithmRunResult<S, A, T>> runResults = runOneTask(task, allSingleProblemRunResults);
                benchmarkResults.get(i).put(task.getFileNameString(), runResults);
            }
        }

        addParameter(EnumBenchmarkInfoParameters.BENCHMARK_RUN_DURATION,
                "" + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start));

        performStatistics();
    }

    /**
     * @param task
     * @param allSingleProblemRunResults
     * @return
     */
    protected ArrayList<AlgorithmRunResult<S, A, T>> runOneTask(T task, BankOfResults allSingleProblemRunResults) {

        ArrayList<AlgorithmRunResult<S, A, T>> runResults = new ArrayList<>();
        if (runInParallel) {
            ExecutorService pool = Executors.newFixedThreadPool(algorithms.size());
            Set<Future<AlgorithmRunResult>> set = new HashSet<>();
            for (A al : algorithms) {
                Future<AlgorithmRunResult> future = pool.submit(al.createRunnable(al, (T) task.clone()));
                set.add(future);
            }

            for (Future<AlgorithmRunResult> future : set) {
                try {
                    AlgorithmRunResult result = future.get();

                    if (printInfo)
                        System.out.println("Total execution time for " + result.algorithm.getID() + ": " + result.algorithm.getLastRunDuration());

                    //TODO generic feasibility check for result
                    runResults.add(result);
                    allSingleProblemRunResults.add(result.task, result.solution, result.algorithm);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            pool.shutdown();
        } else {
            long start;
            long duration;
            for (A algorithm : algorithms) {
                try {
                    GraphDataRecorder.SetContext(algorithm, task);
                    T taskCopy = (T) task.clone();
                    start = System.nanoTime();
                    S result = algorithm.execute(taskCopy); // this can be done parallel
                    duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
                    algorithm.addRunDuration(duration, duration - taskCopy.getEvaluationTimeMs());

                    if (printInfo)
                        System.out.println(algorithm.getID() + ": " + duration / 1000.0);
                    runResults.add(new AlgorithmRunResult(result, algorithm, taskCopy));
                    allSingleProblemRunResults.add(taskCopy, result, algorithm);
                    //TODO generic feasibility check
                } catch (StopCriterionException e) {
                    System.err.println(algorithm.getID() + " StopCriterionException for:" + task + "\n" + e);
                }
            }
        }
        return runResults;
    }

    private void performStatistics() {

        for (A algorithm : algorithms) {
            resultArena.addPlayer(algorithm, algorithm.getID());
        }
        performTournament();
        resultArena.calculateRatings();

        //variable display statistics
        //other statistical methods
        //package statistics


        for (Player p : resultArena.getPlayers()) System.out.println(p); //print ratings

        if (displayRatingIntervalChart) {
            displayRatingIntervalsChart(resultArena.getPlayers());
        }
    }

    protected abstract void performTournament();

    public void displayRatingIntervalsChart(ArrayList<Player> list) {
        RatingIntervalPlot plot = new RatingIntervalPlot("Rating Interval", list);
        plot.pack();
        RefineryUtilities.centerFrameOnScreen(plot);
        plot.setVisible(true);
    }

    public void allPlayed() {
        for (AlgorithmBase al : algorithms) {
            al.setPlayed(true);
        }
    }

    public String getStopCondition() {
        switch (stopCriterion) {
            case EVALUATIONS:
                return Integer.toString(maxEvaluations);
            case ITERATIONS:
                return Integer.toString(maxIterations);
            case CPU_TIME:
                return Long.toString(timeLimit);
            case STAGNATION:
                return Integer.toString(tasks.get(0).getMaxTrialsBeforeStagnation()); //TODO stagnation trials
            case GLOBAL_OPTIMUM_OR_EVALUATIONS:
                return Integer.toString(maxEvaluations);
            default:
                return null;
        }
    }

    public String[] getProblems() {
        String[] problems = new String[tasks.size()];
        for (int i = 0; i < tasks.size(); i++) {
            problems[i] = tasks.get(i).getProblemName();
        }
        return problems;
    }
}
