package org.um.feri.ears.problems.moo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.um.feri.ears.problems.SolutionBase;
import org.um.feri.ears.util.Util;

public class MOSolutionBase<Type> extends SolutionBase<Type> {
	
	protected double eval[]; //more than one objective
    protected double fitness;
	protected double rank; 
	protected int location;
	protected double crowdingDistance;
	protected double[] normalizedObjective_;

	protected Map<Object, Object> attributes;
	protected int clusterID_;
	protected double vDistance_;

	public MOSolutionBase() {
	}
	
	/**
	   * Constructor
	   * @param numberOfObjectives Number of objectives of the solution
	   * 
	   * This constructor is used mainly to read objective values from a file to
	   * variables of a SolutionSet to apply quality indicators.
	   */
	public MOSolutionBase (int numberOfObjectives) {

		eval = new double[numberOfObjectives];
		attributes = new HashMap<>() ;
		normalizedObjective_ = new double[numberOfObjectives];
	}
	
	public MOSolutionBase(MOSolutionBase<Type> s) {
		super(s);
		crowdingDistance = s.getCrowdingDistance();
		eval = new double[s.eval.length];
		upperLimit = new ArrayList<Type>(s.upperLimit);
		lowerLimit = new ArrayList<Type>(s.lowerLimit);
		
	    System.arraycopy(s.eval, 0, eval, 0, eval.length);
		this.fitness = s.fitness;
		this.rank = s.rank;
		this.location = s.location;
	    this.attributes = new HashMap<Object, Object>(s.attributes) ;
		
		normalizedObjective_ = new double[eval.length];

		for (int i = 0; i < eval.length; i++) {
			normalizedObjective_[i] = s.getNormalizedObjective(i);
		}
	}
	
	
	public MOSolutionBase(List<Type> var, double[] eval, List<Type> upperLimit, List<Type> lowerLimit) {

		this.upperLimit = upperLimit;
		this.lowerLimit = lowerLimit;
		
		
		variable = new ArrayList<Type>(var);
		
		//System.arraycopy(var, 0, variable, 0, var.length);
		
 		//System.arraycopy(x, 0, this.variable, 0, x.size());
		this.eval = new double[eval.length];
		System.arraycopy(eval, 0, this.eval, 0, eval.length);
		constraintsMet = true;
		attributes = new HashMap<>() ;
		normalizedObjective_ = new double[eval.length];
		
	}

	public MOSolutionBase<Type> copy() {
		return new MOSolutionBase<>(this);
	}
	
	/**
	 * !!!This constructor is for unconstrained optimization!
	 * 
	 * @param x
	 * @param eval
	 */
	public MOSolutionBase(Type[] var, double[] eval) {

		System.arraycopy(var, 0, variable, 0, var.length);
 		//System.arraycopy(x, 0, this.variable, 0, x.size());
		this.eval = new double[eval.length];
		System.arraycopy(eval, 0, this.eval, 0, eval.length);
		constraintsMet = true;
		attributes = new HashMap<>() ;
		normalizedObjective_ = new double[eval.length];
	}


	public int numberOfObjectives() {
		if (eval == null)
			return 0;
		else
			return eval.length;
	}


	public void setAttribute(Object id, Object value) {
		attributes.put(id, value) ;
	}


	public Object getAttribute(Object id) {
		return attributes.get(id) ;
	}

	public void setClusterID(int id){
		this.clusterID_ = id;
	}

	public int getClusterID(){
		return this.clusterID_;
	}


	public void setVDistance(double val){
		this.vDistance_ = val;
	}

	public double getVDistance(){
		return this.vDistance_;
	}

	public void setNormalizedObjective(int i, double value) {
		normalizedObjective_[i] = value;
	}

	public double getNormalizedObjective(int i) {
		return normalizedObjective_[i];
	}

	public String toString() {
		String aux = "";
		for (int i = 0; i < numberOfObjectives(); i++)
			aux = aux + this.getObjective(i) + " ";
		return aux;
	}

	public String toStringCSV() {
		String aux = "";
		for (int i = 0; i < numberOfObjectives(); i++)
			aux = aux + this.getObjective(i) + ";";
		aux = aux.replace(".", ",");
		return aux;
	}

	public void setObjective(int i, double value) {
		eval[i] = value;
	}

	public double getObjective(int i) {
		return eval[i];
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

	public double[] getObjectives() {
		return eval;
	}

	public void setObjectives(double[] eval) {
		this.eval = eval;

	}

	public String toStringFitness() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < eval.length; i++)
			sb.append(Util.df0.format(eval[i])).append("\t");
		return sb.toString();
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public void setLocation(int location) {
		this.location = location;
	}

	/**
	 * Gets the location of this solution in a <code>SolutionSet</code>. <b>
	 * REQUIRE </b>: This method has to be invoked after calling <code>setLocation</code>.
	 * @return the location of the solution into a solutionSet
	 */
	public int getLocation() {
		return this.location;
	}

	@Override
	public double getEval() {
		throw new UnsupportedOperationException();
	}
}
