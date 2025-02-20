package org.um.feri.ears.util.report;

import java.util.ArrayList;
import java.util.HashMap;

public class ReportBank {
    public static final String DUPLICATE_BEFORE_GLOBAL = "DBG";
    public static final String DUPLICATE_ALL = "DALL";
    public static final String SR = "SR";
    public static final String BEST = "BEST";
    public static final String HITS = "HITS";
    public static final String TIME = "TIME";
    public static final String MEMORY_START = "MEMORY_START";
    public static final String MEMORY_END = "MEMORY_END";

    public static final String MFES = "MFES";

    private static ReportBank single;
    public static String keyID = ""; //prefix key
    public static long startTime;

    public static ReportBank getSingelton() {
        if (single == null) return single = new ReportBank();
        return single;
    }

    public static void resetTime() {
        startTime = System.currentTimeMillis();
    }

    public static void logMemory(String key) {
        ReportBank.addDoubleValueNoID(key, (double) (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));

    }

    public static void logTime(String key) {
        ReportBank.addDoubleValue(key, (double) (System.currentTimeMillis() - startTime));
    }

    public static void setSingle(ReportBank s) {
        single = s;
    }

    public ReportBank() {
        listDoubles = new HashMap<>();
        listPairs = new HashMap<>();
    }

    private static boolean collectReport = false;

    public static boolean isCollectReport() {
        return collectReport;
    }

    public static void setCollectReport(boolean collectReport) {
        ReportBank.collectReport = collectReport;
        if (isCollectReport()) {
            getSingelton();
        } else {
            single = null;
        }
    }

    private HashMap<String, ArrayList<Pair>> listPairs;
    private HashMap<String, ArrayList<Double>> listDoubles;

    public static void addDoubleValue(String key, double value) {
        if (getSingelton().listDoubles.containsKey(keyID + key)) {
            getSingelton().listDoubles.get(keyID + key).add(value);
        } else {
            ArrayList<Double> tmp = new ArrayList<Double>();
            tmp.add(value);
            getSingelton().listDoubles.put(keyID + key, tmp);
        }
    }

    public static void addDoubleValues(String key, ArrayList<Double> values) {
        if (getSingelton().listDoubles.containsKey(keyID + key)) {
            getSingelton().listDoubles.get(keyID + key).addAll(values);
        } else {
            ArrayList<Double> tmp = new ArrayList<Double>();
            tmp.addAll(values);
            getSingelton().listDoubles.put(keyID + key, tmp);
        }
    }

    public static void addDoubleValueNoID(String key, double value) {
        if (getSingelton().listDoubles.containsKey(key)) {
            getSingelton().listDoubles.get(key).add(value);
        } else {
            ArrayList<Double> tmp = new ArrayList<Double>();
            tmp.add(value);
            getSingelton().listDoubles.put(key, tmp);
        }
    }

    public static void addPairValue(String key, Pair value) {
        if (getSingelton().listPairs.containsKey(keyID + key)) {
            getSingelton().listPairs.get(keyID + key).add(value);
        } else {
            ArrayList<Pair> tmp = new ArrayList<Pair>();
            tmp.add(value);
            getSingelton().listPairs.put(keyID + key, tmp);
        }
    }

    public static ArrayList<Double> getDoubleList(String key) {
        //if (!getSingelton().listDoubles.containsKey(key)) System.out.println("No key:" + key);
        return getSingelton().getDoubleListO(key);
    }

    public ArrayList<Double> getDoubleListO(String key) {
        if (!listDoubles.containsKey(key)) System.out.println("No key:" + key);
        return listDoubles.get(key);
    }

    public static ArrayList<Pair> getPairList(String key) {
        //if (!getSingelton().listPairs.containsKey(key)) System.out.println("No key:"+key);
        return getSingelton().getPairListO(key);
    }

    public ArrayList<Pair> getPairListO(String key) {
        if (!listPairs.containsKey(key)) System.out.println("No key:" + key);
        return listPairs.get(key);
    }


}
