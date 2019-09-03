package org.um.feri.ears.memory;

import java.util.HashMap;

import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.report.Pair;
import org.um.feri.ears.util.report.ReportBank;

public class MemoryBankDoubleSolutionFast {
  public static final String FITNESS = "FIT";
  public static final String CONVERGENCE = "CON";
  public static final String CONVERGENCE_DUPLICATE = "CONdUP";
  public static final String CONVERGENCE_DUPLICATE_VALUE = "CONdUPvAL";
  double precisionPower;
  int precisionInDecimalPlaces;
  int duplicationHitSum;
  int duplicationBeforeGlobal;
  private HashMap<Long, DoubleSolution> hashMapMemory;
  private HashMap<Long, Integer> hashMapMemoryHits;
  DuplicationRemovalStrategy updateStrategy;
  public static boolean converganceGraphDataCollect = false;
  DoubleSolution best4ConverganceGraph;

  public static void convergenceGraphRecord() {
    converganceGraphDataCollect = true;
  }
  public static void convergenceGraphRecordStop() {
    converganceGraphDataCollect = false;
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

  public void round(double x[]) {
    for (int i = 0; i < x.length; i++) {
      x[i] = round(x[i]);
    }
  }

  public long fastKeyNot100Unique(double x[]) {
    long res = 0;
    for (int i=0; i<x.length; i++) {
    //for (double d : x) {
      res+=(precisionPower * x[i])+Math.pow(10, i);
    }
    return res;

  }

  
  public DoubleSolution getRandomSolution(TaskWithMemory task) throws StopCriteriaException {
    double[] d = task.getRandomVariables();
    return eval(task, d);
  }
  /*
   * public String encodeKey(double x[]) { StringBuffer sb = new StringBuffer();
   * for(double d:x) { sb.append(""+Double.doubleToLongBits(d)).append("."); }
   * return sb.toString();
   * 
   * }
   */

  public DoubleSolution eval(TaskWithMemory task, double x[]) throws StopCriteriaException {
    round(x);
    DoubleSolution ds;
    Long key = fastKeyNot100Unique(x);
    if (hashMapMemory.containsKey(key)) {
      duplicationHitSum++;
      if (!task.isGlobal()) {
        duplicationBeforeGlobal++;
      }
      if (converganceGraphDataCollect) {
        ds = hashMapMemory.get(key);
        // eval+1 one plus becuse we fake that we need additional evaluation
        ReportBank.addPairValue(CONVERGENCE_DUPLICATE, new Pair(task.getNumberOfEvaluations() + 1, best4ConverganceGraph.getEval()));
        ReportBank.addPairValue(CONVERGENCE_DUPLICATE_VALUE, new Pair(task.getNumberOfEvaluations() + 1, ds.getEval()));
      }

      if (updateStrategy.criteria4Change(hashMapMemoryHits.get(key))) {
        updateStrategy.changeSolution(x);
        hashMapMemoryHits.put(key, 1); // new one�
        ds = task.evalOrg(x);
        if (converganceGraphDataCollect) {
          if (best4ConverganceGraph == null)
            best4ConverganceGraph = ds;
          else if (task.isFirstBetter(ds, best4ConverganceGraph))
            best4ConverganceGraph = ds;
        }

        hashMapMemory.put(key, ds);
      } else {
        hashMapMemoryHits.put(key, hashMapMemoryHits.get(key) + 1);
      }
      ds = hashMapMemory.get(key);
      if (converganceGraphDataCollect) {
        if (best4ConverganceGraph == null)
          best4ConverganceGraph = ds;
        else if (task.isFirstBetter(ds, best4ConverganceGraph))
          best4ConverganceGraph = ds;
        ReportBank.addPairValue(CONVERGENCE, new Pair(task.getNumberOfEvaluations(), best4ConverganceGraph.getEval()));
        ReportBank.addPairValue(FITNESS, new Pair(task.getNumberOfEvaluations(), ds.getEval()));
      }
      if (task.isStopCriteria()) { // TODO be careful clear here or in main?
        clearMemory();
      }
      return ds;
    }
    hashMapMemoryHits.put(key, 1); // new one�
    ds = task.evalOrg(x);
    if (converganceGraphDataCollect) {
      if (best4ConverganceGraph == null)
        best4ConverganceGraph = ds;
      else if (task.isFirstBetter(ds, best4ConverganceGraph))
        best4ConverganceGraph = ds;
      ReportBank.addPairValue(CONVERGENCE, new Pair(task.getNumberOfEvaluations(), best4ConverganceGraph.getEval()));
      ReportBank.addPairValue(FITNESS, new Pair(task.getNumberOfEvaluations(), ds.getEval()));
    }
    hashMapMemory.put(key, ds);
    if (task.isStopCriteria())
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
    StringBuffer sb = new StringBuffer();
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
