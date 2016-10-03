package org.um.feri.ears.benchmark.research.pso;

import java.util.Comparator;

public class IndividualComparator implements Comparator<PSOFSIndividual> {
	@Override
    public int compare(PSOFSIndividual o1, PSOFSIndividual o2) {
        return  ((Double)o1.getEval()).compareTo((Double)o2.getEval()); //Double.compare(o1.getEval(), o2.getEval());
    }
}