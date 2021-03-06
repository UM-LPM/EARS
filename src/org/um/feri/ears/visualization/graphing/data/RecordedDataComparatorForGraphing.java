package org.um.feri.ears.visualization.graphing.data;

import java.util.Comparator;
import org.um.feri.ears.problems.moo.MOSolutionBase;




@SuppressWarnings("rawtypes")
public class RecordedDataComparatorForGraphing implements Comparator<RecordedData>
{
	int objectiveIndexes[] = null;
	
	public RecordedDataComparatorForGraphing()
	{}
	
	public RecordedDataComparatorForGraphing(int axisX, int axisY)
	{
		objectiveIndexes = new int[]{axisX, axisY};
	}
	
	public RecordedDataComparatorForGraphing(int[] objectiveIndexes)
	{
		this.objectiveIndexes = objectiveIndexes;
	}
	
	
	
	public int compare(RecordedData obj1, RecordedData obj2) 
	{

			double[] o1 = ((MOSolutionBase)(obj1.solution)).getObjectives();
			double[] o2 = ((MOSolutionBase)(obj2.solution)).getObjectives();
			if (objectiveIndexes==null)
			{
				for (int i=0; i<o1.length; i++)
				{
					if (o1[i]<o2[i])
						return -1;
					else if (o1[i]>o2[i])
						return 1;
				}
			}
			else
			{
				for (int i=0; i<objectiveIndexes.length; i++)
				{
					if (o1[objectiveIndexes[i]]<o2[objectiveIndexes[i]])
						return -1;
					else if (o1[objectiveIndexes[i]]>o2[objectiveIndexes[i]])
						return 1;
				}
			}

		return 0;
	}
}
