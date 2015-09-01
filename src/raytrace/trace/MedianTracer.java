package raytrace.trace;

import java.util.Arrays;
import java.util.Comparator;

import math.ray.Ray;
import raster.PixelBuffer;
import raytrace.camera.Camera;
import raytrace.color.Color;
import raytrace.data.RenderData;
import raytrace.framework.Tracer;

@Deprecated
public class MedianTracer implements Tracer {
	
	/*
	 * A tracer for passing a median filter over a pixel buffer
	 * Used to reduce salt and pepper noise in low sample renders
	 * 
	 * TODO: Update the use the render buffer
	 */

	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected PixelBuffer intermediate;
	protected Object intermediateLock = new Object();
	protected boolean bufferCopyComplete = false;
	protected MedianComparator comparator;

	protected int kernelWidth = 6;
	protected int kernelHeight = 6;
	

	/* *********************************************************************************************
	 * Constructors
	 * *********************************************************************************************/
	public MedianTracer()
	{
		super();
		
		this.comparator = new MedianComparator();
		this.kernelWidth = 3;
		this.kernelHeight = 3;
	}
	
	public MedianTracer(int kernelWidth, int kernelHeight)
	{
		super();
		
		this.comparator = new MedianComparator();
		this.kernelWidth = kernelWidth;
		this.kernelHeight = kernelHeight;
	}
	

	/* *********************************************************************************************
	 * Configuration
	 * *********************************************************************************************/
	@Override
	public void trace(RenderData data)
	{
		PixelBuffer pixelBuffer = data.getPixelBuffer(); 
		Camera camera = data.getCamera();
		
		//Pixels
		int[] target = pixelBuffer.getPixels();
		int[] source = null;
		
		//Set up the intermediate pixel buffer
		//If it is not the same size as the incoming one, create a new one
		//And if this is the first thread to get here, copy the pixel buffer
		bufferCopyComplete = false;
		synchronized(intermediateLock)
		{
			if(intermediate == null || intermediate.getWidth() != pixelBuffer.getWidth() || intermediate.getHeight() != pixelBuffer.getHeight())
				intermediate = new PixelBuffer(pixelBuffer.getWidth(), pixelBuffer.getHeight());
			
			if(!bufferCopyComplete)
			{
				source = intermediate.getPixels();
				
				System.arraycopy(target, 0, source, 0, target.length);
				
				bufferCopyComplete = true;
			}
		}
		
		
		//ray count and color storage for super sampling support
		int width = pixelBuffer.getWidth();
		int height = pixelBuffer.getHeight();
		int x = 0;
		int y = 0;
		
		int kx = 0;
		int ky = 0;
		
		int arrIndex = 0;
		Color[] colors = new Color[kernelWidth * kernelHeight];
		for(int c = 0; c < colors.length; c++)
			colors[c] = new Color();
		
		//For each ray, calculate the pixel color
		for(Ray rays : camera)
		{	
			x = rays.getPixelX();
			y = rays.getPixelY();
			arrIndex = 0;
			
			//Do median filter
			for(int j = -(kernelHeight/2); j < ((kernelHeight+1)/2); j++)
			{
				ky = (y + j);
				if(ky < 0 || ky >= height)
					continue;
				
				for(int i = -(kernelWidth/2); i < ((kernelWidth+1)/2); i++)
				{
					kx = (x + i);
					if(kx < 0 || kx >= width)
						continue;
					
					colors[arrIndex].setARGB(source[kx + ky * width]);
					++arrIndex;
				}
			}
			
			//Sort the colors
			Arrays.sort(colors, comparator);
			
			//Set the color
			target[x + y * width] = colors[colors.length / 2].argb24();
		}
		
	}


	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/

	private class MedianComparator implements Comparator<Color>
	{
		@Override
		public int compare(Color o1, Color o2) {
			return (o2.intensity3() > o1.intensity3()) ? -1 : (o2.intensity3() < o1.intensity3()) ? 1 : 0;
		}
	}

}