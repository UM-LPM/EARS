package org.um.feri.ears.qualityIndicator.wfg;

import java.util.Comparator;

import org.um.feri.ears.problems.moo.MOProblemBase;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.qualityIndicator.MetricsUtil;
import org.um.feri.ears.qualityIndicator.QualityIndicator;
import org.um.feri.ears.util.PointComparator;

/**
 * Created by ajnebro on 2/2/15.
 */
public class WfgHypervolume<T> extends QualityIndicator<T>{
	
	private double offset = 0.0;
	static final int OPT = 2;
	ParetoSolution<T>[] fs;
	private MOSolutionBase<T> referencePoint;
	boolean maximizing;
	private int currentDeep;
	private int currentDimension;
	private Comparator<MOSolutionBase<T>> pointComparator;

	public WfgHypervolume(MOProblemBase moProblemBase) {
		super(moProblemBase, (ParetoSolution<T>) getReferenceSet(moProblemBase.getFileName()));
		
		name = "WFG Hypervolume";
		maximizing = false;
		currentDeep = 0;
		currentDimension = moProblemBase.getNumberOfObjectives();
		numberOfObjectives = moProblemBase.getNumberOfObjectives();
		pointComparator = new PointComparator();

		referencePoint = new MOSolutionBase<T>(numberOfObjectives);
		for (int i = 0; i < numberOfObjectives; i++) {
			referencePoint.setObjective(i, 0.0);
		}

	}


	@Override
	public double evaluate(ParetoSolution<T> population) {

		double hv = 0;
		
		ParetoSolution<T> copy = new ParetoSolution<T>(population);
		
		MetricsUtil.normalizeFront(copy, maximumValue, minimumValue);
		MetricsUtil.invertedFront(copy);
		
		int maxd = copy.size() - (OPT / 2 + 1);
		fs = new ParetoSolution[maxd];
		for (int i = 0; i < maxd; i++) {
			fs[i] = new ParetoSolution<T>(copy.size(), numberOfObjectives);
		}
		
		if (population.size() == 0) {
			hv = 0.0;
		} else {
			numberOfObjectives = population.get(0).numberOfObjectives();
			updateReferencePoint(copy);

			if (numberOfObjectives == 2) {
				//Collections.sort(population, new ObjectiveComparator<Solution<?>>(numberOfObjectives-1, ObjectiveComparator.Ordering.DESCENDING));
				//hv = get2DHV(population) ;
			} else {

				hv = getHV(new ParetoSolution<T>(copy));
			}
		}

		return hv;
	}

	/**
	 * Updates the reference point
	 */
	private void updateReferencePoint(ParetoSolution<T> front) {
		double[] maxObjectives = new double[numberOfObjectives];
		for (int i = 0; i < numberOfObjectives; i++) {
			maxObjectives[i] = 0;
		}

		for (int i = 0; i < front.size(); i++) {
			for (int j = 0; j < numberOfObjectives; j++) {
				if (maxObjectives[j] < front.get(i).getObjective(j)) {
					maxObjectives[j] = front.get(i).getObjective(j) ;
				}
			}
		}

		for (int i = 0; i < referencePoint.numberOfObjectives(); i++) {
			referencePoint.setObjective(i, maxObjectives[i] + offset);
		}
	}

	public double get2DHV(ParetoSolution<T> front) {
		double hv = 0.0;

		hv = Math.abs((front.get(0).getObjective(0) - referencePoint.getObjective(0)) *
				(front.get(0).getObjective(1) - referencePoint.getObjective(1))) ;

		int v = front.size() ;
		for (int i = 1; i < front.size(); i++) {
			hv += Math.abs((front.get(i).getObjective(0) - referencePoint.getObjective(0)) *
					(front.get(i).getObjective(1) - front.get(i - 1).getObjective(1)));

		}

		return hv;
	}

	public double getInclusiveHV(MOSolutionBase<T> point) {
		double volume = 1;
		for (int i = 0; i < currentDimension; i++) {
			volume *= Math.abs(point.getObjective(i) - referencePoint.getObjective(i));
		}

		return volume;
	}

	public double getExclusiveHV(ParetoSolution<T> front, int point) {
		double volume;

		volume = getInclusiveHV(front.get(point));
		if (front.size() > point + 1) {
			makeDominatedBit(front, point);
			double v = getHV(fs[currentDeep - 1]);
			volume -= v;
			currentDeep--;
		}

		return volume;
	}

	public double getHV(ParetoSolution<T> front) {
		double volume ;
		front.sort(pointComparator);

		if (currentDimension == 2) {
			volume = get2DHV(front);
		} else {
			volume = 0.0;

			currentDimension--;
			int numberOfPoints = front.size() ;
			for (int i = numberOfPoints - 1; i >= 0; i--) {
				volume += Math.abs(front.get(i).getObjective(currentDimension) -
						referencePoint.getObjective(currentDimension)) *
						this.getExclusiveHV(front, i);
			}
			currentDimension++;
		}

		return volume;
	}


	public void makeDominatedBit(ParetoSolution<T> front, int p) {
		int z = front.size() - 1 - p;

		for (int i = 0; i < z; i++) {
			for (int j = 0; j < currentDimension; j++) {
				MOSolutionBase<T> point1 = front.get(p) ;
				MOSolutionBase<T> point2 = front.get(p + 1 + i) ;
				double worseValue = worse(point1.getObjective(j), point2.getObjective(j), false) ;
				int cd = currentDeep ;
				MOSolutionBase<T> point3 = fs[currentDeep].get(i) ;
				point3.setObjective(j, worseValue);
			}
		}

		MOSolutionBase<T> t;
		fs[currentDeep].setCapacity(1);

		for (int i = 1; i < z; i++) {
			int j = 0;
			boolean keep = true;
			while (j < fs[currentDeep].getCapacity() && keep) {
				switch (dominates2way(fs[currentDeep].get(i), fs[currentDeep].get(j))) {
				case -1:
					t = fs[currentDeep].get(j);
					fs[currentDeep].setCapacity(fs[currentDeep].getCapacity()-1);
					fs[currentDeep].set(j, fs[currentDeep].get(fs[currentDeep].getCapacity()));
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
				fs[currentDeep].setCapacity(fs[currentDeep].getCapacity()+1);
			}
		}

		currentDeep++;
	}

	public int getLessContributorHV(ParetoSolution<T> solutionList) {

		int index = 0;
		double contribution = Double.POSITIVE_INFINITY;

		for (int i = 0; i < solutionList.size(); i++) {
			double[] v = new double[solutionList.get(i).numberOfObjectives()];
			for (int j = 0; j < v.length; j++) {
				v[j] = solutionList.get(i).getObjective(j);
			}

			double aux = this.getExclusiveHV(solutionList, i);
			if ((aux) < contribution) {
				index = i;
				contribution = aux;
			}

			//HypervolumeContributionAttribute<Solution<?>> hvc = new HypervolumeContributionAttribute<Solution<?>>() ;
			//hvc.setAttribute(solutionList.get(i), aux);
			//solutionList.get(i).setCrowdingDistance(aux);
		}

		return index;
	}

	private double worse(double x, double y, boolean maximizing) {
		double result;
		if (maximizing) {
			if (x > y) {
				result = y;
			} else {
				result = x;
			}
		} else {
			if (x > y) {
				result = x;
			} else {
				result = y;
			}
		}
		return result;
	}

	int dominates2way(MOSolutionBase<T> p, MOSolutionBase<T> q) {
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
		return IndicatorType.Unary;
	}

	@Override
	public boolean isMin() {
		return false;
	}

	@Override
	public boolean requiresReferenceSet() {
		return true;
	}

	@Override
	public int compare(ParetoSolution<T> front1, ParetoSolution<T> front2, Double epsilon) {
		return 0;
	}
}
