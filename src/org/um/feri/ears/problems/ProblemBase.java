package org.um.feri.ears.problems;

import java.util.List;

public abstract class ProblemBase<Type> {

    public List<Type> upperLimit;
    public List<Type> lowerLimit;
    protected int numberOfDimensions;
    protected int numberOfObjectives = 1; //TODO set in constructor
    protected boolean minimize = true;
    public int numberOfConstraints;
    protected Type[] max_constraints;
    protected Type[] min_constraints;
    protected Type[] count_constraints;
    protected Type[] sum_constraints;
    protected Type[] normalization_constraints_factor; // used for normalization
    protected String name;
    protected String shortName;
    protected String benchmarkName;
    protected String description;

    protected String version = "1.0";
    public static final int CONSTRAINED_TYPE_COUNT = 1;
    public static final int CONSTRAINED_TYPE_SUM = 2;
    public static final int CONSTRAINED_TYPE_NORMALIZATION = 3;
    public static int constrained_type = CONSTRAINED_TYPE_SUM;

    public ProblemBase(int numberOfDimensions, int numberOfConstraints) {
        this.numberOfDimensions = numberOfDimensions;
        this.numberOfConstraints = numberOfConstraints;
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
     * @return filename safe string which contains the problem name, dimension, and version
     */
    public String getFileNameString() {
        String fileName = name.trim().replaceAll("[\\s_]", "-"); // replace all spaces and underscores with hyphen/dash
        fileName = fileName.replaceAll("[\\\\/:*?\"<>'%&@,.|{}+]", ""); // remove invalid characters
        fileName += "-"+numberOfDimensions+"D-"+version;
        return fileName;
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

    public int getNumberOfDimensions() {
        return numberOfDimensions;
    }

    public String getProblemInfoCSV() {
        return "problem name:" + name + ",problem number of dimensions:" + numberOfDimensions + ",problem number of constraints:" + numberOfConstraints + ",problem version:" + version + ",";
    }

    /**
     * Important! Do not use this function for constrained problems,
     * if fitness is not reflecting feasibility of the solution.
     *
     * @param first first fitness
     * @param second second fitness
     * @return true if the first fitness value is better than the second one
     */
    public boolean isFirstBetter(double first, double second) {
        if (minimize)
            return first < second;
        return first > second;
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
