package org.um.feri.ears.problems;

import com.ctc.wstx.shaded.msv_core.datatype.xsd.Comparator;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.comparator.DominanceComparator;

public abstract class Problem<S extends Solution> {

    protected int numberOfObjectives;
    protected int numberOfGlobalOptima;
    protected boolean minimize = true;
    protected boolean[] objectiveMaximizationFlags;

    protected double[] objectiveSpaceOptima;

    public int numberOfConstraints;
    protected double[] maxConstraints;
    protected double[] minConstraints;
    protected double[] countConstraints;
    protected double[] sumConstraints;
    protected double[] normalizationConstraintsFactor; // used for normalization
    protected String name;
    protected String shortName;
    protected String referenceSetFileName;
    protected String benchmarkName;
    protected String description;
    DominanceComparator dominanceComparator = new DominanceComparator();

    protected String version = "1.0";
    public static final int CONSTRAINED_TYPE_COUNT = 1;
    public static final int CONSTRAINED_TYPE_SUM = 2;
    public static final int CONSTRAINED_TYPE_NORMALIZATION = 3;
    public static int constrained_type = CONSTRAINED_TYPE_SUM;

    public Problem(String name, int numberOfGlobalOptima, int numberOfObjectives, int numberOfConstraints) {
        this.name = name;
        this.numberOfGlobalOptima = numberOfGlobalOptima;
        this.numberOfObjectives = numberOfObjectives;
        this.numberOfConstraints = numberOfConstraints;
        objectiveMaximizationFlags = new boolean[numberOfObjectives];

        objectiveSpaceOptima = new double[numberOfGlobalOptima];
    }

    public abstract void evaluate(S solution);

    /**
     * Makes the provided solution feasible.
     *
     * @param solution to be made feasible
     */
    public abstract void makeFeasible(S solution);

    /**
     * Checks if the provided solution is feasible
     *
     * @param solution to be checked
     * @return true if the solution is feasible, false otherwise
     */
    public abstract boolean isFeasible(S solution);

    public abstract S getRandomSolution();

    public boolean isFirstBetter(S solution1, S solution2)  {
        //TODO dominance comparator with information about minimization of each objective - objectiveMaximizationFlags
        return Comparator.LESS == dominanceComparator.compare(solution1,solution2);
    }

    /**
     * Checks if the first objective value is better than the second. The function check if the objective has to be minimized or maximized.
     * It is important to note that the function considers only the objective value and doesn't check if the value is feasible.
     *
     * @param first objective value of the first solution
     * @param second objective value of the second solution
     * @param objective the index of the objective
     * @return true if the first fitness value is better than the second one
     */
    public boolean isFirstBetter(double first, double second, int objective) {
        if (objectiveMaximizationFlags[objective])
            return first > second;
        return first < second;
    }

    /**
     * Generates a random evaluated solution.
     *
     * @return a random evaluated solution.
     */
    public S getRandomEvaluatedSolution() {
        S solution = getRandomSolution();
        evaluate(solution);
        return solution;
    }

    public void evaluateConstraints(S solution) {
        if (numberOfConstraints > 0)
            System.out.println("evaluateConstraints not overriden in subclass");
        //TODO check if problem has constraints
    }

    /**
     * Returns an array containing the global optima of each objective (minimum or maximum).
     *
     * @return value of global optimum.
     */
    public double[] getGlobalOptima() {
        return objectiveSpaceOptima;
    }

    public int getNumberOfObjectives() {
        return numberOfObjectives;
    }

    /**
     * Allows to set different name. That can be used in report generating process.
     *
     * @param name of the problem
     */
    public void setName(String name) {
        this.name = name;
    }

    public boolean isMinimize() {
        return minimize;
    }

    public String getShortName() {
        if (shortName == null) return name;
        return shortName;
    }

    /**
     * Returns a filename safe string which contains the problem name, dimension, and version
     *
     * @return filename safe string which contains the problem name, dimension, and version
     */
    public String getFileNameString() {
        String fileName = name.trim().replaceAll("[\\s_]", "-"); // replace all spaces and underscores with hyphen/dash
        fileName = fileName.replaceAll("[\\\\/:*?\"<>'%&@,.|{}+]", ""); // remove invalid characters
        fileName += "D-" + version;
        return fileName;
    }

    /**
     * @return The file name of reference set
     */
    public String getReferenceSetFileName() {
        return referenceSetFileName;
    }

    public String getVersion() {
        return version;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public int getNumberOfConstraints() {
        return numberOfConstraints;
    }

    public void setNumberOfConstraints(int numberOfConstraints) {
        this.numberOfConstraints = numberOfConstraints;
    }

    public int countConstraintViolation(double[] constraints) {
        int c = 0;
        for (int i = 0; i < numberOfConstraints; i++) {
            if (constraints[i] > 0) {
                c++;
            }
        }
        return c;
    }

    public String getProblemInfoCSV() {
        return "problem name:" + name + ", problem number of constraints:" + numberOfConstraints + ",problem version:" + version + ",";
    }

    public boolean isEqualToGlobalOptimum(double objectiveValue, int objective) {
        return Math.abs(objectiveValue - getGlobalOptima()[objective]) <= 0;
    }

    public String getName() {
        return name;
    }

    public String getBenchmarkName() {
        return benchmarkName;
    }

    public void setBenchmarkName(String benchmarkName) {
        this.benchmarkName = benchmarkName;
    }
}
