package org.um.feri.ears.visualization.graphing;

import java.io.BufferedWriter;
import java.io.File;
//import java.io.FileInputStream; OLD
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

//import javax.imageio.ImageIO; OLD

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmBase;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.visualization.graphing.data.AlgorithmStatistics;
import org.um.feri.ears.visualization.graphing.data.GraphDataManager;
import org.um.feri.ears.visualization.graphing.data.MOSolutionComparatorForGraphing;
import org.um.feri.ears.visualization.graphing.data.RecordedData;
import org.um.feri.ears.problems.SolutionBase;
import org.um.feri.ears.problems.moo.MOSolutionBase;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.NamedPlotColor;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.RgbPlotColor;
import com.panayotis.gnuplot.style.Style;

public class GraphEARSStatic extends GraphEARS
{
	// Constants
	public final static int DEFAULT_EVALUATIONS_PER_STEP = 20;
	protected int evalsPerStep = DEFAULT_EVALUATIONS_PER_STEP;
	protected AlgorithmStatistics statistics;
	
	
	// Constructor:
	public GraphEARSStatic()
	{
		this.plot = new JavaPlot();
	}
	// Constructor:
	public GraphEARSStatic(RecordedData[] data)
	{
		this.plot = new JavaPlot();
		setData(data);
	}
	// Constructor:
	public GraphEARSStatic(RecordedData[] data, int evalsPerStep)
	{
		this.plot = new JavaPlot();
		setData(data);
	}
	// Constructor:
	public GraphEARSStatic(RecordedData[] data, int axisX, int axisY)
	{
		this.axisX = axisX;
		this.axisY = axisY;
		this.axisXLabel = "obj. "+axisX;
		this.axisYLabel = "obj. "+axisY;
		this.plot = new JavaPlot();
		setData(data);
	}
		
	
	protected void setOutputFilePrivate(String filename, boolean overwrite)
	{
		//iterm = null;	OLD
		file = null;
		
		//iterm = new ImageTerminal();	OLD
		
		// If absolute path, skip adding output folder.
		if (!Paths.get(filename).isAbsolute())
		{
			filename = System.getProperty("user.dir")+"/output/"+filename;
		}
		file = new File(filename);
		
        try 
        {
        	if (!file.exists())
        	{
        		//file.getParentFile().mkdirs();
        		//file.createNewFile();
        	}
        	else if (!overwrite)
        	{
        		String orgName = filename;
        		int num=1;
        		do
        		{
        			int i = orgName.contains(".") ? orgName.lastIndexOf('.') : orgName.length();
        			String filename2 = orgName.substring(0, i) + " - Copy ("+num+")"+ orgName.substring(i);
        			//file = new File(System.getProperty("user.dir")+"/output/"+filename2);
        			file = new File(filename2);
        			num++;
        		} while (file.exists());
        	}
            /* OLD
            iterm.processOutput(new FileInputStream(file));
            p.setPersist(false);
            p.setTerminal(iterm);
            OLD */
        	// Actual file:
            output = file.getAbsolutePath();
            // Extension:
            int i = filename.lastIndexOf('.');
            if (i > 0)
                outputExtension = filename.substring(i+1);
            else
            	outputExtension = null;
        } 
        catch (Exception ex) 
        {
            System.err.println(ex);
        } 
	}
	// Set output file:
	@Override
	public void setOutputFileAutomatic(boolean overwrite)
	{
		output = null;
		iterm = null;
		file = null;
		outputAutomatic = true;
		if (data != null)
		{
			String tmp="";
			if (data[0].algorithm instanceof MOAlgorithm)
			{
				tmp = data[0].algorithm.getID()+" solving "+data[0].problemName+" it"+data[0].iteration+" ("+axisX+"x"+axisY+")"+".png";
			}
			else if (data[0].algorithm instanceof Algorithm)
			{
				//tmp = data[0].algorithm.getID()+" solving "+data[0].problem.name+" it"+data[0].iteration+".png";
				tmp = data[0].algorithm.getID()+" solving "+data[0].problemName+".png";
			}
			setOutputFilePrivate(tmp, overwrite);
		}
	}
	@Override
	public void setData(RecordedData[] data)
	{
		this.data = data;
		if (data[0].algorithm instanceof Algorithm)
		{
			this.statistics = new AlgorithmStatistics(data, evalsPerStep);
			double opt = 0;
			boolean isMinimization = true;
			if (isMinimization)
			{
				this.minY = opt;
				this.maxY = Double.MIN_VALUE;
				/*for (int i=0; i<data.length; i++)
				{
					if (data[i].solution.getEval() > this.maxY)
					this.maxY = data[i].solution.getEval();
				}*/
				for (int i=0; i<statistics.fitnessAverage.length; i++)
				{
					if (statistics.fitnessAverage[i]+statistics.fitnessSD[i] > this.maxY)
						this.maxY = statistics.fitnessAverage[i]+statistics.fitnessSD[i];
				}
				this.minY = Math.floor(this.minY);
				this.maxY = Math.ceil(this.maxY);
			}
			else
			{
				this.minY = Double.MAX_VALUE;
				/*for (int i=0; i<data.length; i++)
				{
					if (data[i].solution.getEval() < this.minY)
					this.minY = data[i].solution.getEval();
				}*/
				for (int i=0; i<statistics.fitnessAverage.length; i++)
				{
					if (statistics.fitnessAverage[i]-statistics.fitnessSD[i] < this.minY)
						this.minY = statistics.fitnessAverage[i]-statistics.fitnessSD[i];
				}
				this.maxY = opt;
				this.minY = Math.floor(this.minY);
				this.maxY = Math.ceil(this.maxY);
			}
		}
		else if (data[0].algorithm instanceof MOAlgorithm)
	    {
			if (this.minX == null)
        		this.minX = Double.MAX_VALUE;
        	if (this.maxX == null)
        		this.maxX = Double.MIN_VALUE;
        	if (this.minY == null)
        		this.minY = Double.MAX_VALUE;
        	if (this.maxY == null)
        		this.maxY = Double.MIN_VALUE;
	       	double x,y;
	       	for (RecordedData rd : data)
	       	{
	       		x = ((MOSolutionBase)rd.solution).getObjective(axisX);
	       		y = ((MOSolutionBase)rd.solution).getObjective(axisY);
	       		minX = Math.min(minX, x);
	       		maxX = Math.max(maxX, x);
	       		minY = Math.min(minY, y);
	       		maxY = Math.max(maxY, y);
	       	}
	       	fullMinX = minX = Math.floor(minX);
	       	fullMaxX = maxX = Math.ceil(maxX);
	       	fullMinY = minY = Math.floor(minY);
	      	fullMaxY = maxY = Math.ceil(maxY);
	    }

			
		if (outputAutomatic)
		{
			String tmp="";
			if (data[0].algorithm instanceof MOAlgorithm)
			{
				tmp = data[0].algorithm.getID()+" solving "+data[0].problemName+" it"+data[0].iteration+" ("+axisX+"x"+axisY+")"+".png";
			}
			else if (data[0].algorithm instanceof Algorithm)
			{
				//tmp = data[0].algorithm.getID()+" solving "+data[0].problem.name+" it"+data[0].iteration+".png";
				tmp = data[0].algorithm.getID()+" solving "+data[0].problemName+".png";
			}
			setOutputFilePrivate(tmp, false);
		}
	}

	
	
	// Save statistics:
	public void SaveStatistics()
	{
		String statisticsFile = null;
		
		if (output=="")
		{
			if (data != null)
			{

				statisticsFile = data[0].algorithm.getID()+" solving "+data[0].problemName+".csv";
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
				statisticsFile = output.substring((System.getProperty("user.dir")+"/output/").length(), extIndex);
			}
			else 
			{
				statisticsFile = output;
			}
			statisticsFile = System.getProperty("user.dir")+"/output/" + statisticsFile+".csv";
		}

		SaveStatisticsAs(statisticsFile);
	}
	public void SaveStatisticsAs(String statisticsFile)
	{
		try
		{
			File file = new File(statisticsFile);
			if (!file.exists()) 
			{
				file.createNewFile();
			}
			
		    BufferedWriter writer = new BufferedWriter( new FileWriter(statisticsFile));
			writer.write("Average;StandardDeviation;Worst;Best\n");
			for (int i=0; i<statistics.fitnessAverage.length; i++)
			{
				writer.write(statistics.fitnessAverage[i]+";"+statistics.fitnessSD[i]+";"+statistics.fitnessWorst[i]+";"+statistics.fitnessBest[i]+'\n');
			}
			writer.flush();
			writer.close();
		}
		catch (Exception ex)
		{
			System.err.println(ex.getMessage());
		}
	}
	
	
	
	
	// Add plot:
	@Override
	public void Plot(PlotType gt)
	{
		Plot(gt, data);
	}
	// Add plot:
	@Override
	public void Plot(PlotType gt, RecordedData[] data)
	{
		DataSetPlot tmp = null;
		switch(gt)
		{
		/*case AverageInGeneration:
			tmp = GraphAverageInGeneration(data);
			break;
		case WorstInGeneration:
			tmp = GraphWorstInGeneration(data);
			break;
		case BestInGeneration:
			tmp = GraphBestInGeneration(data);
			break;
		case StandardDeviationInGeneration:
			tmp = GraphSDInGeneration(data);
			break;*/
		case AVERAGE_OF_ITERATIONS:
			tmp = statistics.AverageDSP(plotColorScheme);
			break;
		case WORST_OF_ITERATIONS:
			tmp = statistics.WorstDSP(plotColorScheme);
			break;
		case BEST_OF_ITERATIONS:
			tmp = statistics.BestDSP(plotColorScheme);
			break;
		case STANDARD_DEVIATION_OF_ITERATIONS:
			tmp = statistics.StandardDeviationDSP(plotColorScheme);
			RescaleToCanvas(tmp.getPlotStyle(), "dashtype", "(_,_)", new double[]{5,5});
			break;
		case MO_ALL_INDIVIDUALS:
			tmp = GraphAllIndividualsMO(data);
			break;
		case MO_PARETO_INDIVIDUALS:
			tmp = GraphCurrentParetoIndividualsMO(data);
			break;
		case MO_PARETO_FRONT_SEARCH:
			Plot(PlotType.MO_DOMINATED_SPACE_CURRENT, data);
			Plot(PlotType.MO_ALL_INDIVIDUALS, data);
			Plot(PlotType.MO_PARETO_INDIVIDUALS, data);
			break;
		case MO_DOMINATED_SPACE_CURRENT:
			tmp = GraphDominatedSpaceCurrentMO(data);
			break;
		case MO_DOMINATED_SPACE_SO_FAR:
			tmp = GraphDominatedSpaceSoFarMO(data);
			break;
		case MO_FINAL_PARETO_FRONT:
			DataSetPlot[] tmp2 = GraphFinalParetoFront(data);
			for (DataSetPlot dsp : tmp2)
			{
				dataSetPlots.add(dsp);
				plot.addPlot(dsp);
			}
			return;
		default:
			System.err.println("WARNING: Unknown graph type.");
		}
		if (tmp == null)
		{
			return;
		}
		dataSetPlots.add(tmp);
		plot.addPlot(tmp);
	}
	@Override
	public void Plot(PlotType gt, RecordedData[][] data)
	{
		DataSetPlot[] tmp = null;
		switch(gt)
		{
		case MO_ALL_INDIVIDUALS:
			tmp = GraphAllIndividualsMO(data);
			break;
		default:
			Plot(gt, data);
		}
		if (tmp == null)
		{
			return;
		}
		for (DataSetPlot dsp : tmp)
		{
			dataSetPlots.add(dsp);
			plot.addPlot(dsp);
		}
	}
	
	
	// Flush to output:
	public void Flush()
	{
		if (plot.getPlots().size() == 0)
			return;
		
		//p.newGraph();
		if (axisXLabel == null)
		{
			/*if (popSizeKnown)
				p.getAxis("x").setLabel("generation");
			else
				p.getAxis("x").setLabel("group");*/
			plot.getAxis("x").setLabel("evaluations");
		}
		else
		{
			plot.getAxis("x").setLabel(axisXLabel);
		}
		if (axisYLabel == null)
		{
			//p.getAxis("y").setLabel("Eval");
			plot.getAxis("y").setLabel("fitness");
		}
		else
		{
			plot.getAxis("y").setLabel(axisYLabel);
		}
		
		if (minX != null && maxX != null)
			plot.getAxis("x").setBoundaries(minX, maxX);
		if (minY != null && maxY != null)
			plot.getAxis("Y").setBoundaries(minY, maxY);
		//p.set("ylabel", "'Eval'");
		
		/* OLD
		if (iterm != null && file != null)
		{
		//if (outputExtension.toLowerCase() == "png")
			p.set("term "+"png"+" size", CanvasSize.width+","+CanvasSize.height);	// Any other format passed here crashes the application.
		//p.set("size ratio", "2");
		//else
		//	System.err.println("Warning: JavaPlot does not allow custom canvas size for formats other than PNG.");
		}
		OLD */
		
		if (output!=null && output != "")
		{
			file.getParentFile().mkdirs();
			plot.setPersist(false);
			plot.set("terminal pngcairo dashed size", CanvasSize.width+","+CanvasSize.height);
			plot.set("output", "'"+output+"'");
		}
		else
		{
			plot.setPersist(true);
		}
		
		// DEBUG
        //String cmd = p.getCommands();
    	//System.out.println(cmd);
    	
		if (Title != null)
			plot.setTitle(Title);
		else
		{
			/*if (data[0].algorithm instanceof MOAlgorithm)
			{
				p.setTitle(data[0].algorithm.getID()+" solving "+data[0].problem.name+" ("+axisX+"x"+axisY+")");
			}
			else*/
			{
				plot.setTitle(data[0].algorithm.getID().replace("_", "\\_")+" solving "+data[0].problemName);
			}
		}
		plot.plot();
		
		/*ExecutorService es = Executors.newCachedThreadPool();
		es.execute(new Runnable() {
			@Override
			public void run() {
				plot.plot();

			} });
		es.shutdown();
		
		try {
			boolean finshed = es.awaitTermination(1, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/

        

        
		// Dejanski zapis v datoteko:
        /* OLD
        if (iterm != null && file != null)
        {
		    try 
		    {
		    	// DEBUG:
		    	//ArrayList<com.panayotis.gnuplot.plot.Plot> plots = p.getPlots();
		    	//int l = plots.size();
		    	//String cmd = p.getCommands();
		    	//System.out.println(cmd);
		    	
		    	
		        ImageIO.write(iterm.getImage(), outputExtension, file);
		    } 
		    catch (IOException ex) 
		    {
		        System.err.print(ex);
		    }
        } 
        OLD*/
	}
	
	
	@Deprecated
	protected DataSetPlot GraphAverageInGeneration(RecordedData[] data)
	{
		// Set plot style:
		PlotStyle myPlotStyle = new PlotStyle();
        myPlotStyle.setStyle(Style.LINESPOINTS);
        myPlotStyle.setLineWidth(1);
        myPlotStyle.setLineType(NamedPlotColor.BLUE);
        myPlotStyle.setPointType(7);
        myPlotStyle.setPointSize(1);
        
        popSize = GetPopSize(data);
        // Starting population size is usually different from running population size:
        long popCounter = popSize-(data.length%popSize);
        if (popCounter == popSize)
        	popCounter = 0;
        // popCounter = 0;
        
        // Prepare structure:
		double tab[][];
		int numberOfGenerations = (int)Math.ceil((double)data.length/(double)popSize);
        tab = new double[numberOfGenerations][2];
        
        // Convert individual data to generation data:
        int generationIndex = 0;
        double generationY = 0.0000;
        int  generationIndividualCounter = 0;
        for (int i=0; i<data.length; i++)
        {
        	generationY = generationY + data[i].solution.getEval();
        	generationIndividualCounter = generationIndividualCounter + 1;
        	popCounter = popCounter + 1;
        	// If we have all data for current generation, finalize it:
        	if (popCounter>=popSize)
        	{
        		tab[generationIndex][0] = (double)generationIndex+1;
            	tab[generationIndex][1] = generationY/(double)generationIndividualCounter;
            	generationIndex = generationIndex+1;
            	generationY = 0.0000;
            	generationIndividualCounter = 0;
            	popCounter = 0;
        	}
        }
        DataSetPlot dsp = new DataSetPlot(tab);
        dsp.setTitle("Average");
        dsp.setPlotStyle(myPlotStyle);
		return dsp;
	}
	
	
	@Deprecated
	protected DataSetPlot GraphWorstInGeneration(RecordedData[] data)
	{
		// Set plot style:
		PlotStyle myPlotStyle = new PlotStyle();
        myPlotStyle.setStyle(Style.LINESPOINTS);
        myPlotStyle.setLineWidth(1);
        myPlotStyle.setLineType(NamedPlotColor.RED);
        myPlotStyle.setPointType(7);
        myPlotStyle.setPointSize(1);
        
        long popSize = GetPopSize(data);
        // Starting population size is usually different from running population size:
        long popCounter = popSize-(data.length%popSize);
        if (popCounter == popSize)
        	popCounter = 0;
        
        // Prepare structure:
		double tab[][];
		int numberOfGenerations = (int)Math.ceil((double)data.length/(double)popSize);
        tab = new double[numberOfGenerations][2];
        
        // Convert individual data to generation data:
        int generationIndex = 0;
        double generationBestY = data[0].solution.getEval();
        for (int i=0; i<data.length; i++)
        {
        	if (generationBestY < data[i].solution.getEval())
        	//if (data[0].problem.isFirstBetter(generationBestY, data[i].solution.getEval()))
        		generationBestY = data[i].solution.getEval();
        	popCounter = popCounter + 1;
        	// If we have all data for current generation, finalize it:
        	if (popCounter>=popSize)
        	{
        		tab[generationIndex][0] = (double)generationIndex+1;
            	tab[generationIndex][1] = generationBestY;
            	generationIndex = generationIndex+1;
            	if (i+1<data.length)
            		generationBestY = data[i+1].solution.getEval();
            	popCounter = 0;
        	}
        }
        DataSetPlot dsp = new DataSetPlot(tab);
        dsp.setTitle("Worst");
        dsp.setPlotStyle(myPlotStyle);
		return dsp;
	}
	
	
	@Deprecated
	protected DataSetPlot GraphBestInGeneration(RecordedData[] data)
	{
		// Set plot style:
		PlotStyle myPlotStyle = new PlotStyle();
        myPlotStyle.setStyle(Style.LINESPOINTS);
        myPlotStyle.setLineWidth(1);
        myPlotStyle.setLineType(NamedPlotColor.GREEN);
        myPlotStyle.setPointType(7);
        myPlotStyle.setPointSize(1);
        
        long popSize = GetPopSize(data);
        // Starting population size is usually different from running population size:
        long popCounter = popSize-(data.length%popSize);
        if (popCounter == popSize)
        	popCounter = 0;
        
        // Prepare structure:
		double tab[][];
		int numberOfGenerations = (int)Math.ceil((double)data.length/(double)popSize);
        tab = new double[numberOfGenerations][2];
        
        // Convert individual data to generation data:
        int generationIndex = 0;
        double generationBestY = data[0].solution.getEval();
        for (int i=0; i<data.length; i++)
        {
        	if (generationBestY < data[i].solution.getEval())
        	//if (data[0].problem.isFirstBetter(data[i].solution.getEval(), generationBestY))
        		generationBestY = data[i].solution.getEval();
        	popCounter = popCounter + 1;
        	// If we have all data for current generation, finalize it:
        	if (popCounter>=popSize)
        	{
        		tab[generationIndex][0] = (double)generationIndex+1;
            	tab[generationIndex][1] = generationBestY;
            	generationIndex = generationIndex+1;
            	if (i+1<data.length)
            		generationBestY = data[i+1].solution.getEval();
            	popCounter = 0;
        	}
        }
        DataSetPlot dsp = new DataSetPlot(tab);
        dsp.setTitle("Best");
        dsp.setPlotStyle(myPlotStyle);
		return dsp;
	}
	
	
	@Deprecated
	protected DataSetPlot GraphSDInGeneration(RecordedData[] data)
	{
		// Set plot style:
		PlotStyle myPlotStyle = new PlotStyle();
        myPlotStyle.setStyle(Style.LINES);
        //myPlotStyle.set("dashtype", "(5,5)");	// Dash Pattern.
        myPlotStyle.setLineWidth(1);
        myPlotStyle.setLineType(NamedPlotColor.ORANGE);
        myPlotStyle.set("dashtype", "(5,5)");	// Dash Pattern.
        //myPlotStyle.setPointType(7);
        //myPlotStyle.setPointSize(1);
        // Enable scaling of dashed line:
        RescaleToCanvas(myPlotStyle, "dashtype", "(_,_)", new double[]{5,5});
		
		/*PlotStyle myPlotStyle = new PlotStyle();	// DEBUG2
		myPlotStyle.setStyle(Style.LINES);
		myPlotStyle.set("linetype", "3 dashtype (5,5)");*/
        
        popSize = GetPopSize(data);
        // Starting population size is usually different from running population size:
        long popCounter = popSize-(data.length%popSize);
        if (popCounter == popSize)
        	popCounter = 0;
        
        // Prepare structure:
        double avg[];
        double SD[];
        ArrayList<Double> generationValues = new ArrayList<Double>();
		double tabHigh[][];
		double tabLow[][];
		int numberOfGenerations = (int)Math.ceil((double)data.length/(double)popSize);
		avg = new double[numberOfGenerations];
		SD = new double[numberOfGenerations];
        tabHigh = new double[numberOfGenerations][2];
        tabLow = new double[numberOfGenerations][2];
        
        // Convert individual data to generation data:
        int generationIndex = 0;
        double generationY = 0.0000;
        int  generationIndividualCounter = 0;
        for (int i=0; i<data.length; i++)
        {
        	generationY = generationY + data[i].solution.getEval();
        	generationValues.add(data[i].solution.getEval());
        	generationIndividualCounter = generationIndividualCounter + 1;
        	popCounter = popCounter + 1;
        	// If we have all data for current generation, finalize it:
        	if (popCounter>=popSize)
        	{
        		avg[generationIndex] = generationY/(double)generationIndividualCounter;
        		// Deviations:
        		double deviations[] = new double[generationIndividualCounter];
        		for (int index = 0; index<generationIndividualCounter; index++)
        		{
        			deviations[index] = Math.pow((generationValues.get(index)-avg[generationIndex]), 2);
        		}
        		// Variance:
        		double variance = 0.0000;
        		for (int index = 0; index<generationIndividualCounter; index++)
        		{
        			variance = variance + deviations[index];
        		}
        		variance = variance / generationIndividualCounter;
        		// SD:
        		SD[generationIndex] = Math.sqrt(variance);
        		
        		generationValues.clear();
        		
        		tabHigh[generationIndex][0] = (double)generationIndex+1;
            	tabHigh[generationIndex][1] = avg[generationIndex]+SD[generationIndex];
            	tabLow[generationIndex][0] = (double)generationIndex+1;
            	tabLow[generationIndex][1] = avg[generationIndex]-SD[generationIndex];
            	generationIndex = generationIndex+1;
            	generationY = 0.0000;
            	generationIndividualCounter = 0;
            	popCounter = 0;
        	}
        }
        // Seperate upper and lower:
        /*DataSetPlot dspHigh = new DataSetPlot(tabHigh);
        dspHigh.setTitle("SD Upper");
        dspHigh.setPlotStyle(myPlotStyle);
        DataSetPlot dspLow = new DataSetPlot(tabLow);
        dspLow.setTitle("SD Lower");
        dspLow.setPlotStyle(myPlotStyle);
		//return new DataSetPlot[]{dspLow, dspHigh};*/
        
        // Single Line for SD:
        List<double[]> tabHighRev = Arrays.asList(tabHigh);
        Collections.reverse(tabHighRev);
        ArrayList<double[]> tabAllList = new ArrayList<double[]>(Arrays.asList(tabLow));
        tabAllList.addAll(tabHighRev);
        double[][] tabAll = tabAllList.toArray(new double[0][]);
        DataSetPlot dspAll = new DataSetPlot(tabAll);
        dspAll.setTitle("Standard Deviation");
        dspAll.setPlotStyle(myPlotStyle);
        return dspAll;
	}
	
	
	protected DataSetPlot GraphAllIndividualsMO(RecordedData[] data)
	{
		// Set plot style:
		PlotStyle myPlotStyle = new PlotStyle();
        myPlotStyle.setStyle(Style.POINTS);
        myPlotStyle.setLineWidth(0);
        myPlotStyle.setLineType(NamedPlotColor.RED);
        myPlotStyle.setPointType(2);
        myPlotStyle.setPointSize(1);
        
        /*ParetoSolution res = new ParetoSolution();
        while (res.size() < n) {
			int index = 0;
			MOSolution selected = candidate.get(0); // it should be a next! (n <= population size!)
			double distance_value = distance_utility
					.distanceToSolutionSetInObjectiveSpace(selected, res);
			int i = 1;
			while (i < candidate.size()) {
				MOSolution next_candidate = candidate.get(i);
				double aux = distance_value = distance_utility.distanceToSolutionSetInObjectiveSpace(next_candidate, res);
				if (aux > distance_value) {
					distance_value = aux;
					index = i;
				}
				i++;
			}

			// add the selected to res and remove from candidate list
			res.add(new MOSolution(candidate.remove(index)));
		}*/
		
        //popSize = GetPopSize(data);
        // Starting population size is usually different from running population size:
        
        // Prepare structure:
		double tab[][];
        tab = new double[data.length][2];
        
        // Convert:
        for (int i=0; i<data.length; i++)
        {
        	tab[i][0] = ((MOSolutionBase)data[i].solution).getObjective(axisX);
            tab[i][1] = ((MOSolutionBase)data[i].solution).getObjective(axisY);
        }
        DataSetPlot dsp = new DataSetPlot(tab);
        dsp.setTitle("MO Solutions");
        dsp.setPlotStyle(myPlotStyle);
        return dsp;
	}
	protected DataSetPlot[] GraphAllIndividualsMO(RecordedData[][] data)
	{
		int gc = data.length;
		DataSetPlot[] dsps = new DataSetPlot[gc];
		
		for (int gi=0; gi<gc; gi++)
		{
			// Set plot style:
			PlotStyle myPlotStyle = new PlotStyle();
	        myPlotStyle.setStyle(Style.POINTS);
	        myPlotStyle.setLineWidth(0);
	        int tmpGray = GraphEARSAnimated.PREVIOUS_GENERATIONS_DIM_STEP*(gc-gi-1);
	        myPlotStyle.setLineType(new RgbPlotColor(tmpGray, tmpGray, tmpGray));
	        myPlotStyle.setPointType(2);
	        /*if (gi==gc-1)
	        {
	        	myPlotStyle.setPointType(3);
	        }
	        else
	        {
	        	myPlotStyle.setPointType(2);
	        }*/
	        myPlotStyle.setPointSize(1);
	        
	        /*ParetoSolution res = new ParetoSolution();
	        while (res.size() < n) {
				int index = 0;
				MOSolution selected = candidate.get(0); // it should be a next! (n <= population size!)
				double distance_value = distance_utility
						.distanceToSolutionSetInObjectiveSpace(selected, res);
				int i = 1;
				while (i < candidate.size()) {
					MOSolution next_candidate = candidate.get(i);
					double aux = distance_value = distance_utility.distanceToSolutionSetInObjectiveSpace(next_candidate, res);
					if (aux > distance_value) {
						distance_value = aux;
						index = i;
					}
					i++;
				}
	
				// add the selected to res and remove from candidate list
				res.add(new MOSolution(candidate.remove(index)));
			}*/
			
	        //popSize = GetPopSize(data);
	        // Starting population size is usually different from running population size:
	        
	        // Prepare structure:
			double tab[][];
	        tab = new double[data[gi].length][2];
	        
	        // Convert:
	        for (int i=0; i<data[gi].length; i++)
	        {
	        	tab[i][0] = ((MOSolutionBase)data[gi][i].solution).getObjective(axisX);
	            tab[i][1] = ((MOSolutionBase)data[gi][i].solution).getObjective(axisY);
	        }
	        DataSetPlot dsp = new DataSetPlot(tab);
	        if (gi==gc-1)
	        	dsp.setTitle("MO Solutions");
	        else
	        	dsp.setTitle("");
	        dsp.setPlotStyle(myPlotStyle);
	        dsps[gi]=dsp;
		}
        return dsps;
	}
	
	
	protected DataSetPlot GraphCurrentParetoIndividualsMO(RecordedData[] data)
	{
		// Set plot style:
		PlotStyle myPlotStyle = new PlotStyle();
        myPlotStyle.setStyle(Style.POINTS);
        myPlotStyle.setLineWidth(0);
        myPlotStyle.setLineType(NamedPlotColor.BLUE);
        myPlotStyle.setPointType(7);
        myPlotStyle.setPointSize(1);
		
        // Prepare structure:
        /* OLD
        ArrayList<RecordedData> paretoList = new ArrayList<RecordedData>();
        for (RecordedData rd : data)
        {
        	if (rd.isMemberOfParetoSolution)
        		paretoList.add(rd);
        }
		double tab[][];
        tab = new double[paretoList.size()][2];
        
        // Convert:
        int j=0;
        for (int i=0; i<data.length && j<tab.length; i++)
        {
        	tab[j][0] = ((MOSolution)data[i].solution).getObjective(axisX);
            tab[j][1] = ((MOSolution)data[i].solution).getObjective(axisY);
            j++;
        }
       // if (tab.length < 1)
       // 	return null;
        //OLD */
        
        
        /* TEST OLD actual final output:
        double tab2[][];
        tab2 = new double[data[0].paretoFront.length][2];
        int j2 = 0;
        for (SolutionBase sol : data[0].paretoFront)
        {
        	tab2[j2][0] = ((MOSolution)sol).getObjective(axisX);
            tab2[j2][1] = ((MOSolution)sol).getObjective(axisY);
            j2++;
            if (j2>=tab2.length)
            	break;
        } 
        DataSetPlot dsp2 = new DataSetPlot(tab2);
        dsp2.setTitle("Pareto Front");
        dsp2.setPlotStyle(myPlotStyle);
        //TEST */
        
        
        RecordedData[] pareto = GraphDataManager.GetParetoFront(data);
        double tab2[][];
        tab2 = new double[pareto.length][2];
        int j2 = 0;
        for (RecordedData rd : pareto)
        {
        	tab2[j2][0] = ((MOSolutionBase)rd.solution).getObjective(axisX);
            tab2[j2][1] = ((MOSolutionBase)rd.solution).getObjective(axisY);
            j2++;
            if (j2>=tab2.length)
            	break;
        } 
        DataSetPlot dsp2 = new DataSetPlot(tab2);
        dsp2.setTitle("Current Pareto Front");
        dsp2.setPlotStyle(myPlotStyle);
        
        return dsp2;
	}
	
	
	protected DataSetPlot GraphDominatedSpaceCurrentMO(RecordedData[] data)
	{
		// Set plot style:
		PlotStyle myPlotStyle = new PlotStyle();
        myPlotStyle.setStyle(Style.FILLEDCURVES);
		//myPlotStyle.setStyle(Style.LINES);
        myPlotStyle.setLineWidth(1);
        myPlotStyle.setLineType(NamedPlotColor.LIGHT_BLUE);
        //myPlotStyle.setLineType(NamedPlotColor.DARK_BLUE);
        //myPlotStyle.setPointType(2);
        //myPlotStyle.setPointSize(1);
        
        /*ParetoSolution res = new ParetoSolution();
        while (res.size() < n) {
			int index = 0;
			MOSolution selected = candidate.get(0); // it should be a next! (n <= population size!)
			double distance_value = distance_utility
					.distanceToSolutionSetInObjectiveSpace(selected, res);
			int i = 1;
			while (i < candidate.size()) {
				MOSolution next_candidate = candidate.get(i);
				double aux = distance_value = distance_utility.distanceToSolutionSetInObjectiveSpace(next_candidate, res);
				if (aux > distance_value) {
					distance_value = aux;
					index = i;
				}
				i++;
			}

			// add the selected to res and remove from candidate list
			res.add(new MOSolution(candidate.remove(index)));
		}*/
		
        //popSize = GetPopSize(data);
        // Starting population size is usually different from running population size:
        
        // Prepare structure:
        data = GraphDataManager.GetParetoFront(data, axisX, axisY);
		double tab[][];
		ArrayList<double[]> tlist = new ArrayList<double[]>();
		double[] temp;
		double prev;
		//tab = new double[data.length+3][2];
        //tab = new double[data.length+2][2];
        
        // Convert:
        // Minimization:
		temp = new double[]{((MOSolutionBase)data[0].solution).getObjective(axisX), this.maxY};
		prev = ((MOSolutionBase)data[0].solution).getObjective(axisY);
		tlist.add(temp);
        for (int i=0; i<data.length; i++)
        {
        	temp = new double[]{((MOSolutionBase)data[i].solution).getObjective(axisX), prev};
        	tlist.add(temp);
        	temp = new double[]{((MOSolutionBase)data[i].solution).getObjective(axisX), ((MOSolutionBase)data[i].solution).getObjective(axisY)};
        	prev = ((MOSolutionBase)data[i].solution).getObjective(axisY);
        	tlist.add(temp);
        }
        temp = new double[]{this.maxX, ((MOSolutionBase)data[data.length-1].solution).getObjective(axisY)};
    	tlist.add(temp);
    	temp = new double[]{this.maxX, this.maxY};
    	tlist.add(temp);

    	tab = tlist.toArray(new double[0][]);
        
        DataSetPlot dsp = new DataSetPlot(tab);
        dsp.setTitle("MO Dominated Space (this generation)");
        dsp.setPlotStyle(myPlotStyle);
        return dsp;
	}
	
	
	protected DataSetPlot GraphDominatedSpaceSoFarMO(RecordedData[] data)
	{
		// Set plot style:
		PlotStyle myPlotStyle = new PlotStyle();
        myPlotStyle.setStyle(Style.FILLEDCURVES);
		//myPlotStyle.setStyle(Style.LINES);
        myPlotStyle.setLineWidth(1);
        myPlotStyle.setLineType(NamedPlotColor.GREEN);
        //myPlotStyle.setPointType(2);
        //myPlotStyle.setPointSize(1);
        
        /*ParetoSolution res = new ParetoSolution();
        while (res.size() < n) {
			int index = 0;
			MOSolution selected = candidate.get(0); // it should be a next! (n <= population size!)
			double distance_value = distance_utility
					.distanceToSolutionSetInObjectiveSpace(selected, res);
			int i = 1;
			while (i < candidate.size()) {
				MOSolution next_candidate = candidate.get(i);
				double aux = distance_value = distance_utility.distanceToSolutionSetInObjectiveSpace(next_candidate, res);
				if (aux > distance_value) {
					distance_value = aux;
					index = i;
				}
				i++;
			}

			// add the selected to res and remove from candidate list
			res.add(new MOSolution(candidate.remove(index)));
		}*/
		
        //popSize = GetPopSize(data);
        // Starting population size is usually different from running population size:
        
        // Prepare structure:
        data = GraphDataManager.GetParetoFront(data, axisX, axisY);
		double tab[][];
		ArrayList<double[]> tlist = new ArrayList<double[]>();
		double[] temp;
		double prev;
		//tab = new double[data.length+3][2];
        //tab = new double[data.length+2][2];
        
        // Convert:
        // Minimization:
		temp = new double[]{((MOSolutionBase)data[0].solution).getObjective(axisX), this.maxY};
		prev = ((MOSolutionBase)data[0].solution).getObjective(axisY);
		tlist.add(temp);
        for (int i=0; i<data.length; i++)
        {
        	temp = new double[]{((MOSolutionBase)data[i].solution).getObjective(axisX), prev};
        	tlist.add(temp);
        	temp = new double[]{((MOSolutionBase)data[i].solution).getObjective(axisX), ((MOSolutionBase)data[i].solution).getObjective(axisY)};
        	prev = ((MOSolutionBase)data[i].solution).getObjective(axisY);
        	tlist.add(temp);
        }
        temp = new double[]{this.maxX, ((MOSolutionBase)data[data.length-1].solution).getObjective(axisY)};
    	tlist.add(temp);
    	temp = new double[]{this.maxX, this.maxY};
    	tlist.add(temp);

    	tab = tlist.toArray(new double[0][]);
        
        DataSetPlot dsp = new DataSetPlot(tab);
        dsp.setTitle("MO Dominated Space (so far)");
        dsp.setPlotStyle(myPlotStyle);
        return dsp;
	}
	
	
	@SuppressWarnings("unchecked")
	protected DataSetPlot[] GraphFinalParetoFront(RecordedData[] data)
	{
		DataSetPlot[] dsp;
		// If more than 3 objectives or more, plot without connecting, because it's a mesh.
		if (((MOSolutionBase)data[0].solution).getObjectives().length>=3)
			dsp =  new DataSetPlot[1];
		// If it's less than 3 objectives, connect the points because it's a line.
		else
			dsp =  new DataSetPlot[2];
        
		// Points of pareto solution:
		// Set plot style:
		PlotStyle myPlotStyle = new PlotStyle();
		myPlotStyle.setLineWidth(1);
	    myPlotStyle.setLineType(NamedPlotColor.LIGHT_RED);
	    myPlotStyle.setPointType(2);
	    myPlotStyle.setPointSize(1);
		myPlotStyle.setStyle(Style.POINTS);
		
		double tab2[][];
	    tab2 = new double[data[0].paretoFront.length][2];
	    int j2 = 0;
	    for (SolutionBase sol : data[0].paretoFront)
	    {
	    	tab2[j2][0] = ((MOSolutionBase)sol).getObjective(axisX);
	        tab2[j2][1] = ((MOSolutionBase)sol).getObjective(axisY);
	        j2++;
	        if (j2>=tab2.length)
	          	break;
	    } 
	    DataSetPlot dsp1 = new DataSetPlot(tab2);
	    dsp1.setTitle("Final Pareto Front");
	    dsp1.setPlotStyle(myPlotStyle);
	    dsp[0]=dsp1;
		
		// Connect points if less than 3 objectives:
	    // paretoSolution: Runs from lower right to upper left.
		if (dsp.length == 2)
		{
			// Sort unsorted points based on axis:
			//Arrays.sort(data, new RecordedDataComparatorForGraphing(axisX, axisY));
			
			PlotStyle myPlotStyle2 = new PlotStyle();
			myPlotStyle2.setLineWidth(1);
		    myPlotStyle2.setLineType(NamedPlotColor.RED);
		    myPlotStyle2.setPointSize(1);
			myPlotStyle2.setStyle(Style.LINES);
	        
	        MOSolutionBase[] pf = data[0].paretoFront;
	        Arrays.sort(pf, new MOSolutionComparatorForGraphing(axisX, axisY));
	        //double[] prev = new double[]{pf[0].getObjective(axisX), this.maxY};
	        /*double[] prev = new double[]{this.maxY, this.minX};
	        ArrayList<double[]> tab = new ArrayList<double[]>();
	        for (SolutionBase sol : pf)
	        {
	        	//tab.add(new double[]{((MOSolution)sol).getObjective(axisX), prev[1]});
	        	tab.add(new double[]{prev[0], ((MOSolution)sol).getObjective(axisY)});
	        	prev = new double[]{((MOSolution)sol).getObjective(axisX), ((MOSolution)sol).getObjective(axisY)};
	        	tab.add(prev);            
	        } 
	        tab.add(new double[]{prev[0], this.maxY});*/
	        /*if (this.minX == null || this.maxX == null || this.minY == null || this.maxY == null)
	        {
	        	if (this.minX == null)
	        		this.minX = Double.MAX_VALUE;
	        	if (this.maxX == null)
	        		this.maxX = Double.MIN_VALUE;
	        	if (this.minY == null)
	        		this.minY = Double.MAX_VALUE;
	        	if (this.maxY == null)
	        		this.maxY = Double.MIN_VALUE;
	        	double x,y;
	        	for (RecordedData rd : data)
	        	{
	        		x = ((MOSolution)rd.solution).getObjective(axisX);
	        		y = ((MOSolution)rd.solution).getObjective(axisY);
	        		minX = Math.min(minX, x);
	        		maxX = Math.max(maxX, x);
	        		minY = Math.min(minY, y);
	        		maxY = Math.max(maxY, y);
	        	}
	        	minX = Math.floor(minX);
	        	maxX = Math.ceil(maxX);
	        	minY = Math.floor(minY);
	        	maxY = Math.ceil(maxY);
	        }*/
	        double[] prev = new double[]{-Math.pow(10,300), Math.pow(10,300)};							// this.minX, this.maxY can't be handled by JavaPlot / gnuPlot
	        ArrayList<double[]> tab = new ArrayList<double[]>();
	        for (SolutionBase sol : pf)
	        {
	        	tab.add(new double[]{((MOSolutionBase)sol).getObjective(axisX), prev[1]});
	        	prev = new double[]{((MOSolutionBase)sol).getObjective(axisX), ((MOSolutionBase)sol).getObjective(axisY)};
	        	tab.add(prev);            
	        } 
	        tab.add(new double[]{Math.pow(10,300), prev[1]});									// this.maxX can't be handled by JavaPlot / gnuPlot
	        DataSetPlot dsp2 = new DataSetPlot(tab.toArray(new double[0][2]));
	        dsp2.setTitle("");
	        dsp2.setPlotStyle(myPlotStyle2);
	        dsp[1]=dsp2;
		}
		
		return dsp;
	}
	
	
	
	
	
	// Parse pop size from data:
	@Deprecated
	@SuppressWarnings("rawtypes")
	protected long GetPopSize(RecordedData[] data)
	{
		AlgorithmBase alg = data[0].algorithm;
		String pop_size_string = null;
		popSize = GraphEARSStatic.DEFAULT_EVALUATIONS_PER_STEP;
		
		pop_size_string = alg.getAlgorithmInfo().getParameters().get(EnumAlgorithmParameters.POP_SIZE);
		if (pop_size_string == null)
		{
			System.err.println("Error: Could not resolve pop size for algorithm " + alg.getClass().toString() + ".");
			popSizeKnown = false;
		}
		else
		{
			try
			{
				popSize = Long.parseLong(pop_size_string);
				popSizeKnown = true;
			} 
			catch (NumberFormatException ex)
			{
				System.err.println("Error: Could not parse pop size for algorithm " + alg.getClass().toString() + ".");
				popSizeKnown = false;
			}
		}
		return popSize;
	}
	
	
}
