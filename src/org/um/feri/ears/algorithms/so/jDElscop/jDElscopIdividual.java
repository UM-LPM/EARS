package org.um.feri.ears.algorithms.so.jDElscop;


import java.util.Arrays;

import org.um.feri.ears.problems.DoubleSolution;


public class jDElscopIdividual extends DoubleSolution {
	double parameters[];

	public jDElscopIdividual(DoubleSolution eval) {
		super(eval);
		parameters = new double[jDElscop.StrategyDE.COUNT*2];
	}
	public jDElscopIdividual(jDElscopIdividual eval) {
		super(eval);
		parameters = Arrays.copyOf(eval.parameters, eval.parameters.length);
	}
	public static jDElscopIdividual setInitState(DoubleSolution a) {
		jDElscopIdividual tmp = new jDElscopIdividual(a);
		for (int i=0; i<jDElscop.StrategyDE.COUNT;i++) {
			tmp.parameters[i*2] = 0.5; //F
			tmp.parameters[i*2+1] = 0.9; //CR 
		}
		return tmp;
		
	}
	/*public static jDElscopIdividual setParamState(Individual tmpI, jDElscopIdividual parent) {
		jDElscopIdividual tmp = new jDElscopIdividual(tmpI);
		for (int i=0; i<jDElscop.StrategyDE.COUNT;i++) {
			tmp.parameters[i*2] = parent.parameters[i*2]; //F
			tmp.parameters[i*2+1] = parent.parameters[i*2+1]; //CR 
		}
		return tmp;
	}*/
	public double[] getNewPara() {
		return  Arrays.copyOf(parameters, parameters.length);
	}
	public static jDElscopIdividual setParamState(DoubleSolution tmpI,
			double[] tmp_par) {
		jDElscopIdividual tmp = new jDElscopIdividual(tmpI);
		tmp.parameters = tmp_par;
		return tmp;
	}

}
