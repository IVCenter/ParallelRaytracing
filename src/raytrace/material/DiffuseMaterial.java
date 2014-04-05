package raytrace.material;

import math.Ray;
import math.Vector4;
import raytrace.color.Color;
import raytrace.data.IlluminationData;
import raytrace.data.IntersectionData;
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
		
		IntersectionData shadowData;
		
		for(Light light : data.getRootScene().getLightManager())
		{
			//Get illumination data for the current light
			ildata = light.illuminate(point);
			
			//Get the shadow data for the current light
			shadowData = shadowed(data.getRootScene(), 
					new Ray(point, ildata.getDirection().multiply3(-1.0), 0, 0), 
					ildata.getDistance());
			
			//If we are not in shadow, add to the shade
			if(shadowData == null)
				shade.add3M(diffuse(ildata.getColor(), normal, ildata.getDirection()));
		}
		
		return color.multiply3(shade);
	}

}
