package raytrace.material;

import math.Vector4;
import raytrace.color.Color;
import raytrace.data.IlluminationData;
import raytrace.data.ShadingData;
import raytrace.light.Light;

public class FresnelDiffusePTMaterial extends Material{
	
	/*
	 * An implementation of a material that is a diffuse color and affected by light
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Color tint;
	protected double reflectiveRadius = 0.5;
	protected double schlickExponent = 5.0;
	protected int sampleCount = 1;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public FresnelDiffusePTMaterial(Color tint, double reflectiveRadius, double schlickExponent, int sampleCount)
	{
		this.tint = tint;
		this.reflectiveRadius = reflectiveRadius;
		this.schlickExponent = schlickExponent;
		this.sampleCount = sampleCount;
	}
	

	@Override
	public Color shade(ShadingData data)
	{
		Color shade = new Color(0x000000ff);
		
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
			Vector4 sampleDir;
			double reflectiveCoeff;
			for(int i = 0; i < sampleCount; ++i)
			{	
				sampleDir = cosineWeightedSample(uTangent, normal, vTangent);
				reflectiveCoeff = Math.pow((reflectiveRadius - DdotN) / (reflectiveRadius), schlickExponent);
				
				//if reflecting
				if(DdotN < reflectiveRadius && Math.random() < reflectiveCoeff)
				{
					rflectColor.add3M(reflect(data, point, normal, AIR_REFRACTIVE_INDEX));
				}else{
					rflectColor.add3M(recurse(data, point, sampleDir, 1.0).multiply3(tint));
				}
			}
			
			//Add the direct shading and samples shading together
			shade.add3M(rflectColor.multiply3(1.0/sampleCount));
		}
		
		return shade;
	}

}