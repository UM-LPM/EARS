package org.um.feri.ears.algorithms.so.jade;

import org.um.feri.ears.problems.NumberSolution;

public class JADESolution extends NumberSolution<Double> {
    double CR, F;

    /*	public JADEIndividual(Individual i) {
            super(i);
            CR = 0.5; //some default not used
            F = 0.5;
        }*/
    public JADESolution(JADESolution i) {
        super(i);
        CR = i.CR;
        F = i.F;
    }

    public JADESolution(NumberSolution<Double> eval, double cr2, double f2) {
        super(eval);
        CR = cr2;
        F = f2;
    }

    public double getCR() {
        return CR;
    }

    public void setCR(double cR) {
        if (cR > 1) {
            CR = 1;
        } else if (cR < 0) CR = 0;
        else {
            CR = cR;
        }
    }

    public double getF() {
        return F;
    }

    /*
     * I think 1 is too low for max!
     */
    public void setF(double f) {
        if (f > 1) F = 1;
        else
            F = f;
    }
}
