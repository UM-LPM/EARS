/*	
 * Copyright 2011, Robin Roche
 * This file is part of jica.

    jica is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    jica is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with jica.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.um.feri.ears.algorithms.so.ica;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.um.feri.ears.problems.DoubleSolution;


/**
 * Contains several methos on arrays used by the ICA algorithm
 * @author Robin Roche
 */
public class ICAUtils 
{

	/**
	 * Returns the index of the max value contained in the vector
	 * @param vector values
	 * @return the index of the max value
	 */
	public int getMaxIndex(double[] vector)
	{
		double max = Double.MIN_VALUE;
		int i;
		int bestIndex = 0;
		for(i=0; i<vector.length; i++) 
		{
			if(vector[i] > max)
			{
				max = vector[i];
				bestIndex = i;
			}
		}
		return bestIndex;
	}


	/**
	 * Returns the mean value of a vector
	 * @param colonies the vector
	 * @return the mean value
	 */
	public double getMean(DoubleSolution[] colonies) 
	{
		if(colonies.length == 0)
			return 0;
			
		double sum = 0;
		for (int i=0; i<colonies.length; i++) 
		{
			sum += colonies[i].getEval();
		}
		return sum / colonies.length;
	}



	/**
	 * Returns the norm of a vector
	 * @param vector the vector
	 * @return the norm
	 */
	public double getNorm(double[] vector) 
	{
		double sum = 0;		
		for(int i=0; i<vector.length; i++)
		{
			sum = sum + Math.pow(vector[i],2);
		}
		return Math.sqrt(sum);
	}



	/**
	 * Returns the sum of the elements on a vector
	 * @param vector the vector
	 * @return the sum
	 */
	public double getSum(double[] vector)
	{
		double sum = 0;
		for (double i : vector) 
		{
			sum += i;
		}
		return sum;
	}



	/**
	 * Returns the sum of the elements on a vector
	 * @param vector the vector
	 * @return the sum
	 */
	public int getSum(int[] vector)
	{
		int sum = 0;
		for (int i : vector) 
		{
			sum += i;
		}
		return sum;
	}

	/**
	 * Prints the values of an array
	 * @param arrayName
	 * @param array
	 */
	public void printArray(String arrayName, double[] array)
	{
		System.out.println(arrayName + ": " + Arrays.toString(array));
	}



	/**
	 * Prints the values of an array
	 * @param arrayName
	 * @param array
	 */
	public void printArray(String arrayName, double[][] array)
	{
		for(int i=0; i<array.length; i++)
		{
			System.out.println(arrayName + "[" + i + "]: " + Arrays.toString(array[i]));
		}
	}



	/**
	 * Prints the values of an array
	 * @param arrayName
	 * @param array
	 */
	public void printArray(String arrayName, int[] array) 
	{
		System.out.println(arrayName + ": " + Arrays.toString(array));
	}



	public DoubleSolution getMax(DoubleSolution[] allImperialists) {
		
		DoubleSolution max = allImperialists[0];
		for (int i = 1; i < allImperialists.length; i++) {
			
			if(allImperialists[i].getEval() > max.getEval()){
				max = allImperialists[i];
			}
		}
		
		return max;
	}



	public int getMinIndex(DoubleSolution[] colonies) {
		
		double min = Double.MAX_VALUE;
		int bestIndex = 0;
		for(int i=0; i < colonies.length; i++) 
		{
			if(colonies[i].getEval() < min)
			{
				min = colonies[i].getEval();
				bestIndex = i;
			}
		}
		return bestIndex;
	}
}
