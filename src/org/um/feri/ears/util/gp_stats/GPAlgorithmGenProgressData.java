package org.um.feri.ears.util.gp_stats;

import org.um.feri.ears.problems.gp.ProgramSolution;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GPAlgorithmGenProgressData implements Serializable {
    private int generation;

    private String executionPhaseName;
    private List<GPProgramSolutionSimple> populationSimple;
    private List<ProgramSolution> population;

    public GPAlgorithmGenProgressData(int generation, String executionPhaseName){
        this.generation = generation;
        this.executionPhaseName = executionPhaseName;
        this.populationSimple = new ArrayList<>();
        this.population = new ArrayList<>();
    }

    public int getGeneration(){
        return generation;
    }

    public List<GPProgramSolutionSimple> getPopulationSimple(){
        return populationSimple;
    }

    public void setGeneration(int generation){
        this.generation = generation;
    }

    public void setPopulationSimple(List<GPProgramSolutionSimple> populationSimple){
        this.populationSimple = populationSimple;
    }

    public void setPopulationProgramSolution(GPProgramSolutionSimple programSolutionSimple){
        this.populationSimple.add(programSolutionSimple);
    }

    public String getExecutionPhaseName(){
        return executionPhaseName;
    }

    public void setExecutionPhaseName(String executionPhaseName){
        this.executionPhaseName = executionPhaseName;
    }

    public void addProgramSolutionSimple(GPProgramSolutionSimple programSolutionSimple){
        populationSimple.add(programSolutionSimple);
    }

    public void addProgramSolution(ProgramSolution programSolution){
        population.add(programSolution);
    }
}
