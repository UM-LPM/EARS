package org.um.feri.ears.benchmark;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.rating.Game;
import org.um.feri.ears.util.Comparator.AlgorithmResultComparator;
import org.um.feri.ears.util.Util;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Benchmark extends BenchmarkBase<Task, DoubleSolution, Algorithm> {

    protected abstract void addTask(Problem p, StopCriterion sc, int eval, long time, int maxIterations, double epsilon);

    @Override
    protected void performTournament() {

        for (HashMap<Task, ArrayList<AlgorithmRunResult<DoubleSolution, Algorithm, Task>>> problemMap : benchmarkResults.getResultsByRun()) {
            for (ArrayList<AlgorithmRunResult<DoubleSolution, Algorithm, Task>> results : problemMap.values()) {
                Task t = results.get(0).task;

                AlgorithmRunResult<DoubleSolution, Algorithm, Task> win;
                AlgorithmRunResult<DoubleSolution, Algorithm, Task> lose;
                AlgorithmResultComparator rc = new AlgorithmResultComparator(t);
                results.sort(rc); // best first
                for (int i = 0; i < results.size() - 1; i++) {
                    win = results.get(i);
                    for (int j = i + 1; j < results.size(); j++) {
                        lose = results.get(j);
                        if (resultEqual(win, lose)) { // Special for this benchmark
                            if (printInfo)
                                System.out.println("draw of " + win.algorithm.getID() + " ("
                                        + Util.df3.format(win.solution.getEval()) + ", feasable=" + win.solution.areConstraintsMet()
                                        + ") against " + lose.algorithm.getID() + " (" + Util.df3.format(lose.solution.getEval())
                                        + ", feasable=" + lose.solution.areConstraintsMet() + ") for " + t.getProblemName());
                            resultArena.addGameResult(Game.DRAW, win.algorithm.getAlgorithmInfo().getAcronym(),
                                    lose.algorithm.getAlgorithmInfo().getAcronym(), t.getProblemName());
                        } else {
                            if (win.solution == null) {
                                System.out.println(win.algorithm.getID() + " NULL");
                            }
                            if (lose.solution == null) {
                                System.out.println(lose.algorithm.getID() + " NULL");
                            }
                            if (printInfo)
                                System.out.println("win of " + win.algorithm.getID() + " ("
                                        + Util.df3.format(win.solution.getEval()) + ", feasable=" + win.solution.areConstraintsMet()
                                        + ") against " + lose.algorithm.getID() + " (" + Util.df3.format(lose.solution.getEval())
                                        + ", feasable=" + lose.solution.areConstraintsMet() + ") for " + t.getProblemName());
                            resultArena.addGameResult(Game.WIN, win.algorithm.getAlgorithmInfo().getAcronym(),
                                    lose.algorithm.getAlgorithmInfo().getAcronym(), t.getProblemName());
                        }
                    }
                }
            }
        }
    }

    public boolean resultEqual(AlgorithmRunResult<DoubleSolution, Algorithm, Task> a, AlgorithmRunResult<DoubleSolution, Algorithm, Task> b) {
        if ((a == null) && (b == null))
            return true;
        if (a == null)
            return false;
        if (b == null)
            return false;
        if (!a.solution.areConstraintsMet() && b.solution.areConstraintsMet())
            return false;
        if (a.solution.areConstraintsMet() && !b.solution.areConstraintsMet())
            return false;
        if (!a.solution.areConstraintsMet() && !b.solution.areConstraintsMet())
            return true;

        boolean isDraw = Math.abs(a.solution.getEval() - b.solution.getEval()) < drawLimit;
        // if the results are equal in case of global optimum stop criterion then compare number of evaluations used
        if (isDraw && a.task.getStopCriterion() == StopCriterion.GLOBAL_OPTIMUM_OR_EVALUATIONS) {
            isDraw = a.task.getNumberOfEvaluations() == b.task.getNumberOfEvaluations();
        }

        return isDraw;
    }
}
