package raytrace.framework;

import raster.PixelBuffer;
import raytrace.camera.Camera;
import raytrace.surfaces.CompositeSurface;

public interface Tracer {

	/*
	 * An interface for ray tracers
	 */
	
	/**
	 * 
	 * @param pixelBuffer
	 * @param camera
	 * @param surface
	 */
	public void trace(PixelBuffer pixelBuffer, Camera camera, CompositeSurface surface);
	
}
