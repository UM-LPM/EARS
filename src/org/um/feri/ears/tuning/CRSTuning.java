package org.um.feri.ears.tuning;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmBase;
import org.um.feri.ears.benchmark.RatingBenchmark;
import org.um.feri.ears.problems.results.BankOfResults;
import org.um.feri.ears.rating.Player;
import org.um.feri.ears.rating.Rating;
import org.um.feri.ears.rating.ResultArena;
import org.um.feri.ears.util.Comparator.RatingComparator;
import org.um.feri.ears.util.Util;

public class CRSTuning {
	
	// Control parameters
	private int max_execs;
	private double Cr_rate = 1.00;
	private double F_rate = 1.00;
	private int M = 25; //number of players
	private int E = M/2;
		
	private BankOfResults allSingleProblemRunResults;
    private ArrayList<Player> listAll;
    private boolean printDebug;
    private boolean printSingleRunDuration;
    private ArrayList<Algorithm> players;
    private ResultArena ra;
    protected RatingBenchmark benchMark; // suopm = new RatingRPUOed2();
    private long duration;
    private int noRepeats;
    
    public static void writeToFile(String pFilename, StringBuffer pData) throws IOException {  
        BufferedWriter out = new BufferedWriter(new FileWriter(pFilename));  
        out.write(pData.toString());  
        out.flush();  
        out.close();  
    } 
    
    public StringBuffer outputRanking(int run){
    	StringBuffer sb = new StringBuffer();
    	sb.append("CRS4EAs output " + run + "\n");
    	sb.append("i \t Age \t Algorithm \t Rating \t RD \t RI \t RV" + "\n");
    	for (int i=0;i<listAll.size();i++){
    		sb.append((i) + " \t " + (listAll.get(i).getAlgorithm().getAge()) 
    				+ " \t " + (listAll.get(i).getAlgorithm().getID().replace("\n", "").replace("\r", "")) 
    				+ " \t " + Math.round(listAll.get(i).getRatingData().getRating()) 
    				+ " \t " + Math.round(listAll.get(i).getRatingData().getRD()) 
    				+ " \t [" + Math.round(listAll.get(i).getRatingData().getRating() - 2*listAll.get(i).getRatingData().getRD()) 
    				+ "," + Math.round(listAll.get(i).getRatingData().getRating() + 2*listAll.get(i).getRatingData().getRD()) 
    				+ "] \t " + Math.round(listAll.get(i).getRatingData().getRatingVolatility()) + "\n");
    	}
    	sb.append("-------------------------------------------------------------");
    	return sb;
    }
    
    public void restartRatings(){
        for (Player al : listAll){
        	al.setRatingData(new Rating(1500,350,0.06));
        }
    }

    /**
     * Set all data!
     * 
     * @param printDebug
     * @param banchmark
     * @param arenaName
     * @param arenaOwner
     */
    public static final boolean DEBUG_ON = true; 
    public static final boolean DEBUG_OFF = false;
    
    public CRSTuning(boolean printDebug, boolean printSingleRunDuration, RatingBenchmark banchmark, int max_execs) {
        Util.rnd.setSeed(System.currentTimeMillis());
        players = new ArrayList<Algorithm>();
        this.printDebug = printDebug;
        benchMark = banchmark;
        listAll = new ArrayList<Player>();
        Util.rnd.setSeed(System.currentTimeMillis());
        ra = new ResultArena(100);
        this.printSingleRunDuration = printSingleRunDuration;
        allSingleProblemRunResults =  new BankOfResults();
        this.max_execs = max_execs;
    }
    /**
     * Add algorithms in arena.
     * Then run!
     * 
     * @param al
     * @param startRating
     */
    public void addAlgorithm(Algorithm al, Rating startRating) {
    	int violation = 0;
    	for (int k=0; k<players.size(); k++){
			for (int l=0; l<players.size();l++){
				if (players.get(l).getID().equals(al.getID())){
					violation++;
				}
			}
		}
    	/*if (al.getControlParameters().get(0) < al.getControlParameters().get(3)){
    		violation++;
    	}*/
    	if (violation==0){
	        players.add(al);
	        if (al==null) System.out.println("Add null algorithm");
	        if (al.getAlgorithmInfo()==null) System.out.println("Add algorithm with null AlgorithmInfo "+al.getClass().getName());
	        if (al.getImplementationAuthor()==null)  System.out.println("Add algorithm with null Author "+al.getClass().getName());
	        Player tmp;
	        tmp = new Player(al, al.getID(), startRating, 0, 0, 0);
	        listAll.add(tmp);
	        ra.addPlayer(tmp);
	        benchMark.registerAlgorithm(al);
	  /*      if (benchMark.size() != ra.size()){
	        	System.out.println(al.getID());
	        	System.out.println("exit");
	        	System.exit(-1);
	        }
	        */
    	}
    }
    
    public void removeAlgorithm(AlgorithmBase al) {
    	benchMark.unregisterAlgorithm(al);
		players.remove(al);
		ra.removePlayer(al.getID());
        for (int i=0;i<listAll.size();i++){
        	if (listAll.get(i).getPlayerId().compareTo(al.getID())==0){
        		listAll.remove(i);
        		break;
        	}
        }
        allSingleProblemRunResults.removeAlgorithm(al);
    /*    if (benchMark.size() != ra.size()){
        	System.out.println(al.getID());
        	System.out.println("exit");
        	System.exit(-1);
        }
        */
    }
     
    public void tune(int repeat, ArrayList<ControlParameter> control_parameters, Class<? extends Algorithm> classAlg, String aName, int decimals) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    	noRepeats = repeat;
    	String file = "CRS tuning" + "_" + aName + "M" + M + "F" + F_rate + "Cr" + Cr_rate + "_" + System.currentTimeMillis() + ".txt";
    	StringBuffer sb = new StringBuffer();

    	sb.append("CR: " + Cr_rate + "\nF: " + F_rate + "\nMu: " + M + "\nEa: " + E + "\nMaximum number of executions: " + max_execs + "\n------------------\n\n");
    	
   	
    	//Class<?> classAlg = null;
    	Constructor<?> ctor = null;
		try {
			//classAlg = Class.forName(classAlg);
			ctor = classAlg.getConstructor(ArrayList.class, String.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
     
        while (players.size()<=M){
        	ArrayList<Double> configuration = new ArrayList<Double>();
        	for (ControlParameter cp : control_parameters){
        		configuration.add(cp.randomValue());
        	}
        	
        	Algorithm object = (Algorithm) ctor.newInstance(configuration,aName);
			this.addAlgorithm(object,new Rating(1500, 350, 0.06));
        }

        long stTime = System.currentTimeMillis();
        RatingBenchmark.debugPrint = printDebug; // prints one on one results
        RatingBenchmark.printSingleRunDuration = printSingleRunDuration;
       
        sb.append(outputRanking(0));
        System.out.println(outputRanking(0));

        int i=players.size();
        int gen = 0;
        while (i<max_execs){
        	benchMark.run(ra,allSingleProblemRunResults, repeat);
        	benchMark.allPlayed();
        	ra.calculteRatings();
        	Collections.sort(listAll, new RatingComparator());
        	// Remove significantly worse algorithms
	    	for (int j=1;j<listAll.size();j++){
	    		double difference = Math.abs(Math.round(listAll.get(0).getRatingData().getRating() - listAll.get(j).getRatingData().getRating()));
    			if (difference > 200 || j>E){
    				this.removeAlgorithm(listAll.get(j).getAlgorithm());
	    	        j--;
    			}
	    	}
	    	int n_players = players.size();
        	int cr1 = 0, cr2 = 0;
    		Algorithm new_alg1 = null;
    		Algorithm new_alg2 = null;
    		i = i + (M - players.size());
        	// Create new player through crossover and mutation
	    	if (i<max_execs){
	    		while (players.size()<M){
	    			for (int j=0;j<n_players;j++){
		        		// Find two individuals for crossover
		        		if (Util.rnd.nextDouble()<Cr_rate && players.size()<M){
		        			if (cr1!=0) cr2 = j; else cr1 = j;        		
		        		}
		        		// Uniform Crossover
		        		if (cr1!=0 && cr2!=0){
		        			ArrayList<Double> child1 = new ArrayList<Double>();
		        			ArrayList<Double> child2 = new ArrayList<Double>();
		        			for (int k=0;k<control_parameters.size();k++){
		        				if(Util.rnd.nextDouble()<0.5){
		        					child1.add(players.get(cr1).getControlParameters().get(k));
		        					child2.add(players.get(cr2).getControlParameters().get(k));
		        				}else{
		        					child1.add(players.get(cr2).getControlParameters().get(k));
		        					child2.add(players.get(cr1).getControlParameters().get(k));
		        				}
		        			}
							new_alg1 = (Algorithm) ctor.newInstance(child1,aName);
							new_alg2 = (Algorithm) ctor.newInstance(child2,aName);
			    			if (players.size()<M) this.addAlgorithm(new_alg1,new Rating(1500, 350, 0.06));
			    			if (players.size()<M) this.addAlgorithm(new_alg2,new Rating(1500, 350, 0.06));
			    			cr1=0;
		        			cr2=0;
		        		}
		        		
		        		ArrayList<Double> child = new ArrayList<Double>();
		        		// Mutation
		        		int mutated = 0;
		        		for (int m=0; m<control_parameters.size();m++){
		        			if (Util.rnd.nextDouble()<F_rate && players.size()<M){
		        				child.add(control_parameters.get(m).randomValue());
		        				mutated = 1;
		        			}else{
		        				child.add(players.get(j).getControlParameters().get(m));
		        			}
		        		}
		        		if (mutated==1){
							new_alg1 = (Algorithm) ctor.newInstance(child,aName);
							if (players.size()<M) this.addAlgorithm(new_alg1,new Rating(1500, 350, 0.06));
		        		}
					}
    			}
    		}else{
    			break;
    		}
    		// Output
    		sb.append(outputRanking(gen));
    		System.out.println(outputRanking(gen));
    		System.out.println(i);
    		gen++;
    		//this.restartRatings();
        }

        long endTime = System.currentTimeMillis();
        duration = endTime - stTime;
        System.out.println("DURATION: "+duration/1000+"s");
        sb.append("\n\nDURATION: "+duration/1000+"s\n");
        try {  
            writeToFile(file,sb);  
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
        
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Results for benchmark:").append(benchMark.getAcronym()).append("Benchmark DURATION: ("+duration/1000+"s)").append("\n").append("\n");;
        for (Player a:listAll) {
            sb.append(a.getPlayerId()).append(" ").append(a.getRatingData().toString()).append("\n");
        }
       return sb.toString();
    }
    public BankOfResults getBankOfResults() {
    	return allSingleProblemRunResults;
    }

}
