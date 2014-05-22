package raytrace.material;

import math.Vector4;
import raytrace.color.Color;
import raytrace.data.IlluminationData;
import raytrace.data.ShadingData;
import raytrace.light.Light;

public class DiffuseMaterial extends Material{
	
	/*
	 * An implementation of a material that is a diffuse color and affected by light
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Color color;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public DiffuseMaterial(Color color)
	{
		this.color = color;
	}
	

	@Override
	public Color shade(ShadingData data)
	{
		Color shade = new Color(0x000000ff);
		
		Vector4 point = data.getIntersectionData().getPoint();
		Vector4 normal = data.getIntersectionData().getNormal();
		IlluminationData ildata;
		
		double DdotN = normal.dot3(data.getRay().getDirection());
		//If the ray direction and the normal have an angle of less than Pi/2, then the ray is exiting the material
		if(DdotN > 0.0) {
			normal = normal.multiply3(-1.0);
		}
		
		for(Light light : data.getRootScene().getLightManager())
		{
			//Get illumination data for the current light
			ildata = light.illuminate(data, point);
			
			//Add diffusely reflected light contribution to shade
			shade.add3M(diffuse(ildata.getColor(), normal, ildata.getDirection()));
		}
		
		return shade.multiply3M(color);
	}

}
