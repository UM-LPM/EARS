package org.um.feri.ears.experiment.so.tk;

import java.util.ArrayList;
import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

//Izvorna koda avtorjev spremenjena v Javo
//Vir1: https://www.mathworks.com/matlabcentral/fileexchange/54181-lightning-search-algorithm--lsa-
//Vir2: http://www.mathworks.com/matlabcentral/fileexchange/54181-lightning-search-algorithm--lsa-/content/Standard%20LSA/LSA_Main.m

public class LSA extends Algorithm 
{
	//pop size
	int N;

	//st dimenzij
	int D;

	//populacija
	ArrayList<LSASolution> pop;

	LSASolution best, worst;

	int ch_time;
	int max_ch_time;

	public LSA(int pop_size, int max_time) 
	{
		super();
		this.N = pop_size;
		this.max_ch_time = max_time;
		
		ai = new AlgorithmInfo("Lightning Search Algorithm",
				"H. Shareef, A.H. Mutlag, Lightning Search Algorithm, Applied Soft Computing, 2015",
				"LSA",
				"Phsyics-based metaheuristics algorithm");    
		ai.addParameter(EnumAlgorithmParameters.POP_SIZE,N + "");

		au =  new Author("Tadej Klakocer", "tadej.klakocer@student.um.si");
	}

	public LSA()
	{
		this(50, 10);
	}

	@Override
	public DoubleSolution execute(Task taskProblem) throws StopCriteriaException 
	{
		D = taskProblem.getDimensions();
		
		int iter = 0;

		double max_iter = (taskProblem.getMaxEvaluations() - N) / N;  //-initpopulation!

		ch_time = 0;

		double[] direct = new double[N];

		for(int i=0;i<D;i++)
		{
			direct[i] = Util.nextInt(3) - 1.0 ; // -1, 0, 1	
		}

		//inicializiramo populacijo
		initPopulation(taskProblem);

		//glavna zanka algoritma
		while (!taskProblem.isStopCriteria())
		{
			//eval population, prva iteracij je že evaluirana
			if(iter > 0)
			{
				//tu se zgubi par evalvacij, ker so pri zamenjavi nekateri že pravilno ocenjeni 
				//mogoèe potreba po svojem razredu z dodatnimi spremenljivkami - optimalno
				Evaluate(taskProblem);
			}

			//update channel
			UpdateChannel(taskProblem);

			// Ranking the fitness value
			//[Ms ds]=sort(Ec,'ascend');
			//Collections.sort(pop, 
			//	    Comparator.comparingDouble(Individual::getEval));

			//set worst, best 
			//reset worst
			worst = pop.get(0);

			for (int i=0; i<N; i++) 
			{	
				//global best set
				if (taskProblem.isFirstBetter(pop.get(i), best)) 
				{
					best = pop.get(i);
				}

				//worst set
				if (taskProblem.isFirstBetter(worst, pop.get(i))) 
				{
					worst = pop.get(i);					
				}					
			}


			double Energy = 2.05 - 2.0 * Math.exp(-5.0 * (max_iter - iter ) / max_iter);// Update energy

			// update direction
			direct = UpdateDirect(taskProblem, direct);

			// update position
			UpdatePositions(taskProblem, direct, Energy);

			//po psevdokodu iz èlanka
			if(best.getEval() == worst.getEval())
			{ 		
				//System.out.println("best == worst");
				break;
			}

			iter++;

			if (taskProblem.isStopCriteria()) 
				break;
			
		}

		//% select the optimal value
		//grandmin = min(fitness);	
		worst = pop.get(0);

		//ni potrebno , sem pregledal in je pred tem enak best individual
		for(int i=0;i<N;i++)
		{
			if(taskProblem.isFirstBetter(pop.get(i), best))
				best = pop.get(i);

			if(taskProblem.isFirstBetter(worst, pop.get(i)))
				worst = pop.get(i);
		}

		return best;
	}

	//eval fitnes populacije
	public void Evaluate(Task taskProblem) throws StopCriteriaException
	{
		for (int i=0;i<N;i++)
		{		
			if (taskProblem.isStopCriteria()) break;	
	
			LSASolution tmp = new LSASolution(taskProblem.eval(pop.get(i).getNewVariables()));
			pop.set(i, tmp);
		
			//isce best
			if (taskProblem.isFirstBetter(pop.get(i), best))
				best = pop.get(i);			
		}
	}

	//update channel funkcija
	public void UpdateChannel(Task taskProblem)
	{
		int best_indeks = 0;
		int worst_indeks = 0;

		for(int i=0;i<N;i++)
		{
			if(taskProblem.isFirstBetter(pop.get(i), pop.get(best_indeks)))
			{
				best_indeks = i;
				best = pop.get(i);
			}
			if(taskProblem.isFirstBetter(pop.get(worst_indeks), pop.get(i)))
				worst_indeks = i;

		}

		if (ch_time >= max_ch_time)
		{
			//System.out.println("channel update");
			//[Ms ds]=sort(Ec,'ascend');
			//	Collections.sort(pop, 
			//    Comparator.comparingDouble(Individual::getEval));

			//Dpoint(ds(N),:) = Dpoint(ds(1),:); % Eliminate the worst channel
			//Ec(ds(N)) = Ec(ds(1)); % Update 

			pop.set(worst_indeks, pop.get(best_indeks));
			ch_time = 0; // reset
		}

		ch_time = ch_time + 1;
	}

	//kreiranje zaèetne populacije
	private void initPopulation(Task taskProb) throws StopCriteriaException
	{
		pop = null;
		pop = new ArrayList<>();
		best = null;
		worst = null;

		for (int i=0; i<N; i++) 
		{	
			pop.add(new LSASolution(taskProb));

			//best set
			if (i==0) 
				best = pop.get(0);
			else if (taskProb.isFirstBetter(pop.get(i), best)) 
				best = pop.get(i);

			//worst set
			if(i==0)
				worst = pop.get(0);
			else if(taskProb.isFirstBetter(worst, pop.get(i)))
				worst = pop.get(i);


			if (taskProb.isStopCriteria()) break;
		}
	}

	//update direction
	private double[] UpdateDirect(Task taskProb, double[] direct) throws StopCriteriaException
	{
		double [] new_direct = new double[direct.length];

		for (int d = 0;d<D;d++)
		{
			if (taskProb.isStopCriteria()) break;

			//Dpoint_test = Dpoint(ds(1),:);
			//Dpoint_test(d) = Dpoint_test(d)+direct(d)*0.005*(UB(d)-LB(d));
			//fv_test = evaluateF(Dpoint_test,F_index);
			//if fv_test < best // If better, +ve direction
			//   direct(d) = direct(d);
			//else
			//   direct(d) = -1*direct(d);

			double[] y = best.getNewVariables();

			y[d] = y[d] + direct[d] * 0.005 * (taskProb.getInterval()[d]);

			//eval
			LSASolution test = new LSASolution(taskProb.eval(y));

			if(taskProb.isFirstBetter(test, best))
			{
				new_direct[d] = direct[d];
			}
			else
			{
				new_direct[d] = direct[d] * (-1.0);
			}	   
		}

		return new_direct;
	}

	//update position of lightning strikes
	private void UpdatePositions(Task taskProb, double [] direct, double Energy) throws StopCriteriaException
	{
		for (int i = 0; i < N;i++)
		{
			if (taskProb.isStopCriteria()) break;

			//dist=Dpoint(i,:)-Dpoint(ds(1),:);

			//temp x
			double [] tmp_x = new double[D];

			for (int d = 0;d<D;d++)
			{
				double dist = pop.get(i).getVariables().get(d) - best.getVariables().get(d); //pop.get(best_indeks).getX()[d];

				//if Dpoint(i,:)==Dpoint(ds(1),:)
				if(pop.get(i).getVariables().get(d) == best.getVariables().get(d)) //pop.get(best_indeks).getX()[d])
				{
					tmp_x[d] = pop.get(i).getVariables().get(d) + direct[d] * Math.abs(normrnd(0, Energy));
					//Dpoint_temp(d) = Dpoint(i,d)+direct(d)*abs(normrnd(0,Energy));
				}
				else
				{
					if(dist < 0)
					{
						//Dpoint_temp(d) = Dpoint(i,d)+exprnd(abs(dist(d)));			
						tmp_x[d] = pop.get(i).getVariables().get(d) + exprnd(Math.abs(dist));
					}
					else
					{
						//Dpoint_temp(d) = Dpoint(i,d)-exprnd(dist(d));
						tmp_x[d] = pop.get(i).getVariables().get(d) - exprnd(dist);
					}
				}

				if ((tmp_x[d] >  taskProb.getUpperLimit()[d]) || (tmp_x[d] < taskProb.getLowerLimit()[d]))
				{
					tmp_x[d] = Util.nextDouble() * taskProb.getInterval()[d] + taskProb.getLowerLimit()[d];
					//Dpoint_temp(d) = rand(1)*(UB(d)-LB(d))+LB(d); % Re-initialized
				}
			}//end dimenzija spreminjanje

			if (taskProb.isStopCriteria()) break;

			LSASolution tmp = new LSASolution(taskProb.eval(tmp_x));

			//fv = evaluateF(Dpoint_temp,F_index);
			//  if (fv < Ec(i))
			if (taskProb.isFirstBetter(tmp, pop.get(i)))//fv < Ec(i))
			{
				//Dpoint(i,:) = Dpoint_temp;
				//Ec(i) = fv;
				//tmp.setEvalviran(true);
				pop.set(i, tmp);

				//Forking procedure
				if (Util.nextDouble() < 0.01)
				{
					//System.out.println("forking");

					double [] fock_x = new double[D];

					for( int d = 0;d<D;d++)
					{
						//Dpoint_fock(d) = UB(d)+LB(d)-Dpoint_temp(d);// Forking
						fock_x[d] = taskProb.getUpperLimit()[d] + taskProb.getLowerLimit()[d] - tmp_x[d];

					}//end for dimenzije

					if (taskProb.isStopCriteria()) break;
					LSASolution fork_tmp = new LSASolution(taskProb.eval(fock_x));
					//         if fock_fit < Ec(i) 
					//               Dpoint(i,:) = Dpoint_fock; % Replace the channel
					//                      Ec(i) = fock_fit;
					//                  end
					if(taskProb.isFirstBetter(fork_tmp, pop.get(i)))
					{
						pop.set(i, fork_tmp); //replace the channel
					}

				}//end   
			}//end if
		}
	}

	private static double spare;
	private static boolean isSpareReady = false;

	//normrnd matlab funkcija
	public static synchronized double normrnd(double mean, double stdDev) {
		if (isSpareReady) {
			isSpareReady = false;
			return spare * stdDev + mean;
		} else {
			double u, v, s;
			do {
				u = Math.random() * 2 - 1;
				v = Math.random() * 2 - 1;
				s = u * u + v * v;
			} while (s >= 1 || s == 0);
			double mul = Math.sqrt(-2.0 * Math.log(s) / s);
			spare = v * mul;
			isSpareReady = true;
			return mean + stdDev * u * mul;
		}
	}

	//exprnd matlab funkcija
	public double exprnd(double mu)
	{
		return -Math.log(Util.nextDouble()) * mu;
	}

	@Override
	public void resetDefaultsBeforNewRun() {

	}

}
