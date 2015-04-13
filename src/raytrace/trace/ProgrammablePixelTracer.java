package raytrace.trace;

import java.util.ArrayList;

import math.ray.Ray;
import raster.Pixel;
import raster.PixelBuffer;
import raster.RenderBuffer;
import raster.pixel.PixelTransform;
import raytrace.camera.Camera;
import raytrace.color.Color;
import raytrace.data.RenderData;
import raytrace.framework.Tracer;

public class ProgrammablePixelTracer implements Tracer {
	
	/*
	 * A tracer for outlines, creases, intersections, etc.
	 * Based on this paper: http://www.sci.utah.edu/~roni/research/projects/NPR-lines/NPR-lines.NPAR09.pdf
	 */

	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected ArrayList<PixelTransform> transforms;
	

	/* *********************************************************************************************
	 * Constructors
	 * *********************************************************************************************/
	public ProgrammablePixelTracer()
	{
		super();
		
		transforms = new ArrayList<PixelTransform>();
	}
	

	/* *********************************************************************************************
	 * Configuration
	 * *********************************************************************************************/
	@Override
	public void trace(RenderData data)
	{
		PixelBuffer pixelBuffer = data.getPixelBuffer();
		RenderBuffer outputRenderBuffer = data.getOutputRenderBuffer();
		RenderBuffer inputRenderBuffer = data.getInputRenderBuffer();
		Camera camera = data.getScene().getActiveCamera();
		
		//Pixels
		int[] pb_Pixels = pixelBuffer.getPixels();
		Pixel[] irb_Pixels = inputRenderBuffer.getPixels();
		Pixel[] orb_Pixels = outputRenderBuffer.getPixels();
		int renderWidth = outputRenderBuffer.getWidth();
		
		//ray count and color storage for super sampling support
		Color color = new Color();
		Pixel pixel = null, oPixel = null;
		int pixelIndex;
		
		//For each ray, calculate the pixel color
		for(Ray rays : camera)
		{
			//Get the pixel index
			pixelIndex = rays.getPixelX() + rays.getPixelY() * renderWidth;
			
			//Get the input pixel
			pixel = irb_Pixels[pixelIndex];
			oPixel = orb_Pixels[pixelIndex];
			
			//Iterate over the transforms, each operating on the output of the previous
			for(PixelTransform transform : transforms)
			{
				color = transform.transform(pixel);
				pixel = oPixel;
				pixel.getColor().set(color);
			}
			
			//Update the pixel buffer and render buffer
			pb_Pixels[pixelIndex] = color.rgb32();
		}
		
	}


	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public ArrayList<PixelTransform> getTransforms() {
		return transforms;
	}

	public void setTransforms(ArrayList<PixelTransform> transforms) {
		this.transforms = transforms;
	}
	
	public void addTransform(PixelTransform transform) {
		this.transforms.add(transform);
	}

}