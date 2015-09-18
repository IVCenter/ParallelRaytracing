package raster.pixel;

import raster.Pixel;
import raytrace.color.Color;

public class ColorFogPT extends PixelTransform {
	
	/*
	 * Applies a color fog
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Color color;
	protected double saturationDistance;
	

	/* *********************************************************************************************
	 * Constructors
	 * *********************************************************************************************/
	public ColorFogPT()
	{
		this.color = Color.white();
		this.saturationDistance = Double.MAX_VALUE;
	}
	
	public ColorFogPT(Color color, double saturationDistance)
	{
		this.color = color;
		this.saturationDistance = saturationDistance;
	}
	

	/* *********************************************************************************************
	 * Transform Override
	 * *********************************************************************************************/
	@Override
	public Color transform(Pixel pixel)
	{
		double opacity = Math.max(0.0, Math.min(pixel.getDepth() / saturationDistance, 1.0));
		
		Color result = Color.interpolate(pixel.getColor(), color, opacity);
		
		return result;
	}

}