package org.um.feri.ears.visualization.graphing.data;

import java.util.ArrayList;

import org.um.feri.ears.algorithms.AlgorithmBase;
import org.um.feri.ears.problems.SolutionBase;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.ParetoSolution;

@SuppressWarnings("rawtypes")
public class RecordedCombination
{
	public AlgorithmBase algorithm;
	public String problemName;
	public int problemHashCode;
	public long iterationCount;
	public ArrayList<ArrayList<RecordedData>> allRecords;	//allRecords[iteration][index_in_iteration]
	protected ArrayList<RecordedData> records;
	protected ParetoSolution paretoSolution = null;
	
	public RecordedCombination(AlgorithmBase algorithm, String problemName, int problemHashCode)
	{
		this.algorithm = algorithm;
		this.problemName = problemName;
		this.problemHashCode = problemHashCode;
		this.iterationCount = 1;
		allRecords = new ArrayList<ArrayList<RecordedData>>();
		records = new ArrayList<RecordedData>();
		allRecords.add(records);
	}
	public RecordedCombination(AlgorithmBase algorithm, String problemName, int problemHashCode, int iteration)
	{
		this.algorithm = algorithm;
		this.problemName = problemName;
		this.problemHashCode = problemHashCode;
		this.iterationCount = iteration;
		allRecords = new ArrayList<ArrayList<RecordedData>>();
		records = new ArrayList<RecordedData>();
		allRecords.add(records);
	}
	
	public void NextIteration()
	{
		iterationCount = iterationCount + 1;
		records = new ArrayList<RecordedData>();
		allRecords.add(records);
	}
	
	public void AddRecord(SolutionBase sol, String problemName)
	{
		//prob.getName();
		RecordedData d = new RecordedData(sol, problemName, algorithm, iterationCount-1);
		if (!problemName.equals(this.problemName))	// DEBUG (to remove if ok)
			System.err.println("WARRNING: prob != problem (RecorderContext.java)");
		records.add(d);
	}
	
	public void setParetoSolution(ParetoSolution ps)
	{
		paretoSolution = ps;
		/*for (RecordedData rd : records)
		{
			MOSolution sol = (MOSolution)rd.solution;
			int index = ps.solutions.indexOf(sol);
			
			if (index>=0)
			{
				rd.isMemberOfParetoSolution = true;
				MOSolution sol1 = (MOSolution)rd.solution;
				MOSolution sol2 = ps.solutions.get(index);
			}
			else
				rd.isMemberOfParetoSolution = false;
		}*/
		
		// Save actual pareto front:
		records.get(0).paretoFront = (MOSolutionBase[]) ps.solutions.toArray(new MOSolutionBase[0]);
	}
	
	public void Include(RecordedCombination other)
	{
		
		long itInc = this.iterationCount;
		int l=other.allRecords.size();
		for (int i=0; i<l; i++)
		{
			int c = allRecords.get(i).size(); //other.allRecords.get(i).size()
			for (int j=0; j < allRecords.get(i).size(); j++)
			{
				RecordedData d = new RecordedData(allRecords.get(i).get(j));
				d.iteration = d.iteration + itInc;
			}
		}
		this.iterationCount = this.iterationCount + other.iterationCount;
	}
	
	@Override
	public String toString()
	{
		//return algorithm.getAlgorithmInfo().getPublishedAcronym() + "  solving  " + problem.getName();
		return algorithm.getID()+"{"+algorithm.getAlgorithmInfo().getAcronym()+ "}" + "  solving  " + problemName;
	}
	
	@Override
    public boolean equals(Object o) 
	{
        if (this == o) 
        	return true;
        if (!(o instanceof RecordedCombination)) 
        	return false;
        RecordedCombination rc = (RecordedCombination)o;
        return ((algorithm == rc.algorithm) && (problemName == rc.problemName));
    }
	
	@Override
    public int hashCode() {
        int result = algorithm.hashCode();
        result = 31 * result + problemHashCode;
        return result;
    }
}
