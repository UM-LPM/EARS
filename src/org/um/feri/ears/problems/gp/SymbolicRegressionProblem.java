package org.um.feri.ears.problems.gp;

import org.um.feri.ears.individual.representations.gp.Node;
import org.um.feri.ears.individual.representations.gp.Tree;
import org.um.feri.ears.individual.generations.gp.GPProgramSolution;
import org.um.feri.ears.individual.representations.gp.Target;
import org.um.feri.ears.operators.gp.GPOperator;

import java.io.Serializable;
import java.util.List;

public class SymbolicRegressionProblem extends ProgramProblem implements Serializable {

    private List<Target> evalData;

    public SymbolicRegressionProblem() {
        super("SymbolicRegression");
    }

    public SymbolicRegressionProblem(List<Class<? extends Node>> baseFunctionNodeTypes, List<Class<? extends Node>> baseTerminalNodeTypes, int minTreeDepth, int maxTreeDepth, int maxTreeSize, GPOperator pruningOperator, GPOperator expansionOperator, GPOperator treeSizePrunningOperator, GPProgramSolution programSolutionGenerator, List<Target> evalData) {
        super("SymbolicRegression", baseFunctionNodeTypes, baseTerminalNodeTypes, minTreeDepth, maxTreeDepth, maxTreeSize, pruningOperator, expansionOperator, treeSizePrunningOperator, programSolutionGenerator, Tree.TreeType.SYMBOLIC, "SymbolicRegressionTree");

        setEvalData(evalData);
    }

    public List<?> getEvalData() {
        return evalData;
    }

    public void setEvalData(List<Target> evalData) {
        this.evalData = evalData;
    }

    @Override
    public void evaluate(ProgramSolution solution) {
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
