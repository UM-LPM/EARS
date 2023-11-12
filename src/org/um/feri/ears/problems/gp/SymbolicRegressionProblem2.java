package org.um.feri.ears.problems.gp;

import org.um.feri.ears.individual.representations.gp.Node;
import org.um.feri.ears.individual.representations.gp.Tree;
import org.um.feri.ears.individual.generations.gp.GPProgramSolution2;
import org.um.feri.ears.individual.representations.gp.Target;
import org.um.feri.ears.operators.gp.GPOperator2;

import java.io.Serializable;
import java.util.List;

public class SymbolicRegressionProblem2 extends ProgramProblem2 implements Serializable {

    private List<Target> evalData;

    public SymbolicRegressionProblem2() {
        super("SymbolicRegression");
    }

    public SymbolicRegressionProblem2(List<Class<? extends Node>> baseFunctionNodeTypes, List<Class<? extends Node>> baseTerminalNodeTypes, int minTreeHeight, int maxTreeHeight, int maxTreeNodes, GPOperator2 pruningOperator, GPOperator2 expansionOperator, GPProgramSolution2 programSolutionGenerator, List<Target> evalData) {
        super("SymbolicRegression", baseFunctionNodeTypes, baseTerminalNodeTypes, minTreeHeight, maxTreeHeight, maxTreeNodes, pruningOperator, expansionOperator, programSolutionGenerator, Tree.TreeType.SYMBOLIC, "SymbolicRegressionTree");

        setEvalData(evalData);
    }

    public List<?> getEvalData() {
        return evalData;
    }

    public void setEvalData(List<Target> evalData) {
        this.evalData = evalData;
    }

    @Override
    public void evaluate(ProgramSolution2 solution) {
        double eval = 0;
        // For each value from evalData evaluate the solution
        for(Target t: evalData) {
            double val = solution.tree.evaluate( t.getContextState());
            eval += Math.pow(t.getTargetValue() - val, 2);
        }

        eval = eval / evalData.size();

        solution.setObjective(0, eval);
    }
}
