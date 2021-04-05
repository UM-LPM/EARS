package org.um.feri.ears.examples;

import org.um.feri.ears.algorithms.moo.nsga2.D_NSGAII;
import org.um.feri.ears.problems.DoubleMOTask;
import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.moo.DoubleMOProblem;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem1;
import org.um.feri.ears.qualityIndicator.IndicatorFactory;
import org.um.feri.ears.qualityIndicator.QualityIndicator;
import org.um.feri.ears.qualityIndicator.QualityIndicator.IndicatorName;

public class QualityIndicatorExample {

	public static void main(String[] args) {

		D_NSGAII nsga2 = new D_NSGAII();
		DoubleMOProblem problem = new UnconstrainedProblem1();
		DoubleMOTask t = new DoubleMOTask(StopCriterion.EVALUATIONS, 300000, 0, 0, 0.0001,  new UnconstrainedProblem1());
		
		QualityIndicator qi = IndicatorFactory.createIndicator(IndicatorName.NATIVE_HV, t.getNumberOfObjectives(), t.getProblemFileName());
		
		try {
			ParetoSolution<Double> result = nsga2.execute(t);
			double value = qi.evaluate(result);
			System.out.println("QI value: "+value);
			
		} catch (StopCriterionException e) {
			e.printStackTrace();
		}
	}
}
