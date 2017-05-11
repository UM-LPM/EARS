package org.um.feri.analyse.EE.metrics;

import java.util.ArrayList;

import org.um.feri.analyse.EE.util.MeanStDev;


public class StatATMetrics {
	ArrayList<MetricsValues> all;
	ArrayList<Double> tmpLista;
	public StatATMetrics() {
		tmpLista = new ArrayList<Double>();
		all = new ArrayList<MetricsValues>();
	}
	public void add(ATMetrics m) {
		all.add(new MetricsValues(m));
	}
	
	public MeanStDev getExplorRatio() {
		tmpLista.clear();
		for (MetricsValues m:all) tmpLista.add(m.getExplorRatio());
		return new MeanStDev(tmpLista);
	}

	public MeanStDev getExplorType_c() {
		tmpLista.clear();
		for (MetricsValues m:all) tmpLista.add(m.getExplorType_c());
		return new MeanStDev(tmpLista);
	}

	public MeanStDev getExplorType_m() {
		tmpLista.clear();
		for (MetricsValues m:all) tmpLista.add(m.getExplorType_m());
		return new MeanStDev(tmpLista);
	}

	public MeanStDev getExplorType_r() {
		tmpLista.clear();
		for (MetricsValues m:all) tmpLista.add(m.getExplorType_r());
		return new MeanStDev(tmpLista);
	}

	public MeanStDev getExplorType_rnd() {
		tmpLista.clear();
		for (MetricsValues m:all) tmpLista.add(m.getExplorType_rnd());
		return new MeanStDev(tmpLista);
	}

	public MeanStDev getExplorGap_1() {
		tmpLista.clear();
		for (MetricsValues m:all) tmpLista.add(m.getExplorGap_1().mean);
		return new MeanStDev(tmpLista);
	}
	
	public MeanStDev getExplorGap_1_std() {
		tmpLista.clear();
		for (MetricsValues m:all) tmpLista.add(m.getExplorGap_1().stdev);
		return new MeanStDev(tmpLista);
	}

	public MeanStDev getExplorProgressiveness() {
		tmpLista.clear();
		for (MetricsValues m:all) tmpLista.add(m.getExplorProgressiveness().mean);
		return new MeanStDev(tmpLista);
	}
	
	public MeanStDev getExplorProgressiveness_std() {
		tmpLista.clear();
		for (MetricsValues m:all) tmpLista.add(m.getExplorProgressiveness().stdev);
		return new MeanStDev(tmpLista);
	}
	public MeanStDev getExploitProgressiveness() {
		tmpLista.clear();
		for (MetricsValues m:all) tmpLista.add(m.getExploitProgressiveness().mean);
		return new MeanStDev(tmpLista);
	}
	
	public MeanStDev getExploitProgressiveness_std() {
		tmpLista.clear();
		for (MetricsValues m:all) tmpLista.add(m.getExploitProgressiveness().stdev);
		return new MeanStDev(tmpLista);
	}
	public MeanStDev getExploitRatio() {
		tmpLista.clear();
		for (MetricsValues m:all) tmpLista.add(m.getExploitRatio());
		return new MeanStDev(tmpLista);
	}

	public MeanStDev getExploitType_c() {
		tmpLista.clear();
		for (MetricsValues m:all) tmpLista.add(m.getExploitType_c());
		return new MeanStDev(tmpLista);
	}

	public MeanStDev getExploitType_m() {
		tmpLista.clear();
		for (MetricsValues m:all) tmpLista.add(m.getExploitType_m());
		return new MeanStDev(tmpLista);
	}

	public MeanStDev getExploitType_r() {
		tmpLista.clear();
		for (MetricsValues m:all) tmpLista.add(m.getExploitType_r());
		return new MeanStDev(tmpLista);
	}

	public MeanStDev getExploitType_cln() {
		tmpLista.clear();
		for (MetricsValues m:all) tmpLista.add(m.getExploitType_cln());
		return new MeanStDev(tmpLista);
	}

	public MeanStDev getExploitSelectionPressure() {
		tmpLista.clear();
		for (MetricsValues m:all) tmpLista.add(m.getExploitSelectionPressure());
		return new MeanStDev(tmpLista);
	}
	public MeanStDev getExploreSelectionPressure() {
		tmpLista.clear();
		for (MetricsValues m:all) tmpLista.add(m.getExploreSelectionPressure());
		return new MeanStDev(tmpLista);
	}
	public MeanStDev getCountAllNodes() {
		tmpLista.clear();
		for (MetricsValues m:all) tmpLista.add(m.getCountAllNodes());
		return new MeanStDev(tmpLista);
	}
	
	public MeanStDev getNonRevisitedRatio() {
		tmpLista.clear();
		for (MetricsValues m:all) tmpLista.add(m.getNonRevisitedRatio());
		return new MeanStDev(tmpLista);
	}
	
	public MeanStDev getBestFitness()
	{
		tmpLista.clear();
		for(MetricsValues m:all)
		{
			tmpLista.add(m.getBestFitness());
		}
		return new MeanStDev(tmpLista);
	}
	
	

}
