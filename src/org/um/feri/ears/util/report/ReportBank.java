package org.um.feri.ears.util.report;

import java.util.ArrayList;
import java.util.HashMap;

public class ReportBank {
	private static boolean collectReport = false;

	public static boolean isCollectReport() {
		return collectReport;
	}

	public static void setCollectReport(boolean collectReport) {
		ReportBank.collectReport = collectReport;
		if (isCollectReport()) {
		  listDoubles = new HashMap<>();
      listPairs = new HashMap<>();
		}
		else {
		  listPairs = null;
		  listDoubles = null;
		}
	}
	
  private static HashMap<String, ArrayList<Pair>> listPairs;
	private static HashMap<String, ArrayList<Double>> listDoubles;
	
	public static void addDoubleValue(String key, double value) {
		if (listDoubles.containsKey(key)) {
			listDoubles.get(key).add(value);
		} else {
			 ArrayList<Double> tmp = new  ArrayList<Double>();
			 tmp.add(value);
			 listDoubles.put(key,  tmp);
		}	
	}
  public static void addPairValue(String key, Pair value) {
    if (listPairs.containsKey(key)) {
      listPairs.get(key).add(value);
    } else {
       ArrayList<Pair> tmp = new  ArrayList<Pair>();
       tmp.add(value);
       listPairs.put(key,  tmp);
    } 
  }

	public static ArrayList<Double> getDoubleList(String key) {
		return listDoubles.get(key);
	}
  public static ArrayList<Pair> getPairList(String key) {
    return listPairs.get(key);
  }

	
}
