package raytrace.material;

import raytrace.color.Color;
import raytrace.data.ShadingData;
import raytrace.map.Texture;

public class ColorMaterial extends Material {
	
	/*
	 * An implementation of a material that is a diffuse color
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Texture colorTexture;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public ColorMaterial(Texture colorTexture)
	{
		this.colorTexture = colorTexture;
	}
	

	@Override
	public Color shade(ShadingData data)
	{
		return colorTexture.evaluate(data.getIntersectionData());
	}

}
