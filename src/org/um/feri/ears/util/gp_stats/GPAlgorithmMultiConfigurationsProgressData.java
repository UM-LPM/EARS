package org.um.feri.ears.util.gp_stats;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.um.feri.ears.algorithms.GPAlgorithm;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
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

    public void saveProgressData(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(multiConfigurationPrograssDataFilePath))) {
            writer.write(objectMapper.writeValueAsString(this));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
