package org.um.feri.ears.algorithms.so.rmo;

import java.util.*;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

public class RMO extends Algorithm
{
	//Algorithm parameters
	int pop_size;
	double C1, C2, k;
	double[][] X;
	double[] cp;
	DoubleSolution cp_s;
	double[][] V;
	
	public RMO(int pop_size, double C1, double C2, double k)
	{
		super();
		this.pop_size = pop_size;
		this.C1 = C1;
		this.C2 = C2;
		this.k = k;
		
		ai = new AlgorithmInfo("R.Rahmani","@article{rahmani2014new,title={A new simple, fast and efficient algorithm for global optimization over continuous search-space problems: Radial movement optimization},author={Rahmani, Rasoul and Yusof, Rubiyah},journal={Applied Mathematics and Computation},volume={248},pages={287--300},year={2014},publisher={Elsevier}}","RMO","RMO paper");  //EARS add algorithm name
        au =  new Author("Luka", "luka.horvat@student.um.si"); //EARS author info
	}
	
	public RMO()
	{
		super();
		
		this.pop_size = 100;
		this.C1 = 0.7;
		this.C2 = 0.8;
		this.k = 10;
		
		ai = new AlgorithmInfo("R.Rahmani","@article{rahmani2014new,title={A new simple, fast and efficient algorithm for global optimization over continuous search-space problems: Radial movement optimization},author={Rahmani, Rasoul and Yusof, Rubiyah},journal={Applied Mathematics and Computation},volume={248},pages={287--300},year={2014},publisher={Elsevier}}","RMO","RMO paper");  //EARS add algorithm name
        au =  new Author("Luka", "luka.horvat@student.um.si"); //EARS author info
	}
	
	@Override
	public DoubleSolution execute(Task taskProblem) throws StopCriteriaException {
		
		double[] globalBest = null;
		DoubleSolution globalBest_s = null;
		
		//Generate initial pop
		X = new double[pop_size][taskProblem.getNumberOfDimensions()];
		V = new double[pop_size][taskProblem.getNumberOfDimensions()];
		
		for (int i = 0; i < pop_size; ++i)
		{
			//Random values
			for (int j = 0; j < X[i].length; ++j)
			{
				X[i][j] = Util.nextDouble(taskProblem.getLowerLimit()[j], taskProblem.getUpperLimit()[j]);
			}

			DoubleSolution eval = taskProblem.eval(X[i]);
			
			//Pick best starting center
			if (i == 0)
			{
				cp = X[i];
				cp_s = new DoubleSolution(eval);
				
				//System.out.println(taskProblem.getNumberOfEvaluations()+" "+ cp_s);
			}
			else if (taskProblem.isFirstBetter(eval,  cp_s))
			{
				cp = X[i];
				cp_s = new DoubleSolution(eval);
				
			}
			
			if (taskProblem.isStopCriteria())
			{
				return cp_s;
			}
			
		}
		
		
		cp = cp.clone();
		
		//Main loop
		while (!taskProblem.isStopCriteria())
		{
			//Calculate W
			double W = 1.0 - (1.0/taskProblem.getMaxEvaluations()) * taskProblem.getNumberOfEvaluations();
			//W = 1;
			double[] currentBest = null;
			DoubleSolution currentBest_s = null;
			
			//Calculate velocity vectors and move particles
			for (int i = 0; i < pop_size; ++i)
			{
				for (int j = 0; j < V[i].length; ++j)
				{
					//Velocity vector
					V[i][j] = Util.nextDouble(-1, 1) * ((taskProblem.getUpperLimit()[j] - taskProblem.getLowerLimit()[j]) / k);//Util.nextDouble(taskProblem.getLowerLimit()[j], taskProblem.getUpperLimit()[j]) / 100.0;
					
					//Move particle and check constrains
					X[i][j] = taskProblem.setFeasible(V[i][j]*W + cp[j], j);
				}
				
				DoubleSolution eval;
				if (taskProblem.isStopCriteria())
				{
					if(globalBest_s != null)
						return globalBest_s;
					else
						return currentBest_s;
				}
				eval = taskProblem.eval(X[i]);

				
				//Check if particles is better
				if (i == 0)
				{
					currentBest_s = eval;
					currentBest = X[i];
				}
				else if (taskProblem.isFirstBetter(eval, currentBest_s))
				{
					currentBest_s = eval;
					currentBest = X[i];
				}				
			}
			
			//Move the center 
			if (globalBest == null)
			{
				for (int j = 0; j < cp.length; ++j)
				{
					cp[j] = cp[j] + C2 * (currentBest[j] - cp[j]);
				}
				
				globalBest = currentBest.clone();
				globalBest_s = new DoubleSolution(currentBest_s);
			}
			else
			{
				for (int j = 0; j < cp.length; ++j)
				{
					cp[j] = cp[j] + C1 * (globalBest[j] - cp[j]) + C2 * (currentBest[j] - cp[j]);
				}
				
				if (taskProblem.isFirstBetter(currentBest_s, globalBest_s))
				{
					globalBest = currentBest.clone();
					globalBest_s = new DoubleSolution(currentBest_s);
					
					//System.out.println(taskProblem.getNumberOfEvaluations()+" "+ globalBest_s);
				}
			}
			
			taskProblem.incrementNumberOfIterations();
		}
		return globalBest_s;
	}

	@Override
	public void resetToDefaultsBeforeNewRun() {
		
	}

}