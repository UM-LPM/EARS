package org.um.feri.ears.individual.generations.gp;

import org.um.feri.ears.problems.gp.ProgramProblem;
import org.um.feri.ears.problems.gp.ProgramProblem2;
import org.um.feri.ears.problems.gp.ProgramSolution;
import org.um.feri.ears.problems.gp.ProgramSolution2;

import java.io.Serializable;

public abstract class GPProgramSolution2 implements Serializable {

    public abstract ProgramSolution2 generate(ProgramProblem2 programProblem, int startHeight, String treeName);
}
