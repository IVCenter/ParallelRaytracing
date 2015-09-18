package raster.pixel;

import raster.Pixel;
import raytrace.color.Color;

public class Gamma22PT extends PixelTransform {
	
	/*
	 * Applies a gamma value of 2.2
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	//
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public Gamma22PT()
	{
		//
	}
	

	/* *********************************************************************************************
	 * Transform Override
	 * *********************************************************************************************/
	@Override
	public Color transform(Pixel pixel)
	{
		Color result = pixel.getColor().gammaEncode(2.2);
		
		return result;
	}
	
}