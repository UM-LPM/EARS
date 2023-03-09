package org.um.feri.ears.problems.gp;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class GraphPrinter {

    private StringBuilder graphBuilder = new StringBuilder();
    private String filePrefix;

    public GraphPrinter() {}

    /**
     * @param filePrefix Only put filename prefix E.g. File1, File2, MyNewFile, myfile etc. It will use the filePrefix to create output dot file and png file with same name.
     */
    public GraphPrinter(String filePrefix) {
        this.filePrefix = filePrefix;
    }

    /**
     * @param line  line contains a valid dot file graphviz text
     */
    public void addln(String line) {
        graphBuilder.append(line).append("\n");
    }

    public void add(String text) {
        graphBuilder.append(text);
    }

    public void addnewln() {
        graphBuilder.append("\n");
    }

    /**
     * Creates an output dot file and uses that to create graphviz png output using following command
     * dot -Tpng <filePrefix>.dot -o <filePrefix>.png
     * If you want to change the certain format, change below.
     */
    public void print() {
        try {

            if (filePrefix == null || filePrefix.isEmpty()) {
                filePrefix = "output";
            }

            graphBuilder.insert(0, "digraph G {").append("\n");
            graphBuilder.append("}").append("\n");
            writeTextToFile(filePrefix + ".dot", graphBuilder.toString());

            StringBuilder command = new StringBuilder();
            command.append("dot -Tpng ").    // output type
                    append(filePrefix).append(".dot ").   // input dot file
                    append("-o ").append(filePrefix).append(".png");  // output image

            executeCommand(command.toString());

            displayImage(filePrefix + ".png");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void executeCommand(String command) throws Exception {
        Process process = Runtime.getRuntime().exec(command);
        process.waitFor();
    }

    private void writeTextToFile(String fileName, String text) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(fileName);
        outputStream.write(text.getBytes());
        outputStream.close();
    }

    public void displayImage(String filename) throws IOException
    {
        try{
            BufferedImage img= ImageIO.read(new File(filename));
            ImageIcon icon=new ImageIcon(img);
            JFrame frame=new JFrame();
            frame.setLayout(new FlowLayout());
            frame.setSize(600,600);
            JLabel lbl=new JLabel();
            lbl.setIcon(icon);
            frame.add(lbl);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        catch (Exception ex) {
            System.err.println("ERROR: " + ex.toString());
        }
    }
}