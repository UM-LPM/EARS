package org.um.feri.ears.problems.constrained;

import java.util.ArrayList;
import java.util.Collections;

import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
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
public class TLBOBenchmarkFunction2 extends DoubleProblem {
	// http://www-optima.amp.i.kyoto-u.ac.jp/member/student/hedar/Hedar_files/TestGO_files/Page2613.htm

	public TLBOBenchmarkFunction2() {
		super("TLBOBenchmarkFunction2 cec-g03", 10, 1, 1, 1);

		objectiveMaximizationFlags[0] = true;
		
		upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 0.0));
		lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 0.0));
		Collections.fill(lowerLimit, 0.0);
		Collections.fill(upperLimit, 1.0);
		
		maxConstraints = new double[numberOfConstraints];
		minConstraints = new double[numberOfConstraints];
		countConstraints = new double[numberOfConstraints];
		sumConstraints = new double[numberOfConstraints];
		normalizationConstraintsFactor = new double[numberOfConstraints];
		// System.out.println(Arrays.toString(interval)+"\n"+Arrays.toString(intervalL));
		decisionSpaceOptima[0] = new double[]{0.31622776601683794, 0.31622776601683794,0.31622776601683794,
				0.31622776601683794, 0.31622776601683794, 0.31622776601683794, 0.31622776601683794,0.31622776601683794, 0.31622776601683794,
				0.31622776601683794};
		objectiveSpaceOptima[0] = 1.0;
	}

	public double eval(double[] x) {
		double t1 = 1;
		for (int i = 0; i < numberOfDimensions; i++)
			t1 *= x[i];
		return Math.pow(Math.sqrt(numberOfDimensions), numberOfDimensions) * t1;
	}

	@Override
	public double[] calculateConstrains(NumberSolution<Double> solution) {

		double[] x = Util.toDoubleArray(solution.getVariables());
		double[] g = new double[numberOfConstraints];
		double d = -1;
		for (int i = 0; i < numberOfDimensions; i++) {
			d += x[i] * x[i];
		}
		d = Math.abs(d);
		if (d <= 0.001)
			g[0] = 0;
		else
			g[0] = d;
		return g;
	}
	
	/*
	 * public double constrainsOK(double x[]) { //h1 double d=-1; for (int i=0;
	 * i<dim; i++) { d+=x[i]*x[i]; } if (Math.abs(d)>0.001) return Math.abs(d);
	 * return 0; }
	 */
	public String constrainsOKtoString(double[] x) {
		// h1
		double d = -1.;
		for (int i = 0; i < numberOfDimensions; i++) {
			d += x[i] * x[i];
		}
		if (Math.abs(d) >= 0.000001)
			return "1 " + d;
		return "0 OK";
	}
}
