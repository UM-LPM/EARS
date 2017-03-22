package org.um.feri.ears.problems.constrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;


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
public class TLBOBenchmarkFunction4 extends Problem {
	// http://www-optima.amp.i.kyoto-u.ac.jp/member/student/hedar/Hedar_files/TestGO_files/Page3235.htm
	// public final static double best_x[]={579.3167, 1359.943, 5110.071,
	// 182.0174, 295.5985, 217.9799, 286.4162, 395.5979};
	public final static double best_x[] = { 579.306685017979589,
			1359.97067807935605, 5109.97065743133317, 182.01769963061534,
			295.601173702746792, 217.982300369384632, 286.41652592786852,
			395.601173702746735 };

	
	public TLBOBenchmarkFunction4() {
		super(8,6);
		minimum = true;
		
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
		lowerLimit.set(0, 100.0);
		upperLimit.set(0, 9900.0);
		lowerLimit.set(1, 1000.0);
		upperLimit.set(1, 9000.0);
		lowerLimit.set(2, 1000.0);
		upperLimit.set(2, 9000.0);
		for (int i = 3; i < numberOfDimensions; i++) {
			lowerLimit.set(i, 10.0);
			upperLimit.set(i, 990.0);
		}

		max_constraints = new Double[numberOfConstraints];
		min_constraints = new Double[numberOfConstraints];
		count_constraints  = new Double[numberOfConstraints];
		sum_constraints  = new Double[numberOfConstraints];
		normalization_constraints_factor = new Double[numberOfConstraints];
		// System.out.println(Arrays.toString(interval)+"\n"+Arrays.toString(intervalL));
		name ="TLBOBenchmarkFunction4 (G10) cec-g10";
		
	}
	public double[] calc_constrains(double x[]) {

		double[] g = new double[numberOfConstraints];
		g[0] = -1.0 + 0.0025 * (x[3] + x[5]);
		g[1] = -1.0 + 0.0025 * (x[4] + x[6] - x[3]);
		g[2] = -1.0 + 0.01 * (x[7] - x[4]);
		g[3] = -x[0] * x[5] + 833.33252 * x[3] + 100.0 * x[0] - 83333.333;
		g[4] = -x[1] * x[6] + 1250.0 * x[4] + x[1] * x[3] - 1250.0 * x[3];
		g[5] = -x[2] * x[7] + 1250000.0 + x[2] * x[4] - 2500.0 * x[4];
		return g;
	}
	
	/*public double[] calc_constrains(double x[]) {

		double[] g = new double[constrains];
		g[0] = -1.0 + 0.0025 * (x[3] + x[5]);
		g[1] = -1.0 + 0.0025 * (x[4] + x[6] - x[3]);
		g[2] = -1.0 + 0.01 * (x[7] - x[4]);
		//g[3] = -x[0] * x[5] + 833.33252 * x[3] + 100.0 * x[0] - 83333.333;
		//g[4] = -x[1] * x[6] + 1250.0 * x[4] + x[1] * x[3] - 1250.0 * x[3];
		//g[5] = -x[2] * x[7] + 1250000.0 + x[2] * x[4] - 2500.0 * x[4];
		return g;
	}
	*/



	public double eval(double x[]) {
		double v = x[0] + x[1] + x[2];
		return v;
	}
	
	@Override
	public double eval(Double[] ds) {
		return eval(ArrayUtils.toPrimitive(ds));
	}

	/*
	 * @Override public boolean isFirstBetter(double[] x, double eval_x,
	 * double[] y, double eval_y) { boolean cons_x = (constrainsOK(x)==0);
	 * boolean cons_y = (constrainsOK(y)==0); if (cons_x) { if (cons_y) { return
	 * eval_x<eval_y; //min } return true; } if (cons_y) { return false; }
	 * return constrainsOK(x)<constrainsOK(y); }
	 * 
	 * 
	 * public double constrainsOKs(double x[]) { if (-1.+0.0025*(x[3]+x[5])>0)
	 * return 1; if (-1.+0.0025*(-x[3]+x[4]+x[6])>0) return 2; if
	 * (-1.+0.01*(-x[4]+x[7])>0) return 3; if
	 * (100.*x[0]-x[0]*x[5]+833.33252*x[3]-83333.333>0) return 4; if
	 * (x[1]*x[3]-x[1]*x[6]-1250.*x[3]+1250.*x[4]>0) return 5; if
	 * (x[2]*x[4]-x[2]*x[7]-2500.*x[4]+1250000.>0) return 6; return 0; }
	 * 
	 * public double constrainsOKss(double x[]) { double d=0; if
	 * (-1.+0.0025*(x[3]+x[5])>0) d+=-1+0.0025*(x[3]+x[5]); if
	 * (-1.+0.0025*(-x[3]+x[4]+x[6])>0) d+=-1+0.0025*(-x[3]+x[4]+x[6]); if
	 * (-1.+0.01*(-x[4]+x[7])>0) d+=-1+0.01*(-x[4]+x[7]); if
	 * (100.*x[0]-x[0]*x[5]+833.33252*x[3]-83333.333>0)
	 * d+=100*x[0]-x[0]*x[5]+833.33252*x[3]-83333.333; if
	 * (x[1]*x[3]-x[1]*x[6]-1250.*x[3]+1250.*x[4]>0)
	 * d+=x[1]*x[3]-x[1]*x[6]-1250*x[3]+1250*x[4]; if
	 * (x[2]*x[4]-x[2]*x[7]-2500.*x[4]+1250000.>0)
	 * d+=x[2]*x[4]-x[2]*x[7]-2500*x[4]+1250000; if
	 * (d/constrainsOK3(x)<0.000001) return 0; //? return d;///constrainsOK3(x);
	 * } public double constrainsOK(double x[]) { double
	 * d1=0,d2=0,d3=0,d4=0,d5=0,d6=0,d=0; if (-1.+0.0025*(x[3]+x[5])>0) {
	 * d+=-1+0.0025*(x[3]+x[5]); } if (-1.+0.0025*(-x[3]+x[4]+x[6])>0)
	 * d+=-1+0.0025*(-x[3]+x[4]+x[6]); if (-1.+0.01*(-x[4]+x[7])>0)
	 * d+=-1+0.01*(-x[4]+x[7]); if
	 * (100.*x[0]-x[0]*x[5]+833.33252*x[3]-83333.333>0)
	 * d+=100*x[0]-x[0]*x[5]+833.33252*x[3]-83333.333; if
	 * (x[1]*x[3]-x[1]*x[6]-1250.*x[3]+1250.*x[4]>0)
	 * d+=x[1]*x[3]-x[1]*x[6]-1250*x[3]+1250*x[4]; if
	 * (x[2]*x[4]-x[2]*x[7]-2500.*x[4]+1250000.>0)
	 * d+=x[2]*x[4]-x[2]*x[7]-2500*x[4]+1250000; //if (d/6<0.000001) return 0;
	 * //? return d/6;///constrainsOK3(x); }
	 * 
	 * 
	 * public double constrainsOK3(double x[]) { double d=0; if
	 * (-1+0.0025*(x[3]+x[5])>0.0000001) d+=1; if
	 * (-1+0.0025*(-x[3]+x[4]+x[6])>0.0000001) d+=1; if
	 * (-1+0.01*(-x[4]+x[7])>0.0000001) d+=1; if
	 * (100*x[0]-x[0]*x[5]+833.33252*x[3]-83333.333>0.0000001) d+=1; if
	 * (x[1]*x[3]-x[1]*x[6]-1250*x[3]+1250*x[4]>0.0000001) d+=1; if
	 * (x[2]*x[4]-x[2]*x[7]-2500*x[4]+1250000>0.0000001) d+=1; return d; }
	 */
	public double getOptimumEval() {
		return 7049.3307;
	}

}
