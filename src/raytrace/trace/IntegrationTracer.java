package raytrace.trace;

import java.util.LinkedList;

import math.Vector3;
import math.ray.Ray;
import process.logging.Logger;
import raster.Pixel;
import raster.PixelBuffer;
import raster.RenderBuffer;
import raytrace.camera.Camera;
import raytrace.color.Color;
import raytrace.data.IntegrationData;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.data.RenderData;
import raytrace.framework.Tracer;
import raytrace.material.ColorEmissionMaterial;
import raytrace.material.Material;
import raytrace.medium.Medium;
import raytrace.medium.VacuumMedium;
import raytrace.scene.Scene;
import raytrace.trace.integration.Integrator;

public class IntegrationTracer implements Tracer {
	
	/*
	 * 
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Integrator integrator;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public IntegrationTracer(Integrator integrator)
	{
		this.integrator = integrator;
	}
	

	/* *********************************************************************************************
	 * Tracer Overrides
	 * *********************************************************************************************/

	@Override
	public void trace(RenderData data)
	{
		PixelBuffer pixelBuffer = data.getPixelBuffer();
		//RenderBuffer inputRenderBuffer = data.getInputRenderBuffer();
		RenderBuffer outputRenderBuffer = data.getOutputRenderBuffer();
		Scene scene = data.getScene();
		Camera camera = data.getCamera();
		
		
		//Pixels
		int[] pb_Pixels = pixelBuffer.getPixels();
		Pixel[] rb_Pixels = outputRenderBuffer.getPixels();
		
		//Integration Data
		IntegrationData idata;
		
		//Intersection Data
		IntersectionData intersectionData = null;
		
		//Build a ray data object
		RayData rdata = new RayData();
		
		Material skyMaterial = scene.getSkyMaterial();
		if(skyMaterial == null)
			skyMaterial = new ColorEmissionMaterial(Color.white());
		
		//ray count for super sampling support
		double rayCount = 0;
		
		//Pixel data
		int pixelIndex;
		Pixel pixel;
		
		//Sample data
		Color color = new Color();
		Vector3 normal = new Vector3();
		Vector3 point = new Vector3();
		double depth = Double.POSITIVE_INFINITY;
		
		//TODO: Calculate the mediums that the camera is inside of
		LinkedList<Medium> mediums = determineMediumsEnclosingCamera(scene, camera);
		
		//For each ray, calculate the pixel color
		for(Ray rays : camera)
		{
			//reset ray count and color
			rayCount = 0.0;
			color.set(0.0, 0.0, 0.0);
			
			//For all rays in the ray
			for(Ray ray : rays)
			{
				//Increment ray count
				rayCount += 1.0;
				
				//Set the current ray
				ray.setRandomValue(Math.random());
				rdata.setRay(ray);
				
				//Get the ray-scene intersection data
				idata = integrator.integrate(scene, mediums, rdata, 0);
				
				//Add the resulting light to the accumulator
				color.add3M(idata.getColor());
				
				//If there was an intersection, store normal, point, and depth
				if((intersectionData = idata.getIntersectionData()) != null)
				{
					normal = intersectionData.getNormal();
					point = intersectionData.getPoint();
					depth = intersectionData.getDistance();
				}

				//Clear the data objects
				rdata.clear();
			}
			
			//If the camera is dirty stop rendering
			if(camera.isDirty()) {
				Logger.message(-1, "RayTracer: Detected a dirty camera, cancelling rendering.");
				break;
			}

			//Calculate the fina color
			color.multiply3M(1.0 / rayCount);

			//Update the render buffer pixel
			pixelIndex = rays.getPixelX() + rays.getPixelY() * pixelBuffer.getWidth();
			pixel = rb_Pixels[pixelIndex];
			pixel.getColor().set(color);
			pixel.getNormal().set(normal);
			pixel.getPoint().set(point);
			pixel.setDepth(depth);
			
			//Set the pixel buffer final color
			pb_Pixels[pixelIndex] = color.argb24();
		}
		
	}
	
	protected LinkedList<Medium> determineMediumsEnclosingCamera(Scene scene, Camera camera)
	{
		LinkedList<Medium> mediums = new LinkedList<Medium>();

		//Add the base air medium
		mediums.add(new VacuumMedium());
		
		if(scene.getGlobalMedium() != null)
			mediums.add(scene.getGlobalMedium());
		
		
		//TODO: Calculate them
		//Shoot a ray continually upwards
		//For all surface it exits without entering, add that surface's medium to the list (if any!)
		
		//Vector3 dir = camera.getUp();
		//RayData rdata = new RayData(new Ray(camera.getPosition(), dir, 0, 0));
		
		//IntersectionData idata = scene.intersects(rdata);
		
		//Repeat until intersection with sky
		
		
		return mediums;
	}

}
