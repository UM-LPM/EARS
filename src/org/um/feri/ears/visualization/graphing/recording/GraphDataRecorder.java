package org.um.feri.ears.visualization.graphing.recording;

import java.util.ArrayList;
import java.util.Hashtable;

import org.um.feri.ears.algorithms.AlgorithmBase;
import org.um.feri.ears.visualization.graphing.data.RecordedCombination;
import org.um.feri.ears.visualization.graphing.data.RecordedData;
import org.um.feri.ears.problems.SolutionBase;
import org.um.feri.ears.problems.TaskBase;
import org.um.feri.ears.problems.moo.ParetoSolution;


public class GraphDataRecorder 
{
	// Variables:
	public static Hashtable<Long,RecorderContext> dataByThread;
	public static boolean enabled = false;
	
	
	
	
	public static boolean isEnabled() {
		return enabled;
	}


	public static void setEnabled(boolean enabled) {
		GraphDataRecorder.enabled = enabled;
	}


	// Initialize:
	static
	{
		dataByThread = new Hashtable<Long,RecorderContext>();
	}
	
	
	
	public static void SetContext(AlgorithmBase alg, TaskBase task)
	{
		// Safety switch:
		if (!enabled)
			return;
		
		// Get context:
		RecorderContext rc = GetContextByThread();
		// Set context:
		rc.SetCurrentContext(alg, task);
	}
	

	public static void AddRecord(SolutionBase sol, String problemName)
	{
		if (!enabled)
			return;
		
		// Get context:
		RecorderContext rc = GetContextByThread();
		rc.AddRecord(sol, problemName);
	}
	
	
	protected static RecorderContext GetContextByThread()
	{
		// Get thread id:
		long threadId = Thread.currentThread().getId();
		//System.err.println("id of the thread is " + threadId + ".");	//DEBUG:
		// New context:
		if (!dataByThread.containsKey(threadId))
		{
			dataByThread.put(threadId, new RecorderContext());
		}
		// Get context:
		RecorderContext rc = dataByThread.get(threadId);
				
		return rc;
	}
	
	
	public static RecordedCombination[] GetAllRecordedCombinations()
	{
		ArrayList<RecordedCombination> allCombinations = new ArrayList<RecordedCombination>();
		RecorderContext[] allContexts = dataByThread.values().toArray(new RecorderContext[0]);
		for (int i=0; i<allContexts.length; i++)
		{
			RecordedCombination[] rcs = allContexts[i].combinations.toArray(new RecordedCombination[0]);
			for (int j=0; j<rcs.length; j++)
			{
				if (!allCombinations.contains(rcs[j]))
				{
					allCombinations.add(rcs[j]);
				}
				else
				{
					RecordedCombination rc1 = allCombinations.get(allCombinations.indexOf(rcs[j]));
					RecordedCombination rc2 = rcs[j];
					rc1.Include(rc2);
				}
			}
		}
		return allCombinations.toArray(new RecordedCombination[0]);
	}
	
	
	public static RecordedData[] GetAllData()
	{
		ArrayList<RecordedData> allData = new ArrayList<RecordedData>();
		RecordedCombination[] allCombinations = GetAllRecordedCombinations();
		for (int i=0; i<allCombinations.length; i++)
		{
			RecordedCombination tempComb = allCombinations[i];
			for (int j=0; j<tempComb.iterationCount; j++)
			{
				allData.addAll(tempComb.allRecords.get(j));
			}
		}
		return allData.toArray(new RecordedData[0]);
	}
	
	
	public static void SetParetoSolution(ParetoSolution ps)
	{
		if (!enabled)
			return;
		
		// Get context:
			RecorderContext rc = GetContextByThread();
			rc.setParetoSolution(ps);
	}
	
}
