package raytrace.trace;

import java.util.ArrayList;

import math.ray.Ray;
import raster.PixelBuffer;
import raster.pixel.PixelTransform;
import raytrace.camera.Camera;
import raytrace.color.Color;
import raytrace.framework.Tracer;
import raytrace.scene.Scene;

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
	public void trace(PixelBuffer pixelBuffer, Camera camera, Scene scene)
	{
		//Pixels
		int[] pixels = pixelBuffer.getPixels();
		
		//ray count and color storage for super sampling support
		Color color = new Color();
		
		
		//For each ray, calculate the pixel color
		for(Ray rays : camera)
		{
			//reset ray count and color
			color.setARGB(pixels[rays.getPixelX() + rays.getPixelY() * pixelBuffer.getWidth()]);
			
			//Iterate over the transforms, each operating on the output of the previous
			for(PixelTransform transform : transforms)
			{
				color = transform.transform(color);
			}
			
			pixels[rays.getPixelX() + rays.getPixelY() * pixelBuffer.getWidth()] = color.rgb32();
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