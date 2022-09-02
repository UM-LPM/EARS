package org.um.feri.ears.benchmark;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.statistic.rating_system.GameInfo;
import org.um.feri.ears.statistic.rating_system.GameResult;
import org.um.feri.ears.statistic.rating_system.Player;
import org.um.feri.ears.statistic.rating_system.Rating;
import org.um.feri.ears.statistic.rating_system.true_skill.*;
import org.um.feri.ears.util.comparator.AlgorithmResultComparator;
import org.um.feri.ears.util.Util;

import java.util.*;

public abstract class Benchmark extends BenchmarkBase<Task, DoubleSolution, Algorithm> {

    protected abstract void addTask(Problem problem, StopCriterion stopCriterion, int maxEvaluations, long time, int maxIterations);

    @Override
    protected void performTournament(int evaluationNumber) {

        GameInfo gameInfo = GameInfo.getDefaultGameInfo();
        TwoPlayerTrueSkillCalculator calculator = new TwoPlayerTrueSkillCalculator();
        Map<String, Team> oneOnOneTeams = new HashMap<>();
        FactorGraphTrueSkillCalculator fgCalculator = new FactorGraphTrueSkillCalculator();
        Map<String, Team> freeForAllTeams = new HashMap<>();

        int[] ranks;
        int rank;
        Team[] teamArray;

        for(Player player : tournamentResults.getPlayers()) {
            Team team = new Team(player, GameInfo.getDefaultTrueSkillRating());
            oneOnOneTeams.put(player.getId(),team);
            freeForAllTeams.put(player.getId(),team);
        }

        AlgorithmRunResult<DoubleSolution, Algorithm, Task> result1;
        AlgorithmRunResult<DoubleSolution, Algorithm, Task> result2;
        Team team1, team2;
        String algorithm1Id, algorithm2Id;

        int numberOfTicks;

        if(ratingCalculation == RatingCalculation.RATING_CONVERGENCE_SUM)
            numberOfTicks = maxEvaluations / evaluationsPerTick;
        else
            numberOfTicks = 2; // the for loop will run only once


        for (int n = 1; n <= numberOfTicks; n++) {
            if(ratingCalculation == RatingCalculation.RATING_CONVERGENCE_SUM)
                evaluationNumber = n * evaluationsPerTick;

            for (HashMap<Task, ArrayList<AlgorithmRunResult<DoubleSolution, Algorithm, Task>>> problemMap : benchmarkResults.getResultsByRun()) {
                for (ArrayList<AlgorithmRunResult<DoubleSolution, Algorithm, Task>> results : problemMap.values()) {
                    Task t = results.get(0).task;

                    AlgorithmResultComparator rc = new AlgorithmResultComparator(t, evaluationNumber);
                    results.sort(rc); // best first

                    ranks = new int[algorithms.size()];
                    teamArray = new Team[algorithms.size()];
                    rank = 1;
                    ranks[0] = rank;
                    teamArray[0] = freeForAllTeams.get(results.get(0).algorithm.getId());

                    for (int i = 1; i < results.size(); i++) {
                        result1 = results.get(i - 1);
                        result2 = results.get(i);
                        if (!resultEqual(result1, result2))
                            rank++;
                        ranks[i] = rank;
                        teamArray[i] = freeForAllTeams.get(result2.algorithm.getId());
                    }

                    if(tournamentResults.getPlayers().size() > 1) {
                        Map<Player, Rating> newTeamRatings = fgCalculator.calculateNewRatings(gameInfo, Arrays.asList(teamArray), ranks);
                        for (Map.Entry<Player, Rating> entry : newTeamRatings.entrySet()) {
                            Player player = entry.getKey();
                            Rating rating = entry.getValue();
                            freeForAllTeams.put(player.getId(), new Team(player, rating));
                        }
                    }

                    for (int i = 0; i < results.size() - 1; i++) {
                        result1 = results.get(i);
                        algorithm1Id = result1.algorithm.getId();
                        team1 = oneOnOneTeams.get(algorithm1Id);
                        for (int j = i + 1; j < results.size(); j++) {
                            result2 = results.get(j);
                            algorithm2Id = result2.algorithm.getId();
                            team2 = oneOnOneTeams.get(algorithm2Id);
                            Collection<ITeam> competingTeams = Team.concat(team1, team2);
                            Map<Player, Rating> newRatings;
                            if (resultEqual(result1, result2)) {
                                newRatings = calculator.calculateNewRatings(gameInfo, competingTeams, 1, 1);
                                if (printInfo)
                                    System.out.println("draw of " + algorithm1Id + " ("
                                            + Util.df3.format(result1.solution.getEval()) + ", feasible=" + result1.solution.areConstraintsMet()
                                            + ") against " + algorithm2Id + " (" + Util.df3.format(result2.solution.getEval())
                                            + ", feasible=" + result2.solution.areConstraintsMet() + ") for " + t.getProblemName());
                                tournamentResults.addGameResult(GameResult.DRAW, algorithm1Id,
                                        algorithm2Id, t.getProblemName());
                            } else {
                                newRatings = calculator.calculateNewRatings(gameInfo, competingTeams, 1, 2);
                                if (result1.solution == null) {
                                    System.out.println(algorithm1Id + " NULL");
                                }
                                if (result2.solution == null) {
                                    System.out.println(algorithm2Id + " NULL");
                                }
                                if (printInfo)
                                    System.out.println("win of " + algorithm1Id + " ("
                                            + Util.df3.format(result1.solution.getEval()) + ", feasible=" + result1.solution.areConstraintsMet()
                                            + ") against " + algorithm2Id + " (" + Util.df3.format(result2.solution.getEval())
                                            + ", feasible=" + result2.solution.areConstraintsMet() + ") for " + t.getProblemName());
                                tournamentResults.addGameResult(GameResult.WIN, algorithm1Id,
                                        algorithm2Id, t.getProblemName());
                            }
                            Player player1 = tournamentResults.getPlayer(algorithm1Id);
                            Player player2 = tournamentResults.getPlayer(algorithm2Id);
                            Rating player1NewRating = newRatings.get(player1);
                            Rating player2NewRating = newRatings.get(player2);
                            oneOnOneTeams.put(algorithm1Id, new Team(player1, player1NewRating));
                            oneOnOneTeams.put(algorithm2Id, new Team(player2, player2NewRating));
                        }
                    }
                }
            }
        }

        for(Player player : tournamentResults.getPlayers()) {
            Team team = oneOnOneTeams.get(player.getId());
            Rating rating = team.get(player);
            player.setOneOnOneTrueSkill(new TrueSkillRating(rating.getRating(), rating.getRatingDeviation()));

            team = freeForAllTeams.get(player.getId());
            rating = team.get(player);
            player.setFreeForAllTrueSkill(new TrueSkillRating(rating.getRating(), rating.getRatingDeviation()));
        }
        tournamentResults.calculateRatings();
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
