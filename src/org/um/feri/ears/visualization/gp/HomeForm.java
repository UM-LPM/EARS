package org.um.feri.ears.visualization.gp;

import org.um.feri.ears.individual.representations.gp.Node;
import org.um.feri.ears.individual.representations.gp.Target;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.Inverter;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.Repeat;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.Selector;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.Sequencer;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.soccer.MoveForward;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.soccer.MoveSide;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.soccer.RayHitObject;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.soccer.Rotate;
import org.um.feri.ears.individual.representations.gp.symbolic.regression.*;
import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.gp.*;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.visualization.gp.components.GraphPanel;
import org.um.feri.ears.visualization.gp.components.ImagePanel;

import org.um.feri.ears.algorithms.GPAlgorithm2;
import org.um.feri.ears.algorithms.gp.DefaultGPAlgorithm2;
import org.um.feri.ears.individual.generations.gp.GPRandomProgramSolution2;
import org.um.feri.ears.operators.gp.GPDepthBasedTreePruningOperator2;
import org.um.feri.ears.operators.gp.GPTreeExpansionOperator2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

public class HomeForm extends JFrame {

    private JPanel contentPane;
    private JLabel genNumLabel;
    private JLabel fitnesEvalNumLabel;
    private JButton nextAlgGenerationButton;
    private JButton resetAlgButton;
    private JButton nextAlgStepButton;
    private JPanel algorithmPopulationPanel;
    private JPanel currentStatusPanel;
    private JButton runAlgButton;

    private ProgramProblem2 programProblem;
    private Task<ProgramSolution2, ProgramProblem2> task;
    private GPAlgorithm2 gpAlgorithm;
    private Thread algorithmThread;
    String imgPathPrefix;

    // Selected individual parameters
    private ImagePanel selectedIndividualImagePanel;
    private JLabel selectedIndividualTreeHeightLabel;
    private JLabel selectedIndividualFitnes;
    private JLabel selectedIndividualTreeSize;
    private JLabel selectedIndividualIsFeasible;
    private JLabel selectedIndividualNumOfFunc;
    private JLabel selectedIndividualNumOfTerm;
    private JLabel selectedIndividualID;
    private JTextField runXAlgGensTextField;
    private GraphPanel fitnessConvergenceGraph;
    private GraphPanel avgFitnessConvergenceGraph;
    private JLabel bestIndividualID;
    private JLabel bestIndividualFitnes;
    private JLabel bestIndividualTreeHeight;
    private JLabel bestIndividualTreeSize;
    private JLabel bestIndividualIsFeasible;
    private JLabel bestIndividualNumOfFunc;
    private JLabel bestIndividualNumOfTerm;
    private ImagePanel bestIndividualImagePanel;
    private GraphPanel avgTreeHeightGraphPanel;
    private GraphPanel avgTreeSizeGraphPanel;
    private JButton saveCurrentAlgState;
    private JButton loadCurrentAlgState;

    private String lastUuid;
    public HomeForm() {
        super("EARS GP (Debug)");
        setContentPane(contentPane);

        imgPathPrefix = "src/org/um/feri/ears/visualization/gp/images/";
        clearImages();

        // Initialize data for gp algorithm
        //initializeDataSymbolicRegression(null);
        initializeDataBehaviourTree(null);

        updateUI(false);

        // Action buttons
        resetAlgButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
            }
        });
        nextAlgStepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Run one step
                try {
                    gpAlgorithm.executeStep();
                    updateUI(true);
                } catch (StopCriterionException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        nextAlgGenerationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Run one generation
                try {
                    gpAlgorithm.executeGeneration();
                    updateUI(true);
                } catch (StopCriterionException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        runAlgButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    try {
                        int numOfGens = Integer.parseInt(runXAlgGensTextField.getText());
                        if(numOfGens <= 0)
                            numOfGens = Integer.MAX_VALUE;

                        for(int i = 0; i < numOfGens; i++){
                            if(gpAlgorithm.executeGeneration() != null)
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
        });

        saveCurrentAlgState.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GPAlgorithm2.serializeAlgorithmState(gpAlgorithm, "gpAlgorithmState.ser");
            }
        });

        loadCurrentAlgState.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initializeDataBehaviourTree("gpAlgorithmState.ser");
                updateUI(true);
            }
        });
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


        // Create fitness convergence line graph
        this.fitnessConvergenceGraph = new GraphPanel(null);
        this.avgFitnessConvergenceGraph = new GraphPanel(null);
        this.avgTreeHeightGraphPanel = new GraphPanel(null);
        this.avgTreeSizeGraphPanel = new GraphPanel(null);

    }

    public void updateUI(boolean updatePopulation) {
        // Update generation number
        genNumLabel.setText("" + task.getNumberOfIterations());
        fitnesEvalNumLabel.setText("" + task.getNumberOfEvaluations());

        // Update convergence graph
        this.fitnessConvergenceGraph.setScores(this.gpAlgorithm.getBestGenFitness());
        this.avgFitnessConvergenceGraph.setScores(this.gpAlgorithm.getAvgGenFitness());
        this.avgTreeHeightGraphPanel.setScores(this.gpAlgorithm.getAvgGenTreeHeight());
        this.avgTreeSizeGraphPanel.setScores(this.gpAlgorithm.getAvgGenTreeSize());

        // Update population list
        displayPopulation(updatePopulation);

        // Update best individual
        updateBestIndividual();

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
                    ProgramSolution2 individual = this.gpAlgorithm.getPopulation().get(index);
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
            String file = individual.getProgram().displayTree(imgPathPrefix + "tree" + String.valueOf(HomeForm.individualCounter), false);
            ImagePanel imagePanel = new ImagePanel(file);
            imagePanel.setMinimumSize(new Dimension(100,100));
            // Add click event on imagePanel
            final int index = HomeForm.individualCounter;
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
            HomeForm.individualCounter += 1;
        }*/

        algorithmPopulationPanel.revalidate();
    }

    public void individualImagePanelMouseClicked(java.awt.event.MouseEvent evt, ProgramSolution2 individual, int index) {
        if(individual == null) {
            selectedIndividualImagePanel.setImage(null);
            selectedIndividualID.setText("" );
            selectedIndividualFitnes.setText("");
            selectedIndividualTreeHeightLabel.setText("");
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
            selectedIndividualTreeHeightLabel.setText("" + individual.getTree().treeHeight());
            selectedIndividualTreeSize.setText("" + individual.getTree().treeSize());
            selectedIndividualIsFeasible.setText("" + this.programProblem.isFeasible(individual));
            selectedIndividualNumOfFunc.setText("" + individual.getTree().numberOfFunctions());
            selectedIndividualNumOfTerm.setText("" + individual.getTree().numberOfTerminals());
        }
    }

    public void updateBestIndividual(){
        if(this.gpAlgorithm.getBest() == null) {
            bestIndividualImagePanel.setImage(null);
            bestIndividualID.setText("" );
            bestIndividualFitnes.setText("");
            bestIndividualTreeHeight.setText("");
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
            bestIndividualTreeHeight.setText("" + this.gpAlgorithm.getBest().getTree().treeHeight());
            bestIndividualTreeSize.setText("" + this.gpAlgorithm.getBest().getTree().treeSize());
            bestIndividualIsFeasible.setText("" + this.programProblem.isFeasible(this.gpAlgorithm.getBest()));
            bestIndividualNumOfFunc.setText("" + this.gpAlgorithm.getBest().getTree().numberOfFunctions());
            bestIndividualNumOfTerm.setText("" + this.gpAlgorithm.getBest().getTree().numberOfTerminals());
        }
    }

    private void initializeDataSymbolicRegression(String initialStateFile) {
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
            this.gpAlgorithm = GPAlgorithm2.deserializeAlgorithmState(initialStateFile);
            this.task = this.gpAlgorithm.getTask();
            this.programProblem = this.task.problem;
        }
        else {
            this.programProblem = new SymbolicRegressionProblem2(baseFunctionNodeTypes, baseTerminalNodeTypes, 3, 8, 200, new GPDepthBasedTreePruningOperator2(),
                    new GPTreeExpansionOperator2(), new GPRandomProgramSolution2(), evalData);
            this.task = new Task<>(programProblem, StopCriterion.EVALUATIONS, 10000, 0, 0);
            this.gpAlgorithm =  new DefaultGPAlgorithm2(100, 0.95, 0.025, 2, this.task, null);
        }
        this.gpAlgorithm.setDebug(true);
    }

     public void initializeDataBehaviourTree(String initialStateFile){
         List<Class<? extends Node>> baseFunctionNodeTypes = Arrays.asList(
                 Repeat.class,
                 Sequencer.class,
                 Selector.class,
                 Inverter.class
         );

         List<Class<? extends Node>> baseTerminalNodeTypes = Arrays.asList(
                 RayHitObject.class,
                 MoveForward.class,
                 MoveSide.class,
                 Rotate.class
         );

         if(initialStateFile != null){
             this.gpAlgorithm = GPAlgorithm2.deserializeAlgorithmState(initialStateFile);
             this.task = this.gpAlgorithm.getTask();
             this.programProblem = this.task.problem;
         }
         else {
             this.programProblem = new SoccerBTProblem2(baseFunctionNodeTypes, baseTerminalNodeTypes, 3, 8, 100, new GPDepthBasedTreePruningOperator2(),
                     new GPTreeExpansionOperator2(), new GPRandomProgramSolution2());
             this.task = new Task<>(this.programProblem, StopCriterion.EVALUATIONS, 10000, 0, 0);
             this.gpAlgorithm =  new DefaultGPAlgorithm2(100, 0.95, 0.025, 2, this.task, null);
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
    public static void main(String[] args) {
        HomeForm form = new HomeForm();
        form.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        form.setSize(1400, 1000);
        form.setVisible(true);
    }
}
