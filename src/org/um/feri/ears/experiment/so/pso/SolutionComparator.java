package org.um.feri.ears.experiment.so.pso;

import java.util.Comparator;

public class SolutionComparator implements Comparator<PSOFSSolution> {
	@Override
    public int compare(PSOFSSolution o1, PSOFSSolution o2) {
        return  ((Double)o1.getEval()).compareTo((Double)o2.getEval()); //Double.compare(o1.getEval(), o2.getEval());
    }
}