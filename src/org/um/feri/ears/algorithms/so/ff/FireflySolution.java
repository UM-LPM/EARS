package org.um.feri.ears.algorithms.so.ff;

import org.um.feri.ears.problems.DoubleSolution;

public class FireflySolution extends DoubleSolution {

    private double I = 1.0; //I[i] in C++ version, intensity
    private double f = 1.0; //f[i] in C++ version, attractiveness
    private int index = 0;
    //alex: per Yang's paper attractiveness != intensity 
    //but since attractiveness is relavent to distance of i and j fireflies 
    //by coding convention C++ code set I and f the same and f is actually computed inside move_ffa function

    public FireflySolution(DoubleSolution s) {
        super(s); //holds ffa variable of C++. ffa is coordinate of firefly in search space
        f = s.getEval();
        I = f;
    }

    public FireflySolution(FireflySolution s) {
        super(s);
        this.I = s.I;
        this.f = s.f;
    }

    public double getAttractiveness() {
        return this.f;
    }

    public void setAttractiveness(double f) {
        this.f = f;
    }

    public double getIntensity() {
        return this.I;
    }

    public void setIntensity(double I) {
        this.I = I;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }


}
