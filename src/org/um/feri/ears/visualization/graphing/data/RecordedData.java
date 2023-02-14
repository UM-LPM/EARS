package org.um.feri.ears.visualization.graphing.data;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.Solution;

@SuppressWarnings("rawtypes")
public class RecordedData //implements Comparable<RecordedData>
{
	public Solution solution;
	//public ProblemBase problem;
	public String problemName;
	public Algorithm algorithm;
	public long iteration;
	//public boolean isMemberOfParetoSolution = false;
	public NumberSolution[] paretoFront = null;
	
	// Constructors:
	public RecordedData(NumberSolution solution)
	{
		this.solution = solution;
	}
	public RecordedData(double[] objectives)
	{
		NumberSolution debug = new NumberSolution(objectives.length);
		for (int i=0; i<objectives.length; i++)
			debug.setObjective(i, objectives[i]);
		this.solution = debug;
		
	}
	public RecordedData(Solution solution, String problemName)
	{
		this.solution = solution;
		this.problemName = problemName;
	}
	public RecordedData(Solution solution, String problemName, Algorithm algorithm)
	{
		this.solution = solution;
		this.problemName = problemName;
		this.algorithm = algorithm;
	}
	public RecordedData(Solution sol, String problemName, Algorithm algorithm, long iteration)
	{
		this.solution = sol;
		this.problemName = problemName;
		this.algorithm = algorithm;
		this.iteration = iteration;
	}
	public RecordedData(RecordedData other)
	{
		this.solution = other.solution;
		this.problemName = other.problemName;
		this.algorithm = other.algorithm;
		this.iteration = other.iteration;
	}
	/*@Override
	public int compareTo(RecordedData other)
	{
		if (solution instanceof MOSolution)
		{
			double[] o1 = ((MOSolution)solution).getObjectives();
			double[] o2 = ((MOSolution)other.solution).getObjectives();
			for (int i=0; i<o1.length; i++)
			{
				if (o1[i]<o2[i])
					return -1;
				else if (o1[i]>o2[i])
					return 1;
			}
		}
		return 0;
	}*/
}
