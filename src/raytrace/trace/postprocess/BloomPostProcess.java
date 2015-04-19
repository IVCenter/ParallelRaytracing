package raytrace.trace.postprocess;

import math.ray.Ray;
import raster.Pixel;
import raster.PixelBuffer;
import raster.RenderBuffer;
import raytrace.camera.Camera;
import raytrace.color.Color;
import raytrace.data.RenderData;

public class BloomPostProcess extends PostProcess {
	
	/*
	 * A post process for the Bloom effect
	 */

	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected double radius = 20.0;
	

	/* *********************************************************************************************
	 * Constructors
	 * *********************************************************************************************/
	public BloomPostProcess()
	{
		super();

		//
	}
	

	/* *********************************************************************************************
	 * Trace
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
		int renderHeight = outputRenderBuffer.getHeight();
		
		//ray count and color storage for super sampling support
		Color color = new Color();
		Pixel iPixel = null, oPixel = null, centerPixel = null;
		int pixelIndex;
		int rayX, rayY;
		int minx, maxx, miny, maxy;
		int samplesUsed;
		
		//For each ray, calculate the pixel color
		for(Ray rays : camera)
		{
			//Clear the color
			color.set(0.0, 0.0, 0.0);
			
			//Get the pixel index
			rayX = rays.getPixelX();
			rayY = rays.getPixelY();
			pixelIndex = rayX + rayY * renderWidth;
			samplesUsed = 0;
			
			//Get the output pixel and center pixel
			oPixel = orb_Pixels[pixelIndex];
			centerPixel = irb_Pixels[pixelIndex];
			
			//Make the kernel bounding box
			minx = Math.max(rayX - (int)radius, 0);
			miny = Math.max(rayY - (int)radius, 0);
			maxx = Math.min(rayX + (int)radius, renderWidth);
			maxy = Math.min(rayY + (int)radius, renderHeight);
			
			//TODO: Update to use a proper airy disk
			
			//Loop through the surrounding pixels
			for(int x = minx; x < maxx; ++x)
			{
				for(int y = miny; y < maxy; ++y)
				{
					//If the curent pixel is outside of the kernel radius, skip
					if(Math.sqrt(Math.pow(x - rayX, 2.0) + Math.pow(y - rayY, 2.0)) > radius)
						continue;
					
					//Get the current pixel
					iPixel = irb_Pixels[x + y * renderWidth];
					
					//If the overall intensity of the current pixel is greater than the center pixel, add it
					if(iPixel.getColor().intensity3() > 1.0)
						color.add3M(iPixel.getColor().multiply3(Math.sqrt(Math.pow(x - rayX, 2.0) + Math.pow(y - rayY, 2.0))/radius));
					
					//Incremen the samplesUsed count
					++samplesUsed;
				}
			}
			
			//Attenuate the bloom color by the numver of samples tested
			color.multiply3M(1.0 / samplesUsed);
			color.add3M(centerPixel.getColor());
			
			//Update the pixel buffer and render buffer
			oPixel.getColor().set(color);
			pb_Pixels[pixelIndex] = color.rgb32();
		}
	}


	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	//


}
