package org.um.feri.ears.visualization.rating;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.chart.renderer.category.MinMaxCategoryRenderer;
import org.jfree.data.category.CategoryDataset;

public class MyMinMaxCategoryRenderer extends MinMaxCategoryRenderer {

    @Override
    public void drawItem(Graphics2D g2, CategoryItemRendererState state,
                         Rectangle2D dataArea, CategoryPlot plot, CategoryAxis domainAxis,
                         ValueAxis rangeAxis, CategoryDataset dataset, int row, int column,
                         int pass) {
        super.drawItem(g2, state, dataArea, plot, domainAxis, rangeAxis, dataset, row, column, pass);

        //Draw label
        if (dataset.getRowCount() - 1 == row) { //last row

            Number value = dataset.getValue(row, column);
            double x1 = domainAxis.getCategoryMiddle(column, getColumnCount(),
                    dataArea, plot.getDomainAxisEdge());
            double y1 = rangeAxis.valueToJava2D(value.doubleValue(), dataArea,
                    plot.getRangeAxisEdge());

            g2.setFont(new Font("SansSerif", Font.BOLD, 14));
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            String name = dataset.getColumnKey(column).toString();
            int width = g2.getFontMetrics().stringWidth(name);


            g2.drawString(name, (int) y1 - width, (int) x1 - 12);
        }
    }
}
