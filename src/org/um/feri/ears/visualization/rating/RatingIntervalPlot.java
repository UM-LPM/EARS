package org.um.feri.ears.visualization.rating;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RefineryUtilities;
import org.um.feri.ears.statistic.rating_system.Player;
import org.um.feri.ears.statistic.rating_system.RatingType;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class RatingIntervalPlot extends ApplicationFrame {

    static final int DEFAULT_WIDTH = 1000, DEFAULT_HEIGHT = 500, DEFAULT_RANGE_LOWER = 1000, DEFAULT_RANGE_UPPER = 2000;
    protected int width, height, rangeLower, rangeUpper;
    protected String categoryAxisName = "", numberAxisName = "Rating";
    protected int plotIndex = 0;
    final CategoryPlot plot;
    RatingType ratingType;


    public RatingIntervalPlot(String title, ArrayList<Player> players) {
        this(players, RatingType.GLICKO2, title, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_RANGE_LOWER, DEFAULT_RANGE_UPPER);
    }

    public RatingIntervalPlot(ArrayList<Player> players, RatingType ratingType, String title, int width, int height, int rangeLower, int rangeUpper) {
        super(title);
        this.ratingType = ratingType;
        this.width = width;
        this.height = height;
        this.rangeLower = rangeLower;
        this.rangeUpper = rangeUpper;

        DefaultCategoryDataset dataset = generateDataSet(players);


        final CategoryAxis xAxis = new CategoryAxis(categoryAxisName);
        final NumberAxis yAxis = new NumberAxis(numberAxisName);
        yAxis.setRange(this.rangeLower, this.rangeUpper);
        xAxis.setTickLabelsVisible(false);
        DecimalFormat df = new DecimalFormat("0"); // Override the decimal format to get integer numbers on the axis (1.800 -> 1800)
        df.setMaximumFractionDigits(1);
        yAxis.setNumberFormatOverride(df);
        yAxis.setAutoRangeIncludesZero(false);

        MyMinMaxCategoryRenderer renderer = createRenderer(Color.black);

        plot = new CategoryPlot(dataset, xAxis, yAxis, renderer);
        plot.setOrientation(PlotOrientation.HORIZONTAL);
        plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
        plotIndex++;

        final JFreeChart chart = new JFreeChart(
                title,
                new Font("SansSerif", Font.BOLD, 16),
                plot,
                false
        );

        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(width, height));
        setContentPane(chartPanel);
        chart.setBackgroundPaint(Color.white);
        chart.setPadding(new RectangleInsets(10, 10, 10, 10)); // Fix: tick label cut off
    }

    public static void displayChart(ArrayList<Player> players, RatingType ratingType, String title, int width, int height) {
        displayChart(players, ratingType, title, width, height, DEFAULT_RANGE_LOWER, DEFAULT_RANGE_UPPER);
    }

    public static void displayChart(ArrayList<Player> players, RatingType ratingType, String title) {

        double rating, RD;
        int rangeLower = Integer.MAX_VALUE, rangeUpper = Integer.MIN_VALUE;
        for (Player p : players) {
            rating = p.getRating(ratingType).getRating();
            RD = p.getRating(ratingType).getRatingDeviation();

            if (rangeLower > rating - 2 * RD) {
                rangeLower = (int) (rating - 2 * RD);
            }
            if (rangeUpper < rating + 2 * RD) {
                rangeUpper = (int) (rating + 2 * RD);
            }
        }
        rangeLower -= 1;

        if(ratingType == RatingType.GLICKO2) {
            if (rangeLower < 0) {
                rangeLower = 0;
            }
        }

        if(rangeUpper < rangeLower)
            rangeUpper = rangeLower;

        rangeUpper += 1;

        displayChart(players, ratingType, title, DEFAULT_WIDTH, DEFAULT_HEIGHT, rangeLower, rangeUpper);
    }

    public static void displayChart(ArrayList<Player> players, RatingType type, String title, int width, int height, int rangeLower, int rangeUpper) {
        RatingIntervalPlot plot = new RatingIntervalPlot(players, type, title, width, height, rangeLower, rangeUpper);
        plot.pack();
        RefineryUtilities.centerFrameOnScreen(plot);
        plot.setVisible(true);
    }

    private MyMinMaxCategoryRenderer createRenderer(Color color) {

        MyMinMaxCategoryRenderer renderer = new MyMinMaxCategoryRenderer();
        renderer.setBaseItemLabelsVisible(true);
        renderer.setSeriesItemLabelsVisible(0, true);
        renderer.setSeriesItemLabelsVisible(1, true);
        renderer.setSeriesItemLabelsVisible(2, true);

        renderer.setGroupPaint(color);

        renderer.setSeriesPaint(0, new Color(0, 0, 0, 0)); // invisible
        renderer.setSeriesPaint(1, color);
        renderer.setSeriesPaint(2, new Color(0, 0, 0, 0)); // invisible

        renderer.setMinIcon(getIcon(new Line2D.Double(0, -6, 0, 6), true, true));
        renderer.setMaxIcon(getIcon(new Line2D.Double(0, -6, 0, 6), true, true));
        renderer.setObjectIcon(getIcon(new Line2D.Double(0, -4, 0, 4), true, true));
        return renderer;
    }

    private DefaultCategoryDataset generateDataSet(ArrayList<Player> players) {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        double rating, RD;
        int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
        for (Player p : players) {
            rating = p.getRating(ratingType).getRating();
            RD = p.getRating(ratingType).getRatingDeviation();

            dataset.addValue(rating - 2 * RD, "1", p.getId());
            dataset.addValue(rating, "2", p.getId());
            dataset.addValue(rating + 2 * RD, "3", p.getId());

            if (min > rating - 2 * RD) {
                min = (int) (rating - 2 * RD);
            }
            if (max < rating + 2 * RD) {
                max = (int) (rating + 2 * RD);
            }
        }

        //Fix range if out of bounds
        if (rangeLower > min) {
            rangeLower = Math.max(min - 1, 0);
        }

        if (rangeUpper < max) {
            rangeUpper = max;
        }


        return dataset;
    }

    public void addDataSet(ArrayList<Player> players, Color c) {
        DefaultCategoryDataset dataset = generateDataSet(players);
        MyMinMaxCategoryRenderer renderer = createRenderer(c);

        plot.setDataset(plotIndex, dataset);
        plot.setRenderer(plotIndex++, renderer);
    }

    private Icon getIcon(Shape shape, final boolean fill,
                         final boolean outline) {
        final int width = shape.getBounds().width;
        final int height = shape.getBounds().height;
        final GeneralPath path = new GeneralPath(shape);
        return new Icon() {
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g;
                path.transform(AffineTransform.getTranslateInstance(x, y));
                if (fill) {
                    //g2.setPaint(Color.green);
                    g2.fill(path);
                }
                if (outline) {
                    g2.draw(path);
                }
                path.transform(AffineTransform.getTranslateInstance(-x, -y));
            }

            public int getIconWidth() {
                return width;
            }

            public int getIconHeight() {
                return height;
            }
        };
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getRangeLower() {
        return rangeLower;
    }

    public void setRangeLower(int rangeLower) {
        this.rangeLower = rangeLower;
    }

    public int getRangeUpper() {
        return rangeUpper;
    }

    public void setRangeUpper(int rangeUpper) {
        this.rangeUpper = rangeUpper;
    }

    public String getCategoryAxisName() {
        return categoryAxisName;
    }

    public void setCategoryAxisName(String categoryAxisName) {
        this.categoryAxisName = categoryAxisName;
    }

    public String getNumberAxisName() {
        return numberAxisName;
    }

    public void setNumberAxisName(String numberAxisName) {
        this.numberAxisName = numberAxisName;
    }
}
