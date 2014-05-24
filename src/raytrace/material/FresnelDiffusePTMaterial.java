package raytrace.material;

import math.Vector4;
import raytrace.color.Color;
import raytrace.data.IlluminationData;
import raytrace.data.ShadingData;
import raytrace.light.Light;
import raytrace.map.Texture;

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
		
		Vector4 point = data.getIntersectionData().getPoint();
		Vector4 normal = data.getIntersectionData().getNormal().normalize3();
		Vector4 rayDir = data.getRay().getDirection().multiply3(-1.0).normalize3();
		
		double DdotN = normal.dot3(rayDir);
		//If the ray direction and the normal have an angle of less than Pi/2, then the ray is exiting the material
		if(DdotN < 0.0) {
			normal = normal.multiply3(-1.0);
			DdotN = normal.dot3(rayDir);
		}

		
		//Basis
		Vector4 uTangent;
		Vector4 vTangent;
		
		if(normal.dot3(positiveYAxis) == 1.0)
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
			
			shade.add3M(diffuse(ildata.getColor(), normal, ildata.getDirection()).multiply3(tint));
		}
		
		
		//Sampling
		if(data.getRecursionDepth() < DO_NOT_EXCEED_RECURSION_LEVEL)
		{
			//Sample random points
			Color rflectColor = new Color();
			Vector4 sampleDir = cosineWeightedSample(uTangent, normal, vTangent);
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