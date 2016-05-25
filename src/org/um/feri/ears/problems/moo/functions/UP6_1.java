//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.

package org.um.feri.ears.problems.moo.functions;

import org.um.feri.ears.problems.moo.Objective;

public class UP6_1 extends Objective {

	int dim;
	int N;
	double epsilon;

	public UP6_1(int dim, int N, double epsilon) {
		this.dim = dim;
		this.N = N;
		this.epsilon = epsilon;
	}

	@Override
	public double eval(double[] ds) {

		int count1;
		double prod1;
		double sum1, yj, hj, pj;
		sum1 = 0.0;
		count1 = 0;
		prod1 = 1.0;

		for (int j = 2; j <= dim; j++) {
			yj = ds[j - 1] - Math.sin(6.0 * Math.PI * ds[0] + j * Math.PI / dim);
			pj = Math.cos(20.0 * yj * Math.PI / Math.sqrt(j));
			if (j % 2 == 0) {
				continue;
			} else {
				sum1 += yj * yj;
				prod1 *= pj;
				count1++;
			}
		}
		hj = 2.0 * (0.5 / N + epsilon) * Math.sin(2.0 * N * Math.PI * ds[0]);
		if (hj < 0.0)
			hj = 0.0;
		return ds[0] + hj + 2.0 * (4.0 * sum1 - 2.0 * prod1 + 2.0) / (double) count1;
	}
}
