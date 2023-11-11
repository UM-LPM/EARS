package org.um.feri.ears.problems;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Solution implements Serializable {

    int numberOfObjectives;

    protected double[] objectives;

    protected double[] constraints;
    protected boolean constraintsMet = true; //checks if constrains are met
    protected double overallConstraintViolation;
    protected int numberOfViolatedConstraints;
    protected long ID;

    //Properties for exploration and exploitation, and saving to file
    protected static long currentID = 1;
    protected long timeStamp;
    protected int generationNumber;
    protected long evaluationNumber;

    public List<Solution> parents;

    /* Multi-objective variables */
    protected double paretoFitness;
    protected double rank;
    protected int location;
    protected double crowdingDistance;
    protected Map<Object, Object> attributes = new HashMap<>();

    public Solution() {
        ID = currentID++;
    }

    public Solution(int numberOfObjectives) {
        this();
        this.numberOfObjectives = numberOfObjectives;
        objectives = new double[numberOfObjectives];
    }

    public Solution(Solution s) {

        numberOfObjectives = s.numberOfObjectives;
        objectives = new double[s.numberOfObjectives];
        System.arraycopy(s.objectives, 0, objectives, 0, s.numberOfObjectives);
        parents = new ArrayList<>();
        this.constraintsMet = s.constraintsMet;
        this.crowdingDistance = s.crowdingDistance;
        if (s.constraints != null) {
            constraints = new double[s.constraints.length];
            System.arraycopy(s.constraints, 0, constraints, 0, constraints.length);
        }
        this.attributes = new HashMap<>(s.attributes);
        overallConstraintViolation = s.getOverallConstraintViolation();
        numberOfViolatedConstraints = s.getNumberOfViolatedConstraint();
        ID = s.ID;
    }

    public abstract Solution copy();

    public double getParetoFitness() {
        return paretoFitness;
    }

    public void setParetoFitness(double paretoFitness) {
        this.paretoFitness = paretoFitness;
    }

    public double getRank() {
        return rank;
    }

    public void setRank(double rank) {
        this.rank = rank;
    }

    public double getCrowdingDistance() {
        return crowdingDistance;
    }

    public void setCrowdingDistance(double crowdingDistance) {
        this.crowdingDistance = crowdingDistance;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public void setAttribute(Object id, Object value) {
        attributes.put(id, value);
    }

    public Object getAttribute(Object id) {
        return attributes.get(id);
    }

    /*
        For single-objective optimization
     */
    public double getEval() {
        return objectives[0];
    }

    public double[] getObjectives() {
        return objectives;
    }

    public double getObjective(int index) {
        return objectives[index];
    }

    public void setObjective(int index, double objective) {
        objectives[index] = objective;
    }

    public int getNumberOfObjectives() {
        return numberOfObjectives;
    }

    public void setObjectives(double[] objectives) {
        this.objectives = objectives;
    }

    public long getID() {
        return ID;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public int getGenerationNumber() {
        return generationNumber;
    }

    public long getEvaluationNumber() {
        return evaluationNumber;
    }

    /**
     * @return true if the solution satisfies the constraints.
     */
    public boolean areConstraintsMet() {
        return constraintsMet;
    }

    protected void checkConstraints(double[] constrains) {
        constraintsMet = true;
        for (int i = 0; i < constrains.length; i++) {
            if (constrains[i] > 0) { //equality constraints need to be solved in Problem (set 0 if<=0.001)
                constraintsMet = false;
                this.constraints = new double[constrains.length];
                System.arraycopy(constrains, 0, this.constraints, 0, constrains.length);
            }
        }
    }

    /**
     * Sets all constraints of this solution.
     *
     * @param constraints the new constraints for this solution
     * @throws IllegalArgumentException if {@code constraints.length !=
     *                                  getNumberOfConstraints()}
     */
    public void setConstraints(double[] constraints) {
        if (this.constraints == null) {
            this.constraints = new double[constraints.length];
        }

        if (constraints.length != this.constraints.length) {
            throw new IllegalArgumentException("invalid number of constraints");
        }

		System.arraycopy(constraints, 0, this.constraints, 0, constraints.length);
    }

    public double[] getConstraints() {
        return constraints;
    }

    /**
     * Sets the overall constraints violated by the solution.
     *
     * @param value The overall constraints violated by the solution.
     */
    public void setOverallConstraintViolation(double value) {
        this.overallConstraintViolation = value;
    }

    /**
     * Gets the overall constraint violated by the solution.
     * <b> REQUIRE </b>: This method has to be invoked after calling
     * <code>overallConstraintViolation</code>.
     *
     * @return the overall constraint violation by the solution.
     */
    public double getOverallConstraintViolation() {
        return this.overallConstraintViolation;
    }  //getOverallConstraintViolation


    /**
     * Sets the number of constraints violated by the solution.
     *
     * @param value The number of constraints violated by the solution.
     */
    public void setNumberOfViolatedConstraint(int value) {
        this.numberOfViolatedConstraints = value;
    } //setNumberOfViolatedConstraint

    /**
     * Gets the number of constraint violated by the solution.
     * <b> REQUIRE </b>: This method has to be invoked after calling
     * <code>setNumberOfViolatedConstraint</code>.
     *
     * @return the number of constraints violated by the solution.
     */
    public int getNumberOfViolatedConstraint() {
        return this.numberOfViolatedConstraints;
    } // getNumberOfViolatedConstraint

    /**
     * Returns {@code true} if any of the constraints are violated;
     * {@code false} otherwise.
     *
     * @return {@code true} if any of the constraints are violated;
     * {@code false} otherwise
     */
    public boolean violatesConstraints() {

        if (constraints == null)
            return false;

        for (int i = 0; i < constraints.length; i++) {
            if (constraints[i] != 0.0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the number of constraints defined by this solution.
     *
     * @return the number of constraints defined by this solution
     */
    public int getNumberOfConstraints() {
        if (constraints == null)
            return 0;
        return constraints.length;
    }

    /**
     * Returns the constraint at the specified index.
     *
     * @param index index of the variable to be returned
     * @return the constraint at the specified index
     * @throws IndexOutOfBoundsException if the index is out of range
     *                                   {@code (index < 0) || (index >= getNumberOfConstraints())}
     */
    public double getConstraint(int index) {
        return constraints[index];
    }

    public static void resetLoggingID() {
        currentID = 1;
    }

}
