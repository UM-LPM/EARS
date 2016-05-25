package org.um.feri.ears.algorithms;
import org.um.feri.ears.problems.DoubleMOTask;
import org.um.feri.ears.problems.MOTask;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.TaskBase;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.qualityIndicator.QualityIndicator;

public abstract class MOAlgorithm<T extends MOTask, Type> extends AlgorithmBase<T,ParetoSolution<Type>> {
	
	protected T task;
	protected static boolean optimalParam;
	
	public static boolean isRunWithOptimalParameters() {
		return optimalParam;
	}

	public static void setRunWithOptimalParameters(boolean runWithOptimalParameters) {
		MOAlgorithm.optimalParam = runWithOptimalParameters;
	}

	@Override
	public abstract ParetoSolution<Type> run(T taskProblem) throws StopCriteriaException;

}
