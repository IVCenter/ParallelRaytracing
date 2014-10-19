package raster.pixel;

import raytrace.color.Color;

public class ColorInversionPT extends PixelTransform{
	
	/*
	 * Inverts the pixels color
	 */

	/* *********************************************************************************************
	 * Transform Override
	 * *********************************************************************************************/
	@Override
	public Color transform(Color pixel)
	{
		return pixel.multiply3M(-1.0).add3M(1.0);
	}

}
