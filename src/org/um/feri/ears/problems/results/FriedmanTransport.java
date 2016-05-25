package org.um.feri.ears.problems.results;

import java.util.Arrays;
import java.util.Vector;

public class FriedmanTransport {
	private double[][] mean;
	private Vector<String> algoritms;
	private Vector<String> datasets;
	public FriedmanTransport(double[][] mean, Vector<String> algoritms, Vector<String> datasets) {
		super();
		this.mean = mean;
		this.algoritms = algoritms;
		this.datasets = datasets;
	}
	public void print() {
		System.out.println(algoritms);
		System.out.println(datasets);
		System.out.println(Arrays.toString(mean));		
	}
	public double[][] getMean() {
		return mean;
	}
	public Vector<String> getAlgoritms() {
		return algoritms;
	}
	public Vector<String> getDatasets() {
		return datasets;
	}
	
	
}
