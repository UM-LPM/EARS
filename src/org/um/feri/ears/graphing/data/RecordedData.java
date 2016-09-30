package org.um.feri.ears.graphing.data;

import org.um.feri.ears.algorithms.AlgorithmBase;
import org.um.feri.ears.problems.ProblemBase;
import org.um.feri.ears.problems.SolutionBase;
import org.um.feri.ears.problems.moo.MOSolutionBase;

@SuppressWarnings("rawtypes")
public class RecordedData //implements Comparable<RecordedData>
{
	public SolutionBase solution;
	//public ProblemBase problem;
	public String problemName;
	public AlgorithmBase algorithm;
	public long iteration;
	//public boolean isMemberOfParetoSolution = false;
	public MOSolutionBase[] paretoFront = null;
	
	// Constructors:
	public RecordedData(MOSolutionBase solution)
	{
		this.solution = solution;
	}
	public RecordedData(double[] objectives)
	{
		MOSolutionBase debug = new MOSolutionBase(objectives.length);
		for (int i=0; i<objectives.length; i++)
			debug.setObjective(i, objectives[i]);
		this.solution = debug;
		
	}
	public RecordedData(SolutionBase solution, String problemName)
	{
		this.solution = solution;
		this.problemName = problemName;
	}
	public RecordedData(SolutionBase solution, String problemName, AlgorithmBase algorithm)
	{
		this.solution = solution;
		this.problemName = problemName;
		this.algorithm = algorithm;
	}
	public RecordedData(SolutionBase sol, String problemName, AlgorithmBase algorithm, long iteration)
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
