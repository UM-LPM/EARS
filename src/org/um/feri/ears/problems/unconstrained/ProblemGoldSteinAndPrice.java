package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
public class ProblemGoldSteinAndPrice  extends Problem{
	public ProblemGoldSteinAndPrice() {
		super(2,0);
		
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
		Collections.fill(lowerLimit, -2.0);
		Collections.fill(upperLimit, 4.0);
		
		name ="Goldstein And Price";
	}
	

	
	public double eval(double x[]) {
		double a,b;
		a = 1+Math.pow(x[0]+x[1]+1,2)*(19-14*x[0]+3*x[0]*x[0]-14*x[1]+6*x[0]*x[1]+3*x[1]*x[1]);
		b = 30+Math.pow(2*x[0]-3*x[1],2)*(18-32*x[0]+12*x[0]*x[0]+48*x[1]-36*x[0]*x[1]+27*x[1]*x[1]);
		double v = a*b;
		return v;
	}
	
	public double getOptimumEval() {
		return 3;
	}
	public double[][] getOptimalVector() {
		double[][] v = new double[1][numberOfDimensions];
		v[0][0] =0;
		v[0][1] =-1;
		return v;
	}
	
	@Override
	public boolean isFirstBetter(List<Double> x, double eval_x, List<Double> y,
			double eval_y) {
		return eval_x<eval_y;
	}



	@Override
	public double eval(List<Double> ds) {
		double a,b;
		a = 1+Math.pow(ds.get(0)+ds.get(1)+1,2)*(19-14*ds.get(0)+3*ds.get(0)*ds.get(0)-14*ds.get(1)+6*ds.get(0)*ds.get(1)+3*ds.get(1)*ds.get(1));
		b = 30+Math.pow(2*ds.get(0)-3*ds.get(1),2)*(18-32*ds.get(0)+12*ds.get(0)*ds.get(0)+48*ds.get(1)-36*ds.get(0)*ds.get(1)+27*ds.get(1)*ds.get(1));
		double v = a*b;
		return v;

	}
	
}
