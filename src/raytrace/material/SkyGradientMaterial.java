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
			idata.setLocalPoint(
					new Vector4(data.getRay().getDirection().multiply3M(1.0/data.getRay().getDirection().magnitude3()).add3(new Vector4(0, -0.5, 0, 0))));
			idata.setTexcoord(new Vector4(
					0, 
					0.5 * (Math.cos(data.getRay().getDirection().dot3(positiveYAxis) / data.getRay().getDirection().magnitude3()) + 1.0), 
					0, 
					0));
		}
		
		return colorTexture.evaluate(idata);
	}

}