package org.um.feri.ears.examples;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.so.cmaes.CMAES;
import org.um.feri.ears.algorithms.so.de.DEAlgorithm;
import org.um.feri.ears.algorithms.so.jade.JADE;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.EnumStopCriteria;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.unconstrained.cec2005.*;
import org.um.feri.ears.util.Util;

public class SOSingleRun {

	public static void main(String[] args) {
		Util.rnd.setSeed(System.currentTimeMillis());
		
		int numberOfDimensions = 10;
		Problem p = new F15(numberOfDimensions);
		
		double eval = p.eval(p.getOptimalVector()[0]);
		System.out.println(eval);
		System.out.println(p.getOptimumEval());

		Task sphere=new Task(EnumStopCriteria.EVALUATIONS, 100000 * numberOfDimensions, 0, 0, 0.001, p);
		//Algorithm test = new JADE(20, .4, .4); //30, .05, .1
		//Algorithm test = new JADE(30, .05, .1);
		//Algorithm test = new CMAES();
		Algorithm test = new DEAlgorithm(DEAlgorithm.JDE_rand_1_bin,20);
		DoubleSolution best;
        try {
            best = test.execute(sphere);
            System.out.println("Best is:"+best);
        } catch (StopCriteriaException e) {
            e.printStackTrace();
        }
	}

}
