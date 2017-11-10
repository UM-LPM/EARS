package org.um.feri.ears.algorithms;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.um.feri.ears.benchmark.EnumBenchmarkInfoParameters;
import org.um.feri.ears.problems.SolutionBase;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.TaskBase;
import org.um.feri.ears.util.Cache;
import org.um.feri.ears.util.Util;

public abstract class AlgorithmBase<T extends TaskBase, T2 extends SolutionBase> {
	
	/**
	 * Search for best solution.
	 * 
	 * if StopCriteriaException is thrown tasks isStopCriteria method is not used properly.
	 * 
	 * @param taskProblem
	 * @return best solution
	 * @throws StopCriteriaException 
	 */
    protected boolean debug;
    protected boolean display_data = false;
    protected boolean save_data = false;
    protected static Cache caching = Cache.None;
   
    protected String version = "1.0";
    protected Author au;
    protected AlgorithmInfo ai;
    protected AlgorithmInfo tmpAi;
    protected AlgorithmRunTime art;
    
    //Tuning
    protected int age;
    protected ArrayList<Double> controlParameters;
    protected boolean played=false;
    
    public void setPlayed(boolean aBool){
    	this.played = aBool;
    }
    public boolean getPlayed(){
    	return played;
    }
    
    public ArrayList<Double> getControlParameters(){
    	return this.controlParameters;
    }
    
    /**
     * Adds total run time of the algorithm and the execution time without function evaluations.
     * @param totalRunTime total run time of the algorithm
     * @param algorithmRunTime execution time of the algorithm without function evaluations
     */
    public void addRunDuration(long totalRunTime, long algorithmRunTime) {
        if (art==null) {
            art = new AlgorithmRunTime();
        }
        art.addRunDuration(totalRunTime, algorithmRunTime);
    }
    
    public String getLastRunDuration()
    {
    	return art.getLastDuration()+" s";
    }
    
    public void setAlgorithmTmpInfo(AlgorithmInfo aii) {
        tmpAi = ai;
        ai = aii;
    }
    public void setAlgorithmInfoFromTmp() {
        ai = tmpAi;
    }
    
    public void setAlgorithmInfo(AlgorithmInfo aii) {
        ai = aii;
    }
    
    /**
     * 
     * 
     * @param taskProblem
     * @return
     * @throws StopCriteriaException
     */
	public abstract T2 execute(T taskProblem) throws StopCriteriaException;
	
	public String getCacheKey(String taskString) {
		
		return "Algorithm = "+ ai.getPublishedAcronym() + " version: "+ version + ", " + shortAlgorithmInfo()+" " + taskString;
	}
	
	public String shortAlgorithmInfo() {
		
		String info ="";
		
		for(Entry<EnumAlgorithmParameters, String> entry : ai.getParameters().entrySet())
		{
			info+= entry.getKey().getShortName() + ": " + entry.getValue()+", ";
		}
		
		if(info.length() > 1)
			info = info.substring(0, info.length() - 2);
		
		return info;
	}
	
	public String longAlgorithmInfo() {
		
		String info ="";
		
		for(Entry<EnumAlgorithmParameters, String> entry : ai.getParameters().entrySet())
		{
			info+= entry.getKey().getDescription() + ": " + entry.getValue()+", ";
		}
		
		if(info.length() > 1)
			info = info.substring(0, info.length() - 2);
		
		return info;
	}
	
	/**
	 * It is called every time before every run! 
	 */
	public abstract void resetDefaultsBeforNewRun();
	public boolean isDebug() {
        return debug;
	}

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public Author getImplementationAuthor() {
        return au;
    }
	public AlgorithmInfo getAlgorithmInfo(){
	    return ai;
	}
	public String getID() {
	    return ai.getVersionAcronym();
	}
	/**
	 * Returns algorithms with different settings for selecting the best one!
	 * maxCombinations is usually set to 8!
	 * If maxCombinations==1 than return combination that is expected to perform best!
	 * 
	 * NOTE not static because jave doesnt support abstract static!
	 * 
	 * @param taskProblem
	 * @return
	 */
	public List<AlgorithmBase> getAlgorithmParameterTest(EnumMap<EnumBenchmarkInfoParameters, String> parameters, int maxCombinations) {
	    List<AlgorithmBase> noAlternative = new ArrayList<AlgorithmBase>();
	    noAlternative.add(this);
	    return noAlternative;
	}
	
    public void resetDuration() {
        art = new AlgorithmRunTime();
    }
    
    public void setDisplayData(boolean b)
    {
    	display_data = b;
    }
    
    public boolean getDisplayData()
    {
    	return display_data;
    }
    
    public void setSaveData(boolean b)
    {
    	save_data = b;
    }

    public boolean getSaveData()
    {
    	return save_data;
    }
    
    public static void setCaching(Cache c)
    {
    	caching = c;
    }
    
    public static Cache getCaching()
    {
    	return caching;
    }

    public int getAge(){
    	return this.age;
    }
    public void setAge(int aAge){
    	this.age = aAge;
    }
    public void increaseAge(){
    	this.age = this.age + 1;
    }
}
