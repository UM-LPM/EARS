package org.um.feri.ears.problems.gp;

import org.um.feri.ears.individual.generations.gp.GPRandomProgramSolution;
import org.um.feri.ears.individual.representations.gp.Node;
import org.um.feri.ears.individual.representations.gp.Tree;
import org.um.feri.ears.individual.generations.gp.GPProgramSolution;
import org.um.feri.ears.individual.representations.gp.Target;
import org.um.feri.ears.operators.gp.FeasibilityGPOperator;
import org.um.feri.ears.operators.gp.GPDepthBasedTreePruningOperator;
import org.um.feri.ears.operators.gp.GPOperator;
import org.um.feri.ears.util.GPProblemEvaluatorType;
import org.um.feri.ears.operators.gp.GPTreeExpansionOperator;
import org.um.feri.ears.util.RequestBodyParams;

import java.io.Serializable;
import java.util.List;

public class SymbolicRegressionProblem extends ProgramProblem implements Serializable {

    private List<Target> evalData;

    public SymbolicRegressionProblem() {
        super("SymbolicRegression", Tree.TreeType.SYMBOLIC);
    }

    public SymbolicRegressionProblem(String problemName, List<Class<? extends Node>> baseFunctionNodeTypes, List<Class<? extends Node>> baseTerminalNodeTypes, int minTreeDepth, int maxTreeStartDepth, int maxTreeEndDepth, int maxTreeSize, FeasibilityGPOperator[] feasibilityControlOperators, GPOperator[] bloatControlOperators, GPProgramSolution programSolutionGenerator, List<Target> evalData) {
        super(problemName, baseFunctionNodeTypes, baseTerminalNodeTypes, minTreeDepth, maxTreeStartDepth, maxTreeEndDepth, maxTreeSize, feasibilityControlOperators, bloatControlOperators, GPProblemEvaluatorType.Simple, programSolutionGenerator, Tree.TreeType.SYMBOLIC, "SymbolicRegressionTree", new RequestBodyParams());

        setEvaluationSet(evaluationSet);
    }

    public SymbolicRegressionProblem(List<Class<? extends Node>> baseFunctionNodeTypes, List<Class<? extends Node>> baseTerminalNodeTypes, int minTreeDepth, int maxTreeStartDepth, int maxTreeEndDepth, int maxTreeSize, FeasibilityGPOperator[] feasibilityControlOperators, GPOperator[] bloatControlOperators, GPProgramSolution programSolutionGenerator, List<Target> evalData) {
        super("SymbolicRegression", baseFunctionNodeTypes, baseTerminalNodeTypes, minTreeDepth, maxTreeStartDepth, maxTreeEndDepth, maxTreeSize, feasibilityControlOperators, bloatControlOperators, GPProblemEvaluatorType.Simple, programSolutionGenerator, Tree.TreeType.SYMBOLIC, "SymbolicRegressionTree", new RequestBodyParams());

        setEvaluationSet(evaluationSet);
    }

    public List<Target> getEvalData() {
        return evalData;
    }

    public void setEvaluationSet(List<Target> evaluationSet) {
        this.evalData = evaluationSet;
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
