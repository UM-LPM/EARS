package org.um.feri.ears.util.gp_stats;

import org.um.feri.ears.algorithms.GPAlgorithm;
import org.um.feri.ears.problems.gp.ProgramSolution;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GPAlgorithmRunProgressData implements Serializable {
    private List<GPAlgorithmGenProgressData> genProgressData;

    private List<GPProgramSolutionSimple> masterTournamentGraphData;
    private GPProgramSolutionSimple convergenceGraphData;

    public GPAlgorithmRunProgressData() {
        genProgressData = new ArrayList<>();
    }

    public List<GPAlgorithmGenProgressData> getGensProgressData() {
        return genProgressData;
    }

    public void setGensProgressData(List<GPAlgorithmGenProgressData> genProgressData) {
        this.genProgressData = genProgressData;
    }

    public List<GPProgramSolutionSimple> getMasterTournamentGraphData() {
        return masterTournamentGraphData;
    }

    public void setMasterTournamentGraphData(List<GPProgramSolutionSimple> masterTournamentGraphData) {
        this.masterTournamentGraphData = masterTournamentGraphData;
    }

    public GPProgramSolutionSimple getConvergenceGraphData() {
        return convergenceGraphData;
    }

    public void setConvergenceGraphData(GPProgramSolutionSimple convergenceGraphData) {
        this.convergenceGraphData = convergenceGraphData;
    }

    public void addProgressDataGen(int generation, String executionPhaseName, GPAlgorithm progressDataGen) {

        GPAlgorithmGenProgressData gpAlgorithmGenProgressData = new GPAlgorithmGenProgressData(generation, executionPhaseName);

        for(int i = 0; i < progressDataGen.getPopulation().size(); i++){
            gpAlgorithmGenProgressData.addProgramSolutionSimple(
                    new GPProgramSolutionSimple(progressDataGen.getPopulation().get(i), false));
        }

        genProgressData.add(gpAlgorithmGenProgressData);
    }

    public void addMasterTournamentGraphData(List<ProgramSolution> solutions) {
        if(masterTournamentGraphData == null) {
            masterTournamentGraphData = new ArrayList<>();
        }

        for(int i = 0; i < solutions.size(); i++){
            masterTournamentGraphData.add(
                    new GPProgramSolutionSimple(solutions.get(i), false));
        }
    }

    public void addConvergenceGraphData(ProgramSolution solution) {
        // solution contains matches result with all best gen individuals
        convergenceGraphData = new GPProgramSolutionSimple(solution, true);
    }
}
