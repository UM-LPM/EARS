package org.um.feri.ears.util.gp_stats;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GPAlgorithmMultiRunProgressData implements Serializable {
    private List<GPAlgorithmProgressData> progressData;

    public GPAlgorithmMultiRunProgressData() {
        this.progressData = new ArrayList<>();
    }

    public List<GPAlgorithmProgressData> getProgressData() {
        return progressData;
    }

    public void setProgressData(List<GPAlgorithmProgressData> progressData) {
        this.progressData = progressData;
    }

    public void addProgressData(GPAlgorithmProgressData progressData) {
        this.progressData.add(progressData);
    }
}
