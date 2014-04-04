package raytrace;

import math.Ray;
import raster.PixelBuffer;
import raytrace.camera.Camera;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.data.ShadingData;
import raytrace.framework.Tracer;
import raytrace.surfaces.CompositeSurface;

public class RayTracer implements Tracer {

	@Override
	public void trace(PixelBuffer pixelBuffer, Camera camera, CompositeSurface surface)
	{
		//Pixels
		int[] pixels = pixelBuffer.getPixels();
		
		//Trace each ray in the camera
		IntersectionData idata;
		
		RayData rdata = new RayData();
		
		ShadingData sdata = new ShadingData();
		sdata.setRootSurface(surface);
		
		for(Ray ray : camera)
		{
			rdata.setRay(ray);
			idata = surface.intersects(rdata);
			if(idata != null)
			{
				sdata.setIntersectionData(idata);
				pixels[ray.getPixelX() + ray.getPixelY() * pixelBuffer.getWidth()] = 
						idata.getSurface().getMaterial().shade(sdata).rgb32();
			}else{
				pixels[ray.getPixelX() + ray.getPixelY() * pixelBuffer.getWidth()] = 
						0xffff0068;
			}
		}
	}

}
