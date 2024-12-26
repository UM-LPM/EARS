package org.um.feri.ears.visualization.gp;

import org.um.feri.ears.algorithms.GPAlgorithm;
import org.um.feri.ears.algorithms.gp.ElitismGPAlgorithm;
import org.um.feri.ears.algorithms.gp.GPAlgorithmExecutor;
import org.um.feri.ears.individual.generations.gp.GPRampedHalfAndHalf;
import org.um.feri.ears.individual.generations.gp.GPRandomProgramSolution;
import org.um.feri.ears.individual.representations.gp.Node;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.Inverter;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.Selector;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.Sequencer;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.robostrike.*;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.movement.MoveForward;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.sensors.RayHitObject;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.movement.Rotate;
import org.um.feri.ears.operators.gp.*;
import org.um.feri.ears.problems.StopCriterionException;
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
import java.util.*;
import java.util.List;
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
    private JCheckBox displayPopulationCheckBox;
    private JCheckBox useDefaultFileNameCheckBox;
    private JTextArea jsonConfigTextArea;
    private GraphPanel multiRunBestIndividualGraphPanel;
    private GraphPanel multiRunBestAvgGraphPanel;

    private String lastUuid;

    public GPAlgorithmExecutor gpAlgorithmExecutor;

    private Thread algorithmThread;
    String imgPathPrefix;

    public GPInterface() {
        super("EARS GP (Debug)");

        setContentPane(tabbPane);

        imgPathPrefix = "src/org/um/feri/ears/visualization/gp/images/";

        clearImages();

        // Initialize data for gp algorithm
        //initializeDataSymbolicRegression(null);
        //initializeGpAlgorithmStateBehaviourTree(null);
        initializeGPAlgorithmExecutor();

        updateGPAlgorithmParamsUI();
        updateUI();

        setSaveGPAlgorithmStatsFilename();

        addButtonListeners();
        addGPAlgorithmParamsListeners();

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
                    gpAlgorithmExecutor.getGpAlgorithm().resetToDefaultsBeforeNewRun();
                    updateUI();
                    individualImagePanelMouseClicked(null, null, -1);
                }
            });
            algorithmThread.start();
        });
        nextAlgStepButton.addActionListener(e -> {
            // Run one step
            try {
                gpAlgorithmExecutor.getGpAlgorithm().executeStep();
                updateUI();
            } catch (StopCriterionException ex) {
                throw new RuntimeException(ex);
            }
        });
        nextAlgGenerationButton.addActionListener(e -> {
            // Run one generation
            try {
                gpAlgorithmExecutor.getGpAlgorithm().executeGeneration();
                updateUI();
            } catch (StopCriterionException ex) {
                throw new RuntimeException(ex);
            }
        });
        runAlgButton.addActionListener(e -> {
            if(configurationFileTextField.getText().length() > 0 || gpAlgorithmExecutor.getConfiguration() != null){
                runConfigurations();
            }
            else {
                JOptionPane.showMessageDialog(null, "Load configuration first", "Error", JOptionPane.ERROR_MESSAGE);
                /*try {
                    int numOfGens = Integer.parseInt(runXAlgGensTextField.getText());
                    gpAlgorithmExecutor.execute(numOfGens, saveGPAlgorithmStatsFilename.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid number of generations to run (-1 for run to the end).", "Error", JOptionPane.ERROR_MESSAGE);
                }*/
            }
        });

        saveCurrentAlgState.addActionListener(e -> {
            if(saveGPAlgorithmStatsFilename.getText().length() == 0){
                JOptionPane.showMessageDialog(null, "Please enter a valid filename.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            else{
                // Serialize algorithm state
                GPAlgorithm.serializeAlgorithmState(gpAlgorithmExecutor.getGpAlgorithm(), saveGPAlgorithmStatsFilename.getText());
            }
            //GPAlgorithm.serializeAlgorithmState(gpAlgorithm, "gpAlgorithmState.ser");
        });

        loadCurrentAlgState.addActionListener(e -> {
            String gpDataFile = selectGPDataFile();

            try{
                gpAlgorithmExecutor.initializeGpAlgorithmStateFromFile(gpDataFile);
                setSaveGPAlgorithmStatsFilename();

                updateGPAlgorithmParamsUI();
                updateUI();

                return;
            }
            catch (Exception ex){
                System.out.println("Error loading gp algorithm state from file: " + ex.getMessage() + "\n Trying to load gp algorithm executor state");
            }

            try {
                this.gpAlgorithmExecutor = GPAlgorithmExecutor.deserializeGPAlgorithmExecutorState(gpDataFile);
                setSaveGPAlgorithmStatsFilename();

                updateGPAlgorithmParamsUI();
                updateUI();

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void addGPAlgorithmParamsListeners(){
        populationSizeTextField.addActionListener(e -> {
            try {
                int populationSize = Integer.parseInt(populationSizeTextField.getText());
                gpAlgorithmExecutor.getGpAlgorithm().setPopSize(populationSize);
                setSaveGPAlgorithmStatsFilename();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number for population size.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        crossoverProbTextField.addActionListener(e -> {
            try {
                double crossoverProb = Double.parseDouble(crossoverProbTextField.getText());
                gpAlgorithmExecutor.getGpAlgorithm().setCrossoverProbability(crossoverProb);
                setSaveGPAlgorithmStatsFilename();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number for crossover probability.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        mutationProbTextField.addActionListener(e -> {
            try {
                double mutationProb = Double.parseDouble(mutationProbTextField.getText());
                gpAlgorithmExecutor.getGpAlgorithm().setMutationProbability(mutationProb);
                setSaveGPAlgorithmStatsFilename();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number for mutation probability.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        numOfTurnamentsTextField.addActionListener(e -> {
            try {
                int numOfTurnaments = Integer.parseInt(numOfTurnamentsTextField.getText());
                gpAlgorithmExecutor.getGpAlgorithm().setNumberOfTournaments(numOfTurnaments);
                setSaveGPAlgorithmStatsFilename();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number for number of tournaments.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        minTreeDepthTextField.addActionListener(e -> {
            try {
                int minTreeDepth = Integer.parseInt(minTreeDepthTextField.getText());
                gpAlgorithmExecutor.getProgramProblem().setMinTreeDepth(minTreeDepth);
                setSaveGPAlgorithmStatsFilename();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number for minimum tree depth.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        maxTreeStartDepthTextField.addActionListener(e -> {
            try {
                int maxTreeStartDepth = Integer.parseInt(maxTreeStartDepthTextField.getText());
                gpAlgorithmExecutor.getProgramProblem().setMaxTreeStartDepth(maxTreeStartDepth);
                setSaveGPAlgorithmStatsFilename();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number for maximum tree depth.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        maxTreeEndDepthTextField.addActionListener(e -> {
            try {
                int maxTreeEndDepth = Integer.parseInt(maxTreeEndDepthTextField.getText());
                gpAlgorithmExecutor.getProgramProblem().setMaxTreeEndDepth(maxTreeEndDepth);
                setSaveGPAlgorithmStatsFilename();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number for maximum tree depth.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        maxTreeSizeTextField.addActionListener(e -> {
            try {
                int maxTreeSize = Integer.parseInt(maxTreeSizeTextField.getText());
                gpAlgorithmExecutor.getProgramProblem().setMaxTreeSize(maxTreeSize);
                setSaveGPAlgorithmStatsFilename();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number for maximum tree size.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        problemNameTextField.addActionListener(e -> {
            gpAlgorithmExecutor.getProgramProblem().setName(problemNameTextField.getText());
            setSaveGPAlgorithmStatsFilename();
        });

        elitismProbTextField.addActionListener(e -> {
            try {
                double elitismProb = Double.parseDouble(elitismProbTextField.getText());
                gpAlgorithmExecutor.getGpAlgorithm().setElitismProbability(elitismProb);
                setSaveGPAlgorithmStatsFilename();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number for elitism probability.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        evalEnvInstanceURIsTextField.addActionListener(e -> {
            if(evalEnvInstanceURIsTextField.getText().length() > 0){
                gpAlgorithmExecutor.getProgramProblem().getRequestBodyParams().setEvalEnvInstanceURIs(evalEnvInstanceURIsTextField.getText().split(","));
            }
            else{
                gpAlgorithmExecutor.getProgramProblem().getRequestBodyParams().setEvalEnvInstanceURIs(new String[]{});
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
                gpAlgorithmExecutor.getProgramProblem().setJsonBodyDestFolderPath(jsonBodyDestFolderPathTextField.getText());
            }
            else{
                gpAlgorithmExecutor.getProgramProblem().setJsonBodyDestFolderPath("");
            }
        });

        individualGenMethodComboBox.addActionListener(e -> {
            String selectedMethod = (String) individualGenMethodComboBox.getSelectedItem();
            if(selectedMethod.equals(Configuration.InitPopGeneratorMethod.Random.toString())){
                gpAlgorithmExecutor.getProgramProblem().setProgramSolutionGenerator(new GPRandomProgramSolution());
            }
            else if(selectedMethod.equals(Configuration.InitPopGeneratorMethod.RampedHalfAndHalfMethod.toString())){
                gpAlgorithmExecutor.getProgramProblem().setProgramSolutionGenerator(new GPRampedHalfAndHalf());
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
            loadConfiguration(true);
        });

        functionsTextField.addActionListener(e -> {
            if(functionsTextField.getText().length() > 0){
                gpAlgorithmExecutor.getProgramProblem().setBaseFunctionNodeTypesFromStringList(Arrays.asList(functionsTextField.getText().split(",")));
            }
            else{
                gpAlgorithmExecutor.getProgramProblem().setBaseFunctionNodeTypesFromStringList(List.of());
            }
        });

        terminalsTextField.addActionListener(e -> {
            if(terminalsTextField.getText().length() > 0){
                gpAlgorithmExecutor.getProgramProblem().setBaseTerminalNodeTypesFromStringList(Arrays.asList(terminalsTextField.getText().split(",")));
            }
            else{
                gpAlgorithmExecutor.getProgramProblem().setBaseTerminalNodeTypesFromStringList(List.of());
            }
        });

        jsonConfigTextArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                // Load configuration if ctrl + s is pressed
                if (evt.isControlDown() && evt.getKeyCode() == java.awt.event.KeyEvent.VK_S)
                    loadConfiguration(false);
            }
        });
    }

    public void loadConfiguration(boolean loadFromFile){
        try {
            if (loadFromFile) {
                if (configurationFileTextField.getText().length() > 0) {

                    String configFile = configurationFileTextField.getText();
                    gpAlgorithmExecutor.setConfiguration(Configuration.deserializeFromFile(configFile));

                    // Read file from configFile and set jsonConfigTextArea
                    jsonConfigTextArea.setText(new String(Files.readAllBytes(Paths.get(configFile))));
                }
                else{
                    return;
                }
            } else {
                gpAlgorithmExecutor.setConfiguration(Configuration.deserializeFromJson(jsonConfigTextArea.getText()));
            }

            JOptionPane.showMessageDialog(null, "Configuration file loaded.", "Operation successful", JOptionPane.INFORMATION_MESSAGE);
            // Update UI with configuration values (load fist configuration only for display purposes)
            gpAlgorithmExecutor.setGpAlgorithm(null);
            gpAlgorithmExecutor.loadDefaultConfiguration();

            updateGPAlgorithmParamsUI();
            updateUI();
        }
        catch (Exception ex){
            JOptionPane.showMessageDialog(null, "Error loading configuration file. \n" + ex, "Error", JOptionPane.ERROR_MESSAGE);
        }
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

        this.multiRunBestIndividualGraphPanel = new GraphPanel(null);
        this.multiRunBestAvgGraphPanel = new GraphPanel(null);

        bestIndividualHorizontalHistogramPanel = new HorizontalHistogramPanel();
        selectedIndividualHorizontalHistogramPanel = new HorizontalHistogramPanel();
    }

    public void updateUI() {
        // Update generation number
        genNumLabel.setText("" + gpAlgorithmExecutor.getTask().getNumberOfIterations());
        fitnesEvalNumLabel.setText("" + gpAlgorithmExecutor.getTask().getNumberOfEvaluations());

        // Update graphs
        this.overalBestFitnessGraphPanel.setScores(gpAlgorithmExecutor.getGpAlgorithm().getBestOverallFitnesses());
        this.avgFitnessConvergenceGraph.setScores(gpAlgorithmExecutor.getGpAlgorithm().getAvgGenFitnesses());
        this.avgTreeDepthGraphPanel.setScores(gpAlgorithmExecutor.getGpAlgorithm().getAvgGenTreeDepths());
        this.avgTreeSizeGraphPanel.setScores(gpAlgorithmExecutor.getGpAlgorithm().getAvgGenTreeSizes());
        this.bestGenerationFitnessGraphPanel.setScores(gpAlgorithmExecutor.getGpAlgorithm().getBestGenFitnesses());

        // Update population list
        displayPopulation(displayPopulationCheckBox.isSelected());

        // Update best individual
        updateBestIndividual();

        // Clear selected individual
        clearSelectedIndividualUI();

        // Update multiRun graphs (GPAlgorithmExecutor.gpAlgorithmRunStats)
        if(gpAlgorithmExecutor != null && gpAlgorithmExecutor.getGpAlgorithmRunStats() != null){
            this.multiRunBestIndividualGraphPanel.setScores(GPAlgorithmRunStats.getBestRunFitnesses(gpAlgorithmExecutor.getGpAlgorithmRunStats()));
            this.multiRunBestAvgGraphPanel.setScores(GPAlgorithmRunStats.getBestRunAvgFitnesses(gpAlgorithmExecutor.getGpAlgorithmRunStats()));
        }

        if(gpAlgorithmExecutor != null && gpAlgorithmExecutor.getGpAlgorithm().getBest() != null)
            this.bestIndividualJsonTextField.setText(gpAlgorithmExecutor.getGpAlgorithm().getBest().getTree().toJsonString());
    }

    public void updateGPAlgorithmParamsUI(){
        populationSizeTextField.setText("" + gpAlgorithmExecutor.getGpAlgorithm().getPopSize());
        maxTreeSizeTextField.setText("" + gpAlgorithmExecutor.getProgramProblem().getMaxTreeSize());
        maxTreeStartDepthTextField.setText("" + gpAlgorithmExecutor.getProgramProblem().getMaxTreeStartDepth());
        maxTreeEndDepthTextField.setText("" + gpAlgorithmExecutor.getProgramProblem().getMaxTreeEndDepth());
        minTreeDepthTextField.setText("" + gpAlgorithmExecutor.getProgramProblem().getMinTreeDepth());
        mutationProbTextField.setText("" + gpAlgorithmExecutor.getGpAlgorithm().getMutationProbability());
        crossoverProbTextField.setText("" + gpAlgorithmExecutor.getGpAlgorithm().getCrossoverProbability());
        numOfTurnamentsTextField.setText("" + gpAlgorithmExecutor.getGpAlgorithm().getNumberOfTournaments());
        elitismProbTextField.setText("" + gpAlgorithmExecutor.getGpAlgorithm().getElitismProbability());
        if(gpAlgorithmExecutor.getGpAlgorithm().getBest() != null)
            bestIndividualJsonTextField.setText(gpAlgorithmExecutor.getGpAlgorithm().getBest().getTree().toJsonString());

        problemNameTextField.setText(gpAlgorithmExecutor.getProgramProblem().getName());
        seqSelNumOfChildrenTextField.setText("" + Sequencer.MAX_CHILDREN);
        imagePathTextField.setText(imgPathPrefix);
        jsonBodyDestFolderPathTextField.setText(gpAlgorithmExecutor.getProgramProblem().getJsonBodyDestFolderPath());
        evalEnvInstanceURIsTextField.setText(Arrays.stream(gpAlgorithmExecutor.getProgramProblem().getRequestBodyParams().getEvalEnvInstanceURIs()).map(Object::toString).reduce((a, b) -> a + "," + b).orElse(""));

        individualGenMethodComboBox.setSelectedItem(gpAlgorithmExecutor.getProgramProblem().getProgramSolutionGenerator().getInitPopGeneratorMethod().toString());

        if(gpAlgorithmExecutor.getConfiguration() != null){
            //configurationFileTextField.setText(configurationFileTextField.getText());
            unityConfigDestFileTextField.setText(gpAlgorithmExecutor.getConfiguration().UnityConfigDestFilePath);
        }

        // Fill functions and terminals text fields with class names
        functionsTextField.setText(gpAlgorithmExecutor.getProgramProblem().getBaseFunctionNodeTypes().stream().map(Class::getName).collect(Collectors.joining(", ")));
        terminalsTextField.setText(gpAlgorithmExecutor.getProgramProblem().getBaseTerminalNodeTypes().stream().map(Class::getName).collect(Collectors.joining(", ")));

        // ImagePath
        if(gpAlgorithmExecutor.getConfiguration() != null)
            imagePathTextField.setText(gpAlgorithmExecutor.getConfiguration().ImagePath);

        // Update filename
        setSaveGPAlgorithmStatsFilename();

    }

    public void displayPopulation(boolean updatePopulation){
        // Remove all elements from algorithmPopulationPanel
        clearImages();
        algorithmPopulationPanel.removeAll();

        if(!updatePopulation || gpAlgorithmExecutor.getGpAlgorithm().getPopulation() == null || gpAlgorithmExecutor.getGpAlgorithm().getPopulation().size() == 0)
            return;

        int numOfCols = 6;
        GridLayout gridLayout = new GridLayout(gpAlgorithmExecutor.getGpAlgorithm().getPopulation().size() / numOfCols ,numOfCols);
        gridLayout.setHgap(5);
        gridLayout.setVgap(5);
        algorithmPopulationPanel.setLayout(gridLayout);

        // Parallel
        IntStream.range(0, gpAlgorithmExecutor.getGpAlgorithm().getPopulation().size())
                .parallel()
                .forEach(index -> {
                    final int indexFinal =  index + (gpAlgorithmExecutor.getTask().getNumberOfIterations() * gpAlgorithmExecutor.getGpAlgorithm().getPopulation().size());
                    ProgramSolution individual = gpAlgorithmExecutor.getGpAlgorithm().getPopulation().get(index);
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
            clearSelectedIndividualUI();
        }
        else{
            String file = individual.getTree().displayTree(imgPathPrefix + "tree" + String.valueOf(index), false);
            selectedIndividualImagePanel.setImage(file);
            selectedIndividualID.setText("" + index);
            selectedIndividualFitnes.setText("" + Util.roundDouble(individual.getEval(), 2));
            selectedIndividualTreeDepthLabel.setText("" + individual.getTree().treeMaxDepth());
            selectedIndividualTreeSize.setText("" + individual.getTree().treeSize());
            selectedIndividualIsFeasible.setText("" + gpAlgorithmExecutor.getProgramProblem().isFeasible(individual));
            selectedIndividualNumOfFunc.setText("" + individual.getTree().numberOfFunctions());
            selectedIndividualNumOfTerm.setText("" + individual.getTree().numberOfTerminals());

            selectedIndividualHorizontalHistogramPanel.setData(individual.getFitnessesCombined());
        }
    }

    public void clearSelectedIndividualUI(){
        selectedIndividualImagePanel.setImage(null);
        selectedIndividualID.setText("" );
        selectedIndividualFitnes.setText("");
        selectedIndividualTreeDepthLabel.setText("");
        selectedIndividualTreeSize.setText("");
        selectedIndividualIsFeasible.setText("");
        selectedIndividualNumOfFunc.setText("");
        selectedIndividualNumOfTerm.setText("");
        selectedIndividualHorizontalHistogramPanel.setData(new HashMap<>());
    }

    public void updateBestIndividual(){
        if(gpAlgorithmExecutor.getGpAlgorithm().getBest() == null) {
            bestIndividualImagePanel.setImage(null);
            bestIndividualID.setText("" );
            bestIndividualFitnes.setText("");
            bestIndividualTreeDepth.setText("");
            bestIndividualTreeSize.setText("");
            bestIndividualIsFeasible.setText("");
            bestIndividualNumOfFunc.setText("");
            bestIndividualNumOfTerm.setText("");
            bestIndividualHorizontalHistogramPanel.setData(new HashMap<>());
        }
        else{
            this.lastUuid = UUID.randomUUID().toString();
            String file = gpAlgorithmExecutor.getGpAlgorithm().getBest().getTree().displayTree(imgPathPrefix + "tree" + this.lastUuid, false);
            bestIndividualImagePanel.setImage(file);
            bestIndividualID.setText("" + gpAlgorithmExecutor.getGpAlgorithm().getBest().getID());
            bestIndividualFitnes.setText("" + Util.roundDouble(gpAlgorithmExecutor.getGpAlgorithm().getBest().getEval(), 2));
            bestIndividualTreeDepth.setText("" + gpAlgorithmExecutor.getGpAlgorithm().getBest().getTree().treeMaxDepth());
            bestIndividualTreeSize.setText("" + gpAlgorithmExecutor.getGpAlgorithm().getBest().getTree().treeSize());
            bestIndividualIsFeasible.setText("" + gpAlgorithmExecutor.getProgramProblem().isFeasible(gpAlgorithmExecutor.getGpAlgorithm().getBest()));
            bestIndividualNumOfFunc.setText("" + gpAlgorithmExecutor.getGpAlgorithm().getBest().getTree().numberOfFunctions());
            bestIndividualNumOfTerm.setText("" + gpAlgorithmExecutor.getGpAlgorithm().getBest().getTree().numberOfTerminals());

            // Display fitness values histogram
            bestIndividualHorizontalHistogramPanel.setData(gpAlgorithmExecutor.getGpAlgorithm().getBest().getFitnessesCombined());
        }
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

    public void runConfigurations(){
        this.gpAlgorithmExecutor.runConfigurations(configurationFileTextField.getText(), useDefaultFileNameCheckBox.isSelected() ? null : saveGPAlgorithmStatsFilename.getText());
        updateUI();
    }

    public void initializeGPAlgorithmExecutor(){
        this.gpAlgorithmExecutor = new GPAlgorithmExecutor(true);

        List<Class<? extends Node>> baseFunctionNodeTypes = Arrays.asList(
                //Repeat.class, // TODO Use this?
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

        this.gpAlgorithmExecutor.initializeGpAlgorithmState("UnityBTProblem",baseFunctionNodeTypes, baseTerminalNodeTypes, null, 5, 7, 10, 100, new FeasibilityGPOperator[]{ new GPTreeExpansionOperator(), new GPDepthBasedTreePruningOperator()},
                new GPOperator[]{}, GPProblemEvaluatorType.Simple, LastEvalIndividualFitnessesRatingCompositionType.Default, new GPRandomProgramSolution(), 500000, false, ElitismGPAlgorithm.class, 100, 0.75, 0.05, 0.07, 4);
    }

    public void setSaveGPAlgorithmStatsFilename(){
        saveGPAlgorithmStatsFilename.setText(gpAlgorithmExecutor.getDefaultGPAlgorithmStateFilename());
    }
    
    public static void main(String[] args) {
        GPInterface form = new GPInterface();
        form.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        form.setSize(1800, 1000);
        form.setVisible(true);
    }
}
