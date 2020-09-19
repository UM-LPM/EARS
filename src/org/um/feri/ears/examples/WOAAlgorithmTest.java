package org.um.feri.ears.examples;

import java.util.ArrayList;
import java.util.List;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.so.es.ES1p1sAlgorithm;
import org.um.feri.ears.algorithms.so.woa.WOA;
import org.um.feri.ears.algorithms.so.gwo.GWO;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.EnumStopCriteria;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.constrained.RealWorldCompressionSpringDesign;
import org.um.feri.ears.problems.constrained.RealWorldPressureVesselDesign;
import org.um.feri.ears.problems.unconstrained.Sphere;
import org.um.feri.ears.util.FakeRandomGenerator;
import org.um.feri.ears.util.Util;

public class WOAAlgorithmTest {
	
	public static void main(String[] args) {
		Sphere sphereProblem = new Sphere(2);
		RealWorldCompressionSpringDesign springProblem = new RealWorldCompressionSpringDesign();
		RealWorldPressureVesselDesign vesselProblem = new RealWorldPressureVesselDesign();
		ArrayList<DoubleSolution> bests = TestWOAWithProblem(springProblem,5);
		
		System.out.println("Done");
	}
	
	public static ArrayList<DoubleSolution> TestWOAWithProblem(Problem prob, int repetitions) {
		ArrayList<DoubleSolution> listOfBests = new ArrayList<DoubleSolution>();
		for(int i = 0; i < repetitions; i++) {
			Task problem = new Task(EnumStopCriteria.EVALUATIONS, 10000, 0, 0, 0.001, prob);
			int populationSize = 30;
			boolean useFakeGenerator = false;
			Algorithm woa = new WOA(populationSize, useFakeGenerator, false);
			try {
				DoubleSolution best = woa.execute(problem);
				listOfBests.add(best);
				System.out.println("Best #"+ i+":" + best.getEval());
			} catch(StopCriteriaException e) {
				e.printStackTrace();
			}
		}
		
		return listOfBests;
	}
	
	public static void FakeRandomGeneratorTest() {
		Util.rnd.setSeed(System.currentTimeMillis());
		
		// Definicija problema 
		int maxEval = 10000;
		int numDimensions = 2;
		EnumStopCriteria stopCriteria = EnumStopCriteria.EVALUATIONS;
		Sphere problem = new Sphere(numDimensions);
		Task problem1 = new Task(stopCriteria, maxEval, 0, 0, 0.001, problem);
		Task problem2 = new Task(stopCriteria, maxEval, 0, 0, 0.001, problem);
		
		// Definicija algoritma za resevanje problema
		int populationSize = 30;
		boolean useFakeGenerator = false;
		Algorithm woa1 = new WOA(populationSize, useFakeGenerator, false);
		Algorithm woa2 = new WOA(populationSize, useFakeGenerator, false);
		
		// Optimizacija problema
		try {
		    DoubleSolution best_woa1 = woa1.execute(problem1);
		    DoubleSolution best_woa2 = woa2.execute(problem2);
		    
		    System.out.println("WOA 1 Best: "+ best_woa1.getEval()); 
		    System.out.println("WOA 2 Best: "+ best_woa2.getEval()); 
		    // WOA 1 Best: 1.5796177249618618E-6
		    // WOA 2 Best: 1.5796177249618618E-6
		} catch (StopCriteriaException e) {
		    e.printStackTrace();
		}
	}
}
