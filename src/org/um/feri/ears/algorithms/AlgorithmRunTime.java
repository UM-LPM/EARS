package org.um.feri.ears.algorithms;

import java.util.ArrayList;

import org.um.feri.ears.util.MeanStDev;

public class AlgorithmRunTime {
    private int numberOfRuns;
    private long sumTime;
    private ArrayList<Double> durations; //in seconds need for standard deviation
    private static final double TO_SECONDS=0.001;
    public int getNumberOfRuns() {
        return numberOfRuns;
    }
    public long getSumTime() {
        return sumTime;
    }
    public AlgorithmRunTime() {
        numberOfRuns = 0;
        sumTime = 0;
        durations = new ArrayList<Double>();
    }
    public boolean isCalculated() {
        return (numberOfRuns>0);
    }
    public void addRunDuration(long duration) {
        durations.add(duration*TO_SECONDS);
        numberOfRuns++;
        sumTime+=duration;
    }
    public MeanStDev getStandardDev() {
        return new MeanStDev(durations);
    }
}
