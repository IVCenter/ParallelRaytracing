package raytrace.material;

import math.Vector4;
import raytrace.color.Color;
import raytrace.data.IntersectionData;
import raytrace.data.ShadingData;
import raytrace.map.texture.Texture;

public class SkyGradientMaterial extends Material {
	
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
	public SkyGradientMaterial(Texture colorTexture)
	{
		this.colorTexture = colorTexture;
	}
	

	@Override
	public Color shade(ShadingData data)
	{
		IntersectionData idata = data.getIntersectionData();
		
		if(idata == null)
		{
			idata = new IntersectionData();
			idata.setLocalPoint(new Vector4(data.getRay().getDirection()));
			idata.setTexcoord(new Vector4(0, Math.sin(data.getRay().getDirection().dot3(positiveYAxis)), 0, 0));
		}
		
		return colorTexture.evaluate(idata);
	}

}