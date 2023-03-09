//  WFGHypervolume.java
//
//  Authors:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2013 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
//
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.
//

//  CREDIT
//  This class is based on the code of the WFG group (http://www.wfg.csse.uwa.edu.au/hypervolume/)
//  Copyright (C) 2010 Lyndon While, Lucas Bradstreet.

package org.um.feri.ears.quality_indicator;

import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.comparator.PointComparator;
import org.um.feri.ears.util.Point;

import java.util.Comparator;

/**
 * This class implements the exact hypervolume indicator.
 * Reference: While L, Bradstreet L, Barone L.
 * A fast way of calculating exact hypervolumes,
 * IEEE Transactions on Evolutionary Computation, vol. 16, no. 1,
 * pp. 86-95, 2012.
 */
public class WFGHypervolume<T extends Number> extends QualityIndicator<T> {

    static final int OPT = 2;
    ParetoSolution<T>[] fs;
    private Point referencePoint;
    boolean maximizing;
    private int currentDeep;
    private int currentDimension;
    private int maxNumberOfPoints;
    private Comparator<NumberSolution<T>> pointComparator;

    /**
     * Constructor Creates a new instance of MultiDelta
     */
    public WFGHypervolume(int numObj, String fileName) {
        super(numObj, fileName, (ParetoSolution<T>) getReferenceSet(fileName));
        name = "WFGHypervolume";

        maximizing = false;
        currentDeep = 0;
        pointComparator = new PointComparator();

        currentDimension = numObj;
        numberOfObjectives = numObj;

    }

    @Override
    public double evaluate(ParetoSolution<T> paretoFrontApproximation) {

        ParetoSolution<T> copy = new ParetoSolution<T>(paretoFrontApproximation.getCapacity());

        for (NumberSolution<T> solution : paretoFrontApproximation) {
            NumberSolution<T> clone = solution.copy();
            copy.add(clone);
        }

        QualityIndicatorUtil.normalizeFront(copy, maximumValue, minimumValue);
        QualityIndicatorUtil.invertedFront(copy);

        referencePoint = new Point(numberOfObjectives);
        for (int i = 0; i < numberOfObjectives; i++) {
            referencePoint.setDimensionValue(i, 0.0);
        }


        this.maxNumberOfPoints = paretoFrontApproximation.size();
        int maxd = this.maxNumberOfPoints - (OPT / 2 + 1);
        fs = new ParetoSolution[maxd];
        for (int i = 0; i < maxd; i++) {
            fs[i] = new ParetoSolution<T>(maxNumberOfPoints, numberOfObjectives);
        }


        return getHV(paretoFrontApproximation);
    }

    public double get2DHV(ParetoSolution<T> front) {
        double hv = 0.0;

        hv = Math.abs((front.get(0).getObjective(0) - referencePoint.getDimensionValue(0)) *
                (front.get(0).getObjective(1) - referencePoint.getDimensionValue(1)));

        int v = front.getCapacity();
        for (int i = 1; i < front.getCapacity(); i++) {
            hv += Math.abs((front.get(i).getObjective(0) - referencePoint.getDimensionValue(0)) *
                    (front.get(i).getObjective(1) - front.get(i - 1).getObjective(1)));

        }

        return hv;
    }

    public double getInclusiveHV(NumberSolution<T> point) {
        double volume = 1;
        for (int i = 0; i < currentDimension; i++) {
            volume *= Math.abs(point.getObjective(i) - referencePoint.getDimensionValue(i));
        }

        return volume;
    }

    public double getExclusiveHV(ParetoSolution<T> front, int point) {
        double volume;

        volume = getInclusiveHV(front.get(point));
        if (front.getCapacity() > point + 1) {
            makeDominatedBit(front, point);
            double v = getHV(fs[currentDeep - 1]);
            volume -= v;
            currentDeep--;
        }

        return volume;
    }

    public double getHV(ParetoSolution<T> front) {
        double volume;
        front.sort(pointComparator);

        if (currentDimension == 2) {
            volume = get2DHV(front);
        } else {
            volume = 0.0;

            currentDimension--;
            int numberOfPoints = front.getCapacity();
            for (int i = numberOfPoints - 1; i >= 0; i--) {
                volume += Math.abs(front.get(i).getObjective(currentDimension) -
                        referencePoint.getDimensionValue(currentDimension)) *
                        this.getExclusiveHV(front, i);
            }
            currentDimension++;
        }

        return volume;
    }


    public void makeDominatedBit(ParetoSolution<T> front, int p) {
        int z = front.getCapacity() - 1 - p;

        for (int i = 0; i < z; i++) {
            for (int j = 0; j < currentDimension; j++) {
                NumberSolution<T> point1 = front.get(p);
                NumberSolution<T> point2 = front.get(p + 1 + i);
                double worseValue = worse(point1.getObjective(j), point2.getObjective(j), false);
                int cd = currentDeep;
                NumberSolution<T> point3 = fs[currentDeep].get(i);
                point3.setObjective(j, worseValue);
            }
        }

        NumberSolution<T> t;
        fs[currentDeep].setCapacity(1);

        for (int i = 1; i < z; i++) {
            int j = 0;
            boolean keep = true;
            while (j < fs[currentDeep].getCapacity() && keep) {
                switch (dominates2way(fs[currentDeep].get(i), fs[currentDeep].get(j))) {
                    case -1:
                        t = fs[currentDeep].get(j);
                        fs[currentDeep].setCapacity(fs[currentDeep].getCapacity() - 1);
                        fs[currentDeep].set(j,
                                fs[currentDeep].get(fs[currentDeep].getCapacity()));
                        fs[currentDeep].set(fs[currentDeep].getCapacity(), t);
                        break;
                    case 0:
                        j++;
                        break;
                    default:
                        keep = false;
                        break;
                }
            }
            if (keep) {
                t = fs[currentDeep].get(fs[currentDeep].getCapacity());
                fs[currentDeep].set(fs[currentDeep].getCapacity(), fs[currentDeep].get(i));
                fs[currentDeep].set(i, t);
                fs[currentDeep].setCapacity(fs[currentDeep].getCapacity() + 1);
            }
        }

        currentDeep++;
    }

    public int getLessContributorHV(ParetoSolution<T> solutionList) {
        ParetoSolution<T> wholeFront = loadFront(solutionList, -1);

        int index = 0;
        double contribution = Double.POSITIVE_INFINITY;

        for (int i = 0; i < solutionList.getCapacity(); i++) {
            double[] v = new double[solutionList.get(i).getNumberOfObjectives()];
            for (int j = 0; j < v.length; j++) {
                v[j] = solutionList.get(i).getObjective(j);
            }

            double aux = this.getExclusiveHV(wholeFront, i);
            if ((aux) < contribution) {
                index = i;
                contribution = aux;
            }

			  /*HypervolumeContribution<Solution<?>> hvc = new HypervolumeContribution<Solution<?>>() ;
			  hvc.setAttribute(solutionList.get(i), aux);*/
            //solutionList.get(i).setCrowdingDistance(aux);
        }

        return index;
    }

    private ParetoSolution<T> loadFront(ParetoSolution<T> solutionSet, int notLoadingIndex) {
        int numberOfPoints;
        if (notLoadingIndex >= 0 && notLoadingIndex < solutionSet.size()) {
            numberOfPoints = solutionSet.size() - 1;
        } else {
            numberOfPoints = solutionSet.size();
        }

        int dimensions = solutionSet.get(0).getNumberOfObjectives();

        ParetoSolution<T> front = new ParetoSolution<T>(numberOfPoints);

        int index = 0;
        for (int i = 0; i < solutionSet.size(); i++) {
            if (i != notLoadingIndex) {
                NumberSolution<T> point = new NumberSolution<T>(dimensions);
                for (int j = 0; j < dimensions; j++) {
                    point.setObjective(j, solutionSet.get(i).getObjective(j));
                }
                front.set(index++, point);
            }
        }

        return front;
    }

    private double worse(double x, double y, boolean maximizing) {
        double result;
        if (maximizing) {
			result = Math.min(x, y);
        } else {
			result = Math.max(x, y);
        }
        return result;
    }

    int dominates2way(NumberSolution<T> p, NumberSolution<T> q) {
        // returns -1 if p dominates q, 1 if q dominates p, 2 if p == q, 0 otherwise
        // ASSUMING MINIMIZATION

        // domination could be checked in either order

        for (int i = currentDimension - 1; i >= 0; i--) {
            if (p.getObjective(i) < q.getObjective(i)) {
                for (int j = i - 1; j >= 0; j--) {
                    if (q.getObjective(j) < p.getObjective(j)) {
                        return 0;
                    }
                }
                return -1;
            } else if (q.getObjective(i) < p.getObjective(i)) {
                for (int j = i - 1; j >= 0; j--) {
                    if (p.getObjective(j) < q.getObjective(j)) {
                        return 0;
                    }
                }
                return 1;
            }
        }
        return 2;
    }

    @Override
    public IndicatorType getIndicatorType() {
        return QualityIndicator.IndicatorType.UNARY;
    }

    @Override
    public boolean isMin() {
        return false;
    }

    @Override
    public boolean requiresReferenceSet() {
        return true; // for normalization
    }

    @Override
    public int compare(ParetoSolution<T> front1, ParetoSolution<T> front2, Double epsilon) {
        return 0;
    }
}