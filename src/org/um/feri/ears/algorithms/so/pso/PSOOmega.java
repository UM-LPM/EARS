package org.um.feri.ears.algorithms.so.pso;

import java.util.ArrayList;


import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

public class PSOOmega extends Algorithm  {

	int pop_size;
	ArrayList<MyPSOSolution> x;
	Task task;
	MyPSOSolution g; //blobal best
	double omega, phiG, phiP;
	public PSOOmega() {
		this(10,0.7, 2, 2);
	}

	public PSOOmega(int pop_size, double om, double p1, double p2) {
		super();
		this.pop_size = pop_size;
		this.omega = om;
		this.phiP = p1;
		this.phiG = p2;
	    setDebug(debug);  //EARS prints some debug info
        ai = new AlgorithmInfo("Wiki","Wiki Omega","PSOomega","Wiki PSOOmega change in formula");  //EARS add algorithm name
        ai.addParameter(EnumAlgorithmParameters.POP_SIZE, pop_size + "");
        ai.addParameter(EnumAlgorithmParameters.C1, p1 + "");
        ai.addParameter(EnumAlgorithmParameters.C2, p2 + "");
        ai.addParameter(EnumAlgorithmParameters.UNNAMED1, om + "");
        //ai.addParameter(EnumAlgorithmParameters., F + "");
        au =  new Author("jaz 2", "Srednji M"); //EARS author info
	}
	
	@Override
	public DoubleSolution execute(Task taskProblem) throws StopCriteriaException {
		task = taskProblem;
		initPopulation();
		double rp, rg;
		double v[];
		while (!task.isStopCriteria()) {
			for (int i=0; i<pop_size; i++) {
				rp = Util.rnd.nextDouble();
				rg = Util.rnd.nextDouble();
				v = new double[task.getNumberOfDimensions()];
				// r*vec(x) double r = Util.rnd.nextDouble();
				for (int d=0; d<task.getNumberOfDimensions(); d++) {
					//http://www.atscience.org/IJISAE/article/view/7
					//omega different formula omega multiplies with 
					v[d] = omega*(
							x.get(i).getV()[d]+
							phiP* Util.rnd.nextDouble()*(x.get(i).getP().getValue(d)-x.get(i).getValue(d))+
							phiG* Util.rnd.nextDouble()*(g.getValue(d)-x.get(i).getValue(d)));
					//if (v[d]>(taskProblem.getIntervalLength()[d])) v[d]=taskProblem.getIntervalLength()[d]; 
					//if (v[d]<(taskProblem.getIntervalLength()[d])) v[d]=-taskProblem.getIntervalLength()[d]; 
				}
				x.set(i, x.get(i).update(task,v));
				if (task.isFirstBetter(x.get(i), g)) g=x.get(i); 
				if (task.isStopCriteria()) break;
			}
			task.incrementNumberOfIterations();
		}
		return g;
	}
	
	private void initPopulation() throws StopCriteriaException {
		x = new ArrayList<>();
		for (int i=0; i<pop_size; i++) {
			x.add(new MyPSOSolution(task));
			if (i==0) g = x.get(0);
			else if (task.isFirstBetter(x.get(i), g)) g=x.get(i);
		}
	}

	@Override
	public void resetToDefaultsBeforeNewRun() {
		
	}
}