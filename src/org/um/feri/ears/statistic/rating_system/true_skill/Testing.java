package org.um.feri.ears.statistic.rating_system.true_skill;

import org.um.feri.ears.statistic.rating_system.GameInfo;
import org.um.feri.ears.statistic.rating_system.Player;
import org.um.feri.ears.statistic.rating_system.Rating;

import java.util.Collection;
import java.util.Map;

public class Testing {

    public static void main(String[] args) {
        TwoPlayerTrueSkillCalculator calculator = new TwoPlayerTrueSkillCalculator();
        Player player1 = new Player("1");
        Player player2 = new Player("2");
        GameInfo gameInfo = GameInfo.getDefaultGameInfo();

        Team team1 = new Team(player1, GameInfo.getDefaultTrueSkillRating());
        Team team2 = new Team(player2, GameInfo.getDefaultTrueSkillRating());
        Collection<ITeam> teams = Team.concat(team1, team2);

        Map<Player, Rating> newRatings = calculator.calculateNewRatings(gameInfo, teams, 1, 2);

        Rating player1NewRating = newRatings.get(player1);
        System.out.println(player1NewRating);
        //assertRating(29.39583201999924, 7.171475587326186, player1NewRating);

        Rating player2NewRating = newRatings.get(player2);
        System.out.println(player2NewRating);
        //assertRating(20.60416798000076, 7.171475587326186, player2NewRating);

        //assertMatchQuality(0.447, calculator.calculateMatchQuality(gameInfo, teams));
    }
}
