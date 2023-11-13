package org.um.feri.ears.algorithms;

import java.io.Serializable;
import java.util.ArrayList;

import org.um.feri.ears.util.MeanStDev;

public class AlgorithmRunTime implements Serializable {
    private int numberOfRuns;
    private long sumTime;
    private long algorithmOnlySumTime;
    private ArrayList<Double> durations; //in seconds need for standard deviation
    private ArrayList<Double> algorithmOnlyDurations; //in seconds need for standard deviation
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
        algorithmOnlyDurations = new ArrayList<Double>();
    }
    public boolean isCalculated() {
        return (numberOfRuns>0);
    }
    public void addRunDuration(long duration, long algorithmRunTime) {
        durations.add(duration*TO_SECONDS);
        algorithmOnlyDurations.add(algorithmRunTime*TO_SECONDS);
        numberOfRuns++;
        sumTime+=duration;
        algorithmOnlySumTime+= algorithmRunTime;
    }
    public MeanStDev getStandardDev() {
        return new MeanStDev(durations);
    }
    
    public double getLastDuration()
    {
    	return durations.get(durations.size() - 1);
    }
	public long getAlgorithmOnlySumTime() {
		return algorithmOnlySumTime;
	}
	public ArrayList<Double> getAlgorithmOnlyDurations() {
		return algorithmOnlyDurations;
	}
    
    
}
