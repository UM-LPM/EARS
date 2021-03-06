package org.um.feri.ears.visualization.graphing.data;

import java.util.ArrayList;

import org.um.feri.ears.visualization.graphing.GraphEARSStatic;
import org.um.feri.ears.visualization.graphing.PlotColorScheme;
import org.um.feri.ears.visualization.graphing.PlotType;

import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.NamedPlotColor;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.RgbPlotColor;
import com.panayotis.gnuplot.style.Style;

public class AlgorithmStatistics
{
	// Vars:
	public int evalsPerStep;
	public RecordedData[][] rawdata = null;
	public RecordedData[][] data = null;
	public double[] fitnessAverage = null;
	public double[] fitnessWorst = null;
	public double[] fitnessBest = null;
	public double[] fitnessSD = null;
	
	
	// Contructors:
	public AlgorithmStatistics(RecordedData[] data)
	{
		this(data, GraphEARSStatic.DEFAULT_EVALUATIONS_PER_STEP);
	}
	public AlgorithmStatistics(RecordedData[] data, int evalsPerStep)
	{
		this.evalsPerStep = evalsPerStep;
		//Problem p = (Problem)data[0].problem;
		
		// Parse to iterations:
		int iterCount=0;
		ArrayList<ArrayList<RecordedData>> tmpData = new ArrayList<ArrayList<RecordedData>>();
		int tmpIteration=-1;
		for (int i=0; i<data.length; i++)
		{
			tmpIteration = (int)data[i].iteration;
			while (tmpIteration>=iterCount)
			{
				tmpData.add(new ArrayList<RecordedData>());
				iterCount = iterCount+1;
			}
			tmpData.get(tmpIteration).add(data[i]);
		}
		this.rawdata = new RecordedData[iterCount][];
		for (int i=0; i<iterCount; i++)
		{
			this.rawdata[i] = tmpData.get(i).toArray(new RecordedData[0]);
		}
		
		// [iteration][interval]
		
		// Split to best each interval each iteration:
		this.data = new RecordedData[iterCount][];
		for (int i1=0; i1<iterCount; i1++)	//iteration
		{
			int intCount = (int)Math.ceil((double)this.rawdata[i1].length/(double)evalsPerStep);
			this.data[i1] = new RecordedData[intCount];
			RecordedData curBestSol = this.rawdata[i1][0];
			double curBestVal = curBestSol.solution.getEval();
			for (int i2=0; i2<intCount; i2++)	//interval
			{
				int startIndex = i2*evalsPerStep;
				int endIndex = Math.min((i2+1)*evalsPerStep,this.rawdata[i1].length)-1;
				this.data[i1][i2]=curBestSol;
				for (int index=startIndex; index<endIndex; index++)	//individual
				{
					if (this.rawdata[i1][index].solution.getEval() < curBestVal)
					//if (p.isFirstBetter(this.rawdata[i1][index].solution.getEval(), curBestVal))
					{
						curBestSol = this.rawdata[i1][index];
						curBestVal = curBestSol.solution.getEval();
						//System.err.println("this.data["+i1+"]["+i2+"] = "+ curBestVal + "        "+curBestVal+" < "+this.data[i1][i2].solution.getEval());
						this.data[i1][i2] = curBestSol;
					}
				}
			}
		}
		
		// Calc all statistics:
		//int iterCount = this.data.length;
		int intCount = this.data[0].length;
		fitnessAverage = new double[intCount];
		fitnessWorst = new double[intCount];
		fitnessBest = new double[intCount];
		fitnessSD = new double[intCount];
		for (int i2=0; i2<intCount; i2++)
		{
			double sum = 0;
			double best = this.data[0][i2].solution.getEval();
			double worst = this.data[0][i2].solution.getEval();
			for (int i1=0; i1<iterCount; i1++)
			{
				double tmp = this.data[i1][i2].solution.getEval();
				sum = sum + tmp;
				if (tmp < best)
				//if (p.isFirstBetter(tmp, best))
					best = tmp;
				else if (worst < tmp)
					worst = tmp;
			}
			double avg = sum/(double)iterCount;
			fitnessAverage[i2] = avg;
			fitnessWorst[i2] = worst;
			fitnessBest[i2] = best;
			double sum2 = 0;
			for (int i1=0; i1<iterCount; i1++)
			{
				sum2 = sum2 + Math.pow((this.data[i1][i2].solution.getEval()-avg), 2);
			}
			fitnessSD[i2] = Math.sqrt(sum2/iterCount);
		}
		
		//
		/*System.err.println("+");System.err.println("+");
		System.err.println("Average;StandardDeviation;Worst;Best");
		for (int i=0; i<fitnessAverage.length; i++)
		{
			System.err.println(fitnessAverage[i]+";"+fitnessSD[i]+";"+fitnessWorst[i]+";"+fitnessBest[i]);
		}
		return;*/
	}
	
	
	public DataSetPlot getDataSetPlot(PlotType gt, PlotColorScheme pcs)
	{
		switch(gt)
		{
		case AVERAGE_OF_ITERATIONS:
			return AverageDSP(pcs);
		case WORST_OF_ITERATIONS:
			return WorstDSP(pcs);
		case BEST_OF_ITERATIONS:
			return BestDSP(pcs);
		case STANDARD_DEVIATION_OF_ITERATIONS:
			return StandardDeviationDSP(pcs);
		default:
			return null;
		}
	}
	
	
	public DataSetPlot AverageDSP(PlotColorScheme pcs)
	{
		// Set plot style:
		PlotStyle ps = new PlotStyle();
		switch(pcs)
		{
		case COLORED:
			ps.setStyle(Style.LINESPOINTS);
			ps.setLineWidth(1);
			ps.setLineType(NamedPlotColor.BLUE);
			ps.setPointType(7);
			ps.setPointSize(1);
			break;
		case GRAYSCALE:
			ps.setStyle(Style.LINESPOINTS);
			ps.setLineWidth(1);
			ps.setLineType(new RgbPlotColor(0,0,0));
			ps.setPointType(7);
			ps.setPointSize(1);
			break;
		}
		        
		// Prepare structure:
		int tc = fitnessAverage.length;
		double tab[][] = new double[tc][2];
		        
		// Convert individual data to generation data:
		for (int i=0; i<tc; i++)
		{
			tab[i][0] = (i+1)*evalsPerStep;
			tab[i][1] = fitnessAverage[i];
		}
		
		DataSetPlot dsp = new DataSetPlot(tab);
		dsp.setTitle("Average");
		dsp.setPlotStyle(ps);
		return dsp;
	}
	
	public DataSetPlot WorstDSP(PlotColorScheme pcs)
	{
		// Set plot style:
		PlotStyle ps = new PlotStyle();
		switch(pcs)
		{
		case COLORED:
			ps.setStyle(Style.LINESPOINTS);
			ps.setLineWidth(1);
			ps.setLineType(NamedPlotColor.RED);
			ps.setPointType(7);
			ps.setPointSize(1);
			break;
		case GRAYSCALE:
			ps.setStyle(Style.LINESPOINTS);
			ps.setLineWidth(1);
			ps.setLineType(new RgbPlotColor(220,220,220));
			ps.setPointType(7);
			ps.setPointSize(1);
			break;
		}
		     
		// Prepare structure:
		int tc = fitnessWorst.length;
		double tab[][] = new double[tc][2];
		        
		// Convert individual data to generation data:
		for (int i=0; i<tc; i++)
		{
			tab[i][0] = (i+1)*evalsPerStep;
			tab[i][1] = fitnessWorst[i];
		}
		
		DataSetPlot dsp = new DataSetPlot(tab);
		dsp.setTitle("Worst");
		dsp.setPlotStyle(ps);
		return dsp;
	}
	
	public DataSetPlot BestDSP(PlotColorScheme pcs)
	{
		// Set plot style:
		PlotStyle ps = new PlotStyle();
		switch(pcs)
		{
		case COLORED:
			ps.setStyle(Style.LINESPOINTS);
			ps.setLineWidth(1);
			ps.setLineType(NamedPlotColor.GREEN);
			ps.setPointType(7);
			ps.setPointSize(1);
			break;
		case GRAYSCALE:
			ps.setStyle(Style.LINESPOINTS);
			ps.setLineWidth(1);
			ps.setLineType(new RgbPlotColor(150,150,150));
			ps.setPointType(7);
			ps.setPointSize(1);
			break;
		}
		        
		// Prepare structure:
		int tc = fitnessBest.length;
		double tab[][] = new double[tc][2];
		        
		// Convert individual data to generation data:
		for (int i=0; i<tc; i++)
		{
			tab[i][0] = (i+1)*evalsPerStep;
			tab[i][1] = fitnessBest[i];
		}
		
		DataSetPlot dsp = new DataSetPlot(tab);
		dsp.setTitle("Best");
		dsp.setPlotStyle(ps);
		return dsp;
	}
	
	public DataSetPlot StandardDeviationDSP(PlotColorScheme pcs)
	{
		// Set plot style:
		PlotStyle ps = new PlotStyle();
		switch(pcs)
		{
		case COLORED:
			ps.setStyle(Style.LINES);
			ps.setLineWidth(1);
			ps.setLineType(NamedPlotColor.ORANGE);
			ps.set("dashtype", "(5,5)");
			ps.setPointType(7);
			ps.setPointSize(1);
			break;
		case GRAYSCALE:
			ps.setStyle(Style.LINES);
			ps.setLineWidth(1);
			ps.setLineType(new RgbPlotColor(110,110,110));
			ps.set("dashtype", "(5,5)");
			ps.setPointType(7);
			ps.setPointSize(1);
			break;
		}
		        
		// Prepare structure:
		int tc1 = fitnessAverage.length;
		int tc2 = fitnessSD.length*2;
		double tab[][] = new double[tc2][2];
		        
		// Convert individual data to generation data:
		for (int i=0; i<tc1; i++)
		{
			tab[i][0] = (i+1)*evalsPerStep;
			tab[i][1] = fitnessAverage[i]+fitnessSD[i];
		}
		for (int i=0; i<tc1; i++)
		{
			int j=tc1-1-i;
			tab[tc1+i][0] = (j+1)*evalsPerStep;
			tab[tc1+i][1] = fitnessAverage[j]-fitnessSD[j];
		}
		
		DataSetPlot dsp = new DataSetPlot(tab);
		dsp.setTitle("Standard Deviation");
		dsp.setPlotStyle(ps);
		return dsp;
	}
	
}
