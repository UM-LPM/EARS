package org.um.feri.ears.individual.generations.gp;

import org.um.feri.ears.individual.representations.gp.Node;
import org.um.feri.ears.problems.gp.ProgramProblem;
import org.um.feri.ears.problems.gp.ProgramSolution;

import java.io.Serializable;

public abstract class GPProgramSolution implements Serializable {

    public abstract ProgramSolution generate(ProgramProblem programProblem, int startDepth, String treeName);

    public abstract Node generateRandomTerminalNode(ProgramProblem programProblem);
}
