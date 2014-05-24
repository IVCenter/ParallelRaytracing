package raytrace.material;

import math.Vector4;
import raytrace.color.Color;
import raytrace.data.IlluminationData;
import raytrace.data.ShadingData;
import raytrace.light.Light;
import raytrace.map.texture.Texture;

public class DiffusePTMaterial extends Material{
	
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
	public DiffusePTMaterial(Texture tintTexture)
	{
		this.tintTexture = tintTexture;
	}
	

	@Override
	public Color shade(ShadingData data)
	{
		//Storage for result color
		Color shade = new Color(0x000000ff);
		
		//Get the material color from the texture
		Color tint = tintTexture.evaluate(data.getIntersectionData());
		
		Vector4 point = data.getIntersectionData().getPoint();
		Vector4 normal = data.getIntersectionData().getNormal().normalize3();
		
		double DdotN = normal.dot3(data.getRay().getDirection());
		//If the normal is facing in the wrong direction, flip it
		if(DdotN > 0.0) {
			normal = normal.multiply3(-1.0);
		}

		
		//Basis
		Vector4 uTangent;
		Vector4 vTangent;
		
		if(Math.abs(normal.dot3(positiveYAxis)) == 1.0)
			uTangent = normal.cross3(cosineWeightedSample()).normalize3();
		else
			uTangent = normal.cross3(positiveYAxis).normalize3();
		vTangent = uTangent.cross3(normal).normalize3();
		
		
		//Direct Illumination
		IlluminationData ildata;
		for(Light light : data.getRootScene().getLightManager())
		{
			//Get illumination data for the current light
			ildata = light.illuminate(data, point);
			
			shade.add3M(diffuse(ildata.getColor(), normal, ildata.getDirection()));
		}
		
		
		//Sampling
		if(data.getRecursionDepth() < DO_NOT_EXCEED_RECURSION_LEVEL)
		{
			//Sample a random point
			Vector4 sampleDir = cosineWeightedSample(uTangent, normal, vTangent);
			
			//Add the direct shading and samples shading together
			shade.add3M(recurse(data, point, sampleDir, 1.0));
		}
		
		return shade.multiply3M(tint);
	}

}