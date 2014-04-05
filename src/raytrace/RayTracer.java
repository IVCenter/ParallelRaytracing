package raytrace;

import math.Ray;
import raster.PixelBuffer;
import raytrace.camera.Camera;
import raytrace.color.Color;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.data.ShadingData;
import raytrace.framework.Tracer;
import raytrace.material.ColorMaterial;
import raytrace.material.Material;
import raytrace.scene.Scene;

public class RayTracer implements Tracer {

	@Override
	public void trace(PixelBuffer pixelBuffer, Camera camera, Scene scene)
	{
		//Pixels
		int[] pixels = pixelBuffer.getPixels();
		
		//Trace each ray in the camera
		IntersectionData idata;
		
		//Build a ray data object
		RayData rdata = new RayData();
		
		//Build a shading data object
		ShadingData sdata = new ShadingData();
		sdata.setRootScene(scene);
		
		Material skyMaterial = scene.getSkyMaterial();
		if(skyMaterial == null)
			skyMaterial = new ColorMaterial(Color.black());
		
		//For each ray, calculate the pixel color
		for(Ray ray : camera)
		{
			//Set the current ray
			rdata.setRay(ray);
			sdata.setRay(ray);
			
			//Get the ray-scene intersection data
			idata = scene.intersects(rdata);
			
			//If there was an intersection, do shading
			if(idata != null)
			{
				sdata.setIntersectionData(idata);
				pixels[ray.getPixelX() + ray.getPixelY() * pixelBuffer.getWidth()] = 
						idata.getMaterial().shade(sdata).rgb32();
				
			//If there wasn't an intersection, TODO do something more clever! (Sky material object? on scene?)
			}else{
				
				//Make sure the intersection data is null!
				sdata.setIntersectionData(null);
				
				//TODO: Replace this with a scene stored sky material
				pixels[ray.getPixelX() + ray.getPixelY() * pixelBuffer.getWidth()] = 
						skyMaterial.shade(sdata).rgb32();
			}
		}
	}

}
