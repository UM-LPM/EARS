package org.um.feri.ears.memory;

import org.um.feri.ears.problems.*;
import org.um.feri.ears.util.Util;

public class TaskWithMemory extends Task<NumberSolution<Double>, DoubleProblem> {
    protected MemoryBankDoubleSolution mb;
    int xPrecision;
    private int stopAtEval;
    StringBuilder sb;
    boolean stopWhenPercDuplicates;
    int stopIfDuplicatesCount;
    private static int NOT_SET_EVAL=-1;
    private boolean isStagnation;
    int internalStagnationCounter;
    NumberSolution<Double> best;

    public TaskWithMemory(StopCriterion stop, int eval, long allowedTime, int maxIterations, double epsilon,
                          DoubleProblem p, int xPrecision, DuplicationRemovalStrategy strategy, int stopDuplicatesStagnationPerc) {
        this(stop, eval, allowedTime, maxIterations, epsilon, p, xPrecision, strategy);
        stopWhenPercDuplicates = true;
        stopIfDuplicatesCount = eval/stopDuplicatesStagnationPerc;
        stopAtEval=NOT_SET_EVAL; //NOT_REACH
        internalStagnationCounter = 0;
        if (stop==StopCriterion.STAGNATION) { //fake it TODO
            isStagnation = (stop == StopCriterion.STAGNATION);
            this.stopCriterion=StopCriterion.EVALUATIONS; //
            stopWhenPercDuplicates = false;
            maxTrialsBeforeStagnation = eval/stopDuplicatesStagnationPerc;
        }


    }

    public TaskWithMemory(StopCriterion stop, int eval, long allowedTime, int maxIterations, double epsilon,
                          DoubleProblem p, int xPrecision, DuplicationRemovalStrategy strategy) {
        super(p, stop, eval, allowedTime, maxIterations, epsilon);
        stopWhenPercDuplicates = false;
        this.xPrecision = xPrecision;
        strategy.setTask(this);
        mb = new MemoryBankDoubleSolution(xPrecision, strategy);
        sb = new StringBuilder();
    }

    public NumberSolution<Double> evalOrg(double[] x) throws StopCriterionException {

        NumberSolution<Double> tmp = new NumberSolution<>(Util.toDoubleArrayList(x));
        super.eval(tmp);

        if (isStagnation) {
            if (best == null) {
                best = tmp;
                internalStagnationCounter = 1;
            }
            else {
                if (problem.isFirstBetter(tmp,best)) {
                    best= tmp;
                    internalStagnationCounter = 0;
                } else
                    ++internalStagnationCounter;
            }
        }
        return tmp;
    }

    @Override
    public NumberSolution<Double> getRandomEvaluatedSolution() throws StopCriterionException {
        return mb.getRandomSolution(this);
    }

    @Override
    public String toString() {
        sb.setLength(0);
        sb.append(super.toString()).append("\n");
        sb.append(mb);
        return sb.toString();
    }

    @Override
    public boolean isStopCriterion() {
        if (isStagnation) {
           if (internalStagnationCounter>=getMaxTrialsBeforeStagnation()) {
               stopAtEval = numberOfEvaluations;
               isStop = true;
               return true;
           }
        }
        if (stopWhenPercDuplicates) {
            if (mb.getDuplicationHitSum()>=stopIfDuplicatesCount) {
                stopAtEval = numberOfEvaluations;
                isStop = true;
                return true;
            }
        }
        return super.isStopCriterion();
    }

    public int getStopAtEval() {
        if (stopAtEval==NOT_SET_EVAL) return numberOfEvaluations;
        return stopAtEval;
    }

    public int getDuplicationHitSum() {
        return mb.getDuplicationHitSum();
    }

    public int getDuplicationBeforeGlobal() {
        return mb.getDuplicationBeforeGlobal();
    }

}
