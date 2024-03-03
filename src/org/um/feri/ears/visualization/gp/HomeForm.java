package org.um.feri.ears.visualization.gp;

import org.um.feri.ears.algorithms.GPAlgorithm;
import org.um.feri.ears.algorithms.gp.DefaultGPAlgorithm;
import org.um.feri.ears.individual.generations.gp.GPRandomProgramSolution;
import org.um.feri.ears.individual.representations.gp.MathOp;
import org.um.feri.ears.individual.representations.gp.Op;
import org.um.feri.ears.individual.representations.gp.OperationType;
import org.um.feri.ears.individual.representations.gp.Target;
import org.um.feri.ears.operators.gp.GPDepthBasedTreePruningOperator;
import org.um.feri.ears.operators.gp.GPTreeExpansionOperator;
import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.gp.ProgramProblem;
import org.um.feri.ears.problems.gp.ProgramSolution;
import org.um.feri.ears.problems.gp.SymbolicRegressionProblem;
import org.um.feri.ears.problems.gp.Utils;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.visualization.gp.components.GraphPanel;
import org.um.feri.ears.visualization.gp.components.ImagePanel;
import org.um.feri.ears.visualization.gp.components.LineGraphPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
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

    private SymbolicRegressionProblem sgp2;
    private Task<ProgramSolution<Double>, ProgramProblem<Double>> symbolicRegressionTask;
    private GPAlgorithm gpAlgorithm;
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

    private String lastUuid;
    public HomeForm() {
        super("EARS GP (Debug)");
        setContentPane(contentPane);

        imgPathPrefix = "src/org/um/feri/ears/visualization/gp/images/";
        clearImages();
        // Initialize data for gp algorithm
        initializeData();
        updateUI(false);
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
        genNumLabel.setText("" + symbolicRegressionTask.getNumberOfIterations());
        fitnesEvalNumLabel.setText("" + symbolicRegressionTask.getNumberOfEvaluations());

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
                    final int indexFinal =  index + (this.symbolicRegressionTask.getNumberOfIterations() * this.gpAlgorithm.getPopulation().size());
                    ProgramSolution<Double> individual = this.gpAlgorithm.getPopulation().get(index);
                    String file = individual.getProgram().displayTree(imgPathPrefix + "tree" + String.valueOf(indexFinal), false);
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

    public void individualImagePanelMouseClicked(java.awt.event.MouseEvent evt, ProgramSolution<Double> individual, int index) {
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
            String file = individual.getProgram().displayTree(imgPathPrefix + "tree" + String.valueOf(index), false);
            selectedIndividualImagePanel.setImage(file);
            selectedIndividualID.setText("" + index);
            selectedIndividualFitnes.setText("" + Util.roundDouble(individual.getEval(), 2));
            selectedIndividualTreeHeightLabel.setText("" + individual.getProgram().treeHeight());
            selectedIndividualTreeSize.setText("" + individual.getProgram().treeSize());
            selectedIndividualIsFeasible.setText("" + this.sgp2.isFeasible(individual));
            selectedIndividualNumOfFunc.setText("" + individual.getProgram().numberOfFunctions());
            selectedIndividualNumOfTerm.setText("" + individual.getProgram().numberOfTerminals());
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
            String file = this.gpAlgorithm.getBest().getProgram().displayTree(imgPathPrefix + "tree" + this.lastUuid, false);
            bestIndividualImagePanel.setImage(file);
            bestIndividualID.setText("" + this.gpAlgorithm.getBest().getID());
            bestIndividualFitnes.setText("" + Util.roundDouble(this.gpAlgorithm.getBest().getEval(), 2));
            bestIndividualTreeHeight.setText("" + this.gpAlgorithm.getBest().getProgram().treeHeight());
            bestIndividualTreeSize.setText("" + this.gpAlgorithm.getBest().getProgram().treeSize());
            bestIndividualIsFeasible.setText("" + this.sgp2.isFeasible(this.gpAlgorithm.getBest()));
            bestIndividualNumOfFunc.setText("" + this.gpAlgorithm.getBest().getProgram().numberOfFunctions());
            bestIndividualNumOfTerm.setText("" + this.gpAlgorithm.getBest().getProgram().numberOfTerminals());
        }
    }

    private void initializeData() {
        // y=x^3 + 2x^2 + 8x + 12
        sgp2 = new SymbolicRegressionProblem(
                Utils.list(MathOp.ADD, MathOp.SUB, MathOp.MUL, MathOp.DIV, MathOp.CONST),
                Utils.list(Op.define("x", OperationType.VARIABLE)),
                3,
                8,
                200,
                new GPDepthBasedTreePruningOperator<>(),
                new GPTreeExpansionOperator<>(),
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
                new GPRandomProgramSolution<>()
        );

        this.symbolicRegressionTask = new Task<>(sgp2, StopCriterion.EVALUATIONS, 10000, 0, 0);
        this.gpAlgorithm = new DefaultGPAlgorithm(100, 0.95, 0.025, 2, this.symbolicRegressionTask);
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
