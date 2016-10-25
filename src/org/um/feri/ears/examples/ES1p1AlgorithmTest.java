package org.um.feri.ears.examples;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.so.es.ES1p1sAlgorithm;
import org.um.feri.ears.algorithms.so.random.RandomWalkAlgorithm;
import org.um.feri.ears.problems.EnumStopCriteria;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.unconstrained.ProblemEasom;
import org.um.feri.ears.problems.unconstrained.ProblemSphere;

public class ES1p1AlgorithmTest {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Task sphere=new Task(EnumStopCriteria.EVALUATIONS, 1000, 0, 0, 0.001, new ProblemSphere(2));
		Algorithm test = new ES1p1sAlgorithm(true);
		DoubleSolution best;
        try {
            best = test.execute(sphere);
            System.out.println("Best is:"+best);
        } catch (StopCriteriaException e) {
            e.printStackTrace();
        }
	}

}
