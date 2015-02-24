package raster.pixel;

import raster.Pixel;
import raytrace.color.Color;

public class ColorInversionPT extends PixelTransform{
	
	/*
	 * Inverts the pixels color
	 */

	/* *********************************************************************************************
	 * Transform Override
	 * *********************************************************************************************/
	@Override
	public Color transform(Pixel pixel)
	{
		return pixel.getColor().multiply3M(-1.0).add3M(1.0);
	}

}
