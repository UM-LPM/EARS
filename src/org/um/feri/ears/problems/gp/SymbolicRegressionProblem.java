package org.um.feri.ears.problems.gp;

import java.util.List;
import java.util.Map;

public class SymbolicRegressionProblem extends ProgramProblem<Double>{

    private List<Target> evalData;

    public SymbolicRegressionProblem() {
        super("SymbolicRegression");
    }

    public List<?> getEvalData() {
        return evalData;
    }

    public void setEvalData(List<Target> evalData) {
        this.evalData = evalData;
    }

    @Override
    public void evaluate(ProgramSolution<Double> solution) {
        double eval = 0;
        //Za vse vrednosti iz evalData pridobim vrednost
        for(Target t: this.evalData) {
            //System.out.println(t.getContextState().get(this.baseTerminals.get(0)));
            double val = evaluateSubTree(solution.program, t.getContextState());
            eval += Math.pow(t.getTargetValue() - val, 2);
            //System.out.println("TargetValue: " + t.getTargetValue() + ", " + "ActualValue: " + val);
        }

        eval = Math.sqrt((eval / this.evalData.size()));

        solution.setEval(eval);
        System.out.println("Current Fitness: " + eval);
    }

    private double evaluateSubTree(TreeNode<Double> node, Map<String, Double> contextState) {

        if(!node.getOperation().isTerminal()){
            if(node.getOperation().isVariable()){
                Double val = contextState.get(node.getOperation().name());
                if(node.getOperation().isVariable() && val != null)
                    return val;
                else
                    throw new IllegalArgumentException("Operation not acceptable");
            }
            else {
                Double[] evaluatedSubTrees = new Double[node.childCount()];
                int i = 0;
                for (TreeNode<Double> next : node.getChildren()) {
                    evaluatedSubTrees[i] = (evaluateSubTree(next, contextState));
                    i++;
                }
                return node.getOperation().apply(evaluatedSubTrees);
            }
        }
        else  {
            return node.getOperation().apply(new Double[]{node.getCoefficient()});
        }
    }

    @Override
    public boolean isFirstBetter(ProgramSolution<Double> solution1, ProgramSolution<Double> solution2) {
        return Double.compare(solution1.eval, solution2.eval) < 0; //TODO replace with comparator
    }

    @Override
    public boolean isFirstBetter(double first, double second) {
        if (minimize)
            return first < second;
        return first > second;
    }
}
