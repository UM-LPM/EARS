//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.

package org.um.feri.ears.problems.moo.functions;

import org.um.feri.ears.problems.moo.Objective;

public class UP3_2 extends Objective{
	
	int dim;

	public UP3_2(int dim) {
		this.dim = dim;
	}

	@Override
	public double eval(double[] ds) {

		int count2;
		double sum2, prod2, yj, pj;
		sum2 = 0.0;
		count2 = 0;
		prod2 = 1.0;

		for (int j = 2; j <= dim; j++) {
			yj = ds[j - 1] - Math.pow(ds[0], 0.5 * (1.0 + 3.0 * (j - 2.0) / (dim - 2.0)));
			pj = Math.cos(20.0 * yj * Math.PI / Math.sqrt(j));
			if (j % 2 == 0) {
				sum2 += yj * yj;
				prod2 *= pj;
				count2++;
			}
		}

		return 1.0 - Math.sqrt(ds[0]) + 2.0 * (4.0 * sum2 - 2.0 * prod2 + 2.0)
				/ (double) count2;
	}
}
