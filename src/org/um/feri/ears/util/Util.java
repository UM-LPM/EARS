package org.um.feri.ears.util;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.moo.ParetoSolution;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Util {
    public static DecimalFormat df0 = new DecimalFormat("####.##");
    public static DecimalFormat df01 = new DecimalFormat("####.####");
    public static DecimalFormat df1 = new DecimalFormat("#,###.#");
    public static DecimalFormat df = new DecimalFormat("#,###.##");
    public static DecimalFormat df3 = new DecimalFormat("#,###.###");
    public static DecimalFormat df6 = new DecimalFormat("#,###.######");
    public static DecimalFormat dfc1 = new DecimalFormat("#,##0.#######E0");
    public static DecimalFormat dfc2 = new DecimalFormat("#,##0.##E0");
    public static DecimalFormat dfcshort = new DecimalFormat("0.##E0");
    public static DecimalFormat intf = new DecimalFormat("###,###,###");
    static final String JSON_DIR = "Cache\\Pareto_Cache_%s.json";

    public static double roundDouble3(double r) {
        return roundDouble(r, 3);
    }

    public static double roundDouble(double r, int dec) {
        r = Math.round(r * Math.pow(10, dec));
        return r / Math.pow(10, dec);
    }

    public static String arrayToString(List<Double> d) {
        String s = "";
        for (int i = 0; i < d.size(); i++) {
            s = s + df01.format(d.get(i));
            if (i < d.size() - 1)
                s = s + ", ";
        }
        return s;
    }

    public static String arrayToString(double[] d) {
        String s = "";
        for (int i = 0; i < d.length; i++) {
            s = s + df01.format(d[i]);
            if (i < d.length - 1)
                s = s + ", ";
        }
        return s;
    }

    public static <T extends Number> void addParetoToJSON(String listID, String benchmark, String file, ParetoSolution<T> best) {
        ParetoSolutionCache cache = new ParetoSolutionCache();
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();

        try {
            BufferedReader br = new BufferedReader(new FileReader(String.format(JSON_DIR, file + "_" + benchmark)));
            cache = gson.fromJson(br, ParetoSolutionCache.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (cache == null)
            cache = new ParetoSolutionCache();

        if (cache.data.containsKey(listID)) {
            if (cache.data.get(listID).size() >= cache.limit) {
                return;
            }
            //List<List<double[]>> solutions = cache.data.get(listID);
            List<ParetoWithEval> pareto = cache.data.get(listID);

            ParetoWithEval paretoList = new ParetoWithEval();

            for (NumberSolution<T> s : best.solutions) {
                double[] objectives = s.getObjectives();
                //check for NaN
                for (int i = 0; i < objectives.length; i++) {
                    if (Double.isNaN(objectives[i])) {
                        System.err.println("Pareto contains NaN!");
                        return;
                    }
                }

                paretoList.pareto.add(objectives);
            }

            HashMap<String, Double> allQI = best.getAllQiEval();
            for (Entry<String, Double> qi : allQI.entrySet()) {
                if (Double.isNaN(qi.getValue())) {
                    System.out.println("QI value is NaN for: " + qi.getKey());
                    allQI.put(qi.getKey(), Double.MAX_VALUE);
                }
            }

            paretoList.qiEval = allQI;
            pareto.add(paretoList);
            cache.data.put(listID, pareto);

        } else {
            //List<List<double[]>> solutions = new ArrayList<List<double[]>>();
            List<ParetoWithEval> pareto = new ArrayList<ParetoWithEval>();

            ParetoWithEval paretoList = new ParetoWithEval();

            for (NumberSolution<T> s : best.solutions) {
                double[] objectives = s.getObjectives();
                //check for NaN
                for (int i = 0; i < objectives.length; i++) {
                    if (Double.isNaN(objectives[i])) {
                        System.err.println("Pareto contains NaN!");
                        return;
                    }
                }

                paretoList.pareto.add(objectives);
            }

            HashMap<String, Double> allQI = best.getAllQiEval();
            for (Entry<String, Double> qi : allQI.entrySet()) {
                if (Double.isNaN(qi.getValue())) {
                    System.out.println("QI value is NaN for: " + qi.getKey());
                    allQI.put(qi.getKey(), Double.MAX_VALUE);
                }
            }

            paretoList.qiEval = allQI;
            pareto.add(paretoList);
            cache.data.put(listID, pareto);
        }

        try (Writer writer = new FileWriter(String.format(JSON_DIR, file + "_" + benchmark))) {
            gson.toJson(cache, writer);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ww");
        }
    }

    public static ParetoSolutionCache readParetoListFromJSON(String file, String benchmark) {
        System.out.println("Loading cache for algorithm: " + file + " and benchmark: " + benchmark);
        ParetoSolutionCache cache = new ParetoSolutionCache();
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();

        try {
            BufferedReader br = new BufferedReader(new FileReader(String.format(JSON_DIR, file + "_" + benchmark)));
            cache = gson.fromJson(br, ParetoSolutionCache.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cache;
    }
	
/*
	public static void printPopulationCompare(double d[][], double e[],
			Problem p, int gen) {
		System.out.println("Generation:" + gen);
		for (int i = 0; i < d.length; i++) {
			System.out.println(arrayToString(d[i]) + " eval:" + e[i]
					+ " real: " + p.eval(d[i]) + " constrain: "
					+ p.constrainsEvaluations(d[i]));
			if (e[i] != p.eval(d[i]))
				System.out.println("ERROR!!!");
		}
	}

	public static void printPopulation(double d[][], Problem p, int gen) {
		System.out.println("Generation:" + gen);
		for (int i = 0; i < d.length; i++) {
			System.out.println(arrayToString(d[i]) + " eval:" + p.eval(d[i])
					+ " constrain: " + p.constrainsEvaluations(d[i]));
		}
	}*/

    public static void writeToFile(String fileLocation, String data) {

        File file = new File(fileLocation);
        String directory = file.getParent();
        if (directory == null) {
            directory = System.getProperty("user.dir");
            fileLocation = directory + File.separator + fileLocation;
            file = new File(fileLocation);
        }
        File fileDirectory = new File(directory);
        try {
            fileDirectory.mkdirs();
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false)))) {
            bw.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFromFile(String fileLocation) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fileLocation))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    public static double max(double... args) {
        double maxValue = args[0];
        for (double arg : args) {
            maxValue = Math.max(maxValue, arg);
        }
        return maxValue;
    }

    public static <T> List<T> list(T... items) {
        List<T> list = new LinkedList<T>();
        for (T item : items) {
            list.add(item);
        }
        return list;
    }

    public static double[] toDoubleArray(List<Double> list) {
        return list.stream().mapToDouble(d -> d).toArray();
    }

    public static int[] toIntArray(List<Integer> list) {
        return list.stream().mapToInt(i -> i).toArray();
    }

    public static ArrayList<Double> toDoubleArrayList(double[] array) {

        return Arrays.stream(array).boxed().collect(Collectors.toCollection(ArrayList::new));
    }
}
