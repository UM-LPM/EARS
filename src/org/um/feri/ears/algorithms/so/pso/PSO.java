package org.um.feri.ears.algorithms.so.pso;

import java.util.ArrayList;



import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.problems.DoubleMOTask;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

public class PSO extends Algorithm  {

	int pop_size;
	ArrayList<MyPSOSolution> pop_x; //population
	MyPSOSolution g; //global best
	double omega, phiG, phiP;
	Task task;
	public PSO() {
		this(10,0.7, 2, 2);
	}

	public PSO(int pop_size, double om, double p1, double p2) {
		super();
		this.pop_size = pop_size;
		this.omega = om;
		this.phiP = p1;
		this.phiG = p2;
	    setDebug(debug);  //EARS prints some debug info
        ai = new AlgorithmInfo("Wiki","Wiki","PSO","My Wiki PSO");  //EARS add algorithm name
        ai.addParameter(EnumAlgorithmParameters.POP_SIZE, pop_size + "");
        ai.addParameter(EnumAlgorithmParameters.C1, p1 + "");
        ai.addParameter(EnumAlgorithmParameters.C2, p2 + "");
        ai.addParameter(EnumAlgorithmParameters.UNNAMED1, om + "");
        //ai.addParameter(EnumAlgorithmParameters., F + "");
        au =  new Author("Matej", "matej.crepinsek at um.si"); //EARS author info
	}
	
	@Override
	public DoubleSolution execute(Task taskProblem) throws StopCriteriaException {
		task = taskProblem;
		initPopulation();
		//double rp, rg;
		double v[];
		while (!task.isStopCriteria()) {
			for (int i=0; i<pop_size; i++) {
				//rp = Util.rnd.nextDouble(); better to use vector of real numbers
				//rg = Util.rnd.nextDouble(); 
				v = new double[task.getNumberOfDimensions()];
				// r*vec(x) double r = Util.rnd.nextDouble();
				for (int d=0; d<task.getNumberOfDimensions(); d++) {
					v[d] = omega*(
							pop_x.get(i).getV()[d])+
							phiP* Util.rnd.nextDouble()*(pop_x.get(i).getP().getVariables()[d]-pop_x.get(i).getVariables()[d])+
							phiG* Util.rnd.nextDouble()*(g.getVariables()[d]-pop_x.get(i).getVariables()[d]);
					//if (v[d]>(taskProblem.getIntervalLength()[d])) v[d]=taskProblem.getIntervalLength()[d]; 
					//if (v[d]<(taskProblem.getIntervalLength()[d])) v[d]=-taskProblem.getIntervalLength()[d]; 
				}
				pop_x.set(i, pop_x.get(i).update(taskProblem,v));
				if (task.isFirstBetter(pop_x.get(i), g)) g = pop_x.get(i); 
				if (task.isStopCriteria()) break;
			}
			task.incrementNumberOfIterations();
		}
		return g;
	}
	
	private void initPopulation() throws StopCriteriaException {
		pop_x = new ArrayList<>();
		for (int i=0; i<pop_size; i++) {
			pop_x.add(new MyPSOSolution(task));
			if (i==0) g = pop_x.get(0);
			else if (task.isFirstBetter(pop_x.get(i), g)) g=pop_x.get(i);
			if (task.isStopCriteria()) break;
		}
	}

	@Override
	public void resetDefaultsBeforNewRun() {
		
	}
}