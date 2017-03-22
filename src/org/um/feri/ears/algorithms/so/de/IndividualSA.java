package org.um.feri.ears.algorithms.so.de;

import java.util.List;

import org.um.feri.ears.problems.DoubleSolution;

public class IndividualSA extends DoubleSolution {
    private double F;
    private double CR;
    
    public IndividualSA(Double[] x, double eval, double F, double CR) {
        super(x, eval);
        this.F = F;
        this.CR =CR;
    }
    
    public IndividualSA(IndividualSA i) {
        super(i);
        this.F = i.F;
        this.CR =i.CR;
    }
    public IndividualSA(DoubleSolution i, double F, double CR) {
        super(i);
        this.F = F;
        this.CR = CR;
    }
    
    public double getF() {
        return F;
    }
    public void setF(double f) {
        F = f;
    }
    public double getCR() {
        return CR;
    }
    public void setCR(double cR) {
        CR = cR;
    }
    public String toString() {
        return super.toString()+" F:"+F+" CR:"+CR;
    }

}
