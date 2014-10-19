package raster.pixel;

import raytrace.color.Color;

public abstract class PixelTransform {
	
	/*
	 * A base class for Pixel Transforms (color only until render buffer is complete)
	 */

	/* *********************************************************************************************
	 * Abstract Methods
	 * *********************************************************************************************/
	public abstract Color transform(Color pixel);
}
