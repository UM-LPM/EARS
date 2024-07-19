package org.um.feri.ears.problems.gp;

import org.um.feri.ears.individual.representations.gp.Node;
import org.um.feri.ears.individual.representations.gp.Tree;
import org.um.feri.ears.individual.generations.gp.GPProgramSolution;
import org.um.feri.ears.individual.representations.gp.Target;
import org.um.feri.ears.operators.gp.FeasibilityGPOperator;
import org.um.feri.ears.operators.gp.GPOperator;

import java.io.Serializable;
import java.util.List;

public class SymbolicRegressionProblem extends ProgramProblem implements Serializable {

    private List<Target> evalData;

    public SymbolicRegressionProblem() {
        super("SymbolicRegression");
    }

    public SymbolicRegressionProblem(List<Class<? extends Node>> baseFunctionNodeTypes, List<Class<? extends Node>> baseTerminalNodeTypes, int minTreeDepth, int maxTreeStartDepth, int maxTreeEndDepth, int maxTreeSize, FeasibilityGPOperator[] feasibilityControlOperators, GPOperator[] bloatControlOperators, GPProgramSolution programSolutionGenerator, List<Target> evalData) {
        super("SymbolicRegression", baseFunctionNodeTypes, baseTerminalNodeTypes, minTreeDepth, maxTreeStartDepth, maxTreeEndDepth, maxTreeSize, feasibilityControlOperators, bloatControlOperators, programSolutionGenerator, Tree.TreeType.SYMBOLIC, "SymbolicRegressionTree", new String[]{});

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
