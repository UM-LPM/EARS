package org.um.feri.ears.visualization.gp.components;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class HorizontalHistogramPanel extends JPanel {

    private Map<String, Double> data;
    private Color[] barColors;

    public HorizontalHistogramPanel() {
        this.data = new HashMap<>();
    }

    public HorizontalHistogramPanel(Map<String, Double> data) {
        this.data = data;
        this.barColors = generateRandomColors(data.size()); // Generate random colors for each bar
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(data.isEmpty())
            return;

        double maxValue = getMaxValue();
        int barHeight = (getHeight()) / data.size(); // Deducting space for x-axis
        int labelXOffset = 5; // Offset for labels

        // Draw x-axis values
        int i = 0;
        for (Map.Entry<String, Double> entry : data.entrySet()) {
            double value = Math.abs(entry.getValue());
            int barWidth = (int) (value / maxValue * (getWidth() - 50));
            int x = labelXOffset; // Start from the left side
            int y = i * barHeight;
            g.setColor(barColors[i]); // Set color for each bar
            g.fillRect(x, y, barWidth, barHeight);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, barWidth, barHeight);

            // Drawing labels beside the histogram bars
            g.setColor(Color.WHITE);
            int labelY = i * barHeight + barHeight / 2 + g.getFontMetrics().getHeight() / 2;
            g.drawString(entry.getKey(), x + 5, labelY);

            // Drawing histogram sizes on the right side
            g.setColor(Color.BLACK);
            String valueString = String.valueOf(entry.getValue());
            int valueStringWidth = g.getFontMetrics().stringWidth(valueString);
            g.drawString(valueString, x + barWidth + 5, labelY);

            i++;
        }
    }

    private double getMaxValue() {
        double maxValue = Float.MIN_VALUE;
        for (double value : data.values()) {
            if (Math.abs(value) > maxValue) {
                maxValue = Math.abs(value);
            }
        }
        return maxValue;
    }

    // Method to generate random colors for bars
    private Color[] generateRandomColors(int numColors) {
        Random random = new Random();
        Color[] colors = new Color[numColors];
        for (int i = 0; i < numColors; i++) {
            colors[i] = new Color(random.nextInt(200), random.nextInt(200), random.nextInt(200));
        }
        return colors;
    }

    public void setData(Map<String, Double> data) {
        this.data = data;
        this.barColors = generateRandomColors(data.size());
        repaint();
    }

    /*public static void main(String[] args) {
        Map<String, Float> data = new HashMap<>();
        data.put("Label1", 10f);
        data.put("Label2", 20f);
        data.put("Label3", 30f);
        data.put("Label4", 40f);
        data.put("Label5", 50f);
        data.put("Label6", 60f);

        JFrame frame = new JFrame("Horizontal Histogram Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        HorizontalHistogramPanel histogram = new HorizontalHistogramPanel(data);
        frame.add(histogram);

        frame.setVisible(true);
    }*/

}
