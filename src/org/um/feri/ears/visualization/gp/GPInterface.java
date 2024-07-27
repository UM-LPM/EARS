package org.um.feri.ears.visualization.gp;

import org.um.feri.ears.algorithms.GPAlgorithm;
import org.um.feri.ears.algorithms.gp.DefaultGPAlgorithm;
import org.um.feri.ears.algorithms.gp.ElitismGPAlgorithm;
import org.um.feri.ears.individual.generations.gp.GPRampedHalfAndHalf;
import org.um.feri.ears.individual.generations.gp.GPRandomProgramSolution;
import org.um.feri.ears.individual.representations.gp.Node;
import org.um.feri.ears.individual.representations.gp.Target;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.Inverter;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.Selector;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.Sequencer;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.robostrike.*;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.movement.MoveForward;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.sensors.RayHitObject;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.movement.Rotate;
import org.um.feri.ears.individual.representations.gp.symbolic.regression.*;
import org.um.feri.ears.operators.gp.*;
import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.gp.*;
import org.um.feri.ears.util.*;
import org.um.feri.ears.visualization.gp.components.GraphPanel;
import org.um.feri.ears.visualization.gp.components.HorizontalHistogramPanel;
import org.um.feri.ears.visualization.gp.components.ImagePanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GPInterface extends JFrame {

    private JPanel contentPane;
    private JLabel genNumLabel;
    private JLabel fitnesEvalNumLabel;
    private JButton nextAlgGenerationButton;
    private JButton resetAlgButton;
    private JButton nextAlgStepButton;
    private JPanel algorithmPopulationPanel;
    private JPanel currentStatusPanel;
    private JButton runAlgButton;

    private ProgramProblem programProblem;
    private Task<ProgramSolution, ProgramProblem> task;
    private GPAlgorithm gpAlgorithm;
    private Thread algorithmThread;
    String imgPathPrefix;

    // Selected individual parameters
    private ImagePanel selectedIndividualImagePanel;
    private JLabel selectedIndividualTreeDepthLabel;
    private JLabel selectedIndividualFitnes;
    private JLabel selectedIndividualTreeSize;
    private JLabel selectedIndividualIsFeasible;
    private JLabel selectedIndividualNumOfFunc;
    private JLabel selectedIndividualNumOfTerm;
    private JLabel selectedIndividualID;
    private JTextField runXAlgGensTextField;

    private JLabel bestIndividualID;
    private JLabel bestIndividualFitnes;
    private JLabel bestIndividualTreeDepth;
    private JLabel bestIndividualTreeSize;
    private JLabel bestIndividualIsFeasible;
    private JLabel bestIndividualNumOfFunc;
    private JLabel bestIndividualNumOfTerm;
    private ImagePanel bestIndividualImagePanel;
    private GraphPanel overalBestFitnessGraphPanel;
    private GraphPanel avgFitnessConvergenceGraph;
    private GraphPanel avgTreeDepthGraphPanel;
    private GraphPanel avgTreeSizeGraphPanel;
    private GraphPanel bestGenerationFitnessGraphPanel;
    private JButton saveCurrentAlgState;
    private JButton loadCurrentAlgState;
    private JTabbedPane tabbPane;
    private HorizontalHistogramPanel bestIndividualHorizontalHistogramPanel;
    private HorizontalHistogramPanel selectedIndividualHorizontalHistogramPanel;
    private JTextField saveGPAlgorithmStatsFilename;

    // GP Algorithm parameters
    private JTextField populationSizeTextField;
    private JTextField maxTreeSizeTextField;
    private JTextField maxTreeEndDepthTextField;
    private JTextField minTreeDepthTextField;
    private JTextField mutationProbTextField;
    private JTextField crossoverProbTextField;
    private JTextField numOfTurnamentsTextField;
    private JTextField bestIndividualJsonTextField;
    private JTextField problemNameTextField;
    private JTextField elitismProbTextField;
    private JTextField evalEnvInstanceURIsTextField;
    private JTextField imagePathTextField;
    private JTextField jsonBodyDestFolderPathTextField;
    private JTextField configurationFileTextField;
    private JTextField unityConfigDestFileTextField;
    private JTextField maxTreeStartDepthTextField;
    private JComboBox individualGenMethodComboBox;
    private JTextField seqSelNumOfChildrenTextField;
    private JTextField functionsTextField;
    private JTextField terminalsTextField;

    private String lastUuid;

    public static GPInterface Instance;

    public Configuration configuration;

    public GPInterface() {
        super("EARS GP (Debug)");
        Instance = this;

        setContentPane(tabbPane);

        imgPathPrefix = "src/org/um/feri/ears/visualization/gp/images/";

        clearImages();

        // Initialize data for gp algorithm
        //initializeDataSymbolicRegression(null);
        initializeGpAlgorithmStateBehaviourTree(null);

        updateGPAlgorithmParamsUI();
        updateUI(false);

        saveGPAlgorithmStatsFilename.setText(this.programProblem.getName() + "_" + this.gpAlgorithm.getPopSize() + "_" + this.gpAlgorithm.getCrossoverProbability() + "_" + this.gpAlgorithm.getElitismProbability() + "_" + this.gpAlgorithm.getMutationProbability() + "_" + this.gpAlgorithm.getNumberOfTournaments() + "_" + this.programProblem.getMinTreeDepth() + "_" + this.programProblem.getMaxTreeStartDepth() + "_" + this.programProblem.getMaxTreeEndDepth() + "_" + this.programProblem.getMaxTreeSize() + "_gpAlgorithmState_" + getFormattedDate() + ".ser" );

        addButtonListeners();
        addGPAlgorithmParamsListeners();
        addGeneralListeners();

        evalEnvInstanceURIsTextField.setText("http://localhost:4444/");

        setUIDefaults();

    }

    private void setUIDefaults(){
        // fill IndividualGenMethodComboBox with Individual generation methods from Configuration.InitPopGeneratorMethod
        for (Configuration.InitPopGeneratorMethod method : Configuration.InitPopGeneratorMethod.values()) {
            individualGenMethodComboBox.addItem(method.toString());
        }
    }

    private void addButtonListeners(){
        resetAlgButton.addActionListener(e -> {
            algorithmThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    // Reset algorithm
                    gpAlgorithm.resetToDefaultsBeforeNewRun();
                    updateUI(true);
                    individualImagePanelMouseClicked(null, null, -1);
                }
            });
            algorithmThread.start();
        });
        nextAlgStepButton.addActionListener(e -> {
            // Run one step
            try {
                gpAlgorithm.executeStep();
                updateUI(true);
            } catch (StopCriterionException ex) {
                throw new RuntimeException(ex);
            }
        });
        nextAlgGenerationButton.addActionListener(e -> {
            // Run one generation
            try {
                gpAlgorithm.executeGeneration();
                updateUI(true);
            } catch (StopCriterionException ex) {
                throw new RuntimeException(ex);
            }
        });
        runAlgButton.addActionListener(e -> {
            if(configurationFileTextField.getText().length() > 0 || configuration != null){
                runConfigurations();
            }
            else {
                try {
                    int numOfGens = Integer.parseInt(runXAlgGensTextField.getText());
                    runGPAlgorithm(numOfGens);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid number of generations to run (-1 for run to the end).", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        saveCurrentAlgState.addActionListener(e -> {
            if(saveGPAlgorithmStatsFilename.getText().length() == 0){
                JOptionPane.showMessageDialog(null, "Please enter a valid filename.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            else{
                // Serialize algorithm state
                GPAlgorithm.serializeAlgorithmState(gpAlgorithm, saveGPAlgorithmStatsFilename.getText());
            }
            //GPAlgorithm.serializeAlgorithmState(gpAlgorithm, "gpAlgorithmState.ser");
        });

        loadCurrentAlgState.addActionListener(e -> {
            try{

                String gpDataFile = selectGPDataFile();

                //initializeDataBehaviourTree("gpAlgorithmState.ser");
                initializeGpAlgorithmStateBehaviourTree(gpDataFile);
                saveGPAlgorithmStatsFilename.setText(programProblem.getName() + "_" + gpAlgorithm.getPopSize() + "_" + gpAlgorithm.getCrossoverProbability() + "_" + gpAlgorithm.getElitismProbability() + "_" + gpAlgorithm.getMutationProbability() + "_" + gpAlgorithm.getNumberOfTournaments() + "_" + programProblem.getMinTreeDepth() + "_" + programProblem.getMaxTreeStartDepth() + "_" + programProblem.getMaxTreeEndDepth() + "_" + programProblem.getMaxTreeSize() + "_gpAlgorithmState_" + getFormattedDate() + ".ser" );
            }
            catch (Exception ex){
                initializeGpAlgorithmStateBehaviourTree(null);
            }

            updateGPAlgorithmParamsUI();
            updateUI(true);
        });
    }

    private void addGPAlgorithmParamsListeners(){
        populationSizeTextField.addActionListener(e -> {
            try {
                int populationSize = Integer.parseInt(populationSizeTextField.getText());
                gpAlgorithm.setPopSize(populationSize);
                saveGPAlgorithmStatsFilename.setText(programProblem.getName() + "_" + gpAlgorithm.getPopSize() + "_" + gpAlgorithm.getCrossoverProbability() + "_" + gpAlgorithm.getElitismProbability() + "_" + gpAlgorithm.getMutationProbability() + "_" + gpAlgorithm.getNumberOfTournaments() + "_" + programProblem.getMinTreeDepth() + "_" + programProblem.getMaxTreeStartDepth() + "_" + programProblem.getMaxTreeEndDepth() + "_" + programProblem.getMaxTreeSize() + "_gpAlgorithmState_" + getFormattedDate() + ".ser" );
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number for population size.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        crossoverProbTextField.addActionListener(e -> {
            try {
                double crossoverProb = Double.parseDouble(crossoverProbTextField.getText());
                gpAlgorithm.setCrossoverProbability(crossoverProb);
                saveGPAlgorithmStatsFilename.setText(programProblem.getName() + "_" + gpAlgorithm.getPopSize() + "_" + gpAlgorithm.getCrossoverProbability() + "_" + gpAlgorithm.getElitismProbability() + "_" + gpAlgorithm.getMutationProbability() + "_" + gpAlgorithm.getNumberOfTournaments() + "_" + programProblem.getMinTreeDepth() + "_" + programProblem.getMaxTreeStartDepth() + "_" + programProblem.getMaxTreeEndDepth() + "_" + programProblem.getMaxTreeSize() + "_gpAlgorithmState_" + getFormattedDate() + ".ser" );
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number for crossover probability.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        mutationProbTextField.addActionListener(e -> {
            try {
                double mutationProb = Double.parseDouble(mutationProbTextField.getText());
                gpAlgorithm.setMutationProbability(mutationProb);
                saveGPAlgorithmStatsFilename.setText(programProblem.getName() + "_" + gpAlgorithm.getPopSize() + "_" + gpAlgorithm.getCrossoverProbability() + "_" + gpAlgorithm.getElitismProbability() + "_" + gpAlgorithm.getMutationProbability() + "_" + gpAlgorithm.getNumberOfTournaments() + "_" + programProblem.getMinTreeDepth() + "_" + programProblem.getMaxTreeStartDepth() + "_" + programProblem.getMaxTreeEndDepth() + "_" + programProblem.getMaxTreeSize() + "_gpAlgorithmState_" + getFormattedDate() + ".ser" );
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number for mutation probability.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        numOfTurnamentsTextField.addActionListener(e -> {
            try {
                int numOfTurnaments = Integer.parseInt(numOfTurnamentsTextField.getText());
                gpAlgorithm.setNumberOfTournaments(numOfTurnaments);
                saveGPAlgorithmStatsFilename.setText(programProblem.getName() + "_" + gpAlgorithm.getPopSize() + "_" + gpAlgorithm.getCrossoverProbability() + "_" + gpAlgorithm.getElitismProbability() + "_" + gpAlgorithm.getMutationProbability() + "_" + gpAlgorithm.getNumberOfTournaments() + "_" + programProblem.getMinTreeDepth() + "_" + programProblem.getMaxTreeStartDepth() + "_" + programProblem.getMaxTreeEndDepth() + "_" + programProblem.getMaxTreeSize() + "_gpAlgorithmState_" + getFormattedDate() + ".ser" );
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number for number of tournaments.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        minTreeDepthTextField.addActionListener(e -> {
            try {
                int minTreeDepth = Integer.parseInt(minTreeDepthTextField.getText());
                programProblem.setMinTreeDepth(minTreeDepth);
                saveGPAlgorithmStatsFilename.setText(programProblem.getName() + "_" + gpAlgorithm.getPopSize() + "_" + gpAlgorithm.getCrossoverProbability() + "_" + gpAlgorithm.getElitismProbability() + "_" + gpAlgorithm.getMutationProbability() + "_" + gpAlgorithm.getNumberOfTournaments() + "_" + programProblem.getMinTreeDepth() + "_" + programProblem.getMaxTreeStartDepth() + "_" + programProblem.getMaxTreeEndDepth() + "_" + programProblem.getMaxTreeSize() + "_gpAlgorithmState_" + getFormattedDate() + ".ser" );
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number for minimum tree depth.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        maxTreeStartDepthTextField.addActionListener(e -> {
            try {
                int maxTreeStartDepth = Integer.parseInt(maxTreeStartDepthTextField.getText());
                programProblem.setMaxTreeStartDepth(maxTreeStartDepth);
                saveGPAlgorithmStatsFilename.setText(programProblem.getName() + "_" + gpAlgorithm.getPopSize() + "_" + gpAlgorithm.getCrossoverProbability() + "_" + gpAlgorithm.getElitismProbability() + "_" + gpAlgorithm.getMutationProbability() + "_" + gpAlgorithm.getNumberOfTournaments() + "_" + programProblem.getMinTreeDepth() + "_" + programProblem.getMaxTreeStartDepth() + "_" + programProblem.getMaxTreeEndDepth() + "_" + programProblem.getMaxTreeSize() + "_gpAlgorithmState_" + getFormattedDate() + ".ser" );
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number for maximum tree depth.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        maxTreeEndDepthTextField.addActionListener(e -> {
            try {
                int maxTreeEndDepth = Integer.parseInt(maxTreeEndDepthTextField.getText());
                programProblem.setMaxTreeEndDepth(maxTreeEndDepth);
                saveGPAlgorithmStatsFilename.setText(programProblem.getName() + "_" + gpAlgorithm.getPopSize() + "_" + gpAlgorithm.getCrossoverProbability() + "_" + gpAlgorithm.getElitismProbability() + "_" + gpAlgorithm.getMutationProbability() + "_" + gpAlgorithm.getNumberOfTournaments() + "_" + programProblem.getMinTreeDepth() + "_" + programProblem.getMaxTreeStartDepth() + "_" + programProblem.getMaxTreeEndDepth() + "_" + programProblem.getMaxTreeSize() + "_gpAlgorithmState_" + getFormattedDate() + ".ser" );
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number for maximum tree depth.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        maxTreeSizeTextField.addActionListener(e -> {
            try {
                int maxTreeSize = Integer.parseInt(maxTreeSizeTextField.getText());
                programProblem.setMaxTreeSize(maxTreeSize);
                saveGPAlgorithmStatsFilename.setText(programProblem.getName() + "_" + gpAlgorithm.getPopSize() + "_" + gpAlgorithm.getCrossoverProbability() + "_" + gpAlgorithm.getElitismProbability() + "_" + gpAlgorithm.getMutationProbability() + "_" + gpAlgorithm.getNumberOfTournaments() + "_" + programProblem.getMinTreeDepth() + "_" + programProblem.getMaxTreeStartDepth() + "_" + programProblem.getMaxTreeEndDepth() + "_" + programProblem.getMaxTreeSize() + "_gpAlgorithmState_" + getFormattedDate() + ".ser" );
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number for maximum tree size.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        problemNameTextField.addActionListener(e -> {
            programProblem.setName(problemNameTextField.getText());
            saveGPAlgorithmStatsFilename.setText(programProblem.getName() + "_" + gpAlgorithm.getPopSize() + "_" + gpAlgorithm.getCrossoverProbability() + "_" + gpAlgorithm.getElitismProbability() + "_" + gpAlgorithm.getMutationProbability() + "_" + gpAlgorithm.getNumberOfTournaments() + "_" + programProblem.getMinTreeDepth() + "_" + programProblem.getMaxTreeStartDepth() + "_" + programProblem.getMaxTreeEndDepth() + "_" + programProblem.getMaxTreeSize() + "_gpAlgorithmState_" + getFormattedDate() + ".ser" );
        });

        elitismProbTextField.addActionListener(e -> {
            try {
                double elitismProb = Double.parseDouble(elitismProbTextField.getText());
                ((ElitismGPAlgorithm)gpAlgorithm).setElitismProbability(elitismProb);
                saveGPAlgorithmStatsFilename.setText(programProblem.getName() + "_" + gpAlgorithm.getPopSize() + "_" + gpAlgorithm.getCrossoverProbability() + "_" + gpAlgorithm.getElitismProbability() + "_" + gpAlgorithm.getMutationProbability() + "_" + gpAlgorithm.getNumberOfTournaments() + "_" + programProblem.getMinTreeDepth() + "_" + programProblem.getMaxTreeStartDepth() + "_" + programProblem.getMaxTreeEndDepth() + "_" + programProblem.getMaxTreeSize() + "_gpAlgorithmState_" + getFormattedDate() + ".ser" );
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number for elitism probability.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        evalEnvInstanceURIsTextField.addActionListener(e -> {
            if(evalEnvInstanceURIsTextField.getText().length() > 0){
                programProblem.setEvalEnvInstanceURIs(evalEnvInstanceURIsTextField.getText().split(","));
            }
            else{
                programProblem.setEvalEnvInstanceURIs(new String[]{});
            }
        });

        imagePathTextField.addActionListener(e -> {
            if(imagePathTextField.getText().length() > 0){
                imgPathPrefix = imagePathTextField.getText();
            }
            else{
                imgPathPrefix = "src/org/um/feri/ears/visualization/gp/images/";
            }
        });

        jsonBodyDestFolderPathTextField.addActionListener(e -> {
            if(jsonBodyDestFolderPathTextField.getText().length() > 0){
                programProblem.setJsonBodyDestFolderPath(jsonBodyDestFolderPathTextField.getText());
            }
            else{
                programProblem.setJsonBodyDestFolderPath("");
            }
        });

        individualGenMethodComboBox.addActionListener(e -> {
            String selectedMethod = (String) individualGenMethodComboBox.getSelectedItem();
            if(selectedMethod.equals(Configuration.InitPopGeneratorMethod.Random.toString())){
                programProblem.setProgramSolutionGenerator(new GPRandomProgramSolution());
            }
            else if(selectedMethod.equals(Configuration.InitPopGeneratorMethod.RampedHalfAndHalfMethod.toString())){
                programProblem.setProgramSolutionGenerator(new GPRampedHalfAndHalf());
            }
            else{
                throw new RuntimeException("Unknown method for generating individuals");
            }
        });

        seqSelNumOfChildrenTextField.addActionListener(e -> {
            try {
                int numOfChildren = Integer.parseInt(seqSelNumOfChildrenTextField.getText());
                Selector.MAX_CHILDREN = numOfChildren;
                Sequencer.MAX_CHILDREN = numOfChildren;
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number for number of children.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        configurationFileTextField.addActionListener(e -> {
            if(configurationFileTextField.getText().length() > 0){
                try{
                    String configFile = configurationFileTextField.getText();
                    configuration = Configuration.deserialize(configFile);
                    JOptionPane.showMessageDialog(null, "Configuration file loaded.", "Operation successful", JOptionPane.INFORMATION_MESSAGE);

                    // Update UI with configuration values (load fist configuration)
                    loadDefaultConfiguration();
                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(null, "Error loading configuration file.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        functionsTextField.addActionListener(e -> {
            if(functionsTextField.getText().length() > 0){
                programProblem.setBaseFunctionNodeTypesFromStringList(Arrays.asList(functionsTextField.getText().split(",")));
            }
            else{
                programProblem.setBaseFunctionNodeTypesFromStringList(List.of());
            }
        });

        terminalsTextField.addActionListener(e -> {
            if(terminalsTextField.getText().length() > 0){
                programProblem.setBaseTerminalNodeTypesFromStringList(Arrays.asList(terminalsTextField.getText().split(",")));
            }
            else{
                programProblem.setBaseTerminalNodeTypesFromStringList(List.of());
            }
        });
    }

    private void addGeneralListeners(){

    }

    String selectGPDataFile(){
        Frame frame = new Frame("File Dialog Example");

        // Create a FileDialog with frame as its parent component
        FileDialog fileDialog = new FileDialog(frame, "Select File");

        // Set the mode of the file dialog to load file
        fileDialog.setMode(FileDialog.LOAD);

        // Show the file dialog
        fileDialog.setVisible(true);

        // Get the selected file and directory
        String directory = fileDialog.getDirectory();
        String file = fileDialog.getFile();

        // Dispose the frame
        frame.dispose();

        return directory + file;
    }

    private void createUIComponents() {
        selectedIndividualImagePanel = new ImagePanel(null);
        selectedIndividualImagePanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    // Double clicked
                    try {
                        Desktop.getDesktop().open(Paths.get(imgPathPrefix + "tree" + selectedIndividualID.getText() + ".png").toFile());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        bestIndividualImagePanel = new ImagePanel(null);
        // Add double click on bestIndividualImagePanel and open image
        bestIndividualImagePanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    // Double clicked
                    try {
                        Desktop.getDesktop().open(Paths.get(imgPathPrefix + "tree" + lastUuid + ".png").toFile());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        // Initialize graphs
        this.overalBestFitnessGraphPanel = new GraphPanel(null);
        this.avgFitnessConvergenceGraph = new GraphPanel(null);
        this.avgTreeDepthGraphPanel = new GraphPanel(null);
        this.avgTreeSizeGraphPanel = new GraphPanel(null);
        this.bestGenerationFitnessGraphPanel = new GraphPanel(null);

        bestIndividualHorizontalHistogramPanel = new HorizontalHistogramPanel();
        selectedIndividualHorizontalHistogramPanel = new HorizontalHistogramPanel();
    }

    public void updateUI(boolean updatePopulation) {
        // Update generation number
        genNumLabel.setText("" + task.getNumberOfIterations());
        fitnesEvalNumLabel.setText("" + task.getNumberOfEvaluations());

        // Update graphs
        this.overalBestFitnessGraphPanel.setScores(this.gpAlgorithm.getBestOverallFitnesses());
        this.avgFitnessConvergenceGraph.setScores(this.gpAlgorithm.getAvgGenFitnesses());
        this.avgTreeDepthGraphPanel.setScores(this.gpAlgorithm.getAvgGenTreeDepths());
        this.avgTreeSizeGraphPanel.setScores(this.gpAlgorithm.getAvgGenTreeSizes());
        this.bestGenerationFitnessGraphPanel.setScores(this.gpAlgorithm.getBestGenFitnesses());

        // Update population list
        displayPopulation(updatePopulation);

        // Update best individual
        updateBestIndividual();

    }

    public void updateGPAlgorithmParamsUI(){
        populationSizeTextField.setText("" + gpAlgorithm.getPopSize());
        maxTreeSizeTextField.setText("" + programProblem.getMaxTreeSize());
        maxTreeStartDepthTextField.setText("" + programProblem.getMaxTreeStartDepth());
        maxTreeEndDepthTextField.setText("" + programProblem.getMaxTreeEndDepth());
        minTreeDepthTextField.setText("" + programProblem.getMinTreeDepth());
        mutationProbTextField.setText("" + gpAlgorithm.getMutationProbability());
        crossoverProbTextField.setText("" + gpAlgorithm.getCrossoverProbability());
        numOfTurnamentsTextField.setText("" + gpAlgorithm.getNumberOfTournaments());
        elitismProbTextField.setText("" + gpAlgorithm.getElitismProbability());
        if(gpAlgorithm.getBest() != null)
            bestIndividualJsonTextField.setText(gpAlgorithm.getBest().getTree().toJsonString());

        problemNameTextField.setText(programProblem.getName());
        seqSelNumOfChildrenTextField.setText("" + Sequencer.MAX_CHILDREN);
        imagePathTextField.setText(imgPathPrefix);
        jsonBodyDestFolderPathTextField.setText(programProblem.getJsonBodyDestFolderPath());
        evalEnvInstanceURIsTextField.setText(Arrays.stream(programProblem.getEvalEnvInstanceURIs()).map(Object::toString).reduce((a, b) -> a + "," + b).orElse(""));

        individualGenMethodComboBox.setSelectedItem(programProblem.getProgramSolutionGenerator().getInitPopGeneratorMethod().toString());

        if(configuration != null){
            //configurationFileTextField.setText(configurationFileTextField.getText());
            unityConfigDestFileTextField.setText(configuration.UnityConfigDestFilePath);
        }

        // Fill functions and terminals text fields with class names
        functionsTextField.setText(programProblem.getBaseFunctionNodeTypes().stream().map(Class::getName).collect(Collectors.joining(", ")));
        terminalsTextField.setText(programProblem.getBaseTerminalNodeTypes().stream().map(Class::getName).collect(Collectors.joining(", ")));
    }

    public void displayPopulation(boolean updatePopulation){
        // Remove all elements from algorithmPopulationPanel
        clearImages();
        algorithmPopulationPanel.removeAll();

        if(!updatePopulation || this.gpAlgorithm.getPopulation() == null || this.gpAlgorithm.getPopulation().size() == 0)
            return;

        int numOfCols = 6;
        GridLayout gridLayout = new GridLayout(this.gpAlgorithm.getPopulation().size() / numOfCols ,numOfCols);
        gridLayout.setHgap(5);
        gridLayout.setVgap(5);
        algorithmPopulationPanel.setLayout(gridLayout);

        // Parallel
        IntStream.range(0, this.gpAlgorithm.getPopulation().size())
                .parallel()
                .forEach(index -> {
                    final int indexFinal =  index + (this.task.getNumberOfIterations() * this.gpAlgorithm.getPopulation().size());
                    ProgramSolution individual = this.gpAlgorithm.getPopulation().get(index);
                    String file = individual.getTree().displayTree(imgPathPrefix + "tree" + String.valueOf(indexFinal), false);
                    ImagePanel imagePanel = new ImagePanel(file);
                    imagePanel.setMinimumSize(new Dimension(100, 100));
                    imagePanel.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                            individualImagePanelMouseClicked(evt, individual, indexFinal);
                            if (evt.getClickCount() == 2) {
                                try {
                                    Desktop.getDesktop().open(Paths.get(file).toFile());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    synchronized (algorithmPopulationPanel) {
                        algorithmPopulationPanel.add(imagePanel);
                    }
                });


        // Sequential
        /*for (ProgramSolution<Double> individual : this.gpAlgorithm.getPopulation()) {
            String file = individual.getProgram().displayTree(imgPathPrefix + "tree" + String.valueOf(GPInterface.individualCounter), false);
            ImagePanel imagePanel = new ImagePanel(file);
            imagePanel.setMinimumSize(new Dimension(100,100));
            // Add click event on imagePanel
            final int index = GPInterface.individualCounter;
            imagePanel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    individualImagePanelMouseClicked(evt, individual, index);
                    if (evt.getClickCount() == 2) {
                        try {
                            Desktop.getDesktop().open(Paths.get(file).toFile());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            algorithmPopulationPanel.add(imagePanel);
            GPInterface.individualCounter += 1;
        }*/

        algorithmPopulationPanel.revalidate();
    }

    public void individualImagePanelMouseClicked(java.awt.event.MouseEvent evt, ProgramSolution individual, int index) {
        if(individual == null) {
            selectedIndividualImagePanel.setImage(null);
            selectedIndividualID.setText("" );
            selectedIndividualFitnes.setText("");
            selectedIndividualTreeDepthLabel.setText("");
            selectedIndividualTreeSize.setText("");
            selectedIndividualIsFeasible.setText("");
            selectedIndividualNumOfFunc.setText("");
            selectedIndividualNumOfTerm.setText("");
        }
        else{
            String file = individual.getTree().displayTree(imgPathPrefix + "tree" + String.valueOf(index), false);
            selectedIndividualImagePanel.setImage(file);
            selectedIndividualID.setText("" + index);
            selectedIndividualFitnes.setText("" + Util.roundDouble(individual.getEval(), 2));
            selectedIndividualTreeDepthLabel.setText("" + individual.getTree().treeDepth());
            selectedIndividualTreeSize.setText("" + individual.getTree().treeSize());
            selectedIndividualIsFeasible.setText("" + this.programProblem.isFeasible(individual));
            selectedIndividualNumOfFunc.setText("" + individual.getTree().numberOfFunctions());
            selectedIndividualNumOfTerm.setText("" + individual.getTree().numberOfTerminals());

            selectedIndividualHorizontalHistogramPanel.setData(individual.getFitnessesCombined());
        }
    }

    public void updateBestIndividual(){
        if(this.gpAlgorithm.getBest() == null) {
            bestIndividualImagePanel.setImage(null);
            bestIndividualID.setText("" );
            bestIndividualFitnes.setText("");
            bestIndividualTreeDepth.setText("");
            bestIndividualTreeSize.setText("");
            bestIndividualIsFeasible.setText("");
            bestIndividualNumOfFunc.setText("");
            bestIndividualNumOfTerm.setText("");
        }
        else{
            this.lastUuid = UUID.randomUUID().toString();
            String file = this.gpAlgorithm.getBest().getTree().displayTree(imgPathPrefix + "tree" + this.lastUuid, false);
            bestIndividualImagePanel.setImage(file);
            bestIndividualID.setText("" + this.gpAlgorithm.getBest().getID());
            bestIndividualFitnes.setText("" + Util.roundDouble(this.gpAlgorithm.getBest().getEval(), 2));
            bestIndividualTreeDepth.setText("" + this.gpAlgorithm.getBest().getTree().treeDepth());
            bestIndividualTreeSize.setText("" + this.gpAlgorithm.getBest().getTree().treeSize());
            bestIndividualIsFeasible.setText("" + this.programProblem.isFeasible(this.gpAlgorithm.getBest()));
            bestIndividualNumOfFunc.setText("" + this.gpAlgorithm.getBest().getTree().numberOfFunctions());
            bestIndividualNumOfTerm.setText("" + this.gpAlgorithm.getBest().getTree().numberOfTerminals());

            // Display fitness values histogram
            bestIndividualHorizontalHistogramPanel.setData(this.gpAlgorithm.getBest().getFitnessesCombined());
        }
    }

    private void initializeGpAlgorithmStateSymbolicRegression(String initialStateFile) {
        java.util.List<Class<? extends Node>> baseFunctionNodeTypes = Arrays.asList(
                AddNode.class,
                DivNode.class,
                MulNode.class,
                SubNode.class
        );

        java.util.List<Class<? extends Node>> baseTerminalNodeTypes = Arrays.asList(
                ConstNode.class,
                VarNode.class
        );

        VarNode.variables = Arrays.asList("x");

        /*
        // y=x^3 + 2x^2 + 8x + 12
        Util.list( new Target().when("x", 0).targetIs(12),
                new Target().when("x", 1).targetIs(23),
                new Target().when("x", 2).targetIs(44),
                new Target().when("x", 3).targetIs(81),
                new Target().when("x", 4).targetIs(140),
                new Target().when("x", 5).targetIs(227),
                new Target().when("x", 6).targetIs(348),
                new Target().when("x", 7).targetIs(509),
                new Target().when("x", 8).targetIs(716),
                new Target().when("x", 9).targetIs(975),
                new Target().when("x", 10).targetIs(1292)),
        */

        List<Target> evalData = Util.list( new Target().when("x", 0).targetIs(0),
                new Target().when("x", 1).targetIs(11),
                new Target().when("x", 2).targetIs(24),
                new Target().when("x", 3).targetIs(39),
                new Target().when("x", 4).targetIs(56),
                new Target().when("x", 5).targetIs(75),
                new Target().when("x", 6).targetIs(96),
                new Target().when("x", 7).targetIs(119),
                new Target().when("x", 8).targetIs(144),
                new Target().when("x", 9).targetIs(171),
                new Target().when("x", 10).targetIs(200));

        if(initialStateFile != null) {
            this.gpAlgorithm = GPAlgorithm.deserializeAlgorithmState(initialStateFile);
            this.task = this.gpAlgorithm.getTask();
            this.programProblem = this.task.problem;
        }
        else {
            this.programProblem = new SymbolicRegressionProblem(baseFunctionNodeTypes, baseTerminalNodeTypes, 3, 12, 12, 100, new FeasibilityGPOperator[]{ new GPTreeExpansionOperator(), new GPDepthBasedTreePruningOperator()},
                    new GPOperator[]{}, new GPRandomProgramSolution(), evalData);
            this.task = new Task<>(programProblem, StopCriterion.EVALUATIONS, 10000, 0, 0);

            this.gpAlgorithm =  new DefaultGPAlgorithm(300, 0.90, 0.1, 2, this.task, null);
            //this.gpAlgorithm =  new ElitismGPAlgorithm(100, 0.8,0.05, 0.1, 4, this.task, null); // Collector_conf_4
        }
        this.gpAlgorithm.setDebug(true);
    }

    public void initializeGpAlgorithmStateBehaviourTree(String initialStateFile){
        List<Class<? extends Node>> baseFunctionNodeTypes = Arrays.asList(
                //Repeat.class, // TODO remove comment
                Sequencer.class,
                Selector.class,
                Inverter.class
        );

        List<Class<? extends Node>> baseTerminalNodeTypes = Arrays.asList(
                RayHitObject.class,
                MoveForward.class,
                Rotate.class,

                //MoveSide.class,

                // Robostrike nodes
                RotateTurret.class,
                Shoot.class,
                HealthLevelBellow.class,
                ShieldLevelBellow.class,
                AmmoLevelBellow.class
        );

        if(initialStateFile != null){
            this.gpAlgorithm = GPAlgorithm.deserializeAlgorithmState(initialStateFile);
            this.task = this.gpAlgorithm.getTask();
            this.programProblem = this.task.problem;
        }
        else {
            this.programProblem = new UnityBTProblem(baseFunctionNodeTypes, baseTerminalNodeTypes, 5, 10, 10, 100, new FeasibilityGPOperator[]{ new GPTreeExpansionOperator(), new GPDepthBasedTreePruningOperator()},
                    new GPOperator[]{}, new GPRampedHalfAndHalf());
            this.programProblem.setName(problemNameTextField.getText());

            this.task = new Task<>(this.programProblem, StopCriterion.EVALUATIONS, 500000, 0, 0);

            //this.gpAlgorithm =  new DefaultGPAlgorithm(300, 0.9, 0.1, 4, this.task, null); // Collector_conf_4
            this.gpAlgorithm =  new ElitismGPAlgorithm(100, 0.9,0.05, 0.1, 4, this.task, null); // Collector_conf_4
        }
        this.gpAlgorithm.setDebug(true);
    }

    public void clearImages(){
        try {
            Files.walk(Paths.get(this.imgPathPrefix))
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            System.err.println("Failed to delete file: " + path);
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            System.err.println("Failed to remove files from the folder: " + this.imgPathPrefix);
            e.printStackTrace();
        }
    }

    public String getFormattedDate() {
        // Get current date
        LocalDate date = LocalDate.now();
        // Create a new string builder
        // Append the day
        String formattedDate = String.format("%02d", date.getDayOfMonth()) +
                // Append the month
                String.format("%02d", date.getMonthValue()) +
                // Append the year
                date.getYear();
        
        return formattedDate;
    }

    public void runConfigurations(){
        if(configuration == null) {
            String configFile = configurationFileTextField.getText();
            configuration = Configuration.deserialize(configFile);
        }

        for (int i = 0; i < configuration.Configurations.size(); i++) {
            RunConfiguration runConfiguration = configuration.Configurations.get(i);
            System.out.println("Run configuration: " + i + " (" + runConfiguration.Name + ")");

            // 1. Set EARS configuration
            int generations = setEARSConfiguration(runConfiguration);

            // 2. Save Unity configuration
            Configuration.serializeUnityConfig(runConfiguration, configuration.UnityConfigDestFilePath);

            // 3 Start Unity Instances
            restartUnityInstances();

            // 6. Run algorithm
            runGPAlgorithm(generations);

            System.out.println("Run configuration: " + i + " (" + runConfiguration.Name + ") done");
        }
    }

    public void runGPAlgorithm(int numOfGens) {
        try {
            try {
                if (numOfGens <= 0)
                    numOfGens = Integer.MAX_VALUE;

                for (int i = 0; i < numOfGens; i++) {
                    ProgramSolution sol = gpAlgorithm.executeGeneration();
                    // Print current gpAlgorithm statistics to console
                    if(this.gpAlgorithm.isDebug()){
                        System.out.println("Generation: " + task.getNumberOfIterations() + ", Best Fitness: " + gpAlgorithm.getBest().getEval() + ", Avg Fitness: " + gpAlgorithm.getAvgGenFitnesses().get(gpAlgorithm.getAvgGenFitnesses().size() - 1) + ", Avg Tree Depth: " + gpAlgorithm.getAvgGenTreeDepths().get(gpAlgorithm.getAvgGenTreeDepths().size() - 1) + ", Avg Tree Size: " + gpAlgorithm.getAvgGenTreeSizes().get(gpAlgorithm.getAvgGenTreeSizes().size() - 1));
                        System.out.println("Best Individual: " + gpAlgorithm.getBest().getTree().toJsonString());
                    }

                    if (saveGPAlgorithmStatsFilename.getText().length() == 0) {
                        // Serialize algorithm state
                        GPAlgorithm.serializeAlgorithmState(gpAlgorithm, "gpAlgorithmState.ser");
                    } else {
                        // Serialize algorithm state
                        GPAlgorithm.serializeAlgorithmState(gpAlgorithm, saveGPAlgorithmStatsFilename.getText());
                    }
                    if (sol != null)
                        break;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number of generations to run (-1 for run to the end).", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            updateUI(true);
        } catch (StopCriterionException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void restartUnityInstances(){
        return; // TODO Uncomment
        /*
        // 1. Close all running GeneralTrainingPlatformForMAS instances
        try {
            Runtime.getRuntime().exec("taskkill /F /IM " + Instance.configuration.UnityGameFile);
            Thread.sleep(5000);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


        // 2. Rerun GeneralTrainingPlatformForMAS (Unity)
        for (int instances = 0; instances < Instance.configuration.EvalEnvInstanceURIs.split(",").length; instances++) {
            try {
                Runtime.getRuntime().exec("cmd /c start " + Instance.configuration.UnityExeLocation + Instance.configuration.UnityGameFile);
                Thread.sleep(2000);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 3. Wait for Unity instances to start
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }

    private void loadDefaultConfiguration(){
        // Load first configuration in the list
        if(configuration != null){
            setEARSConfiguration(configuration.Configurations.get(0));
            updateGPAlgorithmParamsUI();
        }
    }

    private int setEARSConfiguration(RunConfiguration runConfiguration){
        EARSConfiguration earsConfiguration = runConfiguration.EARSConfiguration;
        int generations = 0;
        if(earsConfiguration.FitnessEvaluations > 0){
            this.task = new Task<>(this.programProblem, StopCriterion.EVALUATIONS, earsConfiguration.FitnessEvaluations, 0, 0);
            generations = ((int) Math.ceil((float)earsConfiguration.FitnessEvaluations / earsConfiguration.PopSize)) - 1;  // -1 because init population evaluation takes one generation of evals
        }
        else if(earsConfiguration.Generations > 0){
            this.task = new Task<>(this.programProblem, StopCriterion.ITERATIONS, earsConfiguration.Generations, 0, 0);
            generations = earsConfiguration.Generations;
        }

        // AlgorithmType
        if(earsConfiguration.AlgorithmType == AlgorithmType.DGP){
            this.gpAlgorithm =  new DefaultGPAlgorithm(100, 0.9, 0.1, 4, this.task, null); // Collector_conf_4
        }
        else if(earsConfiguration.AlgorithmType == AlgorithmType.EGP){
            this.gpAlgorithm =  new ElitismGPAlgorithm(100, 0.9,0.05, 0.1, 4, this.task, null); // Collector_conf_4
        }
        this.gpAlgorithm.setDebug(true);
        // ProblemName
        programProblem.setName(earsConfiguration.ProblemName);
        // PopSize
        this.gpAlgorithm.setPopSize(earsConfiguration.PopSize);
        // CrossoverProb
        this.gpAlgorithm.setCrossoverProbability(earsConfiguration.CrossoverProb);
        // MutationProb
        this.gpAlgorithm.setMutationProbability(earsConfiguration.MutationProb);
        // ElitisProb
        if(this.gpAlgorithm instanceof ElitismGPAlgorithm)
            ((ElitismGPAlgorithm)this.gpAlgorithm).setElitismProbability(earsConfiguration.ElitismProb);
        // NumOfTournaments
        this.gpAlgorithm.setNumberOfTournaments(earsConfiguration.NumOfTournaments);
        // MinTreeDepth
        programProblem.setMinTreeDepth(earsConfiguration.MinTreeDepth);
        // MaxTreeStartDepth
        programProblem.setMaxTreeStartDepth(earsConfiguration.MaxTreeStartDepth);
        // MaxTreeEndDepth
        programProblem.setMaxTreeEndDepth(earsConfiguration.MaxTreeEndDepth);
        // MaxTreeSize
        programProblem.setMaxTreeSize(earsConfiguration.MaxTreeSize);
        // InitPopGeneratorMethod
        if(Objects.equals(earsConfiguration.InitPopGeneratorMethod, Configuration.InitPopGeneratorMethod.Random.toString())){
            programProblem.setProgramSolutionGenerator(new GPRandomProgramSolution());
        }
        else if(Objects.equals(earsConfiguration.InitPopGeneratorMethod, Configuration.InitPopGeneratorMethod.RampedHalfAndHalfMethod.toString())){
            programProblem.setProgramSolutionGenerator(new GPRampedHalfAndHalf());
        }
        // EvalEnvInstanceURIs
        programProblem.setEvalEnvInstanceURIs(configuration.EvalEnvInstanceURIs.split(","));
        // JsonBodyDestFolderPath
        programProblem.setJsonBodyDestFolderPath(configuration.JsonBodyDestFilePath);
        // ImagePath
        imgPathPrefix = configuration.ImagePath;
        // Update filename
        saveGPAlgorithmStatsFilename.setText(0 + "_" + programProblem.getName() + "_" + gpAlgorithm.getPopSize() + "_" + Util.roundDouble(gpAlgorithm.getCrossoverProbability(), 2) + "_" + Util.roundDouble(gpAlgorithm.getElitismProbability(), 2) + "_" + Util.roundDouble(gpAlgorithm.getMutationProbability(), 2) + "_" + gpAlgorithm.getNumberOfTournaments() + "_" + programProblem.getMinTreeDepth() + "_" + programProblem.getMaxTreeStartDepth() + "_" + programProblem.getMaxTreeEndDepth() + "_" + programProblem.getMaxTreeSize() + "_gpAlgorithmState_" + getFormattedDate() + ".ser" );
        // Seq, Sel number of children
        Selector.MAX_CHILDREN = earsConfiguration.SeqSelNumOfChildren;
        Sequencer.MAX_CHILDREN = earsConfiguration.SeqSelNumOfChildren;

        // Functions
        programProblem.setBaseFunctionNodeTypesFromStringList(Arrays.asList(earsConfiguration.Functions.split(",")));
        // Terminals
        programProblem.setBaseTerminalNodeTypesFromStringList(Arrays.asList(earsConfiguration.Terminals.split(",")));

        // FeasibilityControlOperators
        programProblem.setFeasibilityControlOperatorsFromStringArray(earsConfiguration.FeasibilityControlOperators);
        // BloatControlOperators
        programProblem.setBloatControlOperatorsFromStringArray(earsConfiguration.BloatControlOperators);

        return generations;
    }
    
    public static void main(String[] args) {
        GPInterface form = new GPInterface();
        form.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        form.setSize(1800, 1000);
        form.setVisible(true);
    }
}
