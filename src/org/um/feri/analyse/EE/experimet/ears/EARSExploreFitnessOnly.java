package org.um.feri.analyse.EE.experimet.ears;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.um.feri.analyse.EE.metrics.ATMetrics;
import org.um.feri.analyse.EE.metrics.PrintAMetrics;
import org.um.feri.analyse.EE.metrics.PrintStatATMetrics;
import org.um.feri.analyse.EE.metrics.StatATMetrics;
import org.um.feri.analyse.EE.tree_data.NodesEARS;

public class EARSExploreFitnessOnly {
	// RastriginEEstatPSO_a1.stat
	// SpherePSOOut_a1.STAT
	public static String dir = "D:/Doktorat/OutPutEars/EE/CEC2014/"; // on mac
																		// linux
																		// "/"
	public static String problem_1 = "DESphered100r";
	// public static String problem_2 = "SphereEEstatPSO_a";
	public static String analiza = "";

	public static String[] mixrun;
	public static int[] mixrunID;
	public static int[] printrunID; // latex column name
	public static double epsilon;
	public static String problemFiles[];

	public static void setArrays(String subdir, int i) {
		mixrun = new String[i];
		for (int j = 0; j < i; j++)
			mixrun[j] = subdir;
		mixrunID = new int[i];
		for (int j = 0; j < i; j++)
			mixrunID[j] = j + 1;
		printrunID = new int[i];
		for (int j = 0; j < i; j++)
			printrunID[j] = j + 1;
	}

	// cols2.add(createXproblmTable(problemX[i],number_of_test_repetition,scenario_type,x,false));
	private static void createXproblmTable(String fileName, int number_of_repetition, int type, int myX,
			boolean print) {
		ArrayList<String> heads = new ArrayList<String>();
		ArrayList<ArrayList<String>> cols = new ArrayList<ArrayList<String>>();
		cols.add(PrintAMetrics.getInfoColumn());
		NodesEARS n;
		ATMetrics m;
		PrintAMetrics pm = null;
		String problem2;
		StatATMetrics sam = new StatATMetrics();
		int maxgeneration = 100000;
		// long start = System.currentTimeMillis();
		// ---------------------------------------------------------
		for (int id = 0; id < number_of_repetition; id++) {
			problem2 = dir + fileName + id + ".csv";
			//System.out.println(problem2);
			n = new NodesEARS();
			n.createAll_EARS(problem2, maxgeneration, epsilon, false);
			if (type == NodesEARS.SCENARIO_OPTIMISTIC) {
				n.transformInOptimisticParetoTree();
			}
			if (type == NodesEARS.SCENARIO_SEMI_OPTIMISTIC) {
				n.transformInOptimisticPlusParetoTree();
			}
			heads.add("" + id);
			m = new ATMetrics(n.getInitTrees(), myX);
			pm = new PrintAMetrics(m);
			sam.add(m);
			cols.add(pm.getColumn());

		}
		PrintStatATMetrics statTAble = new PrintStatATMetrics(sam);
		if (print)
			System.out.println(pm.toLatex(heads, cols, fileName));
		// New table clear and print
		heads.clear();
		cols.clear();
		DecimalFormat df = new DecimalFormat("####0.000");
		int pos = fileName.indexOf('_');
		System.out.println(fileName.substring(0, pos)+" Fitness="+ df.format(statTAble.m.getBestFitness().getMean())+" ("+
				df.format(statTAble.m.getBestFitness().getStDev())+")" +
				" Explore="+df.format(statTAble.m.getExplorRatio().getMean())+"("+
				df.format(statTAble.m.getExplorRatio().getStDev())+")\\\\");

	}

	private static void mutationMainTest(String problemX[], int x, int number_of_test_repetition) {
		int scenario_type = 3; // normal
		for (int i = 0; i < problemX.length; i++) {
			if (problemX[i].contains("F01 Ellips Function")) {
				epsilon = 38.787499999999184;
			} else if (problemX[i].contains("F02 Bent Cigar Function")) {
				epsilon = 51.782499999999246;
			} else if (problemX[i].contains("F03 Discus Function")) {
				epsilon = 55.24749999999932;
			} else if (problemX[i].contains("F04 Rosenbrock Function")) {
				epsilon = 45.67999999999933;
			} else if (problemX[i].contains("F05 Ackley Function")) {
				epsilon = 0.5674999999999901;
			} else if (problemX[i].contains("F06 Weierstrass Function")) {
				epsilon = 0.5574999999999901;
			} else if (problemX[i].contains("F07 Griewank Function")) {
				epsilon = 0.3524999999999993;
			} else if (problemX[i].contains("F08 Rastrigin Function")) {
				epsilon = 5.329999999999864;
			} else if (problemX[i].contains("F09 Rastrigin Function")) {
				epsilon = 3.9300000000000046;
			} else if (problemX[i].contains("F10 Schwefel function")) {
				epsilon = 3.112499999999925;
			} else if (problemX[i].contains("F11 Schwefel function")) {
				epsilon = 4.582499999999987;
			} else if (problemX[i].contains("F12 Katsuura function")) {
				epsilon = 1.172500000000008;
			} else if (problemX[i].contains("F13 HappyCat function")) {
				epsilon = 59.142499999999224;
			} else if (problemX[i].contains("F14 HGBat function")) {
				epsilon = 41.489999999999405;
			} else if (problemX[i].contains("F15 Griewank-Rosenbrock function")) {
				epsilon = 30.162499999999614;
			} else if (problemX[i].contains("F16 Expanded Scaffer's function")) {
				epsilon = 2.264999999999997;
			}
			//System.out.println("Start: " + (i + 1) + "/" + problemX.length + "epsilon:" + epsilon);
			createXproblmTable(problemX[i], number_of_test_repetition, scenario_type, x, false);
		};
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		setArrays(dir, 1);
		epsilon = 0; // for binary vector is any value less than 1 ok!
		int number_of_test_repetition = 10;
		int x = 1; // X dimension-s is/are changed by epsilon
		problemFiles = new String[6];
		problemFiles[0] = "RWSi_"+args[0];
		problemFiles[1] = "HillClimbing_"+args[0]; 
		problemFiles[2] = "JADE_"+args[0];
		problemFiles[3] = "DE-best-1-bin_"+args[0]; 
		problemFiles[4] = "jDElscop_"+args[0]; 
		problemFiles[5] = "TLBO_"+args[0];

		mutationMainTest(problemFiles, x, number_of_test_repetition);

	}

}
