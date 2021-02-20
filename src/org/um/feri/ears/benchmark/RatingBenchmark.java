package org.um.feri.ears.benchmark;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.EnumStopCriterion;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.rating.Game;
import org.um.feri.ears.rating.ResultArena;
import org.um.feri.ears.util.Comparator.AlgorithmResultComparator;
import org.um.feri.ears.util.Util;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class RatingBenchmark extends RatingBenchmarkBase<Task, DoubleSolution, Algorithm> {

    protected abstract void registerTask(Problem p, EnumStopCriterion sc, int eval, long time, int maxIterations, double epsilon);

    @Override
    protected void performTournament() {

        for (HashMap<String, ArrayList<AlgorithmRunResult<DoubleSolution, Algorithm, Task>>> problemMap : benchmarkResults) {
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
                        if (resultEqual(win.solution, lose.solution)) { // Special for this benchmark
                            if (printInfo)
                                System.out.println("draw of " + win.getAlgorithm().getID() + " ("
                                        + Util.df3.format(win.getSolution().getEval()) + ", feasable=" + win.getSolution().areConstraintsMet()
                                        + ") against " + lose.getAlgorithm().getID() + " (" + Util.df3.format(lose.getSolution().getEval())
                                        + ", feasable=" + lose.getSolution().areConstraintsMet() + ") for " + t.getProblemName());
                            resultArena.addGameResult(Game.DRAW, win.getAlgorithm().getAlgorithmInfo().getAcronym(),
                                    lose.getAlgorithm().getAlgorithmInfo().getAcronym(), t.getProblemName());
                        } else {
                            if (win.getSolution() == null) {
                                System.out.println(win.getAlgorithm().getID() + " NULL");
                            }
                            if (lose.getSolution() == null) {
                                System.out.println(lose.getAlgorithm().getID() + " NULL");
                            }
                            if (printInfo)
                                System.out.println("win of " + win.getAlgorithm().getID() + " ("
                                        + Util.df3.format(win.getSolution().getEval()) + ", feasable=" + win.getSolution().areConstraintsMet()
                                        + ") against " + lose.getAlgorithm().getID() + " (" + Util.df3.format(lose.getSolution().getEval())
                                        + ", feasable=" + lose.getSolution().areConstraintsMet() + ") for " + t.getProblemName());
                            resultArena.addGameResult(Game.WIN, win.getAlgorithm().getAlgorithmInfo().getAcronym(),
                                    lose.getAlgorithm().getAlgorithmInfo().getAcronym(), t.getProblemName());
                        }
                    }
                }
            }
        }
    }

    public boolean resultEqual(DoubleSolution a, DoubleSolution b) {
        if ((a == null) && (b == null))
            return true;
        if (a == null)
            return false;
        if (b == null)
            return false;
        if (!a.areConstraintsMet() && b.areConstraintsMet())
            return false;
        if (a.areConstraintsMet() && !b.areConstraintsMet())
            return false;
        if (!a.areConstraintsMet() && !b.areConstraintsMet())
            return true;

        // TODO global optimum (requires number of evaluations)
        // if global optimum first check if draw then compare number of
        // evaluations
        /*
         * if(stopCriterion == EnumStopCriterion.GLOBAL_OPTIMUM_OR_EVALUATIONS) {
         * // if results are equal check number of evaluations
         * if(Math.abs(a.getEval()-b.getEval())<draw_limit){ } }
         */

        return Math.abs(a.getEval() - b.getEval()) < drawLimit;
    }
}
