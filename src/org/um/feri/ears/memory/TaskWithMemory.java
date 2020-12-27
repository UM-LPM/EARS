package org.um.feri.ears.memory;

import org.um.feri.ears.problems.*;
import org.um.feri.ears.problems.StopCriterionException;

public class TaskWithMemory extends Task {
	protected MemoryBankDoubleSolution mb;
	int xPrecision;
	StringBuilder sb;

	public TaskWithMemory(EnumStopCriterion stop, int eval, long allowedTime, int maxIterations, double epsilon,
						  Problem p, int xPrecision, DuplicationRemovalStrategy strategy) {
		super(stop, eval, allowedTime, maxIterations, epsilon, p);
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
		return mb.eval(this,x);

	}
	@Override
	public String toString() {
		sb.setLength(0);
		sb.append(super.toString()).append("\n");
		sb.append(mb);
		return sb.toString();
	}
	public int getDuplicationHitSum() {
		return mb.getDuplicationHitSum();
	}

	public int getDuplicationBeforeGlobal() {
		return mb.getDuplicationBeforeGlobal();
	}

}
