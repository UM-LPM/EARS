package org.um.feri.ears.util.gp_stats;

import org.um.feri.ears.algorithms.GPAlgorithm;
import org.um.feri.ears.problems.gp.ProgramSolution;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GPAlgorithmRunProgressData implements Serializable {
    private List<GPAlgorithmGenProgressData> genProgressData;
    private List<GPAlgorithmGenProgressData> xGenBestProgressData;

    private List<GPProgramSolutionSimple> masterTournamentGraphData;
    private GPProgramSolutionSimple convergenceGraphData;

    public GPAlgorithmRunProgressData() {
        genProgressData = new ArrayList<>();
        xGenBestProgressData = new ArrayList<>();
    }

    public List<GPAlgorithmGenProgressData> getGensProgressData() {
        return genProgressData;
    }

    public void setGensProgressData(List<GPAlgorithmGenProgressData> genProgressData) {
        this.genProgressData = genProgressData;
    }

    public List<GPAlgorithmGenProgressData> getXGenBestProgressData() {
        return xGenBestProgressData;
    }

    public void setXGensProgressData(List<GPAlgorithmGenProgressData> xGenBestProgressData) {
        this.xGenBestProgressData = xGenBestProgressData;
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

        // TODO Verify if this can be removed !!
        /*for(int i = 0; i < progressDataGen.getPopulation().size(); i++){
            gpAlgorithmGenProgressData.addProgramSolutionSimple(
                    new GPProgramSolutionSimple(progressDataGen.getPopulation().get(i), false));
        }*/

        // Only add the best solution
        gpAlgorithmGenProgressData.addProgramSolutionSimple(
                new GPProgramSolutionSimple(progressDataGen.getBest(), false));

        //gpAlgorithmGenProgressData.addProgramSolution(progressDataGen.getBest());

        genProgressData.add(gpAlgorithmGenProgressData);

        if(generation % 5 == 0)
            addXGenBestProgressData(generation, executionPhaseName, progressDataGen);
    }

    public void addXGenBestProgressData(int generation, String executionPhaseName, GPAlgorithm progressDataGen) {
        GPAlgorithmGenProgressData gpAlgorithmXGenBestProgressData = new GPAlgorithmGenProgressData(generation, executionPhaseName);

        gpAlgorithmXGenBestProgressData.addProgramSolutionSimple(
                new GPProgramSolutionSimple(progressDataGen.getBest(), false));

        gpAlgorithmXGenBestProgressData.addProgramSolution(progressDataGen.getBest());

        xGenBestProgressData.add(gpAlgorithmXGenBestProgressData);
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
