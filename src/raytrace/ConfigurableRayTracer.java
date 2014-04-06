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
		Logger.progress(-1, "Updating...");
		if(scene != null) {
			scene.update(data);
			scene.bake(new BakeData());//TODO: Do we need a bake method on the interface for Renderers?
		}
	}

	@Override
	public void render(RenderData data)
	{
		Logger.progress(-1, "Rendering...");
		if(tracer != null && scene != null)
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
