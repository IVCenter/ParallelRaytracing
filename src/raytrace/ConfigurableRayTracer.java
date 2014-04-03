package raytrace;

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
	protected Scene scene;
	
	
	/* *********************************************************************************************
	 * Constructors
	 * *********************************************************************************************/
	public ConfigurableRayTracer()
	{
		
	}
	
	public ConfigurableRayTracer(Tracer tracer, Scene scene)
	{
		this.tracer = tracer;
		this.scene = scene;
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
		if(scene != null)
			scene.update(data);
	}

	@Override
	public void render(RenderData data)
	{
		if(tracer != null)
			tracer.trace(data.getPixelBuffer(), scene.getActiveCamera(), scene);
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

	public Scene getScene() {
		return scene;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

}
