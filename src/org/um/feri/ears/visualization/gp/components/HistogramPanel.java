package org.um.feri.ears.visualization.gp.components;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class HistogramPanel extends JPanel {

    private int[] data;
    private String[] labels;
    private Color[] barColors;

    public HistogramPanel(int[] data, String[] labels) {
        this.data = data;
        this.labels = labels;
        this.barColors = generateRandomColors(data.length); // Generate random colors for each bar
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int maxValue = getMaxValue();
        int barWidth = getWidth() / data.length;

        for (int i = 0; i < data.length; i++) {
            int barHeight = (int) ((double) data[i] / maxValue * (getHeight() - 20)); // Deducting 20 for labels
            int x = i * barWidth;
            int y = getHeight() - barHeight - 20;
            g.setColor(barColors[i]);
            g.fillRect(x, y, barWidth, barHeight);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, barWidth, barHeight);
        }

        // Drawing labels below the histogram bars
        for (int i = 0; i < data.length; i++) {
            int x = i * barWidth + barWidth / 2 - g.getFontMetrics().stringWidth(labels[i]) / 2;
            int y = getHeight() - 5;
            g.drawString(labels[i], x, y);
        }

    }

    private int getMaxValue() {
        int maxValue = Integer.MIN_VALUE;
        for (int value : data) {
            if (value > maxValue) {
                maxValue = value;
            }
        }
        return maxValue;
    }

    // Method to generate random colors for bars
    private Color[] generateRandomColors(int numColors) {
        Random random = new Random();
        Color[] colors = new Color[numColors];
        for (int i = 0; i < numColors; i++) {
            colors[i] = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        }
        return colors;
    }

    /*public static void main(String[] args) {
        int[] data = {10, 20, 30, 40, 50, 60,40,45,45,67,56,55,35,45,43,23,95,75}; // Example data
        String[] labels = {"Label1", "Label2", "Label3", "Label4", "Label5", "Label6", "Label1", "Label2", "Label3", "Label4", "Label5", "Label6", "Label1", "Label2", "Label3", "Label4", "Label5", "Label6"}; // Example labels

        JFrame frame = new JFrame("HistogramPanel Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        HistogramPanel histogram = new HistogramPanel(data, labels);
        frame.add(histogram);

        frame.setVisible(true);
    }*/
}