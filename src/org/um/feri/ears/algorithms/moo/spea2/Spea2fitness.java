//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.

package org.um.feri.ears.algorithms.moo.spea2;

import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.comparator.DominanceComparator;
import org.um.feri.ears.util.comparator.MOFitnessComparator;
import org.um.feri.ears.util.Distance;

import java.util.*;

public class Spea2fitness {

    private double[][] distance;

    private ParetoSolution solutionSet;

    private static final Distance DISTANCE = new Distance();

    private static final Comparator distanceNodeComparator = new DistanceNodeComparator();

    private static final Comparator dominance = new DominanceComparator();

    public Spea2fitness(ParetoSolution solutionSet) {
        distance = DISTANCE.distanceMatrix(solutionSet);
        this.solutionSet = solutionSet;
        for (int i = 0; i < this.solutionSet.size(); i++) {
            this.solutionSet.get(i).setLocation(i);
        } // for
    } // Spea2Fitness

    public void fitnessAssign() {
        double[] strength = new double[solutionSet.size()];
        double[] rawFitness = new double[solutionSet.size()];
        double kDistance;

        // Calculate the strength value strength(i) = |{j | j <- SolutionSet and i dominate j}|
        for (int i = 0; i < solutionSet.size(); i++) {
            for (int j = 0; j < solutionSet.size(); j++) {
                if (dominance.compare(solutionSet.get(i), solutionSet.get(j)) == -1) {
                    strength[i] += 1.0;
                }
            }
        }

        // Calculate the raw fitness rawFitness(i) = |{sum strenght(j) | j <- SolutionSet and j dominate i}|
        for (int i = 0; i < solutionSet.size(); i++) {
            for (int j = 0; j < solutionSet.size(); j++) {
                if (dominance.compare(solutionSet.get(i), solutionSet.get(j)) == 1) {
                    rawFitness[i] += strength[j];
                }
            }
        }

        // Add the distance to the k-th individual. In the reference paper of SPEA2,
        // k = sqrt(population.size()), but a value of k = 1 recommended. See
        // http://www.tik.ee.ethz.ch/pisa/selectors/spea2/spea2_documentation.txt
        int k = 1;
        for (int i = 0; i < distance.length; i++) {
            Arrays.sort(distance[i]);
            kDistance = 1.0 / (distance[i][k] + 2.0); // Calcule de D(i) distance
            // population.get(i).setFitness(rawFitness[i]);
            solutionSet.get(i).setParetoFitness(rawFitness[i] + kDistance);
        }
    }

    /**
     * Gets 'size' elements from a population of more than 'size' elements using
     * for this de enviromentalSelection truncation
     *
     * @param size The number of elements to get.
     */
    public ParetoSolution environmentalSelection(int size) {

        if (solutionSet.size() < size) {
            size = solutionSet.size();
        }

        // Create a new auxiliar population for no alter the original population
        ParetoSolution aux = new ParetoSolution(solutionSet.size());

        int i = 0;
        while (i < solutionSet.size()) {
            if (solutionSet.get(i).getParetoFitness() < 1.0) {
                aux.add(solutionSet.get(i));
                solutionSet.remove(i);
            } else {
                i++;
            }
        }

        // archive not full yet
        if (aux.size() < size) {
            Comparator comparator = new MOFitnessComparator();
            solutionSet.sort(comparator);
            int remain = size - aux.size();
            for (i = 0; i < remain; i++) {
                aux.add(solutionSet.get(i));
            }
            return aux;
        } else if (aux.size() == size) {
            return aux;
        }

        // archive overfilled

        double[][] distance = DISTANCE.distanceMatrix(aux);
        List<List<DistanceNode>> distanceList = new LinkedList<List<DistanceNode>>();
        for (int pos = 0; pos < aux.size(); pos++) {
            aux.get(pos).setLocation(pos);
            List<DistanceNode> distanceNodeList = new ArrayList<DistanceNode>();
            for (int ref = 0; ref < aux.size(); ref++) {
                if (pos != ref) {
                    distanceNodeList.add(new DistanceNode(distance[pos][ref], ref));
                }
            }
            distanceList.add(distanceNodeList);
        }

        for (List<DistanceNode> aDistanceList : distanceList) {
            Collections.sort(aDistanceList, distanceNodeComparator);
        }

        while (aux.size() > size) {
            double minDistance = Double.MAX_VALUE;
            int toRemove = 0;
            i = 0;
            for (List<DistanceNode> dn : distanceList) {
                if (dn.get(0).getDistance() < minDistance) {
                    toRemove = i;
                    minDistance = dn.get(0).getDistance();
                    // i y toRemove have the same distance to the first solution
                } else if (dn.get(0).getDistance() == minDistance) {
                    int k = 0;
                    while ((dn.get(k).getDistance() == distanceList.get(toRemove).get(k).getDistance()) && k < (distanceList.get(i).size() - 1)) {
                        k++;
                    }

                    if (dn.get(k).getDistance() < distanceList.get(toRemove).get(k).getDistance()) {
                        toRemove = i;
                    }
                }
                i++;
            }

            int tmp = aux.get(toRemove).getLocation();
            aux.remove(toRemove);
            distanceList.remove(toRemove);

            for (List<DistanceNode> aDistanceList : distanceList) {
                Iterator<DistanceNode> interIterator = aDistanceList.iterator();
                while (interIterator.hasNext()) {
                    if (interIterator.next().getReference() == tmp) {
                        interIterator.remove();
                    }
                }
            }
        }
        return aux;
    }
}
