package org.um.feri.ears.quality_indicator;

import org.um.feri.ears.problems.moo.MOProblemBase;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.EuclideanDistance;
import org.um.feri.ears.util.ManhattanDistance;
import org.um.feri.ears.util.NondominatedPopulation;
import org.um.feri.ears.util.PointDistance;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * This class provides some utilities to compute quality indicators.
 **/
public final class QualityIndicatorUtil<T> {

    private QualityIndicatorUtil() {
    }


    /**
     * This method reads a Pareto Front for a file.
     *
     * @param path The path to the file that contains the pareto front
     * @return double [][] whit the pareto front
     **/
    public static double[][] readFront(String path) {
        try {
            // Open the file
            FileInputStream fis = new FileInputStream(path);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            List<double[]> list = new ArrayList<double[]>();
            int numberOfObjectives = 0;
            String aux = br.readLine();
            while (aux != null) {
                StringTokenizer st = new StringTokenizer(aux);
                int i = 0;
                numberOfObjectives = st.countTokens();
                double[] vector = new double[st.countTokens()];
                while (st.hasMoreTokens()) {
                    double value = Double.parseDouble(st.nextToken());
                    vector[i] = value;
                    i++;
                }
                list.add(vector);
                aux = br.readLine();
            }

            br.close();

            double[][] front = new double[list.size()][numberOfObjectives];
            for (int i = 0; i < list.size(); i++) {
                front[i] = list.get(i);
            }
            return front;

        } catch (Exception e) {
            System.out.println("InputFacilities crashed reading for file: " + path);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets the maximun values for each objectives in a given pareto front
     *
     * @param front        The pareto front
     * @param noObjectives Number of objectives in the pareto front
     * @return double [] An array of noOjectives values whit the maximun values for each objective
     **/
    public static double[] getMaximumValues(double[][] front, int noObjectives) {
        double[] maximumValue = new double[noObjectives];
        for (int i = 0; i < noObjectives; i++)
            maximumValue[i] = Double.NEGATIVE_INFINITY;

        for (double[] aFront : front) {
            for (int j = 0; j < aFront.length; j++) {
                if (aFront[j] > maximumValue[j])
                    maximumValue[j] = aFront[j];
            }
        }
        return maximumValue;
    }


    /**
     * Gets the minimun values for each objectives in a given pareto
     * front
     *
     * @param front        The pareto front
     * @param noObjectives Number of objectives in the pareto front
     * @return double [] An array of noOjectives values whit the minimum values
     * for each objective
     **/
    public static double[] getMinimumValues(double[][] front, int noObjectives) {
        double[] minimumValue = new double[noObjectives];
        for (int i = 0; i < noObjectives; i++)
            minimumValue[i] = Double.MAX_VALUE;

        for (double[] aFront : front) {
            for (int j = 0; j < aFront.length; j++) {
                if (aFront[j] < minimumValue[j])
                    minimumValue[j] = aFront[j];
            }
        }
        return minimumValue;
    }


    /**
     * Gets the distance between a point and the nearest one in a given front. The Euclidean distance
     * is assumed
     *
     * @param point The point
     * @param front The front that contains the other points to calculate the
     *              distances
     * @return The minimum distance between the point and the front
     */
    public static double distanceToNearestPoint(double[] point, double[][] front) {
        return distanceToNearestPoint(point, front, new EuclideanDistance());
    }

    /**
     * Gets the distance between a point and the nearest one in a front. If a distance equals to 0
     * is found, that means that the point is in the front, so it is excluded
     *
     * @param point The point
     * @param front The front that contains the other points to calculate the distances
     * @return The minimum distance between the point and the front
     */
    public static double distanceToNearestPoint(double[] point, double[][] front, PointDistance distance) {
        if (front == null) {
            System.err.println("The front is null");
            return Double.MAX_VALUE;
        } else if (front.length == 0) {
            System.err.println("The front is empty");
            return Double.MAX_VALUE;
        } else if (point == null) {
            System.err.println("The point is null");
            return Double.MAX_VALUE;
        }

        double minDistance = Double.MAX_VALUE;

        for (int i = 0; i < front.length; i++) {
            double aux = distance.compute(point, front[i]);
            if (aux < minDistance && aux > 0) {
                minDistance = aux;
            }
        }

        return minDistance;
    }

    /**
     * Returns the Manhattan distance in objective space between the two
     * solutions.
     *
     * @param pointA the first solution
     * @param pointB the second solution
     * @return the Manhattan distance in objective space between the two
     * solutions
     */
    public static double manhattanDistance(double[] pointA, double[] pointB) {
        return distance(pointA, pointB, new ManhattanDistance());
    }

    public static double distance(double[] ds, double[] ds2) {
        return distance(ds, ds2, new EuclideanDistance());
    }

    public static double distance(double[] ds, double[] ds2, PointDistance distance) {
        return distance.compute(ds, ds2);
    }

    public static double distanceToNearestPoint(int index, double[][] front) {
        return distanceToNearestPoint(index, front, new EuclideanDistance());
    }

    /**
     * Gets the distance between a point with {@code index} and the nearest one in
     * the given front
     *
     * @param index Index of the point
     * @param front The front that contains the other points to calculate the distances
     * @return The minimun distances greater than zero between the point and
     * the front
     */
    public static double distanceToNearestPoint(int index, double[][] front, PointDistance distance) {
        double minDistance = Double.MAX_VALUE;

        for (int i = 0; i < front.length; i++) {
            if (index == i)
                continue;
            double aux;

            aux = distance.compute(front[index], front[i]);

            if (aux < minDistance) {
                minDistance = aux;
            }
        }

        return minDistance;
    }

    /**
     * This method receives a pareto front and two points, one whit maximum values
     * and the other with minimum values allowed, and returns a the normalized Pareto front.
     *
     * @param front        A pareto front.
     * @param maximumValue The maximun values allowed
     * @param minimumValue The mininum values allowed
     * @return the normalized pareto front
     **/
    public static double[][] getNormalizedFront(double[][] front, double[] maximumValue, double[] minimumValue) {

        if (maximumValue == null || minimumValue == null)
            return front;

        double[][] normalizedFront = new double[front.length][];

        for (int i = 0; i < front.length; i++) {
            normalizedFront[i] = new double[front[i].length];
            for (int j = 0; j < front[i].length; j++) {
                if (front[i][j] < minimumValue[j]) {
                    System.err.println("Warning: when performing normaliazation, objective " + (j + 1) + " value " + front[i][j] + " is smaller than min " + minimumValue[j] + "");
                }
                if (front[i][j] > maximumValue[j]) {
                    System.err.println("Warning: when performing normaliazation, objective " + (j + 1) + " value " + front[i][j] + " is largeer than max " + maximumValue[j] + "");
                }
                normalizedFront[i][j] = (front[i][j] - minimumValue[j]) / (maximumValue[j] - minimumValue[j]);
            }
        }
        return normalizedFront;
    }

    /**
     * This method receives a pareto front and two points, one whit maximum values
     * and the other with minimum values allowed, and normalizes the objective values.
     *
     * @param front        A pareto front.
     * @param maximumValue The maximun values allowed
     * @param minimumValue The mininum values allowed
     **/
    public static <T extends Number> void normalizeFront(ParetoSolution<T> front, double[] maximumValue, double[] minimumValue) {

        MOSolutionBase<T> normSolution;
        for (int i = 0; i < front.size(); i++) {
            normSolution = front.get(i);
            for (int j = 0; j < front.get(i).numberOfObjectives(); j++) {
                normSolution.setObjective(j, (normSolution.getObjective(j) - minimumValue[j]) / (maximumValue[j] - minimumValue[j]));
            }
        }
    }

    public static <T extends Number, P extends MOProblemBase<T>> double[][] getNormalizedFront(double[][] front, P problem) {

        ParetoSolution<T> referenceSet = new ParetoSolution<T>(0);
        String fileName = problem.getFileName();
        double[][] normalizedFront = new double[front.length][];

        double[] maximumValue = new double[problem.getNumberOfObjectives()];
        double[] minimumValue = new double[problem.getNumberOfObjectives()];

        if (fileName != null && !fileName.isEmpty()) {
            referenceSet = QualityIndicatorUtil.readNonDominatedSolutionSet("pf_data/" + fileName + ".dat");
        } else {
            System.out.println("The file name containg the Paret front is not valid.");
        }


        for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
            minimumValue[i] = Double.POSITIVE_INFINITY;
            maximumValue[i] = Double.NEGATIVE_INFINITY;
        }

        for (int i = 0; i < referenceSet.size(); i++) {
            MOSolutionBase<T> solution = referenceSet.get(i);

            if (solution.violatesConstraints()) {
                continue;
            }

            for (int j = 0; j < problem.getNumberOfObjectives(); j++) {
                minimumValue[j] = Math.min(minimumValue[j], solution.getObjective(j));
                maximumValue[j] = Math.max(maximumValue[j], solution.getObjective(j));
            }
        }

        for (int i = 0; i < front.length; i++) {
            normalizedFront[i] = new double[front[i].length];
            for (int j = 0; j < front[i].length; j++) {
                normalizedFront[i][j] = (front[i][j] - minimumValue[j]) / (maximumValue[j] - minimumValue[j]);
            }
        }
        return normalizedFront;

    }


    /**
     * This method receives a normalized pareto front and return the inverted one.
     * This operation needed for minimization problems
     *
     * @param front The pareto front to inverse
     * @return The inverted pareto front
     **/
    public static double[][] invertedFront(double[][] front) {
        double[][] invertedFront = new double[front.length][];

        for (int i = 0; i < front.length; i++) {
            invertedFront[i] = new double[front[i].length];
            for (int j = 0; j < front[i].length; j++) {
                if (front[i][j] <= 1.0 && front[i][j] >= 0.0) {
                    invertedFront[i][j] = 1.0 - front[i][j];
                } else if (front[i][j] > 1.0) {
                    invertedFront[i][j] = 0.0;
                } else if (front[i][j] < 0.0) {
                    invertedFront[i][j] = 1.0;
                }
            }
        }
        return invertedFront;
    }

    public static <T extends Number> void invertedFront(ParetoSolution<T> population) {

        for (MOSolutionBase<T> sol : population) {
            for (int i = 0; i < sol.numberOfObjectives(); i++) {
                double value = sol.getObjective(i);
                if (value < 0.0) {
                    value = 0.0;
                } else if (value > 1.0) {
                    value = 1.0;
                }
                sol.setObjective(i, 1.0 - value);
            }
        }
    }


    /**
     * Reads a set of non dominated solutions from a file
     *
     * @param path The path of the file containing the data
     * @return A solution set
     */
    public static <T extends Number> ParetoSolution<T> readNonDominatedSolutionSet(String path) {

        InputStream inputStream = QualityIndicatorUtil.class.getResourceAsStream(path);
        if (inputStream == null) {
            System.out.println("\n Error: Cannot open input file for reading ");
        }

        ParetoSolution<T> solutionSet = null;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {

            solutionSet = new NondominatedPopulation<>();

            String aux = br.readLine();
            while (aux != null) {
                StringTokenizer st = new StringTokenizer(aux);
                int i = 0;
                MOSolutionBase<T> solution = new MOSolutionBase<T>(st.countTokens());
                while (st.hasMoreTokens()) {
                    double value = Double.parseDouble(st.nextToken());
                    solution.setObjective(i, value);
                    i++;
                }
                solutionSet.add(solution);
                aux = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return solutionSet;
    }

    /**
     * Reads reference point from file if available
     *
     * @param path The path of the file containing the data
     * @return A solution set
     */
    public static double[] readReferencePoint(String path, String problemName) {

        InputStream inputStream = QualityIndicatorUtil.class.getResourceAsStream(path);
        if (inputStream == null) {
            System.out.println("\n Error: Cannot open input file for reading ");
        }
        double[] referencePoint = null;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String name = "";

            String aux = br.readLine();
            while (aux != null) {
                StringTokenizer st = new StringTokenizer(aux);
                if (st.hasMoreTokens())
                    name = new String(st.nextToken());

                if (problemName.toLowerCase().equals(name.toLowerCase())) {
                    referencePoint = new double[st.countTokens()];

                    int i = 0;
                    while (st.hasMoreTokens()) {
                        double value = Double.parseDouble(st.nextToken());
                        referencePoint[i] = value;
                        i++;
                    }
                    break;
                }

                aux = br.readLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return referencePoint;
    }

    /**
     * Reads a set of non dominated solutions from a file
     * and store it in a existing non dominated solution set
     *
     * @param path The path of the file containing the data
     */
    public static <T> void readNonDominatedSolutionSet(String path, NondominatedPopulation solutionSet) {
        try {
            /* Open the file */
            FileInputStream fis = new FileInputStream(path);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            String aux = br.readLine();
            while (aux != null) {
                StringTokenizer st = new StringTokenizer(aux);
                int i = 0;
                MOSolutionBase<T> solution = new MOSolutionBase<T>(st.countTokens());

                while (st.hasMoreTokens()) {
                    double value = Double.parseDouble(st.nextToken());
                    solution.setObjective(i, value);
                    i++;
                }
                solutionSet.add(solution);
                aux = br.readLine();
            }
            br.close();
        } catch (Exception e) {
            System.out.println("jmetal.qualityIndicator.util.readNonDominatedSolutionSet: " + path);
            e.printStackTrace();
        }
    }


    /**
     * Calculates how much hypervolume each point dominates exclusively. The points
     * have to be transformed beforehand, to accommodate the assumptions of Zitzler's
     * hypervolume code.
     * @param front transformed objective values
     * @return HV contributions
     */
/*	public static double[] hvContributions(int numberOfobjectives, double[][] front) {
		Hypervolume hypervolume = new Hypervolume();
		int numberOfObjectives = numberOfobjectives;
		double[] contributions = new double[front.length];
		double[][] frontSubset = new double[front.length - 1][front[0].length];
		LinkedList<double[]> frontCopy = new LinkedList<double[]>();
		Collections.addAll(frontCopy, front);
		double[][] totalFront = frontCopy.toArray(frontSubset);
		double totalVolume = hypervolume.calculateHypervolume(totalFront,
				totalFront.length, numberOfObjectives);
		for (int i = 0; i < front.length; i++) {
			double[] evaluatedPoint = frontCopy.remove(i);
			frontSubset = frontCopy.toArray(frontSubset);
			// STEP4. The hypervolume (control is passed to java version of Zitzler code)
			double hv = hypervolume.calculateHypervolume(frontSubset, frontSubset.length, numberOfObjectives);
			double contribution = totalVolume - hv;
			contributions[i] = contribution;
			// put point back
			frontCopy.add(i, evaluatedPoint);
		}
		return contributions;
	}*/


    /**
     * Calculates the hv contribution of different populations.
     * Receives an array of populations and computes the contribution to HV of the
     * population consisting in the union of all of them
     * @param populations, consisting in all the populatoins
     * @return HV contributions of each population
     **//*
	public static double[] hvContributions(MOParetoIndividual[] populations) {
		boolean empty = true;
		for (MOParetoIndividual population2 : populations)
			if (population2.size() > 0)
				empty = false;

		if (empty) {
			double[] contributions = new double[populations.length];
			for (int i = 0; i < populations.length; i++)
				contributions[i] = 0;
			for (int i = 0; i < populations.length; i++)
				System.out.println(contributions[i]);
			return contributions;
		}

		MOParetoIndividual union;
		int size = 0;
		double offset_ = 0.0;

		// determining the global size of the population
		for (MOParetoIndividual population1 : populations)
			size += population1.size();

		// allocating space for the union
		union = new MOParetoIndividual(size);

		// filling union
		for (MOParetoIndividual population : populations)
			for (int j = 0; j < population.size(); j++)
				union.add(population.get(j));

		// determining the number of objectives
		int numberOfObjectives = union.get(0).numberOfObjectives();

		// writing everything in matrices
		double[][][] frontValues = new double[populations.length + 1][][];

		frontValues[0] = union.writeObjectivesToMatrix();
		for (int i = 0; i < populations.length; i++)
			if (populations[i].size() > 0)
				frontValues[i + 1] = populations[i].writeObjectivesToMatrix();
			else
				frontValues[i + 1] = new double[0][];

		// obtain the maximum and minimum values of the Pareto front
		double[] maximumValues = getMaximumValues(union.writeObjectivesToMatrix(), numberOfObjectives);
		double[] minimumValues = getMinimumValues(union.writeObjectivesToMatrix(), numberOfObjectives);

		// normalized all the fronts
		double[][][] normalizedFront = new double[populations.length + 1][][];
		for (int i = 0; i < normalizedFront.length; i++) {
			if (frontValues[i].length > 0)
				normalizedFront[i] = getNormalizedFront(frontValues[i], maximumValues, minimumValues);
			else
				normalizedFront[i] = new double[0][];
		}

		// compute offsets for reference point in normalized space
		double[] offsets = new double[maximumValues.length];
		for (int i = 0; i < maximumValues.length; i++) {
			offsets[i] = offset_ / (maximumValues[i] - minimumValues[i]);
		}

		// Inverse all the fronts front. This is needed because the original
		// metric by Zitzler is for maximization problems

		double[][][] invertedFront = new double[populations.length + 1][][];
		for (int i = 0; i < invertedFront.length; i++)
			if (normalizedFront[i].length > 0)
				invertedFront[i] = invertedFront(normalizedFront[i]);
			else
				invertedFront[i] = new double[0][];

		// shift away from origin, so that boundary points also get a
		// contribution > 0
		for (double[][] anInvertedFront : invertedFront) {
			for (double[] point : anInvertedFront) {
				for (int i = 0; i < point.length; i++) {
					point[i] += offsets[i];
				}
			}
		}

		// calculate contributions
		double[] contribution = new double[populations.length];
		Hypervolume hypervolume = new Hypervolume();

		for (int i = 0; i < populations.length; i++) {
			if (invertedFront[i + 1].length == 0)
				contribution[i] = 0;
			else {
				if (invertedFront[i + 1].length != invertedFront[0].length) {
					double[][] aux = new double[invertedFront[0].length - invertedFront[i + 1].length][];
					int startPoint = 0, endPoint;
					for (int j = 0; j < i; j++) {
						startPoint += invertedFront[j + 1].length;
					}
					endPoint = startPoint + invertedFront[i + 1].length;
					int index = 0;
					for (int j = 0; j < invertedFront[0].length; j++) {
						if (j < startPoint || j >= (endPoint)) {
							aux[index++] = invertedFront[0][j];
						}
					}
					// System.out.println(hypervolume.calculateHypervolume(invertedFront[0], invertedFront[0].length, numberOfObjectives));
					// System.out.println(hypervolume.calculateHypervolume(aux, aux.length, numberOfObjectives));
					contribution[i] = hypervolume.calculateHypervolume(invertedFront[0], invertedFront[0].length, numberOfObjectives) - hypervolume.calculateHypervolume(aux, aux.length, numberOfObjectives);
				} else {
					contribution[i] = hypervolume.calculateHypervolume(invertedFront[0], invertedFront[0].length, numberOfObjectives);
				}
			}
		}
		// for (int i = 0; i < contribution.length; i++)
		// System.out.println(invertedFront[0].length +" "+ invertedFront[i+1].length +" "+ contribution[i]);
		return contribution;
	}
	  
	*//**
     * Calculates the hv contribution of different populations.
     * Receives an array of populations and computes the contribution to HV of the
     * population consisting in the union of all of them
     * @param populations, consisting in all the populatoins
     * @return HV contributions of each population
     **//*
	public static double[] hvContributions(MOParetoIndividual archive, MOParetoIndividual[] populations) {

		MOParetoIndividual union;
		int size = 0;
		double offset_ = 0.0;

		// determining the global size of the population
		for (MOParetoIndividual population : populations)
			size += population.size();

		// allocating space for the union
		union = archive;

		// determining the number of objectives
		int numberOfObjectives = union.get(0).numberOfObjectives();

		// writing everything in matrices
		double[][][] frontValues = new double[populations.length + 1][][];

		frontValues[0] = union.writeObjectivesToMatrix();
		for (int i = 0; i < populations.length; i++)
			if (populations[i].size() > 0)
				frontValues[i + 1] = populations[i].writeObjectivesToMatrix();
			else
				frontValues[i + 1] = new double[0][];

		// obtain the maximum and minimum values of the Pareto front
		double[] maximumValues = getMaximumValues(union.writeObjectivesToMatrix(), numberOfObjectives);
		double[] minimumValues = getMinimumValues(union.writeObjectivesToMatrix(), numberOfObjectives);

		// normalized all the fronts
		double[][][] normalizedFront = new double[populations.length + 1][][];
		for (int i = 0; i < normalizedFront.length; i++) {
			if (frontValues[i].length > 0)
				normalizedFront[i] = getNormalizedFront(frontValues[i], maximumValues, minimumValues);
			else
				normalizedFront[i] = new double[0][];
		}

		// compute offsets for reference point in normalized space
		double[] offsets = new double[maximumValues.length];
		for (int i = 0; i < maximumValues.length; i++) {
			offsets[i] = offset_ / (maximumValues[i] - minimumValues[i]);
		}

		// Inverse all the fronts front. This is needed because the original
		// metric by Zitzler is for maximization problems

		double[][][] invertedFront = new double[populations.length + 1][][];
		for (int i = 0; i < invertedFront.length; i++)
			if (normalizedFront[i].length > 0)
				invertedFront[i] = invertedFront(normalizedFront[i]);
			else
				invertedFront[i] = new double[0][];

		// shift away from origin, so that boundary points also get a
		// contribution > 0
		for (double[][] anInvertedFront : invertedFront) {
			for (double[] point : anInvertedFront) {
				for (int i = 0; i < point.length; i++) {
					point[i] += offsets[i];
				}
			}
		}

		// calculate contributions
		double[] contribution = new double[populations.length];
		Hypervolume hypervolume = new Hypervolume();

		for (int i = 0; i < populations.length; i++) {
			if (invertedFront[i + 1].length == 0)
				contribution[i] = 0;
			else {

				int auxSize = 0;
				for (int j = 0; j < populations.length; j++) {
					if (j != i)
						auxSize += invertedFront[j + 1].length;
				}

				if (size == archive.size()) { // the contribution is the maximum hv
					contribution[i] = hypervolume.calculateHypervolume(invertedFront[0], invertedFront[0].length, numberOfObjectives);
				} else {
					// make a front with all the populations but the target one
					int index = 0;
					double[][] aux = new double[auxSize][];
					for (int j = 0; j < populations.length; j++) {
						if (j != i)
							for (int k = 0; k < populations[j].size(); k++)
								aux[index++] = invertedFront[j + 1][k];
					}
					contribution[i] = hypervolume.calculateHypervolume(invertedFront[0], invertedFront[0].length, numberOfObjectives) - hypervolume.calculateHypervolume(aux, aux.length, numberOfObjectives);
				}

				
				 * int size2 = 0; for (int j = 0; j < populations.length; j++)
				 * size2+=invertedFront[j+1].length;
				 * 
				 * 
				 * double [][] aux = new double[size2 -
				 * invertedFront[i+1].length][]; int index = 0; for (int j = 0;
				 * j < populations.length; j++) { if (j!=i) { for (int k = 0; k
				 * < invertedFront[j+1].length; k++) aux[index++] =
				 * invertedFront[j+1][k]; } }
				 * 
				 * System.out.println(hypervolume.calculateHypervolume(invertedFront
				 * [0], invertedFront[0].length, numberOfObjectives));
				 * System.out.println(index+" "+aux.length);
				 * System.out.println(hypervolume.calculateHypervolume(aux,
				 * aux.length, numberOfObjectives));
				 * 
				 * 
				 * 
				 * contribution[i] =
				 * hypervolume.calculateHypervolume(invertedFront[0],
				 * invertedFront[0].length, numberOfObjectives) -
				 * hypervolume.calculateHypervolume(aux, aux.length,
				 * numberOfObjectives);
				 
			}
		}

		// for (int i = 0; i < contribution.length; i++)
		// System.out.println(invertedFront[0].length +" "+
		// invertedFront[i+1].length +" "+ contribution[i]);

		return contribution;
	}*/

}
