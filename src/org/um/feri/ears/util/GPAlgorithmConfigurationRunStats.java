package org.um.feri.ears.util;

import org.um.feri.ears.problems.gp.ProgramSolution;
import org.um.feri.ears.util.gp_stats.GPProgramSolutionSimple;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GPAlgorithmConfigurationRunStats implements Serializable {
    protected ArrayList<GPAlgorithmRunStats> gpAlgorithmRunStats;

    protected List<GPProgramSolutionSimple> masterTournamentGraphData;

    public GPAlgorithmConfigurationRunStats() {
        this.gpAlgorithmRunStats = new ArrayList<>();
    }

    public ArrayList<GPAlgorithmRunStats> getGpAlgorithmRunStats() {
        return gpAlgorithmRunStats;
    }

    public void setGpAlgorithmRunStats(ArrayList<GPAlgorithmRunStats> gpAlgorithmRunStats) {
        this.gpAlgorithmRunStats = gpAlgorithmRunStats;
    }

    public void addGpAlgorithmRunStats(GPAlgorithmRunStats gpAlgorithmRunStats) {
        this.gpAlgorithmRunStats.add(gpAlgorithmRunStats);
    }

    public ProgramSolution getBestRunSolution() {
        return this.gpAlgorithmRunStats.get(this.gpAlgorithmRunStats.size() - 1).getBestRunSolution();
    }

    public List<GPProgramSolutionSimple> getMasterTournamentGraphData() {
        return masterTournamentGraphData;
    }

    public void setMasterTournamentGraphData(List<GPProgramSolutionSimple> masterTournamentGraphData) {
        this.masterTournamentGraphData = masterTournamentGraphData;
    }
}
