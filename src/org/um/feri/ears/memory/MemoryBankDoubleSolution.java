package org.um.feri.ears.memory;

import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.report.Pair;
import org.um.feri.ears.util.report.ReportBank;

import java.util.HashMap;

public class MemoryBankDoubleSolution {
    public static final String FITNESS = "FIT";
    public static final String CONVERGENCE = "CON";
    public static final String CONVERGENCE_DUPLICATE = "CONdUP";
    public static final String CONVERGENCE_DUPLICATE_VALUE = "CONdUPvAL";
    public static final String DUPLICATE_CONVERGENCE = "DupCon";
    public static final String DUPLICATE_CONVERGENCE_NOT = "DupConUnique";
    public static final String EVALUATIONS_IF_DUPLICATE_STOP = "evalStopByDup";
    double precisionPower;
    int precisionInDecimalPlaces;
    int duplicationHitSum;
    int duplicationBeforeGlobal;
    StringBuilder sb;
    private HashMap<String, NumberSolution<Double>> hashMapMemory;
    private HashMap<String, Integer> hashMapMemoryHits;
    DuplicationRemovalStrategy updateStrategy;
    public static boolean convergenceGraphDataCollect = false;
    NumberSolution<Double> best4ConvergenceGraph;

    public static void convergenceGraphRecord() {
        convergenceGraphDataCollect = true;
    }

    public static void convergenceGraphRecordStop() {
        convergenceGraphDataCollect = false;
    }

    private static final int[] POW10 = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000};

    public static String format(double val, int precision) {
        StringBuilder sb = new StringBuilder();
        if (val < 0) {
            sb.append('-');
            val = -val;
        }
        int exp = POW10[precision];
        long lval = (long) (val * exp + 0.5);
        sb.append(lval / exp).append('.');
        long fval = lval % exp;
        for (int p = precision - 1; p > 0 && fval < POW10[p]; p--) {
            sb.append('0');
        }
        sb.append(fval);
        return sb.toString();
    }

    public MemoryBankDoubleSolution(int precisionInDecimalPlaces, DuplicationRemovalStrategy updateStrategy) {
        this.precisionInDecimalPlaces = precisionInDecimalPlaces;
        this.updateStrategy = updateStrategy;
        precisionPower = (long) Math.pow(10, precisionInDecimalPlaces);
        hashMapMemory = new HashMap<>(10000);
        hashMapMemoryHits = new HashMap<>(10000);
        sb = new StringBuilder();
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


    public String encodeKeyPerc(double[] x) {
        sb.setLength(0);
        for (double d : x) {
            sb.append(format(d, precisionInDecimalPlaces)).append(" ");
            //sb.append("" + Double.toString(d)).append(".");
        }
        return sb.toString();

    }


    public String encodeKey(double[] x) {
        sb.setLength(0);
        for (double d : x) {
            // sb.append(""+Double.toString(d)).append(".");
            // TODO sb.append(""+Long.toString(Double.doubleToLongBits(d),
            // Character.MAX_RADIX));
            sb.append(Double.doubleToLongBits(d)).append(".");
            //sb.append("" + Double.toString(d)).append(".");
        }
        return sb.toString();

    }

    public NumberSolution<Double> getRandomSolution(TaskWithMemory task) throws StopCriterionException {
        double[] d = task.problem.getRandomVariables();
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
        // round(x);
        NumberSolution<Double> ds;
        String key = encodeKeyPerc(x);
        //last evaluation record data
        if (1 + duplicationHitSum + task.getNumberOfEvaluations() == task.getMaxEvaluations()) {
            //Log
            ReportBank.logMemory(ReportBank.MEMORY_END + ReportBank.MFES);

            ReportBank.addDoubleValue(ReportBank.BEST + ReportBank.MFES, best4ConvergenceGraph.getEval());
            if (task.isGlobal()) {
                ReportBank.addDoubleValue(ReportBank.SR + ReportBank.MFES, 1.);
            } else
                ReportBank.addDoubleValue(ReportBank.SR + ReportBank.MFES, 0.);

            ReportBank.addDoubleValue(ReportBank.DUPLICATE_BEFORE_GLOBAL + ReportBank.MFES, task.getDuplicationBeforeGlobal());
            ReportBank.addDoubleValue(ReportBank.DUPLICATE_ALL + ReportBank.MFES, task.getDuplicationHitSum());
            ReportBank.logTime(ReportBank.TIME + ReportBank.MFES);
        }
        if (hashMapMemory.containsKey(key)) { //duplicate
            duplicationHitSum++;
            if (!task.isGlobal()) {
                duplicationBeforeGlobal++;
            }
            if (convergenceGraphDataCollect) {
                ds = hashMapMemory.get(key);
                // eval+1 one plus becuse we fake that we need additional evaluation
                ReportBank.addPairValue(CONVERGENCE_DUPLICATE, new Pair(task.getNumberOfEvaluations() + 1, best4ConvergenceGraph.getEval()));
                ReportBank.addPairValue(CONVERGENCE_DUPLICATE_VALUE, new Pair(task.getNumberOfEvaluations() + 1, ds.getEval()));
                ReportBank.addPairValue(DUPLICATE_CONVERGENCE, new Pair(task.getNumberOfEvaluations() + 1, duplicationHitSum));
            }

            if (updateStrategy.criteria4Change(hashMapMemoryHits.get(key))) {
                updateStrategy.changeSolution(x);
                return eval(task, x);
       /* round(x);
        //hashMapMemoryHits.put(key, 1); // new one� ERROR
        ds = task.evalOrg(x);
        if (converganceGraphDataCollect) {
          if (best4ConverganceGraph == null)
            best4ConverganceGraph = ds;
          else if (task.isFirstBetter(ds, best4ConverganceGraph))
            best4ConverganceGraph = ds;
        }

        hashMapMemory.put(key, ds);
        */
            } else {
                if (updateStrategy.forceIncEvaluation()) {
                    task.incrementNumberOfEvaluations(1); //no change but we...
                }
                hashMapMemoryHits.put(key, hashMapMemoryHits.get(key) + 1);
            }
            ds = new NumberSolution<>(hashMapMemory.get(key)); //do I need new?
  /*    if (converganceGraphDataCollect) {
        if (best4ConverganceGraph == null)
          best4ConverganceGraph = ds;
        else if (task.isFirstBetter(ds, best4ConverganceGraph))
          best4ConverganceGraph = ds;
        ReportBank.addPairValue(CONVERGENCE, new Pair(task.getNumberOfEvaluations(), best4ConverganceGraph.getEval()));
        ReportBank.addPairValue(FITNESS, new Pair(task.getNumberOfEvaluations(), ds.getEval()));
      }
      */
            if (task.isStopCriterion()) { // TODO be careful clear here or in main?
                clearMemory();
            }
            return ds;
        } else { //not duplicate
            hashMapMemoryHits.put(key, 1); // new one�
            ds = task.evalOrg(x);
            if (best4ConvergenceGraph == null)
                best4ConvergenceGraph = ds;
            else if (task.problem.isFirstBetter(ds, best4ConvergenceGraph))
                best4ConvergenceGraph = ds;
            if (convergenceGraphDataCollect) {
                ReportBank.addPairValue(CONVERGENCE, new Pair(task.getNumberOfEvaluations(), best4ConvergenceGraph.getEval()));
                ReportBank.addPairValue(FITNESS, new Pair(task.getNumberOfEvaluations(), ds.getEval()));
                ReportBank.addPairValue(DUPLICATE_CONVERGENCE_NOT, new Pair(task.getNumberOfEvaluations() + 1, task.getNumberOfEvaluations() + 1-duplicationHitSum));
            }
            hashMapMemory.put(key, ds);
            if (task.isStopCriterion())
                clearMemory();
            return ds;
        }
    }

    public void reset() {
        duplicationHitSum = 0;
        hashMapMemory.clear();
        duplicationBeforeGlobal = 0;
        hashMapMemoryHits.clear();
    }

    public boolean contains(NumberSolution<Double> ds) {
        return hashMapMemory.containsKey(encodeKey(Util.toDoubleArray(ds.getVariables())));
    }

    public void clearMemory() {
        System.out.println(ReportBank.keyID + " Mem:" + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
        ReportBank.logMemory(ReportBank.MEMORY_END);
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
