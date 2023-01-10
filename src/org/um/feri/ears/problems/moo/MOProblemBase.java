package org.um.feri.ears.problems.moo;

import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.ProblemBase;
import org.um.feri.ears.quality_indicator.QualityIndicator;

import java.util.ArrayList;
import java.util.List;

public abstract class MOProblemBase<Type extends Number> extends ProblemBase<Type> {

    public MOProblemBase(int numberOfDimensions, int numberOfConstraints) {
        super(numberOfDimensions, numberOfConstraints);
    }

    protected String fileName;

    public String getVersion() {
        return version;
    }

    protected List<Objective> functions = new ArrayList<Objective>();

    public MOProblemBase(int numberOfDimensions, int numberOfConstraints, int numberOfObjectives) {
        super(numberOfDimensions, numberOfConstraints);
        this.numberOfObjectives = numberOfObjectives;
    }

    public void addObjective(Objective o) {
        functions.add(o);
    }

    public abstract void evaluate(NumberSolution<Type> solution);

    public Type getLowerLimit(int i) {
        return lowerLimit.get(i);
    }

    public Type getUpperLimit(int i) {
        return upperLimit.get(i);
    }

    public abstract boolean areDimensionsInFeasibleInterval(ParetoSolution<Type> ps);

    /**
     * Evaluates a solution
     *
     * @return a double [] with the evaluation results
     */
    abstract public double[] evaluate(Type[] ds);

    abstract public NumberSolution<Type> getRandomSolution();

    public abstract void evaluateConstraints(NumberSolution<Type> solution);


    public int getNumberOfObjectives() {
        return numberOfObjectives;
    }

    public void setNumberOfObjectives(int numberOfObjectives) {
        this.numberOfObjectives = numberOfObjectives;
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isFirstBetter(ParetoSolution<Type> x, ParetoSolution<Type> y, QualityIndicator<Type> qi) {
        return x.isFirstBetter(y, qi);
    }

    @Override
    public String toString() {

        return "Problem: " + name + " version: " + version + " dimensions: " + numberOfDimensions + " objectives: " + numberOfObjectives + " constraints: " + numberOfConstraints;
    }
}
