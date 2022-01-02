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
import org.um.feri.ears.statistic.glicko2.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class RatingIntervalPlot extends ApplicationFrame {

	protected int width, height , rangeLower = 1000, rangeUpper = 2000;
	protected String categoryAxisName = "",numberAxisName = "Rating";
	protected int plotIndex = 0;
	final CategoryPlot plot;


	public RatingIntervalPlot(String title, ArrayList<Player> players) {
		this(players, title,1000, 500);
	}

	public RatingIntervalPlot(ArrayList<Player> players, String title, int width, int height) {
		super(title);
		this.width = width;
		this.height = height;

		DefaultCategoryDataset dataset = generateDataSet(players);


		final CategoryAxis xAxis = new CategoryAxis(categoryAxisName);
		final NumberAxis yAxis = new NumberAxis(numberAxisName);
		yAxis.setRange(rangeLower, rangeUpper);
        xAxis.setTickLabelsVisible(false);
		DecimalFormat df = new DecimalFormat("0"); // Override the decimal format to get integer numbers on the axis (1.800 -> 1800)
		yAxis.setNumberFormatOverride(df);
		yAxis.setAutoRangeIncludesZero(false);

		MyMinMaxCategoryRenderer renderer = createRenderer(Color.black);


		plot = new CategoryPlot(dataset, xAxis, yAxis, renderer);
		plot.setOrientation(PlotOrientation.HORIZONTAL);
		plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
		plotIndex++;

		/*
		DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
		dataset2.addValue(1604, "1", "PESAII");
        dataset2.addValue(1704, "2", "PESAII");
        dataset2.addValue(1804, "3", "PESAII");
        dataset2.addValue(1512, "1", "NSGAII");
        dataset2.addValue(1612, "2", "NSGAII");
        dataset2.addValue(1712, "3", "NSGAII");
        
		MyMinMaxCategoryRenderer renderer2 = createRenderer(Color.green);
		
		plot.setDataset(plotIndex, dataset2);
		plot.setRenderer(plotIndex++, renderer2);*/
		
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

	public static void displayChart(ArrayList<Player> list, String title, int width, int height) {
		RatingIntervalPlot plot = new RatingIntervalPlot(list, title, width, height);
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
		
		renderer.setSeriesPaint(0, new Color(0,0,0,0)); // invisible
		renderer.setSeriesPaint(1, color);
		renderer.setSeriesPaint(2, new Color(0,0,0,0)); // invisible

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
			rating = p.getRatingData().getRating();
			RD = p.getRatingData().getRD();

			dataset.addValue(rating - 2 * RD, "1", p.getPlayerId());
			dataset.addValue(rating,          "2", p.getPlayerId());
			dataset.addValue(rating + 2 * RD, "3", p.getPlayerId());

			if(min > rating - 2 * RD)
			{
				min = (int) (rating - 2 * RD);
			}
			if(max < rating + 2 * RD)
			{
				max = (int) (rating + 2 * RD);
			}
		}
		
		//Fix range if out of bounds
		if(rangeLower > min)
		{
			rangeLower = min - (min % 100);
		}
		
		if(rangeUpper < max)
		{
			rangeUpper = max + (100 - max % 100);
		}
		

		return dataset;
	}
	
	public void addDataSet(ArrayList<Player> players, Color c)
	{
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
