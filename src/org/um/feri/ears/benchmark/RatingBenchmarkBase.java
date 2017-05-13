package org.um.feri.ears.benchmark;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.concurrent.TimeUnit;

import org.jfree.ui.RefineryUtilities;
import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmBase;
import org.um.feri.ears.export.data.EDBenchmark;
import org.um.feri.ears.export.data.EDTask;
import org.um.feri.ears.problems.EnumStopCriteria;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.MOTask;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.SolutionBase;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.TaskBase;
import org.um.feri.ears.problems.results.BankOfResults;
import org.um.feri.ears.rating.Player;
import org.um.feri.ears.rating.ResultArena;
import org.um.feri.ears.util.RatingIntervalPlot;

public abstract class RatingBenchmarkBase<T extends TaskBase, T2 extends AlgorithmBase, T3> {
    public static boolean debugPrint=false;
    protected ArrayList<T> listOfProblems;
    protected ArrayList<T2> listOfAlgorithmsPlayers;
    protected boolean recheck = false;
    protected boolean displayRatingIntervalChart = true;
    public static boolean printSingleRunDuration = true;
    
   	// Default benchmark settings
    protected EnumStopCriteria stopCriteria = EnumStopCriteria.STAGNATION; 
    protected int maxEvaluations = 1500;
    protected long timeLimit = TimeUnit.MILLISECONDS.toNanos(500); //milliseconds
    protected int maxIterations = 500;
    public double draw_limit = 1e-7;
    protected int dimension = 2;
    
    protected ArrayList<T3> results;
    protected EnumMap<EnumBenchmarkInfoParameters,String> parameters; //add all specific parameters
    protected int duelNumber;
    private int resetCallCounter; // increment when calling task reset counter 
    public void addParameter(EnumBenchmarkInfoParameters id, String value){
        parameters.put(id, value);
    }
    
    public EnumMap<EnumBenchmarkInfoParameters, String> getParameters() {
        return parameters;
    }
    public void clearPlayers() {
        listOfAlgorithmsPlayers.clear();
        results.clear();
    }
    
    protected void reset(TaskBase tb)
    {
    	incrementResetCallCounter();
    	tb.resetCounter();
    }

    public ArrayList<TaskBase> getAllTasks() {
        ArrayList<TaskBase> a = new  ArrayList<TaskBase>();
        for (TaskBase tw:listOfProblems) {
            a.add(tw);
        }
        return a;
    }
    
    /**
     * Remove algorithm from benchmark
     * @param al
     */
    public void unregisterAlgorithm(AlgorithmBase al) {
        listOfAlgorithmsPlayers.remove(al);
        results.remove(al);
    }
    
    public String getParams() {
        StringBuffer sb = new StringBuffer();
        sb.append("Parameters:\n");
        for (EnumBenchmarkInfoParameters a:parameters.keySet()) {
            sb.append(a.getShortName()).append(" = ").append(parameters.get(a)).append("\t").append("(").append(a.getDescription()).append(")\n");
        }
        return sb.toString();
    }
    public void updateParameters() {
        parameters.put(EnumBenchmarkInfoParameters.NUMBER_OF_TASKS, ""+listOfProblems.size());
        addParameter(EnumBenchmarkInfoParameters.STOPPING_CRITERIA,""+stopCriteria);
        addParameter(EnumBenchmarkInfoParameters.DIMENSION,""+dimension);
        addParameter(EnumBenchmarkInfoParameters.EVAL,String.valueOf(maxEvaluations));
        addParameter(EnumBenchmarkInfoParameters.CPU_TIME,String.valueOf(timeLimit));
        addParameter(EnumBenchmarkInfoParameters.ITTERATIONS,String.valueOf(maxIterations));
        addParameter(EnumBenchmarkInfoParameters.DRAW_PARAM,"abs(evaluation_diff) < "+draw_limit);
    }
    public EDBenchmark export() {
        updateParameters();
        EDBenchmark ed=new EDBenchmark();
        ed.acronym = getAcronym();
        ed.name = getName();
        ed.info = getParams();
        if (getInfo().length()>3) ed.info=ed.info+"\n"+getInfo();
        EDTask tmp;
        for (TaskBase ta:listOfProblems) {
            tmp = new EDTask();
            tmp.name = ta.getProblemName();
            tmp.info = ta.getStopCriteriaDescription(); //tu!!!
            ed.tasks.add(tmp);
        }
        return ed;
    }
    public RatingBenchmarkBase() {
        listOfProblems = new ArrayList<T>();
        listOfAlgorithmsPlayers = new ArrayList<T2>();
        results = new ArrayList<T3>();
        parameters = new  EnumMap<EnumBenchmarkInfoParameters, String>(EnumBenchmarkInfoParameters.class);
        resetCallCounter = 0;
        //initFullProblemList();
    }
    
    public boolean isDisplayRatingIntervalChart() {
		return displayRatingIntervalChart;
	}

	public void setDisplayRatingIntervalChart(boolean displayRatingIntervalChart) {
		this.displayRatingIntervalChart = displayRatingIntervalChart;
	}
    
    protected abstract void initFullProblemList(); 
    
    public abstract void registerAlgorithm(T2 al);
    
    public abstract void registerAlgorithms(ArrayList<T2> algorithms);
    
    protected void incrementResetCallCounter()
    {
    	resetCallCounter++;
    }
    public int getCounter()
    {
    	return resetCallCounter;
    }
    
    public abstract String getName(); //long name 
    public abstract String getAcronym(); //short name for tables etc    
    public abstract String getInfo(); //some explanation
    
    /**
     * @param task
     * @param allSingleProblemRunResults 
     */
    protected abstract void runOneProblem(T task, BankOfResults allSingleProblemRunResults);
    
    protected abstract void setWinLoseFromResultList(ResultArena arena, T t);
    
    /**
     * Fill all data!
     *  
     * @param arena needs to be filed with players and their ratings
     * @param allSingleProblemRunResults 
     * @param repetition
     */
    public abstract void run(ResultArena arena, BankOfResults allSingleProblemRunResults, int repetition);
    
    public void displayRatingIntervalsChart(ArrayList<Player> list)
    {
        RatingIntervalPlot plot = new RatingIntervalPlot("Rating Interval", list);
        plot.pack();
        RefineryUtilities.centerFrameOnScreen(plot);
        plot.setVisible(true);
    }
    
    public void allPlayed(){
    	for (AlgorithmBase al: listOfAlgorithmsPlayers) {
        	al.setPlayed(true);
        }
    }
    
	protected boolean isCheating() {
		int sum = 0;
        for (TaskBase t:listOfProblems) {
        	sum+= t.getResetCount();
        }
        if(sum > getCounter())
        {
        	return true;
        }
        
		return false;
	}
}
