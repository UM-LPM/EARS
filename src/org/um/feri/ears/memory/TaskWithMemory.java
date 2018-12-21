package org.um.feri.ears.memory;

import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.EnumStopCriteria;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;

public class TaskWithMemory extends Task {
	MemoryBankDoubleSolution mb;
	int maxHits;
	int xPrecision;
	
	public TaskWithMemory(EnumStopCriteria stop, int eval, long allowedTime, int maxIterations, double epsilon,
			Problem p, int xPrecision, int maxHits) {
		super(stop, eval, allowedTime, maxIterations, epsilon, p);
		this.xPrecision = xPrecision;
		mb = new MemoryBankDoubleSolution(xPrecision, new DuplicationRemovalStrategyRandom(this, maxHits));
	}
	
	public DoubleSolution evalOrg(double[] x) throws StopCriteriaException {
		return super.eval(x);
	}
	
	@Override
	public DoubleSolution eval(double[] x) throws StopCriteriaException {
		return mb.eval(this,x);

	}
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(super.toString()).append("\n");
		sb.append(mb);
		return sb.toString();
	}
	

}
