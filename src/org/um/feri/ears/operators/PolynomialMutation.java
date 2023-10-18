//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.

package org.um.feri.ears.operators;

import org.um.feri.ears.problems.NumberProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.util.random.RNG;


public class PolynomialMutation implements MutationOperator<NumberProblem<Double>, NumberSolution<Double>>{
	
	
	private static final double ETA_M_DEFAULT_ = 20.0;
	private final double eta_m = ETA_M_DEFAULT_;

	private Double mutationProbability = null;
	private Double distributionIndex = eta_m;

	public PolynomialMutation(Double mutationProbability, Double distributionIndex) {

		this.mutationProbability = mutationProbability;
		this.distributionIndex = distributionIndex;
	}

	public void doMutation(double probability, NumberSolution<Double> solution, NumberProblem<Double> problem) {
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

	public NumberSolution<Double> execute(NumberSolution<Double> object, NumberProblem<Double> problem) {

		NumberSolution<Double> solution = object;
		doMutation(mutationProbability, solution, problem);
		return solution;
	}

	@Override
	public void setProbability(double mutationProbability) {
		this.mutationProbability = 1.0 / mutationProbability;	
	}
	
}
