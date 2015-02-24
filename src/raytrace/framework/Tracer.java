package raytrace.framework;

import raytrace.data.RenderData;

public interface Tracer {

	/*
	 * An interface for ray tracers
	 */
	
	/**
	 * 
	 * @param pixelBuffer
	 * @param renderBuffer
	 * @param camera
	 * @param scene
	 */
	public void trace(RenderData data);
	//public void trace(PixelBuffer pixelBuffer, Camera camera, CompositeSurface surface);
	
}
