package org.um.feri.ears.benchmark;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.Solution;
import org.um.feri.ears.problems.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * Class for storing benchmark results in different data structures/formats
 */
public class BenchmarkResults<R extends Solution, S extends Solution, P extends Problem<S>, A extends Algorithm<R, S, P>> {

    ArrayList<HashMap<Task, ArrayList<AlgorithmRunResult<R, S, P, A>>>> runTaskAlgorithm; //run - task - results from all algorithms for the task
    Hashtable<A, Hashtable<Task, ArrayList<AlgorithmRunResult<R, S, P, A>>>> algorithmTaskRun; // algorithm - task - task results for all runs

    ArrayList<HashMap<Task, ArrayList<AlgorithmRunResult<R, S, P, A>>>> getResultsByRun() {
        return runTaskAlgorithm;
    }

    public Hashtable<A, Hashtable<Task, ArrayList<AlgorithmRunResult<R, S, P, A>>>> getResultsByAlgorithm() {
        return algorithmTaskRun;
    }

    public BenchmarkResults() {
        runTaskAlgorithm = new ArrayList<>();
        algorithmTaskRun = new Hashtable<>();
    }

    public void addResults(int runNumber, Task task, ArrayList<AlgorithmRunResult<R, S, P, A>> runResults) {

        while (runTaskAlgorithm.size() < runNumber + 1) {
            runTaskAlgorithm.add(new HashMap<>());
        }

        runTaskAlgorithm.get(runNumber).put(task, runResults);

        for (AlgorithmRunResult<R, S, P, A> res : runResults) {

            Hashtable<Task, ArrayList<AlgorithmRunResult<R, S, P, A>>> algorithmsHs = algorithmTaskRun.computeIfAbsent(res.algorithm, k -> new Hashtable<>());

            ArrayList<AlgorithmRunResult<R, S, P, A>> tasksHs = algorithmsHs.computeIfAbsent(task, k -> new ArrayList<>());
            tasksHs.add(res);
        }
    }

    public void addResult(int runNumber, Task task, AlgorithmRunResult<R, S, P, A> runResult) {

        while (runTaskAlgorithm.size() < runNumber + 1) {
            runTaskAlgorithm.add(new HashMap<>());
        }
        //runTaskAlgorithm.get(runNumber).put(task, runResult);
        ArrayList<AlgorithmRunResult<R, S, P, A>> resultArray = runTaskAlgorithm.get(runNumber).computeIfAbsent(task, k -> new ArrayList<>());
        resultArray.add(runResult);

        Hashtable<Task, ArrayList<AlgorithmRunResult<R, S, P, A>>> algorithmsHs = algorithmTaskRun.computeIfAbsent(runResult.algorithm, k -> new Hashtable<>());
        ArrayList<AlgorithmRunResult<R, S, P, A>> tasksHs = algorithmsHs.computeIfAbsent(task, k -> new ArrayList<>());
        tasksHs.add(runResult);
    }

    public void clear() {
        runTaskAlgorithm.clear();
        algorithmTaskRun.clear();
    }

    public void removeAlgorithm(Algorithm<R, S, P> algorithm) {
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
