package org.um.feri.analyse.EE.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import org.um.feri.analyse.EE.metrics.Util;


public class MeanStDev {
	public double mean, stdev;
	ArrayList<Double> lista;
	public static NumberFormat meanFormat=new DecimalFormat("#0.0000"), stdFormat=new DecimalFormat("#0.00");
	public MeanStDev(ArrayList<Double> lista) {
		super();
		this.lista = lista;
		stdev = standard_deviation(lista);
		mean = mean(lista);
	}
	public String toStringMean() {
		if (stdev<0.000000000001) return "{\\footnotesize $"+meanFormat.format(mean)+"$}";
		return "{\\footnotesize $"+meanFormat.format(mean)+"$}";
	}
	public String toStringStDev() {
		if (stdev<0.000000000001) return "{\\footnotesize $\\pm 0$}";
		return "{\\footnotesize $"+"\\pm"+stdFormat.format(stdev)+"$}";
	}
	
	public String toString() {
		if (stdev<0.000000000001) return "{\\footnotesize $"+meanFormat.format(mean)+"\\pm 0$}";
		return "{\\footnotesize $"+meanFormat.format(mean)+"\\pm"+stdFormat.format(stdev)+"$}";
	}
	public double getMean() {
		return mean;
	}
	public ArrayList<Double> getLista() {
		return lista;
	}
	/**
	 * @param population an array, the population
	 * @return the variance
	 */
	public double variance(ArrayList<Double> population) {
		long n = 0;
		double mean = 0;
		double s = 0.0;
		
		for (Double x : population) {
			n++;
			double delta = x.doubleValue() - mean;
			mean += delta / n;
			s += delta * (x.doubleValue() - mean);
		}
		return Util.divide(s,(n-1));
		//return (s / n);
	}

	/**
	 * @param population an array, the population
	 * @return the standard deviation
	 */
	public double standard_deviation(ArrayList<Double> population) {
		if (population.size()==0) return 0;
		double v=variance(population);
		if (v>0)
		  return Math.sqrt(variance(population));
		else return 0;
	}
    public double mean(ArrayList<Double> population) {
		if (population.size()==0) return 0;
		double sum=0;
		for (Double d:population) {
			sum+=d;
		}
		return Util.divide(sum,population.size());
    }
}
