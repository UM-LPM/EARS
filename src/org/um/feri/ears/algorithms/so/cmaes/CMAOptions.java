package org.um.feri.ears.algorithms.so.cmaes;

import java.util.Properties;

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

/** Simple container of (mostly generic) options for the
 * optimization, like the maximum number of objective
 * function evaluations, see class fields.  No explicit setting of 
 * options is needed to 
 * initialize the CMA-ES ({@link CMAEvolutionStrategy#init()}) 
 * and options of the CMA-ES can be set
 * and changed any time, either via a property file and the method
 * {@link CMAEvolutionStrategy#readProperties()}, or new values can simply be 
 * assigned to the fields of the public <code>opts</code> field of 
 * the class <code>CMAEvolutionStrategy</code> (yeah, I know, not exactly Java style).
 * 
 */
public class CMAOptions implements java.io.Serializable {
	// needs to be public to make sure that a using class can excess Options.
	// Therefore, if not nested, needs to move into a separate file

	private static final long serialVersionUID = 2255162105325585121L;

	/** number of initial iterations with diagonal covariance matrix, where
	 * 1 means always. Default is 
	 * diagonalCovarianceMatrix=0, but this will presumably change in future. 
	 * As long as iterations<=diagonalCovarianceMatrix 
	 * the internal time complexity is linear in the search space dimensionality
	 * (memory requirements remain quadratic). 
	 */
	public long diagonalCovarianceMatrix = 0; // -1; 

	/** lower bound for standard deviations (step sizes). The
	 * Array can be of any length. The i-th entry corresponds to
	 * the i-th variable. If length&#60;dim the last entry is recycled for
	 * all remaining variables. Zero entries mean, naturally, no
	 * lower bound. <P>CAVE: there is an interference with stopTolX (and stopTolXFactor):
	 * if lowerStdDev is larger than stopTolX, the termination criterion
	 * can never be satisfied.</P> 
	 * <p>Example:
	 * <pre> CMAEvolutionStrategy es = new CMAEvolutionStrategy(); 
	 * es.options.lowerStandardDeviations = new double[]{1e-4,1e-8}; // 1e-8 for all but first variable
	 * </pre> 
	 * @see #stopTolX
	 * @see #stopTolXFactor
	 * */
	public double[] lowerStandardDeviations;
	/** upper bound for standard deviations (step lengths). 
	 * Zero entries mean no upper
	 * bound. Be aware of the interference with option stopTolUpXFactor. 
	 * @see #lowerStandardDeviations
	 * @see #stopTolUpXFactor
	 * */
	public double[] upperStandardDeviations;

	/** if true stopping message "Manual:..." is generated */
	public boolean stopnow = false; 


	/** only for >= 1 results are always exactly reproducible, as otherwise the update of the 
	 * eigensystem is conducted depending on time measurements, defaut is 0.2 */
	public double maxTimeFractionForEigendecomposition = 0.2;
	/** default is 0.1 
	 */
	public double maxTimeFractionForWriteToDefaultFiles = 0.1;

	/** checks eigendecomposition mainly for debugging purpose, default is 0==no-check; 
	 * the function checkEigenSystem requires O(N^3) operations. 
	 */
	public int checkEigenSystem = 0;

}

