package org.um.feri.ears.util.gp_stats;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GPAlgorithmGenProgressData implements Serializable {
    private int generation;

    private String executionPhaseName;
    private List<GPProgramSolutionSimple> population;

    public GPAlgorithmGenProgressData(int generation, String executionPhaseName){
        this.generation = generation;
        this.executionPhaseName = executionPhaseName;
        this.population = new ArrayList<>();
    }

    public int getGeneration(){
        return generation;
    }

    public List<GPProgramSolutionSimple> getPopulation(){
        return population;
    }

    public void setGeneration(int generation){
        this.generation = generation;
    }

    public void setPopulation(List<GPProgramSolutionSimple> population){
        this.population = population;
    }

    public void setPopulationProgramSolution(GPProgramSolutionSimple programSolutionSimple){
        this.population.add(programSolutionSimple);
    }

    public String getExecutionPhaseName(){
        return executionPhaseName;
    }

    public void setExecutionPhaseName(String executionPhaseName){
        this.executionPhaseName = executionPhaseName;
    }

    public void addProgramSolutionSimple(GPProgramSolutionSimple programSolutionSimple){
        population.add(programSolutionSimple);
    }
}
