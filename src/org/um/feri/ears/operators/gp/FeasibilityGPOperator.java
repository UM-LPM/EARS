package org.um.feri.ears.operators.gp;

import org.um.feri.ears.problems.gp.ProgramProblem;
import org.um.feri.ears.problems.gp.ProgramSolution;

public abstract class FeasibilityGPOperator extends GPOperator {
    public abstract boolean isSolutionFeasible(ProgramSolution tProgramSolution, ProgramProblem tProgramProblem);
}
