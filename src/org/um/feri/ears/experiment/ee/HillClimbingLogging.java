package org.um.feri.ears.experiment.ee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.SolutionBase;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

public class HillClimbingLogging extends Algorithm {
	String algName;
	String datoteka;
	double dxProcent;
    public HillClimbingLogging(double dxProcent) {
		au = new Author("miha", "miha.ravber@um.si");
		ai = new AlgorithmInfo("HC", "Hill Climbing ",
				"@article{russell2002artificial," +
						"title={Artificial intelligence: a modern approach}," +
						"author={Russell, Stuart and Norvig, Peter}," +
						"year={2002}" +
						"}"
		);
    }

    public static void print(int eval, NumberSolution<Double> s, String a) {
    	System.out.println(eval+";"+s.getEval()+" "+Arrays.toString(Util.toDoubleArray(s.getVariables()))+a);
    }
	@Override
	public NumberSolution<Double> execute(Task task) throws StopCriterionException {
		NumberSolution<Double> bestGlobal = task.getRandomEvaluatedSolution();
		NumberSolution<Double> best = new NumberSolution<>(bestGlobal); //clone
		NumberSolution<Double> tmpSolution;
		double[] interval = task.getInterval();
		ArrayList<NumberSolution<Double>> list = new ArrayList<>();
		double[] tmp,  x, bst;
		boolean better=false;
		while (!task.isStopCriterion()) {
			while (!task.isStopCriterion()) { //is improvement
				list.clear();
				bst = Util.toDoubleArray(best.getVariables());
				better=false;
				for (int i=0; i< task.getNumberOfDimensions(); i++) {
					List<SolutionBase> parents = new ArrayList<>();
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
