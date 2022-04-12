package org.um.feri.ears.examples;

import org.um.feri.ears.statistic.rating_system.GameResult;
import org.um.feri.ears.statistic.rating_system.glicko2.Game;
import org.um.feri.ears.statistic.rating_system.Player;
import org.um.feri.ears.statistic.rating_system.glicko2.Glicko2Rating;
import org.um.feri.ears.statistic.rating_system.glicko2.Glicko2RatingCalculator;

import java.util.HashMap;

public class RatingExample {

    public static void main(String[] args) {
        //Example from the paper
        Player a = new Player("a");
        a.setGlicko2Rating(new Glicko2Rating(1500, 200, 0.06));
        Player a1 = new Player("a1");
        a1.setGlicko2Rating(new Glicko2Rating(1400, 30, 0.06));
        Player a2 = new Player("a2");
        a2.setGlicko2Rating(new Glicko2Rating(1550, 100, 0.06));
        Player a3 = new Player("a3");
        a3.setGlicko2Rating(new Glicko2Rating(1700, 300, 0.06));

        Game g1 = new Game(GameResult.WIN, a, a1, "a");
        a.addGame(g1);
        a1.addGame(g1);
        Game g2 = new Game(GameResult.WIN, a, a1, "a");
        a.addGame(g2);
        a1.addGame(g2);
        Game g3 = new Game(GameResult.WIN, a1, a, "a");
        a.addGame(g3);
        a1.addGame(g3);
        Game g4 = new Game(GameResult.WIN, a, a2, "a");
        a.addGame(g4);
        a2.addGame(g4);
        Game g5 = new Game(GameResult.WIN, a, a3, "a");
        a.addGame(g5);
        a3.addGame(g5);

        System.out.println(a.getGlicko2Rating());
        System.out.println(a1.getGlicko2Rating());
        System.out.println(a2.getGlicko2Rating());
        System.out.println(a3.getGlicko2Rating());
        HashMap<String, Player> players = new HashMap<String, Player>();
        players.put(a.getId(), a);
        players.put(a1.getId(), a1);
        players.put(a2.getId(), a2);
        players.put(a3.getId(), a3);
        Glicko2RatingCalculator.calculateNewRatings(players, false);
        System.out.println(a.getGlicko2Rating());
        System.out.println(a1.getGlicko2Rating());
        System.out.println(a2.getGlicko2Rating());
        System.out.println(a3.getGlicko2Rating());
        /* 
        System.out.println(a.getR());
        System.out.println(a1.getR());
        new Game(Game.LOSE,a,a1);
        new Game(Game.LOSE,a,a2);
        new Game(Game.WIN,a,a3);
        Glicko2RatingCalculator.calculateNewRatings(players);
        System.out.println(a.getR());
        System.out.println(a1.getR());
        new Game(Game.WIN,a,a2);
        Glicko2RatingCalculator.calculateNewRatings(players);
        System.out.println(a.getR());
        System.out.println(a1.getR());*/

    }

}
