package org.um.feri.ears.examples;

import java.util.ArrayList;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.so.woa.WOA;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.constrained.RealWorldCompressionSpringDesign;
import org.um.feri.ears.problems.constrained.RealWorldPressureVesselDesign;

public class ConstrainedProblemsTest {
	
	public static void main(String[] args) {

		RealWorldCompressionSpringDesign springProblem = new RealWorldCompressionSpringDesign();
		RealWorldPressureVesselDesign vesselProblem = new RealWorldPressureVesselDesign();
		ArrayList<DoubleSolution> bests = TestWOAWithProblem(springProblem,5);
		
		System.out.println("Done");
	}
	
	public static ArrayList<DoubleSolution> TestWOAWithProblem(Problem prob, int repetitions) {
		ArrayList<DoubleSolution> listOfBests = new ArrayList<DoubleSolution>();
		for(int i = 0; i < repetitions; i++) {
			Task problem = new Task(StopCriterion.EVALUATIONS, 10000, 0, 0, 0.001, prob);
			int populationSize = 30;
			Algorithm woa = new WOA(populationSize, false);
			try {
				DoubleSolution best = woa.execute(problem);
				listOfBests.add(best);
				System.out.println("Best #"+ i+":" + best.getEval());
			} catch(StopCriterionException e) {
				e.printStackTrace();
			}
		}
		
		return listOfBests;
	}
}
