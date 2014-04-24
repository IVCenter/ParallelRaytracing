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
		//IntersectionData idata = new IntersectionData();//Allocate once
		
		//Build a ray data object
		RayData rdata = new RayData();
		
		//Build a shading data object
		ShadingData sdata = new ShadingData();
		sdata.setRootScene(scene);
		
		Material skyMaterial = scene.getSkyMaterial();
		if(skyMaterial == null)
			skyMaterial = new ColorMaterial(Color.white());
		
		//ray count and color storage for super sampling support
		double rayCount = 0;
		Color color = new Color();
		
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
				rdata.setRay(ray);
				sdata.setRay(ray);
				
				//Get the ray-scene intersection data
				idata = scene.intersects(rdata);
				
				//If there was an intersection, do shading
				if(idata != null)
				{
					sdata.setIntersectionData(idata);
					color.add3M(idata.getMaterial().shade(sdata));
					
				//If there wasn't an intersection, use the sky material
				}else{
					
					//Make sure the intersection data is null!
					sdata.setIntersectionData(null);
					
					//Shade the pixel using the sky material
					color.add3M(skyMaterial.shade(sdata));
				}
				
				//if((int)rayCount % 10 == 0)
				//	pixels[rays.getPixelX() + rays.getPixelY() * pixelBuffer.getWidth()] = color.multiply3(1.0/rayCount).rgb32();
			}
			
			//Set the final color
			color.multiply3M(1.0 / rayCount);
			pixels[rays.getPixelX() + rays.getPixelY() * pixelBuffer.getWidth()] = color.rgb32();
		}
	}

}
