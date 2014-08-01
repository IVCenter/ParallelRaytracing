package raytrace.material;

import math.Vector3;
import raytrace.color.Color;
import raytrace.data.IlluminationData;
import raytrace.data.ShadingData;
import raytrace.light.Light;
import raytrace.map.texture.Texture;

public class FresnelDiffusePTMaterial extends Material{
	
	/*
	 * An implementation of a material that is a diffuse color and affected by light
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Texture tintTexture;
	protected double reflectiveRadius = 0.5;
	protected double schlickExponent = 5.0;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public FresnelDiffusePTMaterial(Texture tintTexture, double reflectiveRadius, double schlickExponent)
	{
		this.tintTexture = tintTexture;
		this.reflectiveRadius = reflectiveRadius;
		this.schlickExponent = schlickExponent;
	}
	

	@Override
	public Color shade(ShadingData data)
	{
		//Storage for result color
		Color shade = new Color(0x000000ff);
		
		//Get the material color from the texture
		Color tint = tintTexture.evaluate(data.getIntersectionData());
		
		Vector3 point = data.getIntersectionData().getPoint();
		Vector3 normal = data.getIntersectionData().getNormal().normalizeM();
		Vector3 rayDir = data.getRay().getDirection().multiply(-1.0).normalizeM();
		
		double DdotN = normal.dot(rayDir);
		//If the ray direction and the normal have an angle of less than Pi/2, then the ray is exiting the material
		if(DdotN < 0.0) {
			normal = normal.multiply(-1.0);
			DdotN = normal.dot(rayDir);
		}

		
		//Basis
		Vector3 uTangent;
		Vector3 vTangent;
		
		if(normal.dot(positiveYAxis) == 1.0)
			uTangent = normal.cross(cosineWeightedSample()).normalizeM();
		else
			uTangent = normal.cross(positiveYAxis).normalizeM();
		vTangent = uTangent.cross(normal).normalizeM();
		
		
		//Direct Illumination
		IlluminationData ildata;
		for(Light light : data.getRootScene().getLightManager())
		{
			//Get illumination data for the current light
			ildata = light.illuminate(data, point);
			
			shade.add3M(diffuse(ildata.getColor(), normal, ildata.getDirection()).multiply3(tint));
		}
		
		
		//Sampling
		if(data.getRecursionDepth() < DO_NOT_EXCEED_RECURSION_LEVEL)
		{
			//Sample random points
			Color rflectColor = new Color();
			Vector3 sampleDir = cosineWeightedSample(uTangent, normal, vTangent);
			double reflectiveCoeff = Math.pow((reflectiveRadius - DdotN) / (reflectiveRadius), schlickExponent);;
			
			//if reflecting
			//Add the direct shading and samples shading together
			if(DdotN < reflectiveRadius && Math.random() < reflectiveCoeff)
			{
				shade.add3M(rflectColor.add3M(reflect(data, point, normal, AIR_REFRACTIVE_INDEX)));
			}else{
				shade.add3M(rflectColor.add3M(recurse(data, point, sampleDir, 1.0).multiply3(tint)));
			}	
		}
		
		return shade;
	}

}