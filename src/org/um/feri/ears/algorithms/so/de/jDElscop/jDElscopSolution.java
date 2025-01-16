package org.um.feri.ears.algorithms.so.de.jDElscop;


import org.um.feri.ears.problems.NumberSolution;

import java.util.Arrays;


public class jDElscopSolution extends NumberSolution<Double> {
    private double[] parameters;

    public jDElscopSolution(NumberSolution<Double> eval) {
        super(eval);
        parameters = new double[jDElscop.Strategy.COUNT * 2];
    }

    public jDElscopSolution(jDElscopSolution eval) {
        super(eval);
        parameters = Arrays.copyOf(eval.parameters, eval.parameters.length);
    }

    public static jDElscopSolution setInitState(NumberSolution<Double> a) {
        jDElscopSolution tmp = new jDElscopSolution(a);
        for (int i = 0; i < jDElscop.Strategy.COUNT; i++) {
            tmp.parameters[i * 2] = 0.5; //F
            tmp.parameters[i * 2 + 1] = 0.9; //CR
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
        return Arrays.copyOf(parameters, parameters.length);
    }

    public static jDElscopSolution setParamState(NumberSolution<Double> tmpI,
                                                 double[] tmp_par) {
        jDElscopSolution tmp = new jDElscopSolution(tmpI);
        tmp.parameters = tmp_par;
        return tmp;
    }

}
