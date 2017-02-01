package org.um.feri.ears.algorithms;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collector.Characteristics;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.algorithms.moo.moead.MOEAD;
import org.um.feri.ears.graphing.recording.GraphDataRecorder;
import org.um.feri.ears.problems.DoubleMOTask;
import org.um.feri.ears.problems.MOTask;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.TaskBase;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.qualityIndicator.QualityIndicator;
import org.um.feri.ears.util.Cache;
import org.um.feri.ears.util.FutureResult;
import org.um.feri.ears.util.ParetoSolutionCache;
import org.um.feri.ears.util.ParetoWithEval;
import org.um.feri.ears.util.Ranking;
import org.um.feri.ears.util.Util;

public abstract class MOAlgorithm<T extends MOTask, Type extends Number> extends AlgorithmBase<T,ParetoSolution<Type>> {
	
	protected T task;
	protected static boolean optimalParam;
	
	protected ParetoSolution<Type> result;
	
	private HashMap<String, Integer> positions = new HashMap<String, Integer>();  //stores the position of the current solution
	private HashMap<String, List<ParetoSolution<Type>>> all_solutions = new HashMap<String, List<ParetoSolution<Type>>>();
	private HashMap<String, Integer[]> permutations = new HashMap<String, Integer[]>();
	private ParetoSolutionCache cache;
	private String benchmarkInCache;
	protected ParetoSolution<Type> best;
	protected int num_var;
	protected int num_obj;
	
	public Callable<FutureResult<T, Type>> createRunnable(final MOAlgorithm<T, Type> al, final T taskProblem) {

		Callable<FutureResult<T, Type>> aRunnable = new Callable<FutureResult<T, Type>>(){
			@Override
			public FutureResult<T, Type> call() throws Exception {
				
				long duration = System.nanoTime();
				
				GraphDataRecorder.SetContext(al,taskProblem);
				taskProblem.startTimer();
				ParetoSolution<Type> res = execute(taskProblem);
                GraphDataRecorder.SetParetoSolution(res);
				

				duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - duration);
                al.addRunDuration(duration, duration - task.getEvaluationTimeMs());
				FutureResult<T, Type> future = new FutureResult(al, res);
				
				return future;
			}

	    };
	    return aRunnable;
	}
	
	public ParetoSolution<Type> getLastResult()
	{
		return result;
	}
	
	protected ParetoSolution<Type> returnNext(MOTask task)
	{
		// load cache if empty or if different benchmark in cache
		if(cache == null || !benchmarkInCache.equals(task.getBenchmarkName()))
		{
			cache = Util.readParetoListFromJSON(ai.getPublishedAcronym(),task.getBenchmarkName());
			if(cache !=null)
			{
				for(Entry<String, List<ParetoWithEval>> s : cache.data.entrySet())
				{
					List<ParetoSolution<Type>> solutions = new ArrayList<ParetoSolution<Type>>();
					//List<List<double[]>> ps = cache.data.get(listID);
					
					List<ParetoWithEval> ps = s.getValue();
					
					for(ParetoWithEval pareto : ps)
					{
						ParetoSolution<Type> solution = new ParetoSolution<Type>(pareto.pareto.size());
						
						for(double[] obj : pareto.pareto)
						{
							MOSolutionBase<Type> sol = new MOSolutionBase<Type>(obj.length);
							sol.setObjectives(obj);
							solution.add(sol);
						}
						solution.setEvalForAllUnaryQIs(pareto.qiEval);
						
						solutions.add(solution);
					}
					all_solutions.put(s.getKey(), solutions);
					positions.put(s.getKey(), 0);
					Integer[] per = ArrayUtils.toObject(Util.randomPermutation(all_solutions.get(s.getKey()).size()));
					permutations.put(s.getKey(), per);
				}
				benchmarkInCache = task.getBenchmarkName();
			}
			else
			{
				System.out.println("Cache does not exists!");
				return null;
			}
		}
				
		String key = getCacheKey(task.taskInfo());
		
		if(all_solutions.containsKey(key))
		{
			if(caching == Cache.Random)
			{
				List<ParetoSolution<Type>> pareto = all_solutions.get(key);
				return pareto.get(Util.nextInt(pareto.size()));
			}
			if(caching == Cache.RoundRobin)
			{
				List<ParetoSolution<Type>> pareto = all_solutions.get(key);
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
				List<ParetoSolution<Type>> pareto = all_solutions.get(key);
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
		}
		System.out.println("No match in cache for key: "+key);
		return null;
	}

	public static boolean isRunWithOptimalParameters() {
		return optimalParam;
	}

	public static void setRunWithOptimalParameters(boolean runWithOptimalParameters) {
		MOAlgorithm.optimalParam = runWithOptimalParameters;
	}

	@Override
	public ParetoSolution<Type> execute(T taskProblem) throws StopCriteriaException
	{
		task = taskProblem;
		num_var = task.getNumberOfDimensions();
		num_obj = task.getNumberOfObjectives();
		
		//ai.addParameter(EnumAlgorithmParameters.POP_SIZE, populationSize+"");
		long initTime = System.currentTimeMillis();
		init();
		
		// check cache after initialization when all parameters are set
		if(caching != Cache.None && caching != Cache.Save)
		{
			ParetoSolution<Type> next = returnNext(task);
			if(next != null)
				return next;
			else
				System.out.println("No solution found in chache for algorithm: "+ai.getPublishedAcronym()+" on problem: "+task.getProblemName());
		}
		
		start();
		long estimatedTime = System.currentTimeMillis() - initTime;
		//System.out.println("Total execution time: "+estimatedTime + "ms");

		Ranking ranking = new Ranking(best);
		best = ranking.getSubfront(0);
		
		if(save_data)
		{
			String algName = this.getAlgorithmInfo().getPublishedAcronym();
			best.saveParetoImage(algName,task.getProblemName());
			best.printFeasibleFUN("FUN_"+algName);
			best.printVariablesToFile("VAR_"+algName);
			best.printObjectivesToCSVFile("FUN_"+algName);
		}
		if(display_data)
		{
			best.displayAllUnaryQulaityIndicators(task.getNumberOfObjectives(), task.getProblemFileName());
			best.displayData(this.getAlgorithmInfo().getPublishedAcronym(),task.getProblemName());
		}
		
		if(caching == Cache.Save)
		{
			try {
				best.evaluteWithAllUnaryQI(num_obj, taskProblem.getProblemFileName());
			} catch (Exception e) {
				e.printStackTrace();
			}
			Util.addParetoToJSON(getCacheKey(task.taskInfo()),task.getBenchmarkName(), ai.getPublishedAcronym(), best);
		}
		
		return best;
	}

	protected abstract void init() throws StopCriteriaException;
	protected abstract void start() throws StopCriteriaException;

}
