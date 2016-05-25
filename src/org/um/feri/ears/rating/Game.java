/**
 * Describes result of the one on one match. Result state is always for first one!
 * 
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

public class Game {
    public static final double LOSS = 0;
    public static final double WIN = 1;
    public static final double DRAW = 0.5;    
    private double gameResult;
    private String idProblem; //some print info data
    private Player a,b;
    private String indicator; //only for multi-objective
    private boolean evaluated;
    
    public Game() {
    	setEvaluated(false);
    }
    /**
     * Sets game result for first (a). if a win than b is lost. 
     * With creation of new object new game result is set. 
     * 
     * @param gameResult
     * @param a
     * @param b
     */
    public Game(double gameResult, Player a, Player b, String idProblem) {
        super();
        setEvaluated(false);
        this.gameResult = gameResult;
        this.idProblem = idProblem; //needed in add
        this.a = a;
        this.b = b;
    }
    
    /**
     * Sets game result for first (a). if a win than b is lost. 
     * With creation of new object new game result is set. 
     * 
     * @param gameResult
     * @param a
     * @param b
     */
    public Game(double gameResult, Player a, Player b, String idProblem, String indicator) {
        super();
        setEvaluated(false);
        this.gameResult = gameResult;
        this.idProblem = idProblem; //needed in add
        this.indicator = indicator;
        this.a = a;
        this.b = b;
    }
    
    public String toString(){
        return a.getPlayerId()+" : "+b.getPlayerId()+" r:"+gameResult;
    }
    public boolean isEvaluated() {
		return evaluated;
	}
	public void setEvaluated(boolean evaluated) {
		this.evaluated = evaluated;
	}
	public String getOpponent(String one){
        if (a.getPlayerId().equals(one)) return b.getPlayerId();
        return a.getPlayerId();
    }
	
	public Player getOpponent(Player player) {
		if (a.getPlayerId().equals(player.getPlayerId())) 
			return b;
		return a;
	}
	
    public double getGameResult() {
        return gameResult;
    }

    public String getIdProblem() {
        return idProblem;
    }
    
    public String getIndicator(){
    	return indicator;
    }


    /**
     * Different result depend for who we are asking. First or second.
     * 
     * @param id 
     * @return 
     */
    public double getGameResult(String id) {
    	if (a.getPlayerId().equals(id)) {
            return gameResult;    		
    	}
        return 1-gameResult; //win -> loss; loss->win draw->draw
    }
    
    public Player getA() {
        return a;
    }
    public void setA(Player a) {
        this.a = a;
    }
    public Player getB() {
        return b;
    }
    public void setB(Player b) {
        this.b = b;
    }

}
