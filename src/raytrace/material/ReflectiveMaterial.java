package raytrace.material;

import math.Vector3;
import raytrace.color.Color;
import raytrace.data.IlluminationData;
import raytrace.data.ShadingData;
import raytrace.light.Light;
import raytrace.map.texture.Texture;

public class ReflectiveMaterial  extends Material{
	
	/*
	 * An implementation of a material that is a diffuse color and affected by light
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Texture tintTexture;
	double reflectivePercent = 0.5;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public ReflectiveMaterial(Texture tintTexture, double reflectivePercent)
	{
		this.tintTexture = tintTexture;
		this.reflectivePercent = reflectivePercent;
	}
	

	@Override
	public Color shade(ShadingData data)
	{
		//Storage for the result color
		Color shade = new Color(0x000000ff);
		
		//Get the material color from the texture
		Color tint = tintTexture.evaluate(data.getIntersectionData());
		
		Vector3 point = data.getIntersectionData().getPoint();
		Vector3 normal = data.getIntersectionData().getNormal();
		IlluminationData ildata;
		
		//Diffuse lighting and shadows
		for(Light light : data.getRootScene().getLightManager())
		{
			//Get illumination data for the current light
			ildata = light.illuminate(data, point);
			
			shade.add3M(diffuse(ildata.getColor(), normal, ildata.getDirection()));
		}

		//If reflective, go divin'
		Color rflectColor = new Color();
		if(reflectivePercent != 0.0 && data.getRecursionDepth() < DO_NOT_EXCEED_RECURSION_LEVEL)
		{
			rflectColor = reflect(data, point, normal, data.getRefractiveIndex());
		}
		
		
		Color diffuseColor = tint.multiply3(shade).multiply3(1.0 - reflectivePercent);
		Color reflectiveColor = rflectColor.multiply3(reflectivePercent);
		return diffuseColor.add3(reflectiveColor);
	}

}