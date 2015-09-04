package raytrace;

import process.logging.Logger;
import raytrace.data.BakeData;
import raytrace.data.RenderData;
import raytrace.data.UpdateData;
import raytrace.framework.Renderer;
import raytrace.framework.Tracer;
import system.Configuration;

public class ConfigurableRenderer implements Renderer {

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
	public ConfigurableRenderer()
	{
		
	}
	
	public ConfigurableRenderer(Tracer tracer)
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
		Logger.message(-1, "Updating...");
		if(data.getScene() != null) {
			data.getScene().update(data);
			data.getScene().bake(new BakeData());
		}
	}

	@Override
	public void render(RenderData data)
	{
		Logger.message(-1, "Rendering...");
		if(tracer != null && data.getScene() != null && data.getScene().getActiveCamera() != null)
		{
			//Update the active camera to use the render dimensions
			data.getScene().getActiveCamera().setPixelWidth(Configuration.getRenderWidth());
			data.getScene().getActiveCamera().setPixelHeight(Configuration.getRenderHeight());
			
			//Set the camera to clean
			data.getScene().getActiveCamera().setDirty(false);
			
			//And trace
			tracer.trace(data);
		}
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
