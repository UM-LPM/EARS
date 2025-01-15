package org.um.feri.ears.util.gp_stats;

import org.um.feri.ears.problems.gp.ProgramSolution;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GPAlgorithmMultiRunProgressData implements Serializable {
    protected List<GPAlgorithmRunProgressData> multiRunProgressData;

    protected List<GPProgramSolutionSimple> masterMasterTournamentGraphData;

    public GPAlgorithmMultiRunProgressData() {
        this.multiRunProgressData = new ArrayList<>();
    }

    public List<GPAlgorithmRunProgressData> getMultiRunProgressData() {
        return multiRunProgressData;
    }

    public void setMultiRunProgressData(List<GPAlgorithmRunProgressData> multiRunProgressData) {
        this.multiRunProgressData = multiRunProgressData;
    }

    public List<GPProgramSolutionSimple> getMasterMasterTournamentGraphData() {
        return masterMasterTournamentGraphData;
    }

    public void setMasterMasterTournamentGraphData(List<ProgramSolution> masterMasterTournamentSolutions){
        this.masterMasterTournamentGraphData = new ArrayList<>();
        for (ProgramSolution solution : masterMasterTournamentSolutions) {
            this.masterMasterTournamentGraphData.add(new GPProgramSolutionSimple(solution, true));
        }
    }

    public void addMultiRunProgressData(GPAlgorithmRunProgressData progressData) {
        this.multiRunProgressData.add(progressData);
    }

    public void resetMultiRunProgressData() {
        this.multiRunProgressData = new ArrayList<>();
    }
}
