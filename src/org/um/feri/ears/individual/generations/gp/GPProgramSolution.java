package org.um.feri.ears.individual.generations.gp;

import org.um.feri.ears.individual.representations.gp.Node;
import org.um.feri.ears.problems.gp.ProgramProblem;
import org.um.feri.ears.problems.gp.ProgramSolution;
import org.um.feri.ears.util.Configuration;

import java.io.Serializable;

public abstract class GPProgramSolution implements Serializable {

    public static final long serialVersionUID = 5655931880676281285L;

    public abstract ProgramSolution generate(ProgramProblem programProblem, int startDepth, String treeName);

    public abstract Node generateRandomTerminalNode(ProgramProblem programProblem);

    public abstract Configuration.InitPopGeneratorMethod getInitPopGeneratorMethod();
}
