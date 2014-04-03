package raytrace;

import process.logging.Logger;
import raster.PixelBuffer;
import raytrace.camera.Camera;
import raytrace.framework.Tracer;
import raytrace.surfaces.CompositeSurface;

public class ParallelRayTracer implements Tracer {
	
	/*
	 * A parallel ray tracers that spawns workers based on available resources
	 */
	/* *********************************************************************************************
	 * Local Constants
	 * *********************************************************************************************/
	protected static final String k = "";
	
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	//
	

	/* *********************************************************************************************
	 * Constructors
	 * *********************************************************************************************/
	public ParallelRayTracer()
	{
		//
	}
	

	/* *********************************************************************************************
	 * Trace Overrides
	 * *********************************************************************************************/
	@Override
	public void trace(PixelBuffer pixelBuffer, Camera camera, CompositeSurface surface)
	{
		Logger.progress(-1, "Tracing...");
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
