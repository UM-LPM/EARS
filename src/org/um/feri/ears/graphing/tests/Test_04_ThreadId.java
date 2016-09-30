package org.um.feri.ears.graphing.tests;

public class Test_04_ThreadId
{
	static public void main(String[] args)
	{
		Thread currentThread = Thread.currentThread();
		System.out.println("Executing  thread : " + currentThread.getName()) ;
		System.out.println("id of the thread is " + currentThread.getId());   
	}
}

// Thread.currentThread().getId()