package raytrace.trace;

import process.logging.Logger;
import math.Vector3;
import math.ray.Ray;
import raster.Pixel;
import raster.PixelBuffer;
import raster.RenderBuffer;
import raytrace.camera.Camera;
import raytrace.color.Color;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.data.RenderData;
import raytrace.data.ShadingData;
import raytrace.framework.Tracer;
import raytrace.material.ColorMaterial;
import raytrace.material.Material;
import raytrace.scene.Scene;

public class RayTracer implements Tracer {

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
		
		//Trace each ray in the camera
		IntersectionData idata;
		//IntersectionData idata = new IntersectionData();//Allocate once
		
		//Build a ray data object
		RayData rdata = new RayData();
		
		//Build a shading data object
		ShadingData sdata = new ShadingData();
		sdata.setRootScene(scene);
		sdata.setRayData(rdata);
		
		Material skyMaterial = scene.getSkyMaterial();
		if(skyMaterial == null)
			skyMaterial = new ColorMaterial(Color.white());
		
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
				idata = scene.intersects(rdata);
				
				//If there was an intersection, do shading
				if(idata != null)
				{
					sdata.setIntersectionData(idata);
					color.add3M(idata.getMaterial().shade(sdata));
					normal = idata.getNormal();
					point = idata.getPoint();
					depth = idata.getDistance();
					
				//If there wasn't an intersection, use the sky material
				}else{
					
					//Make sure the intersection data is null!
					sdata.setIntersectionData(null);
					
					//Shade the pixel using the sky material
					color.add3M(skyMaterial.shade(sdata));
				}

				//Clear the data objects
				rdata.clear();
				sdata.clear();
			}
			
			//If the camera is dirty stop rendering
			if(camera.isDirty()) {
				Logger.progress(-1, "RayTracer: Detected a dirty camera, cancelling rendering.");
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

}
