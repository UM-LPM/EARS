package org.um.feri.ears.mine.graphing;

import javax.imageio.*;
import javax.imageio.metadata.*;
import javax.imageio.stream.*;

import java.awt.Image;
import java.awt.image.*;
import java.io.*;
import java.nio.file.Paths;
import java.util.Iterator;

public class AnimatedGIFCreator 
{
	protected File file;
	protected ImageOutputStream ios = null;
	protected ImageWriter iw;
	protected ImageWriteParam iwp;
	protected IIOMetadata meta;
	// GIF settings (DEFAULT):
	protected Integer imgType = null;
	protected int msPerFrame = 25;	//*10 for actual
	protected boolean loopAnimation = true;
	
  
  
  	public int getTimePerFrame()
	{
		return msPerFrame;
	}
	public void setTimePerFrame(int msPerFrame)
	{
		this.msPerFrame = msPerFrame/10;
	}
	public boolean getLoopAnimation()
	{
		return loopAnimation;
	}
	public void setLoopAnimation(boolean loopAnimation)
	{
		this.loopAnimation = loopAnimation;
	}
	
	
	// Constructors:
  	public AnimatedGIFCreator(String filename) throws IOException
  	{
  		this(filename, true);
  	}
  	public AnimatedGIFCreator(String filename, boolean overwrite) throws IOException
  	{
  		if (!Paths.get(filename).isAbsolute())
		{
			filename = System.getProperty("user.dir")+"/output/"+filename;
		}
		file = new File(filename);
		
		if (!file.exists())
	  	{
			file.getParentFile().mkdirs();
			//file.createNewFile();
	  	}
	  	else if (!overwrite)
	  	{
	  		String orgName = filename;
	  		int num=1;
	  		do
	  		{
	  			int i = orgName.contains(".") ? orgName.lastIndexOf('.') : orgName.length();
	  			String filename2 = orgName.substring(0, i) + " - Copy ("+num+")"+ orgName.substring(i);
	  			//file = new File(System.getProperty("user.dir")+"/output/"+filename2);
	  			file = new File(filename2);
	  			num++;
	  		} while (file.exists());
	  	}
  	}
	public AnimatedGIFCreator(File outputfile) throws IIOException, IOException
  	{
  		file = outputfile;
  	}
  	
  	
  	
  	
  	public void Initialize(File outputfile) throws IIOException, IOException
  	{
  		if (ios==null)
  			ios = new FileImageOutputStream(outputfile);

	    iw = getImageWriter(); 
	    iwp = iw.getDefaultWriteParam();
	    ImageTypeSpecifier its = ImageTypeSpecifier.createFromBufferedImageType(imgType);
	    meta = iw.getDefaultImageMetadata(its, iwp);
	
	    String metaFormatName = meta.getNativeMetadataFormatName();
	    IIOMetadataNode root = (IIOMetadataNode)meta.getAsTree(metaFormatName);
		IIOMetadataNode temp = null;
		
	    temp = getNodeNamed(root, "GraphicControlExtension");
	    temp.setAttribute("disposalMethod", "none");
	    temp.setAttribute("userInputFlag", "FALSE");
	    temp.setAttribute("transparentColorFlag", "FALSE");
	    temp.setAttribute("delayTime", Integer.toString(msPerFrame));
	    temp.setAttribute("transparentColorIndex", "0");
	
	    //temp = getNodeNamed(root, "CommentExtensions");
	    //temp.setAttribute("CommentExtension", "Created by MAH");
	
	    temp = getNodeNamed(root, "ApplicationExtensions");
	    IIOMetadataNode child = new IIOMetadataNode("ApplicationExtension");
	    child.setAttribute("applicationID", "NETSCAPE");
	    child.setAttribute("authenticationCode", "2.0");
	    int loop = loopAnimation ? 0 : 1;
	    child.setUserObject(new byte[]{ 0x1, (byte)(loop & 0xFF), (byte)((loop>>8) & 0xFF)});
	    temp.appendChild(child);
	
	    meta.setFromTree(metaFormatName, root);
	
	    iw.setOutput(ios);
	    iw.prepareWriteSequence(null);
  	}
  	
  	
  	public void addFrame(String filename) throws IOException
  	{
  		BufferedImage img = ImageIO.read(new File(filename));
  		addFrame(img);
  	}
  	public void addFrame(RenderedImage img) throws IOException 
  	{
  		if (imgType == null)
  		{
  			imgType = ((BufferedImage)img).getType();
  			Initialize(file);
  		}
  		IIOImage ioimg = new IIOImage(img, null, meta);
  		iw.writeToSequence(ioimg, iwp);
  	}
  	
  	
  	public void close() throws IOException 
  	{
    	iw.endWriteSequence();
    	ios.close();
  	}
  	
  	
  	
  	protected static ImageWriter getImageWriter() throws IIOException 
  	{
    	Iterator<ImageWriter> iter = ImageIO.getImageWritersBySuffix("gif");
    	if(!iter.hasNext()) 
    	{
      		throw new IIOException("No GIF Image Writers Exist");
    	} 
    	else 
    	{
      		return iter.next();
    	}
  	}
  	protected static IIOMetadataNode getNodeNamed(IIOMetadataNode rootNode, String nodeName) 
  	{
  		// Find existing:
    	int nNodes = rootNode.getLength();
    	for (int i = 0; i < nNodes; i++) 
    	{
    	  	if (rootNode.item(i).getNodeName().compareToIgnoreCase(nodeName) == 0) 
    	  	{
        		return ((IIOMetadataNode)rootNode.item(i));
     	 	}
    	}
    	// If doesn't exist, add:
   		IIOMetadataNode node = new IIOMetadataNode(nodeName);
    	rootNode.appendChild(node);
    	return(node);
  }
  	
  	
}
