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
	protected RenderBuffer renderBuffer;
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

	public RenderBuffer getRenderBuffer() {
		return renderBuffer;
	}

	public void setRenderBuffer(RenderBuffer renderBuffer) {
		this.renderBuffer = renderBuffer;
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
