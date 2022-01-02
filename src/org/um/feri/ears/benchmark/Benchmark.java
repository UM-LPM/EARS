package org.um.feri.ears.benchmark;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.statistic.glicko2.Game;
import org.um.feri.ears.statistic.true_skill.*;
import org.um.feri.ears.util.Comparator.AlgorithmResultComparator;
import org.um.feri.ears.util.Util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class Benchmark extends BenchmarkBase<Task, DoubleSolution, Algorithm> {

    protected abstract void addTask(Problem problem, StopCriterion stopCriterion, int maxEvaluations, long time, int maxIterations, double epsilon);

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
                                        + Util.df3.format(win.solution.getEval()) + ", feasible=" + win.solution.areConstraintsMet()
                                        + ") against " + lose.algorithm.getID() + " (" + Util.df3.format(lose.solution.getEval())
                                        + ", feasible=" + lose.solution.areConstraintsMet() + ") for " + t.getProblemName());
                            resultArena.addGameResult(Game.DRAW, win.algorithm.getID(),
                                    lose.algorithm.getID(), t.getProblemName());
                        } else {
                            if (win.solution == null) {
                                System.out.println(win.algorithm.getID() + " NULL");
                            }
                            if (lose.solution == null) {
                                System.out.println(lose.algorithm.getID() + " NULL");
                            }
                            if (printInfo)
                                System.out.println("win of " + win.algorithm.getID() + " ("
                                        + Util.df3.format(win.solution.getEval()) + ", feasible=" + win.solution.areConstraintsMet()
                                        + ") against " + lose.algorithm.getID() + " (" + Util.df3.format(lose.solution.getEval())
                                        + ", feasible=" + lose.solution.areConstraintsMet() + ") for " + t.getProblemName());
                            resultArena.addGameResult(Game.WIN, win.algorithm.getID(),
                                    lose.algorithm.getID(), t.getProblemName());
                        }
                    }
                }
            }
        }
        calculateTrueSkill();
    }

    private void calculateTrueSkill() {

        TwoPlayerTrueSkillCalculator calculator = new TwoPlayerTrueSkillCalculator();
        GameInfo gameInfo = GameInfo.getDefaultGameInfo();
        Map<String, Player<String>> players = new HashMap<>();
        Map<String, Team> teams = new HashMap<>();

        //////////////////////
        int[] ranks = new int[algorithms.size()];
        int rank = 1;
        FactorGraphTrueSkillCalculator fgCalculator = new FactorGraphTrueSkillCalculator();
        Map<String, Team> teams2 = new HashMap<>();


        for(Algorithm algorithm : algorithms) {
            Player<String> player = new Player<>(algorithm.getID());
            players.put(algorithm.getID(),player);
            Team team = new Team(player, gameInfo.getDefaultRating());
            teams.put(algorithm.getID(),team);
            teams2.put(algorithm.getID(),team);
        }

        for (HashMap<Task, ArrayList<AlgorithmRunResult<DoubleSolution, Algorithm, Task>>> problemMap : benchmarkResults.getResultsByRun()) {
            for (ArrayList<AlgorithmRunResult<DoubleSolution, Algorithm, Task>> results : problemMap.values()) {
                Task t = results.get(0).task;

                AlgorithmRunResult<DoubleSolution, Algorithm, Task> algorithm1;
                AlgorithmRunResult<DoubleSolution, Algorithm, Task> algorithm2;
                Team team1, team2;

                AlgorithmResultComparator rc = new AlgorithmResultComparator(t);
                results.sort(rc); // best first
                for (int i = 0; i < results.size() - 1; i++) {
                    algorithm1 = results.get(i);
                    team1 = teams.get(algorithm1.algorithm.getID());
                    for (int j = i + 1; j < results.size(); j++) {
                        algorithm2 = results.get(j);
                        team2 = teams.get(algorithm2.algorithm.getID());
                        Collection<ITeam> competingTeams = Team.concat(team1, team2);
                        Map<IPlayer, Rating> newRatings;
                        if (resultEqual(algorithm1, algorithm2)) {
                            newRatings = calculator.calculateNewRatings(gameInfo, competingTeams, 1, 1);
                        } else {
                            newRatings = calculator.calculateNewRatings(gameInfo, competingTeams, 1, 2);
                        }
                        Player<String> player1 = players.get(algorithm1.algorithm.getID());
                        Player<String> player2 = players.get(algorithm2.algorithm.getID());
                        Rating player1NewRating = newRatings.get(player1);
                        Rating player2NewRating = newRatings.get(player2);
                        teams.put(algorithm1.algorithm.getID(), new Team(player1, player1NewRating));
                        teams.put(algorithm2.algorithm.getID(), new Team(player2, player2NewRating));
                    }
                }
            }
        }

        for(Team t: teams.values()) {
            for (Map.Entry<IPlayer, Rating> es : t.entrySet()) {
                System.out.println(es.getKey().toString() + " " + es.getValue());
            }
        }

        //TODO check if same

        //Map<IPlayer, Rating> newRatings = calculator.calculateNewRatings(gameInfo, Team.concat(team1, team2, team3, team4, team5, team6, team7, team8, team9, team10, team11, team12, team13, team14, team15, team16),1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
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
