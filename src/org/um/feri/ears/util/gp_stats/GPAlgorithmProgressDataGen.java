package org.um.feri.ears.util.gp_stats;

import java.io.Serializable;
import java.util.List;

public class GPAlgorithmProgressDataGen implements Serializable {
    private int generation;

    private List<GPProgramSolutionSimple> population;
}
