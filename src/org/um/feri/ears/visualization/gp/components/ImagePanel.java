package org.um.feri.ears.visualization.gp.components;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {
    private Image image;

    public ImagePanel(String imagePath) {
        image = new ImageIcon(imagePath).getImage();
    }

    public void setImage(String imagePath) {
        image = new ImageIcon(imagePath).getImage();
        repaint();
    }

    public Image getImage() {
        return image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the image on the panel
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            //image.paintIcon(this, g, x, y);
        }
    }
}