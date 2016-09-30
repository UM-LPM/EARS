package org.um.feri.ears.graphing;

import com.panayotis.gnuplot.style.PlotStyle;



public class GraphLineStyleChanger
{
	public PlotStyle ps;
	public String key;
	public String value;
	private double[] baseValues;
	
	
	// Constructors:
	public GraphLineStyleChanger(PlotStyle ps, String key, String value)
	{
		this.ps = ps;
		this.key = key;
		this.value = value;
	}
	public GraphLineStyleChanger(PlotStyle ps, String key, String value, double[] baseValues)
	{
		this.ps = ps;
		this.key = key;
		this.value = value;
		this.baseValues = baseValues;
	}
	
	
	public void setBaseValues(double[] baseValues)
	{
		this.baseValues = baseValues;
	}
	
	
	public void Replacement(String[] replacementValues)
	{
		String tmp = value;
		int replacementIndex = 0;
		while(tmp.indexOf("_") >= 0)
		{
			if (replacementIndex >= replacementValues.length)
				replacementIndex = 0;
			tmp = tmp.replaceFirst("_", replacementValues[replacementIndex++]);
		}
		ps.set(key, tmp);
	}
	
	
	public void Scaling(double[] scales)
	{
		String tmp = value;
		int baseIndex = 0;
		int scalingIndex = 0;
		while(tmp.indexOf("_") >= 0)
		{
			if (baseIndex >= baseValues.length)
				baseIndex = 0;
			if (scalingIndex >= scales.length)
				scalingIndex = 0;
			tmp = tmp.replaceFirst("_", (baseValues[baseIndex++]*scales[scalingIndex++])+"");
		}
		ps.set(key, tmp);
	}
	
}
