package org.um.feri.ears.util.gp_stats;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GPAlgorithmProgressData implements Serializable {
    private List<GPAlgorithmProgressDataGen> progressDataGens;

    public GPAlgorithmProgressData() {
        progressDataGens = new ArrayList<>();
    }

    public List<GPAlgorithmProgressDataGen> getProgressDataGens() {
        return progressDataGens;
    }

    public void setProgressDataGens(List<GPAlgorithmProgressDataGen> progressDataGens) {
        this.progressDataGens = progressDataGens;
    }

    public void addProgressDataGen(GPAlgorithmProgressDataGen progressDataGen) {
        progressDataGens.add(progressDataGen);
    }
}
