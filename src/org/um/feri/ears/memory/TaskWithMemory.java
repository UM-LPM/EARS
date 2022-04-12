package org.um.feri.ears.memory;

import org.um.feri.ears.problems.*;

public class TaskWithMemory extends Task {
    protected MemoryBankDoubleSolution mb;
    int xPrecision;
    private int stopAtEval;
    StringBuilder sb;
    boolean stopWhenPercDuplicates;
    int stopIfDuplicatesCount;
    private static int NOT_SET_EVAL=-1;

    public TaskWithMemory(StopCriterion stop, int eval, long allowedTime, int maxIterations,
                          Problem p, int xPrecision, DuplicationRemovalStrategy strategy, int stopDuplicatesPerc) {
        this(stop, eval, allowedTime, maxIterations, p, xPrecision, strategy);
        stopWhenPercDuplicates = true;
        stopIfDuplicatesCount = eval/stopDuplicatesPerc;
        stopAtEval=NOT_SET_EVAL; //NOT_REACH

    }

    public TaskWithMemory(StopCriterion stop, int eval, long allowedTime, int maxIterations,
                          Problem p, int xPrecision, DuplicationRemovalStrategy strategy) {
        super(p, stop, eval, allowedTime, maxIterations);
        stopWhenPercDuplicates = false;
        this.xPrecision = xPrecision;
        strategy.setTask(this);
        mb = new MemoryBankDoubleSolution(xPrecision, strategy);
        sb = new StringBuilder();
    }

    public DoubleSolution evalOrg(double[] x) throws StopCriterionException {
        return super.eval(x);
    }

    @Override
    public DoubleSolution getRandomEvaluatedSolution() throws StopCriterionException {
        return mb.getRandomSolution(this);
    }

    @Override
    public DoubleSolution eval(double[] x) throws StopCriterionException {
        return mb.eval(this, x);
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
