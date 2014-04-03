package raytrace.framework;

import raytrace.data.RenderData;
import raytrace.data.UpdateData;

public interface Renderer {
	
	/*
	 * An API template for renderers
	 * 
	 * This interface and be implemented to allow any existing renderer to 
	 * be controlled by this framework
	 */
	
	/**
	 * 
	 */
	public void initialize();
	
	/**
	 * 
	 */
	public void update(UpdateData data);
	
	/**
	 * 
	 */
	public void render(RenderData data);

}
