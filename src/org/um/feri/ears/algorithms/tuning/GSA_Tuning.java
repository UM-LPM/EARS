package org.um.feri.ears.algorithms.tuning;

import java.util.ArrayList;

import org.um.feri.ears.algorithms.so.gsa.GSA;

public class GSA_Tuning extends GSA {
	  public GSA_Tuning(ArrayList<Double> conf, String aName) {
		  super(conf.get(0),conf.get(1), conf.get(2)); //public GSA(double RPower, double alfa, double G0)
		  aName = aName + "-RPower" + String.format("%.1f",conf.get(0))+ "-alfa" + String.format("%.1f",conf.get(1))+ "-G0" + String.format("%.1f",conf.get(2));
		  ai.setVersionAcronym(aName);
		  ai.setPublishedAcronym(aName);
		  ai.setVersionDescription(aName);
		  controlParameters = conf;
	  }
}
  