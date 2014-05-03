package raytrace.data;

import raster.PixelBuffer;
import raytrace.scene.Scene;

public class RenderData {
	
	/*
	 * A storage class for data used by render calls
	 */

	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected PixelBuffer pixelBuffer;
	protected Scene scene;
	

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

	public Scene getScene() {
		return scene;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}
}
