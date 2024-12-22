package org.um.feri.ears.util.gp_stats;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GPAlgorithmMultiRunProgressData implements Serializable {
    private List<GPAlgorithmRunProgressData> multiRunProgressData;

    public GPAlgorithmMultiRunProgressData() {
        this.multiRunProgressData = new ArrayList<>();
    }

    public List<GPAlgorithmRunProgressData> getMultiRunProgressData() {
        return multiRunProgressData;
    }

    public void setMultiRunProgressData(List<GPAlgorithmRunProgressData> multiRunProgressData) {
        this.multiRunProgressData = multiRunProgressData;
    }

    public void addMultiRunProgressData(GPAlgorithmRunProgressData progressData) {
        this.multiRunProgressData.add(progressData);
    }

    public void resetMultiRunProgressData() {
        this.multiRunProgressData = new ArrayList<>();
    }
}
