package org.um.feri.ears.tuning;

import java.util.ArrayList;

import org.um.feri.ears.benchmark.MOAlgorithmEvalResult;
import org.um.feri.ears.rating.Player;

public class CRSSolution {
	
    private double F;
    private double CR;
	
	Player p;
	double[] params;
	ArrayList<MOAlgorithmEvalResult> allGamesPlayed;
	public String name;
	
	public CRSSolution()
	{
		allGamesPlayed = new ArrayList<MOAlgorithmEvalResult>(); 
	}
	
    public CRSSolution(CRSSolution sol) {
    	this.params = new double[sol.params.length];
    	System.arraycopy( sol.params, 0, this.params, 0, sol.params.length );
        this.p = new Player(sol.p);
        this.F = sol.F;
        this.CR =sol.CR;
        this.name = sol.name;
        
        allGamesPlayed = new ArrayList<MOAlgorithmEvalResult>(); 
        for(MOAlgorithmEvalResult res : sol.allGamesPlayed){
        	allGamesPlayed.add(new MOAlgorithmEvalResult(res));
        }  
    }
    
    public CRSSolution(CRSSolution sol, double F, double CR) {
    	this.params = new double[sol.params.length];
    	System.arraycopy( sol.params, 0, this.params, 0, sol.params.length );
        this.p = new Player(sol.p);
        this.F = F;
        this.CR = CR;
        this.name = sol.name;
        
        allGamesPlayed = new ArrayList<MOAlgorithmEvalResult>(); 
        for(MOAlgorithmEvalResult res : sol.allGamesPlayed){
        	allGamesPlayed.add(new MOAlgorithmEvalResult(res));
        }  
    }
    
    public double getEval() {
    	return p.getRatingData().getRating();
    }
	
    public double getF() {
        return F;
    }
    public void setF(double f) {
        F = f;
    }
    public double getCR() {
        return CR;
    }
    public void setCR(double cR) {
        CR = cR;
    }

}
