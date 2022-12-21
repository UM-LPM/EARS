package org.um.feri.ears.experiment.ee;

import org.um.feri.ears.problems.NumberSolution;

public class IndividualSA extends NumberSolution<Double> {
    private double F;
    private double CR;

    public IndividualSA(IndividualSA i) {
        super(i);
        this.F = i.F;
        this.CR =i.CR;
    }
    public IndividualSA(NumberSolution<Double> i, double F, double CR) {
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
