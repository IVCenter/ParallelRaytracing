package raytrace.data;

import raster.PixelBuffer;
import raster.RenderBuffer;
import raytrace.camera.Camera;
import raytrace.scene.Scene;

public class RenderData {
	
	/*
	 * A storage class for data used by render calls
	 */

	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected PixelBuffer pixelBuffer;
	protected RenderBuffer inputRenderBuffer;
	protected RenderBuffer outputRenderBuffer;
	protected Scene scene;
	protected Camera camera;
	

	/* *********************************************************************************************
	 * Constructors
	 * *********************************************************************************************/
	public RenderData()
	{
		//
	}
	

	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public PixelBuffer getPixelBuffer() {
		return pixelBuffer;
	}

	public void setPixelBuffer(PixelBuffer pixelBuffer) {
		this.pixelBuffer = pixelBuffer;
	}

	public RenderBuffer getInputRenderBuffer() {
		return inputRenderBuffer;
	}

	public void setInputRenderBuffer(RenderBuffer renderBuffer) {
		this.inputRenderBuffer = renderBuffer;
	}

	public RenderBuffer getOutputRenderBuffer() {
		return outputRenderBuffer;
	}

	public void setOutputRenderBuffer(RenderBuffer renderBuffer) {
		this.outputRenderBuffer = renderBuffer;
	}

	public Scene getScene() {
		return scene;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}
}
