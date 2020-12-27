package org.um.feri.ears.experiment.ee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;

public class HillClimbingLogging extends Algorithm {
	String algName;
	String datoteka;
	double dxProcent;
    public HillClimbingLogging(double dxProcent) {
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
	public DoubleSolution execute(Task task) throws StopCriterionException {
		DoubleSolution bestGlobal = task.getRandomEvaluatedSolution();
		DoubleSolution best = new DoubleSolution(bestGlobal); //clone
		DoubleSolution tmpSolution;
		double interval[] = task.getInterval();
		ArrayList<DoubleSolution> list = new ArrayList<>();
		double tmp[],  x[], bst[] ;
		boolean better=false;
		while (!task.isStopCriterion()) {
			while (!task.isStopCriterion()) { //is improvement
				list.clear();
				bst = best.getDoubleVariables();
				better=false;
				for (int i=0; i< task.getNumberOfDimensions(); i++) {
					List<DoubleSolution> parents = new ArrayList<DoubleSolution>();
					if (i==2) {
						//System.out.println("i:"+i);
					}
					x = Arrays.copyOf(bst, bst.length);
					x[i]+=interval[i]*dxProcent;
					parents.add(bestGlobal);
					parents.add(best);
					tmpSolution = task.eval(x, parents);
					if (task.isFirstBetter(tmpSolution, best)) {
						best = tmpSolution;
						//print(task.getNumberOfEvaluations(),best,i+"");
						better=true;
					}
					parents.clear();
					parents.add(bestGlobal);
					parents.add(best);
					if (task.isStopCriterion()) break;
					x[i]-=interval[i]*dxProcent;
					tmpSolution = task.eval(x, parents);
					if (task.isFirstBetter(tmpSolution, best)) {
						best = tmpSolution;
						//print(task.getNumberOfEvaluations(),best,i+"");
						 better=true;
					}
					if (task.isStopCriterion()) break;
				}
				if (!better) break;
			}
			if (task.isFirstBetter(best,bestGlobal)) {
				bestGlobal = best;
			}
			if (task.isStopCriterion()) break;
			best = task.getRandomEvaluatedSolution();
			System.out.println("New start");
		}
		return bestGlobal;
	}

	@Override
	public void resetToDefaultsBeforeNewRun() {
		
	}

}
