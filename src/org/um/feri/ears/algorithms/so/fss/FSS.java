/*
 * Copyright (c) 2011 Murilo Rebelo Pontes
 * murilo.pontes@gmail.com
 * 
 * GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)
 */
package org.um.feri.ears.algorithms.so.fss;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.EnumStopCriteria;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.qualityIndicator.MetricsUtil;
import org.um.feri.ears.util.Util;

import java.util.ArrayList;

public class FSS extends Algorithm{
	
	DoubleSolution best;
	
	int popSize; //school size
	Task task;
	
	
	//Parameters
	public static final double fish_weight_min = 1;
	public static final double fish_weight_max = 5000;
	
	public static final double step_individual_init = 1.0;
	public static final double step_individual_final = 0.00001;
	
	public static final double step_volitive_init = 1.0;
	public static final double step_volitive_final = 0.00001;
	
	
	ArrayList<FishSolution> school;
	
	public FSS()
	{
		this(100);
	}
	
	public FSS(int popSize)
	{
		super();
		this.popSize = popSize;
		
		au = new Author("miha", "miha.ravber@um.si");
		ai = new AlgorithmInfo("FSS",
				"@inproceedings{bastos2008novel,"
				+ "title={A novel search algorithm based on fish school behavior},"
				+ "author={Bastos Filho, Carmelo JA and de Lima Neto, Fernando B and Lins, Anthony JCC and Nascimento, Antonio IS and Lima, Marilia P},"
				+ "booktitle={Systems, Man and Cybernetics, 2008. SMC 2008. IEEE International Conference on},"
				+ "pages={2646--2651},"
				+ "year={2008},"
				+ "organization={IEEE}}",
				"FSS", "Fish School Search");
		ai.addParameter(EnumAlgorithmParameters.POP_SIZE, popSize + "");
	}

	@Override
	public DoubleSolution execute(Task taskProblem) throws StopCriteriaException {
		task = taskProblem;
		initPopulation();

		while (!task.isStopCriteria()) {
			double step_individual = 1.0;
			
			//TODO iterations and cpu time
			if(task.getStopCriteria() == EnumStopCriteria.EVALUATIONS) {
				
				step_individual = FSS.step_individual_init - (FSS.step_individual_init - FSS.step_individual_final)*((double)task.getNumberOfEvaluations()/(double)task.getMaxEvaluations());
			}
			
			individualOperator(step_individual);
			
			feedingOperator();
			
			double[] school_instinctive = colletiveInstinctiveOperator();
			
			//TODO iterations and cpu time
			double step_volitive = FSS.step_volitive_init - (FSS.step_volitive_init - FSS.step_volitive_final)*((double)task.getNumberOfEvaluations()/(double)task.getMaxEvaluations());
			
			individualOperator(step_individual);
			
			colletiveVolitiveOperator(step_volitive*(task.getUpperLimit()[0]- task.getLowerLimit()[0]),school_instinctive);
			
			task.incrementNumberOfIterations();
		}
		return best;
	}
	
	private int colletiveVolitiveOperator(double step_size, double[] school_instinctive) throws StopCriteriaException {
		
		double[] school_barycentre=new double[task.getNumberOfDimensions()];
		double[] sum_prod=new double[task.getNumberOfDimensions()];
		double sum_weight_now = 0;
		double sum_weight_past = 0;

		//clear
		for(int i=0;i<sum_prod.length;i++){
			sum_prod[i]=0;
			school_barycentre[i]=0;
		}

		//for each fish contribute with your neighbor position and weight
		for(FishSolution fish: school){
			for(int i=0;i<fish.delta_x.length;i++){
				sum_prod[i]+= fish.neighbor.getValue(i) * fish.weight_now;
			}
			//sum weight
			sum_weight_now+=fish.weight_now;
			sum_weight_past+=fish.weight_past;
		}
		//calculate barycentre
		for(int i=0;i<sum_prod.length;i++){
			school_barycentre[i]=sum_prod[i]/sum_weight_now;
		}

		double direction=0;
		if(sum_weight_now>sum_weight_past){
			//weight gain -> contract
			direction=1;
		} 
		else {
			//weight loss -> dilate
			direction=-1;
		}
		
		int count_success=0;
		for(FishSolution fish: school) {
			
			double[] newSolution = fish.neighbor.getDoubleVariables();
			
			double de = 0.0;
			
			try {
				de = MetricsUtil.distance(newSolution, school_barycentre);
			} catch (Exception e) {
				e.printStackTrace();
			}

			//take care about zero division
			if(de!=0){
				
				for(int i = 0;i < task.getNumberOfDimensions(); i++){

					//continue to update neighbor with dilate/shrink
					newSolution[i] += ( step_size * direction * Util.nextDouble() * (newSolution[i] - school_barycentre[i]) ) / de;
				}
				
				//take care about bounds of search space
				newSolution = task.setFeasible(newSolution);
				
				//evaluate new current solution
				if(task.isStopCriteria())
					return count_success;
				
				fish.neighbor = task.eval(newSolution);

				//update current if neighbor is best
				fish.volitive_move_success = false;
				if(task.isFirstBetter(fish.neighbor, fish.current)){
					fish.current = new DoubleSolution(fish.neighbor);
					fish.volitive_move_success = true;
					count_success++;
				}

				//if need replace best solution
				if(task.isFirstBetter(fish.current, fish.best)){
					fish.best = new DoubleSolution(fish.current);
				}
				
				if(task.isFirstBetter(fish.best, best)){
					best = new DoubleSolution(fish.best);
				}
				
			} else {
				//warning user
				System.err.println("bypass volitive operator (zero division)");
			}
		}

		return count_success;
		
	}

	private double[] colletiveInstinctiveOperator() {

		double[] school_instinctive = calculateInstinctiveDirection();

		for(FishSolution fish: school) {
			//use current as template to neighbor
			fish.neighbor = new DoubleSolution(fish.current);

			//update neighbor with instinctive direction
			for(int i = 0;i < task.getNumberOfDimensions(); i++){
				fish.neighbor.setValue(i, fish.neighbor.getValue(i) + school_instinctive[i]);
			}
		}

		return school_instinctive;
	}

	private double[] calculateInstinctiveDirection() {
		
		double[] school_instinctive = new double[task.getNumberOfDimensions()];
		double[] sum_prod = new double[task.getNumberOfDimensions()];
		double sum_fitness_gain = 0;


		//for each fish contribute with your direction scaled by your fitness gain
		for(FishSolution fish: school){
			//only good fishes
			if(fish.individual_move_success){
				//sum product of solution by fitness gain 
				for(int i=0; i < fish.delta_x.length; i++){
					sum_prod[i]+= fish.delta_x[i] * fish.fitness_gain_normalized;
				}
				//sum fitness gains
				sum_fitness_gain+=fish.fitness_gain_normalized;
			}
		}

		//calculate global direction of good fishes
		for(int i=0;i<sum_prod.length;i++){
			//take care about zero division
			if(sum_fitness_gain!=0){
				school_instinctive[i]=sum_prod[i]/sum_fitness_gain;
			} 
			else {
				school_instinctive[i]=0;
			}
		}
		return school_instinctive;
	}

	private void feedingOperator() {
		
		//sort school by fitness gain
		school.sort(new FSSComparatorByFitnessGain());

		//max absolute value of fitness gain
		double abs_delta_f_max=Math.abs(school.get(0).delta_f);
		double abs_delta_f_max2=Math.abs(school.get(school.size()-1).delta_f);
		if(abs_delta_f_max2>abs_delta_f_max) abs_delta_f_max=abs_delta_f_max2;

		//take care about zero division
		if(abs_delta_f_max!=0){
			//calculate normalized gain
			for(FishSolution fish: school){
				fish.fitness_gain_normalized = fish.delta_f/abs_delta_f_max;
			}

			//feed all fishes
			for(FishSolution fish: school) {
				//
				fish.weight_past = fish.weight_now;
				fish.weight_now += fish.fitness_gain_normalized;
				//take care about min and max weight
				if(fish.weight_now<FSS.fish_weight_min) fish.weight_now=FSS.fish_weight_min; 
				if(fish.weight_now>FSS.fish_weight_max) fish.weight_now=FSS.fish_weight_max; 
			}
		} 
		else {
			//warning user
			System.err.println("bypass feeding (zero division)");
		}
	}

	private int individualOperator(double step_size) throws StopCriteriaException{

		int count_success = 0;
		for(FishSolution fish: school){
			//use current as template for neighbor
			fish.neighbor = new DoubleSolution(fish.current);

			double[] newSolution = fish.neighbor.getDoubleVariables();
			
			for(int i = 0; i < task.getNumberOfDimensions(); i++){
				//calculate displacement 
				fish.delta_x[i]= Util.nextDouble(-1, 1) * step_size;
				//generate new solution in neighbor
				newSolution[i] += fish.delta_x[i];
			}
			
			//take care about bounds of search space 
			newSolution = task.setFeasible(newSolution);

			//evaluate new current solution
			if(task.isStopCriteria())
				return count_success;
			
			
			fish.neighbor = task.eval(newSolution);

			//calculate fitness difference
			fish.delta_f = fish.neighbor.getEval() - fish.current.getEval();

			//update current if neighbor is best
			fish.individual_move_success = false;
			if(task.isFirstBetter(fish.neighbor, fish.current)){
				fish.current = new DoubleSolution(fish.neighbor);
				fish.individual_move_success = true;
				count_success++;
			}

			//if need replace best solution
			if(task.isFirstBetter(fish.current, fish.best)){
				fish.best = new DoubleSolution(fish.current);
			}
			
			if(task.isFirstBetter(fish.best, best)){
				best = new DoubleSolution(fish.best);
			}
			
		}
		return count_success;
	}

	private void initPopulation() throws StopCriteriaException {
		school = new ArrayList<FishSolution>();

		for (int i = 0; i < popSize; i++) {
			if (task.isStopCriteria())
				break;
			FishSolution newSolution = new FishSolution(task.getRandomSolution());
			school.add(newSolution);
		}
		
		school.sort(new FSSComparatorByBestFitness(task));
		best = new DoubleSolution(school.get(0).current);
		
		/*StringBuilder sb = new StringBuilder();
		
		sb.append("[");
		for(int k = 0; k < pop_size; k++)
		{
			
			double[] var = population.get(k).getDoubleVariables();
			for (int i = 0; i < var.length; i++) {
				sb.append(var[i]);
				if(i+1 < var.length)
					sb.append("\t");
			}
			if(k+1 < pop_size)
				sb.append(";");

		}
		sb.append("]");
		System.out.println(sb.toString());*/
	}
	

	@Override
	public void resetToDefaultsBeforeNewRun() {
	}

}
