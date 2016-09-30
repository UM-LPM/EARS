package org.um.feri.ears.graphing.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;


public class GraphDataSet
{
	// Variables:
	protected ArrayList<RecordedData[]> Subsets;
	
	
	// Get/Set
	public RecordedData[][] getSubsets()
	{
		return Subsets.toArray(new RecordedData[0][]);
	}
	public void setSubsets(RecordedData[][] datasets)
	{
		Subsets = new ArrayList<RecordedData[]>(Arrays.asList(datasets));
	}
	
	
	// Constructor:
	public GraphDataSet()
	{
		Subsets = new ArrayList<RecordedData[]>(); 
	}
	// Constructor:
	public GraphDataSet(RecordedData[][] allData)
	{
		Subsets = new ArrayList<RecordedData[]>(Arrays.asList(allData));
	}
	
	
	// Add:
	public void add(RecordedData[] newdataset)
	{
		Subsets.add(newdataset);
	}
	public void add(GraphDataSet other)
	{
		RecordedData[][] otherSets = other.getSubsets();
		for (int i=0; i<otherSets.length; i++)
		{
			Subsets.add(otherSets[i]);
		}
	}
}
