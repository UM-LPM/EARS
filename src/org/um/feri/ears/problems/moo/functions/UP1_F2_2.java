//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.

package org.um.feri.ears.problems.moo.functions;

import org.um.feri.ears.problems.moo.Objective;

public class UP1_F2_2 extends Objective {

	int dim;

	public UP1_F2_2(int dim) {
		this.dim = dim;
	}

	@Override
	public double eval(double[] ds) {

		int count2;
		double sum2, yj;
		sum2 = 0.0;
		count2 = 0;

		for (int j = 2; j <= dim; j++) {
			yj = ds[j - 1] - Math.sin(6.0 * Math.PI * ds[0] + j * Math.PI / dim);
			yj = yj * yj;
			if (j % 2 == 0) {
				sum2 += yj;
				count2++;
			}
		}
		return 1.0 - Math.sqrt(ds[0]) + 2.0 * sum2 / (double) count2;
	}

}
