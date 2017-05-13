package org.um.feri.ears.algorithms.so.hc;

import java.util.ArrayList;
import java.util.Arrays;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;

public class HillClimbing extends Algorithm {
	String algName;
	String datoteka;
	double dxProcent;
    public HillClimbing(double dxProcent) {
		ai = new AlgorithmInfo(
				"HillClimbing",
				"HillClimbing",
				"HillClimbing", "HillClimbing"); // EARS add algorithm name
		this.dxProcent=dxProcent;
		au = new Author("Matej", "matej.crepinsek at um.si"); // EARS author
    }

    public static void print(int eval, DoubleSolution s,String a) {
    	System.out.println(eval+";"+s.getEval()+" "+Arrays.toString(s.getDoubleVariables())+a);
    }
	@Override
	public DoubleSolution execute(Task task) throws StopCriteriaException {
		DoubleSolution bestGlobal = task.getRandomSolution();
		DoubleSolution best = new DoubleSolution(bestGlobal); //clone
		DoubleSolution tmpSolution;
		double interval[] = task.getInterval();
		ArrayList<DoubleSolution> list = new ArrayList<>();
		double tmp[],  x[], bst[] ;
		boolean better=false;
		while (!task.isStopCriteria()) {
			while (!task.isStopCriteria()) { //is improvement
				list.clear();
				bst = best.getDoubleVariables();
				better=false;
				for (int i=0; i< task.getNumberOfDimensions(); i++) {
					if (i==2) {
						//System.out.println("i:"+i);
					}
					x = Arrays.copyOf(bst, bst.length);
					x[i]+=interval[i]*dxProcent;
					tmpSolution = task.eval(x);
					if (task.isFirstBetter(tmpSolution, best)) {
						best = tmpSolution;
						print(task.getNumberOfEvaluations(),best,i+"");
						better=true;
					}
					if (task.isStopCriteria()) break;
					x[i]-=interval[i]*dxProcent;
					tmpSolution = task.eval(x);
					if (task.isFirstBetter(tmpSolution, best)) {
						best = tmpSolution;
						print(task.getNumberOfEvaluations(),best,i+"");
						 better=true;
					}
					if (task.isStopCriteria()) break;
				}
				if (!better) break;
			}
			if (task.isFirstBetter(best,bestGlobal)) {
				bestGlobal = best;
			}
			if (task.isStopCriteria()) break;
			best = task.getRandomSolution();	
			System.out.println("New start");
		}
		return bestGlobal;
	}

	@Override
	public void resetDefaultsBeforNewRun() {
		
	}

}
