package org.um.feri.ears.algorithms.so.fdo;

import org.um.feri.ears.problems.DoubleSolution;

import java.util.ArrayList;

public class Bee extends DoubleSolution {
    private ArrayList<Double> lastPace = new ArrayList<>();

    public Bee(DoubleSolution solution) {
        super(solution);
    }

    public ArrayList<Double> getLastPace() {
        return lastPace;
    }

    public void setLastPace(ArrayList<Double> lastPace) {
        this.lastPace = lastPace;
    }
}
