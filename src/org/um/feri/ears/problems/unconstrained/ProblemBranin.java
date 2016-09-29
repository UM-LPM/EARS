package org.um.feri.ears.problems.unconstrained;


import java.util.ArrayList;
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
public class ProblemBranin  extends Problem{
	public ProblemBranin() {
		super(2,0);

		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));

		lowerLimit.set(0, -5.0);
		upperLimit.set(0, 15.0);
		
		lowerLimit.set(1, 0.0); //diff than in paper
		upperLimit.set(1, 15.0);

		name ="Branin function";
	}
	
	public int getNumberOfGlobalOpt() {
		return 3;
	}
	
	public double eval(double x[]) {
		double v = 0;
		v = Math.pow(x[1]-(5.1/(4*Math.PI*Math.PI))*x[0]*x[0]+5*x[0]/Math.PI-6,2)+10*(1-1/(8*Math.PI))*Math.cos(x[0])+10;
		return v;
	}
	
	@Override
	public double eval(List<Double> ds) {
		double v = 0;
		v = Math.pow(ds.get(1)-(5.1/(4*Math.PI*Math.PI))*ds.get(0)*ds.get(0)+5*ds.get(0)/Math.PI-6,2)+10*(1-1/(8*Math.PI))*Math.cos(ds.get(0))+10;
		return v;
	}
	
	public double getOptimumEval() {
		return  0.39788735772973816;
	}
	public double[][] getOptimalVector() {
		double[][] v = new double[3][numberOfDimensions]; 
		//-¹ , 12.275), (¹ , 2.275), (9.42478, 2.475
		v[0][0] = -Math.PI; 
		v[0][1] = 12.275;
		v[1][0] = Math.PI; 
		v[1][1] = 2.275;
		v[2][0] = 9.42478; 
		v[2][1] = 2.475;
		return v;
	}
	
	@Override
	public boolean isFirstBetter(List<Double> x, double eval_x, List<Double> y,
			double eval_y) {
		return eval_x<eval_y;
	}	
}
