package org.um.feri.ears.visualization.rating;

import com.itextpdf.awt.DefaultFontMapper;
import com.itextpdf.awt.PdfPrinterGraphics2D;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import com.itextpdf.text.Document;


import net.sf.epsgraphics.ColorMode;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.TranscodingHints;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.xmlgraphics.java2d.GraphicContext;
import org.apache.xmlgraphics.java2d.ps.EPSDocumentGraphics2D;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
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
import org.um.feri.ears.statistic.rating_system.Rating;
import org.um.feri.ears.statistic.rating_system.RatingType;
import net.sf.epsgraphics.EpsGraphics;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class RatingIntervalPlot extends ApplicationFrame {

    public enum FileType {
        PNG, JPEG, SVG, PDF, EPS
    }

    static class Range {
        int lower, upper;

        public Range(int lower, int upper) {
            this.lower = lower;
            this.upper = upper;
        }
    }

    static final int DEFAULT_WIDTH = 1000, DEFAULT_HEIGHT = 500, DEFAULT_RANGE_LOWER = 1000, DEFAULT_RANGE_UPPER = 2000;
    protected int width, height, rangeLower, rangeUpper;
    protected String categoryAxisName = "", numberAxisName = "Rating";
    protected int plotIndex = 0;
    final CategoryPlot plot;
    RatingType ratingType;
    final JFreeChart chart;

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

        chart = new JFreeChart(
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

    /**
     * Displays the rating interval chart with the default window dimensions. The range of the rating interval is calculated automatically.
     * @param players that will be displayed
     * @param ratingType used to calculate the rating
     * @param title of the chart
     */
    public static void displayChart(ArrayList<Player> players, RatingType ratingType, String title) {
        Range range = getRange(players, ratingType);
        displayChart(players, ratingType, title, DEFAULT_WIDTH, DEFAULT_HEIGHT, range.lower, range.upper);
    }

    /**
     * Displays the rating interval chart with the given dimensions. The default range of the rating interval is used.
     * @param players that will be displayed
     * @param ratingType used to calculate the rating
     * @param title of the chart
     * @param width of the window
     * @param height of the window
     */
    public static void displayChart(ArrayList<Player> players, RatingType ratingType, String title, int width, int height) {
        displayChart(players, ratingType, title, width, height, DEFAULT_RANGE_LOWER, DEFAULT_RANGE_UPPER);
    }
    /**
     * Displays the rating interval chart with the given dimensions and range of the rating interval.
     * @param players that will be displayed
     * @param ratingType used to calculate the rating
     * @param title of the chart
     * @param width of the window
     * @param height of the window
     * @param rangeLower lower bound of the rating interval
     * @param rangeUpper upper bound of the rating interval
     */
    public static void displayChart(ArrayList<Player> players, RatingType ratingType, String title, int width, int height, int rangeLower, int rangeUpper) {
        RatingIntervalPlot plot = new RatingIntervalPlot(players, ratingType, title, width, height, rangeLower, rangeUpper);
        plot.pack();
        RefineryUtilities.centerFrameOnScreen(plot);
        plot.setVisible(true);
    }

    /**
     * Saves the chart with the default window dimensions in the given file. The range of the rating interval is calculated automatically.
     * @param players that will be stored
     * @param ratingType used to calculate the rating
     * @param fileName of the chart
     * @param fileType of the chart
     */
    public static void saveChartToFile(ArrayList<Player> players, RatingType ratingType, String fileName, FileType fileType) {
        Range range = getRange(players, ratingType);
        saveChartToFile(players, ratingType, fileName, fileType, DEFAULT_WIDTH, DEFAULT_HEIGHT, range.lower, range.upper);
    }

    /**
     * Saves the chart the with the given dimensions in the given file. The range of the rating interval is calculated automatically.
     * @param players that will be stored
     * @param ratingType used to calculate the rating
     * @param fileName of the chart
     * @param fileType of the chart
     * @param width of the window
     * @param height of the window
     */
    public static void saveChartToFile(ArrayList<Player> players, RatingType ratingType, String fileName, FileType fileType, int width, int height) {
        saveChartToFile(players, ratingType, fileName, fileType, width, height, DEFAULT_RANGE_LOWER, DEFAULT_RANGE_UPPER);
    }

    /**
     * Saves the chart the with the given dimensions and range of the rating interval in the given file.
     * @param players that will be stored
     * @param ratingType used to calculate the rating
     * @param fileName of the chart
     * @param fileType of the chart
     * @param width of the window
     * @param height of the window
     * @param rangeLower lower bound of the rating interval
     * @param rangeUpper upper bound of the rating interval
     */
    public static void saveChartToFile(ArrayList<Player> players, RatingType ratingType, String fileName, FileType fileType, int width, int height, int rangeLower, int rangeUpper) {
        RatingIntervalPlot plot = new RatingIntervalPlot(players, ratingType, "Rating Interval", width, height, rangeLower, rangeUpper);
        String file = fileName + "." + fileType.toString().toLowerCase();
        try {
            switch (fileType) {
                case PNG:
                    ChartUtilities.saveChartAsPNG(new File(file), plot.chart, width, height);
                    break;
                case JPEG:
                    ChartUtilities.saveChartAsJPEG(new File(file), plot.chart, width, height);
                    break;
                case PDF: {
                    com.itextpdf.text.Document document = new com.itextpdf.text.Document(new com.itextpdf.text.Rectangle(width, height), 0, 0, 0, 0);
                    PdfWriter writer = PdfWriter.getInstance(document, Files.newOutputStream(Paths.get(file)));
                    document.open();
                    PdfContentByte contentByte = writer.getDirectContent();
                    PdfTemplate template = contentByte.createTemplate(width, height);
                    Graphics2D g2 = template.createGraphics(width, height, new DefaultFontMapper());
                    plot.chart.draw(g2, new Rectangle(width, height));
                    g2.dispose();
                    contentByte.addTemplate(template, 0, 0);
                    document.close();
                    break;
                }
                case SVG: {
                    DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
                    org.w3c.dom.Document document = domImpl.createDocument(null, "svg", null);
                    SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
                    plot.chart.draw(svgGenerator, new Rectangle2D.Double(0, 0, width, height), null);
                    try (Writer writer = new OutputStreamWriter(Files.newOutputStream(Paths.get(file)), StandardCharsets.UTF_8)) {
                        svgGenerator.stream(writer, true);
                    }
                    break;
                }
                case EPS: {
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        EpsGraphics g = new EpsGraphics(fileName, fos, 0, 0,
                                width, height, ColorMode.COLOR_RGB);
                        plot.chart.draw(g, new Rectangle2D.Double(0, 0, width, height), null);

                        g.flush();
                        g.close();
                    }
                    break;
                }
                default:
                    ChartUtilities.saveChartAsPNG(new File(file), plot.chart, width, height);
                    break;
            }
        } catch (Exception e) {
            System.out.println("Error saving chart to file: " + e.getMessage());
        }
    }
    /**
     * Gets upper and lower of the rating interval of all players
     * @param players
     * @param ratingType
     * @return
     */
    private static Range getRange(ArrayList<Player> players, RatingType ratingType) {

        int rangeLower = Integer.MAX_VALUE, rangeUpper = Integer.MIN_VALUE;
        for (Player p : players) {
            Rating rating = p.getRating(ratingType);

            if (rangeLower > rating.getRatingIntervalLower()) {
                rangeLower = (int) rating.getRatingIntervalLower();
            }
            if (rangeUpper < rating.getRatingIntervalUpper()) {
                rangeUpper = (int) rating.getRatingIntervalUpper();
            }
        }
        rangeLower -= 1;

        if (ratingType == RatingType.GLICKO2) {
            if (rangeLower < 0) {
                rangeLower = 0;
            }
        }

        if (rangeUpper < rangeLower)
            rangeUpper = rangeLower;

        rangeUpper += 1;

        return new Range(rangeLower, rangeUpper);
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
        int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
        for (Player p : players) {
            Rating rating = p.getRating(ratingType);

            dataset.addValue(rating.getRatingIntervalLower(), "1", p.getId());
            dataset.addValue(rating.getRating(), "2", p.getId());
            dataset.addValue(rating.getRatingIntervalUpper(), "3", p.getId());

            if (min > rating.getRatingIntervalLower()) {
                min = (int) rating.getRatingIntervalLower();
            }
            if (max < rating.getRatingIntervalUpper()) {
                max = (int) rating.getRatingIntervalUpper();
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
