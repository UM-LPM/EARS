package org.um.feri.ears.algorithms.so.aaa;

import org.um.feri.ears.problems.DoubleSolution;

public class Alga extends DoubleSolution {
    private double colonySize;
    private double colonyStarving;
    private double energy;
    private double friction;

    public Alga(DoubleSolution solution) {
        super(solution);
        colonySize = 1;
        colonyStarving = 0;
    }

    public void copyAttributes(Alga alga) {
        colonySize = alga.colonySize;
        colonyStarving = alga.colonyStarving;
        energy = alga.energy;
        friction = alga.friction;
    }

    public double getColonySize() {
        return colonySize;
    }

    public void setColonySize(double colonySize) {
        this.colonySize = colonySize;
    }

    public double getColonyStarving() {
        return colonyStarving;
    }

    public void setColonyStarving(double colonyStarving) {
        this.colonyStarving = colonyStarving;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public double getFriction() {
        return friction;
    }

    public void setFriction(double friction) {
        this.friction = friction;
    }
}
