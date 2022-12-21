package org.um.feri.ears.examples;

import java.util.ArrayList;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.so.woa.WOA;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.constrained.RealWorldCompressionSpringDesign;
import org.um.feri.ears.problems.constrained.RealWorldPressureVesselDesign;

public class EvaluateConstrainedProblemsExample {
	
	public static void main(String[] args) {

		RealWorldCompressionSpringDesign springProblem = new RealWorldCompressionSpringDesign();
		RealWorldPressureVesselDesign vesselProblem = new RealWorldPressureVesselDesign();
		ArrayList<NumberSolution<Double>> bests = TestWOAWithProblem(springProblem,5);
		
		System.out.println("Done");
	}
	
	public static ArrayList<NumberSolution<Double>> TestWOAWithProblem(Problem prob, int repetitions) {
		ArrayList<NumberSolution<Double>> listOfBests = new ArrayList<>();
		for(int i = 0; i < repetitions; i++) {
			Task problem = new Task(prob, StopCriterion.EVALUATIONS, 10000, 0, 0);
			int populationSize = 30;
			Algorithm woa = new WOA(populationSize, false);
			try {
				NumberSolution<Double> best = woa.execute(problem);
				listOfBests.add(best);
				System.out.println("Best #"+ i+":" + best.getEval());
			} catch(StopCriterionException e) {
				e.printStackTrace();
			}
		}
		
		return listOfBests;
	}
}
