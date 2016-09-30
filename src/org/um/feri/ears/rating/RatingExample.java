/**
 * Insert data
 * <p>
 * 
 * @author Matej Crepinsek
 * @version 1
 * 
 *          <h3>License</h3>
 * 
 *          Copyright (c) 2011 by Matej Crepinsek. <br>
 *          All rights reserved. <br>
 * 
 *          <p>
 *          Redistribution and use in source and binary forms, with or without
 *          modification, are permitted provided that the following conditions
 *          are met:
 *          <ul>
 *          <li>Redistributions of source code must retain the above copyright
 *          notice, this list of conditions and the following disclaimer.
 *          <li>Redistributions in binary form must reproduce the above
 *          copyright notice, this list of conditions and the following
 *          disclaimer in the documentation and/or other materials provided with
 *          the distribution.
 *          <li>Neither the name of the copyright owners, their employers, nor
 *          the names of its contributors may be used to endorse or promote
 *          products derived from this software without specific prior written
 *          permission.
 *          </ul>
 *          <p>
 *          THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *          "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *          LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 *          FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 *          COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 *          INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 *          BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *          LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 *          CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 *          LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 *          ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *          POSSIBILITY OF SUCH DAMAGE.
 * 
 */
package org.um.feri.ears.rating;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class RatingExample {

    /**
     * @param args
     */
    public static void main(String[] args) {
        //Example from the paper
        Player a = new Player("a", new Rating(1500,200,0.06),0,0,0);
        Player a1 = new Player("a1", new Rating(1400,30,0.06),0,0,0);
        Player a2 = new Player("a2", new Rating(1550,100,0.06),0,0,0);
        Player a3 = new Player("a3", new Rating(1700,300,0.06),0,0,0);
        Game g1 = new Game(Game.WIN,a,a1,"a");
        a.addGame(g1);
        a1.addGame(g1);
        Game g2 =new Game(Game.WIN,a,a1,"a");
        a.addGame(g2);
        a1.addGame(g2);
        Game g3 =new Game(Game.WIN,a1,a,"a");
        a.addGame(g3);
        a1.addGame(g3);
        Game g4 =new Game(Game.WIN,a,a2,"a");
        a.addGame(g4);
        a2.addGame(g4);
        Game g5 =new Game(Game.WIN,a,a3,"a");
        a.addGame(g5);
        a3.addGame(g5);
        
        System.out.println(a.getRatingData());
        System.out.println(a1.getRatingData());
        System.out.println(a2.getRatingData());
        System.out.println(a3.getRatingData());
        HashMap<String,Player> players = new HashMap<String, Player>();
        players.put(a.getPlayerId(), a);
        players.put(a1.getPlayerId(), a1);
        players.put(a2.getPlayerId(), a2);
        players.put(a3.getPlayerId(), a3);   
            RatingCalculations.computePlayerRatings(players);
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
