package raytrace;

import process.logging.Logger;
import raytrace.data.BakeData;
import raytrace.data.RenderData;
import raytrace.data.UpdateData;
import raytrace.framework.Renderer;
import raytrace.framework.Tracer;
import raytrace.scene.Scene;

public class ConfigurableRayTracer implements Renderer {

	/*
	 * A configurable ray tracer that allows 
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Tracer tracer;
	
	
	/* *********************************************************************************************
	 * Constructors
	 * *********************************************************************************************/
	public ConfigurableRayTracer()
	{
		
	}
	
	public ConfigurableRayTracer(Tracer tracer)
	{
		this.tracer = tracer;
	}

	@Override
	public void initialize()
	{
		//TODO
	}
	
	
	/* *********************************************************************************************
	 * Render Methods
	 * *********************************************************************************************/
	@Override
	public void update(UpdateData data)
	{
		Logger.progress(-1, "Updating...");
		if(data.getScene() != null) {
			data.getScene().update(data);
			data.getScene().bake(new BakeData());//TODO: Do we need a bake method on the interface for Renderers?
		}
	}

	@Override
	public void render(RenderData data)
	{
		Logger.progress(-1, "Rendering...");
		if(tracer != null && data.getScene() != null)
			tracer.trace(data.getPixelBuffer(), data.getScene().getActiveCamera(), data.getScene());
	}

	
	/* *********************************************************************************************
	 * Getter/Setter Methods
	 * *********************************************************************************************/
	public Tracer getTracer() {
		return tracer;
	}

	public void setTracer(Tracer tracer) {
		this.tracer = tracer;
	}

}
