package org.um.feri.ears.problems.gp;


import org.um.feri.ears.individual.btdemo.gp.Node;
import org.um.feri.ears.individual.btdemo.gp.behaviour.Tree;
import org.um.feri.ears.individual.generations.gp.GPProgramSolution2;
import org.um.feri.ears.individual.representations.gp.Target;
import org.um.feri.ears.operators.gp.GPOperator;
import org.um.feri.ears.operators.gp.GPOperator2;

import java.util.List;

public class SoccerBTProblem2 extends ProgramProblem2 {

    public SoccerBTProblem2() {
        super("SoccerBTProblem");
    }

    public SoccerBTProblem2(List<Class<? extends Node>> baseFunctionNodeTypes, List<Class<? extends Node>> baseTerminalNodeTypes, int minTreeHeight, int maxTreeHeight, int maxTreeNodes, GPOperator2 pruningOperator, GPOperator2 expansionOperator, GPProgramSolution2 programSolutionGenerator) {
        super("SoccerBTProblem", baseFunctionNodeTypes, baseTerminalNodeTypes, minTreeHeight, maxTreeHeight, maxTreeNodes, pruningOperator, expansionOperator, programSolutionGenerator, Tree.TreeType.BEHAVIOUR, "DemoBT");

    }

    @Override
    public void evaluate(ProgramSolution2 solution) {
        double eval = solution.tree.evaluate(null);

        solution.setObjective(0, eval);
    }
}