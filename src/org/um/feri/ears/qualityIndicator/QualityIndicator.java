package org.um.feri.ears.qualityIndicator;

import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.ParetoSolution;

public abstract class QualityIndicator<Type extends Number> {

    /**
     * Returns the value which represents the quality of the Pareto front approximation
     * @param paretoFrontApproximation the Pareto front approximation to be evaluated
     */
    public abstract double evaluate(ParetoSolution<Type> paretoFrontApproximation);

    protected String name;

    /**
     * The epsilon value of the indicator.
     */
    protected double eps;

    protected String problem_file;

    protected ParetoSolution<Type> referencePopulation;

    /**
     * Stores the number of objectives.
     */
    protected int numberOfObjectives;

    /**
     * Stores the Reference set.
     */
    protected double[][] referenceSet;

    /**
     * Stores the maximum values of the reference set.
     */
    protected double[] maximumValue;
    /**
     * Stores the minimum values of the reference set.
     */
    protected double[] minimumValue;

    /**
     * Stores the reference point for the problem.
     */
    double[] referencePoint;

    /**
     * Stores the normalized Reference set.
     */
    double[][] normalizedReference;

    public QualityIndicator(int numObj, String fileName, ParetoSolution<Type> referenceSet) {

        this.numberOfObjectives = numObj;
        this.problem_file = fileName;

		/*minimumValue = new double[numberOfObjectives];
		maximumValue = new double[numberOfObjectives];*/
        referencePoint = getReferencePoint(problem_file);
        referencePopulation = referenceSet;
        this.referenceSet = referenceSet.writeObjectivesToMatrix();
        normalizedReference = normalize(referenceSet);
    }

    /**
     * Quality Indicator constructor for indicators without reference sets
     */
    public QualityIndicator(int numObj) {
        this.numberOfObjectives = numObj;
    }

    public double getEpsilon() {
        return eps;
    }

    private double[][] normalize(ParetoSolution<Type> population) {
		
		/*if (population.solutions.size() < 2) {
			throw new IllegalArgumentException("requires at least two solutions");
		}*/

        //no reference set given return unchanged front
        if (referenceSet == null) {
            return population.writeObjectivesToMatrix();
        }

        minimumValue = new double[numberOfObjectives];
        maximumValue = new double[numberOfObjectives];

        for (int i = 0; i < numberOfObjectives; i++) {
            minimumValue[i] = Double.POSITIVE_INFINITY;
            maximumValue[i] = Double.NEGATIVE_INFINITY;
        }

        for (int i = 0; i < referenceSet.length; i++) {
            MOSolutionBase<Type> solution = population.get(i);

            if (solution.violatesConstraints()) {
                continue;
            }

            for (int j = 0; j < numberOfObjectives; j++) {
                minimumValue[j] = Math.min(minimumValue[j], solution.getObjective(j));
                maximumValue[j] = Math.max(maximumValue[j], solution.getObjective(j));
            }
        }

        if (referencePoint != null) {
            for (int i = 0; i < numberOfObjectives; i++) {
                if (referencePoint[i] > maximumValue[i])
                    maximumValue[i] = referencePoint[i];
            }
        }

        checkRanges();

        return QualityIndicatorUtil.getNormalizedFront(population.writeObjectivesToMatrix(), maximumValue, minimumValue);
    }

    /**
     * Checks if any objective has a range that is smaller than machine
     * precision.
     *
     * @throws IllegalArgumentException if any objective has a range that is
     *                                  smaller than machine precision
     */
    private void checkRanges() {
        for (int i = 0; i < numberOfObjectives; i++) {
            if (Math.abs(minimumValue[i] - maximumValue[i]) < 1e-10) {
                throw new IllegalArgumentException(
                        "objective with empty range");
            }
        }
    }

    protected static <T extends Number> ParetoSolution<T> getReferenceSet(String fileName) {

        ParetoSolution<T> referenceSet = new ParetoSolution<T>(0);

        if (fileName != null && !fileName.isEmpty()) {
            referenceSet = QualityIndicatorUtil.<T>readNonDominatedSolutionSet("/org/um/feri/ears/problems/moo/pf_data/" + fileName + ".dat");
        } else {
            System.out.println("The file name containg the Paret front is not valid.");
        }

        return referenceSet;
    }

    protected static double[] getReferencePoint(String problemName) {
        double[] referencePoint;

        referencePoint = QualityIndicatorUtil.readReferencePoint("/org/um/feri/ears/problems/moo/pf_data/ReferencePoint.dat", problemName);

        return referencePoint;
    }

    public enum IndicatorType {
        UNARY,
        BINARY,
        ARBITRARY
    }

    public enum IndicatorName {
        COVERAGE_OF_TWO_SETS("CS", " Coverage of two sets"),
        EPSILON("eps", "Epsilon"),
        EPSILON_BIN("eps bin", "Binary Epsilon"),
        ERROR_RATIO("ER", "Error Ratio"),
        GD("GD", "Generational Distance"),
        //Hypervolume("HV","Hypervolume"),
        IGD("IGD", "Inverted Generational Distance"),
        IGD_PLUS("IGD+", "Inverted Generational Distance Plus"),
        MPFE("MPFE", "Maximum Pareto Front Error"),
        MAXIMUM_SPREAD("MS", "Maximum Spread"),
        NR("NR", "Nondominated Ratio"),
        ONVG("ONVG", "Overall Nondominated Vector Generation"),
        ONVGR("ONVG", "Overall Nondominated Vector Generation Ratio"),
        R1("R1", "R1"),
        R2("R2", "R2"),
        R3("R3", "R3"),
        RNI("RNI", "Ratio of Nondominated Individuals"),
        SPACING("S", "Spacing"),
        SPREAD("Spread", "Spread"),
        GENERALIZED_SPREAD("GS", "Generalized Spread"),
        NATIVE_HV("HV", "Hypervolume");

        private String shortName, longName;

        IndicatorName(String sn, String ln) {
            shortName = sn;
            longName = ln;

        }

        public String getLongName() {
            return longName;
        }

        public String getShortName() {
            return shortName;
        }

        public String toString() {
            return shortName;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * The method returns an enum which tells the number of approximations the quality indicator requires to operate.
     * Unary - one approximation
     * Binary - two approximation
     * Arbitrary - arbitrary number of approximation
     *
     * @return the enum for the indicator type.
     */
    abstract public IndicatorType getIndicatorType();

    /**
     * The method must return true if smaller values are better else return false.
     *
     * @return true if smaller values are better else return false.
     */
    abstract public boolean isMin();

    /**
     * The method returns true if reference set is required else returns false.
     *
     * @return true if reference set is required else returns false.
     */
    abstract public boolean requiresReferenceSet();

    /**
     * Compares two approximations.
     *
     * @param front1  Object representing the first front.
     * @param front2  Object representing the second front.
     * @param epsilon the draw limit
     * @return -1, or 0, or 1 if front1 is better than front2, both are
     * equal, or front2 is better than front1, respectively.
     */
    public abstract int compare(ParetoSolution<Type> front1, ParetoSolution<Type> front2, Double epsilon);

}
