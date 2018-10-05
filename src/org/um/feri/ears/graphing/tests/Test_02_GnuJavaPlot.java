package org.um.feri.ears.graphing.tests;

import java.io.*;
import javax.imageio.ImageIO;

import org.um.feri.ears.examples.RunMain;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.terminal.ImageTerminal;

 

public class Test_02_GnuJavaPlot 
{

    public static void main(String[] args) 
    {
        //JavaPlot p = Test1_DoesItPlot();
        //String cwd = Test2_CWD();
        Test3_SavePlot(); 
        //Test4_OverrideJavaClasses();
    }

    
    // Prvi test, sploh zazna javaplot?
    public static JavaPlot Test1_DoesItPlot()
    {
	    JavaPlot p = new JavaPlot();
	    p.addPlot("sin(x)");
	    p.plot();
	    
	    return p;
    }

    
    // CWD test:
    public static String Test2_CWD()
    {
    	//System.out.println("Working Directory = " + System.getProperty("user.dir"));
    	
    	return System.getProperty("user.dir");
    }
    
    
    // Save plot to image file in folder:
    public static void Test3_SavePlot()
    {
    	// http://stackoverflow.com/questions/9806354/how-make-output-png-file-in-javaplot
    	
    	// Priprava izhodne datoteke:
    	ImageTerminal png = new ImageTerminal();
        File file = new File(System.getProperty("user.dir")+"/_output/"+"out_test.png");
        try 
        {
        	if (!file.exists())
        	{
        		file.getParentFile().mkdirs();
        		file.createNewFile();
        	}
            png.processOutput(new FileInputStream(file));
        } 
        catch (FileNotFoundException ex) 
        {
            System.err.println(ex);
        } 
        catch (IOException ex) 
        {
            System.err.println(ex);
        }    	
        
        System.out.println("HELLO! 44");
        
        // Izdelava grafa:
        JavaPlot p = new JavaPlot();
        p.setTerminal(png);
        p.setPersist(false);
        p.addPlot("cos(x)");
	    p.plot();
	    
	    System.out.println("HELLO! 55");
	    
	    // Dejanski zapis v datoteko:
	    try 
	    {
	        ImageIO.write(png.getImage(), "png", file);
	    } 
	    catch (IOException ex) 
	    {
	        System.err.print(ex);
	    }
	    
	    System.out.println("HELLO! 66");
    }


    // Bi �lo overridat source iz drugega projekta?
    public static void Test4_OverrideJavaClasses()
    {
    	//RunMain r =  new RunMain(false, false, null);
    	//System.out.println(r.isPrintDebug());
    	// Drotpir�na = OK!
    }
}
