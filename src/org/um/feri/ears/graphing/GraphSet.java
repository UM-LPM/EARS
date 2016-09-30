package org.um.feri.ears.graphing;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.graphing.data.GraphDataSet;
import org.um.feri.ears.graphing.data.RecordedData;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.moo.MOSolutionBase;

import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.NamedPlotColor;
import com.panayotis.gnuplot.style.PlotColor;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.RgbPlotColor;


public class GraphSet
{
	// Vars:
	protected ArrayList<GraphEARS> graphs;
	protected GraphDataSet data;
	// SubVars:
	protected PlotColorScheme plotColorScheme = PlotColorScheme.Colored;
	protected Dimension CanvasSize = null;
	protected double zoomLevel = 1.0;
	protected Integer AnimationDuration = null;
	protected Long EvaluationsPerFrame = null;
	protected Integer FramesPerSecond = null;
	protected Boolean outputAutomatic = null;
	protected Boolean overwrite = null;
	// Constants:
	protected final static NamedPlotColor[] MULTI_PLOT_COLORS = new NamedPlotColor[]{NamedPlotColor.RED,NamedPlotColor.BLUE,NamedPlotColor.GREEN,NamedPlotColor.VIOLET,
		NamedPlotColor.BROWN, NamedPlotColor.YELLOW, NamedPlotColor.MAGENTA, NamedPlotColor.BLACK, NamedPlotColor.PINK,NamedPlotColor.BEIGE, NamedPlotColor.CYAN, NamedPlotColor.PURPLE};
																
	
	
	// Constructor:
	/**
	 * Constructor for graph set with no contents.
	 */
	public GraphSet()
	{
		this.graphs = new ArrayList<GraphEARS>();
	}
	/**
	 * Constructor for graph set containing graphs made with passed data set.
	 * @param data Data set containing data subsets for all graphs.
	 */
	public GraphSet(GraphDataSet data)
	{
		this(data, 0);
	}
	/**
	 * Constructor for graph set containing graphs made with passed data set.
	 * Filters creation of graphs, static, animated, or both.
	 * @param data Data set containing data subsets for all graphs.
	 * @param type Type of graphs to create. (0-All, 1-Static, 2-Animated)
	 */
	public GraphSet(GraphDataSet data, int type)
	{
		this(data.getSubsets(), type);
	}

	public GraphSet(GraphDataSet data, int type, int evalsPerStep)
	{
		this(data.getSubsets(), type, evalsPerStep);
	}
	/**
	 * Constructor for graph set containing graphs made with passed data set.
	 * @param data Data set containing data subsets for all graphs.
	 */
	public GraphSet(RecordedData[][] allData)
	{
		this(allData, 0);
	}
	/**
	 * Constructor for graph set containing graphs made with passed data set.
	 * Filters creation of graphs, static, animated, or both.
	 * @param data Data set containing data subsets for all graphs.
	 * @param type Type of graphs to create. (0-All, 1-Static, 2-Animated)
	 */
	public GraphSet(RecordedData[][] allData, int type)
	{
		this(allData, type, GraphEARSStatic.DEFAULT_EVALUATIONS_PER_STEP);
	}
	public GraphSet(RecordedData[][] allData, int type, int evalsPerStep)
	{
		this.data = new GraphDataSet(allData);
		this.graphs = new ArrayList<GraphEARS>();
		for (int i=0; i<allData.length; i++)
		{
			if (allData[i][0].algorithm instanceof Algorithm)
			{
				if (type == 0 || type == 1)
					graphs.add(new GraphEARSStatic(allData[i], evalsPerStep));
				if (type == 0 || type == 2)
				{
					// no supported graphs
				}
			}
			else
			{
				System.err.println("Wrong algorithm type");
			}
		}
	}
	/**
	 * Constructor for graph set containing graphs made with passed data set.
	 * Filters creation of graphs, static, animated, or both. Limits to graphs for specific axis combination.
	 * @param data Data set containing data subsets for all graphs.
	 * @param type Type of graphs to create. (0-All, 1-Static, 2-Animated)
	 * @param objIndexX Objective index that should be presented on graphs X axis.
	 * @param objIndexY Objective index that should be presented on graphs Y axis.
	 */
	public GraphSet(RecordedData[][] allData, int type, int objIndexX, int objIndexY)
	{
		this.data = new GraphDataSet(allData);
		this.graphs = new ArrayList<GraphEARS>();
		for (int i=0; i<allData.length; i++)
		{
			if (allData[i][0].algorithm instanceof Algorithm)
			{
				if (type == 0 || type == 1)
					graphs.add(new GraphEARSStatic(allData[i]));
				if (type == 0 || type == 2)
				{
					// no supported graphs
				}
			}
			else if (allData[i][0].algorithm instanceof MOAlgorithm)
			{
				for(int i1=0; i1<1; i1++)
				if (type == 0 || type == 1)
					graphs.add(new GraphEARSStatic(allData[i], objIndexX, objIndexY));
				if (type == 0 || type == 2)
				{
					graphs.add(new GraphEARSAnimated(allData[i], objIndexX, objIndexY));
				}
					
			}
		}
	}
	
	
	
	// Get/Set:
	/**
	 * Retrieve graph from set with specific index.
	 * @param index Index of graph.
	 * @return Single GraphEARS object.
	 */
	public GraphEARS getGraph(int index)
	{
		return graphs.get(index);
	}
	public GraphEARS[] getGraphs()
	{
		return graphs.toArray(new GraphEARS[0]);
	}
	public int getGraphCount()
	{
		return graphs.size();
	}
	/**
	 * Retrieve data that is/will be displayed on a selected graph.
	 * @param index Index of graph in set.
	 * @return Array of recorded data samples.
	 */
	public RecordedData[] getGraphDataSet(int index)
	{
		return data.getSubsets()[index];
	}
	
	
	// Add:
	public void add(GraphEARS grph)
	{
		this.data.add(grph.data);
		this.graphs.add(grph);
		if (this.CanvasSize != null)
			grph.setCanvasSize(CanvasSize);
		if (this.AnimationDuration != null)
			if (grph instanceof GraphEARSAnimated)
				((GraphEARSAnimated)grph).setAnimationDuration(AnimationDuration);
		if (this.EvaluationsPerFrame != null)
			if (grph instanceof GraphEARSAnimated)
				((GraphEARSAnimated)grph).setAnimationEvaluationsPerFrame(EvaluationsPerFrame);
		if (this.FramesPerSecond != null)
			if (grph instanceof GraphEARSAnimated)
				((GraphEARSAnimated)grph).setAnimationFramesPerSecond(FramesPerSecond);
		if (this.outputAutomatic != null)
			grph.setOutputFileAutomatic();
	}
	public void add(Collection<GraphEARS> grphs)
	{
		for (GraphEARS g : grphs)
		{
			this.graphs.add(g);
		}
	}
	public void add(GraphDataSet other)
	{
		GraphSet t = new GraphSet(other);
		GraphEARS[] g = t.getGraphs();
		for (int i=0; i<g.length; i++)
		{
			if (this.CanvasSize != null)
				g[i].setCanvasSize(CanvasSize);
			if (this.outputAutomatic != null)
				g[i].setOutputFileAutomatic();
			if (this.AnimationDuration != null)
				if (g[i] instanceof GraphEARSAnimated)
					((GraphEARSAnimated)g[i]).setAnimationDuration(AnimationDuration);
			if (this.EvaluationsPerFrame != null)
				if (g[i] instanceof GraphEARSAnimated)
					((GraphEARSAnimated)g[i]).setAnimationEvaluationsPerFrame(EvaluationsPerFrame);
			if (this.FramesPerSecond != null)
				if (g[i] instanceof GraphEARSAnimated)
					((GraphEARSAnimated)g[i]).setAnimationFramesPerSecond(FramesPerSecond);
			this.graphs.add(g[i]);
		}
	}
	
	
	// Sub get/set:
	public Dimension getCanvasSize()
	{
		if (this.CanvasSize != null)
			return new Dimension(CanvasSize);
		else
			return new Dimension(GraphEARSStatic.DEFAULT_CANVAS_WIDTH,GraphEARSStatic.DEFAULT_CANVAS_HEIGHT);
	}
	public int getCanvasWidth()
	{
		if (this.CanvasSize != null)
			return CanvasSize.width;
		else
			return GraphEARSStatic.DEFAULT_CANVAS_WIDTH;
	}
	public int getCanvasHeight()
	{
		if (this.CanvasSize != null)
			return CanvasSize.height;
		else 
			return GraphEARSStatic.DEFAULT_CANVAS_HEIGHT;
	}
	public void setCanvasSize(Dimension canvasSize)
	{
		CanvasSize = canvasSize;
		if (canvasSize == null)
			canvasSize = new Dimension(GraphEARSStatic.DEFAULT_CANVAS_WIDTH,GraphEARSStatic.DEFAULT_CANVAS_HEIGHT);
		int l = graphs.size();
		for (int i=0; i<l; i++)
		{
			graphs.get(i).setCanvasSize(canvasSize);
		}
	}
	public void setCanvasSize(int width, int height)
	{
		this.setCanvasSize(new Dimension(width,height));
	}
	public RecordedData[] getData(int index)
	{
		return data.getSubsets()[index];
	}
	public int getAnimationLength()
	{
		return AnimationDuration;
	}
	public void setAnimationDuration(int ms)
	{
		AnimationDuration = ms;
		int l = graphs.size();
		for (int i=0; i<l; i++)
		{
			GraphEARS tmp = graphs.get(i);
			if (tmp instanceof GraphEARSAnimated)
				((GraphEARSAnimated)tmp).setAnimationDuration(ms);
		}
	}
	public long getAnimationEvaluationsPerFrame()
	{
		return EvaluationsPerFrame;
	}
	public void setAnimationEvaluationsPerFrame(long evalCount)
	{
		EvaluationsPerFrame = evalCount;
		int l = graphs.size();
		for (int i=0; i<l; i++)
		{
			GraphEARS tmp = graphs.get(i);
			if (tmp instanceof GraphEARSAnimated)
				((GraphEARSAnimated)tmp).setAnimationEvaluationsPerFrame(evalCount);
		}
	}
	public int getAnimationFramesPerSecond()
	{
		return FramesPerSecond;
	}
	public void setAnimationFramesPerSecond(int fps)
	{
		FramesPerSecond = fps;
		int l = graphs.size();
		for (int i=0; i<l; i++)
		{
			GraphEARS tmp = graphs.get(i);
			if (tmp instanceof GraphEARSAnimated)
				((GraphEARSAnimated)tmp).setAnimationFramesPerSecond(fps);
		}
	}
	
	
	// Set output file:
	public void setOutputFilesAutomatic()
	{
		this.outputAutomatic = true;
		int l = graphs.size();
		for (int i=0; i<l; i++)
		{
			graphs.get(i).setOutputFileAutomatic();
		}
	}
	public void setOutputFilesAutomatic(boolean overwrite)
	{
		this.outputAutomatic = true;
		this.overwrite = overwrite;
		int l = graphs.size();
		for (int i=0; i<l; i++)
		{
			graphs.get(i).setOutputFileAutomatic(overwrite);
		}
	}
	public void setOutputFile(int index, String filename)
	{
		graphs.get(index).setOutputFile(filename);
	}
	public void setOutputFile(int index, String filename, boolean overwrite)
	{
		graphs.get(index).setOutputFile(filename, overwrite);
	}
	public String getOutputFile(int index)
	{
		return graphs.get(index).getOutputFile();
	}
	public void setTitle(int index, String title)
	{
		graphs.get(index).Title = title;
	}
	
	
	// Add plot:
	public void Plot(PlotType gt)
	{
		int l = graphs.size();
		for (int i=0; i<l; i++)
		{
			GraphEARS tmp = graphs.get(i);
			if (tmp.data==null)
				continue;
			// Static image, SO Algorithm
			if (tmp instanceof GraphEARSStatic && tmp.data[0].algorithm instanceof Algorithm)
			{
				if (gt == PlotType.AverageOfIterations 
						|| gt == PlotType.BestOfIterations 
						|| gt == PlotType.WorstOfIterations 
						|| gt == PlotType.StandardDeviationOfIterations)
				{
					tmp.Plot(gt);
				}
			}
		}
	}
	// Add plot:
	public void Plot(PlotType gt, RecordedData[] data)
	{
		int l = graphs.size();
		for (int i=0; i<l; i++)
		{
			GraphEARS tmp = graphs.get(i);
			// Static image, SO Algorithm
			if (tmp instanceof GraphEARSStatic && tmp.data[0].algorithm instanceof Algorithm)
			{
				if (gt == PlotType.AverageOfIterations 
						|| gt == PlotType.BestOfIterations 
						|| gt == PlotType.WorstOfIterations 
						|| gt == PlotType.StandardDeviationOfIterations)
				{
					tmp.Plot(gt, data);
				}
			}
		}
	}
	// Add plot:
	public void Plot(PlotType gt, RecordedData[][] data)
	{
		int l = graphs.size();
		for (int i=0; i<l; i++)
		{
			GraphEARS tmp = graphs.get(i);
			// Static image, SO Algorithm
			if (tmp instanceof GraphEARSStatic && tmp.data[0].algorithm instanceof Algorithm)
			{
				if (gt == PlotType.AverageOfIterations 
						|| gt == PlotType.BestOfIterations 
						|| gt == PlotType.WorstOfIterations 
						|| gt == PlotType.StandardDeviationOfIterations)
				{
					tmp.Plot(gt, data[i]);
				}
			}
		}
	}
	
	
	/**
	 * Set zoom on pareto front in all contained graphs.
	 * @param zoomLevel Default = 1.0 (unzoomed), 0.6 = display 0.6 of full graph
	 */
	public void setZoomScale(double zoom)
	{
		this.zoomLevel = zoom;
		int l = graphs.size();
		for (int i=0; i<l; i++)
		{
			if (graphs.get(i).data[0].algorithm instanceof MOAlgorithm)
				graphs.get(i).setZoomScale(this.zoomLevel);
		}
	}
	
	
	public void setPlotColorScheme(PlotColorScheme pcs)
	{
		this.plotColorScheme = pcs;
		int l = graphs.size();
		for (int i=0; i<l; i++)
		{
			graphs.get(i).setPlotColorScheme(pcs);
		}
	}
	
	
	public void SaveToPlotFiles()
	{
		int l = graphs.size();
		for (int i=0; i<l; i++)
		{
			graphs.get(i).SaveToPlotFile();
		}
	}
	public void SaveStatisticsToFiles()
	{
		int l = graphs.size();
		for (int i=0; i<l; i++)
		{
			if (graphs.get(i) instanceof GraphEARSStatic)
				((GraphEARSStatic)graphs.get(i)).SaveStatistics();;
		}
	}
	
	
	// Flush:
	public void Flush()
	{
		int l = graphs.size();
		for (int i=0; i<l; i++)
		{
			graphs.get(i).Flush();
		}
	}
	
	
	// Add:
	public void Add(GraphSet other)
	{
		for (GraphEARS g : other.graphs)
		{
			this.graphs.add(g);
		}
	}
	public void Add(GraphEARS g)
	{
		this.graphs.add(g);
	}
	
	
	// Combine graphs in this set:
	public GraphSet getCombinedGraphsByProblem()
	{
		GraphSet output = new GraphSet();
		output.data = this.data;
		Hashtable<String,GraphEARS> newGraphs = new Hashtable<String,GraphEARS>();
		
		Hashtable<String,ArrayList<GraphEARS>> problemToGraphs = new Hashtable<String,ArrayList<GraphEARS>>();
		String tmpName;
		for (GraphEARS g : this.graphs)
		{
			if (g.data == null)
				continue;
			tmpName = g.data[0].problemName;
			if (!problemToGraphs.containsKey(tmpName))
			{
				problemToGraphs.put(tmpName, new ArrayList<GraphEARS>());
			}
			problemToGraphs.get(tmpName).add(g);
		}
		
		ArrayList<String> allProblems = Collections.list(problemToGraphs.keys());
		for (String problem : allProblems)
		{
			double minX = Double.MAX_VALUE;
			double maxX = Double.MIN_VALUE;
			double minY = Double.MAX_VALUE;
			double maxY = Double.MIN_VALUE;
			
			ArrayList<GraphEARS> tmpGraphs = problemToGraphs.get(problem);
			// START Single objective optimization:

			if (!newGraphs.containsKey(problem))
			{
				GraphEARS tmpGraph = new GraphEARSStatic();
				tmpGraph.Title = "Algorithms solving "+problem;
				String fileName = "algorithms solving "+problem+".png";

				if (this.outputAutomatic != null && this.outputAutomatic == true)
				{
					if (this.overwrite == null)
						tmpGraph.setOutputFile(fileName);
					else
						tmpGraph.setOutputFile(fileName, this.overwrite);
				}
				newGraphs.put(problem, tmpGraph);
			}
			GraphEARS tmpGraph = newGraphs.get(problem);

			//NamedPlotColor[] tmpColors = NamedPlotColor.values();
			int colorIndex = 0; //RED(30)
			PlotColor tmpColor = new RgbPlotColor(0,0,0);
			for (GraphEARS g : tmpGraphs)
			{	
				if (g instanceof GraphEARSAnimated)
					continue;


				if (g.minY < minY)
					minY = g.minY;
				if (g.maxY > maxY)
					maxY = g.maxY;


				/*NamedPlotColor tmpColor = tmpColors[colorIndex];
					colorIndex = (colorIndex + 3)%tmpColors.length;	// Normal, Light, Dark*/
				switch (plotColorScheme)
				{
				case Colored:
					tmpColor = MULTI_PLOT_COLORS[colorIndex];
					colorIndex = (colorIndex + 1)%MULTI_PLOT_COLORS.length;
					break;
				case Grayscale:
					tmpColor = new RgbPlotColor(colorIndex,colorIndex,colorIndex);
					colorIndex = colorIndex + (235/tmpGraphs.size());
					break;
				}
				DataSetPlot[] plots = g.getDataSetPlots();
				boolean inLegend = false;
				for (DataSetPlot plot : plots)
				{
					PlotStyle tmpPlotStyle = (PlotStyle)plot.getPlotStyle().clone();
					DataSetPlot tmpPlot = (DataSetPlot)plot.clone();
					tmpPlotStyle.setLineType(tmpColor);
					tmpPlot.setPlotStyle(tmpPlotStyle);
					if (!inLegend)
					{
						//String tmpTitle = tmpPlot.get("title");	// REMOVE '
						tmpPlot.setTitle(g.data[0].algorithm.getID().replace("_", "\\_")/*+" "+tmpTitle*/);
						inLegend = true;
					}
					else
					{
						tmpPlot.setTitle("");
					}
					tmpGraph.Plot(tmpPlot);
				}
			}

			// Set bounds to optimum:
			tmpGraph.setBoundsY(minY, maxY);

			// END Single objective optimization
		}
		
		output.add(newGraphs.values());
		output.setCanvasSize(this.CanvasSize);
		return output;
	}
	
	
	
}
