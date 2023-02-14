package org.um.feri.ears.problems;

import org.um.feri.ears.problems.moo.Objective;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.quality_indicator.QualityIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class NumberProblem<Type extends Number> extends Problem<NumberSolution<Type>> {

    public List<Type> upperLimit;
    public List<Type> lowerLimit;

    protected int numberOfDimensions;

    protected double[][] decisionSpaceOptima;

    public NumberProblem(int numberOfDimensions) {
        this(numberOfDimensions,1,1,0);
    }

    public NumberProblem(int numberOfDimensions, int numberOfGlobalOptima, int numberOfObjectives, int numberOfConstraints) {
        super(numberOfGlobalOptima, numberOfObjectives, numberOfConstraints);

        this.numberOfDimensions = numberOfDimensions;

                decisionSpaceOptima = new double[numberOfGlobalOptima][numberOfDimensions];
        Arrays.fill(decisionSpaceOptima[0], 0); // default global optimum is at [0, 0, ...., 0, 0]
    }

    /**
     * Returns a 2 dimensional vector containing all the global optima in the decision (design) space.
     *
     * @return 2 dimensional vector containing the global optima in the decision (design) space.
     */
    public final double[][] getDecisionSpaceOptima() {
        return decisionSpaceOptima;
    }

    public Type getLowerLimit(int i) {
        return lowerLimit.get(i);
    }

    public Type getUpperLimit(int i) {
        return upperLimit.get(i);
    }

    public List<Type> getUpperLimit() {
        return upperLimit;
    }

    public List<Type> getLowerLimit() {
        return lowerLimit;
    }

    public int getNumberOfDimensions() {
        return numberOfDimensions;
    }

    @Override
    public String getProblemInfoCSV() {
        return "problem name:" + name + ",problem number of dimensions:" + numberOfDimensions + ",problem number of constraints:" + numberOfConstraints + ",problem version:" + version + ",";
    }

    @Override
    /*
     * Returns a filename safe string which contains the problem name, dimension, and version
     * @return filename safe string which contains the problem name, dimension, and version
     */
    public String getFileNameString() {
        String fileName = name.trim().replaceAll("[\\s_]", "-"); // replace all spaces and underscores with hyphen/dash
        fileName = fileName.replaceAll("[\\\\/:*?\"<>'%&@,.|{}+]", ""); // remove invalid characters
        fileName += "-"+numberOfDimensions+"D-"+version;
        return fileName;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    // MOProblem

    protected List<Objective> objectives = new ArrayList<>();

    public void addObjective(Objective o) {
        objectives.add(o);
    }

    public boolean isFirstBetter(ParetoSolution<Type> x, ParetoSolution<Type> y, QualityIndicator<Type> qi) {
        return x.isFirstBetter(y, qi);
    }
}
