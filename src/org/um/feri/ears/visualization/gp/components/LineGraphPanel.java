package org.um.feri.ears.visualization.gp.components;

import javax.swing.*;
import java.awt.*;

public class LineGraphPanel extends JPanel {
    private double[] data;

    private String[] labels;
    private Color color;

    public LineGraphPanel(double[] data, Color color) {
        setData(data);
        this.color = Color.BLUE;
    }

    public void setData(double[] data) {
        this.data = data;
        this.labels = new String[data.length];
        for(int i = 0; i < data.length; i++){
            this.labels[i] = (i+1) + "";
        }
        repaint();
    }

    public void setColor(Color color) {
        this.color = color;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int widthSpace = 20;
        int width = getWidth() + widthSpace;
        int height = getHeight();
        int numPoints = this.data.length;
        int xInterval = width / (numPoints + 1);

        // Calculate the scaling factor based on the maximum and minimum values of the data
        double minValue = Double.POSITIVE_INFINITY;
        double maxValue = Double.NEGATIVE_INFINITY;
        for (double value : this.data) {
            minValue = Math.min(minValue, value);
            maxValue = Math.max(maxValue, value);
        }

        double range = maxValue - minValue;
        double scaleFactor = (height - 20) / range; // Subtract 20 to leave space for the labels

        g.setColor(this.color);
        for (int i = 0; i < numPoints - 1; i++) {
            int x1 = i * xInterval + widthSpace;
            int y1 = height - 20 - (int) ((data[i] - minValue) * scaleFactor);
            int x2 = (i + 1) * xInterval + widthSpace;
            int y2 = height - 20 - (int) ((data[i + 1] - minValue) * scaleFactor);
            g.drawLine(x1, y1, x2, y2);
        }

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        for (int i = 0; i < numPoints; i++) {
            int x = i * xInterval + widthSpace - 5;
            int y = height - 5; // Position the labels at the bottom
            String label = labels[i];
            g.drawString(label, x, y);
        }

        // Calculate y-axis label range
        int numLabels = 5; // Number of labels desired
        double labelStep = range / (numLabels - 1);
        double startValue = Math.floor(minValue / labelStep) * labelStep;

        // Draw y-axis labels
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        for (int i = 0; i < numLabels; i++) {
            double labelValue = startValue + i * labelStep;
            int y = height - 20 - (int) ((labelValue - minValue) * scaleFactor);
            String valueLabel = String.valueOf(Math.round(labelValue)); // Round to nearest whole number
            int labelWidth = g.getFontMetrics().stringWidth(valueLabel);
            int labelHeight = g.getFontMetrics().getHeight();
            g.drawString(valueLabel, 5, y + labelHeight / 2);
        }
    }
}