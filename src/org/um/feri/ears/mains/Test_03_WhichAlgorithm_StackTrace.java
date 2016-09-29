package org.um.feri.ears.mains;

import org.um.feri.ears.algorithms.EnumAlgorithmParameters;

public class Test_03_WhichAlgorithm_StackTrace
{
	public static int some_number = 451;
	
	public static void main(String[] args)
	{
		Test1();
	}
	
	public static void Test1()
	{
		Test2();
	};
	
	public static void Test2()
	{
		// Test:
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		for (StackTraceElement item : stackTraceElements)
		{
			System.out.print(item.getClassName()+".");
			System.out.println(item.getMethodName());
		}
		
		// Blank line:
		System.out.println("");
		
		// Source name:
		{
			StackTraceElement item = stackTraceElements[2];
			System.out.println("SOURCE:");
			System.out.print(item.getClassName()+".");
			System.out.println(item.getMethodName());
		}
		
		// Source parameters:
		{
			//StackTraceElement item = stackTraceElements[2];
			//Test_03_WhichAlgorithm_StackTrace ref = (Test_03_WhichAlgorithm_StackTrace)(item.getClass());	//NOPE
			// NEED:  ai.addParameter(EnumAlgorithmParameters.POP_SIZE, NP + "");
		}
	}
}
