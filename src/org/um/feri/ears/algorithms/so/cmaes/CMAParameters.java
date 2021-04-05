package org.um.feri.ears.algorithms.so.cmaes;

import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.Task;

/*
    Copyright 2003, 2005, 2007 Nikolaus Hansen 
    e-mail: hansen .AT. bionik.tu-berlin.de
            hansen .AT. lri.fr

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License, version 3,
    as published by the Free Software Foundation.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

  Last change: $Date: 2010-12-02 23:57:21 +0100 (Thu, 02 Dec 2010) $
*/

/**
 * Interface to strategy parameters for the CMA Evolution
 * Strategy, most importantly the population size lambda, while the change
 * of other parameters is discouraged. 
 * The class CMAParameters processes the
 * strategy parameters, like population size and learning rates, for
 * the class CMAEvolutionStrategy where the public field <code>parameters</code> of
 * type <code>CMAParameters</code> can
 * be used to set the parameter values. The method supplementRemainders
 * supplements those parameters that were not explicitly given, 
 * regarding dependencies
 * (eg, the parent number, mu, cannot be larger than the
 * population size lambda) and does a respective consistency checking via method 
 * {@link #check()}. 
 * Parameters cannot be changed after CMAEvolutionStrategy method init()
 * was called. 
 * <P> Example code snippet:</P>
 * <PRE>
        CMAEvolutionStrategy cma = new CMAEvolutionStrategy();
        cma.parameters.setPopulationSize(33); // set lambda
        int mu = cma.parameters.getMu(); // will fail as mu was not set and missing 
                                         // parameters were not supplemented yet 
        cma.readProperties();         // read necessary initial values, might overwrite lambda
        mu = cma.parameters.getMu();  // might still fail		
        cma.init();                   // finalize initialization, supplement missing parameters
        mu = cma.parameters.getMu();  // OK now
        cma.parameters.setMu(4);      // runtime error, parameters cannot be changed after init()
 * </PRE>
 * 
 *  <P>Most commonly, the offspring population size lambda can be changed 
 *  (increased) from its default value via setPopulationSize to improve the 
 *  global search capability, see file CMAExample2.java. It is recommended to use the default 
 *  values first! </P>
 */
public class CMAParameters implements java.io.Serializable { 
	/**
	 * 
	 */
	private static final long serialVersionUID = -1305062342816588003L;
	int supplemented; // after supplementation it is undecidable whether a parameter was 
	                  // explicitly set from outside, therefore another supplementation is not advisable
	int locked; // lock when lambda is used to new data structures
	int lambda;          /* -> mu, <- N */ //population size
	private int mu;              /* -> weights, (lambda) */
	private double mucov;        /* -> ccov */
	private double mueff;        /* <- weights */
	private double[] weights;    /* <- mu, -> mueff, mucov, ccov */
	private double damps;        /* <- cs, maxeval, lambda */
	private double cs;           /* -> damp, <- N */
	private double cc;           /* <- N */
	private double ccov;         /* <- mucov, <- N, <- diagonalcov */
	private double ccovsep;      /* <- ccov */

	double chiN;

	public CMAParameters() { 
		mucov = -1;
		ccov = -1; 
	}

	/**
	 *  Checks strategy parameter setting with respect to principle 
	 *  consistency. Returns a string with description of the first
	 *  error found, otherwise an empty string "".  
	 *  */
	public String check() {
		if (lambda <= 1)
			return "offspring population size lambda must be greater than onem is " + lambda;
		if (mu < 1)
			return "parent number mu must be greater or equal to one, is " + mu;
		if (mu > lambda)
			return "parent number mu " + mu + " must be smaller or equal to offspring population size lambda " + lambda;
		if (weights.length != mu)
			return "number of recombination weights " + weights.length + " disagrees with parent number mu " + mu; 

		if (cs <= 0 || cs > 1)
			return "0 < cs <= 1 must hold for step-size cumulation parameter cs, is " + cs;
		if (damps <= 0)
			return "step-size damping parameter damps must be greater than zero, is " + damps; 
		if (cc <= 0 || cc > 1)
			return "0 < cc <= 1 must hold for cumulation parameter cc, is " + cc;
		if (mucov < 0)
			return "mucov >= 0 must hold, is " + mucov; 
		if (ccov < 0)
			return "learning parameter ccov >= 0 must hold, is " + ccov;
		return "";
	}

	/**
	 * Supplements all default parameter values that were not explicitly set already. 
	 * Also checks whether the values that were already explicitly set are fine. 
	 * @param N search space dimension
	 * @param opts {@link CMAOptions} where stopMaxFunEvals and 
	 * stopMaxIter are used to set step-size damping parameter damps. This is of minor relevance.
	 */
	public void supplementRemainders(int N, CMAOptions opts, Task task) {
		// parameters that can be zero were initialized to -1
		/*if (supplemented > 0)
			System.out.println("defaults cannot be supplemented twice");*/
		if (N == 0)
			System.out.println("dimension must be greater than zero");

		supplemented = 1;
		locked = 1;

		chiN = Math.sqrt(N)
		* (1.0 - 1.0 / (4.0 * N) + 1.0 / (21.0 * N * N));

		// set parameters to their default if they were not set before
		if (lambda <= 0)
			lambda = (int) (4.0 + 3.0 * Math.log(N));
		if (mu <= 0)
			mu = (int) Math.floor(lambda/2.);

		if (weights == null)
			setWeights(mu, recombinationType);
		else if (weights.length == 0)
			setWeights(mu, recombinationType);

		if (cs <= 0)
			cs = (mueff+2) / (N+mueff+3);

		if (damps <= 0)
		{
			long opt = Long.MAX_VALUE;
			if(task.getStopCriterion() == StopCriterion.EVALUATIONS)
				opt = task.getMaxEvaluations() / lambda;
			if(task.getStopCriterion() == StopCriterion.ITERATIONS)
				opt = task.getMaxIterations();
			
			damps = (1 + 2 * Math.max(0, Math.sqrt((mueff - 1.) / (N + 1.)) - 1)) * Math.max(0.3, 1 - N / (1e-6 + opt)) + cs;
		}

		if (cc <= 0)
			cc = 4.0 / (N + 4.0);

		if (mucov < 0)
			mucov = mueff;

		if (ccov < 0) { // TODO: setting should depend on gendiagonalcov 
			ccov = 2.0 / (N + 1.41) / (N + 1.41) / mucov
			+ (1 - (1.0 / mucov))
			* Math.min(1, (2 * mueff - 1) / (mueff + (N + 2) * (N + 2)));
			ccovsep = Math.min(1, ccov * (N + 1.5) / 3.0);
		}

		// check everything
		String s = check();
		if (s != null && !s.equals(""))
			System.out.println(s); // if any prior setting does not work

	} // supplementRemainders

	/**
	 * Getter for property mu.
	 * 
	 * @return Value of property mu.
	 * 
	 */
	public int getMu() {
		return mu;
	}

	/**
	 * Setter for parent number mu, be aware of the recombinationType when setting mu 
	 * 
	 * @param mu
	 *            New value for the number of parents mu.
	 * @see #setRecombination(int, CMAParameters.RecombinationType)
	 * @see #setRecombinationWeights(CMAParameters.RecombinationType)
	 */
	public void setMu(int mu) {
		/*if (locked != 0) // needed because of recombination weights
			System.out.println("parameters are locked");*/
		this.mu = mu;
	}

	/**
	 * Getter for offspring population size lambda, no check, whether lambda was already set properly
	 * 
	 * @return Value of lambda
	 * 
	 */
	public int getLambda() {
		return lambda;
	}

	int flgLambdaChanged = 0; // not in use yet
	/**
	 * Setter for offspring population size alias sample size
	 * alias lambda, use setPopulationSize() for outside use.
	 */
	void setLambda(int lambda) {
		/*if (locked != 0)
			System.out.println("parameters cannot be set anymore");*/
		this.lambda = lambda; 
	}
	/** @see #getLambda() */
	public int getPopulationSize() {
		return getLambda();
	}

	/**
	 * Setter for offspring population size (lambda). If (only) lambda is 
	 * set, other parameters, eg. mu and recombination weights and
	 * subsequently learning rates for the covariance matrix etc. are
	 * chosen accordingly  
	 * 
	 * @param lambda is the offspring population size
	 */
	public void setPopulationSize(int lambda) {
		setLambda(lambda);
	}
	
	public enum RecombinationType {SUPERLINEAR, LINEAR, EQUAL};
	private RecombinationType recombinationType = RecombinationType.SUPERLINEAR; // otherwise null
	/**
	 * Getter for property weights.
	 * 
	 * @return Value of property weights.
	 * 
	 */
	public double[] getWeights() {
		return this.weights;
	}

	/**
	 * Recombination weights can be equal, linearly 
	 * decreasing, or super-linearly decreasing (default). The respective parameter value is 
	 * in enum RecombinationType. 
	 * @param recombinationType
	 * @see #setRecombination 
	 * @see #setMu
	 */
	public void setRecombinationWeights(RecombinationType recombinationType) {
		/*if (locked != 0)
			System.out.println("parameters cannot be set anymore");*/
		this.recombinationType = recombinationType;
	}

	/**
	 * Sets parent number mu and the policy for choosing the recombination weights. 
	 * Recombination weights can be equal, linearly 
	 * decreasing, or super-linearly decreasing (default). The respective parameter value is 
	 * The respective parameter value is 
	 * in enum RecombinationType. 
	 * For equal recombination weights mu=lambda/4 is appropriate, otherwise mu=lambda/2.
	 */
	public void setRecombination(int mu, RecombinationType recombinationType) {
		/*if (locked != 0)
			System.out.println("parameters are locked");*/
		this.mu = mu; 
		this.recombinationType = recombinationType;
	}

	/**
	 * Setter for recombination weights
	 *
	 * @param mu is the number of parents, number of weights > 0 
	 */
	private void setWeights(int mu, RecombinationType recombinationType) {
		double[] w = new double[mu];
		if (recombinationType == RecombinationType.EQUAL)
			for (int i = 0; i < mu; ++i) 
				w[i] = 1;
		else if (recombinationType == RecombinationType.LINEAR)
			for (int i = 0; i < mu; ++i) 
				w[i] = mu - i;
		else // default, seems as enums can be null
		for (int i = 0; i < mu; ++i) 	
			w[i] = (Math.log(mu + 1) - Math.log(i + 1));

		setWeights(w);
	}

	/** normalizes recombination weights vector and sets mueff **/
	private void setWeights(double[] weights) {
		assert locked == 0;
		double sum = 0;
		for (int i = 0; i < weights.length; ++i)
			sum += weights[i];
		for (int i = 0; i < weights.length; ++i)
			weights[i] /= sum;
		this.weights = weights;
		// setMu(weights.length);
		double sum1 = 0;
		double sum2 = 0;
		for (int i = 0; i < mu; ++i) {
			sum1 += weights[i];
			sum2 += weights[i] * weights[i];
		}
		this.mueff = sum1 * sum1 / sum2;
	}

	/**
	 * Getter for property mueff, the "variance effective selection mass".
	 * 
	 * @return Value of property mueff.
	 * 
	 */
	public double getMueff() {
		return mueff;
	}

	/**
	 * Getter for property mucov. mucov determines the
	 * mixing between rank-one and rank-mu update. For
	 * mucov = 1, no rank-mu updated takes place. 
	 * 
	 * @return Value of property mucov.
	 * 
	 */
	public double getMucov() {
		return mucov;
	}

	/**
	 * Setter for mucov.
	 * 
	 * @param mucov
	 *            New value of mucov.
	 * @see #getMucov()  
	 */
	public void setMucov(double mucov) {
		/*if (locked != 0) // on the save side as mucov -> ccov, but in principle not essential
			System.out.println("parameters cannot be set anymore");*/
		this.mucov = mucov; // can be set anytime
	}

	/**
	 * Getter for property covariance matrix learning rate ccov
	 * 
	 * @param flgdiag 
	 *        boolean, true for getting the learning rate when 
	 *        only the diagonal of the covariance matrix is updated
	 * @return Value of property ccov.
	 * 
	 */
	public double getCcov(boolean flgdiag) {
		if (flgdiag)
			return ccovsep;
		return ccov;
	}
	/**
	 * Getter for property covariance matrix learning rate ccov
	 * 
	 * @return Value of property ccov.
	 * 
	 */
	public double getCcov() {
		return ccov;
	}


	/**
	 * Setter for covariance matrix learning rate ccov. For ccov=0 no covariance
	 * matrix adaptation takes place and only <EM>Cumulation Step-Size 
	 * Adaptation (CSA)</EM> is conducted, also know as <EM>Path Length Control</EM>.
	 * 
	 * @param ccov
	 *            New value of property ccov.
	 * @see #getCcov()
	 */
	public void setCcov(double ccov) {
		this.ccov = ccov; // can be set anytime, cave: switching from diagonal to full cov
	}

	/**
	 * Getter for step-size damping damps.  The damping damps
	 * determines the amount of step size change. 
	 * 
	 * @return Value of damps.
	 * 
	 */
	public double getDamps() {
		return damps;
	}

	/**
	 * Setter for damps.
	 * 
	 * @param damps
	 *            New value of damps.
	 * @see #getDamps()
	 */
	public void setDamps(double damps) {
		/*if (locked != 0) // not really necessary!?
			System.out.println("parameters cannot be set anymore");*/
		this.damps = damps;
	}

	/**
	 * Getter for backward time horizon parameter cc for
	 * distribution cumulation (for evolution path
	 * p<sub>c</sub>).
	 * 
	 * @return Value of cc.
	 * 
	 */
	public double getCc() {
		return cc;
	}

	/**
	 * Setter for cc to default value.
	 * 
	 */
	public void setCc(double cc) {
		this.cc = cc;
	}

	/**
	 * Getter for cs, parameter for the backward time horizon for the cumulation for sigma.
	 * 
	 * @return Value of property cs.
	 * 
	 */
	public double getCs() {
		return cs;
	}

	/**
	 * Setter for cs to default value.
	 * @see #getCs()
	 */
	public void setCs(double cs) {
		/*if (locked != 0)
			System.out.println("parameters cannot be set anymore");*/
		this.cs = cs;
	}
}

