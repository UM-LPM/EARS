package org.um.feri.ears.examples;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.random.RandomWalkAMAlgorithm;
import org.um.feri.ears.problems.EnumStopCriteria;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.unconstrained.ProblemSphere;

public class RandomAlgorithmAMTest {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Task sphere=new Task(EnumStopCriteria.EVALUATIONS,1000,0.001,new ProblemSphere(4));
		Algorithm test = new RandomWalkAMAlgorithm();
		DoubleSolution best;
        try {
            best = test.run(sphere);
            System.out.println("Best is:"+best);
        } catch (StopCriteriaException e) {
            e.printStackTrace();
        }
	}

}
