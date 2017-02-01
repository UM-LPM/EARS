package org.um.feri.ears.algorithms.so.gsa;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by Nik Orter on 22. 10. 2016.
 */
public class Agent extends DoubleSolution{
    double mass;
    double []velocities;
    double []forces;

    int solutionSize;

    public Agent(Task t,int id) throws StopCriteriaException {
        super(t.getRandomSolution());
        velocities = new double[t.getNumberOfDimensions()];
        forces = new double[t.getNumberOfDimensions()];
        solutionSize = t.getNumberOfDimensions();

    }

    public Agent(DoubleSolution ds,Task t)
    {
        super(ds);
        velocities = new double[t.getNumberOfDimensions()];
        //Arrayscopy
        forces = new double[t.getNumberOfDimensions()];
        solutionSize = t.getNumberOfDimensions();
        //mass
    }

    public Agent(Agent a,Task t)
    {
        super(a);
        velocities = Arrays.copyOf(a.getVelocities(),a.getVelocities().length);
        forces = Arrays.copyOf(a.getForces(),a.getForces().length);
        solutionSize = t.getNumberOfDimensions();
        mass = a.getMass();
    }

    public int getSolutionSize() {
        return solutionSize;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getMass() {
        return mass;
    }

    public double getVelocityAtIndex(int index){
        return velocities[index];
    }

    public double[] getVelocities() {
        return velocities;
    }

    public void setVelocityAtIndex(int index,double value){
        velocities[index] = value;
    }

    public void setForces(double[] forces) {
        this.forces = forces;
    }

    public double[] getForces() {
        return forces;
    }

    public double getAccelerationOfSolution(int index,double G)
    {

        //return forces[index] /mass;
        return forces[index]*G;
    }
}
