package org.um.feri.ears.algorithms.so.laf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.TaskComparator;
import org.um.feri.ears.util.Util;

public class LaF extends Algorithm {
	
	private boolean debug = true;
	private Task task;
	//FireflySolution best;
	ArrayList<DoubleSolution> leaders;
	ArrayList<DoubleSolution> followers;
	
	private int pop_size;
	
    double [] ub;
    double [] lb;
    int dimension;
    
	public LaF(int pop_size, int dimension) { 
		
		super();
		setDebug(debug);  //EARS prints some debug info
		ai = new AlgorithmInfo("LaF",
				"@article{Gonzalez-Fernandez2015,"
				+ "title={Leaders and Followers - A New Metaheuristic to Avoid the Bias of Accumulated Information},"
				+ "author={Gonzalez-Fernandez, Yasser and Chen, Stephen},"
				+ "proceedings={2015 IEEE Congress on Evolutionary Computation },"
				+ "pages={776--783},"
				+ "year={2015},"
				+ "publisher={IEEE press}}",
				"LaF", "Leaders and Followers");
		au =  new Author("alex", "shliu@mail.fresnostate.edu"); //EARS author info
		
		ai.addParameter(EnumAlgorithmParameters.POP_SIZE, pop_size + "");
		this.pop_size = pop_size;
	}
	
	public LaF() { 
		this(20, 10);	
		
	}
	
	@Override  
	public DoubleSolution execute(Task taskProblem) throws StopCriteriaException{ //EARS main evaluation loop 
		task = taskProblem;
		DoubleSolution best=null;
		dimension = task.getNumberOfDimensions();
		ub = task.getUpperLimit();
		lb = task.getLowerLimit();
		
		initPopulation();
		int leaderIndex = 0;
		int followerIndex = 0;
		while (!task.isStopCriteria()) {
			for (int i = 0; i < pop_size; i++)
			{
				if (task.isStopCriteria())
					break;
				leaderIndex = Util.nextInt(pop_size);
				DoubleSolution leader = leaders.get(leaderIndex);
				followerIndex = Util.nextInt(pop_size);
				DoubleSolution follower = followers.get(followerIndex);
				DoubleSolution trial = trial(leader, follower); //one fit eval here
				//System.out.println(trailCost);
				if (task.isFirstBetter(trial, followers.get(followerIndex))) //eval done earlier
					followers.set(followerIndex, trial);
			}
			if (task.isFirstBetter(findMedianSolution(followers), findMedianSolution(leaders))){
				leaders = merge(followers, leaders);
			}
			best = new DoubleSolution(leaders.get(findMinIndex(leaders)));
			System.out.println(best.getEval());
			task.incrementNumberOfIterations();
		}
		return best;

	}

	@Override
	public void resetToDefaultsBeforeNewRun() {
	}
	
	
	public void initPopulation() throws StopCriteriaException {
		leaders = new ArrayList<DoubleSolution>();
		followers = new ArrayList<DoubleSolution>();
		for (int i = 0; i < pop_size; i++) {
			DoubleSolution newLeader = new DoubleSolution(task.getRandomSolution());
			leaders.add(newLeader);
			if (task.isStopCriteria())
				break;
		}
		for (int i = 0; i < pop_size; i++) {
			DoubleSolution newFollower = new DoubleSolution(task.getRandomSolution());
			followers.add(newFollower);
			if (task.isStopCriteria())
				break;
		}
	}
	
	public DoubleSolution trial(DoubleSolution leader, DoubleSolution follower) throws StopCriteriaException
	{
		double maxStep = 2.0;
		double [] gap = new double [dimension];;
		double [] farthest = new double [dimension];
		double [] maxSteps = new double [dimension];
		double [] result =  new double[dimension];

		for (int i = 0; i < dimension; i++){
			maxSteps[i] = maxStep * 1;
		}
		for (int i = 0; i < dimension; i++){
		    gap[i] = leader.getValue(i) - follower.getValue(i);
		    farthest[i] = follower.getValue(i) + maxStep * gap[i];
		    if (farthest[i] < lb[i]){
		    	maxSteps[i] = (lb[i] - follower.getValue(i))/(ub[i]-lb[i]);
		    } 
		    if (farthest[i] > ub[i]){
		    	maxSteps[i] = (ub[i] - follower.getValue(i))/(ub[i]-lb[i]);
		    } 
		    double rand = Util.nextDouble();
		    result[i]= follower.getValue(i) + maxSteps[i] * rand * gap[i];
		}
		
		result = task.setFeasible(result); //fixes upper and lower bound
		return task.eval(result);
	}
	
	DoubleSolution findMedianSolution(ArrayList<DoubleSolution> mArrayList)
	{
		mArrayList.sort(new TaskComparator(task));
		return mArrayList.get(pop_size/2);
	}
	
	ArrayList<DoubleSolution> merge(ArrayList<DoubleSolution> followers, ArrayList<DoubleSolution> leaders)
	{
		ArrayList<DoubleSolution> merged = new ArrayList<DoubleSolution>(leaders);
		merged.addAll(followers); 
		boolean [] selected = new boolean [merged.size()];
		for (int i = 0; i < selected.length; i++){
			selected[i] = false;
		}
		
		ArrayList<DoubleSolution> selectedLeaders = new ArrayList<DoubleSolution>(pop_size);
		
		//add best solution 
		int min_index = findMinIndex(merged);
		selectedLeaders.add(merged.get(min_index));
		selected[min_index] = true;
		
		for (int i = 0; i < pop_size; i++)
		{
			int[] pick2 = randomSample(selected);
			int winner_index = compete(merged, pick2);
			selectedLeaders.add(merged.get(winner_index));
			selected[winner_index] = true;

		}
		return selectedLeaders;
	}
	
	//return the index of smallest eval
	int findMinIndex(ArrayList<DoubleSolution> newLeaders)
	{
		int i = 0;
		int min_index = i;
		for (i=1; i < newLeaders.size(); i++){
			if (task.isFirstBetter(newLeaders.get(i), newLeaders.get(min_index))){
				min_index = i;
			}
		}
		return min_index;
	}
	//return an array w/ two int that are not selected before
	int [] randomSample(boolean[] selected)
	{
		int[] pick2 = {0,0};
		int r1 = Util.nextInt(selected.length);
		while (selected[r1]){
			r1 = Util.nextInt(selected.length);
		}
		
		int r2 = Util.nextInt(selected.length);
		while (selected[r2]){
			r2 = Util.nextInt(selected.length);
		}
		pick2[0] = r1;
		pick2[1] = r2;
		return pick2;
	}
	
	//return the index that has smaller fitness eval
	int compete(ArrayList<DoubleSolution>newLeaders, int [] pick2)
	{
		int r1 = pick2[0];
		int r2 = pick2[1];
		if (task.isFirstBetter(newLeaders.get(r1), newLeaders.get(r2)))
			return r1;
		return r2;	
	}
}
