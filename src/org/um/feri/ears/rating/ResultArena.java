/**
 * Main class where all results are collected!
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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.um.feri.ears.algorithms.AlgorithmBase;
import org.um.feri.ears.benchmark.AlgorithmEvalResult;
import org.um.feri.ears.rating.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.um.feri.ears.util.Comparator.RatingComparator;

public class ResultArena {
	private HashMap<String,Player> players;
	private List<Game> allGames;
	
	int id_period;
	public ResultArena(){
		this(1);
	}
	
	public ResultArena(int id_period) {
		players = new HashMap<String, Player>();
		allGames = new ArrayList<Game>();
		this.id_period = id_period;
	}
	
    public void addPlayer(Player p){
        players.put(p.getPlayerId(),p);
    }

    //Default values
	public void addPlayer(AlgorithmBase algorithm, String id){
		addPlayer(algorithm, id, 1500, 350, 0.06, 0, 0, 0);
	}
    
	public void addPlayer(AlgorithmBase algorithm, String id, double rating, double RD, double ratingVolatility){
		addPlayer(algorithm, id, rating, RD, ratingVolatility, 0, 0, 0);
	}
    
	public void addPlayer(AlgorithmBase algorithm, String id, double rating, double RD, double ratingVolatility, int w, int l, int d){
		players.put(id, new Player(algorithm, id,new Rating(rating, RD, ratingVolatility),w,l,d));
	}
	
	public Player getPlayer(String id){
		return players.get(id);
	}
	
	/**
	 * Players need to be in arena!
	 * 
	 * @param gameResult
	 * @param a
	 * @param b
	 * @param algorithm
	 */
	public void addGameResult(double gameResult, String a, String b, String algorithm) {
		Player one = players.get(a);
		Player two = players.get(b);
		Game newGame = new Game(gameResult, one, two, algorithm);
        one.addGame(newGame);
        two.addGame(newGame);
        allGames.add(newGame);
	}
	
	/**
	 * Players need to be in arena!
	 * 
	 * @param gameResult
	 * @param a
	 * @param b
	 * @param algorithm
	 * @param indicator
	 */
	public void addGameResult(double gameResult, String a, String b, String algorithm, String indicator) {
		Player one = players.get(a);
		Player two = players.get(b);
		Game newGame = new Game(gameResult, one, two, algorithm, indicator);
        one.addGame(newGame);
        two.addGame(newGame);
        allGames.add(newGame);
	}
	/**
	 * Recalculates ranks for all games played. All ranks need to be updated. 
	 * @return list of all players in the arena with new ratings.
	 */
	public ArrayList<Player> recalcRatings() {
		id_period++;
		ArrayList<Player> ap = new ArrayList<Player>();
		RatingCalculations.computePlayerRatings(players, true); //changes ratings
		ap.addAll(players.values());
		Collections.sort(ap, new RatingComparator());
		return ap;
		
	}
	
	/**
	 * Calculates the ratings for all unevaluated games.
	 * @return list of all players in the arena with new ratings.
	 */
	public ArrayList<Player> calculteRatings() {
		id_period++;
		ArrayList<Player> ap = new ArrayList<Player>();
		RatingCalculations.computePlayerRatings(players, false); //changes ratings
		ap.addAll(players.values());
		Collections.sort(ap, new RatingComparator());
		return ap;
	}
	
	public ArrayList<Player> getPlayers() {
		ArrayList<Player> ap = new ArrayList<Player>();
		ap.addAll(players.values());
		Collections.sort(ap, new RatingComparator());
		return ap;
	}
	
	public List<Game> getUnevaluatedGames()
	{
		List<Game> unevaluatedGames = new ArrayList<Game>();
		
		for(Game g : allGames)
		{
			if(!g.isEvaluated())
				unevaluatedGames.add(g);
		}
		return unevaluatedGames;
	}

	public void removePlayer(String id) {
		players.remove(id);
		
	}

	public String getPlayersJson() {
		
		JsonPlayer[] jsonPlayers = new JsonPlayer[players.size()];
		int index = 0;
		for (Player p : players.values()) {
			jsonPlayers[index++] = p.toJson();
		}
		
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    	String json = gson.toJson(jsonPlayers);
	
		return json;
	}
}
