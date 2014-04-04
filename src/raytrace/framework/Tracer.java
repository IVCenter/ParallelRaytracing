package raytrace.framework;

import raster.PixelBuffer;
import raytrace.camera.Camera;
import raytrace.scene.Scene;

public interface Tracer {

	/*
	 * An interface for ray tracers
	 */
	
	/**
	 * 
	 * @param pixelBuffer
	 * @param camera
	 * @param scene
	 */
	public void trace(PixelBuffer pixelBuffer, Camera camera, Scene scene);
	//public void trace(PixelBuffer pixelBuffer, Camera camera, CompositeSurface surface);
	
}
