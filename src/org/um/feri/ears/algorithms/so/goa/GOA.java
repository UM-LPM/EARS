package org.um.feri.ears.algorithms.so.goa;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.EnumStopCriteria;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.TaskComparator;

public class GOA extends Algorithm{
	
	DoubleSolution best;
	
	int pop_size;
	Task task;
	
	double cMax = 1;
	double cMin = 0.00001;
	
	
	ArrayList<DoubleSolution> population;
	ArrayList<DoubleSolution> offspringPopulation;
	
	public GOA()
	{
		this(20);
	}
	
	public GOA(int pop_size)
	{
		super();
		this.pop_size = pop_size;
		
		au = new Author("miha", "miha.ravber@um.si");
		ai = new AlgorithmInfo("GOA",
				"@article{mirjalili2017grasshopper,"
				+ "  title={Grasshopper Optimisation Algorithm: Theory and application},"
				+ "  author={Saremi, Shahrzad and Mirjalili, Seyedali and Lewis, Andrew},"
				+ "  journal={Advances in Engineering Software},"
				+ "  volume={105},"
				+ "  pages={30--47},"
				+ "  year={2017},"
				+ "  publisher={Elsevier}}",
				"GOA", "Grasshopper Optimisation Algorithm");
		ai.addParameter(EnumAlgorithmParameters.POP_SIZE, pop_size + "");
	}

	@Override
	public DoubleSolution execute(Task taskProblem) throws StopCriteriaException {
		task = taskProblem;
		initPopulation();
		int maxIt = 10000;
		if(task.getStopCriteria() == EnumStopCriteria.ITERATIONS)
		{
			maxIt = task.getMaxIteratirons();
		}
		
		if(task.getStopCriteria() == EnumStopCriteria.EVALUATIONS)
		{
			maxIt = task.getMaxEvaluations() / pop_size;
		}
		
		double dist,r_ij1,r_ij2;
		double[] ub = task.getUpperLimit();
		double[] lb = task.getLowerLimit();
		double eps = Math.pow(2,-52);
		while (!task.isStopCriteria()) {
			//TODO normalize variables
		    double c = cMax - (task.getNumberOfIterations()+2) * ((cMax - cMin) / maxIt); // Eq. (2.8) in the paper
			double xj_xi,s_ij1,s_ij2;
			double [] S_total;
			offspringPopulation = new ArrayList<DoubleSolution>();
			for(int i = 0; i < pop_size; i++)
			{
				double[] temp_i = population.get(i).getNewVariables();
				double[] newPosition = new double[task.getNumberOfDimensions()];
				S_total = new double[task.getNumberOfDimensions()];
				for(int k= 0; k < task.getNumberOfDimensions(); k+=2) {
					for(int j = 0; j < pop_size; j++)
					{
						if(i==j)
							continue;

						double[] temp_j = population.get(j).getNewVariables();


						dist = distance(temp_i[k],temp_i[k+1],temp_j[k],temp_j[k+1]); //Calculate the distance between two grasshoppers

						//xj-xi/dij in Eq. (2.7)
						r_ij1 = (temp_j[k] - temp_i[k]) / (dist + eps);
						r_ij2 = (temp_j[k+1] - temp_i[k+1]) / (dist + eps);

						xj_xi = 2 + dist % 2; // |xjd - xid| in Eq. (2.7) 

						s_ij1 = ((ub[k] - lb[k]) * c / 2) * functionS(xj_xi) * r_ij1;
						s_ij2 = ((ub[k+1] - lb[k+1]) * c / 2) * functionS(xj_xi) * r_ij2;
						S_total[k]+= s_ij1;
						S_total[k+1]+= s_ij2;
					}
				}

				for(int k = 0; k < task.getNumberOfDimensions(); k++) {
					newPosition[k] = c * S_total[k] + best.getValue(k);
				}
				
				newPosition = task.setFeasible(newPosition);
				if(task.isStopCriteria())
					break;
				DoubleSolution newGH = task.eval(newPosition);
				
				offspringPopulation.add(newGH);
			}
			
			population = offspringPopulation;
			for(DoubleSolution s : population)
			{
				if(task.isFirstBetter(s, best))
				{
					//System.out.println(s.getEval());
					//best = new DoubleSolution(s);
					best = s;
				}
			}
			//System.out.println("Iteration "+ (task.getNumberOfIterations()+2) +"  "+best.getEval() + " "+ c);
			
			task.incrementNumberOfIterations();
		}
		
		/*StringBuilder sb = new StringBuilder();
		for(ArrayList<DoubleSolution> pop : populationHistory)
		{
			sb.append(pop.toString());
			sb.append("\n");
		}
		
		try {
			FileOutputStream fos = new FileOutputStream("D:\\population_history.txt");
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);
			bw.write(sb.toString());
			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}*/
//		System.out.println(task.getNumberOfIterations());
		return best;
	}

	private double functionS(double r) {
		double f = 0.5, l = 1.5;
		return f * Math.exp(-r/l) - Math.exp(-r); //Eq. (2.3) in the paper
	}

	private double distance(double a1, double a2, double b1, double b2) {
		
		return Math.sqrt(Math.pow((a1 - b1),2) + Math.pow((a2 - b2),2));
	}

	private void initPopulation() throws StopCriteriaException {
		population = new ArrayList<DoubleSolution>();

		for (int i = 0; i < pop_size; i++) {
			if (task.isStopCriteria())
				break;
			DoubleSolution newSolution = task.getRandomSolution();
			population.add(newSolution);
		}
		
		population.sort(new TaskComparator(task));
		best = new DoubleSolution(population.get(0));
		
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
	public void resetDefaultsBeforNewRun() {
	}

}
