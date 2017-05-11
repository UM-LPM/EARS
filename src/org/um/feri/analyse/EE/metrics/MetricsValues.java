package org.um.feri.analyse.EE.metrics;

import java.util.ArrayList;

import org.um.feri.analyse.EE.util.MeanStDev;


public class MetricsValues {
	private double explorRatio;
	private double explorType_c;
	private double explorType_m;
	private double explorType_r;
	private double explorType_rnd;
	private MeanStDev explorGap;
	private MeanStDev explorProgressiveness;
	private MeanStDev exploitProgressiveness; //new
	private double exploitRatio;
	private double exploitType_c;
	private double exploitType_m;
	private double exploitType_r;
	private double exploitType_cln;
	private double exploreSelectionPressure;  //new
	private double exploitSelectionPressure;
	private double countAllNodes;
	private double nonRevisitedRatio;
	private double bestFitness;

	public double getExploreSelectionPressure() {
		return exploreSelectionPressure;
	}
	public MeanStDev getExploitProgressiveness(){
		return exploitProgressiveness;
	}
	public double getExplorRatio() {
		return explorRatio;
	}
	
	public double getNonRevisitedRatio() {
		return nonRevisitedRatio;
	}

	public double getExplorType_c() {
		return explorType_c;
	}

	public double getExplorType_m() {
		return explorType_m;
	}

	public double getExplorType_r() {
		return explorType_r;
	}

	public double getExplorType_rnd() {
		return explorType_rnd;
	}

	public MeanStDev getExplorGap_1() {
		return explorGap;
	}

	public MeanStDev getExplorProgressiveness() {
		return explorProgressiveness;
	}

	public double getExploitRatio() {
		return exploitRatio;
	}

	public double getExploitType_c() {
		return exploitType_c;
	}

	public double getExploitType_m() {
		return exploitType_m;
	}

	public double getExploitType_r() {
		return exploitType_r;
	}

	public double getExploitType_cln() {
		return exploitType_cln;
	}

	public double getExploitSelectionPressure() {
		return exploitSelectionPressure;
	}

	public double getCountAllNodes() {
		return countAllNodes;
	}
	
	public double getBestFitness()
	{
		return this.bestFitness;
	}

	public MetricsValues(ATMetrics m) {
		super();
		this.explorRatio = m.explorRatio();
		explorType_c = m.explorType(0);
		explorType_m = m.explorType(1);
		explorType_r = m.explorType(2);
		explorType_rnd = m.explorType(3);
		this.explorGap = m.explorGap();
		this.explorProgressiveness = m.explorProgressiveness();
		this.exploitProgressiveness = m.exploitProgressiveness();
		this.exploitRatio = m.exploitRatio();
		exploitType_c = m.exploitType(0);
		exploitType_m = m.exploitType(1);
		exploitType_r = m.exploitType(2);
		exploitType_cln = m.exploitType(4);
		this.exploitSelectionPressure = m.exploitSelectionPressure();
		this.exploreSelectionPressure = m.exploreSelectionPressure();
		this.countAllNodes = m.getCount();
		this.nonRevisitedRatio = m.nonRevisitedRatio();
		this.bestFitness = m.bestFitness();
	}
}
