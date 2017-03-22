package org.um.feri.ears.problems;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.benchmark.MORatingBenchmark;
import org.um.feri.ears.benchmark.RatingBenchmarkBase;
import org.um.feri.ears.experiment.ee.so.PSOoriginalLogging;

public abstract class TaskBase<T extends ProblemBase> {
	
	protected EnumStopCriteria stopCriteria;
	protected int maxEvaluations; // for Stop criteria
	protected int numberOfEvaluations = 0; // for Stop criteria
	protected double epsilon; // Stop criteria for global optimum
	protected boolean isStop;
	protected boolean isGlobal;
	protected int precisionOfRealNumbersInDecimalPlaces; //used only for discreet problem presentation (bit presentation in GA)
	protected T p;
	private int resetCount;
	
	protected long evaluationTime = 0;
	protected long timerStart;
	protected long allowedCPUTime; // nanoseconds
	protected int numberOfIterations = 0;
	protected int maxIterations;
    protected int maxEvaluationsBeforStagnation = 10000;
    protected int stagnationTrials = 0;
	/**
	 * Keeps track of the best solution found.
	 */
	protected double bestEval;
	
	//protected StringBuilder ancestorSB;
	protected static List<DoubleSolution> ancestors;
	protected boolean isAncestorLogginEnabled = false;
	
	
	
	/**
	 * Has the global optimum been reached.
	 * @return true if the global optimum found else false.
	 */
	public boolean isGlobal() {
		return isGlobal;
	}

	public long getEvaluationTimeNs() {
		return evaluationTime;
	}
	
	public long getEvaluationTimeMs() {
		return TimeUnit.NANOSECONDS.toMillis(evaluationTime);
	}


	public EnumStopCriteria getStopCriteria() {
		return stopCriteria;
	}

	public TaskBase() {
		resetCount = 0;
	}
	
	public void startTimer()
	{
		timerStart = System.nanoTime();
	}
	
	public void incrementNumberOfIterations() throws StopCriteriaException
	{
		if(stopCriteria == EnumStopCriteria.ITERATIONS)
		{
			if (numberOfIterations >= maxIterations)
				throw new StopCriteriaException("Max iterations");
		}
		
		numberOfIterations++;
		if (numberOfIterations >= maxIterations && stopCriteria == EnumStopCriteria.ITERATIONS)
		{
			isStop = true;
		}
	}
	/**
     * When you subtract 2 solutions and difference is less or equal epsilon,
     * solution are treated as equal good (draw in algorithm match)!
     * 
     * @return condition that is used when 2 solutions are equal good!
     */
	public double getEpsilon() {
	    return epsilon;
	}
	
	public int getNumberOfDimensions() {
		return p.getNumberOfDimensions();
	}
	
	public int getNumberOfConstrains() {
	    return p.numberOfConstraints;
	}
	
	public void enableAncestorLogging()
	{
		isAncestorLogginEnabled = true;
		ancestors = new ArrayList<DoubleSolution>();
	}

	public void disableAncestorLogging()
	{
		isAncestorLogginEnabled = false;
		ancestors.clear();
	}
	
	public void saveAncestorLogging(String fileName) {
		
		try {
			FileOutputStream fos = new FileOutputStream(fileName+".csv");
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);
			
			for(int i = 0; i < ancestors.size(); ++i)
			{
				List<DoubleSolution> parents = ancestors.get(i).parents;
				bw.write(ancestors.get(i).getID()+";"+ancestors.get(i).getEval()+";"+Arrays.toString(ancestors.get(i).getDoubleVariables())+";");
				if(parents != null)
				{
					bw.write("[");
					for(int j = 0; j < parents.size(); ++j)
					{
						bw.write(""+parents.get(j).getID());
						if(j+1 < parents.size())
							bw.write(",");
					}
					bw.write("]");
					
				}
				bw.write("\n");
			}
			
			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveGraphingFile(String fileName, Algorithm alg) {
		
		try {
			FileOutputStream fos = new FileOutputStream(fileName+".txt");
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);
			
			AlgorithmInfo info = alg.getAlgorithmInfo();
			Map<EnumAlgorithmParameters, String> algParams = info.getParameters();

			StringBuffer sb = new StringBuffer();
			for (EnumAlgorithmParameters t:algParams.keySet()) {
				sb.append("\""+algParams.get(t)+"\",");
			}
			String algorithmParams = sb.toString();
			algorithmParams = algorithmParams.substring(0, algorithmParams.length()-2);
		    
			bw.write("'"+alg.getID()+";"+info.getPublishedAcronym()+";["+algorithmParams+"];"+getProblemName()+";"+getNumberOfDimensions()+";["+stopCriteria+"];'+\n");
			
			for(int i = 0; i < ancestors.size(); ++i)
			{
				List<DoubleSolution> parents = ancestors.get(i).parents;
				
				bw.write("'{"+ancestors.get(i).getID()+";"+ancestors.get(i).getGenerationNumber()+";");
				
				if(parents != null)
				{
					bw.write("[");
					for(int j = 0; j < parents.size(); ++j)
					{
						bw.write(""+parents.get(j).getID());
						if(j+1 < parents.size())
							bw.write(",");
					}
					bw.write("];");
					
				}
				else
				{
					bw.write("[-1,-1];");
				}

				bw.write(ancestors.get(i).getTimeStamp()+";"+ancestors.get(i).getEvaluationNumner()+";"+ancestors.get(i).getEval()+";"+Arrays.toString(ancestors.get(i).getDoubleVariables())+"}'");
				
				if(i+1 < ancestors.size()){
					bw.write("+\n");
				}
				else{
					bw.write(";");
				}
			}
			
			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Used only for discreet problem presentation (bit presentation in GA)
	 * @return
	 */
	public int getPrecisionMinDecimal() {
	    return precisionOfRealNumbersInDecimalPlaces;
	}
	
	public int getMaxEvaluations() {
        return maxEvaluations;
    }
	
	public int getMaxIteratirons() {
		return maxIterations;
	}
	
	public long getAllowedCPUTime() {
		return allowedCPUTime;
	}
	
	public int getNumberOfEvaluations(){
		return numberOfEvaluations;
	}
	
	public int getNumberOfIterations() {
		return numberOfIterations;
	}
	
	public long getAvailableCPUTime() {
		return allowedCPUTime - System.nanoTime();
	}
	
	public long getUsedCPUTime() {
		return System.nanoTime() - timerStart;
	}
	
	public boolean isStopCriteria() {
		
		if(stopCriteria == EnumStopCriteria.CPU_TIME)
		{
			hasTheCPUTimeBeenExceeded();
		}
		//System.out.println(isStop);
		return isStop||isGlobal;
	}
	
	public boolean hasTheCPUTimeBeenExceeded()
	{
		if(System.nanoTime() - timerStart > allowedCPUTime)
		{
			isStop = true;
			return true;
		}
		return false;
	}
	
	public String getStopCriteriaDescription() {
        if (stopCriteria == EnumStopCriteria.EVALUATIONS) {
            return "E="+getMaxEvaluations();
        }
        if (stopCriteria == EnumStopCriteria.GLOBAL_OPTIMUM_OR_EVALUATIONS) {
                return "Global optimum epsilon="+epsilon+" or  E="+getMaxEvaluations();
        }
        if(stopCriteria == EnumStopCriteria.ITERATIONS)
        {
        	return "ITERATIONS="+getMaxIteratirons();
        }
        if(stopCriteria == EnumStopCriteria.CPU_TIME)
        {
        	return "TIME="+TimeUnit.NANOSECONDS.toMillis(getAllowedCPUTime())+" ms";
        }
        if(stopCriteria == EnumStopCriteria.STAGNATION)
        {
        	return "STAGNATION= trials: "+stagnationTrials;
        }
        return "not defined";
	}
	
	protected void incEvaluate() throws StopCriteriaException {
		if (numberOfEvaluations >= maxEvaluations && stopCriteria == EnumStopCriteria.EVALUATIONS)
			throw new StopCriteriaException("Max evaluations");
		numberOfEvaluations++;
		if (numberOfEvaluations >= maxEvaluations && (stopCriteria == EnumStopCriteria.EVALUATIONS || stopCriteria == EnumStopCriteria.GLOBAL_OPTIMUM_OR_EVALUATIONS))
			isStop = true;
	}
	
	/**
	 * @deprecated
	 * Deprecated is because it is better to use individuals and
	 * isFirstBetter that already is influenced by this parameter.
	 * Returns true id global maximum searching!
	 * 
	 * @return
	 */
	public boolean isMaximize() {
	    return !p.minimum;
	}
	
    /**
     * @return
     */
    public String getProblemName() {
        return p.getName();
    }
    
    public int getProblemHashCode()
    {
    	return p.hashCode();
    }
    
    public void resetCounter() {
    	resetCount++;
        numberOfEvaluations = 0;
        numberOfIterations = 0;
        evaluationTime = 0;
        isStop = false;
        isGlobal = false;
        timerStart = System.nanoTime();
        stagnationTrials = 0;
    }
    
    public int getResetCount()
    {
    	return resetCount;
    }
    
    @Override
    public String toString() {
        return "Task [stopCriteria=" + stopCriteria + ", maxEvaluations=" + maxEvaluations + ", numberOfEvaluations=" + numberOfEvaluations + ", epsilon="
                + epsilon + ", isStop=" + isStop + ", isGlobal=" + isGlobal + ", precisionOfRealNumbersInDecimalPlaces="
                + precisionOfRealNumbersInDecimalPlaces + ", p=" + p + "]";
    }
    
    /**
     * Returns a string containing all the tasks information that doesen't change.
     * @return
     */
    public String taskInfo() {

    	if(stopCriteria == EnumStopCriteria.EVALUATIONS) {
    		return "Task = " + p +" stopCriteria=" + stopCriteria + ", maxEvaluations=" + maxEvaluations + ", epsilon="
    				+ epsilon + ", precisionOfRealNumbersInDecimalPlaces="
    				+ precisionOfRealNumbersInDecimalPlaces;
    	}
    	else if(stopCriteria == EnumStopCriteria.ITERATIONS) {
    		return "Task = " + p +" stopCriteria=" + stopCriteria + ", maxIterations=" + maxIterations + ", epsilon="
    				+ epsilon + ", precisionOfRealNumbersInDecimalPlaces="
    				+ precisionOfRealNumbersInDecimalPlaces;
    	}
    	else if(stopCriteria == EnumStopCriteria.CPU_TIME){
    		return "Task = " + p +" stopCriteria=" + stopCriteria + ", allowedCPUTime=" + allowedCPUTime + ", epsilon="
    				+ epsilon + ", precisionOfRealNumbersInDecimalPlaces="
    				+ precisionOfRealNumbersInDecimalPlaces;
    	}
    	else
    		return "";
    }

}
