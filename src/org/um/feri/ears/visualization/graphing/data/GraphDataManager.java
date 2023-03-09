package org.um.feri.ears.visualization.graphing.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.visualization.graphing.recording.GraphDataRecorder;

public class GraphDataManager
{
	/**
	 * Supress warnings (e.g. Unknown populations for algorithms, etc.).
	 */
	public static boolean SuppressWarnings = false;
	
	
	/**
	 * Get data sets that are covered by given filters. Any "null" value will be understood
	 * as an unfiltered parameter.
	 * @param alg Pick all data sets for given algorithm.
	 * @param prob Pick all data sets for given problem.
	 * @param iteration Pick data for chosen iteration with given parameters.
	 * @return DataSets that are specified with given filter.
	 */
	@SuppressWarnings("rawtypes")
	public static GraphDataSet GetDataFor(Class alg, String prob, Integer iteration)
	{
		RecordedCombination[] all = GraphDataRecorder.GetAllRecordedCombinations();
		GraphDataSet output = new GraphDataSet();
		Hashtable<String,ArrayList<RecordedData>> allSOdata = new Hashtable<String,ArrayList<RecordedData>>();
		
		
		for (int i=0; i<all.length; i++)
		{
			// Algorithm filter:
			if (alg == null || all[i].algorithm.getClass() == alg)
			{
				// Problem filter:
				if(prob == null || all[i].problemName == prob)
				{
					// SO:
					if (all[i].algorithm instanceof NumberAlgorithm)
					{
						if (!allSOdata.contains(all[i].algorithm.hashCode()+"_"+all[i].problemHashCode))
						{
							allSOdata.put(all[i].algorithm.hashCode()+"_"+all[i].problemHashCode, new ArrayList<RecordedData>());
						}
						ArrayList<RecordedData> tmp = allSOdata.get(all[i].algorithm.hashCode()+"_"+all[i].problemHashCode);
						int l = all[i].allRecords.size();
						for (int index=0; index<l; index++)
							tmp.addAll(all[i].allRecords.get(index));
					}
					// MO:
					else if (all[i].algorithm instanceof MOAlgorithm)
					{
						if (iteration == null)
						{
							int l = all[i].allRecords.size();
							for (int index=0; index<l; index++)
								output.add(all[i].allRecords.get(index).toArray(new RecordedData[0]));
						}
						else if (iteration <= all[i].allRecords.size())
						{
							output.add(all[i].allRecords.get(iteration).toArray(new RecordedData[0]));
						}
					}
				}
			}
		}

		Collection<ArrayList<RecordedData>> tmpColl = allSOdata.values();
		for (ArrayList<RecordedData> dataList : tmpColl)
		{
			output.add(dataList.toArray(new RecordedData[0]));
		}	
		
		return output;
	}
	@SuppressWarnings("rawtypes")
	public static GraphDataSet GetDataFor(Class alg, String prob)
	{
		RecordedCombination[] all = GraphDataRecorder.GetAllRecordedCombinations();
		GraphDataSet output = new GraphDataSet();
		Hashtable<String,ArrayList<RecordedData>> allSOdata = new Hashtable<String,ArrayList<RecordedData>>();
		
		
		for (int i=0; i<all.length; i++)
		{
			// Algorithm filter:
			if (alg == null || all[i].algorithm.getClass() == alg)
			{
				// Problem filter:
				if(prob == null || all[i].problemName == prob)
				{
					// SO:
					if (all[i].algorithm instanceof NumberAlgorithm)
					{
						if (!allSOdata.contains(all[i].algorithm.hashCode()+"_"+all[i].problemHashCode))
						{
							allSOdata.put(all[i].algorithm.hashCode()+"_"+all[i].problemHashCode, new ArrayList<RecordedData>());
						}
						ArrayList<RecordedData> tmp = allSOdata.get(all[i].algorithm.hashCode()+"_"+all[i].problemHashCode);
						int l = all[i].allRecords.size();
						for (int index=0; index<l; index++)
							tmp.addAll(all[i].allRecords.get(index));
					}
					// MO:
					else if (all[i].algorithm instanceof MOAlgorithm)
					{
						int l = all[i].allRecords.size();
						for (int index=0; index<l; index++)
							output.add(all[i].allRecords.get(index).toArray(new RecordedData[0]));
					}
				}
			}
		}
		
		Collection<ArrayList<RecordedData>> tmpColl = allSOdata.values();
		for (ArrayList<RecordedData> dataList : tmpColl)
		{
			output.add(dataList.toArray(new RecordedData[0]));
		}	
		
		return output;
	}
	
	
	@SuppressWarnings("unchecked")
	public static RecordedData[] GetParetoFront(RecordedData[] all)
	{
		// Is it a minimization or maximization problem?
		//MOProblem problem = (MOProblem)all[0].problem;
		//problem.
		ArrayList<RecordedData> out = new ArrayList<RecordedData>(Arrays.asList(all));
		NumberSolution sol1;
		NumberSolution sol2;
		int l = all.length;
		int ol = ((NumberSolution)all[0].solution).getObjectives().length;
		
		for (int i=0; i<l; i++)
		{
			sol1 = (NumberSolution)all[i].solution;
			for (int j=0; j<l; j++)
			{
				if (i==j || !out.contains(all[j]))
					continue;
				sol2 = (NumberSolution)all[j].solution;
				if (isFirstDominatedBySecond(sol1, sol2, ol))
				{
					if (out.contains(all[i]))
						out.remove(all[i]);
					break;
				}
			}
		}
		RecordedData[] out2 = out.toArray(new RecordedData[0]);
		Arrays.sort(out2, new RecordedDataComparatorForGraphing());
		return out2;
	}
	@SuppressWarnings("unchecked")
	public static RecordedData[] GetParetoFront(RecordedData[] all, int axisX, int axisY)
	{
//System.err.println("axisX: " + axisX + " axisY: " + axisY); //DEBUG
		// Is it a minimization or maximization problem?
		//MOProblem problem = (MOProblem)all[0].problem;
		//problem.
		ArrayList<RecordedData> out = new ArrayList<RecordedData>(Arrays.asList(all));
		NumberSolution sol1;
		NumberSolution sol2;
		int l = all.length;
		int[] axisArray = new int[]{axisX, axisY};
		
		for (int i=0; i<l; i++)
		{
			sol1 = (NumberSolution)all[i].solution;
			for (int j=0; j<l; j++)
			{
				if (i==j || !out.contains(all[j]))
					continue;
				sol2 = (NumberSolution)all[j].solution;
				if (isFirstDominatedBySecond(sol1, sol2, axisArray))	//if (isFirstDominatedBySecond(sol1, sol2, ol)) OLD: for all axis dominated space
				{
					if (out.contains(all[i]))
						out.remove(all[i]);
					break;
				}
			}
		}
		RecordedData[] out2 = out.toArray(new RecordedData[0]);
		Arrays.sort(out2, new RecordedDataComparatorForGraphing(axisX, axisY));
		return out2;
	}
	
	protected static boolean isFirstDominatedBySecond(NumberSolution sol1, NumberSolution sol2, int[] objectiveIndexes)
	{
		boolean allObjBetterOrEqual;
		int numOfBetterObj;
		allObjBetterOrEqual = true;
		numOfBetterObj = 0;
		
		for(int oi=0; oi<objectiveIndexes.length; oi++)
		{
			// MIN:
			// 1 dom.by 2 if: all 2<=1 && at least one 2<1.
			if(!(sol2.getObjective(objectiveIndexes[oi])<=sol1.getObjective(objectiveIndexes[oi])))
			{
				allObjBetterOrEqual=false;
				break;
			}
			else if (sol2.getObjective(objectiveIndexes[oi])<sol1.getObjective(objectiveIndexes[oi]))
			{
				numOfBetterObj++;
			}
		}
		// 2 IS NOT Pareto:
		if(numOfBetterObj<1 || !allObjBetterOrEqual)
		{
			return false;
		}
		// 2 DOMINATES 1:
		else
		{
			return true;
		}
	}
	
	
	protected static boolean isFirstDominatedBySecond(NumberSolution sol1, NumberSolution sol2, int ol)
	{
		boolean allObjBetterOrEqual;
		int numOfBetterObj;
		allObjBetterOrEqual = true;
		numOfBetterObj = 0;
		
		for(int oi=0; oi<ol; oi++)
		{
			// MIN:
			// 1 dom.by 2 if: all 2<=1 && at least one 2<1.
			if(!(sol2.getObjective(oi)<=sol1.getObjective(oi)))
			{
				allObjBetterOrEqual=false;
				break;
			}
			else if (sol2.getObjective(oi)<sol1.getObjective(oi))
			{
				numOfBetterObj++;
			}
		}
		// 2 DOMINATES 1:
		if(numOfBetterObj<1 || !allObjBetterOrEqual)
		{
			return false;
		}
		// 2 IS NOT Pareto:
		else
		{
			return true;
		}
	}
}
