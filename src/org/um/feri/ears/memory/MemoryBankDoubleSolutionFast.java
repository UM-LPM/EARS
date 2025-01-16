package org.um.feri.ears.memory;

import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.util.report.Pair;
import org.um.feri.ears.util.report.ReportBank;

import java.util.HashMap;

public class MemoryBankDoubleSolutionFast {
    public static final String FITNESS = "FIT";
    public static final String CONVERGENCE = "CON";
    public static final String CONVERGENCE_DUPLICATE = "CONdUP";
    public static final String CONVERGENCE_DUPLICATE_VALUE = "CONdUPvAL";
    double precisionPower;
    int precisionInDecimalPlaces;
    int duplicationHitSum;
    int duplicationBeforeGlobal;
    private HashMap<Long, NumberSolution> hashMapMemory;
    private HashMap<Long, Integer> hashMapMemoryHits;
    DuplicationRemovalStrategy updateStrategy;
    public static boolean convergenceGraphDataCollect = false;
    NumberSolution<Double> best4ConvergenceGraph;

    public static void convergenceGraphRecord() {
        convergenceGraphDataCollect = true;
    }

    public static void convergenceGraphRecordStop() {
        convergenceGraphDataCollect = false;
    }

    private static final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_-";

    /*
     * public static String encodeBase64(long v) { char[] a =
     * alphabet.toCharArray(); v = Math.abs(v); String s = ""; for (int i = 0; i <
     * 11; i++) { long val = v & 63; s = a[(int) val] + s; v >>= 6; } while
     * (s.startsWith("A") && s.length() > 1) s = s.substring(1, s.length()); return
     * s; }
     *
     * public static long decodeBase64(String s) { char[] a =
     * alphabet.toCharArray(); Map<Character, Integer> map = new HashMap<>(); for
     * (int i = 0; i < a.length; i++) map.put(a[i], i); char[] chars =
     * s.toCharArray(); long v = 0; for (char c : chars) { v <<= 6; v = v |
     * map.get(c); } return v; }
     */
    public MemoryBankDoubleSolutionFast(int precisionInDecimalPlaces, DuplicationRemovalStrategy updateStrategy) {
        this.precisionInDecimalPlaces = precisionInDecimalPlaces;
        this.updateStrategy = updateStrategy;
        precisionPower = (long) Math.pow(10, precisionInDecimalPlaces);
        hashMapMemory = new HashMap<>();
        hashMapMemoryHits = new HashMap<>();
        reset();
    }

    private double round(double v) {
        return Math.floor(precisionPower * v + 0.5) / precisionPower;
    }

    public void round(double[] x) {
        for (int i = 0; i < x.length; i++) {
            x[i] = round(x[i]);
        }
    }

    public long fastKeyNot100Unique(double[] x) {
        long res = 0;
        for (int i = 0; i < x.length; i++) {
            //for (double d : x) {
            res += (precisionPower * x[i]) + Math.pow(10, i);
        }
        return res;

    }


    public NumberSolution<Double> generateRandomSolution(TaskWithMemory task) throws StopCriterionException {
        double[] d = task.problem.generateRandomVariables();
        return eval(task, d);
    }
    /*
     * public String encodeKey(double x[]) { StringBuffer sb = new StringBuffer();
     * for(double d:x) { sb.append(""+Double.doubleToLongBits(d)).append("."); }
     * return sb.toString();
     *
     * }
     */

    public NumberSolution<Double> eval(TaskWithMemory task, double[] x) throws StopCriterionException {
        round(x);
        NumberSolution<Double> ds;
        Long key = fastKeyNot100Unique(x);
        if (hashMapMemory.containsKey(key)) {
            duplicationHitSum++;
            if (!task.isGlobal()) {
                duplicationBeforeGlobal++;
            }
            if (convergenceGraphDataCollect) {
                ds = hashMapMemory.get(key);
                // eval+1 one plus because we fake that we need additional evaluation
                ReportBank.addPairValue(CONVERGENCE_DUPLICATE, new Pair(task.getNumberOfEvaluations() + 1, best4ConvergenceGraph.getEval()));
                ReportBank.addPairValue(CONVERGENCE_DUPLICATE_VALUE, new Pair(task.getNumberOfEvaluations() + 1, ds.getEval()));
            }

            if (updateStrategy.criteria4Change(hashMapMemoryHits.get(key))) {
                updateStrategy.changeSolution(x);
                hashMapMemoryHits.put(key, 1); // new one�
                ds = task.evalOrg(x);
                if (convergenceGraphDataCollect) {
                    if (best4ConvergenceGraph == null)
                        best4ConvergenceGraph = ds;
                    else if (task.problem.isFirstBetter(ds, best4ConvergenceGraph))
                        best4ConvergenceGraph = ds;
                }

                hashMapMemory.put(key, ds);
            } else {
                hashMapMemoryHits.put(key, hashMapMemoryHits.get(key) + 1);
            }
            ds = hashMapMemory.get(key);
            if (convergenceGraphDataCollect) {
                if (best4ConvergenceGraph == null)
                    best4ConvergenceGraph = ds;
                else if (task.problem.isFirstBetter(ds, best4ConvergenceGraph))
                    best4ConvergenceGraph = ds;
                ReportBank.addPairValue(CONVERGENCE, new Pair(task.getNumberOfEvaluations(), best4ConvergenceGraph.getEval()));
                ReportBank.addPairValue(FITNESS, new Pair(task.getNumberOfEvaluations(), ds.getEval()));
            }
            if (task.isStopCriterion()) { // TODO be careful clear here or in main?
                clearMemory();
            }
            return ds;
        }
        hashMapMemoryHits.put(key, 1); // new one�
        ds = task.evalOrg(x);
        if (convergenceGraphDataCollect) {
            if (best4ConvergenceGraph == null)
                best4ConvergenceGraph = ds;
            else if (task.problem.isFirstBetter(ds, best4ConvergenceGraph))
                best4ConvergenceGraph = ds;
            ReportBank.addPairValue(CONVERGENCE, new Pair(task.getNumberOfEvaluations(), best4ConvergenceGraph.getEval()));
            ReportBank.addPairValue(FITNESS, new Pair(task.getNumberOfEvaluations(), ds.getEval()));
        }
        hashMapMemory.put(key, ds);
        if (task.isStopCriterion())
            clearMemory();
        return ds;
    }

    public void reset() {
        duplicationHitSum = 0;
        hashMapMemory.clear();
        duplicationBeforeGlobal = 0;
        hashMapMemoryHits.clear();
    }

    public void clearMemory() {
        hashMapMemory.clear();
        hashMapMemoryHits.clear();
        System.gc();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Precision decimal:").append(precisionInDecimalPlaces).append(" ");
        sb.append("Double hits:").append(duplicationHitSum).append(" ");
        sb.append("Double before global:").append(duplicationBeforeGlobal).append(" ");
        sb.append("Max single hit:").append(updateStrategy).append(" ");
        return sb.toString();
    }

    public int getDuplicationHitSum() {
        return duplicationHitSum;
    }

    public int getDuplicationBeforeGlobal() {
        return duplicationBeforeGlobal;
    }

}
