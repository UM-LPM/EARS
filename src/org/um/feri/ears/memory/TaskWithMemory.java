package org.um.feri.ears.memory;

import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.EnumStopCriteria;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;

public class TaskWithMemory extends Task {
	protected MemoryBankDoubleSolution mb;
	int xPrecision;
	StringBuilder sb;

	public TaskWithMemory(EnumStopCriteria stop, int eval, long allowedTime, int maxIterations, double epsilon,
						  Problem p, int xPrecision,  DuplicationRemovalStrategy strategy) {
		super(stop, eval, allowedTime, maxIterations, epsilon, p);
		this.xPrecision = xPrecision;
		strategy.setTask(this);
		mb = new MemoryBankDoubleSolution(xPrecision, strategy);
		sb = new StringBuilder();
	}

	public DoubleSolution evalOrg(double[] x) throws StopCriteriaException {
		return super.eval(x);
	}

	@Override
	public DoubleSolution getRandomSolution() throws StopCriteriaException {
		return mb.getRandomSolution(this);
	}
	@Override
	public DoubleSolution eval(double[] x) throws StopCriteriaException {
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
