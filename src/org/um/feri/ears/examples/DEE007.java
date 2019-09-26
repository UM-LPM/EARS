package org.um.feri.ears.examples;

import java.util.ArrayList;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

public class DEE007 extends Algorithm {
    int pop_size;
    double CR, F;
	ArrayList<DoubleSolution> pop;
	DoubleSolution best;
	//Initialize all agents {\displaystyle \mathbf {x} } \mathbf {x}  with random positions in the search-space.
	public DEE007(int ps, double CR, double F, String s) {
		pop_size = ps;
		this.CR = CR;
		this.F = F;
		ai = new AlgorithmInfo("","",s,s);  //EARS add algorithm name
		au =  new Author("E007", "N/A"); //EARS author info

	}
	public void init(Task taskProblem) throws StopCriteriaException {
		pop = new ArrayList<>();
		DoubleSolution tmp;
		for (int i=0; i<pop_size;i++) {
			if (taskProblem.isStopCriteria()) break;
			tmp = taskProblem.getRandomSolution();
			if (i==0) best = tmp;
			else if (taskProblem.isFirstBetter(tmp, best)) best = tmp;
			pop.add(tmp);
		}
	   	
	}
	
	@Override
	public DoubleSolution execute(Task taskProblem) throws StopCriteriaException {
		init(taskProblem);
		int a, b, c, R;
		DoubleSolution yEval;
		while (!taskProblem.isStopCriteria()) {
			for (int i=0; i<pop_size;i++) {
				if (taskProblem.isStopCriteria()) break;
				do 
				  a = Util.rnd.nextInt(pop_size);
				while (a==i);
				do 
					  b = Util.rnd.nextInt(pop_size);
				while ((b==i)||(b==a));
				do 
					  c = Util.rnd.nextInt(pop_size);
				while ((c==i)||(c==a)||(c==b));
				R = Util.rnd.nextInt(taskProblem.getNumberOfDimensions());
				double y[] = new double[taskProblem.getNumberOfDimensions()];
				for (int j=0; j<taskProblem.getNumberOfDimensions(); j++) {
					if ((Util.nextDouble()<CR) || (j==R)) {
						y[j] = taskProblem.setFeasible(pop.get(a).getDoubleVariables()[j]+F*(pop.get(b).getDoubleVariables()[j]-pop.get(c).getDoubleVariables()[j]),j);
					} else y[j] = pop.get(i).getDoubleVariables()[j];
				}
				yEval = taskProblem.eval(y);
				if (taskProblem.isFirstBetter(yEval, pop.get(i))){
					pop.set(i, yEval);
					if (taskProblem.isFirstBetter(yEval, best)) best = yEval;
				}
			}
		}
		return best;
	}

	@Override
	public void resetToDefaultsBeforeNewRun() {
		// TODO Auto-generated method stub
		
	}

}
