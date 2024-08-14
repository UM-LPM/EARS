package org.um.feri.ears.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Configuration {

    public String EvalEnvInstanceURIs;
    public String JsonBodyDestFilePath;
    public String ImagePath;
    public String UnityGameFile;
    public String UnityExeLocation;
    public String UnityConfigDestFilePath;
    public List<RunConfiguration> Configurations;
    public List<EncapsulatedNodeConfigDefinition> EncapsulatedNodeDefinitions;

    public static Configuration deserialize(String filePath){
        ObjectMapper objectMapper = new ObjectMapper();
        Configuration configuration = null;
        try {
            configuration = objectMapper.readValue(new File(filePath), Configuration.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return configuration;
    }

    public static void serializeUnityConfig(RunConfiguration configuration, String filePath){
        ObjectMapper objectMapper = new ObjectMapper();
        String serializedJson = "";
        try {
            serializedJson = objectMapper.writeValueAsString(configuration.UnityConfiguration);
            // Save json to file
            Files.write(Paths.get(filePath), serializedJson.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    enum ProblemType {
        SymbolicRegressionProblem,
        UnityBTProblem
    }

    enum CrossoverOperatorType {
        OnePointCrossover,
        TwoPointCrossover,
        NpointCrossover,
    }

    public enum MutationOperatorType {
        SubtreeMutation,
        SingleNodeMutation,
        HoistMutation,
    }

    public enum SelectionOperatorType {
        TournamentSelection,
        RouletteWheelSelection,
        RankSelection,
    }

    public enum InitPopGeneratorMethod {
        Random,
        RampedHalfAndHalfMethod,
        FullMethod, // TODO ???
        GrowMethod, // TODO ???
    }

    public enum BloatControlMethod {
        DepthLimit,
        SizeLimit,
        DepthAndSizeLimit,
        None
    }
}
