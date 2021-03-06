package org.um.feri.ears.benchmark;

import org.um.feri.ears.algorithms.AlgorithmBase;
import org.um.feri.ears.problems.SolutionBase;
import org.um.feri.ears.problems.TaskBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * Class for storing benchmark results in different data structures/formats
 */
public class BenchmarkResults<T extends TaskBase<?>, S extends SolutionBase<?>, A extends AlgorithmBase<T, S>> {

    ArrayList<HashMap<T, ArrayList<AlgorithmRunResult<S, A, T>>>> runTaskAlgorithm; //run - task - results from all algorithms for the task
    Hashtable<A, Hashtable<T, ArrayList<AlgorithmRunResult<S, A, T>>>> algorithmTaskRun; // algorithm - task - task results for all runs

    ArrayList<HashMap<T, ArrayList<AlgorithmRunResult<S, A, T>>>> getResultsByRun() {
        return runTaskAlgorithm;
    }

    public Hashtable<A, Hashtable<T, ArrayList<AlgorithmRunResult<S, A, T>>>> getResultsByAlgorithm() {
        return algorithmTaskRun;
    }

    public BenchmarkResults() {
        runTaskAlgorithm = new ArrayList<>();
        algorithmTaskRun = new Hashtable<>();
    }

    public void addResults(int runNumber, T task, ArrayList<AlgorithmRunResult<S, A, T>> runResults) {

        while (runTaskAlgorithm.size() < runNumber + 1) {
            runTaskAlgorithm.add(new HashMap<>());
        }

        runTaskAlgorithm.get(runNumber).put(task, runResults);

        for (AlgorithmRunResult<S, A, T> res : runResults) {

            Hashtable<T, ArrayList<AlgorithmRunResult<S, A, T>>> algorithmsHs = algorithmTaskRun.computeIfAbsent(res.algorithm, k -> new Hashtable<>());

            ArrayList<AlgorithmRunResult<S, A, T>> tasksHs = algorithmsHs.computeIfAbsent(task, k -> new ArrayList<>());
            tasksHs.add(res);
        }
    }

    public void addResult(int runNumber, T task, AlgorithmRunResult<S, A, T> runResult) {

        while (runTaskAlgorithm.size() < runNumber + 1) {
            runTaskAlgorithm.add(new HashMap<>());
        }
        //runTaskAlgorithm.get(runNumber).put(task, runResult);
        ArrayList<AlgorithmRunResult<S, A, T>> resultArray = runTaskAlgorithm.get(runNumber).computeIfAbsent(task, k -> new ArrayList<>());
        resultArray.add(runResult);

        Hashtable<T, ArrayList<AlgorithmRunResult<S, A, T>>> algorithmsHs = algorithmTaskRun.computeIfAbsent(runResult.algorithm, k -> new Hashtable<>());
        ArrayList<AlgorithmRunResult<S, A, T>> tasksHs = algorithmsHs.computeIfAbsent(task, k -> new ArrayList<>());
        tasksHs.add(runResult);
    }

    public void clear() {
        runTaskAlgorithm.clear();
        algorithmTaskRun.clear();
    }

    public void removeAlgorithm(AlgorithmBase<T, S> algorithm) {
        //TODO
        /*for (HashMap<T, ArrayList<AlgorithmRunResult<S, A, T>>> run : runTaskAlgorithm) {
            for(ArrayList<AlgorithmRunResult<S, A, T>> results : run.values()) {
                for (AlgorithmRunResult<S, A, T> res: results) {
                    if(res.algorithm == algorithm)
                }
            }
        }*/

        algorithmTaskRun.remove(algorithm);
    }
}
