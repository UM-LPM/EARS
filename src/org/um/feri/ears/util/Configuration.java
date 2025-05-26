package org.um.feri.ears.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class Configuration implements Serializable{

    public String CoordinatorURI;
    public String EvalEnvInstanceURIs;
    public String DestinationFilePath;
    public String JsonBodyDestFilePath;
    public String ImagePath;
    public String UnityGameFile;
    public String UnityExeLocation;
    public String UnityConfigDestFilePath;
    public String MultiConfigurationPrograssDataFilePath;
    public boolean ExecuteFinalMasterTournaments;
    public long InitialSeed;
    public boolean SetInitialSeedForEachConfigurationRun;
    public List<RunConfiguration> Configurations;
    public RunConfigurationFinal FinalMasterTournamentsConfiguration;

    public static Configuration deserializeFromFile(String filePath){
        ObjectMapper objectMapper = new ObjectMapper();
        Configuration configuration = null;
        try {
            configuration = objectMapper.readValue(new File(filePath), Configuration.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return configuration;
    }

    public static Configuration deserializeFromJson(String json){
        ObjectMapper objectMapper = new ObjectMapper();
        Configuration configuration = null;
        try {
            configuration = objectMapper.readValue(json, Configuration.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return configuration;
    }

    public static void serializeUnityConfig(Object unityConfiguration, String filePath){
        ObjectMapper objectMapper = new ObjectMapper();
        String serializedJson = "";
        try {
            serializedJson = objectMapper.writeValueAsString(unityConfiguration);
            // Save json to file (if file does not exist, it will be created)
            Files.writeString(Paths.get(filePath), serializedJson, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
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
