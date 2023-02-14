package org.um.feri.ears.algorithms.so.gsa;

import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;

import java.util.Arrays;

/**
 * Created by Nik Orter on 22. 10. 2016.
 */
public class Agent extends NumberSolution<Double> {
    private double mass;
    private double[] velocities;
    private double[] forces;

    public Agent(Task<NumberSolution<Double>, DoubleProblem> t, int id) throws StopCriterionException {
        super(t.getRandomEvaluatedSolution());
        velocities = new double[t.problem.getNumberOfDimensions()];
        forces = new double[t.problem.getNumberOfDimensions()];
    }

    public Agent(NumberSolution<Double> ds) {
        super(ds);
        velocities = new double[ds.numberOfVariables()];
        //Arrayscopy
        forces = new double[ds.numberOfVariables()];
        //mass
    }

    public Agent(Agent a) {
        super(a);
        velocities = Arrays.copyOf(a.getVelocities(), a.getVelocities().length);
        forces = Arrays.copyOf(a.getForces(), a.getForces().length);
        mass = a.getMass();
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getMass() {
        return mass;
    }

    public double getVelocityAtIndex(int index) {
        return velocities[index];
    }

    public double[] getVelocities() {
        return velocities;
    }

    public void setVelocityAtIndex(int index, double value) {
        velocities[index] = value;
    }

    public void setForces(double[] forces) {
        this.forces = forces;
    }

    public double[] getForces() {
        return forces;
    }

    public double getAccelerationOfSolution(int index, double G) {

        //return forces[index] /mass;
        return forces[index] * G;
    }
}
