package org.um.feri.ears.algorithms;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collector.Characteristics;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.DoubleMOTask;
import org.um.feri.ears.problems.MOTask;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.TaskBase;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.qualityIndicator.QualityIndicator;
import org.um.feri.ears.util.Cache;
import org.um.feri.ears.util.Util;

public abstract class MOAlgorithm<T extends MOTask, Type extends Number> extends AlgorithmBase<T,ParetoSolution<Type>> {
	
	protected T task;
	protected static boolean optimalParam;
	
	private HashMap<String, Integer> positions = new HashMap<String, Integer>();  //stores the position of the current solution
	private HashMap<String, List<ParetoSolution<Type>>> solutions = new HashMap<String, List<ParetoSolution<Type>>>();
	private HashMap<String, Integer[]> permutations = new HashMap<String, Integer[]>();
	
	protected ParetoSolution<Type> returnNext(String taskString)
	{
		String key = getCacheKey(taskString);
		if(!solutions.containsKey(key))
		{
			List<ParetoSolution<Type>> paretoList = Util.<Type>readParetoListFromJSON(key, ai.getPublishedAcronym());
			if(paretoList != null)
			{
				solutions.put(key, Util.<Type>readParetoListFromJSON(key, ai.getPublishedAcronym()));
				positions.put(key, 0);
				Integer[] per = ArrayUtils.toObject(Util.randomPermutation(solutions.get(key).size()));
				permutations.put(key, per);
			}
			else
			{
				return null;
			}
		}

		if(caching == Cache.Random)
		{
			List<ParetoSolution<Type>> pareto = solutions.get(key);
			return pareto.get(Util.nextInt(pareto.size()));
		}
		if(caching == Cache.RoundRobin)
		{
			List<ParetoSolution<Type>> pareto = solutions.get(key);
			int index = positions.get(key);
			if(index >= pareto.size())
			{
				index = 0;
				positions.put(key, 0);
			}
			positions.put(key, positions.get(key) + 1);
			
			return pareto.get(index);
		}
		if(caching == Cache.RandomPermutation)
		{
			List<ParetoSolution<Type>> pareto = solutions.get(key);
			int index = positions.get(key);
			if(index >= pareto.size())
			{
				index = 0;
				positions.put(key, 0);
			}
			positions.put(key, positions.get(key) + 1);
			index = permutations.get(key)[index];
			return pareto.get(index);
		}
		
		return null;

	}

	public static boolean isRunWithOptimalParameters() {
		return optimalParam;
	}

	public static void setRunWithOptimalParameters(boolean runWithOptimalParameters) {
		MOAlgorithm.optimalParam = runWithOptimalParameters;
	}

	@Override
	public abstract ParetoSolution<Type> run(T taskProblem) throws StopCriteriaException;

}
