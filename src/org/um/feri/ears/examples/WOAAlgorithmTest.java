package org.um.feri.ears.examples;

import java.util.ArrayList;
import java.util.List;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.so.es.ES1p1sAlgorithm;
import org.um.feri.ears.algorithms.so.woa.WOA;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.EnumStopCriteria;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.unconstrained.Sphere;
import org.um.feri.ears.util.FakeRandomGenerator;

public class WOAAlgorithmTest {
	
	public static void main(String[] args) {
		Task sphere=new Task(EnumStopCriteria.EVALUATIONS, 1000, 0, 0, 0.001, new Sphere(2));
		Algorithm test = new WOA(10);
		DoubleSolution best;
		FakeRandomGenerator gen = new FakeRandomGenerator();
		for(int i = 0; i < 30; i++) {
			System.out.println(gen.nextInt(5, 20));
		}

        try {
            best = test.execute(sphere);
            System.out.println("Best is:"+best);
        } catch (StopCriteriaException e) {
            e.printStackTrace();
        }
	}

}
