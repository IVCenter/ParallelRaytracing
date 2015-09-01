package raster;

import process.logging.Logger;

public class RenderBuffer {
	
	/*
	 * A buffer for storing render data for each pixel of the render frame
	 */

	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected int width;
	protected int height;
	protected Pixel[] pixels;

	
	/* *********************************************************************************************
	 * Constructors
	 * *********************************************************************************************/
	public RenderBuffer(int width, int height)
	{
		this.width = width;
		this.height = height;
		
		pixels = new Pixel[width * height];
		
		for(int i = 0; i < pixels.length; ++i)
			pixels[i] = new Pixel();
	}

	
	/* *********************************************************************************************
	 * Copy method
	 * *********************************************************************************************/
	public void copy(RenderBuffer rb)
	{
		Pixel[] otherPixels = rb.pixels;
		
		if(width != rb.width || height != rb.height)
			Logger.warning(101, "Pixel buffer sizes [" + width + "x" + height + "] and [" + rb.width + "x" + rb.height + "] do not match");
		
		for(int i = 0; i < pixels.length; ++i)
			pixels[i].copy(otherPixels[i]);
	}
	
	
	/* *********************************************************************************************
	 * Getters
	 * *********************************************************************************************/
	public Pixel[] getPixels() { return pixels; }


	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	
}
