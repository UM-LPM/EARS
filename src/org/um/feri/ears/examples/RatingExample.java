package org.um.feri.ears.examples;

import org.um.feri.ears.rating.Game;
import org.um.feri.ears.rating.Player;
import org.um.feri.ears.rating.Rating;
import org.um.feri.ears.rating.RatingCalculations;

import java.util.HashMap;

public class RatingExample {

    public static void main(String[] args) {
        //Example from the paper
        Player a = new Player(null, "a", new Rating(1500, 200, 0.06), 0, 0, 0);
        Player a1 = new Player(null, "a1", new Rating(1400, 30, 0.06), 0, 0, 0);
        Player a2 = new Player(null, "a2", new Rating(1550, 100, 0.06), 0, 0, 0);
        Player a3 = new Player(null, "a3", new Rating(1700, 300, 0.06), 0, 0, 0);
        Game g1 = new Game(Game.WIN, a, a1, "a");
        a.addGame(g1);
        a1.addGame(g1);
        Game g2 = new Game(Game.WIN, a, a1, "a");
        a.addGame(g2);
        a1.addGame(g2);
        Game g3 = new Game(Game.WIN, a1, a, "a");
        a.addGame(g3);
        a1.addGame(g3);
        Game g4 = new Game(Game.WIN, a, a2, "a");
        a.addGame(g4);
        a2.addGame(g4);
        Game g5 = new Game(Game.WIN, a, a3, "a");
        a.addGame(g5);
        a3.addGame(g5);

        System.out.println(a.getRatingData());
        System.out.println(a1.getRatingData());
        System.out.println(a2.getRatingData());
        System.out.println(a3.getRatingData());
        HashMap<String, Player> players = new HashMap<String, Player>();
        players.put(a.getPlayerId(), a);
        players.put(a1.getPlayerId(), a1);
        players.put(a2.getPlayerId(), a2);
        players.put(a3.getPlayerId(), a3);
        RatingCalculations.computePlayerRatings(players, false);
        System.out.println(a.getRatingData());
        System.out.println(a1.getRatingData());
        System.out.println(a2.getRatingData());
        System.out.println(a3.getRatingData());
        /* 
        System.out.println(a.getR());
        System.out.println(a1.getR());
        new Game(Game.LOSS,a,a1);
        new Game(Game.LOSS,a,a2);
        new Game(Game.WIN,a,a3);
        RatingCalculations.computePlayerRatings(players);
        System.out.println(a.getR());
        System.out.println(a1.getR());
        new Game(Game.WIN,a,a2);
        RatingCalculations.computePlayerRatings(players);
        System.out.println(a.getR());
        System.out.println(a1.getR());*/

    }

}
