/* 
    Copyright 1996, 2003, 2005, 2007 Nikolaus Hansen 
    e-mail: hansen .AT. lri.fr

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License, version 3,
    as published by the Free Software Foundation.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.   
 */
package org.um.feri.ears.algorithms.so.cmaes;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.comparator.ProblemComparator;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CMAES extends NumberAlgorithm {

	private int popSize;
	private int N;

	private NumberSolution<Double> best;

	private final MyMath math = new MyMath();
	private double axisratio;

	private double sigma = 0.0;
	private double[] typicalX; // eventually used to set initialX
	private double[] initialX; // set in the end of init()
	private double[] xmean;
	private double[] pc;
	private double[] ps;
	private double[][] C;
	private double maxsqrtdiagC;
	private double minsqrtdiagC;
	private double[][] B;
	private double[] diagD;
	private boolean flgdiag; // 0 == full covariance matrix

	/* init information */
	private double[] startsigma;
	private double maxstartsigma;
	private double minstartsigma;

	private boolean iniphase;

	/**
	 * state (postconditions):
	 *  -1 not yet initialized
	 *   0 initialized init()
	 *   0.5 reSizePopulation
	 *   1 samplePopulation, sampleSingle, reSampleSingle
	 *   2.5 updateSingle
	 *   3 updateDistribution
	 */
	private double state = -1;
	private int lockDimension = 0;
	private long countCupdatesSinceEigenupdate;
	private int idxRecentOffspring;

	private NumberSolution<Double>[] arx; //current population
	/** recent population, no idea whether this is useful to be public */
	private NumberSolution<Double>[] population; // offspring population
	private double[] xold;

	private double[] BDz;
	private double[] artmp;

	private Timing timings;
	/** options that can be changed (fields can be assigned) at any time to control 
	 * the running behavior
	 * */
	private CMAOptions options;
	private CMAParameters sp;

	public CMAES()
	{
		this(30);
	}

	public CMAES(int popSize)
	{
		super();
		this.popSize = popSize;

		au = new Author("miha", "miha.ravber@um.si");
		ai = new AlgorithmInfo("CMAES", "Covariance Matrix Adaptation Evolutionary Strategy",
				"@article{hansen2003reducing,"
						+ "	  title={Reducing the time complexity of the derandomized evolution strategy with covariance matrix adaptation (CMA-ES)},"
						+ "	  author={Hansen, Nikolaus and M{\"u}ller, Sibylle D and Koumoutsakos, Petros},"
						+ "	  journal={Evolutionary computation},"
						+ "	  volume={11},"
						+ "	  number={1},"
						+ "	  pages={1--18},"
						+ "	  year={2003},"
						+ "	  publisher={MIT Press}}"
				);
	}

	public void init() {
		int i;
		/*if (N <= 0)
			System.out.println("dimension needs to be determined, use eg. setDimension() or setInitialX()");
		if (state > 0)  
			System.out.println("init() cannot be called after the first population was sampled");*/

		if (sp.supplemented == 0) // a bit a hack
			sp.supplementRemainders(N, options, task);
		sp.locked = 1; // lambda cannot be changed anymore

		diagD = new double[N];
		for (i = 0; i < N; ++i)
			diagD[i] = 1;

		/* Initialization of sigmas */
		if (startsigma != null) { // 
			if (startsigma.length == 1) {
				sigma = startsigma[0];
			} else if (startsigma.length == N) {
				sigma = math.max(startsigma);
				if (sigma <= 0)
					System.out.println("initial standard deviation sigma must be positive");
				for (i = 0; i < N; ++i) {
					diagD[i] = startsigma[i]/sigma;
				}
			} else
				assert false;
		} else {
			// we might use boundaries here to find startsigma, but I prefer to have stddevs mandatory 
			System.out.println("no initial standard deviation specified, use setInitialStandardDeviations()");
			sigma = 0.5;
		}

		if (sigma <= 0 || math.min(diagD) <= 0) {
			System.out.println("initial standard deviations not specified or non-positive, " + 
					"use setInitialStandarddeviations()"); 
			sigma = 1;
		}
		/* save initial standard deviation */
		if (startsigma == null || startsigma.length == 1) { 
			startsigma = new double[N];
			for (i = 0; i < N; ++i) {
				startsigma[i] = sigma * diagD[i];
			}
		}
		maxstartsigma = math.max(startsigma);
		minstartsigma = math.min(startsigma);
		axisratio = maxstartsigma / minstartsigma; // axis parallel distribution

		/* expand typicalX, might still be null afterwards */
		typicalX = expandToDimension(typicalX, N);

		/* Initialization of xmean */
		xmean = expandToDimension(xmean, N);
		if (xmean == null) { 
			/* set via typicalX */
			if (typicalX != null) {
				xmean = typicalX.clone();
				for (i = 0; i < N; ++i)
					xmean[i] += sigma*diagD[i] * RNG.nextGaussian();
				/* set via boundaries, is depriciated */
			} else {
				System.out.println("no initial search point (solution) X or typical X specified");
				xmean = new double[N];
				for (i = 0; i < N; ++i) { /* TODO: reconsider this algorithm to set X0 */
					double offset = sigma*diagD[i];
					double range = (task.problem.getUpperLimit(i) - task.problem.getLowerLimit(i) - 2*sigma*diagD[i]);
					if (offset > 0.4 * (task.problem.getUpperLimit(i) - task.problem.getLowerLimit(i))) {
						offset = 0.4 * (task.problem.getUpperLimit(i) - task.problem.getLowerLimit(i));
						range = 0.2 * (task.problem.getUpperLimit(i) - task.problem.getLowerLimit(i));
					}
					xmean[i] = task.problem.getLowerLimit(i) + offset + RNG.nextDouble() * range;
				}
			} 
			/*else {
				System.out.println("no initial search point (solution) X or typical X specified");
				xmean = new double[N];
				for (i = 0; i < N; ++i)
					xmean[i] = RNG.nextDouble();
			}*/
		}

		assert xmean != null;
		assert sigma > 0; 

		/* interpret missing option value */
		if (options.diagonalCovarianceMatrix < 0) // necessary for hello world message
			options.diagonalCovarianceMatrix = 1 * 150 * N / sp.lambda; // cave: duplication below

		/* non-settable parameters */
		pc = new double[N];
		ps = new double[N];
		B = new double[N][N];
		C = new double[N][N]; // essentially only i <= j part is used

		xold = new double[N];
		BDz = new double[N];
		artmp = new double[N];

		arx = new NumberSolution[sp.getLambda()];
		population = new NumberSolution[sp.getLambda()];

		// initialization
		for (i = 0; i < N; ++i) {
			pc[i] = 0;
			ps[i] = 0;
			for (int j = 0; j < N; ++j) {
				B[i][j] = 0;
			}
			for (int j = 0; j < i; ++j) {
				C[i][j] = 0;
			}
			B[i][i] = 1;
			C[i][i] = diagD[i] * diagD[i];
		}
		maxsqrtdiagC = Math.sqrt(math.max(math.diag(C)));
		minsqrtdiagC = Math.sqrt(math.min(math.diag(C)));
		countCupdatesSinceEigenupdate = 0;
		iniphase = false; // obsolete

		/* Some consistency check */
		for (i = 0; i < N; ++i) {
			if (typicalX != null) {
				if (task.problem.getLowerLimit(i) > typicalX[i])
					System.out.println("lower bound '" + task.problem.getLowerLimit(i) + "'is greater than typicalX" + typicalX[i]);
				if (task.problem.getUpperLimit(i) < typicalX[i])
					System.out.println("upper bound '" + task.problem.getUpperLimit(i) + "' is smaller than typicalX " + typicalX[i]);
			}
		}
		test();

		initialX = xmean.clone(); // keep finally chosen initialX

		timings.start = System.currentTimeMillis();
		timings.starteigen = System.currentTimeMillis();

		state = 0;
	}
	@Override
	public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
		
		this.task = task;
		timings = new Timing();
		options = new CMAOptions();
		sp = new CMAParameters();

		sp.setPopulationSize(popSize);
		N = task.problem.getNumberOfDimensions();
		xmean = new double[]{0.05}; //setInitialX in each dimension, also setTypicalX can be used
		startsigma = new double[]{0.2}; // also a mandatory setting 
		setInitialX(new double[]{0.5});
		sigma = 0.0;
		state = -1;
		lockDimension = 0;
		//set options
		options.diagonalCovarianceMatrix = 0;
		options.maxTimeFractionForEigendecomposition = 0.2;
		options.checkEigenSystem = 0;

		// initialize cma and get fitness array to fill in later
		init();

		while (!task.isStopCriterion()) {

			samplePopulation(); // get a new population of solutions
			for(int i = 0; i < arx.length; ++i) {    // for each candidate solution i
				// a simple way to handle constraints that define a convex feasible domain  
				// (like box constraints, i.e. variable boundaries) via "blind re-sampling" 
				// assumes that the feasible domain is convex, the optimum is  
				while (!task.problem.isFeasible(arx[i]))     //   not located on (or very close to) the domain boundary,
					resampleSingle(i);    //   initialX is feasible and initialStandardDeviations are  
				//   sufficiently small to prevent quasi-infinite looping here
				// compute fitness/objective value
				if(!task.isStopCriterion())
					task.eval(arx[i]);
				else
					return best;

				population[i] = new NumberSolution<>(arx[i]);
			}
			
			updateDistribution();         // pass fitness array to update search distribution
			task.incrementNumberOfIterations();
		}

		return best;
	}

	private void resampleSingle(int index) {
		int i,j;
		double sum;
		if (state != 1)
			System.out.println("call samplePopulation before calling resampleSingle(int index)");

		/* sample the distribution */
		/* generate scaled random vector (D * z) */
		if (flgdiag)
			for (i = 0; i < N; ++i)
				arx[index].setValue(i, xmean[i] + sigma * diagD[i] * RNG.nextGaussian());
		else {
			for (i = 0; i < N; ++i) 
				artmp[i] = diagD[i] * RNG.nextGaussian();

			/* add mutation (sigma * B * (D*z)) */
			for (i = 0; i < N; ++i) {
				for (j = 0, sum = 0; j < N; ++j)
					sum += B[i][j] * artmp[j];
				arx[index].setValue(i, xmean[i] + sigma * sum);
			}
		}
	}

	/** update of the search distribution after samplePopulation(). functionValues 
	 * determines the selection order (ranking) for the solutions in the previously sampled 
	 * population.
	 */   
	private void updateDistribution() {

		if (state == 3) {
			System.out.println("updateDistribution() was already called");
		}

		int i, j, k, iNk, hsig;
		double sum;
		double psxps;

		/* sort function values */
		ProblemComparator<NumberSolution<Double>> s = new ProblemComparator<>(task.problem);
		Arrays.sort(arx, s);

		/* save/update bestever-value */
		if(best == null)
			best = new NumberSolution<>(arx[0]);
		
		if(task.problem.isFirstBetter(arx[0], best))
			best = new NumberSolution<>(arx[0]);


		/* re-calculate diagonal flag */
		flgdiag = (options.diagonalCovarianceMatrix == 1 || options.diagonalCovarianceMatrix >= task.getNumberOfIterations()); 
		if (options.diagonalCovarianceMatrix == -1) // options might have been re-read
			flgdiag = (task.getNumberOfIterations() <= 150 * N / sp.lambda);  // CAVE: duplication of "default"

		/* calculate xmean and BDz~N(0,C) */
		for (i = 0; i < N; ++i) {
			xold[i] = xmean[i];
			xmean[i] = 0.;
			for (iNk = 0; iNk < sp.getMu(); ++iNk)
				xmean[i] += sp.getWeights()[iNk] * arx[iNk].getValue(i);
			BDz[i] = Math.sqrt(sp.getMueff()) * (xmean[i] - xold[i]) / sigma;
		}

		/* cumulation for sigma (ps) using B*z */
		if (flgdiag) {
			/* given B=I we have B*z = z = D^-1 BDz  */
			for (i = 0; i < N; ++i) {
				ps[i] = (1. - sp.getCs()) * ps[i] + Math.sqrt(sp.getCs() * (2. - sp.getCs())) * BDz[i] / diagD[i];
			}
		} else {
			/* calculate z := D^(-1) * B^(-1) * BDz into artmp, we could have stored z instead */
			for (i = 0; i < N; ++i) {
				for (j = 0, sum = 0.; j < N; ++j)
					sum += B[j][i] * BDz[j];
				artmp[i] = sum / diagD[i];
			}
			/* cumulation for sigma (ps) using B*z */
			for (i = 0; i < N; ++i) {
				for (j = 0, sum = 0.; j < N; ++j)
					sum += B[i][j] * artmp[j];
				ps[i] = (1. - sp.getCs()) * ps[i] + Math.sqrt(sp.getCs() * (2. - sp.getCs())) * sum;
			}
		}

		/* calculate norm(ps)^2 */
		psxps = 0;
		for (i = 0; i < N; ++i)
			psxps += ps[i] * ps[i];

		/* cumulation for covariance matrix (pc) using B*D*z~N(0,C) */
		hsig = 0;
		if (Math.sqrt(psxps) / Math.sqrt(1. - Math.pow(1. - sp.getCs(), 2. * task.getNumberOfIterations())) / sp.chiN < 1.4 + 2. / (N + 1.)) {
			hsig = 1;
		}
		for (i = 0; i < N; ++i) {
			pc[i] = (1. - sp.getCc()) * pc[i] + hsig * Math.sqrt(sp.getCc() * (2. - sp.getCc())) * BDz[i];
		}

		/* stop initial phase, not in use anymore as hsig does the job */
		if (iniphase && task.getNumberOfIterations() > Math.min(1 / sp.getCs(), 1 + N / sp.getMucov()))
			if (psxps / sp.getDamps() / (1. - Math.pow((1. - sp.getCs()), task.getNumberOfIterations())) < N * 1.05)
				iniphase = false;

		/* this, it is harmful in a dynamic environment
		 * remove momentum in ps, if ps is large and fitness is getting worse */
		//        if (1 < 3 && psxps / N > 1.5 + 10 * Math.sqrt(2. / N)
		//        		&& fit.history[0] > fit.history[1] && fit.history[0] > fit.history[2]) {
		//          double tfac;
		// 
		//        	infoVerbose(countiter + ": remove momentum " + psxps / N + " "
		//        			+ ps[0] + " " + sigma);
		//
		//        	tfac = Math.sqrt((1 + Math.max(0, Math.log(psxps / N))) * N / psxps);
		//        	for (i = 0; i < N; ++i)
		//        		ps[i] *= tfac;
		//        	psxps *= tfac * tfac;
		//        }

		/* update of C */
		if (sp.getCcov() > 0 && !iniphase) {

			++countCupdatesSinceEigenupdate;

			/* update covariance matrix */
			for (i = 0; i < N; ++i) {
				for (j = (flgdiag ? i : 0); j <= i; ++j) {
					C[i][j] = (1 - sp.getCcov(flgdiag))
							* C[i][j]
							+ sp.getCcov()
							* (1. / sp.getMucov())
							* (pc[i] * pc[j] + (1 - hsig) * sp.getCc()
							* (2. - sp.getCc()) * C[i][j]);
					for (k = 0; k < sp.getMu(); ++k) { /*
					 * additional rank mu
					 * update
					 */
						C[i][j] += sp.getCcov() * (1 - 1. / sp.getMucov()) * sp.getWeights()[k]
										* (arx[k].getValue(i) - xold[i])
										* (arx[k].getValue(j) - xold[j]) // /sigma
										/ sigma;
					}
				}
			}
			maxsqrtdiagC = Math.sqrt(math.max(math.diag(C)));
			minsqrtdiagC = Math.sqrt(math.min(math.diag(C)));
		} // update of C

		/* update of sigma */
		sigma *= Math.exp(((Math.sqrt(psxps) / sp.chiN) - 1) * sp.getCs() / sp.getDamps());

		state = 3;
	}

	/**
	 * Samples the recent search distribution lambda times
	 * @see #resampleSingle(int)
	 * @see #updateDistribution()
	 * @see CMAParameters#getPopulationSize()
	 */
	private void samplePopulation() {
		double sum;

		if (state < 3 && state > 2)
			System.out.println("mixing of calls to updateSingle() and samplePopulation() is not possible");
		else    
			eigendecomposition(0); // latest possibility to generate B and diagD

		state = 1; // can be repeatedly called without problem
		idxRecentOffspring = sp.getLambda() - 1; // not really necessary at the moment


		// ensure maximal and minimal standard deviations
		if (options.lowerStandardDeviations != null && options.lowerStandardDeviations.length > 0) {
			for (int i = 0; i < N; ++i) {
				double d = options.lowerStandardDeviations[Math.min(i,options.lowerStandardDeviations.length-1)]; 
				if(d > sigma * minsqrtdiagC) 
					sigma = d / minsqrtdiagC;
			}
		}
		if (options.upperStandardDeviations != null && options.upperStandardDeviations.length > 0) {
			for (int i = 0; i < N; ++i) {
				double d = options.upperStandardDeviations[Math.min(i,options.upperStandardDeviations.length-1)]; 
				if (d < sigma * maxsqrtdiagC) 
					sigma = d / maxsqrtdiagC;
			}
		}

		testAndCorrectNumerics();
		
		/* sample the distribution */
		for (int iNk = 0; iNk < sp.getLambda(); ++iNk) { // generate scaled random vector (D * z)
			// code duplication from resampleSingle because of possible future resampling before GenoPheno
			/* generate scaled random vector (D * z) */
			List<Double> var = new ArrayList<Double>();
			
			if (flgdiag) { 
				for (int i = 0; i < N; ++i)
					var.add(xmean[i] + sigma * diagD[i] * RNG.nextGaussian());
					//arx[iNk].setValue(i, xmean[i] + sigma * diagD[i] * Util.nextGaussian());
			}
			else {
				for (int i = 0; i < N; ++i) 
					artmp[i] = diagD[i] * RNG.nextGaussian();

				/* add mutation (sigma * B * (D*z)) */
				for (int i = 0; i < N; ++i) {
					sum = 0;
					for (int j = 0; j < N; ++j) {
						sum += B[i][j] * artmp[j];
					}
					//arx[iNk].setValue(i, xmean[i] + sigma * sum);
					var.add(xmean[i] + sigma * sum);
				}
			}
			// redo this while isOutOfBounds(arx[iNk])
			NumberSolution<Double> sol = new NumberSolution<>(1, var);
			arx[iNk] = sol;
		}

		// I am desperately missing a const/readonly/visible qualifier. 
		//population = genoPhenoTransformation(arx, population);  
	}

	private void testAndCorrectNumerics() { // not much left here

		/* Flat Fitness, Test if function values are identical */
		if (task.getNumberOfIterations() > 1 || (task.getNumberOfIterations() == 1 && state >= 3)) {
			if (arx[0].getEval() == arx[Math.min(sp.getLambda()-1, sp.getLambda()/2+1) - 1].getEval()) {
				//System.out.println("flat fitness landscape, consider reformulation of fitness, step-size increased");
				sigma *= Math.exp(0.2+sp.getCs()/sp.getDamps());
			}
		}

		/* Align (renormalize) scale C (and consequently sigma) */
		/* e.g. for infinite stationary state simulations (noise
		 * handling needs to be introduced for that) */
		double fac = 1;
		if (math.max(diagD) < 1e-6) 
			fac = 1./math.max(diagD);
		else if (math.min(diagD) > 1e4)
			fac = 1./math.min(diagD);

		if (fac != 1.) {
			sigma /= fac;
			for(int i = 0; i < N; ++i) {
				pc[i] *= fac;
				diagD[i] *= fac;
				for (int j = 0; j <= i; ++j)
					C[i][j] *= fac*fac;
			}
		}
	}

	/* flgforce == 1 force independent of time measurments, 
	 * flgforce == 2 force independent of uptodate-status
	 */
	void eigendecomposition(int flgforce) {
		/* Update B and D, calculate eigendecomposition */
		int i, j;

		if (countCupdatesSinceEigenupdate == 0 && flgforce < 2)
			return;

		//  20% is usually better in terms of running *time* (only on fast to evaluate functions)
		if (!flgdiag && flgforce <= 0 
				&& (timings.eigendecomposition > 1000 + options.maxTimeFractionForEigendecomposition * (System.currentTimeMillis() - timings.starteigen)
				|| countCupdatesSinceEigenupdate < 1. / sp.getCcov() / N / 5.)) 
			return;

		if (flgdiag) {
			for (i = 0; i < N; ++i) {
				diagD[i] = Math.sqrt(C[i][i]);
			}
			countCupdatesSinceEigenupdate = 0;
			timings.starteigen = System.currentTimeMillis(); // reset starting time
			timings.eigendecomposition = 0;             // not really necessary
		} else {
			// set B <- C
			for (i = 0; i < N; ++i)
				for (j = 0; j <= i; ++j)
					B[i][j] = B[j][i] = C[i][j];

			// eigendecomposition
			double [] offdiag = new double[N];
			long firsttime = System.currentTimeMillis();
			tred2(N, B, diagD, offdiag);
			tql2(N, diagD, offdiag, B);
			timings.eigendecomposition += System.currentTimeMillis() - firsttime;

			if (options.checkEigenSystem > 0) //TODO remove
				checkEigenSystem(N, C, diagD, B); // for debugging

			// assign diagD to eigenvalue square roots
			for (i = 0; i < N; ++i) {
				if (diagD[i] < 0) // numerical problem?
					System.out.println("an eigenvalue has become negative");
				diagD[i] = Math.sqrt(diagD[i]);
			}
			countCupdatesSinceEigenupdate = 0;
		} // end Update B and D
		if (math.min(diagD) == 0) // error management is done elsewhere
			axisratio = Double.POSITIVE_INFINITY;
		else
			axisratio = math.max(diagD) / math.min(diagD);
	}

	// Symmetric Householder reduction to tridiagonal form, taken from JAMA package.
	private void tred2 (int n, double[][] V, double[] d, double[] e) {

		//  This is derived from the Algol procedures tred2 by
		//  Bowdler, Martin, Reinsch, and Wilkinson, Handbook for
		//  Auto. Comp., Vol.ii-Linear Algebra, and the corresponding
		//  Fortran subroutine in EISPACK.

		for (int j = 0; j < n; j++) {
			d[j] = V[n-1][j];
		}

		// Householder reduction to tridiagonal form.
		for (int i = n-1; i > 0; i--) {

			// Scale to avoid under/overflow.
			double scale = 0.0;
			double h = 0.0;
			for (int k = 0; k < i; k++) {
				scale = scale + Math.abs(d[k]);
			}
			if (scale == 0.0) {
				e[i] = d[i-1];
				for (int j = 0; j < i; j++) {
					d[j] = V[i-1][j];
					V[i][j] = 0.0;
					V[j][i] = 0.0;
				}
			} else {
				// Generate Householder vector.
				for (int k = 0; k < i; k++) {
					d[k] /= scale;
					h += d[k] * d[k];
				}
				double f = d[i-1];
				double g = Math.sqrt(h);
				if (f > 0) {
					g = -g;
				}
				e[i] = scale * g;
				h = h - f * g;
				d[i-1] = f - g;
				for (int j = 0; j < i; j++) {
					e[j] = 0.0;
				}

				// Apply similarity transformation to remaining columns.
				for (int j = 0; j < i; j++) {
					f = d[j];
					V[j][i] = f;
					g = e[j] + V[j][j] * f;
					for (int k = j+1; k <= i-1; k++) {
						g += V[k][j] * d[k];
						e[k] += V[k][j] * f;
					}
					e[j] = g;
				}
				f = 0.0;
				for (int j = 0; j < i; j++) {
					e[j] /= h;
					f += e[j] * d[j];
				}
				double hh = f / (h + h);
				for (int j = 0; j < i; j++) {
					e[j] -= hh * d[j];
				}
				for (int j = 0; j < i; j++) {
					f = d[j];
					g = e[j];
					for (int k = j; k <= i-1; k++) {
						V[k][j] -= (f * e[k] + g * d[k]);
					}
					d[j] = V[i-1][j];
					V[i][j] = 0.0;
				}
			}
			d[i] = h;
		}

		// Accumulate transformations.
		for (int i = 0; i < n-1; i++) {
			V[n-1][i] = V[i][i];
			V[i][i] = 1.0;
			double h = d[i+1];
			if (h != 0.0) {
				for (int k = 0; k <= i; k++) {
					d[k] = V[k][i+1] / h;
				}
				for (int j = 0; j <= i; j++) {
					double g = 0.0;
					for (int k = 0; k <= i; k++) {
						g += V[k][i+1] * V[k][j];
					}
					for (int k = 0; k <= i; k++) {
						V[k][j] -= g * d[k];
					}
				}
			}
			for (int k = 0; k <= i; k++) {
				V[k][i+1] = 0.0;
			}
		}
		for (int j = 0; j < n; j++) {
			d[j] = V[n-1][j];
			V[n-1][j] = 0.0;
		}
		V[n-1][n-1] = 1.0;
		e[0] = 0.0;
	} 

	// Symmetric tridiagonal QL algorithm, taken from JAMA package.
	private void tql2 (int n, double[] d, double[] e, double[][] V) {

		//  This is derived from the Algol procedures tql2, by
		//  Bowdler, Martin, Reinsch, and Wilkinson, Handbook for
		//  Auto. Comp., Vol.ii-Linear Algebra, and the corresponding
		//  Fortran subroutine in EISPACK.

		for (int i = 1; i < n; i++) {
			e[i-1] = e[i];
		}
		e[n-1] = 0.0;

		double f = 0.0;
		double tst1 = 0.0;
		double eps = Math.pow(2.0,-52.0);
		for (int l = 0; l < n; l++) {

			// Find small subdiagonal element
			tst1 = Math.max(tst1,Math.abs(d[l]) + Math.abs(e[l]));
			int m = l;
			while (m < n) {
				if (Math.abs(e[m]) <= eps*tst1) {
					break;
				}
				m++;
			}

			// If m == l, d[l] is an eigenvalue,
			// otherwise, iterate.
			if (m > l) {
				do {
					// Compute implicit shift
					double g = d[l];
					double p = (d[l+1] - g) / (2.0 * e[l]);
					double r = math.hypot(p,1.0);
					if (p < 0) {
						r = -r;
					}
					d[l] = e[l] / (p + r);
					d[l+1] = e[l] * (p + r);
					double dl1 = d[l+1];
					double h = g - d[l];
					for (int i = l+2; i < n; i++) {
						d[i] -= h;
					}
					f = f + h;

					// Implicit QL transformation.

					p = d[m];
					double c = 1.0;
					double c2 = c;
					double c3 = c;
					double el1 = e[l+1];
					double s = 0.0;
					double s2 = 0.0;
					for (int i = m-1; i >= l; i--) {
						c3 = c2;
						c2 = c;
						s2 = s;
						g = c * e[i];
						h = c * p;
						r = math.hypot(p,e[i]);
						e[i+1] = s * r;
						s = e[i] / r;
						c = p / r;
						p = c * d[i] - s * g;
						d[i+1] = h + s * (c * g + s * d[i]);

						// Accumulate transformation.

						for (int k = 0; k < n; k++) {
							h = V[k][i+1];
							V[k][i+1] = s * V[k][i] + c * h;
							V[k][i] = c * V[k][i] - s * h;
						}
					}
					p = -s * s2 * c3 * el1 * e[l] / dl1;
					e[l] = s * p;
					d[l] = c * p;

					// Check for convergence.

				} while (Math.abs(e[l]) > eps*tst1);
			}
			d[l] = d[l] + f;
			e[l] = 0.0;
		}

		// Sort eigenvalues and corresponding vectors.

		for (int i = 0; i < n-1; i++) {
			int k = i;
			double p = d[i];
			for (int j = i+1; j < n; j++) {
				if (d[j] < p) { // NH find smallest k>i
					k = j;
					p = d[j];
				}
			}
			if (k != i) {
				d[k] = d[i]; // swap k and i 
				d[i] = p;   
				for (int j = 0; j < n; j++) {
					p = V[j][i];
					V[j][i] = V[j][k];
					V[j][k] = p;
				}
			}
		}
	}

	/* 
    exhaustive test of the output of the eigendecomposition
    needs O(n^3) operations 

    produces error  
    returns number of detected inaccuracies 
	 */
	private int checkEigenSystem(int N, double[][] C, double[] diag, double[][] Q)
	{
		/* compute Q diag Q^T and Q Q^T to check */
		int i, j, k, res = 0;
		double cc, dd; 
		String s;

		for (i=0; i < N; ++i) {
			for (j=0; j < N; ++j) {
				for (cc=0.,dd=0., k=0; k < N; ++k) {
					cc += diag[k] * Q[i][k] * Q[j][k];
					dd += Q[i][k] * Q[j][k];
				}
				/* check here, is the normalization the right one? */
				if (Math.abs(cc - C[i>j?i:j][i>j?j:i])/Math.sqrt(C[i][i]*C[j][j]) > 1e-10 
						&& Math.abs(cc - C[i>j?i:j][i>j?j:i]) > 1e-9) { /* quite large */
					s = " " + i + " " + j + " " + cc + " " + C[i>j?i:j][i>j?j:i] + " " + (cc-C[i>j?i:j][i>j?j:i]);
					System.out.println("cmaes_t:Eigen(): imprecise result detected " + s);
					++res; 
				}
				if (Math.abs(dd - (i==j?1:0)) > 1e-10) {
					s = i + " " + j + " " + dd;
					System.out.println("cmaes_t:Eigen(): imprecise result detected (Q not orthog.) " + s);
					++res;
				}
			}
		}
		return res; 
	}

	private void setInitialX(double[] x) {
		/*if (state >= 0)
			System.out.println("initial x cannot be set anymore");*/
		if (x.length == 1) { // to make properties work
			setInitialX(x[0]);
			return;
		}
		if (N > 0 && N != x.length)
			System.out.println("dimensions do not match");
		if (N == 0)
			N = x.length;
		assert N == x.length;
		xmean = new double[N];
		System.arraycopy(x, 0, xmean, 0, N);
		lockDimension = 1; // because xmean is set up
	}

	private void setInitialX(double x) {
		/*if (state >= 0)
			System.out.println("initial x cannot be set anymore");*/
		xmean = new double[]{x}; // allows "late binding" of dimension N
	}

	/**
	 * 
	 * @param x
	 *            null or x.length==1 or x.length==dim, only for the second case
	 *            x is expanded
	 * @param dim dimension
	 * @return <code>null</code> or <code>double[] x</code> with
	 *         <code>x.length==dim</code>
	 */
	private double[] expandToDimension(double[] x, int dim) {
		if (x == null)
			return null;
		if (x.length == dim)
			return x;
		if (x.length != 1)
			System.out.println("x must have length one or length dimension");

		return getArrayOf(x[0], dim);
	}

	private double[] getArrayOf(double x, int dim) {
		double[] res = new double[dim];
		for (int i = 0; i < dim; ++i)
			res[i] = x;
		return res;
	}

	/**
	 * Tests termination criteria and evaluates to  greater than zero when a
	 * termination criterion is satisfied. Repeated tests append the met criteria repeatedly, 
	 * only if the evaluation count has changed.
	 */
	private void test() {
		/* Internal (numerical) stopping termination criteria */

		/* Test each principal axis i, whether x == x + 0.1 * sigma * rgD[i] * B[i] */
		for (int iAchse = 0; iAchse < N; ++iAchse) {
			int iKoo;
			int l = flgdiag ? iAchse : 0;
			int u = flgdiag ? iAchse+1 : N;
			double fac = 0.1 * sigma * diagD[iAchse];
			for (iKoo = l; iKoo < u; ++iKoo) { 
				if (xmean[iKoo] != xmean[iKoo] + fac * B[iKoo][iAchse])
					break; // is OK for this iAchse
			}
			if (iKoo == u) // no break, therefore no change for axis iAchse
				System.out.println("NoEffectAxis: Mutation " + 0.1*sigma*diagD[iAchse] + " in a principal axis " + iAchse + " has no effect");
		}

		/* Test whether one component of xmean is stuck */
		for (int iKoo = 0; iKoo < N; ++iKoo) {
			if (xmean[iKoo] == xmean[iKoo] + 0.2*sigma*Math.sqrt(C[iKoo][iKoo]))
				System.out.println("NoEffectCoordinate: Mutation of size " + 0.2*sigma*Math.sqrt(C[iKoo][iKoo]) + " in coordinate " + iKoo + " has no effect");
		}

		/* Condition number */
		if (math.min(diagD) <= 0)
			System.out.println("ConditionNumber: smallest eigenvalue smaller or equal zero");
		else if (math.max(diagD)/math.min(diagD) > 1e7)
			System.out.println("ConditionNumber: condition number of the covariance matrix exceeds 1e14");
	}

	@Override
	public void resetToDefaultsBeforeNewRun() {
	}
}

class Timing {
	Timing(){
		birth = System.currentTimeMillis();
		start = birth; // on the save side 
	}
	long birth; // time at construction, not really in use
	long start; // time at end of init()
	long starteigen; // time after flgdiag was turned off, ie when calls to eigen() start
	long eigendecomposition = 0; // spent time in eigendecomposition
}

/** some simple math utilities */
class MyMath { // implements java.io.Serializable {

	double prod(double []ar) {
		double res = 1.0;
		for(int i = 0; i < ar.length; ++i)
			res *= ar[i];
		return res;
	}

	public double median(double[] ar) {
		// need a copy of ar
		double [] ar2 = new double[ar.length];
		System.arraycopy(ar, 0, ar2, 0, ar.length);
		Arrays.sort(ar2);
		if (ar2.length % 2 == 0)
			return (ar2[ar.length/2] + ar2[ar.length/2-1]) / 2.;
		else    
			return ar2[ar.length/2];
	}

	/** @return Maximum value of 1-D double array */
	public double max(double[] ar) {
		int i;
		double m;
		m = ar[0];
		for (i = 1; i < ar.length; ++i) {
			if (m < ar[i])
				m = ar[i];
		}
		return m;
	}

	/** sqrt(a^2 + b^2) without under/overflow. **/
	double hypot(double a, double b) {
		double r  = 0;
		if (Math.abs(a) > Math.abs(b)) {
			r = b/a;
			r = Math.abs(a)*Math.sqrt(1+r*r);
		} else if (b != 0) {
			r = a/b;
			r = Math.abs(b)*Math.sqrt(1+r*r);
		}
		return r;
	}

	/** @return Minimum value of 1-D double array */
	public double min(double[] ar) {
		int i;
		double m;
		m = ar[0];
		for (i = 1; i < ar.length; ++i) {
			if (m > ar[i])
				m = ar[i];
		}
		return m;
	}

	/**
	 * @return Diagonal of an 2-D double array
	 */
	double[] diag(double[][] ar) {
		int i;
		double[] diag = new double[ar.length];
		for (i = 0; i < ar.length && i < ar[i].length; ++i)
			diag[i] = ar[i][i];
		return diag;
	}
}
