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

public abstract class TaskBase<T extends ProblemBase> {
	
	protected EnumStopCriterion stopCriterion;
	protected int maxEvaluations; // for Stop criterion
	protected int numberOfEvaluations = 0; // for Stop criterion
	protected double epsilonForGlobal; // epsilon representing the error margin difference when comparing a solution to the global optimum
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
    protected int maxTrialsBeforeStagnation = 10000;
    protected int stagnationTrialCounter = 0;
	/**
	 * Keeps track of the best solution found.
	 */
	protected double bestEval;
	
	//protected StringBuilder ancestorSB;
	protected static List<DoubleSolution> ancestors;
	protected boolean isAncestorLoggingEnabled = false;
	protected ArrayList<EvaluationStorage.Evaluation> evaluationHistory = new ArrayList<>();
	public boolean storeEvaluationHistory = false;

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

	public EnumStopCriterion getStopCriterion() {
		return stopCriterion;
	}

	public TaskBase() {
		resetCount = 0;
	}
	
	public void startTimer()
	{
		timerStart = System.nanoTime();
	}

	/**
	 * Increments the number of iterations.
	 * @throws StopCriterionException if the number of iterations exceeds the maximum number of iterations.
	 */
	public void incrementNumberOfIterations() throws StopCriterionException
	{
		if(stopCriterion == EnumStopCriterion.ITERATIONS)
		{
			if (numberOfIterations >= maxIterations)
				throw new StopCriterionException("Max iterations");
		}
		
		numberOfIterations++;
		if (numberOfIterations >= maxIterations && stopCriterion == EnumStopCriterion.ITERATIONS)
		{
			isStop = true;
		}
	}

	/**
	 * Increments the number of evaluations. This method shouldn't be called manually.
	 * @throws StopCriterionException if the number of evaluations exceeds the maximum number of evaluations .
	 */
	public void incrementNumberOfEvaluations() throws StopCriterionException {
		if (numberOfEvaluations >= maxEvaluations && stopCriterion == EnumStopCriterion.EVALUATIONS)
			throw new StopCriterionException("Max evaluations");
		numberOfEvaluations++;
		if (numberOfEvaluations >= maxEvaluations && (stopCriterion == EnumStopCriterion.EVALUATIONS || stopCriterion == EnumStopCriterion.GLOBAL_OPTIMUM_OR_EVALUATIONS))
			isStop = true;
	}

	/**
     * When comparing a solution to the global optimum and the error margin difference is less or equal to epsilon, then the
     * global optimum has been reached
     * 
     * @return  epsilon representing the error margin difference when comparing a solution to the global optimum
     */
	public double getEpsilonForGlobal() {
	    return epsilonForGlobal;
	}
	
	public int getNumberOfDimensions() {
		return p.getNumberOfDimensions();
	}
	
	public int getNumberOfConstrains() {
	    return p.numberOfConstraints;
	}
	
	public void enableAncestorLogging()
	{
		isAncestorLoggingEnabled = true;
		ancestors = new ArrayList<DoubleSolution>();
	}

	public void disableAncestorLogging()
	{
		isAncestorLoggingEnabled = false;
		ancestors.clear();
	}

	public void saveAncestorLogging4Visualization(String path, Algorithm alg,  int runID) {
		String algID = alg.getID();
		algID=algID.replaceAll("_","");
		algID=algID.replaceAll("\\\\","");
		algID=algID.replaceAll("/","");
		String fileName= path+"\\"+algID+"_"+getProblemName()+"_D"+getNumberOfDimensions();

		String pop_size=alg.getAlgorithmInfo().getParameters().get(EnumAlgorithmParameters.POP_SIZE);
		StringBuffer head= new StringBuffer();
		if (pop_size==null)  pop_size="1";
		head.append(alg.getID()).append(";").append(";[\"").append(pop_size).append("\"];").append(runID).append(";"); //X id
		head.append(getProblemName()).append(";").append(getNumberOfDimensions()).append(";[").append(getMaxEvaluations()).append("];").append("\n");

		try {
			FileOutputStream fos = new FileOutputStream(fileName+".txt");
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);
			bw.write(head.toString()); //first line
			for(int i = 0; i < ancestors.size(); ++i)
			{
				List<DoubleSolution> parents = ancestors.get(i).parents;
				bw.write("{");
				bw.write(ancestors.get(i).getID()+";"+ancestors.get(i).getGenerationNumber()+";");
				bw.write("[");
				if(parents != null)
				{
					for(int j = 0; j < parents.size(); ++j)
					{
						bw.write(""+parents.get(j).getID());
						if(j+1 < parents.size())
							bw.write(",");
					}

				}
				bw.write("];0;");
				bw.write(ancestors.get(i).getID()+";"+ancestors.get(i).getEval()+";"+Arrays.toString(ancestors.get(i).getDoubleVariables()));
				bw.write("}\n");
			}

			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
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
			algorithmParams = algorithmParams.substring(0, algorithmParams.length()-1);
		    
			bw.write("'"+alg.getID()+";["+algorithmParams+"];"+getProblemName()+";"+getNumberOfDimensions()+";[\""+ stopCriterion +"\"];'+\n");
			
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

				bw.write(ancestors.get(i).getTimeStamp()+";"+ancestors.get(i).getEvaluationNumber()+";"+ancestors.get(i).getEval()+";"+Arrays.toString(ancestors.get(i).getDoubleVariables())+"}'");
				
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

	public ArrayList<EvaluationStorage.Evaluation> getEvaluationHistory() {
		return evaluationHistory;
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
	
	public int getMaxIterations() {
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
	
	public int getMaxTrialsBeforeStagnation() {
		return maxTrialsBeforeStagnation;
	}
	
	public long getUsedCPUTime() {
		return System.nanoTime() - timerStart;
	}
	
	public boolean isStopCriterion() {
		
		if(stopCriterion == EnumStopCriterion.CPU_TIME)
		{
			hasTheCPUTimeBeenExceeded();
		}

		//stop only if stop criteria set to global optimum
		if (stopCriterion == EnumStopCriterion.GLOBAL_OPTIMUM_OR_EVALUATIONS && isGlobal)
			return true;

		return isStop;
	}
	
	public boolean hasTheCPUTimeBeenExceeded()
	{
		if(numberOfEvaluations == 0){ //set start time if 0 evaluations
			this.startTimer();
		}
		
		if(System.nanoTime() - timerStart > allowedCPUTime){
			isStop = true;
			return true;
		}
		return false;
	}
	
	public String getStopCriterionDescription() {
        if (stopCriterion == EnumStopCriterion.EVALUATIONS) {
            return "E="+getMaxEvaluations();
        }
        if (stopCriterion == EnumStopCriterion.GLOBAL_OPTIMUM_OR_EVALUATIONS) {
                return "Global optimum epsilon="+ epsilonForGlobal +" or  E="+getMaxEvaluations();
        }
        if(stopCriterion == EnumStopCriterion.ITERATIONS)
        {
        	return "ITERATIONS="+ getMaxIterations();
        }
        if(stopCriterion == EnumStopCriterion.CPU_TIME)
        {
        	return "TIME="+TimeUnit.NANOSECONDS.toMillis(getAllowedCPUTime())+" ms";
        }
        if(stopCriterion == EnumStopCriterion.STAGNATION)
        {
        	return "STAGNATION= trials: "+ stagnationTrialCounter;
        }
        return "not defined";
	}

	/**
	 * Use to check if the problem is to be minimized.
	 * 
	 * @return true if the problem is to be minimized
	 */
	public boolean isMinimize() {
	    return p.isMinimize();
	}
	
    /**
     * @return name of the problem
     */
    public String getProblemName() {
        return p.getName();
    }

	public String getFileNameString() {
    	return p.getFileNameString();
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
        stagnationTrialCounter = 0;
    }
    
    public int getResetCount()
    {
    	return resetCount;
    }

    @Override
    public String toString() {
        return "Task [stopCriterion=" + stopCriterion + ", maxEvaluations=" + maxEvaluations + ", numberOfEvaluations=" + numberOfEvaluations + ", epsilon="
                + epsilonForGlobal + ", isStop=" + isStop + ", isGlobal=" + isGlobal + ", precisionOfRealNumbersInDecimalPlaces="
                + precisionOfRealNumbersInDecimalPlaces + ", p=" + p + "]";
    }
    
    public String getTaskInfoCSV(){
        return  p.getProblemInfoCSV() + "task stop criterion:" + stopCriterion + ",maxEvaluations:" + maxEvaluations + ",epsilon:"+ epsilonForGlobal + ",";
    }
    
    /**
     * Returns a string containing all the tasks information that doesn't change.
     * @return string containing all information about the task
     */
    public String getTaskInfo() {

    	if(stopCriterion == EnumStopCriterion.EVALUATIONS) {
    		return "Task = " + p +" stopCriterion=" + stopCriterion + ", maxEvaluations=" + maxEvaluations + ", epsilon="
    				+ epsilonForGlobal + ", precisionOfRealNumbersInDecimalPlaces="
    				+ precisionOfRealNumbersInDecimalPlaces;
    	}
    	else if(stopCriterion == EnumStopCriterion.ITERATIONS) {
    		return "Task = " + p +" stopCriterion=" + stopCriterion + ", maxIterations=" + maxIterations + ", epsilon="
    				+ epsilonForGlobal + ", precisionOfRealNumbersInDecimalPlaces="
    				+ precisionOfRealNumbersInDecimalPlaces;
    	}
    	else if(stopCriterion == EnumStopCriterion.CPU_TIME){
    		return "Task = " + p +" stopCriterion=" + stopCriterion + ", allowedCPUTime=" + allowedCPUTime + ", epsilon="
    				+ epsilonForGlobal + ", precisionOfRealNumbersInDecimalPlaces="
    				+ precisionOfRealNumbersInDecimalPlaces;
    	}
    	else
    		return "";
    }
}
