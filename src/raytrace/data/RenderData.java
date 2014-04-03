package raytrace.data;

import raster.PixelBuffer;

public class RenderData {
	
	/*
	 * A storage class for data used by render calls
	 */

	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected PixelBuffer pixelBuffer;
	

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
}
