package org.um.feri.ears.individual.generations.gp;

import org.um.feri.ears.problems.gp.ProgramProblem;
import org.um.feri.ears.problems.gp.ProgramSolution;

public abstract class GPProgramSolution<T> {

    public abstract ProgramSolution<T> generate(ProgramProblem<T> programProblem, boolean isRoot, int currentDepth);
}
