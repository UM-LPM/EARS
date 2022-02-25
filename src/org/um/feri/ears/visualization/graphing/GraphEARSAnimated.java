package org.um.feri.ears.visualization.graphing;

import java.awt.Dimension;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import org.um.feri.ears.algorithms.AlgorithmBase;
import org.um.feri.ears.visualization.graphing.data.GraphDataManager;
import org.um.feri.ears.visualization.graphing.data.RecordedData;
import org.um.feri.ears.problems.moo.MOSolutionBase;

import com.panayotis.gnuplot.JavaPlot;




public class GraphEARSAnimated extends GraphEARS
{
	// Vars:
	String uID;
	MOGraphSet graphs;
	protected int numPlots = 0;
	protected boolean isSetAnimationDuration = false;
	protected int animationDuration = 5000;
	protected boolean isSetEvaluationsPerFrame = false;
	protected long evaluationsPerFrame = DEFAULT_ANIMATION_IMAGE_STEP_INDIVIDUAL_COUNT;
	protected boolean isSetFramesPerSecond = false;
	protected double framesPerSecond = DEFAULT_ANIMATION_FRAMES_PER_SECOND;
	RecordedData[][] groupsCurrentData;
	//RecordedData[][] generationAllPreviousData;
	double minX = Double.MAX_VALUE, 
			maxX = Double.MIN_VALUE, 
			minY = Double.MAX_VALUE, 
			maxY = Double.MIN_VALUE;
	String tempPath = "";
	// Constants:
	public final static int NUMBER_OF_PREVIOUS_GENERATIONS_TO_SHOW = 4;
	public final static int PREVIOUS_GENERATIONS_DIM_STEP = 40;
	public final static int DEFAULT_ANIMATION_FRAMES_PER_SECOND = 10;
	public final static int DEFAULT_ANIMATION_IMAGE_STEP_INDIVIDUAL_COUNT = 300;
	
	
	
	
	// Constructor:
	public GraphEARSAnimated()
	{
		this.plot = new JavaPlot();
		uID = UUID.randomUUID().toString();
		tempPath = uID+"/";
	}
	// Constructor:
	public GraphEARSAnimated(RecordedData[] data)
	{
		this();
		this.data = data;
		this.axisX = 0;
		this.axisY = 1;
		setAxisXLabel("obj. "+axisX);
		setAxisYLabel("obj. "+axisY);
		RefreshGraphData();
	}
	// Constructor:
	public GraphEARSAnimated(RecordedData[] data, int axisX, int axisY)
	{
		this();
		this.data = data;
		this.axisX = axisX;
		this.axisY = axisY;
		setAxisXLabel("obj. "+axisX);
		setAxisYLabel("obj. "+axisY);
		RefreshGraphData();
	}
	
	
	
	
	
	// Set output file:
	@Override
	public void setOutputFileAutomatic()
	{
		setOutputFileAutomatic(true);
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
			String tmp = data[0].algorithm.getId()+" solving "+data[0].problemName+" it"+data[0].iteration+" ("+axisX+"x"+axisY+")"+".gif";
			setOutputFilePrivate(tmp, overwrite);
		}
	}
	
	@Override
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
	// Set data:
	@Override
	public void setData(RecordedData[] data)
	{
		super.setData(data);
		if (outputAutomatic)
		{
			String tmp = data[0].algorithm.getId()+" solving "+data[0].problemName+".gif";
			setOutputFilePrivate(tmp, false);
			RefreshGraphData();
		}
	}
	public void setCanvasSize(Dimension canvasSize)
	{
		super.setCanvasSize(canvasSize);
		graphs.setCanvasSize(canvasSize);
	}
	public void setCanvasSize(int width, int height)
	{
		this.setCanvasSize(new Dimension(width, height));
	}
	@Override
	public void setZoomScale(double zoom)
	{
		super.setZoomScale(zoom);
		graphs.setZoomScale(zoom);
	}
	@Override
	public double getZoomScale()
	{
		double t = graphs.getGraph(0).getZoomScale();
		return this.zoom;
	}
	@Override
	public void setAxisXLabel(String axisXLabel)
	{
		this.axisXLabel = axisXLabel;
		if (graphs == null)
			return;
		int l = graphs.getGraphCount();
		for (int i=0; i<l; i++)
		{
			graphs.getGraph(i).setAxisYLabel(axisXLabel);
		}
	}
	@Override
	public void setAxisYLabel(String axisYLabel)
	{
		this.axisYLabel = axisYLabel;
		if (graphs == null)
			return;
		int l = graphs.getGraphCount();
		for (int i=0; i<l; i++)
		{
			graphs.getGraph(i).setAxisYLabel(axisYLabel);
		}
	}
	public int getAnimationDuration()
	{
		return animationDuration;
	}
	public void setAnimationDuration(int animationDuration)
	{
		this.animationDuration = animationDuration;
		this.isSetAnimationDuration = true;
		
		if (isSetEvaluationsPerFrame && !isSetFramesPerSecond)
		{
			framesPerSecond = (Math.ceil(data.length/evaluationsPerFrame)/((double)animationDuration/1000));
		}
		else if (!isSetEvaluationsPerFrame && isSetFramesPerSecond)
		{
			evaluationsPerFrame = (long)Math.ceil(animationDuration/framesPerSecond);
			RefreshGraphData();
		}
		else if (isSetEvaluationsPerFrame && isSetFramesPerSecond)
		{
			framesPerSecond = (Math.ceil(data.length/evaluationsPerFrame)/((double)animationDuration/1000));
			System.err.println("WARNING: Animation settings incompatible. Overriding \"AnimationFramesPerSecond\" to +"+framesPerSecond+".");
		}
	}
	public long getAnimationEvaluationsPerFrame()
	{
		return this.evaluationsPerFrame;
	}
	public void setAnimationEvaluationsPerFrame(long epf)
	{
		this.evaluationsPerFrame = epf;
		RefreshGraphData();
		this.isSetEvaluationsPerFrame = true;
		
		if (isSetAnimationDuration && !isSetFramesPerSecond)
		{
			framesPerSecond = (Math.ceil(data.length/evaluationsPerFrame)/((double)animationDuration/1000));
		}
		else if (!isSetAnimationDuration && isSetFramesPerSecond)
		{
			animationDuration = (int)(Math.ceil(data.length/evaluationsPerFrame)/framesPerSecond*1000);
		}
		else if (isSetAnimationDuration && isSetFramesPerSecond)
		{
			animationDuration = (int)(Math.ceil(data.length/evaluationsPerFrame)/framesPerSecond*1000);
			System.err.println("WARNING: Animation settings incompatible. Overriding \"AnimationDuration\" to "+animationDuration+"ms.");
		}
	}
	public double getAnimationFramesPerSecond()
	{
		return this.framesPerSecond;
	}
	public void setAnimationFramesPerSecond(double fps)
	{
		this.framesPerSecond = fps;
		this.isSetFramesPerSecond = true;
		
		if (isSetAnimationDuration && !isSetEvaluationsPerFrame)
		{
			evaluationsPerFrame = (long)Math.ceil(animationDuration/framesPerSecond);
			RefreshGraphData();
		}
		else if (!isSetAnimationDuration && isSetEvaluationsPerFrame)
		{
			animationDuration = (int)(Math.ceil(data.length/evaluationsPerFrame)/framesPerSecond*1000);
		}
		else if (isSetAnimationDuration && isSetEvaluationsPerFrame)
		{
			animationDuration = (int)(Math.ceil(data.length/evaluationsPerFrame)/framesPerSecond*1000);
			System.err.println("WARNING: Animation settings incompatible. Overriding \"AnimationDuration\" to +"+animationDuration+"ms.");
		}
	}
	
	
	
	
	// Add plot:
	public void Plot(PlotType gt, RecordedData[] data)
	{
		graphs.Plot(gt, data);
	}
	// Add plot:
	public void Plot(PlotType gt)
	{
		numPlots++;
		switch(gt)
		{
		case MO_ANIMATED_PARETO_FRONT_SEARCH:
			PlotMODominatedSpaceSoFar();
			graphs.Plot(PlotType.MO_DOMINATED_SPACE_CURRENT/*, generationAllPreviousData*/);
			//graphs.Plot(GraphType.MOAllIndividuals);
			PlotAllMOIndividuals();
			Plot(PlotType.MO_ANIMATED_FINAL_PARETO_FRONT);
			graphs.Plot(PlotType.MO_PARETO_INDIVIDUALS);
			break;
		case MO_ANIMATED_PARETO_FRONT_SEARCH_ALL_INDIVIDUALS:
			//graphs.Plot(PlotType.MOAllIndividuals);
			PlotAllMOIndividuals();
			break;
		case MO_ANIMATED_PARETO_FRONT_SEARCH_DOMINATED_SPACE_CURRENT:
			graphs.Plot(PlotType.MO_DOMINATED_SPACE_CURRENT/*, generationAllPreviousData*/);
			break;
		case MO_ANIMATED_PARETO_FRONT_SEARCH_DOMINATED_SPACE_SO_FAR:
			//graphs.Plot(PlotType.MODominatedSpaceSoFar);
			PlotMODominatedSpaceSoFar();
			break;
		case MO_ANIMATED_PARETO_FRONT_SEARCH_PARETO_INDIVIDUALS:
			graphs.Plot(PlotType.MO_PARETO_INDIVIDUALS/*, generationAllPreviousData*/);
			break;
		case MO_ANIMATED_FINAL_PARETO_FRONT:
			graphs.Plot(PlotType.MO_FINAL_PARETO_FRONT, data);
			break;
		default:
			System.err.println("WARNING: Unsupported graph type. ("+gt+")");
			numPlots--;
		}
	}
	public void Plot(PlotType gt, RecordedData[][] data)
	{
		Plot(gt, data[0]);
	}
	// Plot multiple generations:
	protected void PlotAllMOIndividuals()
	{
		//int tmpGenCount = NUMBER_OF_PREVIOUS_GENERATIONS_TO_SHOW+1;
		int tmpGenCount = 0;
		int gc=groupsCurrentData.length;
		for (int gi=0; gi<gc; gi++)
		{
			GraphEARSStatic g = (GraphEARSStatic)graphs.getGraph(gi);
			RecordedData[][] tmpData = Arrays.copyOfRange(groupsCurrentData, gi-tmpGenCount, gi+1);
			g.Plot(PlotType.MO_ALL_INDIVIDUALS, tmpData);
			if (tmpGenCount<(NUMBER_OF_PREVIOUS_GENERATIONS_TO_SHOW+1))
				tmpGenCount = tmpGenCount+1;
		}
	}
	// Plot dominated space from all generations so far:
	protected void PlotMODominatedSpaceSoFar()
	{
		ArrayList<RecordedData> paretoList = new ArrayList<RecordedData>();
		RecordedData[] paretoArray = null;
		
		for (GraphEARS g : graphs.getGraphs())
		{
//System.err.print("Initial: "+paretoList.size());
			paretoList.addAll(Arrays.asList(g.getData()));
//System.err.print("Added:   "+paretoList.size());
			paretoArray = GraphDataManager.GetParetoFront(paretoList.toArray(new RecordedData[0]), axisX, axisY);
			paretoList = new ArrayList<RecordedData>(Arrays.asList(paretoArray));
//System.err.println("Cropped: "+paretoList.size());
			g.Plot(PlotType.MO_DOMINATED_SPACE_SO_FAR, paretoArray);
		}
		//a
	}
	
	
	// Flush to output:
	@Override
	public void Flush()
	{
		if (numPlots == 0)
			return;
		
		if (file == null)
			return;
		
		// Create output folder:
		if (!file.getParentFile().exists())
		{
			file.getParentFile().mkdirs();
		}
		
		// Draw all frames:
		graphs.Flush();
		
		// Create animated GIF:
		try
		{
			AnimatedGIFCreator gif = new AnimatedGIFCreator(file);
			int l = graphs.getGraphCount();
			gif.setTimePerFrame(animationDuration/l);
			gif.setLoopAnimation(true);
			for (int i=0; i<l; i++)
			{
				gif.addFrame(graphs.getOutputFile(i));
			}
			gif.close();
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

		// Delete temp folder:
		if (file.getParentFile().exists())
      	{
			File tempPath = new File(file.getParentFile().getAbsolutePath()+"/"+uID);
			//tempPath.deleteOnExit();
			
			//Delete all files in the folder
			String[]entries = tempPath.list();
			for(String s: entries){
			    File currentFile = new File(tempPath.getPath(),s);
			    currentFile.delete();
			}
			//Delete folder
			tempPath.delete();

      	}
	}
	
	
	// Refresh all for subgraphs/static images:
	protected void RefreshGraphData()
	{
		//ParseDataToGenerations(data);
		ParseDataToImageSteps(data);
		SetUpStaticImages();
		setZoomScale(zoom);
		//System.err.println("OK!");
	}
	
	
	// Split input data into sub populations:
	@Deprecated
	protected void ParseDataToGenerations(RecordedData[] data)
	{
		//RecordedData[][] generationData;
		//double fromX, toX, fromY, toY;
		
		ArrayList<RecordedData[]> generationList = new ArrayList<RecordedData[]>();
		ArrayList<RecordedData> dataList = new ArrayList<RecordedData>();
		
		//ArrayList<RecordedData> dataAllList = new ArrayList<RecordedData>();
		//ArrayList<RecordedData[]> generationAllList = new ArrayList<RecordedData[]>();
		
		this.popSize = GetPopSize(data);//*5;	// *5 for DEBUG ONLY!!!!!!
		//long paretoSize = data[0].paretoFront.length;
		long popCounter = popSize - (data.length%popSize);	// Fix incase of different starting population.
		double tmpX;
		double tmpY;
		for (int i=0; i<data.length; i++)
		{
			dataList.add(data[i]);
			popCounter++;
			
			// Update min/max:
			MOSolutionBase tmp = (MOSolutionBase)data[i].solution;
			tmpX = tmp.getObjective(axisX);
			tmpY = tmp.getObjective(axisY);
			if (tmpX < minX) 
				minX = tmpX;
			else if (tmpX > 
				maxX) maxX = tmpX;
			if (tmpY < minY) 
				minY = tmpY;
			else if (tmpY > maxY) 
				maxY = tmpY;
			
			if (popCounter>=popSize)
			{
				generationList.add(dataList.toArray(new RecordedData[0]));
				//dataAllList.addAll(dataList);
				//generationAllList.add(dataAllList.toArray(new RecordedData[0]));
				dataList.clear();
				popCounter = 0;
			}
		}
		if (popCounter>0)
		{
			generationList.add(dataList.toArray(new RecordedData[0]));
			//dataAllList.addAll(dataList);
			//generationAllList.add(dataAllList.toArray(new RecordedData[0]));
		}
		groupsCurrentData = generationList.toArray(new RecordedData[0][]);
		//generationAllPreviousData = generationAllList.toArray(new RecordedData[0][]);
		
		// Update min/max:
		/*SolutionBase[] pf = data[0].paretoFront;
		for (int i=0; i<pf.length; i++)
		{
			tmp = (MOSolution)pf[i];
			tmpX = tmp.getObjective(axisX);
			tmpY = tmp.getObjective(axisY);
			if (tmpX < minX) 
				minX = tmpX;
			else if (tmpX > 
				maxX) maxX = tmpX;
			if (tmpY < minX) 
				minY = tmpY;
			else if (tmpY > maxY) 
				maxY = tmpY;
		}*/
		//minX = Math.min(minX, 0.0);
		//minY = Math.min(minY, 0.0);
		minX = Math.floor(minX);
		maxX = Math.ceil(maxX);
		minY = Math.floor(minY);
		maxY = Math.ceil(maxY);
		fullMinX = minX;
		fullMaxX = maxX;
		fullMinY = minY;
		fullMaxY = maxY;
		
		//System.err.println("TEST! "+popSize+"  "+paretoSize);
	}
	
	
	// Split input data into sub groups based on image step:
	protected void ParseDataToImageSteps(RecordedData[] data)
	{
		ArrayList<RecordedData[]> stepList = new ArrayList<RecordedData[]>();
		ArrayList<RecordedData> dataList = new ArrayList<RecordedData>();
		
		//ArrayList<RecordedData> dataAllList = new ArrayList<RecordedData>();
		//ArrayList<RecordedData[]> generationAllList = new ArrayList<RecordedData[]>();

		long groupCounter = 0;
		MOSolutionBase tmp;
		double tmpX;
		double tmpY;
		for (int i=0; i<data.length; i++)
		{
			dataList.add(data[i]);
			groupCounter++;
			
			// Update min/max:
			tmp = (MOSolutionBase)data[i].solution;
			tmpX = tmp.getObjective(axisX);
			tmpY = tmp.getObjective(axisY);
			if (tmpX < minX) 
				minX = tmpX;
			else if (tmpX > 
				maxX) maxX = tmpX;
			if (tmpY < minY) 
				minY = tmpY;
			else if (tmpY > maxY) 
				maxY = tmpY;
			
			if (groupCounter>=evaluationsPerFrame)
			{
				stepList.add(dataList.toArray(new RecordedData[0]));
				//dataAllList.addAll(dataList);
				//generationAllList.add(dataAllList.toArray(new RecordedData[0]));
				dataList.clear();
				groupCounter = 0;
				//break; //SIGNLE GROUP FOR FAST DEBUG
			}
		}
		if (groupCounter>0)
		{
			stepList.add(dataList.toArray(new RecordedData[0]));
			//dataAllList.addAll(dataList);
			//generationAllList.add(dataAllList.toArray(new RecordedData[0]));
		}
		groupsCurrentData = stepList.toArray(new RecordedData[0][]);
		//generationAllPreviousData = generationAllList.toArray(new RecordedData[0][]);
		
		// Update min/max:
		/*SolutionBase[] pf = data[0].paretoFront;
		for (int i=0; i<pf.length; i++)
		{
			tmp = (MOSolution)pf[i];
			tmpX = tmp.getObjective(axisX);
			tmpY = tmp.getObjective(axisY);
			if (tmpX < minX) 
				minX = tmpX;
			else if (tmpX > 
				maxX) maxX = tmpX;
			if (tmpY < minX) 
				minY = tmpY;
			else if (tmpY > maxY) 
				maxY = tmpY;
		}*/
		//minX = Math.min(minX, 0.0);
		//minY = Math.min(minY, 0.0);
		minX = Math.floor(minX);
		maxX = Math.ceil(maxX);
		minY = Math.floor(minY);
		maxY = Math.ceil(maxY);
		fullMinX = minX;
		fullMaxX = maxX;
		fullMinY = minY;
		fullMaxY = maxY;
		
		//System.err.println("TEST! "+popSize+"  "+paretoSize);
	}
	
	
	// Set up static images for each generation:
	protected void SetUpStaticImages()
	{
		graphs = new MOGraphSet(groupsCurrentData, 1, axisX, axisY);
		int l = graphs.graphs.size();
		GraphEARS g;
		String title = groupsCurrentData[0][0].algorithm.getId().replaceAll("_", "\\_") + " solving " + groupsCurrentData[0][0].problemName;
		for (int i=0; i<l; i++)
		{
			g = graphs.getGraph(i);
			g.setOutputFile(tempPath+i+".png", true);
			g.Title = title + " ("+(i+1)+"/"+l+")";
			g.setBounds(fullMinX, fullMaxX, fullMinY, fullMaxY);
			g.setAxisXLabel(axisXLabel);
			g.setAxisYLabel(axisYLabel);
			g.setCanvasSize(CanvasSize);
		}
	}
	
	
	
	// Parse pop size from data:
	@SuppressWarnings("rawtypes")
	@Deprecated
	protected long GetPopSize(RecordedData[] data)
	{
		AlgorithmBase alg = data[0].algorithm;
		String pop_size_string = null;
		popSize = GraphEARSStatic.DEFAULT_EVALUATIONS_PER_STEP;
		
		pop_size_string = alg.getParameterValue("popSize");
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
		
		
	// DEBUG:
	public String GetGeneratedScript()
	{
		return plot.getCommands();
	}
	

	
}
