package org.um.feri.ears.problems.constrained;

import java.util.ArrayList;
import java.util.Collections;

import org.um.feri.ears.problems.DoubleProblem;


/**
 * Problem function!
 * <p>
 *
 * @author Matej Crepinsek
 * @version 1
 *
 * <h3>License</h3>
 * <p>
 * Copyright (c) 2011 by Matej Crepinsek. <br>
 * All rights reserved. <br>
 *
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * <ul>
 * <li>Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * <li>Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with
 * the distribution.
 * <li>Neither the name of the copyright owners, their employers, nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 * </ul>
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
public class TLBOBenchmarkFunction5 extends DoubleProblem {
    //http://www.sciencedirect.com/science/article/pii/S0305054811000955

    public TLBOBenchmarkFunction5() {
        super(3, 1, 1, 0);
        minimize = false;

        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
        Collections.fill(lowerLimit, 0.0);
        Collections.fill(upperLimit, 10.0);

        //System.out.println(Arrays.toString(interval)+"\n"+Arrays.toString(intervalL));
        name = "TLBOBenchmarkFunction5 (TP11)";
        decisionSpaceOptima[0] = new double[]{5, 5, 5};
        objectiveSpaceOptima[0] = 1.0;
    }


    public double eval(double[] x) {
        return (100 - Math.pow(x[0] - 5, 2) - Math.pow(x[1] - 5, 2) - Math.pow(x[2] - 5, 2)) / 100.;
    }
	
	/*
	@Override
	public boolean isFirstBetter(double[] x, double eval_x, double[] y,
			double eval_y) {
		boolean cons_x = (constrainsOK(x)==0);
		boolean cons_y = (constrainsOK(y)==0);
		if (cons_x) {
			if (cons_y) {
				return eval_x>eval_y;
			}
			return true;
		}
		if (cons_y) {
			return false;
		}
		return eval_x>eval_y;
	}
	
	@Override
	public boolean isFirstBetter(double[] x, double eval_x, double[] y,
			double eval_y) {
		double cons_x = constrainsOK(x);
		double cons_y = constrainsOK(y);
		if (cons_x < 0.000001) {
			if (cons_y < 0.000001) {
				return eval_x>eval_y; //max
			}
			return true;
		}
		if (cons_y < 0.000001) {
			return false;
		}
		return constrainsOK(x) < constrainsOK(y);
	
	}
	*/

    public double constrainsOKS(double[] x) {
        //optimized version!
        double mindx1 = Double.MAX_VALUE;
        double mindx2 = Double.MAX_VALUE;
        double mindx3 = Double.MAX_VALUE;
        for (int i = 1; i < 10; i++) {
            if (Math.abs((x[0] - i)) < mindx1) mindx1 = Math.abs(x[0] - i);
            if (Math.abs((x[1] - i)) < mindx2) mindx2 = Math.abs(x[1] - i);
            if (Math.abs((x[2] - i)) < mindx3) mindx3 = Math.abs(x[2] - i);
        }
        //System.out.println(mindx1+" "+mindx2+" "+mindx3+" "+(mindx1*mindx1+mindx2*mindx2+mindx3*mindx3-0.0625));
        if ((mindx1 * mindx1 + mindx2 * mindx2 + mindx3 * mindx3 - 0.0625) <= 0) return 0; //ok
        return (mindx1 * mindx1 + mindx2 * mindx2 + mindx3 * mindx3); //constraints
    }

    public double constrainsEvaluations(double[] x) {
        //optimized version!
        double mindx1 = Double.MAX_VALUE;
        double mindx2 = Double.MAX_VALUE;
        double mindx3 = Double.MAX_VALUE;
        for (int i = 1; i < 10; i++) {
            if (Math.abs((x[0] - i)) < mindx1) mindx1 = Math.abs(x[0] - i);
            if (Math.abs((x[1] - i)) < mindx2) mindx2 = Math.abs(x[1] - i);
            if (Math.abs((x[2] - i)) < mindx3) mindx3 = Math.abs(x[2] - i);
        }
        //System.out.println(mindx1+" "+mindx2+" "+mindx3+" "+(mindx1*mindx1+mindx2*mindx2+mindx3*mindx3-0.0625));
        if ((mindx1 * mindx1 + mindx2 * mindx2 + mindx3 * mindx3 - 0.0625) <= 0) return 0; //ok
        return (mindx1 * mindx1 + mindx2 * mindx2 + mindx3 * mindx3); //constraints
    }
}
