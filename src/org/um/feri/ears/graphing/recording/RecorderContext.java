package org.um.feri.ears.graphing.recording;

import java.util.ArrayList;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmBase;
import org.um.feri.ears.graphing.data.*;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.ProblemBase;
import org.um.feri.ears.problems.SolutionBase;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.TaskBase;
import org.um.feri.ears.problems.moo.ParetoSolution;


@SuppressWarnings("rawtypes")
public class RecorderContext
{
	//public ArrayList<RecordedData> records;
	public ArrayList<RecordedCombination> combinations;
	public AlgorithmBase algorithm;
	public TaskBase task;
	public String problemName;
	public long iteration;
	public long pop_size = -1;
	public String pop_size_string = "";
	protected RecordedCombination currentCombination;
	
	
	// Constructors:
	public RecorderContext()
	{
		//records = new ArrayList<RecordedData>();
		combinations = new ArrayList<RecordedCombination>();
	}
	
	
	// Set current context:
	public void SetCurrentContext(AlgorithmBase alg, TaskBase task)
	{
		// Basic data:
		this.algorithm = alg;
		this.task = task;
		this.problemName =  task.getProblemName();
		/*pop_size_string = alg.getAlgorithmInfo().getParameters().get(EnumAlgorithmParameters.POP_SIZE);
		if (pop_size_string == null)
		{
			System.err.println("Error: Could not resolve pop size for algorithm " + alg.getClass().toString() + ".");
		}
		else
		{
			try
			{
				this.pop_size = Long.parseLong(pop_size_string);
			} 
			catch (NumberFormatException ex)
			{
				System.err.println("Error: Could not parse pop size for algorithm " + alg.getClass().toString() + ".");
			}
		}*/
		
		// Combinations:
		RecordedCombination tempComb = new RecordedCombination(alg,problemName, task.getProblemHashCode());
		if (!combinations.contains(tempComb))
		{
			combinations.add(tempComb);
			iteration = tempComb.iterationCount;
			currentCombination = tempComb;
		}
		else
		{
			int i = combinations.indexOf(tempComb);
			currentCombination = combinations.get(i);
			currentCombination.NextIteration();
			iteration = combinations.get(i).iterationCount;
			//System.err.println("Already contains:" + alg.getID() + " " + problem.getName());
		}
	}
	
	
	public void AddRecord(SolutionBase sol, String problemName)
	{
		currentCombination.AddRecord(sol, problemName);
	}
	// Add record to current context:
	/*public void AddRecord(Solution sol, Problem prob)
	{
		//prob.getName();
		RecordedData d = new RecordedData(sol, problem, algorithm, iteration);
		if (prob != problem)	// DEBUG (to remove if ok)
			System.err.println("WARRNING: prob != problem (RecorderContext.java)");
		records.add(d);
	}*/
	
	
	public void setParetoSolution(ParetoSolution ps)
	{
		currentCombination.setParetoSolution(ps);
	}
	
}
