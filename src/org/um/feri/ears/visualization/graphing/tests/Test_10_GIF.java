package org.um.feri.ears.visualization.graphing.tests;

import java.io.File;

import org.um.feri.ears.visualization.graphing.AnimatedGIFCreator;


public class Test_10_GIF
{
	public static void main(String[] args) 
	{
		try
		{
			String[] inputFiles = new String[]{"_gif/in1.png", "_gif/in2.png", "_gif/in3.png"};
			File output = new File("_gif/out.gif");

			AnimatedGIFCreator gif = new AnimatedGIFCreator(output);
			for(int i=0; i<inputFiles.length; i++) 
			{
				gif.addFrame(inputFiles[i]);
			}
			gif.close();
		}
		catch (Exception ex)
		{
			System.err.println(ex.getMessage());
		}
	}
}
