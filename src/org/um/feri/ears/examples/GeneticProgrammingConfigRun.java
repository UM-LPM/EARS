package org.um.feri.ears.examples;

import org.um.feri.ears.algorithms.gp.GPAlgorithmExecutor;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.robostrike.*;
import org.um.feri.ears.util.Configuration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class GeneticProgrammingConfigRun {
    public static void main(String[] args) {
        System.out.println("Genetic Programming Configuration Run START");

        if (args.length < 2) {
            System.out.println("Please provide the required arguments:<inputFileType>, <configFile/gpAlgorithmExecutorFile>");
            return;
        }

        InputFileType inputFileType = InputFileType.fromValue(Integer.parseInt(args[0]));
        String configFile = args[1];

        System.out.println("Input file type: " + inputFileType);
        System.out.println("Specified config file: " + configFile);

        GPAlgorithmExecutor gpAlgorithmExecutor = null;

        switch (inputFileType){
            case CONFIG_FILE:
                System.out.println("Loading configuration file...");
                gpAlgorithmExecutor = loadConfigurationFile(configFile);
                break;
            case GP_ALGORITHM_EXECUTOR_FILE:
                System.out.println("Loading GPAlgorithmExecutor file...");
                gpAlgorithmExecutor = loadGPAlgorithmExecutorFile(configFile);
                break;
            default:
                System.out.println("Invalid input file type.");
                return;
        }

        if(gpAlgorithmExecutor != null){
            System.out.println("GPAlgorithmExecutor loaded successfully. Running...");
            gpAlgorithmExecutor.runConfigurations(null, "gpAlgorithmState.ser");
        }

        System.out.println("Genetic Programming Configuration Run END");
    }

    public static void loadConfiguration(GPAlgorithmExecutor gpAlgorithmExecutor, String configFile) {
        try {
            if (!configFile.isEmpty()) {
                gpAlgorithmExecutor.setConfiguration(Configuration.deserializeFromFile(configFile));
            }
            else{
                System.out.println("No configuration file provided.");
                return;
            }
            gpAlgorithmExecutor.setGpAlgorithm(null);
            gpAlgorithmExecutor.loadDefaultConfiguration();

        }
        catch (Exception ex){
            System.out.println("Error loading configuration: " + ex.getMessage());
        }
    }

    public static GPAlgorithmExecutor loadConfigurationFile(String configFile){
        GPAlgorithmExecutor gpAlgorithmExecutor = new GPAlgorithmExecutor(true);
        loadConfiguration(gpAlgorithmExecutor, configFile);

        try{
            gpAlgorithmExecutor.setConfiguration(Configuration.deserializeFromFile(configFile));

            System.out.println("Configuration loaded successfully.");
        }
        catch (Exception e){
            System.out.println("Error while loading configuration file: " + e.getMessage());
            return null;
        }
        return gpAlgorithmExecutor;
    }

    public static GPAlgorithmExecutor loadGPAlgorithmExecutorFile(String gpAlgorithmExecutorFile){
        return GPAlgorithmExecutor.deserializeGPAlgorithmExecutorState(gpAlgorithmExecutorFile);
    }

    public enum InputFileType {
        CONFIG_FILE(1),
        GP_ALGORITHM_EXECUTOR_FILE(2);

        private final int value;

        InputFileType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static InputFileType fromValue(int value) {
            for (InputFileType type : InputFileType.values()) {
                if (type.getValue() == value) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown enum value: " + value);
        }
    }
}
