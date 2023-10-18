package org.um.feri.ears.operators;

import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.util.random.RNG;

public class PolynomialMutationSO implements MutationOperator<DoubleProblem, NumberSolution<Double>>{

	
	private static final double ETA_M_DEFAULT_ = 20.0;
	private final double eta_m = ETA_M_DEFAULT_;

	private Double mutationProbability = null;
	private Double distributionIndex = eta_m;

	public PolynomialMutationSO(Double mutationProbability, Double distributionIndex) {

		this.mutationProbability = mutationProbability;
		this.distributionIndex = distributionIndex;
	}
	
	@Override
	public NumberSolution<Double> execute(NumberSolution<Double> source, DoubleProblem problem) {
		
		doMutation(mutationProbability, source, problem);
		return source;
	}

	private void doMutation(Double probability, NumberSolution<Double> solution, DoubleProblem problem) {
		double rnd, delta1, delta2, mut_pow, deltaq;
		double y, yl, yu, val, xy;
		for (int var = 0; var < problem.getNumberOfDimensions(); var++) {
			if (RNG.nextDouble() <= probability) {
				y = solution.getValue(var);
				yl = problem.getLowerLimit(var);
				yu = problem.getUpperLimit(var);
				delta1 = (y - yl) / (yu - yl);
				delta2 = (yu - y) / (yu - yl);
				rnd = RNG.nextDouble();
				mut_pow = 1.0 / (eta_m + 1.0);
				if (rnd <= 0.5) {
					xy = 1.0 - delta1;
					val = 2.0 * rnd + (1.0 - 2.0 * rnd) * (Math.pow(xy, (distributionIndex + 1.0)));
					deltaq = java.lang.Math.pow(val, mut_pow) - 1.0;
				} else {
					xy = 1.0 - delta2;
					val = 2.0 * (1.0 - rnd) + 2.0 * (rnd - 0.5) * (java.lang.Math.pow(xy, (distributionIndex + 1.0)));
					deltaq = 1.0 - (java.lang.Math.pow(val, mut_pow));
				}
				y = y + deltaq * (yu - yl);
				if (y < yl)
					y = yl;
				if (y > yu)
					y = yu;
				solution.setValue(var, y);
			}
		}
	}
	@Override
	public void setProbability(double mutationProbability) {
		this.mutationProbability = 1.0 / mutationProbability;
		
	}

}
