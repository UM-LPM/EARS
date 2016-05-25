package org.um.feri.ears.problems.misc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.util.SpecialFunction;
import org.um.feri.ears.util.TrapezoidalRule;

public class SoilModelProblem extends Problem{

	double d[];
	double RM[];
	int layers;
	double xx[];
	double y1[];
	Map<Double, Integer> freq = new HashMap<Double, Integer>();
	int call = 1;
	
	public SoilModelProblem(int numberOfDimensions, int numberOfConstraints, int layers, String filename) {
		this(numberOfDimensions, numberOfConstraints);
		
		xx = new double[15000]; // velikost?
		y1 = new double[15000]; // velikost?
		
		this.layers = layers;
		loadData(filename);
		
	}

	public SoilModelProblem(int numberOfDimensions, int numberOfConstraints) {
		super(numberOfDimensions, numberOfConstraints);
		
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
		
		for(int i = 0; i < numberOfDimensions; i++)
		{
			if(i%2 == 0) // resistance
			{
				lowerLimit.set(i, 5.0);
				upperLimit.set(i, 3500.0);
			}
			else // layer thickness
			{
				lowerLimit.set(i, 0.4);
				upperLimit.set(i, 40.0);
			}
		}

		name="Earth Model";
	}


	@Override
	public double eval(List<Double> ds) {

		int j = 0;
		
		double R[] = new double[numberOfDimensions]; // layer resistance
		double h[] = new double[numberOfDimensions]; // layer thickness
		
		for(int i = 0; i < numberOfDimensions; i+=2)
		{
			R[j] = ds.get(i);
			if(i+1 < numberOfDimensions)
				h[j] = ds.get(i+1);
			j++;
		}

		double step = 1e-2; // korak toèk izraèuna vrednosti funkcije (v odvisnosti od števila plasti!)
		double minValue = 1e-6; // ko je vrednost funkcije v 100 zaporednih toèkah manj od minvrednost je izraèun konèan

		boolean isEnd = true;
		int stk = 0;
		double lambda = -step;
		
		double KN[] = new double[layers];
		double alpha[] = new double[layers];
		
		
		double RI[] = new double[d.length];

		for(int k = 0; k < d.length;k++)
		{
			lambda = -step;
			isEnd = true;
			j = 0;
			stk = 0;
	
			while (isEnd && j < 1e6) { // j < 1e6 braked by xx.length

				lambda += step;
				if(j >= xx.length)
					break; // TODO return infinity
				xx[j] = lambda;

				for (int i = layers - 2; i > -1; i--) {
					if(i == layers - 2)
					{
						KN[i] = (R[i+1]-R[i])/(R[i+1]+R[i]);
						alpha[i]=1+(2*KN[i]*Math.exp(-2*lambda*h[i]))/(1-KN[i]*Math.exp(-2*lambda*h[i]));
					}
					else
					{
						KN[i]=(R[i+1]*alpha[i+1]-R[i])/(R[i+1]*alpha[i+1]+R[i]);
						alpha[i]=1+(2*KN[i]*Math.exp(-2*lambda*h[i]))/(1-KN[i]*Math.exp(-2*lambda*h[i]));
					}
				}
				// Bessel first kind order zero
				y1[j] = (alpha[0]-1)*(SpecialFunction.j0(lambda*d[k])-SpecialFunction.j0(2*lambda*d[k]));

				if (Math.abs(y1[j]) < minValue)
					stk=stk+1;
				else
					stk=0;

				if (stk >= 100)
					isEnd = false;
				
				j++;
			}
			
			double f1 = 0.0;
			
			f1 = TrapezoidalRule.integrate(j,xx,y1);
		    //System.out.println("num: "+j);
		    RI[k]=R[0]*(1+2*d[k]*f1);

		}
		
		// Calculate difference
		double CF = 0.0;
		for(int i = 0; i < RI.length; i++)
		{
			//CF+= Math.abs((RI[i] - RM[i]) * (RI[i] - RM[i])); /// RM[i];
			CF+= Math.abs(RI[i] - RM[i])  / RM[i];
		}
		
		CF = (CF / RI.length) * 100;
		return CF;
	}
	
	private void loadData(String filename) {
		
		if(filename != null && !filename.isEmpty())
		{
			
			filename = "test_data/"+ filename +".txt";
			ArrayList<Double> tempD = new ArrayList<>();
			ArrayList<Double> tempRM = new ArrayList<>();
			
			try {
				/* Open the file */
				FileInputStream fis = new FileInputStream(filename);
				InputStreamReader isr = new InputStreamReader(fis);
				BufferedReader br = new BufferedReader(isr);
				
				String aux = br.readLine();
				while (aux != null) {
					StringTokenizer st = new StringTokenizer(aux);
					
					if(st.hasMoreTokens())
					{
						tempD.add(new Double(st.nextToken()));
						tempRM.add(new Double(st.nextToken()));
					}
					aux = br.readLine();
				}
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			d = new double[tempD.size()];
			RM = new double[tempD.size()];
			for(int i = 0; i < tempD.size(); i++)
			{
				d[i] = tempD.get(i);
				RM[i] = tempRM.get(i);
			}
			
		}
		else
		{
			System.out.println("The file name containg the measured data is not valid.");
		}
	}
}
