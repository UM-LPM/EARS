package org.um.feri.ears.problems.constrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.util.Util;


/**
* Problem function!
* <p>
* 
* @author Matej Crepinsek
* @version 1
* 
*          <h3>License</h3>
* 
*          Copyright (c) 2011 by Matej Crepinsek. <br>
*          All rights reserved. <br>
* 
*          <p>
*          Redistribution and use in source and binary forms, with or without
*          modification, are permitted provided that the following conditions
*          are met:
*          <ul>
*          <li>Redistributions of source code must retain the above copyright
*          notice, this list of conditions and the following disclaimer.
*          <li>Redistributions in binary form must reproduce the above
*          copyright notice, this list of conditions and the following
*          disclaimer in the documentation and/or other materials provided with
*          the distribution.
*          <li>Neither the name of the copyright owners, their employers, nor
*          the names of its contributors may be used to endorse or promote
*          products derived from this software without specific prior written
*          permission.
*          </ul>
*          <p>
*          THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
*          "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
*          LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
*          FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
*          COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
*          INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
*          BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
*          LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
*          CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
*          LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
*          ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
*          POSSIBILITY OF SUCH DAMAGE.
* 
*/
public class TLBOBenchmarkFunction1 extends Problem {
	// http://www-optima.amp.i.kyoto-u.ac.jp/member/student/hedar/Hedar_files/TestGO_files/Page506.htm
	public final static double best_x[] = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 3, 3,
			1 };

	public TLBOBenchmarkFunction1() {
		super(13,9);
		minimum = true;
		max_constraints = new Double[numberOfConstraints];
		min_constraints = new Double[numberOfConstraints];
		count_constraints  = new Double[numberOfConstraints];
		sum_constraints  = new Double[numberOfConstraints];
		normalization_constraints_factor = new Double[numberOfConstraints];
		
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
		for (int i = 0; i < 9; i++) {
			lowerLimit.set(i, 0.0);
			upperLimit.set(i, 1.0);
		}
		for (int i = 9; i < 12; i++) {
			lowerLimit.set(i, 0.0);
			upperLimit.set(i, 100.0);
		}
		lowerLimit.set(12, 0.0);
		upperLimit.set(12, 1.0);
		
		
		
		name = "TLBOBenchmarkFunction1 (TP7) cec-g01";
		// System.out.println(Arrays.toString(interval)+"\n"+Arrays.toString(intervalL));
	}



	public double eval(double x[]) {
		double v = 0;
		v = 5.0 * (x[0] + x[1] + x[2] + x[3]) - 5.0
				* (x[0] * x[0] + x[1] * x[1] + x[2] * x[2] + x[3] * x[3]);
		for (int j = 4; j < 13; j++) {
			v = v - x[j];
		}
		return v;
	}

	
	public double[] calc_constrains(double x[]) {

		double[] g = new double[numberOfConstraints];
		g[0] = 2.0 * x[0] + 2.0 * x[1] + x[9] + x[10] - 10.;
		g[1] = 2.0 * x[0] + 2.0 * x[2] + x[9] + x[11] - 10.;
		g[2] = 2.0 * x[1] + 2.0 * x[2] + x[10] + x[11] - 10.;
		g[3] = -8.0 * x[0] + x[9];
		g[4] = -8.0 * x[1] + x[10];
		g[5] = -8.0 * x[2] + x[11];
		g[6] = -2.0 * x[3] - x[4] + x[9];
		g[7] = -2.0 * x[5] - x[6] + x[10];
		g[8] = -2.0 * x[7] - x[8] + x[11];
		return g;
	}
	
	
	public double[] calc_constrains2(double x[]) {

		double[] g = new double[numberOfConstraints];
		g[0] = 2.0 * x[0] + 2.0 * x[1] + x[9] + x[10] - 10.;
		g[1] = 2.0 * x[0] + 2.0 * x[2] + x[9] + x[11] - 10.;
		g[2] = 2.0 * x[1] + 2.0 * x[2] + x[10] + x[11] - 10.;
		g[3] = -2.0 * x[3] - x[4] + x[9];
		g[4] = -2.0 * x[5] - x[6] + x[10];
		g[5] = -2.0 * x[7] - x[8] + x[11];
		return g;
	}


	public double getOptimumEval() {
		return -15;
	}



	@Override
	public double eval(List<Double> ds) {
		
		double[] x = new double[numberOfDimensions];
		for (int i = 0; i < numberOfDimensions; i++)
			x[i] = ds.get(i);
		
		double v = 0;
		v = 5.0 * (x[0] + x[1] + x[2] + x[3]) - 5.0
				* (x[0] * x[0] + x[1] * x[1] + x[2] * x[2] + x[3] * x[3]);
		for (int j = 4; j < 13; j++) {
			v = v - x[j];
		}
		return v;
	}
	
}
