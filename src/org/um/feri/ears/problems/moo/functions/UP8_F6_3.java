//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.

package org.um.feri.ears.problems.moo.functions;

import org.um.feri.ears.problems.moo.Objective;

public class UP8_F6_3 extends Objective {
	
	int dim;

	public UP8_F6_3(int dim) {
		this.dim = dim;
	}

	@Override
	public double eval(double[] ds) {

		int count3;
		double sum3, yj;
		sum3 = 0.0;
		count3 = 0;

		for (int j = 3; j <= dim; j++) {
			yj = ds[j - 1] - 2.0 * ds[1] * Math.sin(2.0 * Math.PI * ds[0] + j * Math.PI / dim);
			if (j % 3 == 1 || j % 3 == 2) {
				continue;
			} else {
				sum3 += yj * yj;
				count3++;
			}
		}
		return Math.sin(0.5 * Math.PI * ds[0]) + 2.0 * sum3 / (double) count3;
	}
}
