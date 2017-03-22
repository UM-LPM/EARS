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
public class TLBOBenchmarkFunction3  extends Problem{
	//http://www-optima.amp.i.kyoto-u.ac.jp/member/student/hedar/Hedar_files/TestGO_files/Page898.htm
	public final static double best_x[]={2.330499, 1.951372, -0.4775414, 4.365726,-0.6244870, 1.038131, 1.594227};
	public TLBOBenchmarkFunction3() {
		super(7,4);
		minimum = true;
		
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
		Collections.fill(lowerLimit, -10.0);
		Collections.fill(upperLimit, 20.0);
		
		max_constraints = new Double[numberOfConstraints];
		min_constraints = new Double[numberOfConstraints];
		count_constraints  = new Double[numberOfConstraints];
		sum_constraints  = new Double[numberOfConstraints];
		normalization_constraints_factor = new Double[numberOfConstraints];
		//System.out.println(Arrays.toString(interval)+"\n"+Arrays.toString(intervalL));
		name = "TLBOBenchmarkFunction3 cec-g09";
	}
	
	public double[] calc_constrains(double x[]) {

		 double[] g = new double[4];

	     
	        g[0] = -127.0 + 2 * x[0] * x[0] + 3.0 * Math.pow(x[1], 4) + x[2] +
	               4.0 * x[3] * x[3] + 5.0 * x[4];
	        g[1] = -282.0 + 7.0 * x[0] + 3.0 * x[1] + 10.0 * x[2] * x[2] + x[3] -
	               x[4];
	        g[2] = -196.0 + 23.0 * x[0] + x[1] * x[1] + 6.0 * x[5] * x[5] -
	               8.0 * x[6];
	        g[3] = 4.0 * x[0] * x[0] + x[1] * x[1] - 3.0 * x[0] * x[1] +
	               2.0 * x[2] * x[2] + 5.0 * x[5] - 11.0 * x[6];


		return g;
	}
	
	public double[] calc_constrainsActive(double x[]) {

		 double[] g = new double[4];

	     
	        g[0] = -127.0 + 2 * x[0] * x[0] + 3.0 * Math.pow(x[1], 4) + x[2] +
	               4.0 * x[3] * x[3] + 5.0 * x[4];
	        
	        g[1] = 4.0 * x[0] * x[0] + x[1] * x[1] - 3.0 * x[0] * x[1] +
	               2.0 * x[2] * x[2] + 5.0 * x[5] - 11.0 * x[6];


		return g;
	}	

	public double eval(double x[]) {
//		y = (x(1)-10)^2+5*(x(2)-12)^2+x(3)^4+3*(x(4)-11)^2+...
		//		    10*x(5)^6+7*x(6)^2+x(7)^4-4*x(6)*x(7)-10*x(6)-8*x(7);
		double a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,v=0;
		a1=Math.pow(x[0]-10,2);
		a2=5*Math.pow(x[1]-12,2);
		a3=Math.pow(x[2],4);
		a4=3*Math.pow(x[3]-11,2);
		a5=10*Math.pow(x[4],6);
		a6=7*Math.pow(x[5],2);
		a7=Math.pow(x[6],4);
		a8=4*x[5]*x[6];
		a9=10*x[5];
		a10=8*x[6];
		v=a1+a2+a3+a4+a5+a6+a7-a8-a9-a10;
		return v;
	}
	
	@Override
	public double eval(Double[] ds) {
		return eval(ArrayUtils.toPrimitive(ds));
	}

	public double getOptimumEval() {
		return 680.6300573;
	}
}
