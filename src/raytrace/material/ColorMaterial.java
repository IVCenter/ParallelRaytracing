package raytrace.material;

import raytrace.color.Color;
import raytrace.data.ShadingData;

public class ColorMaterial extends Material {
	
	/*
	 * An implementation of a material that is a diffuse color
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Color color;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public ColorMaterial(Color color)
	{
		this.color = color;
	}
	

	@Override
	public Color shade(ShadingData data)
	{
		return color;
	}

}
