package org.um.feri.ears.util.gp_stats;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.um.feri.ears.algorithms.GPAlgorithm;
import org.um.feri.ears.problems.gp.ProgramSolution;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GPAlgorithmMultiConfigurationsProgressData implements Serializable {
    private String multiConfigurationPrograssDataFilePath;
    private List<GPAlgorithmMultiRunProgressData> multiConfigurationProgressData;

    public GPAlgorithmMultiConfigurationsProgressData(String multiConfigurationPrograssDataFilePath) {
        this.multiConfigurationProgressData = new ArrayList<>();
        this.multiConfigurationPrograssDataFilePath = multiConfigurationPrograssDataFilePath;
    }

    public List<GPAlgorithmMultiRunProgressData> getMultiConfigurationProgressData() {
        return multiConfigurationProgressData;
    }

    public void setMultiConfigurationProgressData(List<GPAlgorithmMultiRunProgressData> multiConfigurationProgressData) {
        this.multiConfigurationProgressData = multiConfigurationProgressData;
    }

    public void addMultiConfigurationProgressData(GPAlgorithmMultiRunProgressData progressData) {
        this.multiConfigurationProgressData.add(progressData);
    }

    public String getMultiConfigurationPrograssDataFilePath() {
        return multiConfigurationPrograssDataFilePath;
    }

    public void setMultiConfigurationPrograssDataFilePath(String multiConfigurationPrograssDataFilePath) {
        this.multiConfigurationPrograssDataFilePath = multiConfigurationPrograssDataFilePath;
    }

    public void resetMultiConfigurationProgressData() {
        this.multiConfigurationProgressData = new ArrayList<>();
    }

    public void addGenProgressData(int generation, String executionPhaseName, GPAlgorithm gpAlgorithm) {
        if(executionPhaseName == null)
            executionPhaseName = "Main_phase";

        multiConfigurationProgressData
                .get(multiConfigurationProgressData.size() -1)
                .getMultiRunProgressData().get(multiConfigurationProgressData
                        .get(multiConfigurationProgressData.size() -1).getMultiRunProgressData().size() -1)
                .addProgressDataGen(generation, executionPhaseName, gpAlgorithm);
    }

    public void addMasterTournamentGraphData(List<ProgramSolution> solutions) {
        multiConfigurationProgressData
                .get(multiConfigurationProgressData.size() -1)
                .getMultiRunProgressData().get(multiConfigurationProgressData
                        .get(multiConfigurationProgressData.size() -1).getMultiRunProgressData().size() -1)
                .addMasterTournamentGraphData(solutions);
    }

    public void addConvergenceGraphData(ProgramSolution solution) {
        multiConfigurationProgressData
                .get(multiConfigurationProgressData.size() -1)
                .getMultiRunProgressData().get(multiConfigurationProgressData
                        .get(multiConfigurationProgressData.size() -1).getMultiRunProgressData().size() -1)
                .addConvergenceGraphData(solution);
    }

    public void saveProgressData(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(multiConfigurationPrograssDataFilePath.replace(".ser", ".json")))) {
            writer.write(objectMapper.writeValueAsString(this));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void serializeState(GPAlgorithmMultiConfigurationsProgressData progressData) {
        System.out.println("Serializing current task and population state");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(progressData.multiConfigurationPrograssDataFilePath))) {
            oos.writeObject(progressData);
        } catch (IOException e) {
            e.printStackTrace();
        }

        GPAlgorithm.CAN_RUN = true;
    }

    public static GPAlgorithmMultiConfigurationsProgressData deserializeState(String filename){
        GPAlgorithmMultiConfigurationsProgressData progressData = new GPAlgorithmMultiConfigurationsProgressData(filename);
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(progressData.multiConfigurationPrograssDataFilePath))) {
            progressData = (GPAlgorithmMultiConfigurationsProgressData) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return progressData;
    }
}
