package org.um.feri.ears.problems.unconstrained.cec2015;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.unconstrained.cec2015.input_data.DataReader;

public abstract class CEC2015 extends Problem {
	
    protected double[] OShift;
    protected double[] M;
    protected double[] y;
    protected double[] z;
    protected double[] x_bound;
    protected int func_num;
    protected int[] SS;
    
	
    /**
     * Expensive Test functions are only defined for D=10, 30.
     * @param d number of dimensions
     * @param func_num Function number 1-15
     */
	public CEC2015(int d, int func_num) {
		super(d,0);
		
		this.func_num = func_num;
        if ((func_num < 1) || (func_num > 15)) {
            System.err.println("Function number must be between 1 and 15!");
        }
		
		benchmarkName = "CEC20015";
		shortName = "F"+func_num;
		
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -100.0));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 100.0));
		
        // cf_num not correct for D2
        int cf_num = 10, i, j;
        
        y       = new double[d];
        z       = new double[d];
        x_bound = new double[d];

        for (i = 0; i < d; i++) {
            x_bound[i] = 100.0;
        }


        if (!((d == 10) || (d == 30))) {
            System.out.println("\nError: Expensive Test functions are only defined for D=10, 30.");
        }


        /* Load Matrix M**************************************************** */
        M = DataReader.readRotation(func_num, d, cf_num);

        /* Load shift_data************************************************** */
        OShift = DataReader.readShiftData(func_num, d, cf_num);

        /* Load Shuffle_data****************************************** */
        SS = DataReader.readShuffleData(func_num, d);
        optimum[0] = OShift;
	}

	@Override
	public double getGlobalOptimum() {

		return func_num * 100;
	}
}
