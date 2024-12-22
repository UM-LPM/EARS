package org.um.feri.ears.util.gp_stats;

import org.um.feri.ears.algorithms.GPAlgorithm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GPAlgorithmRunProgressData implements Serializable {
    private List<GPAlgorithmGenProgressData> genProgressData;

    public GPAlgorithmRunProgressData() {
        genProgressData = new ArrayList<>();
    }

    public List<GPAlgorithmGenProgressData> getGensProgressData() {
        return genProgressData;
    }

    public void setGensProgressData(List<GPAlgorithmGenProgressData> genProgressData) {
        this.genProgressData = genProgressData;
    }

    public void addProgressDataGen(int generation, String executionPhaseName, GPAlgorithm progressDataGen) {

        GPAlgorithmGenProgressData gpAlgorithmGenProgressData = new GPAlgorithmGenProgressData(generation, executionPhaseName);

        for(int i = 0; i < progressDataGen.getPopulation().size(); i++){
            gpAlgorithmGenProgressData.addProgramSolutionSimple(
                    new GPProgramSolutionSimple(progressDataGen.getPopulation().get(i)));
        }

        genProgressData.add(gpAlgorithmGenProgressData);
    }
}
