package org.um.feri.ears.graphing;

import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.um.feri.ears.graphing.data.RecordedData;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.terminal.ImageTerminal;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.plot.Plot;

public abstract class GraphEARS
{
	// Constants:
	public final static int DEFAULT_CANVAS_WIDTH = 640;
	public final static int DEFAULT_CANVAS_HEIGHT = 480;
	
	// Vars
	protected boolean outputAutomatic = false;
	protected String output = "";
	protected RecordedData[] data = null;
	protected JavaPlot plot;
	protected File file = null;
	protected ImageTerminal iterm = null;
	protected long popSize;
	protected boolean popSizeKnown = false;
	protected String outputExtension = null;
	protected Dimension CanvasSize = new Dimension(640,480);
	protected PlotColorScheme plotColorScheme = PlotColorScheme.Colored;

	public String Title = null;
	ArrayList<GraphLineStyleChanger> rescaleStylesToCanvas = new ArrayList<GraphLineStyleChanger>();
	protected double zoom = 1.0;
	int axisX=0, axisY=1;
	Double minX = null;
	public Double maxX = null;
	Double minY = null;
	Double maxY = null;
	Double fullMinX = null;
	Double fullMaxX = null;
	Double fullMinY = null;
	Double fullMaxY = null;
	String axisXLabel = null;
	String axisYLabel = null;
	protected ArrayList<DataSetPlot> dataSetPlots = new ArrayList<DataSetPlot>();

	
	public Dimension getCanvasSize()
	{
		return new Dimension(CanvasSize);
	}
	public int getCanvasWidth()
	{
		return CanvasSize.width;
	}
	public int getCanvasHeight()
	{
		return CanvasSize.height;
	}
	public void setCanvasSize(Dimension canvasSize)
	{
		CanvasSize = canvasSize;
		RescaleToCanvas();
	}
	public void setCanvasSize(int width, int height)
	{
		CanvasSize = new Dimension(width,height);
		RescaleToCanvas();
	}
	public void setBounds(double minX, double maxX, double minY, double maxY)
	{
		this.fullMinX = minX;
		this.fullMaxX = maxX;
		this.fullMinY = minY;
		this.fullMaxY = maxY;
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
	}
	public void setBoundsX(double minX, double maxX)
	{
		this.fullMinX = minX;
		this.fullMaxX = maxX;
		this.minX = minX;
		this.maxX = maxX;
	}
	public void setBoundsY(double minY, double maxY)
	{
		this.fullMinY = minY;
		this.fullMaxY = maxY;
		this.minY = minY;
		this.maxY = maxY;
	}
	public void setZoomScale(double zoom)
	{
		if (zoom <= 0.0)
			return;
		else if (fullMinX == null || fullMaxX == null || fullMinY == null || fullMaxY == null)
			return;
		this.zoom = zoom;
		// Minimization:
		maxX = fullMinX + (fullMaxX-fullMinX)*zoom;
		maxY = fullMinY + (fullMaxY-fullMinY)*zoom;
	}
	public double getZoomScale()
	{
		return this.zoom;
	}
	public String getAxisXLabel()
	{
		return axisXLabel;
	}
	public void setAxisXLabel(String axisXLabel)
	{
		this.axisXLabel = axisXLabel;
	}
	public String getAxisYLabel()
	{
		return axisYLabel;
	}
	public void setAxisYLabel(String axisYLabel)
	{
		this.axisYLabel = axisYLabel;
	}
	public RecordedData[] getData()
	{
		return data;
	}
	public void setData(RecordedData[] data)
	{
		this.data = data;
	}
	/*protected void setData(RecordedData[] data, boolean overwrite)
	{
		this.data = data;
		if (outputAutomatic)
		{
			String tmp = data[0].algorithm.getID()+" solving "+data[0].problem.name+" it"+data[0].iteration+".png";
			setOutputFilePrivate(tmp, overwrite);
		}
	}*/
	public Plot[] getPlots()
	{
		return this.plot.getPlots().toArray(new Plot[0]);
	}
	public DataSetPlot[] getDataSetPlots()
	{
		return this.dataSetPlots.toArray(new DataSetPlot[0]);
	}
	public void aPlots(DataSetPlot[] plots)
	{
		for (DataSetPlot tmp : plots)
		{
			plot.addPlot(tmp);
			dataSetPlots.add(tmp);
		}
	}
	public void Plot(DataSetPlot plt)
	{
		plot.addPlot(plt);
		dataSetPlots.add(plt);
	}
	// Color of plots:
	public PlotColorScheme getPlotColorScheme()
	{
		return plotColorScheme;
	}
	public void setPlotColorScheme(PlotColorScheme plotColorScheme)
	{
		this.plotColorScheme = plotColorScheme;
	}
	
	
	// Set output file:
	public void setOutputFileAutomatic()
	{
		setOutputFileAutomatic(true);
	}
	public abstract void setOutputFileAutomatic(boolean overwrite);
	protected abstract void setOutputFilePrivate(String filename, boolean overwrite);
	public void setOutputFile(String filename, boolean overwrite)
	{
		outputAutomatic = false;
		setOutputFilePrivate(filename, overwrite);
	}
	public void setOutputFile(String filename)
	{
			setOutputFile(filename, true);
	}
	// Get output file:
	public String getOutputFile()
	{
		return output;
	}
	
	
	
	public void SaveToPlotFile()
	{
		String plotFile = null;
		
		if (output=="")
		{
			if (data != null)
			{

				plotFile = data[0].algorithm.getID()+" solving "+data[0].problemName+".plot";
			}
			else
			{
				System.err.println("ERR: Nothing to save. Graph data is null.");
				return;
			}
		}
		else
		{
			int extIndex = 0;
			if (outputExtension.equals("png"))
			{
				extIndex = output.lastIndexOf(".png");
				plotFile = output.substring((System.getProperty("user.dir")+"/output/").length(), extIndex);
			}
			else 
			{
				plotFile = output;
			}
			
			plotFile = plotFile+".plot";
		}
		
		SaveAsPlotFile(System.getProperty("user.dir")+"/output/"+plotFile);
	}
	public void SaveAsPlotFile(String plotFile)
	{
		try
		{
			File file = new File(plotFile);
			if (!file.exists()) 
			{
				file.createNewFile();
			}
			
		    BufferedWriter writer = new BufferedWriter( new FileWriter(plotFile));
			writer.write(this.GetGeneratedScript());
			writer.flush();
			writer.close();
		}
		catch (Exception ex)
		{
			System.err.println(ex.getMessage());
		}
	}
	
	
	
	protected void RescaleToCanvas()
	{
		double scale = CanvasSize.width/(double)DEFAULT_CANVAS_WIDTH*CanvasSize.height/(double)DEFAULT_CANVAS_HEIGHT;
		for (GraphLineStyleChanger sc : rescaleStylesToCanvas)
		{
			sc.Scaling(new double[]{scale});
		}
	}
	protected void RescaleToCanvas(PlotStyle ps, String key, String value, double[] baseValues)
	{
		GraphLineStyleChanger tmp = new GraphLineStyleChanger(ps, key, value, baseValues);
		rescaleStylesToCanvas.add(tmp);
		double scale = CanvasSize.width/(double)DEFAULT_CANVAS_WIDTH*CanvasSize.height/(double)DEFAULT_CANVAS_HEIGHT;
		scale = Math.sqrt(scale);
		tmp.Scaling(new double[]{scale});
	}
	
	
	public abstract void Plot(PlotType gt);
	public abstract void Plot(PlotType gt, RecordedData[] data);
	public abstract void Plot(PlotType gt, RecordedData[][] data);
	public abstract void Flush();
	
	public String GetGeneratedScript()
	{
		return plot.getCommands();
	}
}
