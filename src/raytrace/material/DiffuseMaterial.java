package raytrace.material;

import math.Vector3;
import raytrace.color.Color;
import raytrace.data.IlluminationData;
import raytrace.data.ShadingData;
import raytrace.light.Light;
import raytrace.map.texture.Texture;

public class DiffuseMaterial extends Material{
	
	/*
	 * An implementation of a material that is a diffuse color and affected by light
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Texture tintTexture;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public DiffuseMaterial(Texture tintTexture)
	{
		this.tintTexture = tintTexture;
	}
	

	@Override
	public Color shade(ShadingData data)
	{
		//Storage for resulting color
		Color shade = new Color(0x000000ff);
		
		//Get color from texture
		Color tint = tintTexture.evaluate(data.getIntersectionData());
		
		Vector3 point = data.getIntersectionData().getPoint();
		Vector3 normal = data.getIntersectionData().getNormal();
		IlluminationData ildata;
		
		double DdotN = normal.dot(data.getRay().getDirection());
		//If the ray direction and the normal have an angle of less than Pi/2, then the ray is exiting the material
		if(DdotN > 0.0) {
			normal = normal.multiply(-1.0);
		}
		
		for(Light light : data.getRootScene().getLightManager())
		{
			//Get illumination data for the current light
			ildata = light.illuminate(data, point);
			
			//Add diffusely reflected light contribution to shade
			shade.add3M(diffuse(ildata.getColor(), normal, ildata.getDirection()));
		}
		
		return shade.multiply3M(tint);
	}

}
